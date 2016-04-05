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



extension UITextView {
    func vAlignMiddle() {
        var topCorrection = (bounds.size.height - contentSize.height * zoomScale)/2.0
        topCorrection = max(0, topCorrection)
        contentInset = UIEdgeInsets(top: topCorrection, left: 0, bottom: 0, right: 0)

    }
}