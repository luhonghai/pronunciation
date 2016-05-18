//
//  BaseUITableViewController.swift
//  AccentEasy
//
//  Created by CMGVN on 5/17/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

class BaseUITableViewController: UITableViewController {
    
    var baseNotification: BaseNotification!
        
    override func viewDidLoad() {
        super.viewDidLoad()
        baseNotification = BaseNotification(viewController: self)
    }
        
    deinit {
        baseNotification.clean()
    }
    
}