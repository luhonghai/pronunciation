//
//  IPAChartController.swift
//  AccentEasy
//
//  Created by Hai Lu on 16/03/2016.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//
import EZAudio
import SloppySwiper
import FBSDKCoreKit
import FBSDKLoginKit

class IPAChartController: BaseUIViewController , UICollectionViewDataSource, UICollectionViewDelegate, EZAudioPlayerDelegate, IPAPopupViewControllerDelegate, LessonTipPopupVCDelegate,
 UIGestureRecognizerDelegate {
    
    enum IPAChartMode {
        case HEAR_PHONEME
        case VIEW_MY_SCORE
    }
    
    enum IPAChartMenuMode {
        case BACK
        case MENU
        case LOGOUT
    }
    
    @IBOutlet weak var collectionIPA: UICollectionView!
    
    var dbAdapter: WordCollectionDbApdater!
    
    var ipaList: Array<IPAMapArpabet>!
    
    let reuseIdentifier = "ipaChartCell"
    
    var selectedType = IPAMapArpabet.CONSONANT
    
    var selectedMode = IPAChartMode.VIEW_MY_SCORE
    
    var player: EZAudioPlayer!
    
    var selectedIpa: IPAMapArpabet!
    
    var swiper: SloppySwiper!
    
    var popup:IPAInfoPopup!
    
    var ipaPopup: IPAPopupVC!
    
    var userProfile: UserProfile!
    
    var menuMode = IPAChartMenuMode.MENU
    
    var helpText = ""
    @IBOutlet weak var btnMenu: UIBarButtonItem!
    
    @IBOutlet weak var switchMode: UISwitch!
    @IBOutlet weak var lblTitle: UILabel!
    
    @IBOutlet weak var lblMode: UILabel!
    
    @IBAction func switchModeValueChanged(sender: UISwitch) {
        selectedMode = sender.on ? .VIEW_MY_SCORE : .HEAR_PHONEME
        updateLabelMode()
        collectionIPA.reloadData()
    }
    
    func clickBack() {
        Logger.log("click back")
        self.navigationController?.popViewControllerAnimated(true)
    }
    
    func doLogout() {
        Logger.log("do logout")
        if self.userProfile.loginType == UserProfile.TYPE_GOOGLE_PLUS {
            GIDSignIn.sharedInstance().signOut()
        } else if self.userProfile.loginType == UserProfile.TYPE_FACEBOOK {
            //Remove FB Data
            let fbManager = FBSDKLoginManager()
            fbManager.logOut()
            FBSDKAccessToken.setCurrentAccessToken(nil)
        } else {
        }
        
        Login.logout()
        
        dispatch_async(dispatch_get_main_queue(),{
            self.performSegueWithIdentifier("IPAGoToLogin", sender: self)
        })

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
            //clearTimer()
        }
        scheduleShowHelp()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
//        swiper = SloppySwiper(navigationController: self.navigationController)
//        self.navigationController?.delegate = swiper
//        self.navigationController?.interactivePopGestureRecognizer?.enabled = false
        switchMode.on = selectedMode != .HEAR_PHONEME
        updateLabelMode()
        dbAdapter = WordCollectionDbApdater()
        helpText = try! dbAdapter.checkIPAContainVideo() ? "Touch a phoneme to see your average score, listen to the sound and see a video." : "Touch a phoneme to see your average score, listen to the sound and view your score history on a chart."
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
        
//        let lpgr
//            = UILongPressGestureRecognizer(target: self, action: Selector("handleLongPress:"))
//        lpgr.delaysTouchesBegan = true
//        collectionIPA.addGestureRecognizer(lpgr)
        
        activateAudioSession()
        player = EZAudioPlayer(delegate: self)
        //
        
        if menuMode == .MENU {
            if self.revealViewController() != nil {
                btnMenu.target = self.revealViewController()
                btnMenu.action = #selector(SWRevealViewController.revealToggle(_:))
                self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
            }
            btnMenu.image = UIImage(named: "Menu-25.png")
        } else if menuMode == .BACK {
            btnMenu.image = UIImage(named: "Back-25.png")
            btnMenu.target = self
            btnMenu.action = #selector(IPAChartController.clickBack)
        } else {
            btnMenu.target = self
            btnMenu.image = ImageHelper.imageWithImage(image: UIImage(named: "p_logout_red.png")!, w: 40, h: 40)
            btnMenu.action = #selector(IPAChartController.doLogout)
        }
        
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
                    cell.lblIPA.backgroundColor = ColorHelper.APP_LIGHT_GRAY
                }
            } else {
                cell.lblIPA.backgroundColor = ColorHelper.APP_LIGHT_GRAY
            }
        }
        cell.layer.cornerRadius = 10
        return cell
    }
    
    // MARK: - UICollectionViewDelegate protocol
    
    func collectionView(collectionView: UICollectionView, didSelectItemAtIndexPath indexPath: NSIndexPath) {
        // handle tap events
        clearTimer()
        Logger.log("You selected cell #\(indexPath.item)!")
        let ipa = ipaList[indexPath.item]
        switch selectedMode {
        case .HEAR_PHONEME:
            playUrl(ipa.mp3URLShort)
            break
        case .VIEW_MY_SCORE:
            let popup = IPAPopupVC(nibName:"IPAPopupVC", bundle: nil)
            var ipaScore = -1
            if let score = userProfile.phonemeScores[ipa.arpabet] {
                ipaScore = score
            }
            self.selectedIpa = ipa
            popup.score = ipaScore
            popup.selectedIPA = ipa
            popup.delegate = self
            popup.isShow = true
            ipaPopup = popup
            self.presentpopupViewController(popup, animationType: .Fade, completion: { () -> Void in
                
            })
            break
        }
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

            pressShowTip(nil)
            weakSelf!.playUrl(weakSelf!.selectedIpa.mp3URL)
        }
    }
    
    func pressShowTip(sender: IPAPopupVC?) {
        popup = IPAInfoPopup(nibName:"IPAInfoPopup", bundle: nil)
        popup.selectedIpa = selectedIpa
        popup.delegate = self
        self.presentpopupViewController(popup, animationType: SLpopupViewAnimationType.Fade, completion: { () -> Void in
            
        })
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
        
    }
    
    func cleanupPopup() {
        weak var weakSelf = self
        if weakSelf != nil && popup != nil && popup.isShowing {
            weakSelf!.dismissPopupViewController(SLpopupViewAnimationType.Fade)
        }
        if weakSelf != nil && ipaPopup != nil && ipaPopup.isShow {
            weakSelf!.dismissPopupViewController(SLpopupViewAnimationType.Fade)
        }
        if weakSelf != nil && lessonTipPopupVC != nil && lessonTipPopupVC.isShow {
            weakSelf!.dismissPopupViewController(SLpopupViewAnimationType.Fade)
        }
    }
    
    override func viewWillDisappear(animated: Bool) {
        cleanupPopup()
        clearTimer()
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
    
    func pressClosePopup(sender: IPAPopupVC?) {
        weak var weakSelf = self
        if sender != nil {
            cleanupPopup()
        } else {
            if weakSelf != nil && ipaPopup != nil {
                ipaPopup.closeTipPopup()
            }
        }
    }
    
    var lessonTipPopupVC:LessonTipPopupVC!
    
    func showHelp() {
        showHelp(helpText)
    }
    
    func showHelp(message: String) {
        lessonTipPopupVC = LessonTipPopupVC(nibName: "LessonTipPopupVC", bundle: nil)
        lessonTipPopupVC.contentPopup = message
        lessonTipPopupVC.isShow = true
        lessonTipPopupVC.delegate = self
        self.presentpopupViewController(lessonTipPopupVC, animationType: .Fade, completion: {() -> Void in })
    }
    
    func closeLessonTipPopup(sender: LessonTipPopupVC) {
        self.dismissPopupViewController(.Fade)
    }
    
    var timer:NSTimer!
    
    func scheduleShowHelp() {
        clearTimer()
        timer = NSTimer.scheduledTimerWithTimeInterval(3, target: self, selector: #selector(IPAChartController.showHelp as (IPAChartController) -> () -> ()), userInfo: nil, repeats: false)
    }
    
    func clearTimer() {
        if timer != nil {
            timer.invalidate()
            timer = nil
        }
    }
    
}
