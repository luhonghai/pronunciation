//
//  ResultParser.swift
//  AccentEasy
//
//  Created by CMGVN on 2/2/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

class RegisterResult: Mappable{
    var status:Bool!
    var message:String!
    
    required init?(_ map: Map) {
        
    }
    
    required init(){}
    
    // Mappable
    func mapping(map: Map) {
        status    <= map["status"]
        message   <= map["message"]
    }
}

class VoidModelResult: Mappable{
    var status:Bool!
    var message:String!
    var data = UserVoiceModel()
    
    required init?(_ map: Map) {
        
    }
    
    required init(){}
    
    // Mappable
    func mapping(map: Map) {
        status    <= map["status"]
        message   <= map["message"]
        data   <= map["data"]
    }
}