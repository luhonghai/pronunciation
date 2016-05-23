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
    public static let reject:String = "reject"
    public static let pending:String = "pending"
}

public class InvitationData: Mappable{
    public var id:String!
    public var studentName:String!
    public var teacherName:String!
    public var firstTeacherName:String!
    public var lastTeacherName:String!
    public var companyName:String!
    public var status:String!
    
    /*
     @PrimaryKey
     private String id;
     
     @Persistent
     private String studentName;
     
     @Persistent
     private String teacherName;
     
     @Persistent
     private String firstTeacherName;
     
     @Persistent
     private String lastTeacherName;
     
     @Persistent
     private boolean isDeleted;
     
     @Persistent
     private String mappingBy;
     
     @Persistent
     private String status;
     
     @Persistent
     private boolean licence;
     
     @Persistent
     private boolean isView;
     */
    
    public var groupId:Int!
    
    required public init?(_ map: Map) {
        
    }
    
    required public init(){
        
    }
    
    // Mappable
    public func mapping(map: Map) {
        id    <= map["id"]
        studentName    <= map["studentName"]
        teacherName    <= map["teacherName"]
        firstTeacherName    <= map["firstTeacherName"]
        lastTeacherName    <= map["lastTeacherName"]
        companyName    <= map["companyName"]
        status   <= map["status"]
    }
    
}


public class InvitationMessage: Mappable{
    public var type:Int!
    public var title:String!
    public var content:String!
    
    required public init?(_ map: Map) {
        
    }
    
    required public init(){
        
    }
    
    // Mappable
    public func mapping(map: Map) {
        type    <= map["type"]
        title    <= map["title"]
        content    <= map["content"]
    }
    
}