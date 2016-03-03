//
//  HistoryTableController.swift
//  AccentEasy
//
//  Created by Hai Lu on 04/02/2016.
//  Copyright © 2016 Hai Lu. All rights reserved.
//

import Foundation

class HistoryTableController: UITableViewController {
    
    class HistoryRow {
        var word: String!
        var score: Int!
        
        init(_word: String!, _score: Int!) {
            self.word = _word
            self.score = _score
        }
    }
    
    let histories = [
        HistoryRow(_word: "hello", _score: 99),
         HistoryRow(_word: "hello", _score: 15),
         HistoryRow(_word: "love", _score: 45),
         HistoryRow(_word: "fun", _score: 52),
         HistoryRow(_word: "table", _score: 25),
         HistoryRow(_word: "nothing", _score: 18),
         HistoryRow(_word: "king", _score: 95),
         HistoryRow(_word: "group", _score: 88),
         HistoryRow(_word: "mail", _score: 56),
         HistoryRow(_word: "control", _score: 86),
         HistoryRow(_word: "recording", _score: 28),
         HistoryRow(_word: "load", _score: 75),
         HistoryRow(_word: "hello", _score: 15),
         HistoryRow(_word: "display", _score: 86),
         HistoryRow(_word: "accent", _score: 34),
         HistoryRow(_word: "accent", _score: 80),
         HistoryRow(_word: "think", _score: 73),
         HistoryRow(_word: "nothing", _score: 81),
         HistoryRow(_word: "run", _score: 87),
         HistoryRow(_word: "less", _score: 98),
         HistoryRow(_word: "but", _score: 100),
         HistoryRow(_word: "edit", _score: 77),
         HistoryRow(_word: "lift", _score: 100),
         HistoryRow(_word: "you", _score: 100),
         HistoryRow(_word: "hello", _score: 15),
        HistoryRow(_word: "hello", _score: 99),
        HistoryRow(_word: "hello", _score: 15),
        HistoryRow(_word: "love", _score: 45),
        HistoryRow(_word: "fun", _score: 52),
        HistoryRow(_word: "table", _score: 25),
        HistoryRow(_word: "nothing", _score: 18),
        HistoryRow(_word: "king", _score: 95),
        HistoryRow(_word: "group", _score: 88),
        HistoryRow(_word: "mail", _score: 56),
        HistoryRow(_word: "control", _score: 86),
        HistoryRow(_word: "recording", _score: 28),
        HistoryRow(_word: "load", _score: 75),
        HistoryRow(_word: "hello", _score: 15),
        HistoryRow(_word: "display", _score: 86),
        HistoryRow(_word: "accent", _score: 34),
        HistoryRow(_word: "accent", _score: 80),
        HistoryRow(_word: "think", _score: 73),
        HistoryRow(_word: "nothing", _score: 81),
        HistoryRow(_word: "run", _score: 87),
        HistoryRow(_word: "less", _score: 98),
        HistoryRow(_word: "but", _score: 100),
        HistoryRow(_word: "edit", _score: 77),
        HistoryRow(_word: "lift", _score: 100),
        HistoryRow(_word: "you", _score: 100),
        HistoryRow(_word: "hello", _score: 15)
    ]
    
    var historyList: Array<PronunciationScore>!
    
    var freestyleDBAdapter: FreeStyleDBAdapter!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        freestyleDBAdapter = FreeStyleDBAdapter(dbFile: DatabaseHelper.getFreeStyleDatabaseFile()!)
        do {
            try historyList = freestyleDBAdapter.listPronunciationScore(0)
        } catch {
            
        }
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "loadList:",name:"load", object: nil)
        self.tableView.allowsSelection = false
    }
    
    func loadList(notification: NSNotification){
        //load data here
        do {
            try historyList = freestyleDBAdapter.listPronunciationScore(0)
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