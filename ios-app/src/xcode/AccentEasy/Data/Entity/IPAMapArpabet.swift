//
//  IPAMapArpabet.swift
//  AccentEasy
//
//  Created by Hai Lu on 3/2/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

public class IPAMapArpabet: LiteEntity {
    
    static let CONSONANT = "consonant"
    
    static let VOWEL = "vowel"
    
    var arpabet:String!
    var color:String!
    var description:String!
    var ipa:String!
    var type:String!
    var mp3URL:String!
    var tip:String!
    var words:String!
    var mp3URLShort:String!
    var imgTongue:String!
    var index:Int!
    
    func getWordList() -> Array<String> {
        var list:Array<String> = []
        let wordArray = words.componentsSeparatedByString(",")
        for word in wordArray {
            list.append(word.stringByTrimmingCharactersInSet(
                NSCharacterSet.whitespaceAndNewlineCharacterSet()
                ))
        }
        return list;
    }
    
    public override func parse(row: Row) {
        self.index = row[LiteColumn.INDEXING_TYPE]
        self.arpabet = row[LiteColumn.ARPABET]
        self.color = row[LiteColumn.COLOR]
        self.description = row[LiteColumn.DESCRIPTION]
        self.ipa = row[LiteColumn.IPA]
        self.type = row[LiteColumn.TYPE]
        self.mp3URL = row[LiteColumn.MP3_URL]
        self.tip = row[LiteColumn.TIP]
        self.words = row[LiteColumn.WORDS]
        self.mp3URLShort = row[LiteColumn.MP3_URL_SHORT]
        self.imgTongue = row[LiteColumn.IMG_TONGUE]
    }
    
    public override func setters() -> [Setter]? {
        return [
            LiteColumn.ARPABET <- self.arpabet,
            LiteColumn.COLOR <- self.color,
            LiteColumn.DESCRIPTION <- self.description,
            LiteColumn.IPA <- self.ipa,
            LiteColumn.TYPE <- self.type,
            LiteColumn.MP3_URL <- self.mp3URL,
            LiteColumn.TIP <- self.tip,
            LiteColumn.WORDS <- self.words,
            LiteColumn.MP3_URL_SHORT <- self.mp3URLShort,
            LiteColumn.IMG_TONGUE <- self.imgTongue,
            LiteColumn.INDEXING_TYPE <- self.index
        ]
        
    }
    
    public override func table() -> Table? {
        return LiteTable.IPA_MAP_ARPABET
    }
}