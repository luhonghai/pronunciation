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
            for row in try db!.prepare("SELECT WORD, ARPABET, DEFINITION, MP3PATH, PRONUNCIATION FROM WordCollection where WORD like '\(search)%' LIMIT 10") {
                //print("id: \(row[0]), email: \(row[1])")
                let wc = WordCollection()
                wc.word = row[0] as? String
                wc.arpabet = row[1] as? String
                wc.definition = row[2] as? String
                wc.mp3Path = row[3] as? String
                wc.pronunciation = row[4] as? String
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
            for row in try db!.prepare("SELECT ARPABET, COLOR, DESCRIPTION, IPA, MP3URL, TIP, TYPE, WORDS, IMGTONGUE, MP3URLSHORT FROM IPAMapArpabet") {
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
                list.append(wc)
            }
        } catch (let e as NSError) {
            throw e
        }
        return list
    }
}