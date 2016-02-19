//
//  OurViewController.swift
//  SwiftSidebarMenu
//
//  Created by CMGVN on 1/7/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import UIKit
import ObjectMapper
import AVFoundation
import SwiftClient
import EZAudio
import ObjectMapper
import Darwin

class OurViewController: UIViewController, UITableViewDataSource, UITableViewDelegate, UISearchResultsUpdating, EZAudioPlayerDelegate, EZMicrophoneDelegate, EZRecorderDelegate, AnalyzingDelegate {

    //var loginParameter:NSUserDefaults!
    var userProfileSaveInApp:NSUserDefaults!
    var JSONStringUserProfile:String!
    var userProfile = UserProfile()
    var fileName:String = "tmp_record_file"
    var fileType:String = "wav"
    var scoreResult:Float = -1
    var LinkFile:String!
    var selectedWord: WordCollection!
    var isRecording:Bool = false
    
    var microphone: EZMicrophone!
    var player: EZAudioPlayer!
    var recorder: EZRecorder!
    
    @IBOutlet weak var menuButton: UIBarButtonItem!
    @IBOutlet weak var btnRecord: UIButton!
    @IBOutlet weak var btnPlay: UIButton!
    @IBOutlet weak var btnPlayDemo: UIButton!
    @IBOutlet weak var lblIPA: UILabel!
    @IBOutlet weak var tvDescription: UITextView!
    
    @IBOutlet weak var analyzingView: AnalyzingView!
    
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        //
        // Setup the AVAudioSession. EZMicrophone will not work properly on iOS
        // if you don't do this!
        //
        let session: AVAudioSession = AVAudioSession.sharedInstance()
        do {
            try session.setCategory(AVAudioSessionCategoryPlayAndRecord)
        } catch {
            
        }
        do {
            try session.setActive(true)
        } catch {
            
        }
        self.analyzingView.delegate = self
        // Create an instance of the microphone and tell it to use this view controller instance as the delegate
        self.microphone = EZMicrophone(delegate: self)
        self.player = EZAudioPlayer(delegate: self)
        
        //
        // Setup notifications
        //
        self.setupNotifications()
        //
        // Override the output to the speaker. Do this after creating the EZAudioPlayer
        // to make sure the EZAudioDevice does not reset this.
        //
        do {
            try session.overrideOutputAudioPort(AVAudioSessionPortOverride.Speaker)
        } catch {
            
        }
        
        // Do any additional setup after loading the view.
        
        //button style cricle
        btnRecord.layer.cornerRadius = btnRecord.frame.size.width/2
        btnRecord.clipsToBounds = true
        btnPlay.layer.cornerRadius = btnPlay.frame.size.width/2
        btnPlay.clipsToBounds = true
        
        
        //menu
        if self.revealViewController() != nil {
            menuButton.target = self.revealViewController()
            menuButton.action = "revealToggle:"
            self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
        }
        
        //login parameter
        //loginParameter = NSUserDefaults()
        
        userProfileSaveInApp = NSUserDefaults()
        if Login.IS_DEBUG {
            userProfile = Login.getTestUserProfile()
        } else {
            let keyForUserProfile:String = userProfileSaveInApp.objectForKey(Login.KeyUserProfile) as! String
            JSONStringUserProfile = userProfileSaveInApp.objectForKey(keyForUserProfile) as! String
            print("login successfull")
            print(JSONStringUserProfile)
            userProfile = Mapper<UserProfile>().map(JSONStringUserProfile)!
            //lblUsername.text = userProfile.username
        }
        
