//
//  WordCollectionDbApdater.swift
//  AccentEasy
//
//  Created by CMG on 02/02/2016.
//  Copyright © 2016 Hai Lu. All rights reserved.
//

import Foundation

public class WordCollectionDbApdater: BaseDatabaseAdapter {
    
    init() {
        super.init(dbFile: DatabaseHelper.getLessonDatabaseFile()!)
    }
    
    public func search(search:String) throws -> Array<WordCollection> {
        if search.isEmpty {
            return [WordCollection]()
        } else {
            return try query(LiteTable.WORD_COLLECTION.filter(LiteColumn.WORD.like("\(search)%")).limit(20))
        }
    }
    
    public func findByWord(word: String) throws -> WordCollection {
        return try find(LiteTable.WORD_COLLECTION.filter(LiteColumn.WORD == word).limit(1))
    }
    
    public func getIPAMapArpabets() throws -> Array<IPAMapArpabet> {
        return try findAll()
    }
    
    public func getIPAMapArpabet(arpabet: String) throws -> IPAMapArpabet {
        return try find(LiteTable.IPA_MAP_ARPABET.filter(LiteColumn.ARPABET == arpabet))
    }
    
    public func getIPAMapArpabetByType(type: String) throws -> Array<IPAMapArpabet> {
        return try query(LiteTable.IPA_MAP_ARPABET.filter(LiteColumn.TYPE == type).order(LiteColumn.INDEXING_TYPE .asc))
    }
    
    public func getAllCountries() throws -> Array<AECountry> {
        return try findAll()
    }
}