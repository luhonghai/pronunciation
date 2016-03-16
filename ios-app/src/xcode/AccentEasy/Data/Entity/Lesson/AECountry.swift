//
//  AECountry.swift
//  AccentEasy
//
//  Created by CMGVN on 3/15/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

public class AECountry: LiteEntity, Mappable{
    var idString: String!
    var name: String!
    var description: String!
    var isDefault: Bool!
    var imageURL: String!
    
    override public func parse(row: Row) {
        self.idString = row[LiteColumn.IDSTRING]
        self.name = row[LiteColumn.NAME]
        self.description = row[LiteColumn.DESCRIPTION]
        self.isDefault = row[LiteColumn.IS_DEFAULT]
        self.imageURL = row[LiteColumn.IMAGE_URL]
    }
    
    override public func setters() -> [Setter]? {
        return [
            LiteColumn.IDSTRING <- self.idString,
            LiteColumn.NAME <- self.name,
            LiteColumn.DESCRIPTION <- self.description,
            LiteColumn.IS_DEFAULT <- self.isDefault,
            LiteColumn.IMAGE_URL <- self.imageURL        ]
    }
    
    public override func table() -> Table? {
        return LiteTable.COUNTRY
    }
    
    required public init(){
        super.init()
    }
    
    required public init?(_ map: Map) {
        super.init()
    }
    
    public required init(id: Int64) {
        super.init(id: id)
    }
    
    // Mappable
    public func mapping(map: Map) {
        idString    <= map["idString"]
        name   <= map["name"]
        description      <= map["description"]
        isDefault       <= map["isDefault"]
        imageURL  <= map["imageURL"]
    }

}