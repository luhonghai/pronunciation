//
//  FSDetailVC.swift
//  AccentEasy
//
//  Created by CMGVN on 2/29/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import UIKit
import EZAudio
import SloppySwiper

class MyCollectionViewCell: UICollectionViewCell {
    
    //@IBOutlet weak var myLabel: UILabel!
    //@IBOutlet weak var myLabel: UILabel!
    @IBOutlet weak var lblIPA: UILabel!
    
}

class FSDetailVC: UIViewController, UICollectionViewDataSource, UICollectionViewDelegate,  IPAPopupViewControllerDelegate, EZAudioPlayerDelegate {

    var userProfileSaveInApp:NSUserDefaults!

    @IBOutlet weak var menuButton: UIBarButtonItem!
    @IBOutlet weak var btnPlay: UIButton!
    @IBOutlet weak var btnPlayDemo: UIButton!
    @IBOutlet weak var cViewIPAList: UICollectionView!
    @IBOutlet weak var viewAnalyzing: AnalyzingView!
    
    //varible for data
    var wordSelected:WordCollection!
    var userVoiceModelResult:UserVoiceModel!
    
    //play audio
    var player: EZAudioPlayer!
    var fileName:String = "tmp_record_file"
    var fileType:String = "wav"
    
    //collection view
    let reuseIdentifier = "cell" // also enter this string as the cell identifier in the storyboard
    var items = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48"]
    //var items = ["1", "2", "3", "4", "5"]
    var arrIPAMapArpabet = [IPAMapArpabet]()
    var indexCellChoice:Int!
    
