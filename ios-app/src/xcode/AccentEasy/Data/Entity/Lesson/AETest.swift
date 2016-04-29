//
//  AETest.swift
//  AccentEasy
//
//  Created by Hai Lu on 17/03/2016.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation


public class AETest : AELiteEntity {
    var name: String!
    var description: String!
    var percentPass: Double!
    
    override public func parse(row: Row) {
        self.idString = row[LiteColumn.IDSTRING]
        self.name = row[LiteColumn.NAME]
        self.description = row[LiteColumn.DESCRIPTION]
        self.percentPass = row[LiteColumn.PERCENT_PASS]
    }
    
    public override func parse(row: Array<Optional<Binding>>) {
        self.idString = row[0] as? String
        self.name = row[1] as? String
        self.percentPass = row[3] as? Double
    }
    
    override public func setters() -> [Setter]? {
        return [
            LiteColumn.IDSTRING <- self.idString,
            LiteColumn.NAME <- self.name,
            LiteColumn.DESCRIPTION <- self.description,
            LiteColumn.PERCENT_PASS <- self.percentPass
        ]
    }
    
    public override func table() -> Table? {
        return LiteTable.TEST
    }
}