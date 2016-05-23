//
//  FSMainVC.swift
//  SwiftSidebarMenu
//
//  Created by CMGVN on 1/7/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit
import AVFoundation
import EZAudio
import Darwin
import SloppySwiper

class FSMainVC: BaseUIViewController, UITableViewDataSource, UITableViewDelegate, UISearchResultsUpdating, EZAudioPlayerDelegate, EZMicrophoneDelegate, EZRecorderDelegate, AnalyzingDelegate, UISearchBarDelegate, UISearchDisplayDelegate, HelpButtonDelegate {
    
    var userProfileSaveInApp:NSUserDefaults!
    
    let arrayRandomWord = ["hello", "goodbye", "welcome", "please", "no", "yes", "left", "right", "refrigerator", "eighth", "jewellery", "bath", "drawer", "hungry", "finance", "router", "university", "credit", "pronunciation", "business", "colonel", "penguin", "sixth", "anemone", "choir", "candidate", "cacophony", "demure", "barbiturate", "electoral", "necessarily", "rural", "squirrel", "future", "hydrogen"]
    
    let kCellIdentifier = "cellIdentifier"
    //var loginParameter:NSUserDefaults!
    var JSONStringUserProfile:String!
    var userProfile = UserProfile()
    var fileName:String = "tmp_record_file"
    var fileType:String = "wav"
    var scoreResult:Float = -1
    var linkFile:String!
    var selectedWord: WordCollection!
    var isRecording:Bool = false
    
    var microphone: EZMicrophone!
    var player: EZAudioPlayer!
    var recorder: EZRecorder!
    
    var currentMode: UserVoiceModel!
    
    @IBOutlet weak var menuButton: UIBarButtonItem!
    @IBOutlet weak var btnRecord: UIButton!
    @IBOutlet weak var btnPlay: UIButton!
    @IBOutlet weak var btnPlayDemo: UIButton!
    @IBOutlet weak var lblIPA: UILabel!
    @IBOutlet weak var tvDescription: UITextView!
    
    @IBOutlet weak var analyzingView: AnalyzingView!
    
    var freestyleDBAdapter:FreeStyleDBAdapter!
    
    
    var lessonDBAdapter: WordCollectionDbApdater!
    
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
    
    var isShowSlider = true
    
    @IBOutlet weak var sliderContainer: UIView!
    
    @IBOutlet weak var sliderContent: UIView!
    

    
    @IBOutlet weak var btnSlider: UIButton!
    @IBAction func sliderClick(sender: AnyObject) {
        toggleSlider()
    }
    
    var swiper: SloppySwiper!
    
    @IBOutlet weak var sliderConstraint: NSLayoutConstraint!
    
