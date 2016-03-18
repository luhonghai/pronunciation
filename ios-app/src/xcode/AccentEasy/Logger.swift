//
//  Logger.swift
//  AccentEasy
//
//  Created by Hai Lu on 18/03/2016.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation
import NSLogger

public class Logger {
    
    class func log(message: AnyObject!) {
        LogMessageRaw(message as! String)
//        NSLog(message as! String)
        //
    }
    
    class func logError(error: ErrorType!) {
        NSLog("error \(error)")
        print(error)
        
    }
    
    class func startService() {
        let logger = LoggerInit()
        let hostName = NSProcessInfo().environment["NSLoggerViewerHost"] ?? "localhost"
        let bonjourName = NSProcessInfo().environment["NSLoggerBonjourName"] ?? "accenteasy-nslogger"
        LoggerSetViewerHost(logger, hostName, 50000)
        LoggerSetupBonjour(logger, nil, bonjourName)
        LoggerSetOptions(logger, UInt32(
            kLoggerOption_BufferLogsUntilConnection |
            kLoggerOption_UseSSL |
            kLoggerOption_BrowseBonjour |
            kLoggerOption_BrowseOnlyLocalDomain
        ))
        LoggerStart(logger)
    }
}
