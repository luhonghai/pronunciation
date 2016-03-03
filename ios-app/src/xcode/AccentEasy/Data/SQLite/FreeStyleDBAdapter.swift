//
//  FreeStyleDBAdapter.swift
//  AccentEasy
//
//  Created by Hai Lu on 2/25/16.
//  Copyright © 2016 Hai Lu. All rights reserved.
//

import Foundation
import Darwin

class FreeStyleDBAdapter: BaseDatabaseAdapter {
    
    init() {
        super.init(dbFile: DatabaseHelper.getFreeStyleDatabaseFile()!)
    }
    
    override func prepare() {
        for query in LiteTable.CREATE_TABLE_QUERIES {
            do {
                try db!.run(query)
            } catch {
                
            }
        }
    }
    
    func listPronunciationScore(limit: Int, username: String) throws -> Array<PronunciationScore> {
        return try query(limit <= 0
            ?
                LiteTable.PRONUNCIATION_SCORE
                    .filter(LiteColumn.USERNAME == username)
                    .order(LiteColumn.TIME .desc)
            :
            LiteTable.PRONUNCIATION_SCORE
                .filter(LiteColumn.USERNAME == username)
                .order(LiteColumn.TIME .desc).limit(limit))
    }

    func listPhonemeScore(phoneme: String, limit: Int, username: String) throws -> Array<PhonemeScore> {
        return try query(LiteTable.PHONEME_SCORE
                                .filter(LiteColumn.USERNAME == username && LiteColumn.NAME == phoneme)
                                .order(LiteColumn.TIME .desc).limit(limit))
    }
    
    func listPronunciationScore(word: String, limit: Int, username: String) throws -> Array<PronunciationScore> {
        return try query(LiteTable.PRONUNCIATION_SCORE
            .filter(LiteColumn.USERNAME == username && LiteColumn.WORD == word)
            .order(LiteColumn.TIME .desc).limit(limit))
    }
}