//
//  PhonemeScore.swift
//  AccentEasy
//
//  Created by Hai Lu on 02/03/2016.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation
import Darwin

public class PhonemeScore: LiteEntity {
    public var name:String!
    public var index:Int!
    public var score:Int!
    public var time:Double!
    public var username:String!
    public var version:Int!
    public var dataId:String!
    public var ipa:String!
    
    required public init(){
        super.init()
    }
    
    public required init(id: Int64) {
        super.init(id: id)
    }
    
    public class func parse(origin: SphinxResult.PhonemeScore) -> PhonemeScore {
        let obj = PhonemeScore()
        obj.name = origin.name
        obj.index = origin.index
        obj.score = Int(floor(origin.totalScore))
        obj.time = origin.time
        obj.username = origin.username
        obj.version = origin.version
        obj.dataId = origin.userVoiceId
        obj.ipa = origin.ipa
        return obj
    }
    
    public override func parse(row: Row) {
        self.name = row[LiteColumn.NAME]
        self.score = row[LiteColumn.SCORE]
        self.dataId = row[LiteColumn.DATA_ID]
        self.username = row[LiteColumn.USERNAME]
        self.version = row[LiteColumn.VERSION]
        self.time = row[LiteColumn.TIME]
        self.index = row[LiteColumn.INDEX]
        self.ipa = row[LiteColumn.IPA]
    }
    
    public override func setters() -> [Setter]? {
        return [
            LiteColumn.NAME <- self.name,
            LiteColumn.SCORE <- self.score,
            LiteColumn.DATA_ID <- self.dataId,
            LiteColumn.USERNAME <- self.username,
            LiteColumn.VERSION <- self.version,
            LiteColumn.TIME <- self.time,
            LiteColumn.INDEX <- self.index,
            LiteColumn.IPA <- self.ipa
        ]
        
    }
    
    public override func table() -> Table? {
        return LiteTable.PHONEME_SCORE
    }
}