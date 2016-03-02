//
//  ResetPassVC.swift
//  AccentEasy
//
//  Created by CMGVN on 1/14/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class ResetPassVC: UIViewController {

    @IBOutlet weak var txtEmail: UITextField!
    
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
    
    @IBAction func resetTapped(sender: AnyObject) {
        /*
        data.put("acc", profile.getUsername());
        data.put("action", "request");
        data.put("imei", new DeviceUuidFactory(context).getDeviceUuid().toString());
        */
        let username = txtEmail.text!
        if username.isEmpty {
            dispatch_async(dispatch_get_main_queue(),{
                SweetAlert().showAlert("Missing email address!", subTitle: "please enter email", style: AlertStyle.Error)
                
            })
            return
        }
        
        let client = Client()
            .baseUrl("http://localhost:8080")
            .onError({e in print(e)});
        
        client.post("/ResetPasswordHandler").type("form").send(["acc":username,"action":"request","imei":"32131232131"])
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
                    let result:String = res.text!
                    if result == "success" {
                        dispatch_async(dispatch_get_main_queue(),{
                            SweetAlert().showAlert("successfully submitted!", subTitle: "please check message in your email \(username)", style: AlertStyle.Success, buttonTitle: "Ok") {(isOk) -> Void in
                                if isOk == true {
                                    self.performSegueWithIdentifier("ResetPassGoToLogin", sender: self)
                                }
                            }
                        })
                    } else {
                        dispatch_async(dispatch_get_main_queue(),{
                            SweetAlert().showAlert("reset password failed!", subTitle: result, style: AlertStyle.Error)
                            
                        })
                    }
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
