//
//  LoginHomeVC.swift
//  AccentEasy
//
//  Created by CMGVN on 1/13/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import UIKit
import FBSDKCoreKit
import FBSDKLoginKit
import ObjectMapper
import SwiftClient


class LoginHomeVC: UIViewController, GIDSignInUIDelegate, GIDSignInDelegate{
    
    var showLog:Int!
    var JSONStringUserProfile:String!


    @IBOutlet weak var signInButton: GIDSignInButton!
    //@IBOutlet weak var btnLoginG: GIDSignInButton!
    
    @IBOutlet weak var btnLoginFB: UIButton!
    @IBOutlet weak var btnLoginAC: UIButton!
    @IBOutlet weak var btnAltLogin: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        showLog = 1
        GIDSignIn.sharedInstance().uiDelegate = self
        GIDSignIn.sharedInstance().delegate = self
        print("view did load")
    }
    
    override func viewWillAppear(animated: Bool) {
        /*if FBSDKAccessToken.currentAccessToken() != nil {
            let mainPage = self.storyboard?.instantiateViewControllerWithIdentifier("TestNewScreenViewController") as! TestNewScreenViewController
            let mainPageNav = UINavigationController(rootViewController: mainPage)
            
            let appDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
            
                appDelegate.window?.rootViewController = mainPageNav
        }*/
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    @IBAction func btnLoginGTapped(sender: AnyObject) {
        //[[GIDSignIn sharedInstance] signIn]
        GIDSignIn.sharedInstance().signIn()
    }
    
    func signIn(signIn: GIDSignIn!, didSignInForUser user: GIDGoogleUser!,
        withError error: NSError!) {
            if (error == nil) {
                // Perform any operations on signed in user here.
                let userId:String = user.userID                  // For client-side use only!
                let idToken:String = user.authentication.idToken // Safe to send to the server
                let name:String = user.profile.name
                let email:String = user.profile.email
                //let birthday = user.profile.
                
                
                print(userId)
                print(idToken)
                print(name)
                print(email)
                
                // [START_EXCLUDE]
                NSNotificationCenter.defaultCenter().postNotificationName(
                    "ToggleAuthUINotification",
                    object: nil,
                    userInfo: ["statusText": "Signed in user:\n\(name)"])
                // [END_EXCLUDE]
                
                /*let mainStoryBoard:UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
                
                let mainPage = mainStoryBoard.instantiateViewControllerWithIdentifier("SWRevealViewController") as! SWRevealViewController
                
                
                let mainPageNav = UINavigationController(rootViewController: mainPage)
                
                let appDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
                
                appDelegate.window?.rootViewController = mainPageNav*/
                //add data for JSONStringUserProfile
                let userProfile:UserProfile = UserProfile()
                userProfile.name = name
                userProfile.username = email
                //userProfile.dob = result.valueForKey("birthday") as! String
                userProfile.deviceInfo = UserProfile.DeviceInfo()
                userProfile.additionalToken = idToken
                userProfile.loginType = UserProfile.TYPE_GOOGLE_PLUS
                userProfile.deviceInfo.appVersion = "400000"
                userProfile.deviceInfo.appName = "400000"
                
                self.JSONStringUserProfile = Mapper().toJSONString(userProfile, prettyPrint: true)!
                print(self.JSONStringUserProfile)
                self.registerFB()
                
            } else {
                print("\(error.localizedDescription)")
                // [START_EXCLUDE silent]
                NSNotificationCenter.defaultCenter().postNotificationName(
                    "ToggleAuthUINotification", object: nil, userInfo: nil)
                // [END_EXCLUDE]
            }
    }

    
    
    
    // Implement these methods only if the GIDSignInUIDelegate is not a subclass of
    // UIViewController.
    
    // Stop the UIActivityIndicatorView animation that was started when the user
    // pressed the Sign In button
    func signInWillDispatch(signIn: GIDSignIn!, error: NSError!) {
        //myActivityIndicator.stopAnimating()
        print("google signInWillDispatch")
        
        
    }

    
    // Present a view that prompts the user to sign in with Google
    func signIn(signIn: GIDSignIn!,
        presentViewController viewController: UIViewController!) {
            self.presentViewController(viewController, animated: true, completion: nil)
            print("sign in presented")
    }
    
    // Dismiss the "Sign in with Google" view
    func signIn(signIn: GIDSignIn!,
        dismissViewController viewController: UIViewController!) {
            self.dismissViewControllerAnimated(true, completion: nil)
            print("sign in dismissed")
    }
    
    @IBAction func btnLoginFBTapped(sender: AnyObject) {
        fbLoginInitiate()
        print("click login fb")
    }
    
    func fbLoginInitiate() {
        let loginManager = FBSDKLoginManager()
        loginManager.logInWithReadPermissions(["public_profile", "email", "user_friends", "user_birthday"], handler: {(result:FBSDKLoginManagerLoginResult!, error:NSError!) -> Void in
            if (error != nil) {
                // Process error
                print(error)
                self.removeFbData()
            } else if result.isCancelled {
                // User Cancellation
                self.removeFbData()
                print("click cancel")
            } else {
                print("success initiate")
                //Success
                if result.grantedPermissions.contains("email") && result.grantedPermissions.contains("public_profile") {
                    //Do work
                    self.fetchFacebookProfile()
                } else {
                    //Handle error
                    print("grantedPermissions error")
                }
            }
        })
    }
    
    func removeFbData() {
        //Remove FB Data
        let fbManager = FBSDKLoginManager()
        fbManager.logOut()
        FBSDKAccessToken.setCurrentAccessToken(nil)
    }
    
    func fetchFacebookProfile()
    {
        if FBSDKAccessToken.currentAccessToken() != nil {
            let graphRequest : FBSDKGraphRequest = FBSDKGraphRequest(graphPath: "me?fields=id,name,email,birthday", parameters: nil)
            graphRequest.startWithCompletionHandler({ (connection, result, error) -> Void in
                
                if ((error) != nil) {
                    //Handle error
                } else {
                    print(result)
                    //Handle Profile Photo URL String
                    let userId =  result["id"] as! String
                    let profilePictureUrl = "https://graph.facebook.com/\(userId)/picture?type=large"
                    print(profilePictureUrl)
                    let accessToken = FBSDKAccessToken.currentAccessToken().tokenString
                    let fbUser = ["accessToken": accessToken, "user": result]
                    
                    
                    //add data for JSONStringUserProfile
                    let userProfile:UserProfile = UserProfile()
                    userProfile.name = result.valueForKey("name") as! String
                    userProfile.username = result.valueForKey("email") as! String
                    userProfile.dob = result.valueForKey("birthday") as! String
                    userProfile.deviceInfo = UserProfile.DeviceInfo()
                    userProfile.additionalToken = FBSDKAccessToken.currentAccessToken().tokenString
                    userProfile.loginType = UserProfile.TYPE_FACEBOOK
                    userProfile.deviceInfo.appVersion = "400000"
                    userProfile.deviceInfo.appName = "400000"
                    
                    self.JSONStringUserProfile = Mapper().toJSONString(userProfile, prettyPrint: true)!
                    print(self.JSONStringUserProfile)
                    self.registerFB()
                }
            })
        }
    }
    


    
    func registerFB() {
        print("run in register fb")
        let client = Client()
            .baseUrl("http://localhost:8080")
            .onError({e in print(e)});
        
        client.post("/RegisterHandler").type("form").send(["version_code" : "40000","profile":self.JSONStringUserProfile,"lang_prefix":"BE","imei":"32131232131"])
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
                    //dispatch_async(dispatch_get_main_queue(),{
                        //self.performSegueWithIdentifier("LoginScreenGoToMain", sender: self)
                    //})
                    let result = Mapper<RegisterResult>().map(res.text)
                    let status:Bool = result!.status
                    let message:String = result!.message
                    self.loginFB()
                    /*if status {
                        //register suceess
                        dispatch_async(dispatch_get_main_queue(),{
                        //SweetAlert().showAlert("Register Success!", subTitle: "", style: AlertStyle.Success)
                        //[unowned self] in NSThread.isMainThread()
                        //self.performSegueWithIdentifier("GoToComfirmCode", sender: self)
                        })
                    
                    
                    } else {
                        //SweetAlert().showAlert("Register Failed!", subTitle: "It's pretty, isn't it?", style: AlertStyle.Error)
                        dispatch_async(dispatch_get_main_queue(),{
                        SweetAlert().showAlert("Register Failed!", subTitle: message, style: AlertStyle.Error)
                        
                        })
                    }*/
                    //print(result?.message)
                    //print(result?.status)
                }
            })
    }
    
    func loginFB(){
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
                    print("run in login fb")
                    print(res.text)
                    let result = Mapper<RegisterResult>().map(res.text)
                    let status:Bool = result!.status
                    let message:String = result!.message
                    if status {
                        //register suceess
                        dispatch_async(dispatch_get_main_queue(),{
                            //SweetAlert().showAlert("Register Success!", subTitle: "", style: AlertStyle.Success)
                            //[unowned self] in NSThread.isMainThread()
                            self.performSegueWithIdentifier("LoginScreenGoToMain", sender: self)
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
    
    @IBAction func tappedAltLogin(sender: AnyObject) {
        showLog = showLog + 1
        if showLog == 2 {
            btnLoginFB.hidden = false
        } else if showLog == 3{
            btnLoginAC.hidden = false
            btnAltLogin.hidden = true
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
