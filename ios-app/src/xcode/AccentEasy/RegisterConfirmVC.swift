//
//  RegisterConfirmVC.swift
//  AccentEasy
//
//  Created by CMGVN on 1/19/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class RegisterConfirmVC: UIViewController, UITextFieldDelegate {

    var currentUser:UserProfile!
    
    @IBOutlet weak var txtcode: UITextField!
    
    @IBAction func btnOpenUrlTC(sender: AnyObject) {
        let url = NSURL(string: "http://www.accenteasy.com/useraccounts/TnC")!
        UIApplication.sharedApplication().openURL(url)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        currentUser = AccountManager.currentUser()
        
        txtcode.delegate = self
        txtcode.placeholder = currentUser.username
        
        DeviceManager.setNavigationBarTransparent(self)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    /**
     * Called when 'return' key pressed. return NO to ignore.
     */
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        if textField == self.txtcode {
            textField.resignFirstResponder()
            confirmCodeAE()
        }
        return true
    }
    
    
    /**
     * Called when the user click on the view (outside the UITextField).
     */
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true)
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
    
    @IBAction func confirmCodeTapped(sender: AnyObject) {
        DeviceManager.doIfConnectedToNetwork { () -> Void in
            self.confirmCodeAE()
        }
    }
    
    func confirmCodeAE() {
        showLoadding()
        let codeConfirm:String = txtcode.text!
        Logger.log(codeConfirm)
        AccountManager.activate(codeConfirm, userProfile: currentUser) { (userProfile, success, message) -> Void in
            dispatch_async(dispatch_get_main_queue(),{
                if success {
                    Logger.log("success response")
                    SweetAlert().showAlert("validation successful!", subTitle: message, style: AlertStyle.Success, buttonTitle: "Ok") {(isOk) -> Void in
                        if isOk == true {
                            self.performSegueWithIdentifier("ConfirmRegisterGoToLogin", sender: self)
                        }
                    }
                } else {
                    Logger.log("error response")
                    self.hidenLoadding()
                    AccountManager.showError("could not activate", message: message)
                }
            })
        }

    }

    @IBAction func sendCodeAgainTapped(sender: AnyObject) {
        showLoadding()
        AccountManager.resendCode(currentUser) { (userProfile, success, message) -> Void in
            dispatch_async(dispatch_get_main_queue(),{
                if success {
                    self.hidenLoadding()
                    SweetAlert().showAlert("successfully submitted!", subTitle: message, style: AlertStyle.Success)
                } else {
                    self.hidenLoadding()
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
