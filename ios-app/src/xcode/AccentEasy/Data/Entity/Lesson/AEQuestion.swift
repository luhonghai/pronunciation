//
//  AEQuestion.swift
//  AccentEasy
//
//  Created by Hai Lu on 17/03/2016.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

public class AEQuestion : AELiteEntity {
    var name: String!
    var description: String!
    var recorded: Bool = false
    var enabled: Bool = false
    var listWord = [WordCollection]()
    var listScore = [Float]()
    
    override public func parse(row: Row) {
        self.idString = row[LiteColumn.IDSTRING]
        self.name = row[LiteColumn.NAME]
        self.description = row[LiteColumn.DESCRIPTION]
    }
    
    public override func parse(row: Array<Optional<Binding>>) {
        self.idString = row[0] as! String
        self.name = row[1] as! String
        self.description = row[2] as? String
    }
    
    override public func setters() -> [Setter]? {
        return [
            LiteColumn.IDSTRING <- self.idString,
            LiteColumn.NAME <- self.name,
            LiteColumn.DESCRIPTION <- self.description
        ]
    }
    
    public override func table() -> Table? {
        return LiteTable.QUESTION
    }
}
