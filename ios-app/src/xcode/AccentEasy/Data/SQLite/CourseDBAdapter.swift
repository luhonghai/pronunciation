//
//  CourseDBAdapter.swift
//  AccentEasy
//
//  Created by Hai Lu on 4/26/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

class CourseDBAdapter: BaseDatabaseAdapter {
    
    var course: AECourse!
    
    init(course: AECourse) {
        super.init(dbFile: DatabaseHelper.getCourseDbPath(course))
        self.course = course
    }
    
    class func newInstance() -> CourseDBAdapter {
        return CourseDBAdapter(course: AccountManager.currentUser().courseSession.selectedCourse)
    }

    func getLevelByCountry() throws -> Array<AELevel> {
        return try query(FileHelper.readFileBundle("select_all_level_by_country", type: "sql"), values: course.idString)
    }
    
    func getObjective(levelId: String!) throws -> Array<AEObjective>  {
        return try query((db?.prepare(FileHelper.readFileBundle("select_all_objective_by_level", type: "sql")).bind(course.idString, levelId))!)
    }
    
    func getQuestionByLessionCollection(lcId: String!) throws -> Array<AEQuestion> {
        return try query((db?.prepare(FileHelper.readFileBundle("select_all_question_by_lesson_collection", type: "sql")).bind(lcId))!)
    }
    
    func getTest(levelId: String!) throws -> Array<AETest> {
        return try query((db?.prepare(FileHelper.readFileBundle("select_all_test_by_level", type: "sql")).bind(course.idString, levelId))!)
    }
    
    func getLessonCollectionByObjective(levelId: String!, objectiveId: String!) throws -> Array<AELessonCollection> {
        return try query((db?.prepare(FileHelper.readFileBundle("select_all_lesson_collection_by_objective", type: "sql")).bind(course.idString, levelId, objectiveId))!)
    }
    
    func getLessonCollectionByTest(levelId: String!, testId: String!) throws -> Array<AELessonCollection> {
        return try query((db?.prepare(FileHelper.readFileBundle("select_all_lesson_collection_by_test", type: "sql")).bind(course.idString, levelId, testId))!)
    }
    
    func getWordsOfQuestion(questionId: String!) throws -> Array<WordCollection> {
        let list: Array<WordCollection> = try query((db?.prepare(FileHelper.readFileBundle("select_all_words_by_question", type: "sql")).bind(questionId))!)
        var array = Array<WordCollection>()
        let adapter = WordCollectionDbApdater()
        if !list.isEmpty {
            for wc in list {
                let w:WordCollection! = try! adapter.find(LiteTable.WORD_COLLECTION.filter(LiteColumn.IDSTRING == wc.idString))
                if w != nil {
                    array.append(w)
                }
            }
        }
        return array
    }
    
    func getPrevLevelOfLevel(levelId: String!) throws -> AELevel {
        return try find((db?.prepare(FileHelper.readFileBundle("select_prev_level_of_level", type: "sql")).bind(course.idString, course.idString, levelId))!)
    }
    
    func getNextObjectiveOnCurrentLevel(levelId: String!, objectiveId: String!) throws -> AEObjective {
        return try find((db?.prepare(FileHelper.readFileBundle("select_next_objective_on_current_level", type: "sql")).bind(course.idString, levelId, course.idString, levelId, objectiveId))!)
    }
    
    func getFirstLessonOfObjective(levelId: String!, objectiveId: String!) throws -> AELessonCollection {
        return try find((db?.prepare(FileHelper.readFileBundle("select_all_lesson_collection_by_objective", type: "sql")).bind(course.idString, levelId, objectiveId))!)
    }
    
     func getNextLessonOnCurrentObjective(levelId: String!, objectiveId: String!, lessonId: String!) throws -> AELessonCollection {
        return try find((db?.prepare(FileHelper.readFileBundle("select_next_lesson_on_current_objective", type: "sql")).bind(course.idString, levelId, objectiveId,
            course.idString, levelId, objectiveId,
            lessonId))!)
    }
}