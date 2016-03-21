//
//  IPAChartController.swift
//  AccentEasy
//
//  Created by Hai Lu on 16/03/2016.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//
import EZAudio
import SloppySwiper

class IPAChartController: UIViewController , UICollectionViewDataSource, UICollectionViewDelegate, EZAudioPlayerDelegate, IPAPopupViewControllerDelegate,
 UIGestureRecognizerDelegate {
    
    @IBOutlet weak var collectionIPA: UICollectionView!
    
    var dbAdapter: WordCollectionDbApdater!
    
    var ipaList: Array<IPAMapArpabet>!
    
    let reuseIdentifier = "ipaChartCell"
    
    var selectedType = IPAMapArpabet.VOWEL
    
    var player: EZAudioPlayer!
    
    var selectedIpa: IPAMapArpabet!
    
    var swiper: SloppySwiper!
    
    @IBOutlet weak var lblTitle: UILabel!
    
    @IBAction func clickBack(sender: AnyObject) {
        self.navigationController?.popViewControllerAnimated(true)
    }
    
    override func viewDidLoad() {
        swiper = SloppySwiper(navigationController: self.navigationController)
        self.navigationController?.delegate = swiper
        self.navigationController?.interactivePopGestureRecognizer?.enabled = false
        dbAdapter = WordCollectionDbApdater()
        do {
            ipaList = try dbAdapter.getIPAMapArpabetByType(selectedType)
        } catch {
            
        }
        if selectedType == IPAMapArpabet.VOWEL {
            lblTitle.text = "vowels"
        } else {
            lblTitle.text = "con|son¦ants"
        }
        Logger.log("Number of IPA found \(ipaList.count)")
        collectionIPA.delegate = self
        collectionIPA.dataSource = self
        
        let lpgr
            = UILongPressGestureRecognizer(target: self, action: Selector("handleLongPress:"))
        lpgr.delaysTouchesBegan = true
        //lpgr.minimumPressDuration = 2.0
        collectionIPA.addGestureRecognizer(lpgr)
        
        player = EZAudioPlayer(delegate: self)
    }
    
    func collectionView(collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return ipaList.count
    }
    

    func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell {
    
        let cell = collectionView.dequeueReusableCellWithReuseIdentifier(reuseIdentifier, forIndexPath: indexPath) as! IPAChartCell
        let ipa = ipaList[indexPath.item]
        cell.lblIPA.text = ipa.ipa
        if ipa.color != nil && !ipa.color.isEmpty {
            cell.lblIPA.backgroundColor = Multimedia.colorWithHexString(ipa.color)
        } else {
            cell.lblIPA.backgroundColor = ColorHelper.APP_DEFAULT
        }
        cell.layer.cornerRadius = 10
        return cell
    }
    
    // MARK: - UICollectionViewDelegate protocol
    
    func collectionView(collectionView: UICollectionView, didSelectItemAtIndexPath indexPath: NSIndexPath) {
        // handle tap events
        Logger.log("You selected cell #\(indexPath.item)!")
        let ipa = ipaList[indexPath.item]
        playUrl(ipa.mp3URLShort)
    }
    
    func collectionView(collectionView: UICollectionView,
        layout collectionViewLayout: UICollectionViewLayout,
        insetForSectionAtIndex section: Int) -> UIEdgeInsets {
            let flowLayout = (collectionViewLayout as! UICollectionViewFlowLayout)
            let cellSpacing = flowLayout.minimumInteritemSpacing
            let cellWidth = flowLayout.itemSize.width
            let cellCount = CGFloat(collectionView.numberOfItemsInSection(section))
            
            let collectionViewWidth = collectionView.bounds.size.width
            
            let totalCellWidth = cellCount * cellWidth
            let totalCellSpacing = cellSpacing * (cellCount - 1)
            
            let totalCellsWidth = totalCellWidth + totalCellSpacing
            
            let edgeInsets = (collectionViewWidth - totalCellsWidth) / 2.0
            
            return edgeInsets > 0 ? UIEdgeInsetsMake(0, edgeInsets, 0, edgeInsets) : UIEdgeInsetsMake(0, cellSpacing, 0, cellSpacing)
    }
    
    
    func handleLongPress(gestureRecognizer: UILongPressGestureRecognizer)
    {
        if (gestureRecognizer.state != UIGestureRecognizerState.Began) {
            return;
        }
        let p = gestureRecognizer.locationInView(self.collectionIPA)
         weak var weakSelf = self
        if let indexPath = self.collectionIPA.indexPathForItemAtPoint(p) {
            Logger.log("long press item \(indexPath.item)")
            selectedIpa = ipaList[indexPath.item]

            let popup:IPAInfoPopup = IPAInfoPopup(nibName:"IPAInfoPopup", bundle: nil)
            popup.selectedIpa = selectedIpa
            popup.delegate = self
            self.presentpopupViewController(popup, animationType: SLpopupViewAnimationType.Fade, completion: { () -> Void in
                
            })
            weakSelf!.playUrl(weakSelf!.selectedIpa.mp3URL)
        }
    }
    
    func playUrl(url : String?) {
        if url == nil || url!.isEmpty { return }
        weak var weakSelf = self
        HttpDownloader.loadFileSync(NSURL(string: url!)!, completion: { (path, error) -> Void in
            weakSelf!.playSound(NSURL(fileURLWithPath: path))
        })
    }
    
    func playSound(fileUrl: NSURL) {
        Logger.log("run in play")
        if self.player.isPlaying{
            self.player.pause()
        }
        self.player.volume = 1
        self.player.playAudioFile(EZAudioFile(URL: fileUrl))
    }
    
    func pressPlayExample(sender: IPAPopupVC?) {
        playUrl(selectedIpa.mp3URL)
    }
    
    func pressShowChart(sender: IPAPopupVC?) {
        self.dismissPopupViewController(SLpopupViewAnimationType.Fade)
    }
    
}
