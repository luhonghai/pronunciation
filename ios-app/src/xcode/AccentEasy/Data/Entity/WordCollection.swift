//
//  WordCollection.swift
//  AccentEasy
//
//  Created by Hai Lu on 3/2/16.
//  Copyright © 2016 Hai Lu. All rights reserved.
//

import Foundation

public class WordCollection: LiteEntity, Mappable {
    var word: String!
    var arpabet: String!
    var definition: String!
    var mp3Path: String!
    var pronunciation: String!
    
    required public init(){
        super.init()
    }
    func getArpabetList() -> Array<String> {
        var list:Array<String> = []
        if arpabet == nil || arpabet.isEmpty {
            return list
        }
        let wordArray = arpabet.componentsSeparatedByString(" ")
        for word in wordArray {
            list.append(word.stringByTrimmingCharactersInSet(
                NSCharacterSet.whitespaceAndNewlineCharacterSet()
                ))
        }
        return list;
    }
    
    required public init?(_ map: Map) {
        super.init()
    }
    
    public required init(id: Int64) {
        super.init(id: id)
    }
    
    public override func parse(row: Row) {
        self.word = row[LiteColumn.WORD]
        self.arpabet = row[LiteColumn.ARPABET]
        self.definition = row[LiteColumn.DEFINITION]
        self.mp3Path = row[LiteColumn.MP3_PATH]
        self.pronunciation = row[LiteColumn.PRONUNCIATION]
    }
    
    public override func parse(row: Array<Optional<Binding>>) {
        self.word = row[1] as? String
        self.arpabet = row[2] as? String
        self.definition = row[3] as? String
        self.mp3Path = row[4] as? String
        self.pronunciation = row[5] as? String
    }
    
    
    public override func table() -> Table? {
        return LiteTable.WORD_COLLECTION
    }
    
    // Mappable
    public func mapping(map: Map) {
        word    <= map["word"]
        arpabet   <= map["arpabet"]
        definition      <= map["definition"]
        mp3Path       <= map["mp3Path"]
        pronunciation  <= map["pronunciation"]
    }
    
    public override func setters() -> [Setter]? {
        return [
            LiteColumn.WORD <- self.word,
            LiteColumn.ARPABET <- self.arpabet,
            LiteColumn.DEFINITION <- self.definition,
            LiteColumn.MP3_PATH <- self.mp3Path,
            LiteColumn.PRONUNCIATION <- self.pronunciation
        ]
    }
    
}