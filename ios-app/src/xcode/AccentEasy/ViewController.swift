//
//  ViewController.swift
//  SwiftSidebarMenu
//
//  Created by CMGVN on 1/7/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    var loginParameter:NSUserDefaults!
    
    @IBOutlet weak var txtUsername: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        loginParameter = NSUserDefaults()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
        
    }

    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        loginParameter.setObject(txtUsername.text, forKey: "username")
    }

}

