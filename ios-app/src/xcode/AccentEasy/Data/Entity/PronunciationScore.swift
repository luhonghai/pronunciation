//
//  PronunciationScore.swift
//  AccentEasy
//
//  Created by Minh Kelly on 3/2/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import Foundation

public class PronunciationScore: LiteEntity {
    
    var word:String!
    var score:Int!
    var dataId:String!
    var username:String!
    var version:Int!
    var time:Double!
    
    override public func parse(row: Row) {
        self.word = row[LiteColumn.WORD]
        self.score = row[LiteColumn.SCORE]
        self.dataId = row[LiteColumn.DATA_ID]
        self.username = row[LiteColumn.USERNAME]
        self.version = row[LiteColumn.VERSION]
        self.time = row[LiteColumn.TIME]
    }
    
    override public func setters() -> [Setter]? {
        return [
            LiteColumn.WORD <- self.word,
            LiteColumn.SCORE <- self.score,
            LiteColumn.DATA_ID <- self.dataId,
            LiteColumn.USERNAME <- self.username,
            LiteColumn.VERSION <- self.version,
            LiteColumn.TIME <- self.time
        ]
    }
    
    public override func table() -> Table? {
        return LiteTable.PRONUNCIATION_SCORE
    }
}