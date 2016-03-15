//
//  LoginHomeVC.swift
//  AccentEasy
//
//  Created by CMGVN on 1/13/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit
import FBSDKCoreKit
import FBSDKLoginKit


class LoginHomeVC: UIViewController, GIDSignInUIDelegate, GIDSignInDelegate{
    
    var showLogin:Int!
    var isShowLogin:Bool!

    var currentUser: UserProfile!

    //@IBOutlet weak var signInButton: GIDSignInButton!
    //@IBOutlet weak var btnLoginG: GIDSignInButton!
    
    @IBOutlet weak var btnLoginFB: UIButton!
    @IBOutlet weak var btnLoginAC: UIButton!
    @IBOutlet weak var btnAltLogin: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        //process show login button
        if (GlobalData.getInstance().isShowLogin){
            btnLoginFB.hidden = false
            btnLoginAC.hidden = false
            btnAltLogin.hidden = true
        }
        showLogin = 1
        
        //google login delegate
        GIDSignIn.sharedInstance().uiDelegate = self
        GIDSignIn.sharedInstance().delegate = self
        
    }
    
    override func viewWillAppear(animated: Bool) {
        /*if FBSDKAccessToken.currentAccessToken() != nil {
            let mainPage = self.storyboard?.instantiateViewControllerWithIdentifier("TestNewScreenViewController") as! TestNewScreenViewController
            let mainPageNav = UINavigationController(rootViewController: mainPage)
            
            let appDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
            
                appDelegate.window?.rootViewController = mainPageNav
        }*/
        
        //super.viewWillDisappear(animated)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        //userProfileSaveInApp.setObject(false, forKey: Login.KeyIsShowLogin)
    }
    
    
    @IBAction func btnLoginGTapped(sender: AnyObject) {
        DeviceManager.doIfConnectedToNetwork { () -> Void in
            GIDSignIn.sharedInstance().signIn()
        }
    }
    
    func signIn(signIn: GIDSignIn!, didSignInForUser user: GIDGoogleUser!,
        withError error: NSError!) {
            if (error == nil) {
                print("run in google sign in")
                // Perform any operations on signed in user here.
                let userId:String = user.userID                  // For client-side use only!
                let idToken:String = user.authentication.idToken // Safe to send to the server
                let name:String = user.profile.name
                let email:String = user.profile.email
                //let birthday = user.profile.
                let urlImage:String = user.profile.imageURLWithDimension(320).absoluteString
                //print("avatar")
                //print(urlAvate)
                //print(userId)
                
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
                userProfile.profileImage = urlImage
                //userProfile.dob = result.valueForKey("birthday") as! String
                userProfile.additionalToken = idToken
                userProfile.loginType = UserProfile.TYPE_GOOGLE_PLUS
                
                currentUser = userProfile
                self.registerUserProfile()
                //show loadding
                self.showLoadding()
                
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
        DeviceManager.doIfConnectedToNetwork { () -> Void in
            self.fbLoginInitiate()
            print("click login fb")
        }
    }
    
    func fbLoginInitiate() {
        let loginManager = FBSDKLoginManager()
        loginManager.logInWithReadPermissions(["public_profile", "email", "user_friends", "user_birthday"], handler: {(result:FBSDKLoginManagerLoginResult!, error:NSError!) -> Void in
            if (error != nil) {
                // Process error
                print(error)
                self.removeFbData()
                self.showError()
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
                    print(error)
                    self.showError()
                } else {
                    print(result)
                    
                    //Handle Profile Photo URL String
                    //let userId:String =  result["id"] as! String
                    let userId:String =  result.valueForKey("id") as! String
                    let profilePictureUrl:String = "https://graph.facebook.com/\(userId)/picture?type=square"
                    //https://graph.facebook.com/1093146987371181/picture?type=square&width=320&height=320
                    //enum{small, normal, album, large, square}
                    print(profilePictureUrl)
                    let accessToken = FBSDKAccessToken.currentAccessToken().tokenString
                    let fbUser = ["accessToken": accessToken, "user": result]
                    //add data for JSONStringUserProfile
                    let userProfile:UserProfile = UserProfile()
                    userProfile.name = result.valueForKey("name") as! String
                    let username = result.valueForKey("email")
                    if username != nil {
                        userProfile.username = username as! String
                    } else {
                        userProfile.username = "\(userId)@facebook.com"
                    }
                    let dob = result.valueForKey("birthday");
                    if dob != nil {
                        userProfile.dob = dob as! String
                    }
                    userProfile.profileImage = profilePictureUrl
                    userProfile.additionalToken = FBSDKAccessToken.currentAccessToken().tokenString
                    userProfile.loginType = UserProfile.TYPE_FACEBOOK
                    
                    self.currentUser = userProfile
                    self.registerUserProfile()
                    
                    //show loadding
                    self.showLoadding()
                }
            })
        }
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
    
    func showError(){
        dispatch_async(dispatch_get_main_queue(),{
            self.hidenLoadding()
            AccountManager.showError()
        })
    }

    
    func registerUserProfile() {
        weak var weakSelf = self;
        AccountManager.register(currentUser) { (userProfile, success, message) -> Void in
            dispatch_async(dispatch_get_main_queue(),{
                
                 weakSelf!.loginMainPage()
            })
        }
    }
    
    func loginMainPage(){
        weak var weakSelf = self;
        AccountManager.auth(currentUser) { (userProfile, success, message) -> Void in
            dispatch_async(dispatch_get_main_queue(),{
                if success {
                    userProfile.isLogin = true
                    AccountManager.updateProfile(userProfile)
                    weakSelf!.performSegueWithIdentifier("LoginScreenGoToMain", sender: self)
                } else {
                    weakSelf!.hidenLoadding()
                    AccountManager.showError("could not login", message: message)
                }
            })
        }
    }
    
    
    @IBAction func btnLoginACTouchUp(sender: AnyObject) {
        print("run in here")
        
        //let aELoginVC = self.storyboard?.instantiateViewControllerWithIdentifier("AELoginVC") as! AELoginVC
        //let aELoginVC = AELoginVC(nibName: "AELoginVC", bundle:nil)
        //self.navigationController?.pushViewController(aELoginVC, animated: true)
        //self.performSegueWithIdentifier("AELoginVC", sender: self)
        
        
        //let mainPage = self.storyboard?.instantiateViewControllerWithIdentifier("AELoginVC") as! AELoginVC
        //let mainPageNav = UINavigationController(rootViewController: mainPage)
        
        //let appDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
        
        //appDelegate.window?.rootViewController = mainPageNav
    }
    
    @IBAction func tappedAltLogin(sender: AnyObject) {
        if (isShowLogin != nil && isShowLogin == true) {
            btnLoginFB.hidden = false
            btnLoginAC.hidden = false
            btnAltLogin.hidden = true
        } else{
            showLogin = showLogin + 1
            if showLogin == 2 {
                btnLoginFB.hidden = false
            } else if showLogin == 3 {
                btnLoginAC.hidden = false
                btnAltLogin.hidden = true
            } 

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
