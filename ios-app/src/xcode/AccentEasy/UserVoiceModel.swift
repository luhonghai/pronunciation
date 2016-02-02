//
//  UserVoiceModel.swift
//  AccentEasy
//
//  Created by CMGVN on 2/2/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import Foundation
import ObjectMapper

public class UserVoiceModel: Mappable{
    public var id:String!
    public var username:String!
    public var nativeEnglish:Bool!
    public var gender:Bool!
    public var dob:String!
    public var country:String!
    public var englishProficiency:Int!
    public var time:CLong!
    public var serverTime:CLong!
    public var duration:CLong!
    public var score:Float!
    public var latitude:Double!
    public var longitude:Double!
    public var uuid:String!
    public var word:String!
    public var recordFile:String!
    public var cleanRecordFile:String!
    public var phonemes:String!
    public var hypothesis:String!
    public var rawSphinxResult:String!
    public var version:Int!
    public var result:SphinxResult!
    public var serverDate:NSDate!
    public var versionPhoneme:Int!
    
    required public init?(_ map: Map) {

    }
    
    required public init(){
        
    }
    
    // Mappable
    public func mapping(map: Map) {
        id    <- map["id"]
        username   <- map["username"]
        nativeEnglish      <- map["nativeEnglish"]
        gender       <- map["gender"]
        dob  <- map["dob"]
        country  <- map["country"]
        englishProficiency     <- map["englishProficiency"]
        time    <- map["time"]
        serverTime    <- map["serverTime"]
        duration    <- map["duration"]
        score    <- map["score"]
        latitude    <- map["latitude"]
        longitude    <- map["longitude"]
        uuid    <- map["uuid"]
        word    <- map["word"]
        recordFile    <- map["recordFile"]
        cleanRecordFile    <- map["cleanRecordFile"]
        phonemes    <- map["phonemes"]
        hypothesis    <- map["hypothesis"]
        rawSphinxResult    <- map["rawSphinxResult"]
        version    <- map["version"]
        result    <- map["result"]
        serverDate    <- map["serverDate"]
        versionPhoneme    <- map["versionPhoneme"]
    }
    
}