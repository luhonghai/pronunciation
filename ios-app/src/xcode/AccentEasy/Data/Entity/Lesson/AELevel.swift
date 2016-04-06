//
//  AELevel.swift
//  AccentEasy
//
//  Created by Hai Lu on 17/03/2016.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

public class AELevel: AELiteEntity {
    var name: String!
    var isDemo: String?
    var isDefaultActivated: String?
    var description: String!
    var color: String!
    
    var active: Bool!
    var score: Int!
    
    override public func parse(row: Row) {
        self.idString = row[LiteColumn.IDSTRING]
        self.name = row[LiteColumn.NAME]
        self.description = row[LiteColumn.DESCRIPTION]
        self.isDemo = row[LiteColumn.IS_DEMO]
        self.isDefaultActivated = row[LiteColumn.IS_DEFAULT_ACTIVATED]
        self.color = row[LiteColumn.COLOR]
    }
    
    public override func parse(row: Array<Optional<Binding>>) {
        self.idString = row[0] as! String
        self.name = row[1] as! String
        self.description = row[2] as! String
        self.isDemo = row[3] as? String
        self.isDefaultActivated = row[4] as? String
        self.color = row[5] as! String
    }
    
    override public func setters() -> [Setter]? {
        return [
            LiteColumn.IDSTRING <- self.idString,
            LiteColumn.NAME <- self.name,
            LiteColumn.DESCRIPTION <- self.description,
            LiteColumn.IS_DEMO <- self.isDemo,
            LiteColumn.IS_DEFAULT_ACTIVATED <- self.isDefaultActivated,
            LiteColumn.COLOR <- self.color
        ]
    }
    
    public override func table() -> Table? {
        return LiteTable.LEVEL
    }

}