    func toggleSlider() {
        weak var weakSelf = self
        weakSelf!.sliderContainer.layoutIfNeeded()
        UIView.animateWithDuration(0.3) { () -> Void in
            if (weakSelf!.isShowSlider) {
                weakSelf!.lblIPA.hidden = false
                weakSelf!.sliderBackground.alpha = 0
                //                weakSelf!.sliderContainer.frame = CGRectMake(CGRectGetMinX(weakSelf!.sliderContainer.frame), CGRectGetHeight(weakSelf!.view.frame)
                //                    - CGRectGetHeight(weakSelf!.btnSlider.frame) + 3, CGRectGetWidth(weakSelf!.sliderContainer.frame), CGRectGetHeight(weakSelf!.sliderContainer.frame))
                weakSelf!.sliderConstraint.constant = CGRectGetHeight(weakSelf!.sliderContent.frame)
                
            } else {
                weakSelf!.sliderBackground.alpha = weakSelf!.maxAlpha
                weakSelf!.lblIPA.hidden = true
                //                weakSelf!.sliderContainer.frame = CGRectMake(CGRectGetMinX(weakSelf!.sliderContainer.frame), CGRectGetHeight(weakSelf!.view.frame)
                //                    - CGRectGetHeight(weakSelf!.sliderContainer.frame), CGRectGetWidth(weakSelf!.sliderContainer.frame), CGRectGetHeight(weakSelf!.sliderContainer.frame))
                weakSelf!.sliderConstraint.constant = 0
            }
            weakSelf!.sliderContainer.layoutIfNeeded()
            weakSelf!.isShowSlider = !weakSelf!.isShowSlider
            // weakSelf!.sliderContainer.translatesAutoresizingMaskIntoConstraints = true
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //check invitation and redirect to InvitaionMainVC
        if (GlobalData.getInstance().isShowInvitation) {
            //count invitation
            var countInvitation:Int=0
            for invitation in AccountManager.currentUser().invitationData {
                if invitation.status == InvitationStatus.pending {
                    countInvitation += 1
                }
            }
            if (countInvitation > 0){
                //move to invitation page
                let nextController = self.storyboard?.instantiateViewControllerWithIdentifier("InvitationMainVC") as! InvitationMainVC
                //self.navigationController?.popToRootViewControllerAnimated(false)
                self.navigationController?.pushViewController(nextController, animated: false)
                GlobalData.getInstance().isShowInvitation = false
                return
            }
        }
        
        GlobalData.getInstance().isOnLessonMain = false
        
//        swiper = SloppySwiper(navigationController: self.navigationController)
//        self.navigationController?.delegate = swiper
        DeviceManager.requestMicrophonePermission {
            self.microphone = EZMicrophone(delegate: self)
            
        }
        //self.edgesForExtendedLayout = UIRectEdge.None;
        self.player = EZAudioPlayer(delegate: self)
        self.analyzingView.delegate = self
        // Create an instance of the microphone and tell it to use this view controller instance as the delegate
    
        //
        // Setup notifications
        //
        self.setupNotifications()

        // Do any additional setup after loading the view.
        
        
        //menu
        if self.revealViewController() != nil {
            menuButton.target = self.revealViewController()
            menuButton.action = "revealToggle:"
            self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
        }
        
        //login parameter
        //loginParameter = NSUserDefaults()
        userProfileSaveInApp = NSUserDefaults()
        userProfile = AccountManager.currentUser()
        
        //load word default
        lessonDBAdapter = WordCollectionDbApdater()
        freestyleDBAdapter = FreeStyleDBAdapter()
        
        while selectedWord == nil {
            let randomIndex = Int(arc4random_uniform(UInt32(arrayRandomWord.count)))
            do {
                selectedWord = try lessonDBAdapter.findByWord(arrayRandomWord[randomIndex])
                GlobalData.getInstance().currentTipPhonemes = selectedWord.arpabet
                Logger.log("select random word \(selectedWord.word) index \(randomIndex)")
            } catch {
                
            }
        }
        GlobalData.getInstance().selectedWord = ""

        NSNotificationCenter.defaultCenter().postNotificationName("loadTabbar", object: "")
        btnRecord.hidden = true
        btnPlay.hidden = true
        setNavigationBarTransparent()
        
        if !GlobalData.getInstance().isShowHelpFreestyle && AccountManager.currentUser().helpStatus != UserProfile.HELP_NEVER {
            self.initHelpContext()
        }
    }
    
    @IBOutlet weak var helpContext: UIView!
    
    func initHelpContext() {
        let helpButton = HelpButtonController()
        helpButton.delegate = self
        helpButton.imgHelpContext.image = UIImage(named: "help-context-freestyle.png")
        helpButton.show(self.view.frame)
    }
    
    func onHelpButtonClose(neverShowAgain: Bool) {
        self.navigationController!.view.userInteractionEnabled = true
        //helpContext.hidden = true
        GlobalData.getInstance().isShowHelpFreestyle = true
        if neverShowAgain {
            let profile = AccountManager.currentUser()
            profile.helpStatus = UserProfile.HELP_NEVER
            AccountManager.updateProfile(profile)
        }
    }
    
    func onHelpButtonShow() {
        self.navigationController!.view.userInteractionEnabled = false
        //helpContext.hidden = false
    }

    
    func roundButton() {
        //button style cricle
        btnRecord.layer.cornerRadius = btnRecord.frame.size.width/2
        btnRecord.clipsToBounds = true
        btnPlay.layer.cornerRadius = btnPlay.frame.size.width/2
        btnPlay.clipsToBounds = true
        btnRecord.hidden = false
        btnPlay.hidden = false
    }
    
    
    func setupNotifications() {
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "playerDidChangePlayState:", name: EZAudioPlayerDidChangePlayStateNotification, object: self.player)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "playerDidReachEndOfFile:", name: EZAudioPlayerDidReachEndOfFileNotification, object: self.player)
        //NSNotificationCenter.defaultCenter().addObserver(self, selector: "handleBecomeActive:", name: UIApplicationDidBecomeActiveNotification, object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "loadWord:", name: "loadWord", object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "playFile:", name: "playFile", object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "showDetail:", name: "showDetail", object: nil)
    }
    
    func handleBecomeActive(notification: NSNotification)
    {
        Logger.log("Become active")
        activateAudioSession()
    }
    
    func activateAudioSession() {
        let session: AVAudioSession = AVAudioSession.sharedInstance()
        do {
            try session.setCategory(AVAudioSessionCategoryPlayAndRecord)
        } catch {
            
        }
        do {
            try session.setActive(true)
        } catch {
            
        }
        do {
            try session.overrideOutputAudioPort(AVAudioSessionPortOverride.Speaker)
        } catch {
            
        }
    }
    
    func showDetail(notification: NSNotification) {
        let uuid = notification.object as! String
        let modelJsonPath = FileHelper.getFilePath("audio/\(uuid).json")
        if FileHelper.isExists(modelJsonPath) {
            do {
                let model: UserVoiceModel = try JSONHelper.fromJson(FileHelper.readFile(modelJsonPath))
                openDetailView(model)
            } catch {
                
            }
        }
    }
    
    func playFile(notification: NSNotification) {
        let filePath = notification.object as? String
        if filePath != nil && !filePath!.isEmpty && FileHelper.isExists(filePath!) {
            playSound(NSURL(fileURLWithPath: filePath!))
        }
    }
    
    func loadWord(notification: NSNotification) {
        chooseWord(notification.object as! String)
    }
    
    func chooseWord(word: String) {
        do{
            self.selectWord(try lessonDBAdapter.findByWord(word))
        }catch{
            Logger.log("load word default error")
        }
    }
    
    //------------------------------------------------------------------------------
    // Notifications
    //------------------------------------------------------------------------------
    
    func playerDidChangePlayState(notification: NSNotification)
    {
        weak var weakSelf = self
        dispatch_async(dispatch_get_main_queue(), {
            let player = notification.object as! EZAudioPlayer
            let isPlaying = player.isPlaying
            if (isPlaying && weakSelf!.recorder != nil) {
                weakSelf!.recorder.delegate = nil
            }
        });
    }
    
    //------------------------------------------------------------------------------
    
    func playerDidReachEndOfFile(notification: NSNotification)
    {
        weak var weakSelf = self
        dispatch_async(dispatch_get_main_queue(), {
            weakSelf!.doFinishPlaying()
        });
    }
    
    func getTmpFilePath() -> NSURL
    {
        return NSURL(fileURLWithPath: FileHelper.getFilePath("\(fileName).\(fileType)"))
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        
        //loginParameter = NSUserDefaults()
        //let username:String = loginParameter.objectForKey("username") as! String
        //if username != "hoang" {
        //self.performSegueWithIdentifier("goto_login", sender: self)
        //}else{
        //lblUsername.text = username
        //lblUsername.text = ""
        //}
        analyzingView.refreshLayout()
        if currentMode == nil  {
            if selectedWord == nil {
                self.chooseWord("hello")
            } else {
                self.chooseWord(selectedWord.word)
            }
        }
        
        

        delay(0.5) {
            if self.isShowSlider {
                self.toggleSlider()
            }
        }
        //activateAudioSession()
        GlobalData.getInstance().selectedWord = ""
        NSNotificationCenter.defaultCenter().postNotificationName("loadHistory", object: "")
        
    }
    
    override func viewWillAppear(animated: Bool) {
        
    }
    
    override func viewDidLayoutSubviews() {
       roundButton()
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        //loginParameter.setObject(nil, forKey: "username")
    }
    
    //define for search bar
    var arrSearchResultData = [WordCollection]()
    //var filteredAppleProducts = [String]()
    
    var resultSearchController: AnyObject!
    
    let resultsController = UITableViewController(style: .Plain)
    
    func initSearchResultController() {
        arrSearchResultData = [WordCollection]()
        resultsController.tableView.dataSource = self
        resultsController.tableView.delegate = self
        resultsController.tableView.reloadData()
        resultsController.tableView.registerClass(UITableViewCell.self, forCellReuseIdentifier: kCellIdentifier)
    }
    
    @IBAction func barbuttonSearchClick(sender: AnyObject) {
        if #available(iOS 8.0, *) {
            initSearchViewController()
        } else {
            initSearchViewControllerOS7()
        }
    }
    
    func searchDisplayControllerWillBeginSearch(controller: UISearchDisplayController) {
        if #available(iOS 8.0, *) {
        } else {
            Logger.log("searchDisplayControllerWillBeginSearch")
        }
    }
    
    func searchDisplayControllerWillEndSearch(controller: UISearchDisplayController) {
        if #available(iOS 8.0, *) {

        } else {
            Logger.log("searchDisplayControllerWillEndSearch")
            setActiveSearchViewOS7(false)
        }
    }
    
    func searchBarCancelButtonClicked(searchBar: UISearchBar) {
        if #available(iOS 8.0, *) {
            
        } else {
            
        }
    }
    
    func initSearchViewControllerOS7() {
        initSearchResultController()
        let statusBarFrame = UIApplication.sharedApplication().statusBarFrame
        let searchBar = UISearchBar()
        searchBar.delegate = self
        searchBar.showsCancelButton = false
        searchBar.text = ""
        searchBar.becomeFirstResponder()
        resultsController.tableView.tableHeaderView = searchBar
        searchBar.sizeToFit()
        resultSearchController = UISearchDisplayController(searchBar: searchBar, contentsController: resultsController)
        (resultSearchController as! UISearchDisplayController).searchResultsDataSource = self
        (resultSearchController as! UISearchDisplayController).delegate = self
        (resultSearchController as! UISearchDisplayController).searchResultsDelegate = self
        (resultSearchController as! UISearchDisplayController).searchResultsTableView.registerClass(UITableViewCell.self, forCellReuseIdentifier: kCellIdentifier)
        let height = self.view.frame.height
        let width = self.view.frame.width
        let minY = CGRectGetMinY(self.view.frame)
      //  let origin = resultsController.view.frame
        resultsController.tableView.frame = CGRectMake(0, minY + statusBarFrame.size.height, width,height - statusBarFrame.size.height)
        resultsController.view.frame = self.view.frame
        self.presentpopupViewController(resultsController, animationType: SLpopupViewAnimationType.TopBottom, completion: { () -> Void in
            Logger.log("Show popup")
            
        })
    }
    
    func searchBar(searchBar: UISearchBar, textDidChange searchText: String) {
        updateSearchTableData(searchText)
    }
    
    @available(iOS 8.0, *)
    func initSearchViewController() {
        //
        initSearchResultController()
        //
        resultSearchController = UISearchController(searchResultsController: resultsController)
        (resultSearchController as! UISearchController).searchResultsUpdater = self
        (resultSearchController as! UISearchController).dimsBackgroundDuringPresentation = false
        //self.resultSearchController.searchBar.sizeToFit()
        
        self.presentViewController(resultSearchController as! UISearchController, animated: true, completion: nil)
        
    }
    
    @available(iOS 8.0, *)
    func updateSearchResultsForSearchController(searchController: UISearchController)
    {
        //searching word process
        updateSearchTableData(searchController.searchBar.text!)
    }
    
    func updateSearchTableData(searchText: String) {
        //get database
        let adapter = WordCollectionDbApdater()
        do {
            arrSearchResultData = try adapter.search(searchText)
        } catch (let e as NSError) {
            Logger.log(e)
        }
        //reload data for table view search
        Logger.log("Start reload search result")
        resultsController.tableView.reloadData();
        Logger.log("Complete reload search result")
    }
    

    
    func selectWord(wordCollection : WordCollection){
        currentMode = nil
        analyzingView.clear()
        analyzingView.switchType(AnalyzingType.SEARCHING)
        scoreResult = -1
        NSNotificationCenter.defaultCenter().postNotificationName("loadGraph", object: wordCollection.word)
        //set word select for detail screen
        let JSONWordSelected:String = Mapper().toJSONString(wordCollection, prettyPrint: true)!
        userProfileSaveInApp.setObject(JSONWordSelected, forKey: FSScreen.KeyWordSelectedInMainScreen)
        
        selectedWord = wordCollection
        btnPlayDemo.setTitle(wordCollection.word.lowercaseString, forState: UIControlState.Normal)
        lblIPA.text = wordCollection.pronunciation
        tvDescription.text = wordCollection.definition
        tvDescription.becomeFirstResponder()
        linkFile = wordCollection.mp3Path
        
        disableViewRecord()
        self.analyzingView.didCompleteLoadWord = false
        weak var weakSelf = self
        DeviceManager.doIfConnectedToNetwork({ (status) -> Void in
             if weakSelf != nil && weakSelf!.linkFile != nil && !weakSelf!.linkFile.isEmpty && status {
                //playSound(LinkFile)
                HttpDownloader.loadFileAsync(NSURL(string: weakSelf!.linkFile)!, completion: { (path, error) -> Void in
                    Logger.log("load complete " + weakSelf!.linkFile)
                    weakSelf!.analyzingView.isSearching = false
                    weakSelf!.analyzingView.didCompleteLoadWord = true
                })
             } else {
                weakSelf!.analyzingView.isSearching = false
                weakSelf!.analyzingView.didCompleteLoadWord = true
            }
        })
        //close searchControler
        NSNotificationCenter.defaultCenter().postNotificationName("loadTip", object: self.selectedWord.arpabet)
        if resultSearchController != nil {
            if #available(iOS 8.0, *) {
                setActiveSearchView(false)
            } else {
                setActiveSearchViewOS7(false)
            }
        }
    }
    
    func setActiveSearchViewOS7(active: Bool) {
        //self.dismissViewControllerAnimated(true, completion: {})
        self.dismissPopupViewController(SLpopupViewAnimationType.BottomTop)
        //(resultSearchController as! UISearchDisplayController).active = active
    }
    
    @available(iOS 8.0, *)
    func setActiveSearchView(active: Bool) {
        (resultSearchController as! UISearchController).active = active
    }
    
    
    //MARK- UITableViewDataSource
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let identifier = kCellIdentifier
        let cell = UITableViewCell(style: UITableViewCellStyle.Subtitle, reuseIdentifier: identifier)
        let word = arrSearchResultData[indexPath.row]
        cell.textLabel?.text = "\(word.word)"
        //cell.textLabel!.text = arrSearchResultData[indexPath.row].word
        cell.detailTextLabel?.text = "\(word.pronunciation)"
        cell.detailTextLabel?.textColor = ColorHelper.APP_GRAY
