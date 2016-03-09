//
//  RegisterVC.swift
//  AccentEasy
//
//  Created by CMGVN on 1/14/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class RegisterVC: UIViewController {

    var userProfileSaveInApp:NSUserDefaults!
    var JSONStringUserProfile:String!
    
    @IBOutlet weak var txtFirstname: UITextField!
    @IBOutlet weak var txtLastname: UITextField!
    @IBOutlet weak var txtEmail: UITextField!
    @IBOutlet weak var txtPassword: UITextField!
    @IBOutlet weak var txtConfirmPassword: UITextField!
    
    
    
    @IBAction func btnOpenUrlTC(sender: AnyObject) {
        let url = NSURL(string: "http://www.accenteasy.com/useraccounts/TnC")!
        UIApplication.sharedApplication().openURL(url)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        userProfileSaveInApp = NSUserDefaults()
        
        txtEmail.autocorrectionType = UITextAutocorrectionType.No
        //self.performSegueWithIdentifier("GoToComfirmCode", sender: self)
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
    
    func isValidEmail(testStr:String) -> Bool {
        // println("validate calendar: \(testStr)")
        let emailRegEx = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"
        
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        return emailTest.evaluateWithObject(testStr)
    }
    
    @IBAction func registerTapped(sender: AnyObject) {
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
        
        if username.isEmpty || !isValidEmail(username) {
            SweetAlert().showAlert("invalid email address", subTitle: "please enter a valid email address", style: AlertStyle.Error)
            return
        } else if password.characters.count < 6 {
            SweetAlert().showAlert("invalid password", subTitle: "passwords must be at least 6 characters in length", style: AlertStyle.Error)
            return
            
        }else if password != confirmPassword {
            SweetAlert().showAlert("invalid password", subTitle: "passwords doesn't match", style: AlertStyle.Error)
            return
        }
        
        let userProfile = UserProfile()
        userProfile.firstName = firstname
        userProfile.lastName = lastname
        userProfile.username = username
        userProfile.password = password
        userProfile.loginType = UserProfile.TYPE_EASYACCENT
        
        
        JSONStringUserProfile = Mapper().toJSONString(userProfile, prettyPrint: true)!
        
        
        print("JSONStringUserProfile is:" + JSONStringUserProfile)
        print("------------------------------------------------------")
        let client = Client()
            .baseUrl(FileHelper.getAccentEasyBaseUrl())
            .onError({e in print(e)
                Login.showError()
            });
    
        client.post("/RegisterHandler").type("form").send(["version_code" : "40000","profile":JSONStringUserProfile,"lang_prefix":"BE","imei":"32131232131"])
            .set("header", "headerValue")
            .end({(res:Response) -> Void in
                print(res)
                if(res.error) { // status of 2xx
                    //handleResponseJson(res.body)
                    //print(res.body)
                    print(res.text)
                    Login.showError()
                }
                else {
                    //handleErrorJson(res.body)
                    print(res.text)
                    let result = Mapper<RegisterResult>().map(res.text)
                    let status:Bool = result!.status
                    let message:String = result!.message
                    if status {
                        //register suceess
                        dispatch_async(dispatch_get_main_queue(),{
                            //SweetAlert().showAlert("Register Success!", subTitle: "", style: AlertStyle.Success)
                            //[unowned self] in NSThread.isMainThread()
                            //save UserProfile
                            self.userProfileSaveInApp.setObject(self.JSONStringUserProfile, forKey: Login.KeyRegisterUser)
                            //next page
                            self.performSegueWithIdentifier("GoToComfirmCode", sender: self)
                        })
                        
                        
                    } else {
                        //SweetAlert().showAlert("Register Failed!", subTitle: "It's pretty, isn't it?", style: AlertStyle.Error)
                        dispatch_async(dispatch_get_main_queue(),{
                            SweetAlert().showAlert("Register Failed!", subTitle: message, style: AlertStyle.Error)
                            
                        })
                    }
                    //print(result?.message)
                    //print(result?.status)
                }
            })
        
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


