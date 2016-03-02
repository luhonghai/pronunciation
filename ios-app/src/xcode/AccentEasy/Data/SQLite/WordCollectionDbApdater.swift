//
//  WordCollectionDbApdater.swift
//  AccentEasy
//
//  Created by CMG on 02/02/2016.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import Foundation


public class WordCollection {
    var word: String!
    var arpabet: String!
    var definition: String!
    var mp3Path: String!
    var pronunciation: String!
}

public class IPAMapArpabet {
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
}

public class WordCollectionDbApdater: BaseDatabaseAdapter {
    
    public func search(search:String) throws -> Array<WordCollection> {
        
        var list = [WordCollection]()
        if search.isEmpty {
            return list
        }
        
        do {

            for row in try db!.prepare(LiteTable.WORD_COLLECTION.filter(LiteColumn.WORD.like("\(search)%")).limit(10)) {
                let wc = WordCollection()
                wc.word = row[LiteColumn.WORD]
                wc.arpabet = row[LiteColumn.ARPABET]
                wc.definition = row[LiteColumn.DEFINITION]
                wc.mp3Path = row[LiteColumn.MP3_PATH]
                wc.pronunciation = row[LiteColumn.PRONUNCIATION]
                list.append(wc)
            }
        } catch (let e as NSError) {
            throw e
        }
        return list
    }
    
    public func getIPAMapArpabets() throws -> Array<IPAMapArpabet> {
        var list = [IPAMapArpabet] ()
        do {
            for row in try db!.prepare(LiteTable.IPA_MAP_ARPABET) {
                let wc = IPAMapArpabet()
                wc.arpabet = row[LiteColumn.ARPABET]
                wc.color = row[LiteColumn.COLOR]
                wc.description = row[LiteColumn.DESCRIPTION]
                wc.ipa = row[LiteColumn.IPA]
                wc.mp3URL = row[LiteColumn.MP3_URL]
                wc.tip = row[LiteColumn.TIP]
                wc.type = row[LiteColumn.TYPE]
                wc.words = row[LiteColumn.WORDS]
                wc.imgTongue = row[LiteColumn.IMG_TONGUE]
                wc.mp3URLShort = row[LiteColumn.MP3_URL_SHORT]
                list.append(wc)
            }
        } catch (let e as NSError) {
            throw e
        }
        return list
    }
}