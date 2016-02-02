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

class OurViewController: UIViewController {

    //var loginParameter:NSUserDefaults!
    var userProfileSaveInApp:NSUserDefaults!
    var JSONStringUserProfile:String!
    var userProfile = UserProfile()
    var fileName:String = "fixed_6a11adce-13bb-479e-bcbc-13a7319677f9_raw"
    var fileType:String = "wav"
    var audioPlayer:AVAudioPlayer = AVAudioPlayer()
    
    @IBOutlet weak var menuButton: UIBarButtonItem!
    @IBOutlet weak var lblUsername: UILabel!
    @IBOutlet weak var btnRecord: UIButton!
    @IBOutlet weak var btnPlay: UIButton!
    @IBOutlet weak var viewSound: UIView!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        //button style
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
        lblUsername.text = userProfile.username
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

    @IBAction func btnRecordTapped(sender: AnyObject) {
        btnRecord.backgroundColor = Multimedia.colorWithHexString("#7030a0")
        btnRecord.setBackgroundImage(UIImage(named: "ic_close.png"), forState: UIControlState.Normal)
        
        
        /*
        params.put("country", "countryId");
        params.put("profile", gson.toJson(profile));
        params.put("word", selectedWord);
        */
        
        //upload to server
        let client = Client()
            .baseUrl("http://localhost:8080")
            .onError({e in print(e)});
        
        //client.post("/VoiceRecordHandler").attach("imageKey", NSData(contentsOfFile: "image.png")!, "image.png", withMimeType: "image/png")
        
        client.post("/VoiceRecordHandler").field("country", "countryId").field("profile", JSONStringUserProfile).field("word", "fixed").attach("imageKey", "/Volumes/DATA/AccentEasy/pronunciation/ios-app/src/xcode/AccentEasy/fixed_6a11adce-13bb-479e-bcbc-13a7319677f9_raw.wav")
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
                        })
                        
                        
                    } else {
                        //SweetAlert().showAlert("Register Failed!", subTitle: "It's pretty, isn't it?", style: AlertStyle.Error)
                        dispatch_async(dispatch_get_main_queue(),{
                            SweetAlert().showAlert("Login Failed!", subTitle: message, style: AlertStyle.Error)
                            
                        })
                    }
                    //print(result?.message)
                    //print(result?.status)
                }
            })

    }
    
    @IBAction func btnPlayTapped(sender: AnyObject) {
        playSound(fileName, fileType)
    }
    
    func playSound(fileName: String, _ fileType: String) {
        let coinSound = NSURL(fileURLWithPath: NSBundle.mainBundle().pathForResource(fileName, ofType: fileType)!)
        do{
            print("run in play")
            audioPlayer = try AVAudioPlayer(contentsOfURL:coinSound)
            audioPlayer.prepareToPlay()
            audioPlayer.play()
            //while audioPlayer.playing{ audioPlayer.stop() }
        }catch {
            print("Error getting the audio file")
        }
        
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
