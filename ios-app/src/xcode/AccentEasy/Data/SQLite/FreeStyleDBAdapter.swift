//
//  FreeStyleDBAdapter.swift
//  AccentEasy
//
//  Created by Minh Kelly on 2/25/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import Foundation
import Darwin

class FreeStyleDBAdapter: BaseDatabaseAdapter {
    
    override init(dbFile: String) {
        super.init(dbFile: dbFile)
        do {
            try db!.run(LiteTable.PRONUNCIATION_SCORE.create(ifNotExists: true) { t in
                t.column(LiteColumn.ID, primaryKey: .Autoincrement)
                t.column(LiteColumn.WORD)
                t.column(LiteColumn.SCORE)
                t.column(LiteColumn.DATA_ID)
                t.column(LiteColumn.USERNAME)
                t.column(LiteColumn.VERSION)
                t.column(LiteColumn.TIME)
            })
        } catch {
            
        }
        do {
            try db!.run(LiteTable.PHONEME_SCORE.create(ifNotExists: true) { t in
                t.column(LiteColumn.ID, primaryKey: .Autoincrement)
                t.column(LiteColumn.INDEX)
                t.column(LiteColumn.NAME)
                t.column(LiteColumn.IPA)
                t.column(LiteColumn.SCORE)
                t.column(LiteColumn.USERNAME)
                t.column(LiteColumn.VERSION)
                t.column(LiteColumn.DATA_ID)
                t.column(LiteColumn.TIME)
                })
        } catch {
            
        }
    }
    
    func insertPronunciationScore(obj: PronunciationScore) -> Bool {
        do {
            try db!.run(LiteTable.PRONUNCIATION_SCORE.insert(
                LiteColumn.WORD <- obj.word,
                LiteColumn.SCORE <- obj.score,
                LiteColumn.DATA_ID <- obj.dataId,
                LiteColumn.USERNAME <- obj.username,
                LiteColumn.VERSION <- obj.version,
                LiteColumn.TIME <- obj.time
            ))
            return true
        } catch {
            
        }
        return false
    }
    
    func listPronunciationScore(limit: Int?) -> Array<PronunciationScore> {
        var list = [PronunciationScore]()
        do {
            for row in try db!.prepare(
                limit == 0
                    ?
                    LiteTable.PRONUNCIATION_SCORE.order(LiteColumn.TIME .desc)
                    :
                    LiteTable.PRONUNCIATION_SCORE.order(LiteColumn.TIME .desc).limit(limit)) {
                let pScore = PronunciationScore()
                pScore.word = row[LiteColumn.WORD]
                pScore.score = row[LiteColumn.SCORE]
                pScore.time = row[LiteColumn.TIME]
                list.append(pScore)
            }
        } catch {
            
        }
        return list
    }
    
    func insertPhonemeScore(obj: SphinxResult.PhonemeScore) -> Bool {
        do {
            try db!.run(LiteTable.PHONEME_SCORE.insert(
                LiteColumn.NAME <- obj.name,
                LiteColumn.SCORE <- Int(floor(obj.totalScore)),
                LiteColumn.DATA_ID <- obj.userVoiceId,
                LiteColumn.USERNAME <- obj.username,
                LiteColumn.IPA <- obj.ipa,
                LiteColumn.INDEX <- obj.index,
                LiteColumn.VERSION <- obj.version,
                LiteColumn.TIME <- obj.time
                ))
            return true
        } catch {
            
        }
        return false
    }
    
    func listPhonemeScore() -> Array<SphinxResult.PhonemeScore> {
        var list = Array<SphinxResult.PhonemeScore>()
        do {
            for row in try db!.prepare(LiteTable.PHONEME_SCORE.order(LiteColumn.TIME .desc).limit(30)) {
                let pScore = SphinxResult.PhonemeScore()
                pScore.name = row[LiteColumn.NAME]
                pScore.ipa = row[LiteColumn.IPA]
                pScore.totalScore = Float(row[LiteColumn.SCORE]!)
                pScore.time = row[LiteColumn.TIME]
                list.append(pScore)
            }
        } catch {
            
        }
        return list
    }
}