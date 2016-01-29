//
//  OurViewController.swift
//  SwiftSidebarMenu
//
//  Created by CMGVN on 1/7/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import UIKit
import ObjectMapper

class OurViewController: UIViewController {

    var loginParameter:NSUserDefaults!
    var userProfileSaveInApp:NSUserDefaults!
    var JSONStringUserProfile:String!
    var userProfile = UserProfile()
    @IBOutlet weak var menuButton: UIBarButtonItem!
    @IBOutlet weak var lblUsername: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        //menu
        if self.revealViewController() != nil {
            menuButton.target = self.revealViewController()
            menuButton.action = "revealToggle:"
            self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
        }
        
        //login parameter
        loginParameter = NSUserDefaults()
        
        userProfileSaveInApp = NSUserDefaults()
        let keyForUserProfile:String = userProfileSaveInApp.objectForKey(Login.KeyUserProfile) as! String
        JSONStringUserProfile = userProfileSaveInApp.objectForKey(keyForUserProfile) as! String
        print("login successfull")
        print(JSONStringUserProfile)
        userProfile = Mapper<UserProfile>().map(JSONStringUserProfile)!
        lblUsername.text = userProfile.username
    }
    
    override func viewDidAppear(animated: Bool) {
        loginParameter = NSUserDefaults()
        //let username:String = loginParameter.objectForKey("username") as! String
        //if username != "hoang" {
            //self.performSegueWithIdentifier("goto_login", sender: self)
        //}else{
            //lblUsername.text = username
            //lblUsername.text = ""
        //}
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        //loginParameter.setObject(nil, forKey: "username")
    }

    @IBAction func backTapped(sender: AnyObject) {
        self.performSegueWithIdentifier("MainGoToLogin", sender: self)
    }
    
    /*
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }*/
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
