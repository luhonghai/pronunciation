//
//  TestNewScreenViewController.swift
//  SwiftSidebarMenu
//
//  Created by CMGVN on 1/8/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import UIKit

class TestNewScreenViewController: UIViewController {

    
    @IBOutlet weak var btnViewOutlet: UIButton!
    @IBOutlet weak var lblTesst: UILabel!
    @IBOutlet var lblView: UILabel!
    @IBAction func btnView(sender: AnyObject) {
        //code
        lblView.text = "hoang.nguyen"
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //btnViewOutlet.setTitle("enter", forState: UIControlState)
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