        //load word default
        let dbPath = DatabaseHelper.getLessonDatabaseFile()
        let adapter = WordCollectionDbApdater(dbFile: dbPath!)
        do{
            selectWord(try adapter.search("hello")[0])
        }catch{
            print("load word default error")
        }
    }
    
    func setupNotifications() {
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "playerDidChangePlayState:", name: EZAudioPlayerDidChangePlayStateNotification, object: self.player)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "playerDidReachEndOfFile:", name: EZAudioPlayerDidReachEndOfFileNotification, object: self.player)
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
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        //loginParameter.setObject(nil, forKey: "username")
    }
    
    //define for search bar
    var arrSearchResultData = [WordCollection]()
    //var appleProducts = [String]()
    var filteredAppleProducts = [String]()
    var resultSearchController = UISearchController()
    let resultsController = UITableViewController(style: .Plain)
    var searchSelectRow:Int!
    
    @IBAction func barbuttonSearchClick(sender: AnyObject) {
        //
        resultsController.tableView.dataSource = self
        resultsController.tableView.delegate = self
        //
        resultSearchController = UISearchController(searchResultsController: resultsController)
        self.resultSearchController.searchResultsUpdater = self
        self.resultSearchController.dimsBackgroundDuringPresentation = false
        //self.resultSearchController.searchBar.sizeToFit()
        
        self.presentViewController(resultSearchController, animated: true, completion: nil)
    }
    
    
    //MARK- UITableViewDataSource
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let identifier = "cellIdentifier"
        var cell: UITableViewCell! = tableView.dequeueReusableCellWithIdentifier(identifier)
        if cell == nil{
            cell = UITableViewCell(style: .Default, reuseIdentifier: identifier)
        }
        cell?.textLabel?.text = arrSearchResultData[indexPath.row].word
        return cell
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        print (self.arrSearchResultData.count)
        return self.arrSearchResultData.count
    }
    
    
    // MARK- UITableViewDelegate
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        //load data for ViewControll when select word
        searchSelectRow = indexPath.row
        print("Row \(searchSelectRow) selected")
        selectWord(arrSearchResultData[searchSelectRow])
    }
    
    func selectWord(wordCollection : WordCollection){
        selectedWord = wordCollection
        btnPlayDemo.setTitle(wordCollection.word.lowercaseString, forState: UIControlState.Normal)
        lblIPA.text = wordCollection.pronunciation
        tvDescription.text = wordCollection.definition
        LinkFile = wordCollection.mp3Path
        changeColorLoadWord()
        //close searchControler
        resultSearchController.active = false
    }
    
    func updateSearchResultsForSearchController(searchController: UISearchController)
    {
        //searching word process
        print (searchController.searchBar.text!)
        //get database
        let dbPath = DatabaseHelper.getLessonDatabaseFile()
        let adapter = WordCollectionDbApdater(dbFile: dbPath!)
        do {
            //appleProducts.removeAll(keepCapacity: false)
            arrSearchResultData = try adapter.search(searchController.searchBar.text!)
            //let resultCount:Int = arrSearchResultData.count - 1
            //if resultCount >= 0 {
                //for index in 0...resultCount{
                    //print(arrSearchResultData[index].word)
                    //print(arrSearchResultData[index].mp3Path)
                    //appleProducts.append(arrSearchResultData[index].word)
                //}
            //}
        
        } catch (let e as NSError) {
            print(e)
        }
        
        //print(appleProducts)
        print(arrSearchResultData)
        //reload data for table view search
        resultsController.tableView.reloadData();
    }

    func analyzeVoice() {
        weak var weakSelf = self
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)) {
            //btnRecord.backgroundColor = Multimedia.colorWithHexString("#7030a0")
            //btnRecord.setBackgroundImage(UIImage(named: "ic_close.png"), forState: UIControlState.Normal)
            
            //upload to server
            let client = Client()
                .baseUrl(FileHelper.getAccentEasyBaseUrl())
                .onError({e in print(e)});
            NSLog(weakSelf!.getTmpFilePath().path!)
            client.post("/VoiceRecordHandler").field("country", "countryId").field("profile", Mapper().toJSONString(weakSelf!.userProfile, prettyPrint: true)!).field("word", weakSelf!.selectedWord.word).attach("imageKey", weakSelf!.getTmpFilePath().path!)
                .set("header", "headerValue")
                .timeout(5 * 60 * 1000)
                .end({(res:Response) -> Void in
                    print(res)
                    if(res.error) { // status of 2xx
                        //handleResponseJson(res.body)
                        //print(res.body)
                        print(res.text)
                    }
                    else {
                        //handleErrorJson(res.body)
                        print(res.text)
                        let result = Mapper<VoidModelResult>().map(res.text)
                        let status:Bool = result!.status
                        let message:String = result!.message
                        let userVoiceModel = result!.data
                      //  print (userVoiceModel.word)
                        if status {
                            //register suceess
                            dispatch_async(dispatch_get_main_queue(),{
                                //SweetAlert().showAlert("Register Success!", subTitle: "", style: AlertStyle.Success)
                                //[unowned self] in NSThread.isMainThread()
                                //self.performSegueWithIdentifier("AELoginGoToMain", sender: self)
                                weakSelf!.scoreResult = floor(userVoiceModel.score)
                                weakSelf!.showColorOfScoreResult(weakSelf!.scoreResult)
                                weakSelf!.analyzingView.showScore(Int(weakSelf!.scoreResult))
                                weakSelf!.ennableViewRecord()
                                weakSelf!.btnRecord.setBackgroundImage(UIImage(named: "ic_record.png"), forState: UIControlState.Normal)
                                //self.btnRecord.backgroundColor = Multimedia.colorWithHexString("#579e11")
                                weakSelf!.isRecording = false
                            })
                            
                            
                        } else {
                            //SweetAlert().showAlert("Register Failed!", subTitle: "It's pretty, isn't it?", style: AlertStyle.Error)
                            dispatch_async(dispatch_get_main_queue(),{
                                SweetAlert().showAlert("Submit to server error!", subTitle: message, style: AlertStyle.Error)
                                
                            })
                        }
                        //print(result?.message)
                        //print(result?.status)
                    }
                })
        }
    }
    
    @IBAction func btnRecordTouchUp(sender: AnyObject) {
        disableViewRecord()
        //
        if(isRecording){
            print("not run record")
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
            self.btnRecord.setBackgroundImage(UIImage(named: "ic_close.png"), forState: UIControlState.Normal)
            self.btnRecord.backgroundColor = Multimedia.colorWithHexString("#ff3333")
            isRecording = true
            self.analyzingView.clear()
            self.microphone.startFetchingAudio()
            self.recorder = EZRecorder(URL: self.getTmpFilePath(), clientFormat: self.microphone.audioStreamBasicDescription(), fileType: EZRecorderFileType.WAV, delegate: self)
            self.analyzingView.switchType(AnalyzingType.RECORDING)
        }

    }
    
    @IBAction func btnPlayTouchUp(sender: AnyObject) {
        disableViewPlay()
        if self.player.isPlaying {
            print("stop playing")
            self.player.pause()
            self.btnPlay.setBackgroundImage(UIImage(named: "ic_play.png"), forState: UIControlState.Normal)
            self.btnPlay.backgroundColor = Multimedia.colorWithHexString("#579e11")
            self.btnRecord.enabled = true
            if (self.scoreResult >= 0.0) {
                showColorOfScoreResult(scoreResult)
                self.analyzingView.showScore(Int(self.scoreResult), showAnimation: false)
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
        if !LinkFile.isEmpty {
            print("link mp3: " + LinkFile)
            //playSound(LinkFile)
            HttpDownloader.loadFileSync(NSURL(string: LinkFile)!, completion: { (path, error) -> Void in
                self.playSound(NSURL(fileURLWithPath: path))
            })
        }
    }
    
    func doFinishPlaying() {
        if (self.scoreResult >= 0.0) {
            self.btnPlay.setBackgroundImage(UIImage(named: "ic_play.png"), forState: UIControlState.Normal)
            self.btnPlay.backgroundColor = Multimedia.colorWithHexString("#579e11")
            showColorOfScoreResult(scoreResult)
            self.analyzingView.showScore(Int(self.scoreResult), showAnimation: false)
                    ennableViewPlay()
        }

    }
    
    func playSound(fileUrl: NSURL) {
        print("run in play")
        if self.player.isPlaying{
            self.player.pause()
        }
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
        btnRecord.backgroundColor = Multimedia.colorWithHexString("#579e11")
        btnPlay.backgroundColor = Multimedia.colorWithHexString("#929292")
        btnPlay.enabled = false
        btnPlay.setBackgroundImage(UIImage(named: "ic_play.png"), forState: UIControlState.Disabled)
        btnPlayDemo.setTitleColor(Multimedia.colorWithHexString("#579e11"), forState: UIControlState.Normal)
        lblIPA.textColor = Multimedia.colorWithHexString("#579e11")
        tvDescription.textColor = Multimedia.colorWithHexString("#579e11")
    }
    
    func changeColorRed(){
        btnRecord.backgroundColor = Multimedia.colorWithHexString("#ff3333")
        btnPlay.backgroundColor = Multimedia.colorWithHexString("#ff3333")
        btnPlayDemo.setTitleColor(Multimedia.colorWithHexString("#ff3333"), forState: UIControlState.Normal)
        lblIPA.textColor = Multimedia.colorWithHexString("#ff3333")
        tvDescription.textColor = Multimedia.colorWithHexString("#ff3333")
    }
    
    func changeColorOrange(){
        btnRecord.backgroundColor = Multimedia.colorWithHexString("#ff7548")
        btnPlay.backgroundColor = Multimedia.colorWithHexString("#ff7548")
        btnPlayDemo.setTitleColor(Multimedia.colorWithHexString("#ff7548"), forState: UIControlState.Normal)
        lblIPA.textColor = Multimedia.colorWithHexString("#ff7548")
        tvDescription.textColor = Multimedia.colorWithHexString("#ff7548")
    }
    
    func changeColorGreen(){
        btnRecord.backgroundColor = Multimedia.colorWithHexString("#579e11")
        btnPlay.backgroundColor = Multimedia.colorWithHexString("#579e11")
        btnPlayDemo.setTitleColor(Multimedia.colorWithHexString("#579e11"), forState: UIControlState.Normal)
        lblIPA.textColor = Multimedia.colorWithHexString("#579e11")
        tvDescription.textColor = Multimedia.colorWithHexString("#579e11")
    }
    
    func disableViewPlay(){
        btnRecord.enabled = false
        btnRecord.backgroundColor = Multimedia.colorWithHexString("#929292")
        btnPlayDemo.enabled = false
        btnPlayDemo.setTitleColor(Multimedia.colorWithHexString("#929292"), forState: UIControlState.Normal)
        lblIPA.textColor = Multimedia.colorWithHexString("#929292")
        tvDescription.textColor = Multimedia.colorWithHexString("#929292")
    }
    
    func ennableViewPlay(){
        btnRecord.enabled = true
        btnPlayDemo.enabled = true
    }
    
    func disableViewRecord(){
        btnPlay.enabled = false
        btnPlay.backgroundColor = Multimedia.colorWithHexString("#929292")
        btnPlayDemo.enabled = false
        btnPlayDemo.setTitleColor(Multimedia.colorWithHexString("#929292"), forState: UIControlState.Normal)
        lblIPA.textColor = Multimedia.colorWithHexString("#929292")
        tvDescription.textColor = Multimedia.colorWithHexString("#929292")
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
        if self.isRecording {
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
        
    }
    
    func onAnimationMax() {
        
    }

}
