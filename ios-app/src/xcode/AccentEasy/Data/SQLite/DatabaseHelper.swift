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
    
    class func checkDatabaseVersion() -> Bool {
        // Create database folder if not exist
        FileHelper.getFilePath("database", directory: true)
        let versionPath = FileHelper.getFilePath("database/version")
        if (FileHelper.isExists(versionPath)) {
            
        }
        let client = Client()
            .baseUrl(FileHelper.getAccentEasyBaseUrl())
            .onError({e in print(e)});
        
         client.get("/CheckVersion").send([])
            .end({(res:Response) -> Void in
                print(res)
                if(res.error) { // status of 2xx
                    //handleResponseJson(res.body)
                    //print(res.body)
                    print(res.text)
                }
                else {
                    //handleErrorJson(res.body)
                    print(res.text)
                    let result:String = res.text!
                    
                }
            })
        
        HttpDownloader.loadFileSync(NSURL(fileURLWithPath: "")) { (path, error) -> Void in
            
        }
        return false;
    }
    
    class func getFreeStyleDatabaseFile() -> String? {
        return FileHelper.getFilePath(LiteDatabase.FREESTYLE)
    }
    
    class func getLessonDatabaseFile() -> String? {
        let databaseBundle = NSBundle.mainBundle()
        let dbZipPath = databaseBundle.pathForResource("database", ofType: "zip")
        print("Database zip path on bunddle \(dbZipPath)")
        let fileManager = NSFileManager.defaultManager()
        // get the documents folder url
        let dataPath = FileHelper.getFilePath("database", directory: true)
        print("Database directory path \(dataPath)")
        let lessonDbFilePath = FileHelper.getFilePath(LiteDatabase.LESSON)
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
