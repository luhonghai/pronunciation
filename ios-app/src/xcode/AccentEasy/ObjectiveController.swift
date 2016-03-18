//
//  ObjectiveController.swift
//  AccentEasy
//
//  Created by Hai Lu on 17/03/2016.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//
import UIKit

class ObjectiveController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    @IBOutlet weak var scoreTest: UIView!
    
    @IBOutlet weak var tableView: UITableView!
    
    @IBOutlet weak var testContainer: UIView!
    
    @IBOutlet weak var lblTestScore: UILabel!
    
    var selectedLevel: AELevel!
    
    var selectedCountry: AECountry!
    
    var adapter = WordCollectionDbApdater()
    
    var objectives = Array<AEObjective>()
    
    override func viewDidLoad() {
        
        scoreTest.layer.cornerRadius = scoreTest.frame.width / 2
        testContainer.layer.cornerRadius = 5
        
        lblTestScore.layer.cornerRadius = lblTestScore.frame.width / 2
        lblTestScore.layer.masksToBounds = true
        tableView.delegate = self
        tableView.dataSource = self
        do {
            try objectives = adapter.getObjective(selectedCountry.idString, levelId: selectedLevel.idString)
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
        print("Number of objective \(objectives.count)")
        return objectives.count
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let level = objectives[indexPath.row]
        let identifier = "levelRowCell"
        var cell: LessonTableViewCell! = tableView.dequeueReusableCellWithIdentifier(identifier) as! LessonTableViewCell
        cell.lblScore.layer.cornerRadius = cell.lblScore.frame.width / 2
        cell.lblScore.layer.masksToBounds = true
        cell.bg.layer.cornerRadius = 5
        let tapGusture = UITapGestureRecognizer(target: self, action: Selector("tapItem:"))
        tapGusture.numberOfTapsRequired = 1
        cell.bg.tag = indexPath.row
        cell.bg.addGestureRecognizer(tapGusture)
        
        cell?.lblTitle.text = level.name
        //
        var bgColor = ColorHelper.APP_LIGHT_GRAY
        var titleColor = ColorHelper.APP_GRAY
        var bgScoreColor = ColorHelper.APP_GRAY
//        if indexPath.row == 0 || indexPath.row == 1 {
            bgColor = ColorHelper.APP_LIGHT_AQUA
            titleColor = ColorHelper.APP_AQUA
//        }
        cell.bg.backgroundColor = bgColor
        cell.lblTitle.textColor = titleColor
        cell.lblScore.backgroundColor = bgScoreColor
        cell.lblScore.text = ""
        return cell
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        //let level = levels[indexPath.row]
    }
    
    func tapItem(sender: UITapGestureRecognizer) {
        let row: Int = sender.view!.tag
        print("Select row id \(row)")
        let obj = objectives[row]
        let nextController = self.storyboard?.instantiateViewControllerWithIdentifier("LessonCollectionController") as! LessonCollectionController
        nextController.selectedLevel = selectedLevel
        nextController.selectedCountry = selectedCountry
        nextController.selectedObjective = obj
        self.navigationController?.pushViewController(nextController, animated: true)
    }
}

