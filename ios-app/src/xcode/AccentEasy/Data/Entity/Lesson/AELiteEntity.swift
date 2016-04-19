//
//  AELiteEntity.swift
//  AccentEasy
//
//  Created by Hai Lu on 17/03/2016.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

public class AELiteEntity : LiteEntity {
    var idString: String!
    
    func getBoolValue(value: String?) -> Bool {
       // Logger.log("bool value \(value)")
        return value != nil && value! != "\\0"
    }
}