//
//  InvitationData.swift
//  AccentEasy
//
//  Created by CMGVN on 4/26/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

public class InvitationStatus {
    public static let accept:String = "accept"
    public static let delete:String = "delete"
    public static let pending:String = "pending"
}

public class InvitationData: Mappable{
    public var name:String!
    public var status:String!
    
    required public init?(_ map: Map) {
        
    }
    
    required public init(){
        
    }
    
    // Mappable
    public func mapping(map: Map) {
        name    <= map["name"]
        status   <= map["status"]
    }
    
}