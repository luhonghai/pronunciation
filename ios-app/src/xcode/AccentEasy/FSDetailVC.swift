//
//  FSDetailVC.swift
//  AccentEasy
//
//  Created by CMGVN on 2/29/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import UIKit
import EZAudio

class MyCollectionViewCell: UICollectionViewCell {
    
    //@IBOutlet weak var myLabel: UILabel!
    //@IBOutlet weak var myLabel: UILabel!
    @IBOutlet weak var lblIPA: UILabel!
    
}

class FSDetailVC: UIViewController, UICollectionViewDataSource, UICollectionViewDelegate,  IPAPopupViewControllerDelegate, EZAudioPlayerDelegate{

    var userProfileSaveInApp:NSUserDefaults!

    @IBOutlet weak var menuButton: UIBarButtonItem!
    @IBOutlet weak var btnPlay: UIButton!
    @IBOutlet weak var btnPlayDemo: UIButton!
    @IBOutlet weak var cViewIPAList: UICollectionView!
    @IBOutlet weak var viewAnalyzing: AnalyzingView!
    
    //varible for data
    var wordSelected:WordCollection!
    var voidModelResult:VoidModelResult!
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
    var isPlayInIPAPopup:Bool = false
    
    //var global
    let redColor = "#ff3333"
    let orangeColor = "#ff7548"
    let greenColor = "#579e11"
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        //collection view
        cViewIPAList.delegate = self
        cViewIPAList.dataSource = self
        
        //load data for detail screen
        userProfileSaveInApp = NSUserDefaults()
        let keyWordSelectedInMainScreen:String = userProfileSaveInApp.objectForKey(FSScreen.KeyWordSelectedInMainScreen) as! String
        let keyVoidModelResult:String = userProfileSaveInApp.objectForKey(FSScreen.KeyVoidModelResult) as! String
        wordSelected = Mapper<WordCollection>().map(keyWordSelectedInMainScreen)
        voidModelResult = Mapper<VoidModelResult>().map(keyVoidModelResult)
        userVoiceModelResult = voidModelResult.data
        
        //set data for view
        btnPlayDemo.setTitle(wordSelected.word, forState: UIControlState.Normal)
        
        let adapter = WordCollectionDbApdater()
        
        //print("X:\(viewIPAList.frame.origin.x) Y:\(viewIPAList.frame.origin.y) width:\(viewIPAList.frame.width) sizewidth:\(viewIPAList.frame.size.width) height:\(viewIPAList.frame.height) sizeheight:\(viewIPAList.frame.size.height)")
        
        for index in 0...userVoiceModelResult.result.phonemeScores.count-1 {
            //print(userVoiceModelResult.result.phonemeScores[index].name)
            do{
                print("index:\(index) iphaberName:\(userVoiceModelResult.result.phonemeScores[index].name)")
                arrIPAMapArpabet.append(try adapter.getIPAMapArpabet(userVoiceModelResult.result.phonemeScores[index].name))
                //print(iPAMapArpabet.ipa)
            }catch{
                print("load word default error")
            }

        }
        //print(arrIPAMapArpabet.count)
        
        //button style cricle
        btnPlay.layer.cornerRadius = btnPlay.frame.size.width/2
        btnPlay.clipsToBounds = true
        
        //change view color
        showColorOfScoreResult(voidModelResult.data.score)
        
        viewAnalyzing.showScore(Int(voidModelResult.data.score), showAnimation: true)
        
        //init audio plaer
        player = EZAudioPlayer(delegate: self)
        
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
        print("You selected cell #\(indexPath.item)!")
        indexCellChoice = indexPath.item
        self.displayViewController(.Fade)
    }
    
    //select cell
    func displayViewController(animationType: SLpopupViewAnimationType) {
        let cellColor:UIColor = Multimedia.colorWithHexString(getIPAColorByScore(userVoiceModelResult.result.phonemeScores[indexCellChoice].totalScore))
        let ipaPopupVC:IPAPopupVC = IPAPopupVC(nibName:"IPAPopupVC", bundle: nil)
        ipaPopupVC.view.backgroundColor = cellColor
        ipaPopupVC.view.layer.cornerRadius = 5
        ipaPopupVC.lblScore.text = convertScoreToString(userVoiceModelResult.result.phonemeScores[indexCellChoice].totalScore)
        ipaPopupVC.lblIPA.text = arrIPAMapArpabet[indexCellChoice].ipa
        ipaPopupVC.btnPlayExample.tintColor = cellColor
        ipaPopupVC.btnShowChart.tintColor = cellColor
        
        ipaPopupVC.delegate = self
        
        self.presentpopupViewController(ipaPopupVC, animationType: animationType, completion: { () -> Void in
            
        })
    }
    
    func pressPlayExample(sender: IPAPopupVC) {
        print("press PlayExample", terminator: "\n")
        //play ipa
            let linkFile:String = arrIPAMapArpabet[indexCellChoice].mp3URL
            if !linkFile.isEmpty {
                print("link mp3: " + linkFile)
                //playSound(LinkFile)
                HttpDownloader.loadFileSync(NSURL(string: linkFile)!, completion: { (path, error) -> Void in
                    self.playSound(NSURL(fileURLWithPath: path))
                })
            }
        isPlayInIPAPopup = true
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
        if isPlayInIPAPopup {
            self.dismissPopupViewController(.Fade)
        }
        isPlayInIPAPopup = false
    }
    
    func pressShowChart(sender: IPAPopupVC) {
        print("press ShowChart", terminator: "\n")
        self.dismissPopupViewController(.Fade)
    }
    
    func convertScoreToString(score: Float) -> String {
        var myArray = round(score).description.componentsSeparatedByString(".")
        return myArray[0] + "%"
    }
    
    
    
    @IBAction func btnPlayTouchUp(sender: AnyObject) {
        if self.player.isPlaying {
            print("stop playing")
            self.player.pause()
            self.btnPlay.setBackgroundImage(UIImage(named: "ic_play.png"), forState: UIControlState.Normal)
            self.btnPlay.backgroundColor = Multimedia.colorWithHexString("#579e11")
            //if (self.scoreResult >= 0.0) {
                //showColorOfScoreResult(scoreResult)
                //self.analyzingView.showScore(Int(self.scoreResult), showAnimation: false)
            //}
            
        } else {
            print("play")
            self.btnPlay.setBackgroundImage(UIImage(named: "ic_close.png"), forState: UIControlState.Normal)
            self.btnPlay.backgroundColor = Multimedia.colorWithHexString("#ff3333")
            print(getTmpFilePath())
            playSound(getTmpFilePath())
        }

        
    }
    
    func getTmpFilePath() -> NSURL
    {
        return NSURL(fileURLWithPath: FileHelper.getFilePath("\(fileName).\(fileType)"))
    }
    
    @IBAction func btnPlayDemoTouchUp(sender: AnyObject) {
        let linkFile:String = wordSelected.mp3Path
        if !linkFile.isEmpty {
            print("link mp3: " + linkFile)
            //playSound(LinkFile)
            HttpDownloader.loadFileSync(NSURL(string: linkFile)!, completion: { (path, error) -> Void in
                self.playSound(NSURL(fileURLWithPath: path))
            })
        }
    }
    
    func playSound(fileUrl: NSURL) {
        print("run in play")
        if self.player.isPlaying{
            self.player.pause()
        }
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


    
    @IBAction func clickMenuButton(sender: AnyObject) {
        //self.performSegueWithIdentifier("DetailScreenGoToMain", sender: self)
        self.navigationController?.popToRootViewControllerAnimated(true)
    }
    
    
}
