//
//  LessonMainVC.swift
//  SwiftSidebarMenu
//
//  Created by CMGVN on 1/7/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit
import AVFoundation
import EZAudio
import Darwin

class QuestionCVCell: UICollectionViewCell {
    @IBOutlet weak var lblQuestion: UILabel!
    
}

class LessonMainVC: UIViewController, EZAudioPlayerDelegate, EZMicrophoneDelegate, EZRecorderDelegate, AnalyzingDelegate, UICollectionViewDataSource, UICollectionViewDelegate, QuestionCVDatasourceDelegate, LessonTipPopupVCDelegate, HelpButtonDelegate {
    
    var userProfileSaveInApp:NSUserDefaults!
    
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
    
    //@IBOutlet weak var menuButton: UIBarButtonItem!
    @IBOutlet weak var btnRecord: UIButton!
    @IBOutlet weak var btnPlay: UIButton!
    @IBOutlet weak var btnPlayDemo: UIButton!
    @IBOutlet weak var lblIPA: UILabel!
    @IBOutlet weak var tvDescription: UITextView!
    @IBOutlet weak var btnLessonTip: UIButton!
    @IBOutlet weak var botView: UIView!
    
    @IBOutlet weak var analyzingView: AnalyzingView!
    
    var freestyleDBAdapter:LessonDBAdapter!
    var wordCollectionDbApdater: WordCollectionDbApdater!
    
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
    
    var isShowSlider = true
    
    @IBOutlet weak var sliderContainer: UIView!
    
    @IBOutlet weak var sliderContent: UIView!
    
    
    @IBOutlet weak var arrowHistory: UIImageView!
    
    @IBOutlet weak var btnSlider: UIButton!
    @IBAction func sliderClick(sender: AnyObject) {
        toggleSlider()
    }
    @IBOutlet weak var sliderConstraint: NSLayoutConstraint!
    
