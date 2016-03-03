//
//  HistoryTableCell.swift
//  AccentEasy
//
//  Created by CMG on 04/02/2016.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

public class HistoryTableCell: UITableViewCell {
    
    var pc : PronunciationScore!
    
    @IBOutlet weak var btnPlay: UIButton!
    
    @IBOutlet weak var btnUp: UIButton!
    
    @IBOutlet weak var lblTitle: UILabel!
    
    @IBOutlet weak var lblScore: UILabel!
    
    @IBAction func btnUpTouchUpInside(sender: AnyObject) {
        NSNotificationCenter.defaultCenter().postNotificationName("loadWord", object: pc.word)
    }
    
    @IBAction func btnPlayTouchUpInside(sender: AnyObject) {
        NSNotificationCenter.defaultCenter().postNotificationName("playFile", object: FileHelper.getFilePath("audio/\(pc.dataId).wav"))
    }
    
    public func applyCircleButton() {
        btnPlay.layer.cornerRadius = btnPlay.frame.size.width/2
        btnPlay.clipsToBounds = true
        btnUp.layer.cornerRadius = btnUp.frame.size.width/2
        btnUp.clipsToBounds = true
    }
}