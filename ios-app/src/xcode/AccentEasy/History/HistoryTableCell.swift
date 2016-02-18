//
//  HistoryTableCell.swift
//  AccentEasy
//
//  Created by CMG on 04/02/2016.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import Foundation

public class HistoryTableCell: UITableViewCell {
    
    
    @IBOutlet weak var btnPlay: UIButton!
    
    @IBOutlet weak var btnUp: UIButton!
    
    @IBOutlet weak var lblTitle: UILabel!
    
    @IBOutlet weak var lblScore: UILabel!
    
    @IBAction func btnUpTouchUpInside(sender: AnyObject) {
    }
    
    @IBAction func btnPlayTouchUpInside(sender: AnyObject) {
    }
    
    public func applyCircleButton() {
        btnPlay.layer.cornerRadius = btnPlay.frame.size.width/2
        btnPlay.clipsToBounds = true
        btnUp.layer.cornerRadius = btnUp.frame.size.width/2
        btnUp.clipsToBounds = true
    }
}