//
//  AELoginVC.swift
//  AccentEasy
//
//  Created by CMGVN on 1/14/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit
class AELoginVC: UIViewController {

    var userProfileSaveInApp:NSUserDefaults!
    var keyForProfile:String!
    var JSONStringUserProfile:String!
    
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
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        userProfileSaveInApp.setObject(true, forKey: Login.KeyIsShowLogin)
    }
    

    
    @IBAction func loginTapped(sender: AnyObject) {
        /*
        data.put("profile", gson.toJson(profile));
        data.put("check", "true");
        data.put("imei", new DeviceUuidFactory(context).getDeviceUuid().toString());
        */
        
        let username:String = txtEmail.text!
        let password:String = txtPassword.text!
        
        if username.isEmpty || password.isEmpty {
            dispatch_async(dispatch_get_main_queue(),{
                SweetAlert().showAlert("Login Failed!", subTitle: "please enter username and password", style: AlertStyle.Error)
                
            })
            return
        }
        
        let userProfile = UserProfile()
        userProfile.deviceInfo = UserProfile.DeviceInfo()
        userProfile.username = txtEmail.text!
        userProfile.password = txtPassword.text!
        userProfile.loginType = UserProfile.TYPE_EASYACCENT
        userProfile.deviceInfo.appVersion = "400000"
        userProfile.deviceInfo.appName = "400000"
        keyForProfile = txtEmail.text!
        
        
        //deviceIn
        
        JSONStringUserProfile = Mapper().toJSONString(userProfile, prettyPrint: true)!
        
        print(JSONStringUserProfile)
        
        let client = Client()
            .baseUrl(FileHelper.getAccentEasyBaseUrl())
            .onError({e in
                print(e)
                Login.showError()
            });
        
        client.post("/AuthHandler").type("form").send(["profile":JSONStringUserProfile,"check":"false","imei":"32131232131"])
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
                    //print(res.body)
                    let result = Mapper<RegisterResult>().map(res.text)
                    let status:Bool = result!.status
                    let message:String = result!.message
                    if status {
                        //register suceess
                        dispatch_async(dispatch_get_main_queue(),{
                            //SweetAlert().showAlert("Register Success!", subTitle: "", style: AlertStyle.Success)
                            //[unowned self] in NSThread.isMainThread()
                            
                            /*let userProfile:UserProfile = UserProfile()
                            userProfile.name = res.text["id"] as! String
                            userProfile.username = email
                            userProfile.profileImage = urlImage
                            //userProfile.dob = result.valueForKey("birthday") as! String
                            userProfile.deviceInfo = UserProfile.DeviceInfo()
                            userProfile.additionalToken = idToken
                            userProfile.loginType = UserProfile.TYPE_GOOGLE_PLUS
                            userProfile.deviceInfo.appVersion = "400000"
                            userProfile.deviceInfo.appName = "400000"
                            
                            //set key for NSUserDefault
                            keyForProfile = email
                            
                            self.JSONStringUserProfile = Mapper().toJSONString(userProfile, prettyPrint: true)!
                            print(self.JSONStringUserProfile)*/
                            
                            //self.userProfileSaveInApp.setObject(self.JSONStringUserProfile, forKey: self.keyForProfile)
                            //self.userProfileSaveInApp.setObject(self.keyForProfile, forKey: Login.KeyUserProfile)
                            //self.performSegueWithIdentifier("AELoginGoToMain", sender: self)
                            self.getUserProfile()
                        })
                        
                        
                    } else {
                        //SweetAlert().showAlert("Register Failed!", subTitle: "It's pretty, isn't it?", style: AlertStyle.Error)
                        dispatch_async(dispatch_get_main_queue(),{
                            SweetAlert().showAlert("Login Failed!", subTitle: message, style: AlertStyle.Error)
                            
                        })
                    }
                    print(result?.message)
                    print(result?.status)
                }
            })
    }

    
    func getUserProfile () {
        let client = Client()
            .baseUrl(FileHelper.getAccentEasyBaseUrl())
            .onError({e in
                print(e)
                Login.showError()
            });

        
        client.post("/userprofile").type("form").send(["profile":JSONStringUserProfile,"action":"get"])
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
                    print("profile")
                    print(res.text)
                    let result = Mapper<RegisterResult>().map(res.text)
                    let status:Bool = result!.status
                    let message:String = result!.message
                    if status {
                        //register suceess
                        dispatch_async(dispatch_get_main_queue(),{
                            //SweetAlert().showAlert("Register Success!", subTitle: "", style: AlertStyle.Success)
                            //[unowned self] in NSThread.isMainThread()
                            
                            let userProfile:UserProfile = result!.data
                            self.JSONStringUserProfile = Mapper().toJSONString(userProfile, prettyPrint: true)!
                            print(self.JSONStringUserProfile)
                            
                            self.userProfileSaveInApp.setObject(self.JSONStringUserProfile, forKey: self.keyForProfile)
                            self.userProfileSaveInApp.setObject(self.keyForProfile, forKey: Login.KeyUserProfile)
                            self.performSegueWithIdentifier("AELoginGoToMain", sender: self)
                        })
                        
                        
                    } else {
                        //SweetAlert().showAlert("Register Failed!", subTitle: "It's pretty, isn't it?", style: AlertStyle.Error)
                        dispatch_async(dispatch_get_main_queue(),{
                            SweetAlert().showAlert("Login Failed!", subTitle: message, style: AlertStyle.Error)
                            
                        })
                    }
                }
            })

        
    }

}
