//
//  BottomTabBarController.swift
//  AccentEasy
//
//  Created by Minh Kelly on 2/25/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import Foundation

class BottomTabBarController: UITabBarController {
    
    @IBOutlet weak var tabbar: UITabBar!
    
    override func viewDidLoad() {
        self.tabbar.frame = CGRectMake(0,0,320,50);
    }
}