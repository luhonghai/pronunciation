//
//  ObjectiveScore.swift
//  AccentEasy
//
//  Created by CMGVN on 4/1/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

public class ObjectiveScore: LiteEntity {
    
    var username:String!
    var idCountry:String!
    var idLevel:String!
    var idObjective:String!
    var idLesson:String!
    var score:Int!
    
    override public func parse(row: Row) {
        self.username = row[LiteColumn.USERNAME]
        self.idCountry = row[LiteColumn.IDCOUNTRY]
        self.idLevel = row[LiteColumn.IDLEVEL]
        self.idObjective = row[LiteColumn.IDOBJECTIVE]
        self.idLesson = row[LiteColumn.IDLESSON]
        self.score = row[LiteColumn.SCORE]
    }
    
    override public func setters() -> [Setter]? {
        return [
            LiteColumn.USERNAME <- self.username,
            LiteColumn.IDCOUNTRY <- self.idCountry,
            LiteColumn.IDLEVEL <- self.idLevel,
            LiteColumn.IDOBJECTIVE <- self.idObjective,
            LiteColumn.IDLESSON <- self.idLesson,
            LiteColumn.SCORE <- self.score
        ]
    }
    
    public override func table() -> Table? {
        return LiteTable.OBJECTIVE_SCORE
    }
}