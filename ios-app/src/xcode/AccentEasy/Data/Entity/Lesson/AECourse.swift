//
//  AECourse.swift
//  AccentEasy
//
//  Created by Hai Lu on 4/25/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

public class AECourseSession: Mappable {
    
    var courses = Array<AECourse>()
    var username: String!
    
    required public init?(_ map: Map) {
        
    }
    
    required public init(){
        
    }
    
    // Mappable
    public func mapping(map: Map) {
        courses    <= map["courses"]
        username <= map["username"]
    }
}

public class AECourse: AELiteEntity, Mappable {
    
    var name: String!
    var version: Int!
    var imageURL: String!
    var dbURL: String!
    
    required public init(){
        super.init()
    }
    
    required public init?(_ map: Map) {
        super.init()
    }
    
    public required init(id: Int64) {
        super.init(id: id)
    }
    
    // Mappable
    public func mapping(map: Map) {
        idString    <= map["idString"]
        name   <= map["name"]
        version      <= map["version"]
        imageURL  <= map["imageURL"]
        dbURL  <= map["dbURL"]
    }
    
}