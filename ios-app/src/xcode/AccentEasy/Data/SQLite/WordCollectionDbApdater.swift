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
                   // Logger.log("Complete download ipa audio \(ipa.mp3URL)")
                })
            }
            if ipa.mp3URLShort != nil && !ipa.mp3URLShort.isEmpty {
                HttpDownloader.loadFileSync(NSURL(string: ipa.mp3URLShort)!, completion: { (path, error) -> Void in
                    //Logger.log("Complete download ipa audio \(ipa.mp3URLShort)")
                })
            }
            /*if ipa.imgTongue != nil && !ipa.imgTongue.isEmpty {
                try! ImageLoader.load(NSURL(string: ipa.imgTongue)!)
            }*/
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
    
    public func checkIPAContainVideo() throws -> Bool {
        if let _: IPAMapArpabet = try find(LiteTable.IPA_MAP_ARPABET.filter(LiteColumn.YOUTUBE_VIDEO_ID != nil && LiteColumn.YOUTUBE_VIDEO_ID != "")) {
            return true
        } else {
            return false
        }
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
}