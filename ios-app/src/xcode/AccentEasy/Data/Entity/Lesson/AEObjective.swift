//
//  AEObjective.swift
//  AccentEasy
//
//  Created by Hai Lu on 17/03/2016.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

public class AEObjective: AELiteEntity {
    var name: String!
    var description: String!
    var score: Int!
    
    override public func parse(row: Row) {
        self.idString = row[LiteColumn.IDSTRING]
        self.name = row[LiteColumn.NAME]
        self.description = row[LiteColumn.DESCRIPTION]
    }
    
    override public func setters() -> [Setter]? {
        return [
            LiteColumn.IDSTRING <- self.idString,
            LiteColumn.NAME <- self.name,
            LiteColumn.DESCRIPTION <- self.description
        ]
    }

    public override func table() -> Table? {
        return LiteTable.OBJECTIVE
    }
}