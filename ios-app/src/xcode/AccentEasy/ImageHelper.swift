//
//  ImageHelper.swift
//  AccentEasy
//
//  Created by Minh Kelly on 2/25/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import Foundation

class ImageHelper {
    class func imageWithImage(image:UIImage, scaledToSize newSize:CGSize) -> UIImage{
        UIGraphicsBeginImageContextWithOptions(newSize, false, 0.0);
        image.drawInRect(CGRectMake(0, 0, newSize.width, newSize.height))
        let newImage:UIImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return newImage
    }
}