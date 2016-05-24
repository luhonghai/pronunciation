//
//  RegisterVC.swift
//  AccentEasy
//
//  Created by CMGVN on 1/14/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class RegisterVC: UIViewController, UITextFieldDelegate{
    
    @IBOutlet weak var txtFirstname: UITextField!
    @IBOutlet weak var txtLastname: UITextField!
    @IBOutlet weak var txtEmail: UITextField!
    @IBOutlet weak var txtPassword: UITextField!
    @IBOutlet weak var txtConfirmPassword: UITextField!
    
    var currentUser: UserProfile!
    
    @IBAction func btnOpenUrlTC(sender: AnyObject) {
        let url = NSURL(string: "http://www.accenteasy.com/useraccounts/TnC")!
        UIApplication.sharedApplication().openURL(url)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        txtFirstname.delegate = self
        txtLastname.delegate = self
        txtEmail.delegate = self
        txtPassword.delegate = self
        txtConfirmPassword.delegate = self
        
        txtEmail.autocorrectionType = UITextAutocorrectionType.No
        //self.performSegueWithIdentifier("GoToComfirmCode", sender: self)
        
        DeviceManager.setNavigationBarTransparent(self)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        //if("GoToComfirmCode" == segue.identifier){
            //registerInfo.setObject(userProfile, forKey: "userProfile")
            //registerInfo.setObject(JSONStringUserProfile, forKey: "JSONStringUserProfile")
            //userProfileSaveInApp.setObject(self.JSONStringUserProfile, forKey: self.keyForProfile)
            //userProfileSaveInApp.setObject(self.keyForProfile, forKey: "KeyUserProfile")
        //}
    }
    
    @IBOutlet weak var bottomConstraint: NSLayoutConstraint!
    func keyboardWasShown(notification: NSNotification) {
        var info = notification.userInfo!
        let keyboardFrame: CGRect = (info[UIKeyboardFrameEndUserInfoKey] as! NSValue).CGRectValue()
        
        UIView.animateWithDuration(0.1, animations: { () -> Void in
            self.bottomConstraint.constant = keyboardFrame.size.height + 20
        })
    }
    
    
    func textFieldDidBeginEditing(textField: UITextField) {
        animateViewMoving(true, moveValue: 100)
        Logger.log("textFieldDidBeginEditing")
    }
    func textFieldDidEndEditing(textField: UITextField) {
        animateViewMoving(false, moveValue: 100)
        Logger.log("textFieldDidEndEditing")
    }
    
    func animateViewMoving (up:Bool, moveValue :CGFloat){
        let movementDuration:NSTimeInterval = 0.3
        let movement:CGFloat = ( up ? -moveValue : moveValue)
        UIView.beginAnimations( "animateView", context: nil)
        UIView.setAnimationBeginsFromCurrentState(true)
        UIView.setAnimationDuration(movementDuration )
        self.view.frame = CGRectOffset(self.view.frame, 0,  movement)
        UIView.commitAnimations()
    }
    
    
    
    /**
     * Called when 'return' key pressed. return NO to ignore.
     */
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        switch textField {
        case self.txtFirstname:
            self.txtLastname.becomeFirstResponder()
            break
        case self.txtLastname:
            self.txtEmail.becomeFirstResponder()
            break
        case self.txtEmail:
            self.txtPassword.becomeFirstResponder()
            break
        case self.txtPassword:
            self.txtConfirmPassword.becomeFirstResponder()
            break
        case self.txtConfirmPassword:
            textField.resignFirstResponder()
            registerAE()
            break
        default:
            textField.resignFirstResponder()
            break
        }
        return true
    }
    
    
    /**
     * Called when the user click on the view (outside the UITextField).
     */
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true)
    }
        
    @IBAction func registerTapped(sender: AnyObject) {
        DeviceManager.doIfConnectedToNetwork { (status) -> Void in
            if status {
            self.registerAE()
            }
        }

    }

    func registerAE(){
        showLoadding()
        /*
        data.put("version_code", AndroidHelper.getVersionCode(context));
        data.put("profile", gson.toJson(profile));
        data.put("lang_prefix", "BE");
        data.put("imei", new DeviceUuidFactory(context).getDeviceUuid().toString());
        */
        let firstname:String = txtFirstname.text!
        let lastname:String = txtLastname.text!
        let username:String = txtEmail.text!
        let password:String = txtPassword.text!
        let confirmPassword:String = txtConfirmPassword.text!
        
        if username.isEmpty || !Login.isValidEmail(username) {
            hidenLoadding()
            SweetAlert().showAlert("invalid email address", subTitle: "please enter a valid email address", style: AlertStyle.Error)
            return
        } else if password.characters.count < 6 {
            hidenLoadding()
            SweetAlert().showAlert("invalid password", subTitle: "passwords must be at least 6 characters in length", style: AlertStyle.Error)
            return
            
        }else if password != confirmPassword {
            hidenLoadding()
            SweetAlert().showAlert("invalid password", subTitle: "passwords doesn't match", style: AlertStyle.Error)
            return
        }
        
        let userProfile = UserProfile()
        userProfile.firstName = firstname
        userProfile.lastName = lastname
        userProfile.username = username
        userProfile.password = password
        userProfile.loginType = UserProfile.TYPE_EASYACCENT
        
        currentUser = userProfile
        AccountManager.register(userProfile) { (userProfile, success, message) -> Void in
            dispatch_async(dispatch_get_main_queue(),{
                if success {
                    AccountManager.updateProfile(userProfile)
                    self.performSegueWithIdentifier("GoToComfirmCode", sender: self)
                } else {
                    self.hidenLoadding()
                    AccountManager.showError("could not register", message: message)
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


