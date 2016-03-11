//
//  JSONHelper.swift
//  AccentEasy
//
// To Simply JSON process
//  Created by Hai Lu on 3/9/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

public class JSONHelper {
    
    class func fromJson<T: Mappable>(source: String) -> T {
        return Mapper<T>().map(source)!
    }
    
    class func toJson<T: Mappable>(obj: T, optional prettyPrint: Bool = false) -> String {
        return Mapper().toJSONString(obj, prettyPrint: prettyPrint)!
    }
    
}