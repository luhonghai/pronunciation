//
//  LessonDBAdapter.swift
//  AccentEasy
//
//  Created by Hai Lu on 2/25/16.
//  Copyright © 2016 Hai Lu. All rights reserved.
//

import Foundation
import Darwin

class LessonDBAdapter: BaseDatabaseAdapter {
    
    init() {
        super.init(dbFile: DatabaseHelper.getLessonUserScoreDatabaseFile()!)
    }
    
    override func prepare() {
        for query in LiteTable.CREATE_TABLE_SCORE_QUERIES {
            do {
                try db!.run(query)
            } catch {
                
            }
        }
    }
    
    func getCourseScore(username: String, idCourse: String) -> Int {
        let count:Int = (db?.scalar(LiteTable.TEST_SCORE.filter(LiteColumn.USERNAME == username && LiteColumn.IDCOUNTRY == idCourse).count))!
        if count > 0 {
            for row in try! (db?.prepare("SELECT AVG(SCORE) FROM TestScore WHERE USERNAME=? AND IDCOUNTRY=?").bind(username, idCourse))! {
                if row[0] != nil {
                    return Int(round(row[0] as! Double))
                }
            }
        }
        return -1
    }
    
    func getLessonScore(username: String, idCountry: String, idLevel:String, idObjective:String, idLesson: String) throws -> ObjectiveScore? {
        return try find(LiteTable.OBJECTIVE_SCORE
            .filter(LiteColumn.USERNAME == username && LiteColumn.IDCOUNTRY == idCountry && LiteColumn.IDLEVEL == idLevel && LiteColumn.IDOBJECTIVE == idObjective && LiteColumn.IDLESSON == idLesson))
    }
    
    func isExistsLessonScore(username: String, idCountry: String, idLevel:String, idObjective:String, idLesson: String) throws -> Bool {
        if let result:ObjectiveScore = try find(LiteTable.OBJECTIVE_SCORE
            .filter(LiteColumn.USERNAME == username && LiteColumn.IDCOUNTRY == idCountry && LiteColumn.IDLEVEL == idLevel && LiteColumn.IDOBJECTIVE == idObjective && LiteColumn.IDLESSON == idLesson)) {
            return true
        }
        return false
    }
    
    func saveLessonScore(objScore:ObjectiveScore) throws -> Bool {
        if let result:ObjectiveScore = try find(LiteTable.OBJECTIVE_SCORE
            .filter(LiteColumn.USERNAME == objScore.username && LiteColumn.IDCOUNTRY == objScore.idCountry && LiteColumn.IDLEVEL == objScore.idLevel && LiteColumn.IDOBJECTIVE == objScore.idObjective && LiteColumn.IDLESSON == objScore.idLesson)) {
            //update
            try db!.run(LiteTable.OBJECTIVE_SCORE.filter(LiteColumn.USERNAME == objScore.username && LiteColumn.IDCOUNTRY == objScore.idCountry && LiteColumn.IDLEVEL == objScore.idLevel && LiteColumn.IDOBJECTIVE == objScore.idObjective && LiteColumn.IDLESSON == objScore.idLesson).update(objScore.setters()!))
            Logger.log("update lesson score \(objScore.score)")
            return true
        }
        //insert
        try! self.insert(objScore)
        Logger.log("insert lesson score \(objScore.score)")
        return true
    }
    
    func saveTestScore(testScore:TestScore) throws -> Bool {
        if let result:TestScore = try find(LiteTable.TEST_SCORE
            .filter(LiteColumn.USERNAME == testScore.username && LiteColumn.IDCOUNTRY == testScore.idCountry && LiteColumn.IDLEVEL == testScore.idLevel)) {
            //update
            try db!.run(LiteTable.TEST_SCORE.filter(LiteColumn.USERNAME == testScore.username && LiteColumn.IDCOUNTRY == testScore.idCountry && LiteColumn.IDLEVEL == testScore.idLevel).update(testScore.setters()!))
            Logger.log("update test score \(testScore.score)")
            return true
        }
        //insert
        try! self.insert(testScore)
        Logger.log("insert test score \(testScore.score)")
        return true
    }

    
    func getObjectiveScore(username: String, idCountry: String, idLevel:String, idObjective: String) throws -> Array<ObjectiveScore> {
        return try query(LiteTable.OBJECTIVE_SCORE
            .filter(LiteColumn.USERNAME == username && LiteColumn.IDCOUNTRY == idCountry && LiteColumn.IDLEVEL == idLevel  && LiteColumn.IDOBJECTIVE == idObjective))
    }
    
    func getTestScore(username: String, idCountry: String, idLevel: String) throws -> TestScore? {
        return try find(LiteTable.TEST_SCORE
            .filter(LiteColumn.USERNAME == username && LiteColumn.IDCOUNTRY == idCountry && LiteColumn.IDLEVEL == idLevel))
    }
    
    
    
}