//
//  IPAChartController.swift
//  AccentEasy
//
//  Created by Hai Lu on 16/03/2016.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//
import EZAudio
import SloppySwiper

class IPAChartController: BaseUIViewController , UICollectionViewDataSource, UICollectionViewDelegate, EZAudioPlayerDelegate, IPAPopupViewControllerDelegate,
 UIGestureRecognizerDelegate {
    
    enum IPAChartMode {
        case HEAR_PHONEME
        case VIEW_MY_SCORE
    }
    
    @IBOutlet weak var collectionIPA: UICollectionView!
    
    var dbAdapter: WordCollectionDbApdater!
    
    var ipaList: Array<IPAMapArpabet>!
    
    let reuseIdentifier = "ipaChartCell"
    
    var selectedType = IPAMapArpabet.VOWEL
    
    var selectedMode = IPAChartMode.VIEW_MY_SCORE
    
    var player: EZAudioPlayer!
    
    var selectedIpa: IPAMapArpabet!
    
    var swiper: SloppySwiper!
    
    var popup:IPAInfoPopup!
    
    var userProfile: UserProfile!
    
    @IBOutlet weak var switchMode: UISwitch!
    @IBOutlet weak var lblTitle: UILabel!
    
    @IBOutlet weak var lblMode: UILabel!
    
    @IBAction func switchModeValueChanged(sender: UISwitch) {
        selectedMode = sender.on ? .VIEW_MY_SCORE : .HEAR_PHONEME
        updateLabelMode()
        collectionIPA.reloadData()
    }
    
    @IBAction func clickBack(sender: AnyObject) {
        self.navigationController?.popViewControllerAnimated(true)
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        activateAudioSession()
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        self.switchMode.layer.cornerRadius = self.switchMode.frame.height / 2
    }
    
    func updateLabelMode() {
        if selectedMode == .VIEW_MY_SCORE {
            lblMode.text = "view my scores"
        } else if selectedMode == .HEAR_PHONEME {
            lblMode.text = "hear phonemes"
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
//        swiper = SloppySwiper(navigationController: self.navigationController)
//        self.navigationController?.delegate = swiper
//        self.navigationController?.interactivePopGestureRecognizer?.enabled = false
        switchMode.on = selectedMode != .HEAR_PHONEME
        updateLabelMode()
        dbAdapter = WordCollectionDbApdater()
        do {
            ipaList = try dbAdapter.getIPAMapArpabetByType(selectedType)
        } catch {
            
        }
        userProfile = AccountManager.currentUser()
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
        activateAudioSession()
        player = EZAudioPlayer(delegate: self)
        //
        setNavigationBarTransparent()
    }
    
    func activateAudioSession() {
        let session: AVAudioSession = AVAudioSession.sharedInstance()
        do {
            try session.setCategory(AVAudioSessionCategoryPlayback)
            try session.setActive(true)
            try session.overrideOutputAudioPort(AVAudioSessionPortOverride.Speaker)
        } catch {
            
        }
    }
    
    func collectionView(collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return ipaList.count
    }
    

    func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell {
        
        let cell = collectionView.dequeueReusableCellWithReuseIdentifier(reuseIdentifier, forIndexPath: indexPath) as! IPAChartCell
        let ipa = ipaList[indexPath.item]
        cell.lblIPA.text = ipa.ipa
        if selectedMode == IPAChartMode.HEAR_PHONEME {
            if ipa.color != nil && !ipa.color.isEmpty {
                cell.lblIPA.backgroundColor = Multimedia.colorWithHexString(ipa.color)
            } else {
                cell.lblIPA.backgroundColor = ColorHelper.APP_DEFAULT
            }
        } else {
            if let score = userProfile.phonemeScores[ipa.arpabet] {
                if score >= 0 {
                    if score >= 80 {
                        cell.lblIPA.backgroundColor = ColorHelper.APP_GREEN
                    } else if score >= 45 {
                        cell.lblIPA.backgroundColor = ColorHelper.APP_ORANGE
                    } else {
                        cell.lblIPA.backgroundColor = ColorHelper.APP_RED
                    }
                } else {
                    cell.lblIPA.backgroundColor = ColorHelper.APP_GRAY
                }
            } else {
                cell.lblIPA.backgroundColor = ColorHelper.APP_GRAY
            }
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

            popup = IPAInfoPopup(nibName:"IPAInfoPopup", bundle: nil)
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
        HttpDownloader.loadFileAsync(NSURL(string: url!)!, completion: { (path, error) -> Void in
            weakSelf!.playSound(NSURL(fileURLWithPath: path))
        })
    }
    
    func playSound(fileUrl: NSURL) {
        Logger.log("run in play")
        activateAudioSession()
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
        weak var weakSelf = self
        if weakSelf != nil && popup != nil && popup.isShowing {
            weakSelf!.dismissPopupViewController(SLpopupViewAnimationType.Fade)
        }
    }
    
    override func viewWillDisappear(animated: Bool) {
        weak var weakSelf = self
        if weakSelf != nil && popup != nil && popup.isShowing {
            weakSelf!.dismissPopupViewController(SLpopupViewAnimationType.Fade)
        }
    }
    
    func setNavigationBarTransparent() {
        self.navigationController?.navigationBar.setBackgroundImage(UIImage(), forBarMetrics: UIBarMetrics.Default)
        self.navigationController?.navigationBar.shadowImage = UIImage()
        self.navigationController?.navigationBar.translucent = true
        self.navigationController?.view.backgroundColor = UIColor.clearColor()
    }
    
    func goToViewController(sender: IPAPopupVC?, vcId: String) {
        self.dismissPopupViewController(SLpopupViewAnimationType.Fade)
        let nextController = self.storyboard?.instantiateViewControllerWithIdentifier(vcId)
        self.navigationController?.pushViewController(nextController!, animated: false)
    }
}