    //var global
    let redColor = "#ff3333"
    let orangeColor = "#ff7548"
    let greenColor = "#579e11"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        activateAudioSession()
//        let swiper = SloppySwiper(navigationController: self.navigationController)
//        self.navigationController?.delegate = swiper
        cViewIPAList.delegate = self
        cViewIPAList.dataSource = self
        //init audio plaer
        player = EZAudioPlayer(delegate: self)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "selectDetail:",name:"selectDetail", object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "closeDetail:",name:"closeDetail", object: nil)
        btnPlay.hidden = true
        showDetail()
    }
    
    func activateAudioSession() {
        let session: AVAudioSession = AVAudioSession.sharedInstance()
        do {
            try session.setCategory(AVAudioSessionCategoryPlayback)
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
    
    func roundButton() {
        //button style cricle
        btnPlay.layer.cornerRadius = btnPlay.frame.size.width/2
        btnPlay.clipsToBounds = true
        btnPlay.hidden = false
    }
    
    func showDetail() {
        let adapter = WordCollectionDbApdater()
        do {
            wordSelected = try adapter.findByWord(userVoiceModelResult.word)
        } catch {
            
        }
        //load data for detail screen
        userProfileSaveInApp = NSUserDefaults()
        //set data for view
        arrIPAMapArpabet = [IPAMapArpabet]()
        btnPlayDemo.setTitle(wordSelected.word, forState: UIControlState.Normal)
        for index in 0...userVoiceModelResult.result.phonemeScores.count-1 {
            //Logger.log(userVoiceModelResult.result.phonemeScores[index].name)
            do{
                Logger.log("index:\(index) iphaberName:\(userVoiceModelResult.result.phonemeScores[index].name)")
                arrIPAMapArpabet.append(try adapter.getIPAMapArpabet(userVoiceModelResult.result.phonemeScores[index].name))
                //Logger.log(iPAMapArpabet.ipa)
            }catch{
                Logger.log("load word default error")
            }
            
        }
        //Logger.log(arrIPAMapArpabet.count)
        
        //change view color
        showColorOfScoreResult(userVoiceModelResult.score)
        cViewIPAList.reloadData()
        viewAnalyzing.showScore(Int(userVoiceModelResult.score), showAnimation: true)
    }
    
    func selectDetail(notification: NSNotification) {
        let uuid = notification.object as! String
        let modelJsonPath = FileHelper.getFilePath("audio/\(uuid).json")
        if FileHelper.isExists(modelJsonPath) {
            do {
                let model: UserVoiceModel = try JSONHelper.fromJson(FileHelper.readFile(modelJsonPath))
                userVoiceModelResult = model
                showDetail()
            } catch {
                
            }
        }
        
    }
    
    func closeDetail(notification: NSNotification) {
        GlobalData.getInstance().selectedWord = ""
       
        self.navigationController?.popViewControllerAnimated(true)
         NSNotificationCenter.defaultCenter().postNotificationName("loadHistory", object: "")
    }
    
    override func viewDidLayoutSubviews() {
        roundButton()
    }
    
    override func viewDidAppear(animated: Bool) {
        
        viewAnalyzing.refreshLayout()
        NSNotificationCenter.defaultCenter().postNotificationName("loadGraph", object: self.userVoiceModelResult.word)
        GlobalData.getInstance().selectedWord = self.userVoiceModelResult.word
        NSNotificationCenter.defaultCenter().postNotificationName("loadHistory", object: self.userVoiceModelResult.word)
        delay(1) {
            if self.isShowSlider {
                self.toggleSlider()
            }
        }
        activateAudioSession()
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
    
    /*colection view setup
    *************************************************************/
    // tell the collection view how many cells to make
    func collectionView(collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.arrIPAMapArpabet.count
    }
    
    // make a cell for each cell index path
    func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell {
        
        // get a reference to our storyboard cell
        let cell = collectionView.dequeueReusableCellWithReuseIdentifier(reuseIdentifier, forIndexPath: indexPath) as! MyCollectionViewCell
        
        // Use the outlet in our custom class to get a reference to the UILabel in the cell
        cell.lblIPA.text = self.arrIPAMapArpabet[indexPath.item].ipa
        cell.lblIPA.textColor = UIColor.whiteColor()
        cell.lblIPA.backgroundColor = Multimedia.colorWithHexString(getIPAColorByScore(userVoiceModelResult.result.phonemeScores[indexPath.item].totalScore))
        cell.layer.cornerRadius = 5
        cell.backgroundColor = Multimedia.colorWithHexString("#579e11") // make cell more visible in our example project
        
        return cell
    }
    
    // MARK: - UICollectionViewDelegate protocol
    
    func collectionView(collectionView: UICollectionView, didSelectItemAtIndexPath indexPath: NSIndexPath) {
        // handle tap events
        Logger.log("You selected cell #\(indexPath.item)!")
        indexCellChoice = indexPath.item
        self.displayViewController(.Fade)
    }
    
    //select cell
    let ipaPopupVC:IPAPopupVC = IPAPopupVC(nibName:"IPAPopupVC", bundle: nil)
    var timer:NSTimer!
    
    func displayViewController(animationType: SLpopupViewAnimationType) {
        
        let cellColor:UIColor = Multimedia.colorWithHexString(getIPAColorByScore(userVoiceModelResult.result.phonemeScores[indexCellChoice].totalScore))
        ipaPopupVC.view.backgroundColor = cellColor
        ipaPopupVC.view.layer.cornerRadius = 5
        ipaPopupVC.lblScore.text = convertScoreToString(userVoiceModelResult.result.phonemeScores[indexCellChoice].totalScore)
        ipaPopupVC.lblIPA.text = arrIPAMapArpabet[indexCellChoice].ipa
        ipaPopupVC.btnPlayExample.tintColor = cellColor
        ipaPopupVC.btnShowChart.tintColor = cellColor
        ipaPopupVC.isShow = true
        
        ipaPopupVC.delegate = self
        
        clearTimer()
        timer = NSTimer.scheduledTimerWithTimeInterval(10, target: self, selector: Selector("closeIPAPopup"), userInfo: nil, repeats: true)
        
        self.presentpopupViewController(ipaPopupVC, animationType: animationType, completion: { () -> Void in
            
        })
    }
    
    func closeIPAPopup() {
        clearTimer()
        if (ipaPopupVC.isShow) {
            self.dismissPopupViewController(.Fade)
        }
        print("run in timer")
    }
    
    func clearTimer() {
        if timer != nil {
            timer.invalidate()
            timer = nil
        }
    }
    
    func pressPlayExample(sender: IPAPopupVC?) {
        Logger.log("press PlayExample")
        //play ipa
            let linkFile:String = arrIPAMapArpabet[indexCellChoice].mp3URL
            if !linkFile.isEmpty {
                Logger.log("link mp3: " + linkFile)
                //playSound(LinkFile)
                HttpDownloader.loadFileSync(NSURL(string: linkFile)!, completion: { (path, error) -> Void in
                    self.playSound(NSURL(fileURLWithPath: path))
                })
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
    
    
    func audioPlayer(audioPlayer: EZAudioPlayer!, reachedEndOfAudioFile audioFile: EZAudioFile!) {
        self.btnPlay.setBackgroundImage(UIImage(named: "ic_play.png"), forState: UIControlState.Normal)
        showColorOfScoreResult(userVoiceModelResult.score)
    }
    
    func pressShowChart(sender: IPAPopupVC?) {
        Logger.log("press ShowChart")
        self.dismissPopupViewController(.Fade)
        NSNotificationCenter.defaultCenter().postNotificationName("showChart", object: indexCellChoice)
        NSNotificationCenter.defaultCenter().postNotificationName("loadTip", object: arrIPAMapArpabet[indexCellChoice].arpabet)
        toggleSlider()
    }
    
    func convertScoreToString(score: Float) -> String {
        var myArray = round(score).description.componentsSeparatedByString(".")
        return myArray[0] + "%"
    }
    
    
    
    @IBAction func btnPlayTouchUp(sender: AnyObject) {
        if self.player.isPlaying {
            Logger.log("stop playing")
            self.player.pause()
            self.btnPlay.setBackgroundImage(UIImage(named: "ic_play.png"), forState: UIControlState.Normal)
            //self.btnPlay.backgroundColor = Multimedia.colorWithHexString("#579e11")
            showColorOfScoreResult(userVoiceModelResult.score)
            //if (self.scoreResult >= 0.0) {
                //showColorOfScoreResult(scoreResult)
                //self.analyzingView.showScore(Int(self.scoreResult), showAnimation: false)
            //}
            
        } else {
            Logger.log("play")
            self.btnPlay.setBackgroundImage(UIImage(named: "ic_close.png"), forState: UIControlState.Normal)
            self.btnPlay.backgroundColor = Multimedia.colorWithHexString("#ff3333")
            Logger.log(getTmpFilePath())
            playSound(getTmpFilePath())
        }

        
    }
    
    func getTmpFilePath() -> NSURL
    {
        return NSURL(fileURLWithPath: FileHelper.getFilePath("audio/\(userVoiceModelResult.id).wav"))
    }
    
    @IBAction func btnPlayDemoTouchUp(sender: AnyObject) {
        if let linkFile = wordSelected.mp3Path {
            if !linkFile.isEmpty {
                Logger.log("link mp3: " + linkFile)
                //playSound(LinkFile)
                HttpDownloader.loadFileSync(NSURL(string: linkFile)!, completion: { (path, error) -> Void in
                    self.playSound(NSURL(fileURLWithPath: path))
                })
            }
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
    
    
    func getIPAColorByScore(score: Float) ->  String{
        if score < 45 {
            //color < 45 red
            return redColor
        } else if score >= 45 && score < 80 {
            // 45 <= color < 80 orange
            return orangeColor
        } else {
            //color >= 80 green
            return greenColor
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
    
    func changeColorRed(){
        btnPlay.backgroundColor = Multimedia.colorWithHexString(redColor)
        btnPlayDemo.setTitleColor(Multimedia.colorWithHexString(redColor), forState: UIControlState.Normal)
        viewAnalyzing.backgroundColor = Multimedia.colorWithHexString(redColor)
    }
    
    func changeColorOrange(){
        btnPlay.backgroundColor = Multimedia.colorWithHexString(orangeColor)
        btnPlayDemo.setTitleColor(Multimedia.colorWithHexString(orangeColor), forState: UIControlState.Normal)
        viewAnalyzing.backgroundColor = Multimedia.colorWithHexString(orangeColor)
    }
    
    func changeColorGreen(){
        btnPlay.backgroundColor = Multimedia.colorWithHexString(greenColor)
        btnPlayDemo.setTitleColor(Multimedia.colorWithHexString(greenColor), forState: UIControlState.Normal)
        viewAnalyzing.backgroundColor = Multimedia.colorWithHexString(greenColor)
    }


    @IBAction func viewAnalyzingTapped(sender: AnyObject) {
        self.navigationController?.popViewControllerAnimated(true)
    }
    
    
    @IBAction func clickMenuButton(sender: AnyObject) {
        //self.performSegueWithIdentifier("DetailScreenGoToMain", sender: self)
        //self.navigationController?.popToRootViewControllerAnimated(true)
        GlobalData.getInstance().selectedWord = ""
        
        self.navigationController?.popViewControllerAnimated(true)
    }
    
    
    var isShowSlider = true
    
    @IBAction func sliderClick(sender: AnyObject) {
         toggleSlider()
    }
    @IBOutlet weak var btnSlider: UIButton!
    @IBOutlet weak var sliderContent: UIView!
    
    @IBOutlet weak var sliderContainer: UIView!
    
    func toggleSlider() {
        weak var weakSelf = self
        UIView.animateWithDuration(0.3) { () -> Void in
            if (weakSelf!.isShowSlider) {
                weakSelf!.sliderBackground.alpha = 0
                weakSelf!.sliderContainer.frame = CGRectMake(CGRectGetMinX(weakSelf!.sliderContainer.frame), CGRectGetHeight(weakSelf!.view.frame)
                    - CGRectGetHeight(weakSelf!.btnSlider.frame) + 3, CGRectGetWidth(weakSelf!.sliderContainer.frame), CGRectGetHeight(weakSelf!.sliderContainer.frame))
            } else {
                weakSelf!.sliderBackground.alpha = weakSelf!.maxAlpha
                weakSelf!.sliderContainer.frame = CGRectMake(CGRectGetMinX(weakSelf!.sliderContainer.frame), CGRectGetHeight(weakSelf!.view.frame)
                    - CGRectGetHeight(weakSelf!.sliderContainer.frame), CGRectGetWidth(weakSelf!.sliderContainer.frame), CGRectGetHeight(weakSelf!.sliderContainer.frame))
            }
            weakSelf!.isShowSlider = !weakSelf!.isShowSlider
            weakSelf!.sliderContainer.translatesAutoresizingMaskIntoConstraints = true
        }
        
    }
    
    @IBAction func handlePan(sender: UIPanGestureRecognizer) {
        if sender.state == UIGestureRecognizerState.Ended {
            if let view = sliderContainer {
                let currentY = view.center.y
                let halfHeight = CGRectGetHeight(sliderContainer.frame) / 2
                let maxY = CGRectGetHeight(self.view.frame)
                    - CGRectGetHeight(btnSlider.frame) + 3 + halfHeight
                let minY = CGRectGetHeight(self.view.frame)
                    - halfHeight
               // Logger.log("\(currentY) - \(maxY) - \(minY) - \((maxY - minY)/2)")
                isShowSlider = !(currentY >= (maxY - minY) / 2 + minY)
                weak var weakSelf = self
                UIView.animateWithDuration(0.3, animations: { () -> Void in
                    view.center = CGPoint(x:view.center.x,
                        y: (weakSelf!.isShowSlider ? minY : maxY))
                    weakSelf!.sliderBackground.alpha = weakSelf!.isShowSlider ? weakSelf!.maxAlpha : 0
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
                    view.center = CGPoint(x:view.center.x,
                        y:destY)
                    self.sliderBackground.alpha = self.maxAlpha
                        * ((destY - maxY) / (minY - maxY))
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
