//
//  ResetPassVC.swift
//  AccentEasy
//
//  Created by CMGVN on 1/14/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class ResetPassVC: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var txtEmail: UITextField!
    
    @IBAction func btnOpenUrlTC(sender: AnyObject) {
        let url = NSURL(string: "http://www.accenteasy.com/useraccounts/TnC")!
        UIApplication.sharedApplication().openURL(url)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        txtEmail.delegate = self
        
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
        if textField == self.txtEmail {
            textField.resignFirstResponder()
            resetAE()
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

    
    @IBAction func resetTapped(sender: AnyObject) {
        DeviceManager.doIfConnectedToNetwork { (status) -> Void in
            if status {
            self.resetAE()
            }
        }
    }
    
    func resetAE(){
        /*
        data.put("acc", profile.getUsername());
        data.put("action", "request");
        data.put("imei", new DeviceUuidFactory(context).getDeviceUuid().toString());
        */
        showLoadding()
        let username = txtEmail.text!
        
        if !Login.isValidEmail(username) {
            dispatch_async(dispatch_get_main_queue(),{
                SweetAlert().showAlert("invalid email address", subTitle: "please enter a valid email address", style: AlertStyle.Error)
                self.hidenLoadding()
            })
            
            return
        }
        
        let profile = UserProfile()
        profile.username = username
        AccountManager.resetPassword(profile) { (userProfile, success, message) -> Void in
            dispatch_async(dispatch_get_main_queue(),{
                if success {
                    SweetAlert().showAlert("successfully submitted!", subTitle: message, style: AlertStyle.Success, buttonTitle: "Ok") {(isOk) -> Void in
                        if isOk == true {
                            self.performSegueWithIdentifier("ResetPassGoToLogin", sender: self)
                        }
                    }
                } else {
                    self.hidenLoadding()
                    AccountManager.showError("could not send feedback")
                }
            })
        }
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
