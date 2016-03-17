//
//  FileHelper.swift
//  AccentEasy
//
//  Created by CMG on 29/01/2016.
//  Copyright © 2016 Hai Lu. All rights reserved.
//

import Foundation

class FileHelper {
    
    class func getAccentEasyBaseUrl() -> String {
        // TODO try to get base url by environment
        return "http://accenteasytomcat-sat.ap-southeast-1.elasticbeanstalk.com"
        //return "http://reg.accenteasy.com"
        //return "http://192.168.1.196:8080"
        //hoang pc
        //return "http://192.168.1.15:8080"
       // return "http://localhost:8080"
    }
    
    class func getFilePath(path: String, directory: Bool = false) -> String {
        let fileManager = NSFileManager.defaultManager()
        let paths = NSSearchPathForDirectoriesInDomains(NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.UserDomainMask, true)
        let documentsDirectory: AnyObject = paths[0]
        let folderPath = documentsDirectory.stringByAppendingPathComponent(path)
        if (directory) {
            do {
                var isDir = ObjCBool(true)
                if !fileManager.fileExistsAtPath(folderPath, isDirectory: &isDir) {
                    try fileManager.createDirectoryAtPath(folderPath, withIntermediateDirectories: false, attributes: nil)
                }
            } catch let error as NSError {
                print(error.localizedDescription);
            }
        }
        return folderPath;
    }
    
    class func isExists(path: String, directory: Bool = false) -> Bool {
        let fileManager = NSFileManager.defaultManager()
        if (directory) {
            var isDir = ObjCBool(true)
            return fileManager.fileExistsAtPath(path, isDirectory: &isDir)
        } else {
            return fileManager.fileExistsAtPath(path);
        }
    }
    
    class func writeFile(path: String, content: String) throws {
        do {
            try content.writeToFile(path, atomically: false, encoding: NSUTF8StringEncoding)
        } catch let e as NSError {
            throw e;
        }
    }
    
    class func readFile(path: String) throws -> String {
        do {
            return try String(contentsOfFile: path, encoding: NSUTF8StringEncoding)
        } catch let e as NSError {
            throw e;
        }
    }
    
    class func copyFile(fromPath: String, toPath: String) {
        do {
            try NSFileManager.defaultManager().copyItemAtPath(fromPath, toPath: toPath)
        } catch {
            
        }
    }
    
    class func deleteFile(path: String) {
        do {
            try NSFileManager.defaultManager().removeItemAtPath(path)
        } catch {
            
        }
    }
}