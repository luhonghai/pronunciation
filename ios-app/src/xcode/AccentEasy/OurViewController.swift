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

class OurViewController: UIViewController, UITableViewDataSource, UITableViewDelegate, UISearchResultsUpdating, AVAudioPlayerDelegate {

    //var loginParameter:NSUserDefaults!
    var userProfileSaveInApp:NSUserDefaults!
    var JSONStringUserProfile:String!
    var userProfile = UserProfile()
    var fileName:String = "fixed_6a11adce-13bb-479e-bcbc-13a7319677f9_raw"
    var fileType:String = "wav"
    var audioPlayer:AVAudioPlayer = AVAudioPlayer()
    var scoreResult:Float = 45
    var LinkFile:String!
    
    @IBOutlet weak var menuButton: UIBarButtonItem!
    @IBOutlet weak var btnRecord: UIButton!
    @IBOutlet weak var btnPlay: UIButton!
    @IBOutlet weak var viewSound: UIView!
    @IBOutlet weak var btnPlayDemo: UIButton!
    @IBOutlet weak var lblIPA: UILabel!
    @IBOutlet weak var tvDescription: UITextView!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        //button style cricle
        btnRecord.layer.cornerRadius = btnRecord.frame.size.width/2
        btnRecord.clipsToBounds = true
        btnPlay.layer.cornerRadius = btnPlay.frame.size.width/2
        btnPlay.clipsToBounds = true
        viewSound.layer.cornerRadius = viewSound.frame.size.width/2
        viewSound.clipsToBounds = true
        
        //menu
        if self.revealViewController() != nil {
            menuButton.target = self.revealViewController()
            menuButton.action = "revealToggle:"
            self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
        }
        
        //login parameter
        //loginParameter = NSUserDefaults()
        
        userProfileSaveInApp = NSUserDefaults()
        let keyForUserProfile:String = userProfileSaveInApp.objectForKey(Login.KeyUserProfile) as! String
        JSONStringUserProfile = userProfileSaveInApp.objectForKey(keyForUserProfile) as! String
        print("login successfull")
        print(JSONStringUserProfile)
        userProfile = Mapper<UserProfile>().map(JSONStringUserProfile)!
        //lblUsername.text = userProfile.username
        
