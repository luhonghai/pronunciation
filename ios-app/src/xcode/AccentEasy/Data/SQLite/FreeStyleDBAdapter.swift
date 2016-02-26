//
//  FreeStyleDBAdapter.swift
//  AccentEasy
//
//  Created by Minh Kelly on 2/25/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import Foundation

class PronunciationScore {
    var word:String!
    var score:Int!
    var dataId:String!
    var username:String!
    var version:Int!
    var time:Double!
}

class PhonemeScore {
    var index:Int!
    var name:String!
    var ipa:String!
    var totalScore:Float!
    var username:String!
    var version:Int!
    var userVoiceId:String!
    var time:Double!
}

class FreeStyleDBAdapter: BaseDatabaseAdapter {
    
    var tblPronunciationScore = Table("PronunciationScore")
    var tblPhonemeScore = Table("PhonemeScore")
    
    override init(dbFile: String) {
        super.init(dbFile: dbFile)
        do {
            try db!.run(tblPronunciationScore.create(ifNotExists: true) { t in
                t.column(Expression<Int64>("id"), primaryKey: .Autoincrement)
                t.column(Expression<String?>("word"))
                t.column(Expression<Int?>("score"))
                t.column(Expression<String?>("dataId"))
                t.column(Expression<String?>("username"))
                t.column(Expression<Int?>("version"))
                t.column(Expression<Double?>("time"))
            })
        } catch {
            
        }
        do {
            try db!.run(tblPhonemeScore.create(ifNotExists: true) { t in
                t.column(Expression<Int64>("id"), primaryKey: .Autoincrement)
                t.column(Expression<Int?>("index"))
                t.column(Expression<String?>("name"))
                t.column(Expression<String?>("ipa"))
                t.column(Expression<Int?>("totalScore"))
                t.column(Expression<String?>("username"))
                t.column(Expression<Int?>("version"))
                t.column(Expression<String?>("userVoiceId"))
                t.column(Expression<Double?>("time"))
                })
        } catch {
            
        }
    }
    
    func insertPronunciationScore(obj: PronunciationScore) -> Bool {
        do {
            try db!.run(tblPronunciationScore.insert(
                Expression<String?>("word") <- obj.word,
                Expression<Int?>("score") <- obj.score,
                Expression<String?>("dataId") <- obj.dataId,
                Expression<String?>("username") <- obj.username,
                Expression<Int?>("version") <- obj.version,
                Expression<Double?>("time") <- obj.time
            ))
            return true
        } catch {
            
        }
        return false
    }
    
    func listPronunciationScore() -> Array<PronunciationScore> {
        var list = [PronunciationScore]()
        do {
            for user in try db!.prepare(tblPronunciationScore.order(Expression<Double?>("time") .desc)) {
                let pScore = PronunciationScore()
                pScore.word = user[Expression<String?>("word")]
                pScore.score = user[Expression<Int?>("score")]
                pScore.time = user[Expression<Double?>("time")]
                list.append(pScore)
            }
        } catch {
            
        }
        return list
    }
}