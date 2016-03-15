//
//  Global.swift
//  AccentEasy
//
//  Created by Hai Lu on 3/3/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

let IS_DEBUG = false

let DEFAULT_ERROR_MESSAGE = "sorry our engineers are just upgrading the server, please try again"

class GlobalData {
    static var instance:GlobalData?
    
    var selectedWord = ""
    
    var isShowLogin = false
    
    static func getInstance() -> GlobalData {
        if (instance == nil) {
            instance = GlobalData()
        }
        return instance!
    }
}

func delay(delay:Double, closure:()->()) {
    dispatch_after(
        dispatch_time(
            DISPATCH_TIME_NOW,
            Int64(delay * Double(NSEC_PER_SEC))
        ),
        dispatch_get_main_queue(), closure)
}