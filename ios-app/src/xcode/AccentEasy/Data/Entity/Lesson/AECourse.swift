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
    var selectedCourse: AECourse!
    
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

public class AECourseResponse: AELiteEntity, Mappable {
    var status: Bool!
    var message: String!
    var data: Array<AECourse>!
    
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
        status    <= map["status"]
        message   <= map["message"]
        data      <= map["data"]
    }
}

public class AECourse: AELiteEntity, Mappable {
    
    var name: String!
    var version: Int!
    var imageURL: String!
    var dbURL: String!
    
    var active: Bool = true
    var score: Int!
    
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
        idString    <= map["idCourse"]
        name   <= map["name"]
        version      <= map["version"]
        imageURL  <= map["urlImage"]
        dbURL  <= map["urlDownload"]
    }
    
}