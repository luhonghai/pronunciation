//
//  ObjectiveController.swift
//  AccentEasy
//
//  Created by Hai Lu on 17/03/2016.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//
import UIKit

class ObjectiveController: UIViewController, UITableViewDataSource, UITableViewDelegate, LessonTipPopupVCDelegate {
    
    
    @IBOutlet weak var scoreTest: AnalyzingView!
    
    @IBOutlet weak var tableView: UITableView!
    
    @IBOutlet weak var testContainer: UIView!
    
    @IBOutlet weak var lblTestScore: UILabel!
    
    var selectedLevel: AELevel!
    
    var selectedCountry: AECountry!
    
    var adapter = WordCollectionDbApdater()
    
    var objectiveScore = ObjectiveScore()
    
    var lessonDBAdapter = LessonDBAdapter()
    
    var objectives = Array<AEObjective>()
    
    var tests = [AETest]()
    
    var lessionCollections = Array<AELessonCollection>()
    
    var testScore = TestScore()
    
    override func viewDidLoad() {
        
        scoreTest.layer.cornerRadius = scoreTest.frame.width / 2
        scoreTest.switchType(.DISABLE)
        testContainer.layer.cornerRadius = 5
        
        lblTestScore.layer.cornerRadius = lblTestScore.frame.width / 2
        lblTestScore.layer.masksToBounds = true
        tableView.delegate = self
        tableView.dataSource = self
        
        navigationItem.title = selectedLevel.name
        //self.navigationItem.title.
        
        //var rectForNameLabel = CGRect(x: 0, y: 63, width: 190, height: 30)
        //var listNameLabel = UILabel(frame: rectForNameLabel)
        //listNameLabel.text = "Name of the List...."
        //listNameLabel.textAlignment = NSTextAlignment.Center
        //listNameLabel.font = UIFont(name: "HelveticaNeue-Bold", size: 15)
        
        
        //let firstFrame = CGRect(x: 0, y: 0, width: (self.navigationController?.navigationBar.frame.width)!/2, height: (self.navigationController?.navigationBar.frame.height)!)
        //let secondFrame = CGRect(x: (self.navigationController?.navigationBar.frame.width)!/2, y: 0, width: (self.navigationController?.navigationBar.frame.width)!/2, height: (self.navigationController?.navigationBar.frame.height)!)
        
        //print ((self.navigationController?.navigationBar.frame.width)!/1)
        //print((self.navigationController?.navigationItem.leftBarButtonItem?.width)!/1)
        //print((self.navigationController?.navigationItem.rightBarButtonItem?.width)!/1)
        
        
        //let barWidthContent = (self.navigationController?.navigationBar.frame.width)! - (self.navigationController?.navigationItem.leftBarButtonItem?.width)! - (self.navigationController?.navigationItem.rightBarButtonItem?.width)!
        
        //let titleFrame = CGRect(x:(self.navigationController?.navigationItem.leftBarButtonItem?.width)!, y:0, width: barWidthContent, height: (self.navigationController?.navigationBar.frame.height)!)
        
        //let firstLabel = UILabel(frame: firstFrame)
        //firstLabel.text = "First"
        
        //let secondLabel = UILabel(frame: secondFrame)
        //secondLabel.text = "Second"
        
        //var titleLable = UILabel(frame: titleFrame)
        //titleLable.text = "Name of the List"
        //titleLable.textAlignment = NSTextAlignment.Right
        //self.navigationController?.navigationBar.addSubview(titleLable)
        
        setNavigationBarTransparent()
        
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "showPopupObj:",name:"showPopupObj", object: nil)
        
    }
    
    override func viewDidAppear(animated: Bool) {
        scoreTest.refreshLayout()
    }
    
    func showPopupObj(notification: NSNotification) {
        let lessonTipPopupVC:LessonTipPopupVC = LessonTipPopupVC(nibName: "LessonTipPopupVC", bundle: nil)
        lessonTipPopupVC.contentPopup = "Take the test to progress to the next level or you can return to any of the lessons for more practise"
        lessonTipPopupVC.delegate = self
        self.presentpopupViewController(lessonTipPopupVC, animationType: .Fade, completion: {() -> Void in })
    }
    
    func closeLessonTipPopup(sender: LessonTipPopupVC) {
        self.dismissPopupViewController(.Fade)
    }
    
    func setNavigationBarTransparent() {
        self.navigationController?.navigationBar.setBackgroundImage(UIImage(), forBarMetrics: UIBarMetrics.Default)
        self.navigationController?.navigationBar.shadowImage = UIImage()
        self.navigationController?.navigationBar.translucent = true
        self.navigationController?.view.backgroundColor = UIColor.clearColor()
    }
    
    override func viewWillAppear(animated: Bool) {
        loadData()
    }
    
    func loadData() {
        do {
            try objectives = adapter.getObjective(selectedCountry.idString, levelId: selectedLevel.idString)
            
            try tests = adapter.getTest(selectedCountry.idString, levelId: selectedLevel.idString)
            print(tests)
            
            //set value for testScore obj
            testScore.username = AccountManager.currentUser().username
            testScore.idCountry = selectedCountry.idString
            testScore.idLevel = selectedLevel.idString
            testScore.passScore = Int((tests[0].percentPass))
            
            
            //set value for objectiveScore obj
            objectiveScore.username = AccountManager.currentUser().username
            objectiveScore.idCountry = selectedCountry.idString
            objectiveScore.idLevel = selectedLevel.idString
            //var arrObjScore = [ObjectiveScore]()
            
            
            //get obj score
            for objective in objectives {
                if let arrObjScore:[ObjectiveScore] = try lessonDBAdapter.getObjectiveScore(objectiveScore.username, idCountry: objectiveScore.idCountry, idLevel: objectiveScore.idLevel, idObjective: objective.idString){
                    
                    print(arrObjScore)
                    var arrScore = [Int]()
                    print("score lesson of \(objective.name)")
                    for obj in arrObjScore {
                        print(obj.score)
                        arrScore.append(obj.score)
                    }
                    if arrScore.average > 0 {
                        objective.score = arrScore.average
                    }
                }
                
                print("score \(objective.name) is \(objective.score)")
            }
            
            //get test score
            if let tScore = try lessonDBAdapter.getTestScore(testScore.username, idCountry: testScore.idCountry, idLevel: testScore.idLevel) {
                if tScore.score > 0 {
                    //testScore.score = tScore.score
                    lblTestScore.text = String(tScore.score)
                    lblTestScore.backgroundColor = ColorHelper.returnColorOfScore(tScore.score)
                    
                    scoreTest.showScore(tScore.score, showAnimation: true)
                }
            }


            
            tableView.reloadData()
        } catch {
            
        }

    }
    
    
    @IBAction func clickBack(sender: AnyObject) {
        self.navigationController?.popViewControllerAnimated(true)
    }
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        Logger.log("Number of objective \(objectives.count)")
        return objectives.count
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let obj = objectives[indexPath.row]
        let identifier = "levelRowCell"
        var cell: LessonTableViewCell! = tableView.dequeueReusableCellWithIdentifier(identifier) as! LessonTableViewCell
        cell.lblScore.layer.cornerRadius = cell.lblScore.frame.width / 2
        cell.lblScore.layer.masksToBounds = true
        cell.bg.layer.cornerRadius = 5
        let tapGusture = UITapGestureRecognizer(target: self, action: Selector("tapItem:"))
        tapGusture.numberOfTapsRequired = 1
        cell.bg.tag = indexPath.row
        cell.bg.addGestureRecognizer(tapGusture)
        
        cell?.lblTitle.text = obj.name
        if let score = obj.score {
            cell?.lblScore.text = String(score)
            cell?.lblScore.backgroundColor = ColorHelper.returnColorOfScore(score)
        } else {
            cell?.lblScore.text = ""
            cell?.lblScore.backgroundColor = ColorHelper.APP_GRAY
        }

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
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        //let level = levels[indexPath.row]
    }
    
    func tapItem(sender: UITapGestureRecognizer) {
        let row: Int = sender.view!.tag
        Logger.log("Select row id \(row)")
        //let obj = objectives[row]
        let nextController = self.storyboard?.instantiateViewControllerWithIdentifier("LessonCollectionController") as! LessonCollectionController
        nextController.selectedLevel = selectedLevel
        nextController.selectedCountry = selectedCountry
        //nextController.selectedObjective = obj
        nextController.objectives = objectives
        nextController.indexObjectiveSelected = row
        self.navigationController?.pushViewController(nextController, animated: true)
    }
    
    @IBAction func testTapped(sender: AnyObject) {
        let obj = tests[0]
        print("test name is \(obj.name)")
        try! lessionCollections = adapter.getLessonCollectionByTest(testScore.idCountry, levelId: testScore.idLevel, testId: obj.idString)
        
        
        let nextController = self.storyboard?.instantiateViewControllerWithIdentifier("LessonMainVC") as! LessonMainVC
        
        nextController.testScore = testScore
        nextController.selectedLessonCollection = lessionCollections[0]
        nextController.isLessonCollection = false
        nextController.selectedLevel = selectedLevel
        nextController.selectedCountry = selectedCountry
        nextController.selectedTest = obj
        
        self.navigationController?.pushViewController(nextController, animated: true)
    }
    
}

