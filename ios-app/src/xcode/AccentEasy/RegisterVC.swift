//
//  RegisterVC.swift
//  AccentEasy
//
//  Created by CMGVN on 1/14/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import UIKit
import SwiftClient
import ObjectMapper

class RegisterVC: UIViewController {

    
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
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
        
        
        if username.isEmpty || password.isEmpty {
            let alertView:UIAlertView = UIAlertView()
            alertView.title = "Sign Up Failed!"
            alertView.message = "Please enter Username and Password"
            alertView.delegate = self
            alertView.addButtonWithTitle("OK")
            alertView.show()
            return
        }else if password != confirmPassword {
            let alertView:UIAlertView = UIAlertView()
            alertView.title = "Sign Up Failed!"
            alertView.message = "Passwords doesn't Match"
            alertView.delegate = self
            alertView.addButtonWithTitle("OK")
            alertView.show()
            return
        }
        
        var userProfile = UserProfile()
        userProfile.firstName = firstname
        userProfile.lastName = lastname
        userProfile.username = username
        userProfile.password = password
        userProfile.loginType = UserProfile.TYPE_EASYACCENT
        
        let JSONStringUserProfile:String = Mapper().toJSONString(userProfile, prettyPrint: true)!
        
        
        print("JSONStringUserProfile is:" + JSONStringUserProfile)
        print("------------------------------------------------------")
        var client = Client()
            .baseUrl("http://localhost:8080")
            .onError({e in print(e)});

        
        // GET http://myapi.org/get?key=value&key2=value2
        
        //Client().post(url).send([key : value, key2 : value2])
    
        client.post("/RegisterHandler").type("form").send(["version_code" : "40000","profile":JSONStringUserProfile,"lang_prefix":"BE","imei":"32131232131"])
            .set("header", "headerValue")
            .end({(res:Response) -> Void in
                print(res)
                if(res.error) { // status of 2xx
                    //handleResponseJson(res.body)
                    print(res.body)
                }
                else {
                    //handleErrorJson(res.body)
                    print(res.text)
                }
            })
        
        /*client.post("http://localhost:8080/RegisterHandler").query(["version_code" : "40000","profile":JSONStringUserProfile,"lang_prefix":"BE","imei":"32131232131"])
            .set("header", "headerValue")
            .end({(res:Response) -> Void in
                
                if(res.error) { // status of 2xx
                    //handleResponseJson(res.body)
                    print(res)
                }
                else {
                    //handleErrorJson(res.body)
                    print(res)
                }
            })*/
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
