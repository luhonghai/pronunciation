//
//  ColorHelper.swift
//  AccentEasy
//
//  Created by Hai Lu on 04/02/2016.
//  Copyright © 2016 Hai Lu. All rights reserved.
//

import Foundation

public class ColorHelper {
    public static let APP_GRAY = Multimedia.colorWithHexString("#929292")
    public static let APP_TINT = Multimedia.colorWithHexString("#e7e7e7")
    public static let APP_GREEN = Multimedia.colorWithHexString("#579e11")
    public static let APP_ORANGE = Multimedia.colorWithHexString("#ff7548")
    public static let APP_RED = Multimedia.colorWithHexString("#ff3333")
    public static let APP_DEFAULT = Multimedia.colorWithHexString("#003da7")
    public static let APP_BLUE_SKY = Multimedia.colorWithHexString("#33b5e5")
    public static let APP_PURPLE = Multimedia.colorWithHexString("#7030a0")
    public static let APP_LIGHT_PURPLE = Multimedia.colorWithHexString("#c0beff")
    public static let APP_AQUA = Multimedia.colorWithHexString("#1f7180")
    public static let APP_LIGHT_AQUA = Multimedia.colorWithHexString("#aeebe7")
    public static let APP_DARK_GRAY = Multimedia.colorWithHexString("#6c6c6c")
    public static let APP_LIGHT_GRAY = Multimedia.colorWithHexString("#d0d0d0")
    public static let APP_LIGHT_GREEN = Multimedia.colorWithHexString("#9bdaa3")
    public static let APP_LIGHT_BLUE = Multimedia.colorWithHexString("#e1f7f8")
    public static let APP_LIGHT_BLUE_BORDER = Multimedia.colorWithHexString("#daedf3")
    public static let APP_BLUR_LIGHT_BLUE = Multimedia.colorWithHexString("#f0fcfc")
    
    class func generateGradientColor(color1:UIColor, color2: UIColor, radio: CGFloat) -> UIColor {
        let ciColor1 = CoreImage.CIColor(color: color1)
        let ciColor2 = CoreImage.CIColor(color: color2)
        let red = radio * ciColor1.red + (1.0 - radio) * ciColor2.red
        let green = radio * ciColor1.green + (1.0 - radio) * ciColor2.green
        let blue = radio * ciColor1.blue + (1.0 - radio) * ciColor2.blue
        let alpha = radio * ciColor1.alpha + (1.0 - radio) * ciColor2.alpha
        return UIColor(red: red, green: green, blue: blue, alpha: alpha)
    }
    
    public class func returnColorOfScore(score:Int) -> UIColor{
        if score < 45 {
            //color < 45 red
            return ColorHelper.APP_RED
        } else if score >= 45 && score < 80 {
            // 45 <= color < 80 orange
            return ColorHelper.APP_ORANGE
        } else {
            //color >= 80 green
            return ColorHelper.APP_GREEN
        }
        
    }
}