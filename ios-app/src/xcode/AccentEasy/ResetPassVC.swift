//
//  ResetPassVC.swift
//  AccentEasy
//
//  Created by CMGVN on 1/14/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import UIKit

class ResetPassVC: UIViewController {

    @IBAction func btnOpenUrlTC(sender: AnyObject) {
        let url = NSURL(string: "http://www.accenteasy.com/useraccounts/TnC")!
        UIApplication.sharedApplication().openURL(url)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