        //init audioPlayer for check playing
        do{
        audioPlayer = try AVAudioPlayer(contentsOfURL:  NSURL(fileURLWithPath: NSBundle.mainBundle().pathForResource("fixed_6a11adce-13bb-479e-bcbc-13a7319677f9_raw", ofType: "wav")!))
        }catch{
            print("Error getting the audio file")
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



    var isRecord:Bool = false
    @IBAction func btnRecordTouchUp(sender: AnyObject) {
        disableViewRecord()
        //
        if(isRecord){
            print("not run record")
            return
        } else{
            self.btnRecord.setBackgroundImage(UIImage(named: "ic_close.png"), forState: UIControlState.Normal)
            self.btnRecord.backgroundColor = Multimedia.colorWithHexString("#ff3333")
            isRecord = true
        }

        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)) {
            //btnRecord.backgroundColor = Multimedia.colorWithHexString("#7030a0")
            //btnRecord.setBackgroundImage(UIImage(named: "ic_close.png"), forState: UIControlState.Normal)
            
            /*
            params.put("country", "countryId");
            params.put("profile", gson.toJson(profile));
            params.put("word", selectedWord);
            */
            
            //upload to server
            let client = Client()
                .baseUrl(FileHelper.getAccentEasyBaseUrl())
                .onError({e in print(e)});
            
            //client.post("/VoiceRecordHandler").attach("imageKey", NSData(contentsOfFile: "image.png")!, "image.png", withMimeType: "image/png")
            
            client.post("/VoiceRecordHandler").field("country", "countryId").field("profile", self.JSONStringUserProfile).field("word", "fixed").attach("imageKey", "/Volumes/DATA/AccentEasy/pronunciation/ios-app/src/xcode/AccentEasy/fixed_6a11adce-13bb-479e-bcbc-13a7319677f9_raw.wav")
                .set("header", "headerValue")
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
                        print (userVoiceModel.word)
                        if status {
                            //register suceess
                            dispatch_async(dispatch_get_main_queue(),{
                                //SweetAlert().showAlert("Register Success!", subTitle: "", style: AlertStyle.Success)
                                //[unowned self] in NSThread.isMainThread()
                                //self.performSegueWithIdentifier("AELoginGoToMain", sender: self)
                                self.showColorOfScoreResult(userVoiceModel.score)
                                self.ennableViewRecord()
                                self.btnRecord.setBackgroundImage(UIImage(named: "ic_record.png"), forState: UIControlState.Normal)
                                //self.btnRecord.backgroundColor = Multimedia.colorWithHexString("#579e11")
                                self.isRecord = false
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
    
    @IBAction func btnPlayTouchUp(sender: AnyObject) {
        disableViewPlay()
        if audioPlayer.playing {
            print("play")
            audioPlayer.stop()
            self.btnPlay.setBackgroundImage(UIImage(named: "ic_play.png"), forState: UIControlState.Normal)
            self.btnPlay.backgroundColor = Multimedia.colorWithHexString("#579e11")
            
            showColorOfScoreResult(scoreResult)
        } else {
        
            self.btnPlay.setBackgroundImage(UIImage(named: "ic_close.png"), forState: UIControlState.Normal)
            self.btnPlay.backgroundColor = Multimedia.colorWithHexString("#ff3333")
            
            dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)) {
                self.playSound(self.fileName, self.fileType)
            }
        }
    }
    
    @IBAction func btnPlayDemoTouchUp(sender: AnyObject) {
        if !LinkFile.isEmpty {
            print("link mp3: " + LinkFile)
            //playSound(LinkFile)
            HttpDownloader.loadFileSync(NSURL(string: LinkFile)!, completion: { (path, error) -> Void in
                self.playSound(path)
            })
        }
    }
    
    
    func audioPlayerDidFinishPlaying(player: AVAudioPlayer,
        successfully flag: Bool){
            self.btnPlay.setBackgroundImage(UIImage(named: "ic_play.png"), forState: UIControlState.Normal)
            self.btnPlay.backgroundColor = Multimedia.colorWithHexString("#579e11")
            showColorOfScoreResult(scoreResult)
            ennableViewPlay()
    }
    
    func playSound(fileName: String, _ fileType: String) {
        let coinSound = NSURL(fileURLWithPath: NSBundle.mainBundle().pathForResource(fileName, ofType: fileType)!)
        do{
            print("run in play")
            if audioPlayer.playing{
                audioPlayer.stop()
                //btnPlay.backgroundColor = Multimedia.colorWithHexString("#579e11")
                //btnPlay.setBackgroundImage(UIImage(named: "ic_play.png"), forState: UIControlState.Normal)
            }
            audioPlayer = try AVAudioPlayer(contentsOfURL:coinSound)
            audioPlayer.delegate = self
            audioPlayer.prepareToPlay()
            audioPlayer.play()
           
        }catch {
            print("Error getting the audio file")
        }
        
    }
    
    func playSound(linkFile: String) {
        let coinSound = NSURL(fileURLWithPath: linkFile)

        do{
            print("run in play")
            //if audioPlayer.playing{
                //.stop()
                //btnPlay.backgroundColor = Multimedia.colorWithHexString("#579e11")
                //btnPlay.setBackgroundImage(UIImage(named: "ic_play.png"), forState: UIControlState.Normal)
            //}
            audioPlayer = try AVAudioPlayer(contentsOfURL:coinSound)
            //audioPlayer.delegate = self
            audioPlayer.prepareToPlay()
            audioPlayer.play()
            
        }catch (let err as NSError){
            print( err)
        }
        
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
        viewSound.backgroundColor = Multimedia.colorWithHexString("#579e11")
        btnPlayDemo.setTitleColor(Multimedia.colorWithHexString("#579e11"), forState: UIControlState.Normal)
        lblIPA.textColor = Multimedia.colorWithHexString("#579e11")
        tvDescription.textColor = Multimedia.colorWithHexString("#579e11")
    }
    
    func changeColorRed(){
        btnRecord.backgroundColor = Multimedia.colorWithHexString("#ff3333")
        btnPlay.backgroundColor = Multimedia.colorWithHexString("#ff3333")
        viewSound.backgroundColor = Multimedia.colorWithHexString("#ff3333")
        btnPlayDemo.setTitleColor(Multimedia.colorWithHexString("#ff3333"), forState: UIControlState.Normal)
        lblIPA.textColor = Multimedia.colorWithHexString("#ff3333")
        tvDescription.textColor = Multimedia.colorWithHexString("#ff3333")
    }
    
    func changeColorOrange(){
        btnRecord.backgroundColor = Multimedia.colorWithHexString("#ff7548")
        btnPlay.backgroundColor = Multimedia.colorWithHexString("#ff7548")
        viewSound.backgroundColor = Multimedia.colorWithHexString("#ff7548")
        btnPlayDemo.setTitleColor(Multimedia.colorWithHexString("#ff7548"), forState: UIControlState.Normal)
        lblIPA.textColor = Multimedia.colorWithHexString("#ff7548")
        tvDescription.textColor = Multimedia.colorWithHexString("#ff7548")
    }
    
    func changeColorGreen(){
        btnRecord.backgroundColor = Multimedia.colorWithHexString("#579e11")
        btnPlay.backgroundColor = Multimedia.colorWithHexString("#579e11")
        viewSound.backgroundColor = Multimedia.colorWithHexString("#579e11")
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

}
