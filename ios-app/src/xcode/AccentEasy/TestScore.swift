//
//  TestScore.swift
//  AccentEasy
//
//  Created by CMGVN on 4/1/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

public class TestScore: LiteEntity {
    
    var username:String!
    var idCountry:String!
    var idLevel:String!
    var score:Int!
    var isLevelPass:Bool!
    
    var passScore:Int!
    
    override public func parse(row: Row) {
        self.username = row[LiteColumn.USERNAME]
        self.idCountry = row[LiteColumn.IDCOUNTRY]
        self.idLevel = row[LiteColumn.IDLEVEL]
        self.score = row[LiteColumn.SCORE]
        self.isLevelPass = row[LiteColumn.ISLEVELPASS]
    }
    
    override public func setters() -> [Setter]? {
        return [
            LiteColumn.USERNAME <- self.username,
            LiteColumn.IDCOUNTRY <- self.idCountry,
            LiteColumn.IDLEVEL <- self.idLevel,
            LiteColumn.SCORE <- self.score,
            LiteColumn.ISLEVELPASS <- self.isLevelPass
        ]
    }
    
    public override func table() -> Table? {
        return LiteTable.TEST_SCORE
    }
}