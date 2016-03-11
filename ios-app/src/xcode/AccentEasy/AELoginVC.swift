//
//  AELoginVC.swift
//  AccentEasy
//
//  Created by CMGVN on 1/14/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit
class AELoginVC: UIViewController {


    var currentUser: UserProfile!
    
    @IBOutlet weak var txtEmail: UITextField!
    @IBOutlet weak var txtPassword: UITextField!
    
    
    @IBAction func btnOpenUrlTC(sender: AnyObject) {
        
        let url = NSURL(string: "http://www.accenteasy.com/useraccounts/TnC")!
        UIApplication.sharedApplication().openURL(url)
        
    }
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        userProfileSaveInApp = NSUserDefaults()
        //txtEmail.autocorrectionType = UITextAutocorrectionType.No
        
        //Looks for single or multiple taps.
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: "dismissKeyboard")
        view.addGestureRecognizer(tap)
        
    }
    
    //Calls this function when the tap is recognized.
    func dismissKeyboard() {
        //Causes the view (or one of its embedded text fields) to resign the first responder status.
        view.endEditing(true)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        
    }
    

    
    @IBAction func loginTapped(sender: AnyObject) {
        
        let username:String = txtEmail.text!
        let password:String = txtPassword.text!
        
        if username.isEmpty || password.isEmpty {
            dispatch_async(dispatch_get_main_queue(),{
                SweetAlert().showAlert("not enough data", subTitle: "please enter username and password", style: AlertStyle.Error)
                
            })
            return
        }
        
        let userProfile = UserProfile()
        userProfile.username = txtEmail.text!
        userProfile.password = txtPassword.text!
        userProfile.loginType = UserProfile.TYPE_EASYACCENT
        
        currentUser = userProfile
        
        weak var weakSelf = self;
        AccountManager.auth(currentUser) { (userProfile, success, message) -> Void in
            dispatch_async(dispatch_get_main_queue(),{
                if success {
                    weakSelf!.currentUser = userProfile
                    weakSelf!.getUserProfile()
                } else {
                    AccountManager.showError("could not login", message: message)
                }
            })
        }
    }

    func getUserProfile () {
        weak var weakSelf = self
        AccountManager.fetchProfile(currentUser) { (userProfile, success, message) -> Void in
            dispatch_async(dispatch_get_main_queue(),{
                if success {
                    userProfile.isLogin = true
                    AccountManager.updateProfile(userProfile)
                    weakSelf!.performSegueWithIdentifier("AELoginGoToMain", sender: weakSelf!)
                } else {
                    AccountManager.showError("could not fetch user data")
                }
            })
        }
    }
}
