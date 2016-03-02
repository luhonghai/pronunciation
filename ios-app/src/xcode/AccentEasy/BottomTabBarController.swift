//
//  BottomTabBarController.swift
//  AccentEasy
//
//  Created by Minh Kelly on 2/25/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

class BottomTabBarController: UITabBarController {
    
    @IBOutlet weak var tabbar: UITabBar!
    
    override func viewDidLoad() {
        let tabbarHeight:CGFloat = 50;
        //self.tabbar.frame = CGRectMake(0,0,self.view.frame.width,tabbarHeight);
//        for subView:UIView  in self.tabbar.subviews {
//            subView.frame = CGRectMake(0,tabbarHeight,subView.frame.width,subView.frame.height - tabbarHeight);
//        }
    }
}