//
//  LessonCollectionController.swift
//  AccentEasy
//
//  Created by Hai Lu on 18/03/2016.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//


class LessonCollectionController: UIViewController, UITableViewDataSource, UITableViewDelegate  {

    @IBOutlet weak var bgObjectiveHelp: UIView!
    
    @IBOutlet weak var btnObjectiveQuestion: UIButton!
    
    @IBOutlet weak var tableView: UITableView!
    
    var selectedLevel: AELevel!
    
    var selectedCountry: AECountry!
    
    var selectedObjective: AEObjective!
    
    var adapter = WordCollectionDbApdater()
    
    var lessionCollections = Array<AELessonCollection>()
    
    override func viewDidLoad() {
        bgObjectiveHelp.layer.cornerRadius = 5
        btnObjectiveQuestion.layer.cornerRadius = btnObjectiveQuestion.frame.width / 2
        tableView.dataSource = self
        tableView.delegate = self
        loadData()
    }
    
    func loadData() {
        do {
            try lessionCollections = adapter.getLessonCollectionByObjective(selectedCountry.idString, levelId: selectedLevel.idString, objectiveId: selectedObjective.idString)
            tableView.reloadData()
        } catch {
            
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
        
        cell?.lblTitle.text = obj.name
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
    
    func tapItem(sender: UITapGestureRecognizer) {
        let row: Int = sender.view!.tag
        Logger.log("Select row id \(row)")
        
    }
}
