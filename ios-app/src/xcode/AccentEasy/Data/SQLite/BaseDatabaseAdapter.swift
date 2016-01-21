//
//  BaseDatabaseAdapter.swift
//  AccentEasy
//
//  Created by CMG on 20/01/2016.
//  Copyright © 2016 Hai Lu. All rights reserved.
//

import Foundation
import SQLite

public class BaseDatabaseAdapter {
    
    var db:Connection?
    
    init(dbFile:String) {
        do {
            db = try Connection(dbFile)
        }catch let error as NSError {
            print(error.localizedDescription);
        }
    }
    
}