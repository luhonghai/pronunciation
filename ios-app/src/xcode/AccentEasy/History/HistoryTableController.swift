//
//  HistoryTableController.swift
//  AccentEasy
//
//  Created by Hai Lu on 04/02/2016.
//  Copyright © 2016 Hai Lu. All rights reserved.
//

import Foundation

class HistoryTableController: UITableViewController {
    
    var historyList = Array<PronunciationScore>()
    
    var freestyleDBAdapter: FreeStyleDBAdapter!
    
    var isDetail = false
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        freestyleDBAdapter = FreeStyleDBAdapter()
//        do {
//            try historyList = freestyleDBAdapter.listPronunciationScore(0, username: Login.getCurrentUser().username)
//        } catch {
//            
//        }
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "loadList:",name:"loadHistory", object: nil)
        loadTable(GlobalData.getInstance().selectedWord)
        self.tableView.allowsSelection = false
    }
    
    func loadList(notification: NSNotification){
        //load data here
        let word = notification.object as? String
        loadTable(word)
    }
    
    override func viewDidAppear(animated: Bool) {
        self.tableView.setNeedsDisplay()
    }
    
    func loadTable(word: String?) {
        Logger.log("load history of word \(word)")
        do {
            if !word!.isEmpty {
                isDetail = true
                try historyList = freestyleDBAdapter.listPronunciationScoreByWord(word!, limit: 0, username: Login.getCurrentUser().username)
            } else {
                isDetail = false
                try historyList = freestyleDBAdapter.listPronunciationScore(0, username: Login.getCurrentUser().username)
            }
            
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
    
    func tapLabelTitle(sender: UITapGestureRecognizer) {
        let row: Int = sender.view!.tag
        Logger.log("Select row id \(row)")
        let historyItem = historyList[row]
        Logger.log("Select detail \(historyItem.word)")
        if historyItem.dataId != nil {
            if isDetail {
                NSNotificationCenter.defaultCenter().postNotificationName("selectDetail", object: historyItem.dataId)
            } else {
                NSNotificationCenter.defaultCenter().postNotificationName("showDetail", object: historyItem.dataId)
            }
        }
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("HistoryTableCell", forIndexPath: indexPath) as! HistoryTableCell
        
//        cell.translatesAutoresizingMaskIntoConstraints = true
        cell.isDetail = isDetail
        let history = historyList[indexPath.row]
        var color:UIColor = UIColor.clearColor()
        if history.score >= 80 {
            color = ColorHelper.APP_GREEN
        } else if history.score >= 45 {
            color = ColorHelper.APP_ORANGE
        } else {
            color = ColorHelper.APP_RED
        }
//        cell.lblTitle.setValue(history.dataId, forKey: "dataId")
        let tapGusture = UITapGestureRecognizer(target: self, action: Selector("tapLabelTitle:"))
        tapGusture.numberOfTapsRequired = 1
        cell.lblTitle.tag = indexPath.row
        cell.lblTitle.addGestureRecognizer(tapGusture)
        cell.lblTitle.userInteractionEnabled = true
        cell.pc = history
        cell.lblScore.textColor = color
        cell.lblTitle.textColor = color
        cell.btnPlay.backgroundColor = color
        cell.btnPlay.hidden = isDetail
        cell.btnUp.backgroundColor = color
        cell.btnUp.hidden = isDetail
        if isDetail {
            let styler = NSDateFormatter()
            styler.dateFormat = "HH:mm:ss dd/MM/yyyy"
            cell.lblTitle.text = styler.stringFromDate(NSDate(timeIntervalSince1970: history.time / 1000))
            cell.constantWidthPlay.constant = 0
            cell.constantWidthUp.constant = 0
        } else {
            cell.lblTitle.text = history.word
            cell.constantWidthPlay.constant = 36
            cell.constantWidthUp.constant = 36
        }
        cell.applyCircleButton()
        cell.lblScore.text = "\(history.score)%"
        return cell
    }
}