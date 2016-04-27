//
//  LevelControllerTVC.swift
//  AccentEasy
//
//  Created by CMGVN on 3/15/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class LevelControllerTVC: UITableViewController, LSPopupVCDelegate {

    @IBOutlet weak var menuButton: UIBarButtonItem!
    
    var levels = Array<AELevel>()
    
    var testScore = TestScore()
    
    var dbAdapter: WordCollectionDbApdater!
    
    var courseDbAdapter: CourseDBAdapter!
    
    var lessonDBAdapter = LessonDBAdapter()
    
    var userProfile:UserProfile!
    
    @IBAction func clickBack(sender: AnyObject) {
        self.navigationController?.popViewControllerAnimated(true)
    }
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
        
        
        dbAdapter = WordCollectionDbApdater()
        courseDbAdapter = CourseDBAdapter.newInstance()
        navigationItem.title = AccountManager.currentUser().courseSession.selectedCourse.name

        NSNotificationCenter.defaultCenter().addObserver(self, selector: "loadLevel:",name:"loadLevel", object: nil)
        
        DeviceManager.setNavigationBarTransparent(self)
    }
    
    override func viewWillAppear(animated: Bool) {
        userProfile = AccountManager.currentUser()
     //   print(userProfile)
     //   print(userProfile.selectedCountry.name)
        loadTableData()
    }
    
    func loadLevel(notification: NSNotification){
        loadTableData()
    }
    
    func loadTableData() {
        userProfile = AccountManager.currentUser()
      //  if let country = userProfile.selectedCountry {
            do {
                testScore.username = AccountManager.currentUser().username
                testScore.idCountry = AccountManager.currentUser().getSelectedCourseId()
                
                levels = try courseDbAdapter.getLevelByCountry()
                Logger.log("Number of level \(levels.count)")
                
                //print(DatabaseHelper.getLessonUserScoreDatabaseFile())
                
                //get test score
                /*for level in levels{
                    if let tScore = try lessonDBAdapter.getTestScore(testScore.username, idCountry: testScore.idCountry, idLevel: level.idString) {
                        if tScore.score > 0 {
                            level.score = tScore.score
                            if (tScore.isLevelPass != nil && tScore.isLevelPass) {
                                level.active = true
                            }
                        }
                    }
                }*/
                
                for index in 0...levels.count - 1 {
                    if let tScore = try lessonDBAdapter.getTestScore(testScore.username, idCountry: testScore.idCountry, idLevel: levels[index].idString) {
                        if tScore.score > 0 {
                            levels[index].score = tScore.score
                            if (tScore.isLevelPass != nil && tScore.isLevelPass! && index<levels.count-1) {
                                levels[index+1].active = true
                            }
                        }
                    }
                    //TODO should check if activate
                    levels[index].active = true
                }
                
                self.tableView.reloadData()
                
            } catch {
                
            }
      //  }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        Logger.log("Number of level \(levels.count)")
        return levels.count
    }
    
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let level = levels[indexPath.row]
        let identifier = "levelRowCell"
        let cell: LessonTableViewCell! = tableView.dequeueReusableCellWithIdentifier(identifier) as! LessonTableViewCell
        cell.lblScore.layer.cornerRadius = cell.lblScore.frame.width / 2
        cell.lblScore.layer.masksToBounds = true
        cell.bg.layer.cornerRadius = 5
        let tapGusture = UITapGestureRecognizer(target: self, action: Selector("tapItem:"))
        tapGusture.numberOfTapsRequired = 1
        if cell.bg.tag == 0 {
            cell.bg.addGestureRecognizer(tapGusture)
        }
        cell.bg.tag = indexPath.row + 1
        
        
        cell?.lblTitle.text = level.name
        if let score = level.score {
            cell?.lblScore.text = String(score)
            cell?.lblScore.backgroundColor = ColorHelper.returnColorOfScore(score)
        } else {
            cell?.lblScore.text = ""
            cell?.lblScore.backgroundColor = ColorHelper.APP_GRAY
        }

        //
        var bgColor = ColorHelper.APP_LIGHT_GRAY
        var titleColor = ColorHelper.APP_GRAY
        if
            level.getBoolValue(level.isDemo)
            || level.getBoolValue(level.isDefaultActivated)
            || level.active {
            bgColor = ColorHelper.APP_LIGHT_AQUA
            titleColor = ColorHelper.APP_AQUA
        }
        
        
        cell.bg.backgroundColor = bgColor
        cell.lblTitle.textColor = titleColor
        return cell
    }
    
    func tapItem(sender: UITapGestureRecognizer) {
        let row: Int = sender.view!.tag - 1
        Logger.log("Select row id \(row)")
        let level = levels[row]
        
        if
            level.getBoolValue(level.isDemo)
                || level.getBoolValue(level.isDefaultActivated)
                || level.active {
                    let nextController = self.storyboard?.instantiateViewControllerWithIdentifier("ObjectiveController") as! ObjectiveController
                    nextController.selectedLevel = level
                    nextController.selectedCountry = AccountManager.currentUser().selectedCountry
                    
                    self.navigationController?.pushViewController(nextController, animated: true)
        }
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        let level = levels[indexPath.row]
        let ipaChart = self.storyboard?.instantiateViewControllerWithIdentifier("ObjectiveController") as! ObjectiveController
        ipaChart.selectedLevel = level
        ipaChart.selectedCountry = AccountManager.currentUser().selectedCountry
        self.navigationController?.pushViewController(ipaChart, animated: true)
    }
    
    func displayViewController(animationType: SLpopupViewAnimationType) {
        
        let lSPopupVC:LSPopupVC = LSPopupVC(nibName:"LSPopupVC", bundle: nil)
        
        lSPopupVC.delegate = self
        self.presentpopupViewController(lSPopupVC, animationType: animationType, completion: { () -> Void in
            
        })
    }
    
    func closePopup(sender: AnyObject) {
        self.dismissPopupViewController(.Fade)
    }

    

}
