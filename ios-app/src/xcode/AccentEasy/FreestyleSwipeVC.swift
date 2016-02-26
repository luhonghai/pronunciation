//
//  MySwipe.swift
//  EZSwipeController
//
//  Created by Goktug Yilmaz on 14/11/15.
//  Copyright © 2015 Goktug Yilmaz. All rights reserved.
//

import UIKit
// import EZSwipeController // if using Cocoapods
class FreestyleSwipeVC: EZSwipeController {
    
    var navigationBar = UINavigationBar()
    var navigationItemMain:UINavigationItem?
    
    override func setupView() {
        datasource = self
        //        navigationBarShouldBeOnBottom = true
        //        navigationBarShouldNotExist = true
        //        cancelStandardButtonEvents = true
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = UIColor(red: 231/255, green: 231/255, blue: 231/255, alpha: 1)
    }
}

extension FreestyleSwipeVC: EZSwipeControllerDataSource {
    
    func viewControllerData() -> [UIViewController] {
        let redVC = self.storyboard?.instantiateViewControllerWithIdentifier("OurViewController")
        //redVC.view.backgroundColor = UIColor.redColor()
        //let char = UIImageView(x: 50, y: 100, imageName: "char", scaleToWidth: 300)
        //redVC.view.addSubview(char)
        
        let blueVC = UIViewController()
        blueVC.view.backgroundColor = UIColor.blueColor()
        //let squir = UIImageView(x: 50, y: 100, imageName: "squir", scaleToWidth: 300)
        //blueVC.view.addSubview(squir)
        
        let greenVC = UIViewController()
        greenVC.view.backgroundColor = UIColor.greenColor()
        //let bulb = UIImageView(x: 50, y: 125, imageName: "bulb", scaleToWidth: 300)
        //greenVC.view.addSubview(bulb)
        
        return [redVC!, blueVC, greenVC]
    }
    
    func navigationBarDataForPageIndex(index: Int) -> UINavigationBar {
        var title = ""
        if index == 0 {
            title = "Charmander"
        } else if index == 1 {
            title = "Squirtle"
        } else if index == 2 {
            title = "Bulbasaur"
        }
        
        navigationBar = UINavigationBar()
        navigationBar.barStyle = UIBarStyle.Default
        navigationBar.barTintColor = UIColor(red: 231/255, green: 231/255, blue: 231/255, alpha: 1)
        navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: UIColor.blackColor()]
        
        let navigationItem = UINavigationItem(title: title)
        navigationItem.hidesBackButton = true
        
