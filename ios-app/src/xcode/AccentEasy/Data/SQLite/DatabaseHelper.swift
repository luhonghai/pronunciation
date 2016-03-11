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
                print("Previous version \(dbVersion.version)")
            } catch {
                
            }
        }
        let client = Client()
            .baseUrl(FileHelper.getAccentEasyBaseUrl())
            .onError({e in
                print(e)
                completion(success: false)
            });
        var willLoad = false
        client.get("/CheckVersion").type("json").query(["version" : String(dbVersion.version)]).send([])
            .end({(res:Response) -> Void in
                if(res.error) { // status of 2xx
                    //handleResponseJson(res.body)
                    //print(res.body)
                    print(res.text)
                    completion(success: false)
                }
                else {
                    //handleErrorJson(res.body)
                    print(res.text)
                    let dbVersionRes: DatabaseVersion = JSONHelper.fromJson(res.text!)
                    if dbVersionRes.status {
                        willLoad = true
                        dbVersion = dbVersionRes
                    }
                    let lessonDbFilePath = FileHelper.getFilePath(LiteDatabase.LESSON)
                    if willLoad {
                        FileHelper.deleteFile(tmpZip)
                        print("Try to load database zip from url \(dbVersion.data) to \(tmpZip)")
                        HttpDownloader.loadFileAsync(NSURL(string: dbVersion.data)!, skipCache: true, destPath: tmpZip) { (path, error) -> Void in
                            do {
                                if (FileHelper.isExists(tmpZip)) {
                                    FileHelper.deleteFile(lessonDbFilePath)
                                    print("Try to unzip dabase version")
                                    SSZipArchive.unzipFileAtPath(tmpZip, toDestination: dataPath)
                                    if fileManager.fileExistsAtPath(lessonDbFilePath) {
                                        print("Lesson database found. Try to save database version info")
                                        try FileHelper.writeFile(versionPath, content: JSONHelper.toJson(dbVersion))
                                    } else {
                                        print("No lesson database found at path \(lessonDbFilePath)")
                                    }
                                } else {
                                    print("No zip file found at path \(tmpZip)")
                                }
                            } catch {
                                
                            }
                        }
                    } else {
                        print("Use current version. Skip update database")
                    }
                    completion(success: fileManager.fileExistsAtPath(lessonDbFilePath))
                }
            })
    }
    
    class func getFreeStyleDatabaseFile() -> String? {
        return FileHelper.getFilePath(LiteDatabase.FREESTYLE)
    }
    
    class func getLessonDatabaseFile() -> String? {
        return FileHelper.getFilePath(LiteDatabase.LESSON)
    }
}
