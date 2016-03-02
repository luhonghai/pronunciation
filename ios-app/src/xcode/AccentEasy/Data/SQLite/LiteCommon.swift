//
//  LiteCommon.swift
//  AccentEasy
//
//  Created by Hai Lu on 3/2/16.
//  Copyright © 2016 Hai Lu. All rights reserved.
//

import Foundation

public class LiteDatabase {
    public static let FREESTYLE = "database/freestyle.db"
    public static let LESSON = "database/lesson.db"
}

public class LiteTable {
    public static let PRONUNCIATION_SCORE = Table("PronunciationScore")
    public static let PHONEME_SCORE = Table("PhonemeScore")
    public static let IPA_MAP_ARPABET = Table("IPAMapArpabet")
    public static let WORD_COLLECTION = Table("WordCollection")
}

public class LiteColumn {
    public static let ID = Expression<Int64>("ID")
    public static let WORD = Expression<String?>("WORD")
    public static let SCORE = Expression<Int?>("SCORE")
    public static let DATA_ID = Expression<String?>("DATAID")
    public static let USERNAME = Expression<String?>("USERNAME")
    public static let VERSION = Expression<Int?>("VERSION")
    public static let TIME = Expression<Double?>("TIME")
    public static let INDEX = Expression<Int?>("INDEX")
    public static let NAME = Expression<String?>("NAME")
    public static let IPA = Expression<String?>("IPA")
    public static let ARPABET = Expression<String?>("ARPABET")
    public static let DEFINITION = Expression<String?>("DEFINITION")
    public static let MP3_PATH = Expression<String?>("MP3PATH")
    public static let MP3_URL = Expression<String?>("MP3URL")
    public static let PRONUNCIATION = Expression<String?>("PRONUNCIATION")
    public static let COLOR = Expression<String?>("COLOR")
    public static let DESCRIPTION = Expression<String?>("DESCRIPTION")
    public static let TIP = Expression<String?>("TIP")
    public static let TYPE = Expression<String?>("TYPE")
    public static let WORDS = Expression<String?>("WORDS")
    public static let IMG_TONGUE = Expression<String?>("IMGTONGUE")
    public static let MP3_URL_SHORT = Expression<String?>("MP3URLSHORT")
}

public class LiteEntity {
    public var id:Int64!
    
    public func parse(row: Row) {
        
    }
    
    public func setters() -> [Setter]? {
        return nil
    }
    
    public func table() -> Table? {
        return nil
    }
}