    /*colection view setup
     *************************************************************/
    @IBOutlet weak var cvQuestionList: UICollectionView!
    var arrQuestionOfLC = [AEQuestion]()
    var lessionCollections = Array<AELessonCollection>()
    var indexLessonSelected:Int!
    var selectedLessonCollection = AELessonCollection()
    var selectedTest: AETest!
    var selectedCountry: AECountry!
    var selectedLevel: AELevel!
    var objectiveScore:ObjectiveScore!
    let reuseIdentifier = "cell"
    var indexCurrentQuestion:Int!
    var isLessonCollection:Bool = false
    var isShowDetail = true
    var currentSession: String!
    //var for test
    var testScore:TestScore!
    //next lesson
    var objectives = Array<AEObjective>()
    var indexObjectiveSelected:Int!
    
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
        }
        
    }
    
    
    
    override func viewDidLoad() {
        GlobalData.getInstance().isOnLessonMain = true
        super.viewDidLoad()
        DeviceManager.requestMicrophonePermission()
        //self.edgesForExtendedLayout = UIRectEdge.None;
        //
        // Setup the AVAudioSession. EZMicrophone will not work properly on iOS
        // if you don't do this!
        //
        self.analyzingView.delegate = self
        // Create an instance of the microphone and tell it to use this view controller instance as the delegate
        self.microphone = EZMicrophone(delegate: self)
        self.player = EZAudioPlayer(delegate: self)
        
        //
        // Setup notifications
        //
        self.setupNotifications()
        
        
        // Do any additional setup after loading the view.
        
        
        //menu
        /*if self.revealViewController() != nil {
            menuButton.target = self.revealViewController()
            menuButton.action = "revealToggle:"
            self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
        }*/
        
        //login parameter
        //loginParameter = NSUserDefaults()
        userProfileSaveInApp = NSUserDefaults()
        userProfile = AccountManager.currentUser()
        
        //load word default
        wordCollectionDbApdater = WordCollectionDbApdater()
        freestyleDBAdapter = LessonDBAdapter()
        
        //load question colection view
        questionCVInit()
        
        while selectedWord == nil {
            Logger.log("run in first word")
            let randomIndex = Int(arc4random_uniform(UInt32(arrQuestionOfLC[0].listWord.count)))
            do {
                selectedWord = arrQuestionOfLC[0].listWord[randomIndex]
                arrQuestionOfLC[0].selectedWord = selectedWord
                Logger.log("select random word \(selectedWord.word) index \(randomIndex)")
                indexCurrentQuestion = 0
            } catch {
                
            }
        }
        GlobalData.getInstance().selectedWord = ""
        btnRecord.hidden = true
        btnPlay.hidden = true
        setNavigationBarTransparent()
        
 
        
        //print("cellQuestionSelectedInDetail \(cellQuestionSelectedInDetail)")
        
        NSNotificationCenter.defaultCenter().postNotificationName("loadTabbar", object: "")
        
        //swap database
        freestyleDBAdapter.changeDbFile(DatabaseHelper.getLessonUserHistoryDatabaseFile()!)
        freestyleDBAdapter.prepare()
        
        currentSession = StringHelper.uuid()
        resetHistory()
    }
    
    
    func resetHistory() {
        try! freestyleDBAdapter.deleteAll(PronunciationScore())
        try! freestyleDBAdapter.deleteAll(PhonemeScore())
        NSNotificationCenter.defaultCenter().postNotificationName("loadHistory", object: "")
    }
    
    @IBOutlet weak var helpContext: UIView!
    
    func initHelpContext() {
        let helpButton = HelpButtonController()
        helpButton.imgHelpContext.image = UIImage(named: isLessonCollection ? "help-context-lesson.png" : "help-context-test.png")
        helpButton.delegate = self
        helpButton.show(self.view.frame)
    }
    
    func onHelpButtonClose(neverShowAgain: Bool) {
        weak var weakSelf = self
        if weakSelf != nil && weakSelf!.navigationController != nil {
            self.navigationController!.view.userInteractionEnabled = true
            //helpContext.hidden = true
        }
        GlobalData.getInstance().isShowHelpLesson = true
        if neverShowAgain {
            let profile = AccountManager.currentUser()
            profile.helpStatusLesson = UserProfile.HELP_NEVER
            AccountManager.updateProfile(profile)
        }
    }
    
    func onHelpButtonShow() {
        if self.navigationController != nil {
            self.navigationController!.view.userInteractionEnabled = false
            //helpContext.hidden = false
        }
    }
    
    func roundButton() {
        //button style cricle
        btnRecord.layer.cornerRadius = btnRecord.frame.size.width/2
        btnRecord.clipsToBounds = true
        btnPlay.layer.cornerRadius = btnPlay.frame.size.width/2
        btnPlay.clipsToBounds = true
        btnLessonTip.layer.cornerRadius = btnLessonTip.frame.size.width/2
        btnLessonTip.clipsToBounds = true
        btnRecord.hidden = false
        btnPlay.hidden = false
        if isLessonCollection{
            btnLessonTip.hidden = false
        } else {
            btnLessonTip.hidden = true
        }
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
            self.selectWord(try wordCollectionDbApdater.findByWord(word))
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
       // activateAudioSession()
        GlobalData.getInstance().selectedWord = ""
        NSNotificationCenter.defaultCenter().postNotificationName("loadHistory", object: "")
        if !GlobalData.getInstance().isShowHelpLesson && AccountManager.currentUser().helpStatusLesson != UserProfile.HELP_NEVER {
            self.initHelpContext()
        }
    }
    
    override func viewWillAppear(animated: Bool) {
        
    }
    
    override func viewDidLayoutSubviews() {
        roundButton()
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        //loginParameter.setObject(nil, forKey: "username")
    }

    
    func selectWord(wordCollection : WordCollection){
        currentMode = nil
        analyzingView.clear()
        analyzingView.switchType(AnalyzingType.SEARCHING)
        scoreResult = -1
        NSNotificationCenter.defaultCenter().postNotificationName("loadGraph", object: wordCollection.word)
        //set word select for detail screen
        //let JSONWordSelected:String = Mapper().toJSONString(wordCollection, prettyPrint: true)!
        //userProfileSaveInApp.setObject(JSONWordSelected, forKey: FSScreen.KeyWordSelectedInMainScreen)
        
        selectedWord = wordCollection
        btnPlayDemo.setTitle(wordCollection.word.lowercaseString, forState: UIControlState.Normal)
        lblIPA.text = wordCollection.pronunciation
        Logger.log("run in select word")
        //print(arrQuestionOfLC)
        //print(indexCurrentQuestion)
        //print(arrQuestionOfLC[indexCurrentQuestion].description)
        if isLessonCollection {
            if let des = selectedLessonCollection.name {
                tvDescription.text = des
            }
        
        } else if let des = arrQuestionOfLC[indexCurrentQuestion].description {
            tvDescription.text = des
        }
        
        linkFile = wordCollection.mp3Path
        //changeColorLoadWord()
        //close searchControler
        disableViewRecord()
        self.analyzingView.didCompleteLoadWord = false
        weak var weakSelf = self
        DeviceManager.doIfConnectedToNetwork({ () -> Void in
            if weakSelf != nil && weakSelf!.linkFile != nil && !weakSelf!.linkFile.isEmpty {
                Logger.log("link mp3: " + weakSelf!.linkFile)
                //playSound(LinkFile)
                HttpDownloader.loadFileAsync(NSURL(string: weakSelf!.linkFile)!, completion: { (path, error) -> Void in
                    Logger.log("load complete " + weakSelf!.linkFile)
                    weakSelf!.analyzingView.isSearching = false
                    weakSelf!.analyzingView.didCompleteLoadWord = true
                })
            }
        })
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
                    weakSelf!.showErrorAnalyzing()
                });
            NSLog(weakSelf!.getTmpFilePath().path!)
            let question = weakSelf!.arrQuestionOfLC[weakSelf!.indexCurrentQuestion]
            let itemId = weakSelf!.isLessonCollection ? weakSelf!.objectives[weakSelf!.indexObjectiveSelected].idString : weakSelf!.selectedTest.idString
            
            Logger.log("request token \(weakSelf!.userProfile.token)")
            client.post("/CalculationServlet")
                .field("idWord", weakSelf!.selectedWord.idString)
                .field("idQuestion", question.idString)
                .field("idCountry", weakSelf!.selectedCountry.idString)
                .field("session", weakSelf!.currentSession)
                .field("idLessonCollection", weakSelf!.selectedLessonCollection.idString)
                .field("type", weakSelf!.isLessonCollection ? "Q" : "T")
                .field("itemId", itemId)
                .field("levelId", weakSelf!.selectedLevel.idString)
                .field("profile", Mapper().toJSONString(weakSelf!.userProfile, prettyPrint: true)!)
                .field("word", weakSelf!.selectedWord.word)
                .attach("imageKey", (GlobalData.IS_DEBUG ? "/Volumes/DATA/AccentEasy/pronunciation/ios-app/src/xcode/AccentEasy/fixed_6a11adce-13bb-479e-bcbc-13a7319677f9_raw.wav" : weakSelf!.getTmpFilePath().path!))
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
                                NSNotificationCenter.defaultCenter().postNotificationName("loadTip", object: userVoiceModel.word)
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
        let lessonDetailVC = self.storyboard?.instantiateViewControllerWithIdentifier("LessonDetailVC") as! LessonDetailVC
        lessonDetailVC.userVoiceModelResult = model
        lessonDetailVC.arrQuestionOfLC = arrQuestionOfLC
        lessonDetailVC.questionCVDatasourceDelegate = self
        lessonDetailVC.lessonTitle = selectedLessonCollection.title
        lessonDetailVC.objectiveScore = objectiveScore
        lessonDetailVC.testScore = testScore
        lessonDetailVC.isLessonCollection = isLessonCollection
        self.navigationController?.pushViewController(lessonDetailVC, animated: true)
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
            if (result != nil) {
                let phoneScores = result.phonemeScores
                if  !phoneScores.isEmpty {
                    for phoneScore in phoneScores {
                        let ps = PhonemeScore.parseData(phoneScore)
                        ps.username = userProfile.username
                        ps.time = time
                        ps.dataId = model.id
                        Logger.log("insert \(ps.name) score \(ps.score)")
                        try freestyleDBAdapter.insert(ps)
                    }
                }
            }
        } catch {
            
        }
    }
    
    @IBAction func btnRecordTouchUp(sender: AnyObject) {
        //enable next page
        isShowDetail = true
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
            ennableViewRecord()
            return
        } else{
            DeviceManager.doIfConnectedToNetwork({ () -> Void in
                self.currentMode = nil
                self.activateAudioSession()
                self.btnRecord.setBackgroundImage(UIImage(named: "ic_close.png"), forState: UIControlState.Normal)
                self.btnRecord.backgroundColor = Multimedia.colorWithHexString("#ff3333")
                self.isRecording = true
                self.analyzingView.clear()
                self.microphone.startFetchingAudio()
                self.recorder = EZRecorder(URL: self.getTmpFilePath(), clientFormat: self.microphone.audioStreamBasicDescription(), fileType: EZRecorderFileType.WAV, delegate: self)
                self.analyzingView.switchType(AnalyzingType.RECORDING)
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
            DeviceManager.doIfConnectedToNetwork({ () -> Void in
                Logger.log("link mp3: " + self.linkFile)
                //playSound(LinkFile)
                HttpDownloader.loadFileSync(NSURL(string: self.linkFile)!, completion: { (path, error) -> Void in
                    self.playSound(NSURL(fileURLWithPath: path))
                })
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
        weak var weakSelf = self
        dispatch_async(dispatch_get_main_queue(),{
            if scoreResult < 45 {
                //color < 45 red
                weakSelf!.changeColorRed()
            } else if scoreResult >= 45 && scoreResult < 80 {
                // 45 <= color < 80 orange
                weakSelf!.changeColorOrange()
            } else {
                //color >= 80 green
                weakSelf!.changeColorGreen()
            }
        })
    }
    
    func changeColorLoadWord(){
        btnRecord.backgroundColor = ColorHelper.APP_GREEN
        btnPlay.backgroundColor = ColorHelper.APP_GRAY
        btnPlay.enabled = false
        btnPlay.setBackgroundImage(UIImage(named: "ic_play.png"), forState: UIControlState.Disabled)
        btnPlayDemo.setTitleColor(ColorHelper.APP_GREEN, forState: UIControlState.Normal)
        lblIPA.textColor = ColorHelper.APP_GREEN
        tvDescription.textColor = ColorHelper.APP_GREEN
    }
    
    func changeColorRed(){
        btnRecord.backgroundColor = ColorHelper.APP_RED
        btnPlay.backgroundColor = ColorHelper.APP_RED
        btnPlayDemo.setTitleColor(ColorHelper.APP_RED, forState: UIControlState.Normal)
        lblIPA.textColor = ColorHelper.APP_RED
        tvDescription.textColor = ColorHelper.APP_RED
    }
    
    func changeColorOrange(){
        btnRecord.backgroundColor = ColorHelper.APP_ORANGE
        btnPlay.backgroundColor = ColorHelper.APP_ORANGE
        btnPlayDemo.setTitleColor(ColorHelper.APP_ORANGE, forState: UIControlState.Normal)
        lblIPA.textColor = ColorHelper.APP_ORANGE
        tvDescription.textColor = ColorHelper.APP_ORANGE
    }
    
    func changeColorGreen(){
        btnRecord.backgroundColor = ColorHelper.APP_GREEN
        btnPlay.backgroundColor = ColorHelper.APP_GREEN
        btnPlayDemo.setTitleColor(ColorHelper.APP_GREEN, forState: UIControlState.Normal)
        lblIPA.textColor = ColorHelper.APP_GREEN
        tvDescription.textColor = ColorHelper.APP_GREEN
    }
    
    func disableViewPlay(){
        btnRecord.enabled = false
        btnRecord.backgroundColor = ColorHelper.APP_GRAY
        btnPlayDemo.enabled = false
        btnPlayDemo.setTitleColor(ColorHelper.APP_GRAY, forState: UIControlState.Normal)
        lblIPA.textColor = ColorHelper.APP_GRAY
        tvDescription.textColor = ColorHelper.APP_GRAY
        cvQuestionList.userInteractionEnabled = false
        btnLessonTip.enabled = false
    }
    
    func ennableViewPlay(){
        btnRecord.enabled = true
        btnPlayDemo.enabled = true
        cvQuestionList.userInteractionEnabled = true
        btnLessonTip.enabled = true
    }
    
    func disableViewRecord(){
        btnPlay.enabled = false
        btnPlay.backgroundColor = ColorHelper.APP_GRAY
        btnPlayDemo.enabled = false
        btnPlayDemo.setTitleColor(ColorHelper.APP_GRAY, forState: UIControlState.Normal)
        lblIPA.textColor = ColorHelper.APP_GRAY
        tvDescription.textColor = ColorHelper.APP_GRAY
        cvQuestionList.userInteractionEnabled = false
        btnLessonTip.enabled = false
    }
    
    func ennableViewRecord(){
        btnPlay.enabled = true
        btnPlayDemo.enabled = true
        cvQuestionList.userInteractionEnabled = true
        btnLessonTip.enabled = true
    }
    
    func disableViewSelectQuestion(){
        btnPlay.enabled = false
        btnPlay.backgroundColor = ColorHelper.APP_GRAY
        btnPlayDemo.enabled = false
        btnPlayDemo.setTitleColor(ColorHelper.APP_GRAY, forState: UIControlState.Normal)
        lblIPA.textColor = ColorHelper.APP_GRAY
        tvDescription.textColor = ColorHelper.APP_GRAY
        cvQuestionList.userInteractionEnabled = false
        btnLessonTip.enabled = false
        btnRecord.enabled = false
        btnRecord.backgroundColor = ColorHelper.APP_GRAY
    }
    
    func ennableViewSelectQuestion(){
        btnPlay.enabled = true
        btnPlayDemo.enabled = true
        cvQuestionList.userInteractionEnabled = true
        btnLessonTip.enabled = true
        btnRecord.enabled = true
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
    
    @IBAction func clickBack(sender: AnyObject) {
        self.navigationController?.popViewControllerAnimated(true)
    }
    
    
    
    /*colection view setup
     *************************************************************/
    func questionCVInit(){
        disableViewSelectQuestion()
        
        //if lesson of objective, using process next lesson of objective
        if isLessonCollection{
            selectedLessonCollection = lessionCollections[indexLessonSelected]
            
            objectiveScore.idLesson = selectedLessonCollection.idString
        }
        
        Logger.log(selectedLessonCollection.idString)
        
        arrQuestionOfLC = try! wordCollectionDbApdater.getQuestionByLessionCollection(selectedLessonCollection.idString)
        for index in 0...arrQuestionOfLC.count-1 {
            arrQuestionOfLC[index].listWord = try! wordCollectionDbApdater.getWordsOfQuestion(arrQuestionOfLC[index].idString)
        }
        arrQuestionOfLC[0].enabled = true
        Logger.log("arrQuestionOfLesson")
        Logger.log(arrQuestionOfLC)
        
        cvQuestionList.delegate = self
        cvQuestionList.dataSource = self

    }
    
    // tell the collection view how many cells to make
    func collectionView(collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.arrQuestionOfLC.count
    }
    
    // make a cell for each cell index path
    func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell {
        
        // get a reference to our storyboard cell
        let cell = collectionView.dequeueReusableCellWithReuseIdentifier(reuseIdentifier, forIndexPath: indexPath) as! QuestionCVCell
        cell.lblQuestion.layer.cornerRadius = cell.lblQuestion.frame.size.height/2
        cell.lblQuestion.clipsToBounds = true
        //cell.lblQuestion.backgroundColor = ColorHelper.APP_GRAY
        
        // Use the outlet in our custom class to get a reference to the UILabel in the cell
        /*print(arrQuestionOfLesson[indexPath.item].name)
        cell.lblQuestion.text = "Q\(indexPath.item+1)"
        if (indexPath.item == 0 )    {
            arrEnable.append(indexPath.item)
            cell.lblQuestion.backgroundColor = ColorHelper.APP_PURPLE
        }*/
        let question = arrQuestionOfLC[indexPath.item]
        
        if !question.recorded && question.enabled {
            if isRedoLesson {
                let averageScore:Int = Int(round(question.listScore.average))
                cell.lblQuestion.text = String(averageScore)
                //cell.lblQuestion.backgroundColor = ColorHelper.APP_PURPLE
            } else {
                if isLessonCollection {
                    cell.lblQuestion.text = "Q\(indexPath.item+1)"
                } else {
                    cell.lblQuestion.text = "T\(indexPath.item+1)"
                }
            }
            cell.lblQuestion.backgroundColor = ColorHelper.APP_PURPLE
        } else if question.recorded && question.enabled {
            let averageScore:Int = Int(round(question.listScore.average))
            cell.lblQuestion.text = String(averageScore)
            cell.lblQuestion.backgroundColor = questionCVChangeColor(averageScore)
        } else {
            if isRedoLesson {
                let averageScore:Int = Int(round(question.listScore.average))
                cell.lblQuestion.text = String(averageScore)
            } else {
                if isLessonCollection {
                    cell.lblQuestion.text = "Q\(indexPath.item+1)"
                } else {
                    cell.lblQuestion.text = "T\(indexPath.item+1)"
                }
            }
            cell.lblQuestion.backgroundColor = ColorHelper.APP_GRAY
        }
        
        //cell.userInteractionEnabled = false
        //cell.lblIPA.text = self.arrIPAMapArpabet[indexPath.item].ipa
        //cell.lblIPA.textColor = UIColor.whiteColor()
        //cell.lblQuestion.backgroundColor = Multimedia.colorWithHexString(getIPAColorByScore(userVoiceModelResult.result.phonemeScores[indexPath.item].totalScore))
        //cell.layer.cornerRadius = 5
        //cell.backgroundColor = Multimedia.colorWithHexString("#579e11") // make cell more visible in our example project

        
        return cell
    }
    
    func questionCVChangeColor(score:Int) -> UIColor{
        if score < 45 {
            //color < 45 red
            return ColorHelper.APP_RED
        } else if score >= 45 && score < 80 {
            // 45 <= color < 80 orange
            return ColorHelper.APP_ORANGE
        } else {
            //color >= 80 green
            return ColorHelper.APP_GREEN
        }

    }
    
    // MARK: - UICollectionViewDelegate protocol
    func collectionView(collectionView: UICollectionView, didSelectItemAtIndexPath indexPath: NSIndexPath) {
        // handle tap events
        Logger.log("You selected cell #\(indexPath.item)!")
        
        let question = arrQuestionOfLC[indexPath.item]
        if question.enabled {
            if !isLessonCollection{
                //test lesson colection, one recoder per test
                if indexPath.item <= indexCurrentQuestion && question.recorded{
                    return
                }
            }
            indexCurrentQuestion = indexPath.item
            Logger.log(indexCurrentQuestion)
            disableViewSelectQuestion()
            randomWord(question)
        }
    }
    
    func randomWord(question:AEQuestion){
        isShowDetail = false
        var randomIndex = Int(arc4random_uniform(UInt32(question.listWord.count)))
        while question.listWord[randomIndex].word == selectedWord.word {
            randomIndex = Int(arc4random_uniform(UInt32(question.listWord.count)))
        }
        selectedWord = question.listWord[randomIndex]
        //question.selectedWord = selectedWord
        self.chooseWord(selectedWord.word)
        Logger.log("radom word of question \(selectedWord.word) index \(randomIndex)")
        
        //ennableViewRecord()
    }
    
    //process when click question cell on detail screen
    func selectedCellQuestionInDetail(cellIndex: Int) {
        Logger.log("Detail cell selected is \(cellIndex)")
        
        //test lesson colection, one recoder per test
        if !isLessonCollection {
            if cellIndex <= indexCurrentQuestion {
                return
            }
        }
        
        //disableViewSelectQuestion()
        
        indexCurrentQuestion = cellIndex
        if let word = arrQuestionOfLC[cellIndex].selectedWord {
            currentMode = arrQuestionOfLC[cellIndex].currentMode
            selectedWord = word
            //self.chooseWord(selectedWord.word)
            //var score =  arrQuestionOfLesson[cellIndex].listScore[arrQuestionOfLesson[cellIndex].listScore.count-1]
            

            NSNotificationCenter.defaultCenter().postNotificationName("loadGraph", object: currentMode.word)
            NSNotificationCenter.defaultCenter().postNotificationName("loadHistory", object: "")
            NSNotificationCenter.defaultCenter().postNotificationName("loadTip", object: currentMode.word)
            
            scoreResult = round(currentMode.score)
            showColorOfScoreResult(currentMode.score)
            analyzingView.showScore(Int(round(currentMode.score)))
            //ennableViewRecord()
            btnRecord.setBackgroundImage(UIImage(named: "ic_record.png"), forState: UIControlState.Normal)
            //self.btnRecord.backgroundColor = Multimedia.colorWithHexString("#579e11")
            isRecording = false

            //selectedWord = wordCollection
            btnPlayDemo.setTitle(selectedWord.word.lowercaseString, forState: UIControlState.Normal)
            lblIPA.text = selectedWord.pronunciation
            tvDescription.text = arrQuestionOfLC[cellIndex].description
            linkFile = selectedWord.mp3Path
            
            
            //Logger.log(score)
            //analyzingView.showScore(Int(score))
        } else{
            randomWord(arrQuestionOfLC[cellIndex])
        }
    }
    
    //process when click redo on detail screen
    var isRedoLesson:Bool = false
    func redoLesson() {
        isRedoLesson = true
        for question in arrQuestionOfLC {
            question.recorded = false
            question.enabled = false
        }
        arrQuestionOfLC[0].enabled = true
        indexCurrentQuestion = 0
        
        let question = arrQuestionOfLC[0]
        if question.enabled {
            randomWord(question)
        }
        
        cvQuestionList.reloadData()
    }
    
    //process when click next lesson on detail screen
    func nextLesson() {
        if indexLessonSelected < lessionCollections.count - 1 {
            //next lesson
            Logger.log("next lesson")
            indexLessonSelected = indexLessonSelected + 1
            questionCVInit()
            indexCurrentQuestion = 0
            
            let question = arrQuestionOfLC[0]
            if question.enabled {
                randomWord(question)
            }
            currentSession = StringHelper.uuid()
            resetHistory()
            cvQuestionList.reloadData()
        } else {
            //next objective
            if indexObjectiveSelected < objectives.count - 1 {
                //show lesson of nextObjective
                Logger.log("show lesson of next objective")
                //send notification
                NSNotificationCenter.defaultCenter().postNotificationName("nextLesson", object: nil)
                //pop view
                let viewControllers: [UIViewController] = self.navigationController!.viewControllers as [UIViewController];
                self.navigationController!.popToViewController(viewControllers[viewControllers.count - 3], animated: false);
                
                //show popup
            
            }else {
                //obj end of arrObjective
                Logger.log("show objective")
                //send notification
                NSNotificationCenter.defaultCenter().postNotificationName("showPopupObj", object: nil)
                //pop view
                let viewControllers: [UIViewController] = self.navigationController!.viewControllers as [UIViewController];
                self.navigationController!.popToViewController(viewControllers[viewControllers.count - 4], animated: false);
            }
        }
    }
    
    @IBAction func btnLessonTipTouchUp(sender: AnyObject) {
        let lessonTipPopupVC:LessonTipPopupVC = LessonTipPopupVC(nibName: "LessonTipPopupVC", bundle: nil)
        if let des = selectedLessonCollection.description {
            lessonTipPopupVC.contentPopup = des
        }
        lessonTipPopupVC.delegate = self
        self.presentpopupViewController(lessonTipPopupVC, animationType: .Fade, completion: {() -> Void in })
    }
    
    func closeLessonTipPopup(sender: LessonTipPopupVC) {
        self.dismissPopupViewController(.Fade)
    }
    
    
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
            ennableViewSelectQuestion()
            self.analyzingView.didCompleteLoadWord = false
        }
        if self.analyzingView.willDisplayScore {
            self.analyzingView.willDisplayScore = false
            self.analyzingView.didCompleteDisplayScore = true
            self.scoreResult = round(currentMode.score)
            self.analyzingView.showScore(Int(round(self.scoreResult)))
        }
    }
    
    func onAnimationMax() {
        if self.analyzingView.didCompleteDisplayScore {
            self.showColorOfScoreResult(self.scoreResult)
            self.ennableViewRecord()
            self.btnRecord.setBackgroundImage(UIImage(named: "ic_record.png"), forState: UIControlState.Normal)
            self.isRecording = false
            self.arrQuestionOfLC[self.indexCurrentQuestion].currentMode = self.currentMode
            self.arrQuestionOfLC[self.indexCurrentQuestion].recorded = true
            self.arrQuestionOfLC[self.indexCurrentQuestion].listScore.append(round(self.scoreResult))
            
            if self.indexCurrentQuestion+1 < self.arrQuestionOfLC.count {
                self.arrQuestionOfLC[self.indexCurrentQuestion+1].enabled = true
            }
            self.arrQuestionOfLC[self.indexCurrentQuestion].selectedWord = self.selectedWord
            self.cvQuestionList.reloadData()
            weak var weakSelf = self
            //move detail screen
            delay (1) {
                if self.isShowDetail {
                    weakSelf!.openDetailView(weakSelf!.currentMode)
                }
            }
            self.analyzingView.didCompleteDisplayScore = false
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
