//
//  AELoginVC.swift
//  AccentEasy
//
//  Created by CMGVN on 1/14/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit
class AELoginVC: UIViewController, UITextFieldDelegate {


    var currentUser: UserProfile!
    
    @IBOutlet weak var txtEmail: UITextField!
    @IBOutlet weak var txtPassword: UITextField!
    
    
    @IBAction func btnOpenUrlTC(sender: AnyObject) {
        
        let url = NSURL(string: "http://www.accenteasy.com/useraccounts/TnC")!
        UIApplication.sharedApplication().openURL(url)
        
    }
    override func viewDidLoad() {
        super.viewDidLoad()

        txtEmail.autocorrectionType = UITextAutocorrectionType.No
        txtEmail.delegate = self
        txtPassword.delegate = self
        //Looks for single or multiple taps.
        //let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: "dismissKeyboard")
       // view.addGestureRecognizer(tap)
        
    }
    
    //Calls this function when the tap is recognized.
    func dismissKeyboard() {
        //Causes the view (or one of its embedded text fields) to resign the first responder status.
        view.endEditing(true)
    }
    
    /**
     * Called when 'return' key pressed. return NO to ignore.
     */
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        if textField == self.txtEmail {
            self.txtPassword.becomeFirstResponder()
        }else if textField == self.txtPassword {
            textField.resignFirstResponder()
            loginAE()
        }
        return true
    }
    
    
    /**
     * Called when the user click on the view (outside the UITextField).
     */
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true)
    }
    

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        
    }
    
    func showLoadding(){
        //show watting..
        let text = "Please wait..."
        self.showWaitOverlayWithText(text)
    }
    
    func hidenLoadding(){
        // Remove watting
        self.removeAllOverlays()
    }


    
    @IBAction func loginTapped(sender: AnyObject) {
        DeviceManager.doIfConnectedToNetwork { () -> Void in
            self.loginAE()
        }
    }
    
    func loginAE() {
        showLoadding()
        let username:String = txtEmail.text!
        let password:String = txtPassword.text!
        
        if username.isEmpty || password.isEmpty {
            dispatch_async(dispatch_get_main_queue(),{
                SweetAlert().showAlert("not enough data", subTitle: "please enter username and password", style: AlertStyle.Error)
                self.hidenLoadding()
            })
            return
        }else if !Login.isValidEmail(username) {
            dispatch_async(dispatch_get_main_queue(),{
                SweetAlert().showAlert("invalid email address", subTitle: "please enter a valid email address", style: AlertStyle.Error)
                self.hidenLoadding()
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
                    weakSelf!.hidenLoadding()
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
                    weakSelf!.hidenLoadding()
                    weakSelf!.performSegueWithIdentifier("AELoginGoToMain", sender: weakSelf!)
                } else {
                    AccountManager.showError("could not fetch user data")
                    weakSelf!.hidenLoadding()
                }
            })
        }
    }
}
