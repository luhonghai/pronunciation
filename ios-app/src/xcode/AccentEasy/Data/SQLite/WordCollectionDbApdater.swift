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
}