//
//  CoursesViewController.swift
//  AccentEasy
//
//  Created by Hai Lu on 4/26/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit
import ImageLoader

class CourseTableViewCell: LessonTableViewCell {
    
     @IBOutlet weak var imgCourse: UIImageView!
}

class CoursesViewController: UITableViewController {
    
    @IBOutlet weak var menuButton: UIBarButtonItem!
    
    var userProfile: UserProfile!
    
    var courses: Array<AECourse>!
    
    var lessonScoreDbApdater: LessonDBAdapter!

    override func viewDidLoad() {
        super.viewDidLoad()
        if self.revealViewController() != nil {
            menuButton.target = self.revealViewController()
            menuButton.action = "revealToggle:"
            self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
        }
        lessonScoreDbApdater = LessonDBAdapter()
        // Do any additional setup after loading the view.
        courses = AccountManager.currentUser().courseSession.courses
        DeviceManager.setNavigationBarTransparent(self)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "loadLevel:",name:"loadLevel", object: nil)
        if let user:UserProfile = AccountManager.currentUser() {
            if user.selectedCountry == nil {
                user.selectedCountry = try! WordCollectionDbApdater().getDefaultCountry()
                AccountManager.updateProfile(user)
            }
        }
    }
    
    func loadLevel(notification: NSNotification){
        loadTableData()
    }
    
    override func viewWillAppear(animated: Bool) {
        loadTableData()
    }

    func loadTableData() {
        Logger.log("reload course table")
        userProfile = AccountManager.currentUser()
        self.tableView.reloadData()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        Logger.log("Number of course \(courses.count)")
        return courses.count
    }
    
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let course = courses[indexPath.row]
        let score = lessonScoreDbApdater.getCourseScore(AccountManager.currentUser().username, idCourse: course.idString)
        if score >= 0 {
            course.score = score
        }
        let identifier = "courseRowCell"
        let cell: CourseTableViewCell! = tableView.dequeueReusableCellWithIdentifier(identifier) as! CourseTableViewCell
        cell.lblScore.layer.cornerRadius = cell.lblScore.frame.width / 2
        cell.lblScore.layer.masksToBounds = true
        cell.imgCourse.layer.cornerRadius = cell.imgCourse.frame.width / 2
        cell.imgCourse.layer.masksToBounds = true
        cell.bg.layer.cornerRadius = 5
        let tapGusture = UITapGestureRecognizer(target: self, action: Selector("tapItem:"))
        tapGusture.numberOfTapsRequired = 1
        if cell.bg.tag == 0 {
            cell.bg.addGestureRecognizer(tapGusture)
        }
        cell.bg.tag = indexPath.row + 1
        
        cell?.lblTitle.text = course.name
        if let score = course.score {
            cell?.lblScore.text = String(score)
            cell?.lblScore.backgroundColor = ColorHelper.returnColorOfScore(score)
            cell?.imgCourse.hidden = true
            cell?.lblScore.hidden = false
        } else {
            if course.imageURL != nil && !course.imageURL.isEmpty {
                cell?.imgCourse.hidden = false
                cell?.lblScore.hidden = true
                cell?.imgCourse.load(NSURL(string: course.imageURL)!)
            } else {
                cell?.imgCourse.hidden = true
                cell?.lblScore.hidden = false
                cell?.lblScore.text = ""
                cell?.lblScore.backgroundColor = ColorHelper.APP_GRAY
            }
        }
        
        //
        var bgColor = ColorHelper.APP_LIGHT_GRAY
        var titleColor = ColorHelper.APP_GRAY
        if course.active {
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
        let course = courses[row]
        if course.active {
            let user = AccountManager.currentUser()
            user.courseSession.selectedCourse = course
            AccountManager.updateProfile(user)
            let levelController = self.storyboard?.instantiateViewControllerWithIdentifier("LevelControllerTVC") as! LevelControllerTVC
            self.navigationController?.pushViewController(levelController, animated: true)
        }
    }
}
