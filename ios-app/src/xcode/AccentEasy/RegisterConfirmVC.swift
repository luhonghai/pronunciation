//
//  RegisterConfirmVC.swift
//  AccentEasy
//
//  Created by CMGVN on 1/19/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class RegisterConfirmVC: UIViewController {

    var currentUser:UserProfile!
    
    @IBOutlet weak var txtcode: UITextField!
    
    @IBAction func btnOpenUrlTC(sender: AnyObject) {
        let url = NSURL(string: "http://www.accenteasy.com/useraccounts/TnC")!
        UIApplication.sharedApplication().openURL(url)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        currentUser = AccountManager.currentUser()
        txtcode.placeholder = currentUser.username
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func confirmCodeTapped(sender: AnyObject) {
        let codeConfirm:String = txtcode.text!
        AccountManager.activate(codeConfirm, userProfile: currentUser) { (userProfile, success, message) -> Void in
            dispatch_async(dispatch_get_main_queue(),{
                if success {
                    SweetAlert().showAlert("validation successful!", subTitle: message, style: AlertStyle.Success, buttonTitle: "Ok") {(isOk) -> Void in
                        if isOk == true {
                            self.performSegueWithIdentifier("ConfirmRegisterGoToLogin", sender: self)
                        }
                    }
                } else {
                    AccountManager.showError("could not activate", message: message)
                }
            })
        }
    }

    @IBAction func sendCodeAgainTapped(sender: AnyObject) {
        AccountManager.resendCode(currentUser) { (userProfile, success, message) -> Void in
            dispatch_async(dispatch_get_main_queue(),{
                if success {
                    SweetAlert().showAlert("successfully submitted!", subTitle: message, style: AlertStyle.Success)
                } else {
                    AccountManager.showError("could resend code", message: message)
                }
            })
        }
    }
    
    @IBAction func proceeedLoginTapped(sender: AnyObject) {
        self.performSegueWithIdentifier("ConfirmRegisterGoToLogin", sender: self)
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
