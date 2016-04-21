//
//  TestPassPopupVC.swift
//  AccentEasy
//
//  Created by CMGVN on 4/8/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit
import AVFoundation

class TestPassPopupVC: UIViewController {

    var delegate: TestPopupDelegate?
    var toltalScore:Int!
    var audioPlayer = AVAudioPlayer()
    
    @IBOutlet weak var btnClose: UIButton!
    @IBOutlet weak var aviewScore: AnalyzingView!
    @IBOutlet weak var btnGoogleShare: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        //style popup
        var pImage = UIImage(named: "ic_close_dialog.png")!
        pImage = ImageHelper.imageWithImage(image: pImage, w: 25, h: 25)
        btnClose.layer.cornerRadius = btnClose.frame.size.width/2
        btnClose.setImage(pImage, forState: UIControlState.Normal)
        btnClose.tintColor = ColorHelper.APP_PURPLE
        
        btnGoogleShare.backgroundColor = ColorHelper.APP_RED
        btnGoogleShare.layer.cornerRadius = btnGoogleShare.frame.size.width/2
        
        
        aviewScore.showScore(toltalScore, showAnimation: true)
        playSound()

    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewDidDisappear(animated: Bool) {
        print("viewDidDisappear")
        NSNotificationCenter.defaultCenter().postNotificationName("testPassMove", object: nil)
    }
    
    override func viewDidAppear(animated: Bool) {
        print("viewDidAppear")
        
    }
    
    func playSound()
    {
        let coinSound = NSURL(fileURLWithPath: NSBundle.mainBundle().pathForResource("ta_da_fanfare", ofType: "mp3")!)
        do{
            audioPlayer = try AVAudioPlayer(contentsOfURL:coinSound)
            audioPlayer.prepareToPlay()
            audioPlayer.play()
        }catch {
            Logger.log("Error getting the audio file")
        }
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    @IBAction func btnCloseTouchUp(sender: AnyObject) {
        delegate?.closeTestPassPopup!(self)
    }
    
    

}
