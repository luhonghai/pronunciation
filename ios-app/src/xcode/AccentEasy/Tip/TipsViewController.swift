//
//  TipsViewController.swift
//  AccentEasy
//
//  Created by Minh Kelly on 2/25/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit
import EZAudio
import ImageLoader

class TipsViewController: UIViewController, EZAudioPlayerDelegate {

    @IBOutlet weak var btnUp: UIButton!
    
    @IBOutlet weak var btnPlay: UIButton!
    
    @IBOutlet weak var lblWord: UILabel!
    
    @IBOutlet weak var lblTitle: UILabel!
    
    @IBOutlet weak var lblSubTitle: UILabel!
    
    @IBOutlet weak var imgTip: UIImageView!
    
    @IBOutlet weak var lblDescription: UILabel!
    
    var player: EZAudioPlayer!
    
    
    @IBOutlet weak var btnNext: UIButton!

    @IBOutlet weak var btnPrev: UIButton!
    
    @IBAction func tapPrev(sender: AnyObject) {
        prevWord()
    }
    
    @IBAction func tapNext(sender: AnyObject) {
        nextWord()
    }
    @IBAction func btnUpOnTouch(sender: UIButton) {
        NSNotificationCenter.defaultCenter().postNotificationName("closeDetail", object: nil)
        NSNotificationCenter.defaultCenter().postNotificationName("loadWord", object: words[selectedWordIndex])
    }
    
    @IBAction func btnPlayOnTouch(sender: UIButton) {
        if selectedTip != nil && selectedTip.mp3URL != nil && !selectedTip.mp3URL.isEmpty {
            Logger.log("link mp3: " + selectedTip.mp3URL)
            //playSound(LinkFile)
            HttpDownloader.loadFileSync(NSURL(string: selectedTip.mp3URL)!, completion: { (path, error) -> Void in
                self.playSound(NSURL(fileURLWithPath: path))
            })
        }
    }
    
    var wordAdapter:WordCollectionDbApdater!
    
    var tipList:Array<IPAMapArpabet>!
    
    var selectedTip:IPAMapArpabet!
    
    var selectedWordIndex = 0
    
    var words: [String]!
    
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
        
        wordAdapter = WordCollectionDbApdater()
        do {
            try tipList = wordAdapter.getIPAMapArpabets();
            let index:Int = Int(arc4random_uniform(UInt32(tipList.count)))
            selectedTip = tipList[index]
        }catch {
            
        }
        self.showTip()
        self.initUI()
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "loadList:",name:"loadTip", object: nil)
        // Do any additional setup after loading the view.
    }
    
    func loadList(notification: NSNotification){
        //load data here
        selectedWordIndex = 0
        let index:Int = Int(arc4random_uniform(UInt32(tipList.count)))
        selectedTip = tipList[index]
        self.showTip()
    }
    
    func playSound(fileUrl: NSURL) {
        Logger.log("run in play")
        if self.player.isPlaying{
            self.player.pause()
        }
        self.player.playAudioFile(EZAudioFile(URL: fileUrl))
    }
    
    func showWord() {
        if (words.count > 0) {
            lblWord.text = words[selectedWordIndex]
        } else {
            lblWord.text = ""
        }
        if (selectedWordIndex == 0) {
            btnPrev.hidden = true
        } else {
            btnPrev.hidden = false
        }
        if (selectedWordIndex == words.count - 1) {
            btnNext.hidden = true
        } else {
            btnNext.hidden = false
        }
    }
    
    func prevWord() {
        selectedWordIndex--
        if (selectedWordIndex < 0) {
            selectedWordIndex = 0
        }
        
        showWord()
    }
    
    func nextWord() {
        selectedWordIndex++;
        if (selectedWordIndex >= words.count) {
            selectedWordIndex = words.count - 1;
        }
        if (selectedWordIndex < 0) {
            selectedWordIndex = 0
        }
        showWord()
    }
    
    func showTip() {
        weak var weakSelf = self;
        words = selectedTip!.getWordList()
        showWord()
        lblTitle.text = selectedTip!.ipa
        lblSubTitle.text = "<\(selectedTip!.arpabet)> \(selectedTip.words)"
        lblDescription.text = selectedTip!.tip
        imgTip.clipsToBounds = true
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)) {
            if weakSelf!.selectedTip!.imgTongue != nil && !weakSelf!.selectedTip!.imgTongue.isEmpty {
                let url = NSURL(string: weakSelf!.selectedTip!.imgTongue)
                weakSelf!.imgTip.load(url!)
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
