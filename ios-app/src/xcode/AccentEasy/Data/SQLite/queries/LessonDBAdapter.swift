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
    
    func getLessonScore(username: String, idCountry: String, idLevel:String, idObjective:String, idLesson: String) throws -> ObjectiveScore? {
        return try find(LiteTable.OBJECTIVE_SCORE
            .filter(LiteColumn.USERNAME == username && LiteColumn.IDCOUNTRY == idCountry && LiteColumn.IDLEVEL == idLevel && LiteColumn.IDOBJECTIVE == idObjective && LiteColumn.IDLESSON == idLesson))
    }
    
    func getLevelScore(username: String, idCountry: String, idLevel:String, idObjective: String) throws -> Array<ObjectiveScore> {
        return try query(LiteTable.OBJECTIVE_SCORE
            .filter(LiteColumn.USERNAME == username && LiteColumn.IDCOUNTRY == idCountry && LiteColumn.IDLEVEL == idLevel  && LiteColumn.IDOBJECTIVE == idObjective))
    }
    
    func getTestScore(username: String, idCountry: String, idLevel: String) throws -> TestScore {
        return try find(LiteTable.TEST_SCORE
            .filter(LiteColumn.USERNAME == username && LiteColumn.IDCOUNTRY == idCountry && LiteColumn.IDLEVEL == idLevel))
    }
    
}