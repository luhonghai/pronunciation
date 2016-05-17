//
//  AvgPhonemeScore.swift
//  AccentEasy
//
//  Created by Hai Lu on 13/05/2016.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

public class AvgPhonemeScore: Mappable {
    var status: Bool?
    var message: String?
    var data: [String: Int]?
    
    required public init?(_ map: Map) {
        
    }
    
    required public init(){
        
    }
    
    // Mappable
    public func mapping(map: Map) {
        message <= map["message"]
        status   <= map["status"]
        data <= map["data"]
    }
}