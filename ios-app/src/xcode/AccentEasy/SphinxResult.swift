//
//  SphinxResult.swift
//  AccentEasy
//
//  Created by CMGVN on 2/2/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation
import Darwin

public class SphinxResult: Mappable{
    
    public var score:Float!
    public var correctPhonemes = [String]()
    public var bestPhonemes = [Phoneme]()
    public var rawBestPhonemes = [String]()
    public var phonemeScores = [PhonemeScore]()

    public class Phoneme: Mappable {
        public var name:String!
        public var count:Int!
        
        required public init?(_ map: Map) {
            
        }
        
        required public init(){
            
        }
        
        // Mappable
        public func mapping(map: Map) {
            name    <= map["name"]
            count   <= map["count"]
        }
    }
    
    public class PhonemeScore: Mappable {
        public var name:String!
        public var index:Int!
        public var totalScore:Float!
        public var time:Double!
        public var username:String!
        public var version:Int!
        public var userVoiceId:String!
        public var phonemes = [PhonemeScoreUnit]()
        public var ipa:String!
    
        required public init?(_ map: Map) {
            
        }
        
        required public init(){
            
        }
        
        // Mappable
        public func mapping(map: Map) {
            name    <= map["name"]
            index   <= map["index"]
            totalScore      <= map["totalScore"]
            time       <= map["time"]
            username  <= map["username"]
            version  <= map["version"]
            userVoiceId     <= map["userVoiceId"]
            phonemes     <= map["phonemes"]
            ipa     <= map["ipa"]
        }
    }
    
    public class PhonemeScoreUnit: Mappable {
        public static let NOT_MATCH:Int = 0
        public static let MATCHED:Int = 1
        public static let NEIGHBOR:Int = 2
        public static let BEEP_PHONEME:Int = 3
        
        public var index:Int!
        public var type:Int!
        public var name:String!
        public var count:Int!
        
        required public init?(_ map: Map) {
            
        }
        
        required public init(){
            
        }
        
        // Mappable
        public func mapping(map: Map) {
            index    <= map["index"]
            type   <= map["type"]
            name      <= map["name"]
            count       <= map["count"]
        }

        
    }
    
    required public init?(_ map: Map) {
        
    }
    
    required public init(){
        
    }
    
    // Mappable
    public func mapping(map: Map) {
        score    <= map["score"]
        correctPhonemes   <= map["correctPhonemes"]
        bestPhonemes      <= map["bestPhonemes"]
        rawBestPhonemes       <= map["rawBestPhonemes"]
        phonemeScores  <= map["phonemeScores"]
    }

    
}