        if index == 0 {
            var aImage = UIImage(named: "Menu-25")!
            aImage = scaleTo(image: aImage, w: 22, h: 22)
            let leftButtonItem = UIBarButtonItem(image: aImage, style: UIBarButtonItemStyle.Plain, target: self, action: "a")
            leftButtonItem.tintColor = UIColor.blueColor()
            
            var sImage = UIImage(named: "Menu-25")!
            sImage = scaleTo(image: sImage, w: 22, h: 22)
            let rightButtonItem = UIBarButtonItem(image: sImage, style: UIBarButtonItemStyle.Plain, target: self, action: "a")
            rightButtonItem.tintColor = UIColor.blueColor()
            
            navigationItem.leftBarButtonItem = leftButtonItem
            navigationItem.rightBarButtonItem = rightButtonItem
        } else if index == 1 {
            var cImage = UIImage(named: "Menu-25")!
            cImage = scaleTo(image: cImage, w: 22, h: 22)
            let leftButtonItem = UIBarButtonItem(image: cImage, style: UIBarButtonItemStyle.Plain, target: self, action: "a")
            leftButtonItem.tintColor = UIColor.redColor()
            
            var bImage = UIImage(named: "Menu-25")!
            bImage = scaleTo(image: bImage, w: 22, h: 22)
            let rightButtonItem = UIBarButtonItem(image: bImage, style: UIBarButtonItemStyle.Plain, target: self, action: "a")
            rightButtonItem.tintColor = UIColor.greenColor()
            
            navigationItem.leftBarButtonItem = leftButtonItem
            navigationItem.rightBarButtonItem = rightButtonItem
        } else if index == 2 {
            var sImage = UIImage(named: "Menu-25")!
            sImage = scaleTo(image: sImage, w: 22, h: 22)
            let leftButtonItem = UIBarButtonItem(image: sImage, style: UIBarButtonItemStyle.Plain, target: self, action: "a")
            leftButtonItem.tintColor = UIColor.blueColor()
            
            navigationItem.leftBarButtonItem = leftButtonItem
            navigationItem.rightBarButtonItem = nil
        }
        navigationBar.pushNavigationItem(navigationItem, animated: false)
        return navigationBar
    }
    
    func alert(title title: String?, message: String, action: String) {
        if #available(iOS 8.0, *) {
            let alert = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.Alert)
            alert.addAction(UIAlertAction(title: action, style: UIAlertActionStyle.Default, handler: nil))
            self.presentViewController(alert, animated: true, completion: nil)
        }
    }
    
    func disableSwipingForLeftButtonAtPageIndex(index: Int) -> Bool {
        switch index {
        case 0:
            print("0 left")
            return true
        case 1:
            print("1 left")
            return false
        case 2:
            print("2 left")
            return false
        case 3:
            print("3 left")
            return false
        case 4:
            print("4 left")
            return false
        default:
            return false
        }
    }
    
    func clickedLeftButtonFromPageIndex(index: Int) {
        print("index left is: \(index)")
        
        var title = ""
        if index == 0 {
            title = "Charmander"
        } else if index == 1 {
            title = "Squirtle"
        } else if index == 2 {
            title = "Bulbasaur"
        }
        
        switch index {
        case 0:
            print("0 left")
            //menu
            if self.revealViewController() != nil {
                //UINavigationBar
                //navigationItemMain?.leftBarButtonItem?.target = self.revealViewController()
                //navigationItemMain?.leftBarButtonItem?.action = "revealToggle:"
                //self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
                self.performSegueWithIdentifier("FSGoToMenu", sender: self)
            }
            
        case 1:
            print("1 left")
        case 2:
            print("2 left")
        case 3:
            print("3 left")
        case 4:
            print("4 left")
        default:
            print("default left")
        }
        //if index == 1 {
            //print("What!?! Squirtle is evolving!!")
       // }
    }
    
    func clickedRightButtonFromPageIndex(index: Int) {
        print("index right is: \(index)")
        switch index {
        case 0:
            print("0 right")
        case 1:
            print("1 right")
        case 2:
            print("2 right")
        case 3:
            print("3 right")
        case 4:
            print("4 right")
        default:
            print("default right")
        }

    }
    
}

private func scaleTo(image image: UIImage, w: CGFloat, h: CGFloat) -> UIImage {
    let newSize = CGSize(width: w, height: h)
    UIGraphicsBeginImageContextWithOptions(newSize, false, 0.0)
    image.drawInRect(CGRect(x: 0, y: 0, width: newSize.width, height: newSize.height))
    let newImage: UIImage = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()
    return newImage
}

func test(){
    print("run in test function")
}


private extension UIImageView {
    
    /// EZSwiftExtensions
    convenience init(x: CGFloat, y: CGFloat, w: CGFloat, h: CGFloat, imageName: String) {
        self.init(frame: CGRect(x: x, y: y, width: w, height: h))
        image = UIImage(named: imageName)
    }
    
    /// EZSwiftExtensions
    convenience init(x: CGFloat, y: CGFloat, imageName: String, scaleToWidth: CGFloat) {
        self.init(frame: CGRect(x: x, y: y, width: 0, height: 0))
        image = UIImage(named: imageName)
        scaleImageFrameToWidth(width: scaleToWidth)
    }
    
    /// EZSwiftExtensions
    convenience init(x: CGFloat, y: CGFloat, w: CGFloat, h: CGFloat, image: UIImage) {
        self.init(frame: CGRect(x: x, y: y, width: w, height: h))
        self.image = image
    }
    
    /// EZSwiftExtensions
    convenience init(x: CGFloat, y: CGFloat, image: UIImage, scaleToWidth: CGFloat) {
        self.init(frame: CGRect(x: x, y: y, width: 0, height: 0))
        self.image = image
        scaleImageFrameToWidth(width: scaleToWidth)
    }
    
    /// EZSwiftExtensions, scales this ImageView size to fit the given width
    func scaleImageFrameToWidth(width width: CGFloat) {
        let widthRatio = image!.size.width / width
        let newWidth = image!.size.width / widthRatio
        let newHeigth = image!.size.height / widthRatio
        frame = CGRect(x: frame.origin.x, y: frame.origin.y, width: newWidth, height: newHeigth)
    }
    
}
