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
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        baseNotification = BaseNotification(viewController: self)
    }
    
    override func viewDidDisappear(animated: Bool) {
        super.viewDidDisappear(animated)
        if baseNotification != nil {
            Logger.log("run in BaseUIViewController deinit")
            baseNotification.clean()
        }
    }
    
}