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

    var userProfileSaveInApp:NSUserDefaults!
    var JSONStringUserProfile:String!
    var keyForUserProfile:String!
    
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
        
        let alertView:UIAlertView = UIAlertView()
        
        if username.isEmpty || password.isEmpty {
            alertView.title = "Register Failed!"
            alertView.message = "Please enter Username and Password"
            alertView.delegate = self
            alertView.addButtonWithTitle("OK")
            alertView.show()
            return
        }else if password != confirmPassword {
            alertView.title = "Register Failed!"
            alertView.message = "Passwords doesn't Match"
            alertView.delegate = self
            alertView.addButtonWithTitle("OK")
            alertView.show()
            return
        }
        
        let userProfile = UserProfile()
        userProfile.firstName = firstname
        userProfile.lastName = lastname
        userProfile.username = username
        userProfile.password = password
        userProfile.loginType = UserProfile.TYPE_EASYACCENT
        
        //set userProfile key
        keyForUserProfile = username
        
        JSONStringUserProfile = Mapper().toJSONString(userProfile, prettyPrint: true)!
        
        
        print("JSONStringUserProfile is:" + JSONStringUserProfile)
        print("------------------------------------------------------")
        let client = Client()
            .baseUrl("http://localhost:8080")
            .onError({e in print(e)});
    
        client.post("/RegisterHandler").type("form").send(["version_code" : "40000","profile":JSONStringUserProfile,"lang_prefix":"BE","imei":"32131232131"])
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
                            //save UserProfile
                            self.userProfileSaveInApp.setObject(self.JSONStringUserProfile, forKey: self.keyForUserProfile)
                            self.userProfileSaveInApp.setObject(self.keyForUserProfile, forKey: Login.KeyUserProfile)
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


class RegisterResult: Mappable{
    var status:Bool!
    var message:String!
    
    required init?(_ map: Map) {
        
    }
    
    required init(){}
    
    // Mappable
    func mapping(map: Map) {
        status    <- map["status"]
        message   <- map["message"]
    }
}