//        cell.detailTextLabel?.needsUpdateConstraints()
        return cell
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        Logger.log("Search result count \(self.arrSearchResultData.count)")
        return self.arrSearchResultData.count
    }
    
    
    // MARK- UITableViewDelegate
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        //load data for ViewControll when select word
        Logger.log("Row \(indexPath.row) selected")
        selectWord(arrSearchResultData[indexPath.row])
    }
    
    func showErrorAnalyzing() {
        isRecording = false
        btnRecord.setBackgroundImage(UIImage(named: "ic_record.png"), forState: UIControlState.Normal)
        ennableViewPlay()
        analyzingView.showScore(0)
        showColorOfScoreResult(0)
        Login.showError("could not calculate score")
    }

    func analyzeVoice() {
        //fix file test
        let databaseBundle = NSBundle.mainBundle()
        let dbZipPath = databaseBundle.pathForResource("fixed_6a11adce-13bb-479e-bcbc-13a7319677f9_raw", ofType: "wav")
        Logger.log(dbZipPath)
        
        
        weak var weakSelf = self
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)) {
            //btnRecord.backgroundColor = Multimedia.colorWithHexString("#7030a0")
            //btnRecord.setBackgroundImage(UIImage(named: "ic_close.png"), forState: UIControlState.Normal)
            //upload to server
            let client = Client()
                .baseUrl(FileHelper.getAccentEasyBaseUrl())
                .onError({e in
                    Logger.log(e)
                    dispatch_async(dispatch_get_main_queue(),{
                        weakSelf!.showErrorAnalyzing()
                    })
                });
            NSLog(weakSelf!.getTmpFilePath().path!)
            Logger.log("request token \(weakSelf!.userProfile.token)")
            client.post("/VoiceRecordHandler").field("country", "countryId").field("profile", Mapper().toJSONString(weakSelf!.userProfile, prettyPrint: true)!).field("word", weakSelf!.selectedWord.word).attach("imageKey", (GlobalData.IS_DEBUG ? "/Volumes/DATA/AccentEasy/pronunciation/ios-app/src/xcode/AccentEasy/fixed_6a11adce-13bb-479e-bcbc-13a7319677f9_raw.wav" : weakSelf!.getTmpFilePath().path!))
                .set("header", "headerValue")
                .timeout(5 * 60 * 1000)
                .end({(res:Response) -> Void in
                    Logger.log(res)
                    if(res.error) { // status of 2xx
                        //handleResponseJson(res.body)
                        //Logger.log(res.body)
                        Logger.log(res.text)
                        dispatch_async(dispatch_get_main_queue(),{
                            weakSelf!.showErrorAnalyzing()
                        })
                    }
                    else {
                        //handleErrorJson(res.body)
                        Logger.log("Analyze result \(res.text)")
                        let result = Mapper<VoidModelResult>().map(res.text)
                        let status:Bool = result!.status
                        let message:String = result!.message
                        let userVoiceModel = result!.data
                        //  print (userVoiceModel.word)
                        if status {
                            userVoiceModel.score = round(userVoiceModel.score)
                            self.userProfileSaveInApp.setObject(res.text , forKey: FSScreen.KeyVoidModelResult)
                            weakSelf!.currentMode = userVoiceModel
                            weakSelf!.saveDatabase(userVoiceModel)
                            FileHelper.getFilePath("audio", directory: true)
                            do {
                                try FileHelper.writeFile(FileHelper.getFilePath("audio/\(userVoiceModel.id).json"), content: JSONHelper.toJson(userVoiceModel))
                            } catch {
                                
                            }
                            FileHelper.copyFile(FileHelper.getFilePath("\(weakSelf!.fileName).\(weakSelf!.fileType)"), toPath: FileHelper.getFilePath("audio/\(userVoiceModel.id).wav"))
                            
                            //register suceess
                            dispatch_async(dispatch_get_main_queue(),{
                                NSNotificationCenter.defaultCenter().postNotificationName("loadGraph", object: userVoiceModel.word)
                                NSNotificationCenter.defaultCenter().postNotificationName("loadHistory", object: "")
                                weakSelf!.scoreResult = round(userVoiceModel.score)
                                weakSelf!.analyzingView.willDisplayScore = true
                            })
                        } else {
                            //SweetAlert().showAlert("Register Failed!", subTitle: "It's pretty, isn't it?", style: AlertStyle.Error)
                            dispatch_async(dispatch_get_main_queue(),{
                                weakSelf!.showErrorAnalyzing()
                            })
                        }
                        //Logger.log(result?.message)
                        //Logger.log(result?.status)
                    }
                })
        }
    }
    
    func openDetailView(model: UserVoiceModel) {
        DeviceManager.showLockScreen()
        let fsDetailVC = self.storyboard?.instantiateViewControllerWithIdentifier("FSDetailVC") as! FSDetailVC
        fsDetailVC.userVoiceModelResult = model
        self.navigationController?.pushViewController(fsDetailVC, animated: true)
    }
    
    private func saveDatabase(model: UserVoiceModel) {
        do {
            let time = NSDate().timeIntervalSince1970 * 1000.0
            let pScore = PronunciationScore()
            pScore.username = userProfile.username
            pScore.score = Int(round(model.score))
            pScore.word = model.word
            pScore.dataId = model.id
            pScore.time = time
            Logger.log("insert \(pScore.word) score \(pScore.score) uuid \(pScore.dataId)")
            try freestyleDBAdapter.insert(pScore)
            
            let result = model.result
            var lowScorePhoneme = ""
            var lowScore = 101
            if (result != nil) {
                let phoneScores = result.phonemeScores
                if  !phoneScores.isEmpty {
                    for phoneScore in phoneScores {
                        let ps = PhonemeScore.parseData(phoneScore)
                        ps.username = userProfile.username
                        ps.time = time
                        ps.dataId = model.id
                        Logger.log("insert \(ps.name) score \(ps.score)")
                        if ps.score < lowScore {
                            lowScorePhoneme = ps.name
                            lowScore = ps.score
                        }
                        try freestyleDBAdapter.insert(ps)
                    }
                }
            }
            dispatch_async(dispatch_get_main_queue(),{
                NSNotificationCenter.defaultCenter().postNotificationName("loadTip", object: lowScorePhoneme)
            })
        } catch {
            
        }
    }
    
    @IBAction func btnRecordTouchUp(sender: AnyObject) {
        disableViewRecord()
        //
        if(isRecording){
            Logger.log("not run record")
            self.microphone.stopFetchingAudio()
            if (self.recorder != nil) {
                self.recorder.closeAudioFile()
                
            }
            self.analyzingView.switchType(AnalyzingType.DEFAULT)
            self.btnRecord.setBackgroundImage(UIImage(named: "ic_record.png"), forState: UIControlState.Normal)
            self.changeColorLoadWord()
            isRecording = false
            return
        } else{
            DeviceManager.doIfConnectedToNetwork({ (status) -> Void in
                if status {
                self.currentMode = nil
                self.activateAudioSession()
                self.btnRecord.setBackgroundImage(UIImage(named: "ic_close.png"), forState: UIControlState.Normal)
                self.btnRecord.backgroundColor = Multimedia.colorWithHexString("#ff3333")
                self.btnRecord.enabled = true
                self.isRecording = true
                self.analyzingView.clear()
                weak var weakSelf = self
                self.analyzingView.switchType(AnalyzingType.RECORDING)
                dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)) {
                    weakSelf!.microphone.startFetchingAudio()
                    weakSelf!.recorder = EZRecorder(URL: weakSelf!.getTmpFilePath(), clientFormat: weakSelf!.microphone.audioStreamBasicDescription(), fileType: EZRecorderFileType.M4A, delegate: weakSelf!)
                }
                }
            })
        }
        
    }
    
    @IBAction func btnPlayTouchUp(sender: AnyObject) {
        disableViewPlay()
        if self.player.isPlaying {
            Logger.log("stop playing")
            self.ennableViewPlay()
            self.player.pause()
            self.btnPlay.setBackgroundImage(UIImage(named: "ic_play.png"), forState: UIControlState.Normal)
            self.btnPlay.backgroundColor = Multimedia.colorWithHexString("#579e11")
            self.btnRecord.enabled = true
            if (self.scoreResult >= 0.0) {
                showColorOfScoreResult(scoreResult)
                self.analyzingView.showScore(Int(round(self.scoreResult)), showAnimation: false)
            }
            
        } else {
            self.btnPlay.setBackgroundImage(UIImage(named: "ic_close.png"), forState: UIControlState.Normal)
            self.btnPlay.backgroundColor = Multimedia.colorWithHexString("#ff3333")
            if self.isRecording {
                self.microphone.stopFetchingAudio()
                self.isRecording = false;
            }
            self.analyzingView.switchType(AnalyzingType.DEFAULT)
            if (self.recorder != nil)
            {
                self.recorder.closeAudioFile()
            }
            playSound(self.getTmpFilePath())
        }
    }
    
    @IBAction func btnPlayDemoTouchUp(sender: AnyObject) {
        if linkFile != nil && !linkFile.isEmpty {
            DeviceManager.doIfConnectedToNetwork({ (status) -> Void in
                if status {
                Logger.log("link mp3: " + self.linkFile)
                //playSound(LinkFile)
                HttpDownloader.loadFileSync(NSURL(string: self.linkFile)!, completion: { (path, error) -> Void in
                    self.playSound(NSURL(fileURLWithPath: path))
                })
                }
            })
        }
    }
    
    func doFinishPlaying() {
        if (self.scoreResult >= 0.0) {
            self.btnPlay.setBackgroundImage(UIImage(named: "ic_play.png"), forState: UIControlState.Normal)
            self.btnPlay.backgroundColor = Multimedia.colorWithHexString("#579e11")
            showColorOfScoreResult(scoreResult)
            self.analyzingView.showScore(Int(round(self.scoreResult)), showAnimation: false)
            ennableViewPlay()
        }
        
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
    
    func showColorOfScoreResult(scoreResult: Float){
        if scoreResult < 45 {
            //color < 45 red
            changeColorRed()
        } else if scoreResult >= 45 && scoreResult < 80 {
            // 45 <= color < 80 orange
            changeColorOrange()
        } else {
            //color >= 80 green
            changeColorGreen()
        }
    }
    
    func changeColorLoadWord(){
        btnRecord.backgroundColor = ColorHelper.APP_GREEN
        btnPlay.backgroundColor = ColorHelper.APP_GRAY
        btnPlay.enabled = false
        btnRecord.enabled = true
        btnPlay.setBackgroundImage(UIImage(named: "ic_play.png"), forState: UIControlState.Disabled)
        btnPlayDemo.setTitleColor(ColorHelper.APP_GREEN, forState: UIControlState.Normal)
        lblIPA.textColor = ColorHelper.APP_GREEN
        tvDescription.textColor = ColorHelper.APP_GREEN
        tvDescription.becomeFirstResponder()
        btnPlayDemo.enabled = true
    }
    
    func changeColorRed(){
        
        btnRecord.backgroundColor = ColorHelper.APP_RED
        btnPlay.backgroundColor = ColorHelper.APP_RED
        btnPlayDemo.setTitleColor(ColorHelper.APP_RED, forState: UIControlState.Normal)
        lblIPA.textColor = ColorHelper.APP_RED
        tvDescription.textColor = ColorHelper.APP_RED
        tvDescription.becomeFirstResponder()
        btnRecord.enabled = true
        btnPlayDemo.enabled = true
    }
    
    func changeColorOrange(){
        btnRecord.backgroundColor = ColorHelper.APP_ORANGE
        btnPlay.backgroundColor = ColorHelper.APP_ORANGE
        btnPlayDemo.setTitleColor(ColorHelper.APP_ORANGE, forState: UIControlState.Normal)
        lblIPA.textColor = ColorHelper.APP_ORANGE
        tvDescription.textColor = ColorHelper.APP_ORANGE
        tvDescription.becomeFirstResponder()
        btnRecord.enabled = true
        btnPlayDemo.enabled = true
    }
    
    func changeColorGreen(){
        btnRecord.backgroundColor = ColorHelper.APP_GREEN
        btnPlay.backgroundColor = ColorHelper.APP_GREEN
        btnPlayDemo.setTitleColor(ColorHelper.APP_GREEN, forState: UIControlState.Normal)
        lblIPA.textColor = ColorHelper.APP_GREEN
        tvDescription.textColor = ColorHelper.APP_GREEN
        tvDescription.becomeFirstResponder()
        btnRecord.enabled = true
        btnPlayDemo.enabled = true
    }
    
    func disableViewPlay(){
        btnRecord.enabled = false
        btnRecord.backgroundColor = ColorHelper.APP_GRAY
        btnPlayDemo.enabled = false
        btnPlayDemo.setTitleColor(ColorHelper.APP_GRAY, forState: UIControlState.Normal)
        lblIPA.textColor = ColorHelper.APP_GRAY
        tvDescription.textColor = ColorHelper.APP_GRAY
        tvDescription.becomeFirstResponder()
    }
    
    func ennableViewPlay(){
        btnRecord.enabled = true
        btnPlayDemo.enabled = true
    }
    
    func disableViewRecord(){
        btnPlay.enabled = false
        btnPlay.backgroundColor = ColorHelper.APP_GRAY
        btnRecord.backgroundColor = ColorHelper.APP_GRAY
        btnPlayDemo.enabled = false
        btnRecord.enabled = false
        btnPlayDemo.setTitleColor(ColorHelper.APP_GRAY, forState: UIControlState.Normal)
        lblIPA.textColor = ColorHelper.APP_GRAY
        tvDescription.textColor = ColorHelper.APP_GRAY
        tvDescription.becomeFirstResponder()
    }
    
    func ennableViewRecord(){
        btnPlay.enabled = true
        btnPlayDemo.enabled = true
    }
    
    /*
    override func didReceiveMemoryWarning() {
    super.didReceiveMemoryWarning()
    // Dispose of any resources that can be recreated.
    }*/
    
    
    /*
    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
    // Get the new view controller using segue.destinationViewController.
    // Pass the selected object to the new view controller.
    }
    */
    
    
    /**
    Recorder delegate
    */
    func microphone(microphone: EZMicrophone!, changedPlayingState isPlaying: Bool) {
        // self.microphoneStateLabel.text = isPlaying ? "Microphone On" : "Microphone Off"
        // self.microphoneSwitch.on = isPlaying
    }
    
    func microphone(microphone: EZMicrophone!, hasAudioReceived buffer: UnsafeMutablePointer<UnsafeMutablePointer<Float>>, withBufferSize bufferSize: UInt32, withNumberOfChannels numberOfChannels: UInt32) {
        weak var weakSelf = self
        dispatch_async(dispatch_get_main_queue(), {
            // NSLog("hasAudioReceived. size %d", bufferSize)
            weakSelf!.analyzingView.updateBuffer(buffer[0], withBufferSize:bufferSize)
        })
    }
    
    func microphone(microphone: EZMicrophone!, hasBufferList bufferList: UnsafeMutablePointer<AudioBufferList>, withBufferSize bufferSize: UInt32, withNumberOfChannels numberOfChannels: UInt32) {
        if self.isRecording && self.recorder != nil {
            self.recorder.appendDataFromBufferList(bufferList, withBufferSize:bufferSize)
        }
    }
    
    func recorderDidClose(recorder: EZRecorder!) {
        recorder.delegate = nil
    }
    
    func recorderUpdatedCurrentTime(recorder: EZRecorder!) {
        weak var weakSelf = self
        let formattedCurrentTime: String = recorder.formattedCurrentTime
        dispatch_async(dispatch_get_main_queue(), {
            //weakSelf!.currentTimeLabel.text = formattedCurrentTime;
        });
    }
    
    func audioPlayer(audioPlayer: EZAudioPlayer!, playedAudio buffer: UnsafeMutablePointer<UnsafeMutablePointer<Float>>, withBufferSize bufferSize: UInt32, withNumberOfChannels numberOfChannels: UInt32, inAudioFile audioFile: EZAudioFile!) {
        weak var weakSelf = self
        dispatch_async(dispatch_get_main_queue(), {
            weakSelf!.analyzingView.updateBuffer(buffer[0],withBufferSize:bufferSize)
        });
    }
    
    func audioPlayer(audioPlayer: EZAudioPlayer!, updatedPosition framePosition: Int64, inAudioFile audioFile: EZAudioFile!) {
        weak var weakSelf = self
        dispatch_async(dispatch_get_main_queue(), {
            //weakSelf!.currentTimeLabel.text = audioPlayer.formattedCurrentTime
        });
    }
    /* AnalyzingDelegate*/
    func onStopRecording() {
        self.analyzingView.clear()
        self.microphone.stopFetchingAudio()
        if (self.recorder != nil) {
            self.recorder.closeAudioFile()
        }
        self.analyzingView.switchType(AnalyzingType.ANALYZING)
        analyzeVoice()
    }
    
    func onAnimationMin() {
        if self.analyzingView.didCompleteLoadWord {
            changeColorLoadWord()
            self.analyzingView.didCompleteLoadWord = false
        }
        if self.analyzingView.willDisplayScore {
            self.analyzingView.willDisplayScore = false
            self.analyzingView.didCompleteDisplayScore = true
            self.analyzingView.showScore(Int(round(self.scoreResult)))
        }
    }
    
    func onAnimationMax() {
        if self.analyzingView.didCompleteDisplayScore {
            self.showColorOfScoreResult(self.scoreResult)
            self.ennableViewRecord()
            self.btnRecord.setBackgroundImage(UIImage(named: "ic_record.png"), forState: UIControlState.Normal)
            self.isRecording = false
            self.analyzingView.didCompleteDisplayScore = false
            //move detail screen
            weak var weakSelf = self
            DeviceManager.showLockScreen()
            delay(0.8, closure: { 
                weakSelf!.openDetailView(weakSelf!.currentMode)
            })
        }
    }
    
    @IBAction func analyzingViewTapped(sender: AnyObject) {
        if currentMode != nil {
            openDetailView(currentMode)
        }
    }
    
    @IBAction func handleSlide(sender: UIPanGestureRecognizer) {
        if sender.state == UIGestureRecognizerState.Ended {
            if let view = sliderContainer {
                let currentY = view.center.y
                let contentHeight = CGRectGetHeight(sliderContent.frame)
                let halfHeight = CGRectGetHeight(sliderContainer.frame) / 2
                let maxY = CGRectGetHeight(self.view.frame)
                    - CGRectGetHeight(btnSlider.frame) + 3 + halfHeight
                let minY = CGRectGetHeight(self.view.frame)
                    - halfHeight
                isShowSlider = !(currentY >= (maxY - minY) / 2 + minY)
                weak var weakSelf = self
                weakSelf!.sliderContainer.layoutIfNeeded()
                UIView.animateWithDuration(0.3, animations: { () -> Void in
                    //                    view.center = CGPoint(x:view.center.x,
                    //                        y: (weakSelf!.isShowSlider ? minY : maxY))
                    weakSelf!.sliderConstraint.constant = weakSelf!.isShowSlider ? 0 : contentHeight
                    weakSelf!.lblIPA.hidden = weakSelf!.isShowSlider
                    weakSelf!.sliderBackground.alpha = weakSelf!.isShowSlider ? weakSelf!.maxAlpha : 0
                    weakSelf!.sliderContainer.layoutIfNeeded()
                })
            }
        } else {
            let translation = sender.translationInView(self.view)
            if let view = sliderContainer {
                let destY = view.center.y + translation.y
                let halfHeight = CGRectGetHeight(sliderContainer.frame) / 2
                let maxY = CGRectGetHeight(self.view.frame)
                    - CGRectGetHeight(btnSlider.frame) + 3 + halfHeight
                let minY = CGRectGetHeight(self.view.frame)
                    - halfHeight
                if (destY <= maxY && destY >= minY) {
                    //view.center = CGPoint(x:view.center.x, y:destY)
                    self.sliderBackground.alpha = self.maxAlpha
                        * ((destY - maxY) / (minY - maxY))
                    sliderConstraint.constant = sliderConstraint.constant + translation.y
                }
            }
            sender.setTranslation(CGPointZero, inView: self.view)
        }
    }
    
    @IBAction func tapSliderBackground(sender: AnyObject) {
        if self.sliderBackground.alpha >= self.maxAlpha - 0.1 {
            self.toggleSlider()
        }
    }
    
    @IBOutlet weak var sliderBackground: UIView!
    let maxAlpha:CGFloat = 0.7
    
    func setNavigationBarTransparent() {
        self.navigationController?.navigationBar.setBackgroundImage(UIImage(), forBarMetrics: UIBarMetrics.Default)
        self.navigationController?.navigationBar.shadowImage = UIImage()
        self.navigationController?.navigationBar.translucent = true
        self.navigationController?.view.backgroundColor = UIColor.clearColor()
    }
}
