//
//  DatabaseHelper.swift
//  AccentEasy
//
//  Created by CMG on 20/01/2016.
//  Copyright © 2016 Hai Lu. All rights reserved.
//

import Foundation
import SSZipArchive

public class DatabaseHelper {
    
    func getLessonDatabaseFile() -> String? {
        let databaseBundle = NSBundle.mainBundle()
        let dbZipPath = databaseBundle.pathForResource("database", ofType: "zip")
        print("Database zip path on bunddle \(dbZipPath)")
        let fileManager = NSFileManager.defaultManager()
        // get the documents folder url
        let paths = NSSearchPathForDirectoriesInDomains(NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.UserDomainMask, true)
        let documentsDirectory: AnyObject = paths[0]
        let dataPath = documentsDirectory.stringByAppendingPathComponent("database")
        print("Database directory path \(dataPath)")
        let lessonDbFilePath = documentsDirectory.stringByAppendingPathComponent("database/lesson.db")
        print("Lesson database path \(lessonDbFilePath)")
        if !fileManager.fileExistsAtPath(lessonDbFilePath) {
            do {
                var isDir = ObjCBool(true)
                if !fileManager.fileExistsAtPath(dataPath, isDirectory: &isDir) {
                    try fileManager.createDirectoryAtPath(dataPath, withIntermediateDirectories: false, attributes: nil)
                }
                } catch let error as NSError {
                    print(error.localizedDescription);
            }
            print("Try to unzip lesson database")
            SSZipArchive.unzipFileAtPath(dbZipPath, toDestination: dataPath)
        } else {
            print("Lesson database exist. Skip by default")
        }
        //let enumerator:NSDirectoryEnumerator = fileManager.enumeratorAtPath(documentsDirectory.stringByAppendingPathComponent(""))!
        //for url in enumerator.allObjects {
        //    print("\(url)")
        //}
        return lessonDbFilePath
    }
}
