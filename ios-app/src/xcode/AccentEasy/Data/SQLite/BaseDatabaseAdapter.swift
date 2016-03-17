//
//  BaseDatabaseAdapter.swift
//  AccentEasy
//
//  Created by Hai Lu on 20/01/2016.
//  Copyright © 2016 Hai Lu. All rights reserved.
//

import Foundation



public class BaseDatabaseAdapter {
    
    public var db:Connection?
    
    init(dbFile:String) {
        do {
            db = try Connection(dbFile)
        }catch let error as NSError {
            print(error.localizedDescription);
        }
    }
    
    public func prepare() {
    }

    public func insert<T: LiteEntity>(obj: T) throws {
        try db!.run(obj.table()!.insert(obj.setters()!))
    }
    
    public func query<T: LiteEntity>(table: Table) throws -> Array<T> {
        return try query(db!.prepare(table))
    }
    
    public func query<T: LiteEntity>(statement: Statement) throws -> Array<T> {
        return try query(statement)
    }
    
    public func query<T: LiteEntity>(rows: AnySequence<Row>) throws -> Array<T> {
        var list = Array<T>()
        for row in rows {
            let obj = T()
            obj.parse(row)
            list.append(obj)
        }
        return list
    }
    
    public func findAll<T: LiteEntity>() throws -> Array<T> {
        return try query(T().table()!)
    }
    
    public func delete<T: LiteEntity>(obj: T) throws -> T {
        try db!.run((obj.table()?.filter(LiteColumn.ID == obj.id).delete())!)
        return obj
    }
    
    public func delete<T: LiteEntity>(id: Int64) throws -> T {
        return try delete(T(id: id))
    }
    
    public func update<T: LiteEntity>(obj: T) throws {
        try db!.run(obj.table()!.filter(LiteColumn.ID == obj.id).update(obj.setters()!))
    }
    
    public func find<T: LiteEntity>(table: Table) throws -> T! {
        let list:Array<T> = try query(table)
        if !list.isEmpty {
            return list[0]
        }
        return nil
    }
    
    public func find<T: LiteEntity>(statement: Statement) throws -> T! {
        return try find(statement)
    }
    
    public func find<T: LiteEntity>(rows: AnySequence<Row>) throws -> T! {
        let list:Array<T> = try query(rows)
        if !list.isEmpty {
            return list[0]
        }
        return nil
    }

    
    public func findById<T: LiteEntity>(id: Int64) throws -> T {
        return try find((T(id: id).table()?.filter(LiteColumn.ID == id))!)
    }
}