//
//  DatabaseHelper.swift
//  AccentEasy
//
//  Created by Hai Lu on 20/01/2016.
//  Copyright © 2016 Hai Lu. All rights reserved.
//

import Foundation
import SSZipArchive

public class DatabaseVersion: Mappable {
    var status = false
    var message: String!
    var data: String!
    var version = 0
    
    required public init?(_ map: Map) {
        
    }
    
    required public init(){
        
    }
    
    // Mappable
    public func mapping(map: Map) {
        version    <= map["version"]
        status    <= map["status"]
        message    <= map["message"]
        data    <= map["data"]
    }

}

public class DatabaseHelper {
    
    class func getCourseSessionFilePath(username: String) -> String {
        return FileHelper.getFilePath("database/course_session/\(StringHelper.md5(string: username)).json")
    }
    
    class func updateCourses(courseSession: AECourseSession) -> Bool {
        FileHelper.getFilePath("database", directory: true)
        FileHelper.getFilePath("database/course_session", directory: true)
        let courseSessionFilePath = getCourseSessionFilePath(courseSession.username)
        var oldSession = AECourseSession()
        if FileHelper.isExists(courseSessionFilePath) {
            oldSession = try! JSONHelper.fromJson(FileHelper.readFile(courseSessionFilePath))
        }
        var status = true
        if courseSession.courses.count > 0 {
            for course in courseSession.courses {
                var versionChanged = false
                var found = false
                if oldSession.courses.count > 0 {
                    for oldCourse in oldSession.courses {
                        if (oldCourse.idString != nil) &&  (course.idString != nil) && (oldCourse.idString == course.idString){
                            found = true
                            if (oldCourse.version != course.version) {
                                versionChanged = true
                                Logger.log("found version change. from \(oldCourse.idString) to \(course.version). Course id \(course.idString)")
                            }
                        }
                    }
                    
                }
                if versionChanged || !found {
                    if !downloadCourseDatabase(course) {
                        status = false
                        break
                    }
                }
            }
        }
        if status {
            
        } else {
            Logger.log("no course database changed. skip by default")
        }
        try! FileHelper.writeFile(courseSessionFilePath, content: JSONHelper.toJson(courseSession))
        return status
    }
    
    class func getCourseDbPath(course: AECourse) -> String {
        return FileHelper.getFilePath("database/\(course.idString)/lesson.db")
    }
    
    class func downloadCourseDatabase(course: AECourse) -> Bool {
        FileHelper.getFilePath("database", directory: true)
        let dbDirPath = FileHelper.getFilePath("database/\(course.idString)", directory: true)
        let tmpZip = FileHelper.getFilePath("database/\(course.idString)/tmp.zip")
        let dbPath = getCourseDbPath(course)
        FileHelper.deleteFile(tmpZip)
        Logger.log("Download course version \(course.version). name \(course.name). url \(course.dbURL)")
        HttpDownloader.loadFileSync(NSURL(string: course.dbURL)!, skipCache: true, destPath: tmpZip) { (path, error) -> Void in
            do {
                if (FileHelper.isExists(tmpZip)) {
                    FileHelper.deleteFile(dbPath)
                    Logger.log("Try to unzip database \(tmpZip)")
                    SSZipArchive.unzipFileAtPath(tmpZip, toDestination: dbDirPath)
                    if FileHelper.isExists(dbPath) {
                        Logger.log("Course database found. \(dbPath)")
                    } else {
                        Logger.log("No course database found at path \(dbPath)")
                    }
                } else {
                    Logger.log("No zip file found at path \(tmpZip)")
                }
            } catch {
                
            }
        }
        return FileHelper.isExists(dbPath)
    }
    
    class func checkDatabaseVersion(completion:(success: Bool) -> Void) {
        let fileManager = NSFileManager.defaultManager()
        // Create database folder if not exist
        let dataPath = FileHelper.getFilePath("database", directory: true)
        let versionPath = FileHelper.getFilePath("database/version")
        let tmpZip = FileHelper.getFilePath("database/tmp.zip")
        var dbVersion = DatabaseVersion()
        if (FileHelper.isExists(versionPath)) {
            do {
                dbVersion = try JSONHelper.fromJson(FileHelper.readFile(versionPath))
                Logger.log("Previous version \(dbVersion.version)")
            } catch {
                
            }
        }
        let client = Client()
            .baseUrl(FileHelper.getAccentEasyBaseUrl())
            .onError({e in
                Logger.log(e)
                completion(success: false)
            });
        var willLoad = false
        client.get("/CheckVersion").type("json").query(["version" : String(dbVersion.version), "profile" : JSONHelper.toJson(AccountManager.currentUser())]).send([])
            .end({(res:Response) -> Void in
                if(res.error) { // status of 2xx
                    //handleResponseJson(res.body)
                    //Logger.log(res.body)
                    Logger.log(res.text)
                    completion(success: false)
                }
                else {
                    //handleErrorJson(res.body)
                    Logger.log("success CheckVersion")
                    Logger.log(res.text)
                    let dbVersionRes: DatabaseVersion = JSONHelper.fromJson(res.text!)
                    if dbVersionRes.status {
                        willLoad = true
                        dbVersion = dbVersionRes
                    }
                    let lessonDbFilePath = FileHelper.getFilePath(LiteDatabase.LESSON)
                    if willLoad {
                        FileHelper.deleteFile(tmpZip)
                        Logger.log("Try to load database zip from url \(dbVersion.data) to \(tmpZip)")
                        HttpDownloader.loadFileAsync(NSURL(string: dbVersion.data)!, skipCache: true, destPath: tmpZip) { (path, error) -> Void in
                            do {
                                if (FileHelper.isExists(tmpZip)) {
                                    FileHelper.deleteFile(lessonDbFilePath)
                                    Logger.log("Try to unzip dabase version")
                                    SSZipArchive.unzipFileAtPath(tmpZip, toDestination: dataPath)
                                    if fileManager.fileExistsAtPath(lessonDbFilePath) {
                                        Logger.log("Lesson database found. Try to save database version info")
                                        try FileHelper.writeFile(versionPath, content: JSONHelper.toJson(dbVersion))
                                    } else {
                                        Logger.log("No lesson database found at path \(lessonDbFilePath)")
                                    }
                                } else {
                                    Logger.log("No zip file found at path \(tmpZip)")
                                }
                            } catch {
                                
                            }
                            completion(success: fileManager.fileExistsAtPath(lessonDbFilePath))
                        }
                    } else {
                        Logger.log("Use current version. Skip update database")
                        completion(success: fileManager.fileExistsAtPath(lessonDbFilePath))
                    }
                    
                }
            })
    }
    
    class func getFreeStyleDatabaseFile() -> String? {
        return FileHelper.getFilePath(LiteDatabase.FREESTYLE)
    }
    
    class func getLessonDatabaseFile() -> String? {
        return FileHelper.getFilePath(LiteDatabase.LESSON)
    }
    
    class func getLessonUserScoreDatabaseFile() -> String? {
        return FileHelper.getFilePath(LiteDatabase.LESSONUSERSCORE)
    }
    
    class func getLessonUserHistoryDatabaseFile() -> String? {
        return FileHelper.getFilePath(LiteDatabase.LESSONUSERHISTORY)
    }
}
