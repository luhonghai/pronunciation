//
//  WordCollectionDbApdater.swift
//  AccentEasy
//
//  Created by CMG on 02/02/2016.
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

public class IPAMapArpabet: LiteEntity {
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
            LiteColumn.IMG_TONGUE <- self.imgTongue
        ]

    }
    
    public override func table() -> Table? {
        return LiteTable.IPA_MAP_ARPABET
    }
}

public class WordCollectionDbApdater: BaseDatabaseAdapter {
    
    public func search(search:String) throws -> Array<WordCollection> {
        if search.isEmpty {
            return [WordCollection]()
        } else {
            return try query(LiteTable.WORD_COLLECTION.filter(LiteColumn.WORD.like("\(search)%")).limit(20))
        }
    }
    
    public func getIPAMapArpabets() throws -> Array<IPAMapArpabet> {
        return try findAll()
    }
    
    public func getIPAMapArpabet(arpabet: String) throws -> IPAMapArpabet {

        do {
            for row in try db!.prepare("SELECT ARPABET, COLOR, DESCRIPTION, IPA, MP3URL, TIP, TYPE, WORDS, IMGTONGUE, MP3URLSHORT FROM IPAMapArpabet WHERE ARPABET='" + arpabet + "'") {
                let wc = IPAMapArpabet()
                wc.arpabet = row[0] as? String
                wc.color = row[1] as? String
                wc.description = row[2] as? String
                wc.ipa = row[3] as? String
                wc.mp3URL = row[4] as? String
                wc.tip = row[5] as? String
                wc.type = row[6] as? String
                wc.words = row[7] as? String
                wc.imgTongue = row[8] as? String
                wc.mp3URLShort = row[9] as? String
                return wc
            }
        } catch (let e as NSError) {
            throw e
        }
        return IPAMapArpabet()
    }
    
}