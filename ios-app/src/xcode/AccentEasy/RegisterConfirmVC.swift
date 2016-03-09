//
//  RegisterConfirmVC.swift
//  AccentEasy
//
//  Created by CMGVN on 1/19/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class RegisterConfirmVC: UIViewController {

    var userProfileSaveInApp:NSUserDefaults!
    var userProfile = UserProfile()
    var JSONStringUserProfile:String!
    
    @IBOutlet weak var txtcode: UITextField!
    
    @IBAction func btnOpenUrlTC(sender: AnyObject) {
        let url = NSURL(string: "http://www.accenteasy.com/useraccounts/TnC")!
        UIApplication.sharedApplication().openURL(url)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        userProfileSaveInApp = NSUserDefaults()
        JSONStringUserProfile = userProfileSaveInApp.objectForKey(Login.KeyRegisterUser) as! String
        userProfile = Mapper<UserProfile>().map(JSONStringUserProfile)!
        txtcode.placeholder = userProfile.username
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func confirmCodeTapped(sender: AnyObject) {
        /*
        data.put("acc", code);
        data.put("user", profile.getUsername());
        data.put("lang_prefix", "BE");
        data.put("version_code", AndroidHelper.getVersionCode(context));
        data.put("imei", new DeviceUuidFactory(context).getDeviceUuid().toString());
        */
        
        let codeConfirm:String = txtcode.text!
    
        let client = Client()
            .baseUrl(FileHelper.getAccentEasyBaseUrl())
            .onError({e in print(e)
                Login.showError()
            });
        
        client.post("/activate").type("form").send(["acc":codeConfirm,"version_code" : "40000","user":userProfile.username,"lang_prefix":"BE","imei":"32131232131"])
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
                    let result:String = res.text!
                    if result == "success" {
                        dispatch_async(dispatch_get_main_queue(),{
                            SweetAlert().showAlert("validation successful!", subTitle: "your acount has already been activated. please login with your email address and password.", style: AlertStyle.Success, buttonTitle: "Ok") {(isOk) -> Void in
                                if isOk == true {
                                    self.performSegueWithIdentifier("ConfirmRegisterGoToLogin", sender: self)
                                }
                            }
                        })
                    } else {
                        dispatch_async(dispatch_get_main_queue(),{
                            SweetAlert().showAlert("validation failed!", subTitle: result, style: AlertStyle.Error)
                            
                        })
                    }
                }
            })
        
    }

    @IBAction func sendCodeAgainTapped(sender: AnyObject) {
        /*
        data.put("profile", gson.toJson(profile));
        data.put("lang_prefix", "BE");
        data.put("version_code", AndroidHelper.getVersionCode(context));
        data.put("imei", new DeviceUuidFactory(context).getDeviceUuid().toString());
        */
        
        let client = Client()
            .baseUrl(FileHelper.getAccentEasyBaseUrl())
            .onError({e in print(e)
                Login.showError()
            });
        
        client.post("/activate").type("form").send(["version_code" : "40000","profile":JSONStringUserProfile,"lang_prefix":"BE","imei":"32131232131"])
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
                    let result:String = res.text!
                    if result == "success" {
                        dispatch_async(dispatch_get_main_queue(),{
                            SweetAlert().showAlert("successfully submitted!", subTitle: "please check message in your email \(self.userProfile.username)", style: AlertStyle.Success)
                            //self.performSegueWithIdentifier("ConfirmCodeGoToLogin", sender: self)
                            
                            
                            
                        })
                    } else {
                        dispatch_async(dispatch_get_main_queue(),{
                            SweetAlert().showAlert("send code failed!", subTitle: result, style: AlertStyle.Error)
                            
                        })
                    }
                }
            })
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
