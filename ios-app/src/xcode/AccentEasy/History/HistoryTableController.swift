//
//  HistoryTableController.swift
//  AccentEasy
//
//  Created by Hai Lu on 04/02/2016.
//  Copyright © 2016 Hai Lu. All rights reserved.
//

import Foundation

class HistoryTableController: UITableViewController {
    
    var historyList: Array<PronunciationScore>!
    
    var freestyleDBAdapter: FreeStyleDBAdapter!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        freestyleDBAdapter = FreeStyleDBAdapter()
        do {
            try historyList = freestyleDBAdapter.listPronunciationScore(0, username: Login.getCurrentUser().username)
        } catch {
            
        }
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "loadList:",name:"load", object: nil)
        self.tableView.allowsSelection = false
    }
    
    func loadList(notification: NSNotification){
        //load data here
        do {
            try historyList = freestyleDBAdapter.listPronunciationScore(0, username: Login.getCurrentUser().username)
        } catch {
            
        }
        self.tableView.reloadData()
        self.tableView.setContentOffset(CGPoint.zero, animated: true)
    }
    
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return historyList.count
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("HistoryTableCell", forIndexPath: indexPath) as! HistoryTableCell
        cell.applyCircleButton()
        let history = historyList[indexPath.row]
        var color:UIColor = UIColor.clearColor()
        if history.score >= 80 {
            color = ColorHelper.APP_GREEN
        } else if history.score >= 45 {
            color = ColorHelper.APP_ORANGE
        } else {
            color = ColorHelper.APP_RED
        }
        cell.lblScore.textColor = color
        cell.lblTitle.textColor = color
        cell.btnPlay.backgroundColor = color
        cell.btnUp.backgroundColor = color
        cell.lblTitle.text = history.word
        cell.lblScore.text = "\(history.score)%"
        return cell
    }
}