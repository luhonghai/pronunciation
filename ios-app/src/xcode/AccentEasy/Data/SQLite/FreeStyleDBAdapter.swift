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
    
    override init(dbFile: String) {
        super.init(dbFile: dbFile)
    }
    
    override func prepare() {
        for query in LiteTable.CREATE_TABLE_QUERIES {
            do {
                try db!.run(query)
            } catch {
                
            }
        }
    }
    
    func listPronunciationScore(limit: Int?) throws -> Array<PronunciationScore> {
        return try query(limit <= 0
            ?
                LiteTable.PRONUNCIATION_SCORE.order(LiteColumn.TIME .desc)
            :
            LiteTable.PRONUNCIATION_SCORE.order(LiteColumn.TIME .desc).limit(limit))
    }

    func listPhonemeScore() throws -> Array<PhonemeScore> {
        return try query(LiteTable.PHONEME_SCORE.order(LiteColumn.TIME .desc).limit(30))
    }
}