//
//  WordCollectionDbApdater.swift
//  AccentEasy
//
//  Created by CMG on 02/02/2016.
//  Copyright © 2016 Hai Lu. All rights reserved.
//

import Foundation
import ImageLoader

public class WordCollectionDbApdater: BaseDatabaseAdapter {
    
    init() {
        super.init(dbFile: DatabaseHelper.getLessonDatabaseFile()!)
    }
    
    public override func prepare() {
        let list = try! getIPAMapArpabets()
        for ipa in list {
            if ipa.mp3URL != nil && !ipa.mp3URL.isEmpty {
                HttpDownloader.loadFileSync(NSURL(string: ipa.mp3URL)!, completion: { (path, error) -> Void in
                    Logger.log("Complete download ipa audio \(ipa.mp3URL)")
                })
            }
            if ipa.mp3URLShort != nil && !ipa.mp3URLShort.isEmpty {
                HttpDownloader.loadFileSync(NSURL(string: ipa.mp3URLShort)!, completion: { (path, error) -> Void in
                    Logger.log("Complete download ipa audio \(ipa.mp3URLShort)")
                })
            }
            if ipa.imgTongue != nil && !ipa.imgTongue.isEmpty {
                try! ImageLoader.load(NSURL(string: ipa.imgTongue)!)
            }
        }
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
        return try query(LiteTable.COUNTRY.order(LiteColumn.NAME .asc))
    }
    
    public func getDefaultCountry() throws -> AECountry {
        return try find(LiteTable.COUNTRY.filter(LiteColumn.IS_DEFAULT != "\\0"))
    }
    
    public func getLevelByCountry(countryId: String!) throws -> Array<AELevel> {
        return try query(FileHelper.readFileBundle("select_all_level_by_country", type: "sql"), values: countryId)
    }
    
    public func getObjective(countryId: String!, levelId: String!) throws -> Array<AEObjective>  {
        return try query((db?.prepare(FileHelper.readFileBundle("select_all_objective_by_level", type: "sql")).bind(countryId, levelId))!)
    }
    
    public func getQuestionByLessionCollection(lcId: String!) throws -> Array<AEQuestion> {
        return try query((db?.prepare(FileHelper.readFileBundle("select_all_question_by_lesson_collection", type: "sql")).bind(lcId))!)
    }
    
    public func getTest(countryId: String!, levelId: String!) throws -> Array<AETest> {
        return try query((db?.prepare(FileHelper.readFileBundle("select_all_test_by_level", type: "sql")).bind(countryId, levelId))!)
    }
    
    public func getLessonCollectionByObjective(countryId: String!, levelId: String!, objectiveId: String!) throws -> Array<AELessonCollection> {
        return try query((db?.prepare(FileHelper.readFileBundle("select_all_lesson_collection_by_objective", type: "sql")).bind(countryId, levelId, objectiveId))!)
    }
    
    public func getLessonCollectionByTest(countryId: String!, levelId: String!, testId: String!) throws -> Array<AELessonCollection> {
        return try query((db?.prepare(FileHelper.readFileBundle("select_all_lesson_collection_by_test", type: "sql")).bind(countryId, levelId, testId))!)
    }
    
    public func getWordsOfQuestion(questionId: String!) throws -> Array<WordCollection> {
        return try query((db?.prepare(FileHelper.readFileBundle("select_all_words_by_question", type: "sql")).bind(questionId))!)
    }
    
    public func getPrevLevelOfLevel(countryId : String!, levelId: String!) throws -> AELevel {
        return try find((db?.prepare(FileHelper.readFileBundle("select_prev_level_of_level", type: "sql")).bind(countryId, countryId, levelId))!)
    }
    
    public func getNextObjectiveOnCurrentLevel(countryId : String!, levelId: String!, objectiveId: String!) throws -> AEObjective {
        return try find((db?.prepare(FileHelper.readFileBundle("select_next_objective_on_current_level", type: "sql")).bind(countryId, levelId, countryId, levelId, objectiveId))!)
    }
    
    public func getFirstLessonOfObjective(countryId : String!, levelId: String!, objectiveId: String!) throws -> AELessonCollection {
        return try find((db?.prepare(FileHelper.readFileBundle("select_all_lesson_collection_by_objective", type: "sql")).bind(countryId, levelId, objectiveId))!)
    }
    
    public func getNextLessonOnCurrentObjective(countryId : String!, levelId: String!, objectiveId: String!, lessonId: String!) throws -> AELessonCollection {
        return try find((db?.prepare(FileHelper.readFileBundle("select_next_lesson_on_current_objective", type: "sql")).bind(countryId, levelId, objectiveId,
            countryId, levelId, objectiveId,
            lessonId))!)
    }
    
}