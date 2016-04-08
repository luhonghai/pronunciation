//
//  LessonCollectionController.swift
//  AccentEasy
//
//  Created by Hai Lu on 18/03/2016.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//


class LessonCollectionController: UIViewController, UITableViewDataSource, UITableViewDelegate, LessonTipPopupVCDelegate  {

    @IBOutlet weak var bgObjectiveHelp: UIView!
    
    @IBOutlet weak var btnObjectiveQuestion: UIButton!
    
    @IBOutlet weak var tableView: UITableView!
    
    var selectedLevel: AELevel!
    
    var selectedCountry: AECountry!
    
    var selectedObjective: AEObjective!
    
    var objectiveScore = ObjectiveScore()
    
    var lessonDBAdapter = LessonDBAdapter()
    
    var adapter = WordCollectionDbApdater()
    
    var lessionCollections = Array<AELessonCollection>()
    
    override func viewDidLoad() {
        bgObjectiveHelp.layer.cornerRadius = 5
        btnObjectiveQuestion.layer.cornerRadius = btnObjectiveQuestion.frame.width / 2
        tableView.dataSource = self
        tableView.delegate = self
        
        navigationItem.title = selectedLevel.name + "-" + selectedObjective.name
        //loadData()
        setNavigationBarTransparent()
    }
    
    func setNavigationBarTransparent() {
        self.navigationController?.navigationBar.setBackgroundImage(UIImage(), forBarMetrics: UIBarMetrics.Default)
        self.navigationController?.navigationBar.shadowImage = UIImage()
        self.navigationController?.navigationBar.translucent = true
        self.navigationController?.view.backgroundColor = UIColor.clearColor()
    }
    
    override func viewWillAppear(animated: Bool) {
        //print("viewDidAppear")
        loadData()
    }
    
    func loadData() {
        do {
            objectiveScore.username = AccountManager.currentUser().username
            objectiveScore.idCountry = selectedCountry.idString
            objectiveScore.idLevel = selectedLevel.idString
            objectiveScore.idObjective = selectedObjective.idString
            
            try lessionCollections = adapter.getLessonCollectionByObjective(selectedCountry.idString, levelId: selectedLevel.idString, objectiveId: selectedObjective.idString)
            
            
            for lessonCollection in lessionCollections {
                if let objScore = try lessonDBAdapter.getLessonScore(objectiveScore.username, idCountry: objectiveScore.idCountry, idLevel: objectiveScore.idLevel, idObjective: objectiveScore.idObjective, idLesson: lessonCollection.idString) {
                
                    
                    print("\(lessonCollection.title) co score la \(objScore.score)")
                    
                    if objScore.score > 0 {
                        lessonCollection.score = objScore.score
                    }
                    
                }
                
                
                
            }
            
            
            
            tableView.reloadData()
        } catch {
            
        }
    }
    
    //tip popup
    let lessonTipPopupVC:LessonTipPopupVC = LessonTipPopupVC(nibName: "LessonTipPopupVC", bundle: nil)
    var timer:NSTimer!
    
    @IBAction func objTipTouchUp(sender: AnyObject) {
        lessonTipPopupVC.delegate = self
        lessonTipPopupVC.contentPopup = selectedObjective.description
        lessonTipPopupVC.isShow = true
        
        clearTimer()
        timer = NSTimer.scheduledTimerWithTimeInterval(15, target: self, selector: Selector("closeLessonTip"), userInfo: nil, repeats: true)
        self.presentpopupViewController(lessonTipPopupVC, animationType: .Fade, completion: {() -> Void in })
    }
    
    func closeLessonTipPopup(sender: LessonTipPopupVC) {
        clearTimer()
        if (lessonTipPopupVC.isShow) {
            self.dismissPopupViewController(.Fade)
        }
    }
    
    func closeLessonTip() {
        clearTimer()
        if (lessonTipPopupVC.isShow) {
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
    
    @IBAction func tapObjectiveHelper(sender: AnyObject) {
        
    }
    
    @IBAction func clickBack(sender: AnyObject) {
        self.navigationController?.popViewControllerAnimated(true)
    }
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        Logger.log("Number of lesson \(lessionCollections.count)")
        return lessionCollections.count
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let obj = lessionCollections[indexPath.row]
        let identifier = "levelRowCell"
        var cell: LessonTableViewCell! = tableView.dequeueReusableCellWithIdentifier(identifier) as! LessonTableViewCell
        cell.lblScore.layer.cornerRadius = cell.lblScore.frame.width / 2
        cell.lblScore.layer.masksToBounds = true
        cell.bg.layer.cornerRadius = 5
        let tapGusture = UITapGestureRecognizer(target: self, action: Selector("tapItem:"))
        tapGusture.numberOfTapsRequired = 1
        cell.bg.tag = indexPath.row
        cell.bg.addGestureRecognizer(tapGusture)
        
        cell?.lblTitle.text = obj.title
        if let score = obj.score {
            cell?.lblScore.text = String(score)
            cell?.lblScore.backgroundColor = ColorHelper.returnColorOfScore(score)
        } else {
            cell?.lblScore.text = ""
            cell?.lblScore.backgroundColor = ColorHelper.APP_GRAY
        }
        
        print("run in cell, score is:" + String(obj.score))
        //
        var bgColor = ColorHelper.APP_LIGHT_GRAY
        var titleColor = ColorHelper.APP_GRAY
        //        if indexPath.row == 0 || indexPath.row == 1 {
        bgColor = ColorHelper.APP_LIGHT_AQUA
        titleColor = ColorHelper.APP_AQUA
        //        }
        cell.bg.backgroundColor = bgColor
        cell.lblTitle.textColor = titleColor
        
        return cell
    }
    
    func tapItem(sender: UITapGestureRecognizer) {
        let row: Int = sender.view!.tag
        Logger.log("Select row id \(row)")
        
        let obj = lessionCollections[row]
        let nextController = self.storyboard?.instantiateViewControllerWithIdentifier("LessonMainVC") as! LessonMainVC
        //nextController.selectedLevel = selectedLevel
        //nextController.selectedCountry = selectedCountry
        //objectiveScore.idLesson = obj.idString
        
        nextController.objectiveScore = objectiveScore
        //nextController.selectedLessonCollection = obj
        nextController.lessionCollections = lessionCollections
        nextController.indexSelectedLesson = row
        nextController.isLessonCollection = true
        self.navigationController?.pushViewController(nextController, animated: true)
        
    }
}
