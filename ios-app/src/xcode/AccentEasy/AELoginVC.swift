//
//  AELoginVC.swift
//  AccentEasy
//
//  Created by CMGVN on 1/14/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import UIKit
import ObjectMapper
import SwiftClient

class AELoginVC: UIViewController {

    @IBOutlet weak var txtEmail: UITextField!
    @IBOutlet weak var txtPassword: UITextField!
    
    
    @IBAction func btnOpenUrlTC(sender: AnyObject) {
        
        let url = NSURL(string: "http://www.accenteasy.com/useraccounts/TnC")!
        UIApplication.sharedApplication().openURL(url)
        
    }
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
        
        
        //deviceIn
        
        let JSONStringUserProfile:String = Mapper().toJSONString(userProfile, prettyPrint: true)!
        
        print(JSONStringUserProfile)
        
        let client = Client()
            .baseUrl("http://localhost:8080")
            .onError({e in print(e)});
        
        client.post("/AuthHandler").type("form").send(["profile":JSONStringUserProfile,"check":"false","imei":"32131232131"])
            .set("header", "headerValue")
            .end({(res:Response) -> Void in
                print(res)
                if(res.error) { // status of 2xx
                    //handleResponseJson(res.body)
                    //print(res.body)
                    print(res.text)
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
                            self.performSegueWithIdentifier("AELoginGoToMain", sender: self)
                        })
                        
                        
                    } else {
                        //SweetAlert().showAlert("Register Failed!", subTitle: "It's pretty, isn't it?", style: AlertStyle.Error)
                        dispatch_async(dispatch_get_main_queue(),{
                            SweetAlert().showAlert("Login Failed!", subTitle: message, style: AlertStyle.Error)
                            
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
