//
//  TipsViewController.swift
//  AccentEasy
//
//  Created by Minh Kelly on 2/25/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import UIKit
import EZAudio

class TipsViewController: UIViewController, EZAudioPlayerDelegate {

    @IBOutlet weak var btnUp: UIButton!
    
    @IBOutlet weak var btnPlay: UIButton!
    
    @IBOutlet weak var lblWord: UILabel!
    
    @IBOutlet weak var lblTitle: UILabel!
    
    @IBOutlet weak var lblSubTitle: UILabel!
    
    @IBOutlet weak var imgTip: UIImageView!
    
    @IBOutlet weak var lblDescription: UILabel!
    
    var player: EZAudioPlayer!
    
    @IBAction func btnUpOnTouch(sender: UIButton) {
        
    }
    
    @IBAction func btnPlayOnTouch(sender: UIButton) {
        if selectedTip != nil && selectedTip.mp3URL != nil && !selectedTip.mp3URL.isEmpty {
            print("link mp3: " + selectedTip.mp3URL)
            //playSound(LinkFile)
            HttpDownloader.loadFileSync(NSURL(string: selectedTip.mp3URL)!, completion: { (path, error) -> Void in
                self.playSound(NSURL(fileURLWithPath: path))
            })
        }
    }
    
    var wordAdapter:WordCollectionDbApdater!
    
    var tipList:Array<IPAMapArpabet>!
    
    var selectedTip:IPAMapArpabet!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let session: AVAudioSession = AVAudioSession.sharedInstance()
        do {
            try session.setCategory(AVAudioSessionCategoryPlayback)
        } catch {
            
        }
        do {
            try session.setActive(true)
        } catch {
            
        }
        self.player = EZAudioPlayer(delegate: self)
        
        do {
            try session.overrideOutputAudioPort(AVAudioSessionPortOverride.Speaker)
        } catch {
            
        }
        
        wordAdapter = WordCollectionDbApdater(dbFile: DatabaseHelper.getLessonDatabaseFile()!)
        do {
            try tipList = wordAdapter.getIPAMapArpabets();
            let index:Int = Int(arc4random_uniform(UInt32(tipList.count)))
            selectedTip = tipList[index]
        }catch {
            
        }
        self.showTip()
        self.initUI()
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "loadList:",name:"load", object: nil)
        // Do any additional setup after loading the view.
    }
    
    func loadList(notification: NSNotification){
        //load data here
        let index:Int = Int(arc4random_uniform(UInt32(tipList.count)))
        selectedTip = tipList[index]
        self.showTip()
    }
    
    func playSound(fileUrl: NSURL) {
        print("run in play")
        if self.player.isPlaying{
            self.player.pause()
        }
        self.player.playAudioFile(EZAudioFile(URL: fileUrl))
    }
    
    

    
    func showTip() {
        weak var weakSelf = self;
        lblWord.text = selectedTip!.getWordList()[0]
        lblTitle.text = selectedTip!.ipa
        lblSubTitle.text = "<\(selectedTip!.arpabet)> \(selectedTip.words)"
        lblDescription.text = selectedTip!.tip
        imgTip.clipsToBounds = true
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)) {
            if weakSelf!.selectedTip!.imgTongue != nil && !weakSelf!.selectedTip!.imgTongue.isEmpty {
                let url = NSURL(string: weakSelf!.selectedTip!.imgTongue)
                let data = NSData(contentsOfURL: url!) 
                dispatch_async(dispatch_get_main_queue(),{
                    do {
                        try weakSelf!.imgTip.image = ImageHelper.imageWithImage(UIImage(data: data!)!, scaledToSize: CGSize(width: weakSelf!.imgTip.frame.width,height: weakSelf!.imgTip.frame.width))
                    } catch {
                        
                    }
                })
            }
        }
    }
    
    func initUI() {
        btnPlay.layer.cornerRadius = btnPlay.frame.size.width/2
        btnPlay.clipsToBounds = true
        btnPlay.backgroundColor = ColorHelper.APP_GREEN
        btnUp.layer.cornerRadius = btnUp.frame.size.width/2
        btnUp.clipsToBounds = true
        btnUp.backgroundColor = ColorHelper.APP_GREEN
        
        lblWord.textColor = ColorHelper.APP_GREEN
        lblTitle.textColor = ColorHelper.APP_GREEN
        lblSubTitle.textColor = ColorHelper.APP_GREEN
        lblDescription.textColor = ColorHelper.APP_GREEN
        lblDescription.numberOfLines = 0
        lblDescription.sizeToFit()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
