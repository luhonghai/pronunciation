//
//  ExtensionDefaultFunciton.swift
//  AccentEasy
//
//  Created by CMGVN on 3/29/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

protocol DoubleConvertibleType {
    init(_ value: Double)
    var doubleValue: Double { get }
}

extension Double : DoubleConvertibleType { var doubleValue: Double { return self         } }
extension Float  : DoubleConvertibleType { var doubleValue: Double { return Double(self) } }
extension CGFloat: DoubleConvertibleType { var doubleValue: Double { return Double(self) } }
extension Int  : DoubleConvertibleType { var doubleValue: Double { return Double(self) } }

extension Array where Element: DoubleConvertibleType {
    var total: Element {
        guard !isEmpty else { return Element(0) }
        return  Element(reduce(0.0){$0 + $1.doubleValue})
    }
    var average: Element {
        guard !isEmpty else { return Element(0) }
        return  Element(total.doubleValue / Double(count))
    }
}

extension Array {
    func contains<U: Equatable>(object:U) -> Bool {
        return (self.indexOf(object) != nil);
    }
    
    func indexOf<U: Equatable>(object: U) -> Int? {
        for (idx, objectToCompare) in self.enumerate() {
            if let to = objectToCompare as? U {
                if object == to {
                    return idx
                }
            }
        }
        return nil
    }
    
    mutating func removeObject<U: Equatable>(object: U) {
        let index = self.indexOf(object)
        if(index != nil) {
            self.removeAtIndex(index!)
        }
    }
}


extension UITextView {
    func vAlignMiddle() {
        var topCorrection = (bounds.size.height - contentSize.height * zoomScale)/2.0
        topCorrection = max(0, topCorrection)
        contentInset = UIEdgeInsets(top: topCorrection, left: 0, bottom: 0, right: 0)

    }
}

extension UIViewController{
    func showLoadding(message : String = "Please wait..."){
        self.showWaitOverlayWithText(message)
        DeviceManager.showLockScreen()
    }
    
    func hidenLoadding(){
        DeviceManager.hideLockScreen()
        self.removeAllOverlays()
    }
    
    func showError(){
        dispatch_async(dispatch_get_main_queue(),{
            self.hidenLoadding()
            AccountManager.showError()
        })
    }
}