//
//  AppDelegate.swift
//  SwiftSidebarMenu
//
//  Created by CMGVN on 1/7/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit
import Fabric
import Crashlytics
import FBSDKCoreKit
import ImageLoader

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate, GIDSignInDelegate, GGLInstanceIDDelegate, GCMReceiverDelegate {

    var window: UIWindow?
    //look creen
    var lockScreen: LockScreenController!
    
    //GCM
    var connectedToGCM = false
    var subscribedToTopic = false
    var gcmSenderID: String?
    var registrationToken: String?
    var registrationOptions = [String: AnyObject]()
    
    let registrationKey = "onRegistrationCompleted"
    let messageKey = "onMessageReceived"
    let messageKeyInApp = "onMessageReceivedInApp"
    let subscriptionTopic = "/topics/global"
    //
    
    func application(application: UIApplication, didFinishLaunchingWithOptions launchOptions: [NSObject: AnyObject]?) -> Bool {
        // Override point for customization after application launch.
        //fb login
        FBSDKApplicationDelegate.sharedInstance().application(application, didFinishLaunchingWithOptions: launchOptions)
        Fabric.with([Crashlytics.self])
        self.logUser()
        // Initialize google sign-in
        var configureError: NSError?
        GGLContext.sharedInstance().configureWithError(&configureError)
        assert(configureError == nil, "Error configuring Google services: \(configureError)")
        
        GIDSignIn.sharedInstance().delegate = self
        
        // Set Navigation bar Title colour
        UINavigationBar.appearance().titleTextAttributes = [NSForegroundColorAttributeName:ColorHelper.APP_PURPLE]
        
        
        //swipe screen
        //window = UIWindow(frame: UIScreen.mainScreen().bounds)
        //window!.rootViewController = FreestyleSwipeVC()
        //window!.makeKeyAndVisible()
        //Logger.startService()
        
        //create lock screen
        lockScreen = LockScreenController()
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "showLockScreen:", name: "showLockScreen", object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "hideLockScreen:", name: "hideLockScreen", object: nil)
        
        // Register for remote notifications
        gcmApplication(application,didFinishLaunchingWithOptions: launchOptions)
        
        return true
    }
    
    func showLockScreen(notification: NSNotification)
    {
        if !lockScreen.isShowing {
            lockScreen.show(UIScreen.mainScreen().applicationFrame)
        }
    }
    
    func hideLockScreen(notification: NSNotification)
    {
        if lockScreen.isShowing {
            lockScreen.close()
        }
    }
    
    func logUser() {
        let currentUser = AccountManager.currentUser()
        if currentUser.isLogin {
            Crashlytics.sharedInstance().setUserEmail(currentUser.username)
            Crashlytics.sharedInstance().setUserIdentifier(currentUser.username)
            Crashlytics.sharedInstance().setUserName(currentUser.name)
        }
    }
    
    //fb login
    func application(application: UIApplication, openURL url: NSURL, sourceApplication: String?, annotation: AnyObject) -> Bool {
        FBSDKApplicationDelegate.sharedInstance().application(application, openURL: url, sourceApplication: sourceApplication, annotation: annotation)
        GIDSignIn.sharedInstance().handleURL(url, sourceApplication: sourceApplication, annotation: annotation)

        return true
    }
    
    // [START openurl]
    //func application(application: UIApplication, openURL url: NSURL, sourceApplication: String?, annotation: AnyObject) -> Bool {
        //return GIDSignIn.sharedInstance().handleURL(url, sourceApplication: sourceApplication, annotation: annotation)
    //}
    
    // [END openurl]
    /*@available(iOS 9.0, *)
    func application(app: UIApplication, openURL url: NSURL, options: [String : AnyObject]) -> Bool {
        //FBSDKApplicationDelegate.sharedInstance().application(application, openURL: url, sourceApplication: sourceApplication, annotation: annotation)
        GIDSignIn.sharedInstance().handleURL(url,
            sourceApplication: options[UIApplicationOpenURLOptionsSourceApplicationKey] as! String?,
            annotation: options[UIApplicationOpenURLOptionsAnnotationKey])
        return true
    }*/
    
    // [START signin_handler]
    func signIn(signIn: GIDSignIn!, didSignInForUser user: GIDGoogleUser!,
        withError error: NSError!) {
            if (error == nil) {
                // Perform any operations on signed in user here.
                let userId = user.userID                  // For client-side use only!
                let idToken = user.authentication.idToken // Safe to send to the server
                let name = user.profile.name
                let email = user.profile.email
                
                //Logger.log(user.profile.name)
                
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
                
                
            } else {
                Logger.log("\(error.localizedDescription)")
                // [START_EXCLUDE silent]
                NSNotificationCenter.defaultCenter().postNotificationName(
                    "ToggleAuthUINotification", object: nil, userInfo: nil)
                // [END_EXCLUDE]
            }
    }
    // [END signin_handler]
    
    // [START disconnect_handler]
    func signIn(signIn: GIDSignIn!, didDisconnectWithUser user:GIDGoogleUser!,
        withError error: NSError!) {
            // Perform any operations when the user disconnects from app here.
            // [START_EXCLUDE]
            NSNotificationCenter.defaultCenter().postNotificationName(
                "ToggleAuthUINotification",
                object: nil,
                userInfo: ["statusText": "User has disconnected."])
            // [END_EXCLUDE]
    }
    // [END disconnect_handler]

    func applicationWillResignActive(application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
        gcmApplicationDidEnterBackground(application)
    }

    func applicationWillEnterForeground(application: UIApplication) {
        // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
    }

    func applicationDidBecomeActive(application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
        //fb login
        FBSDKAppEvents.activateApp()
        gcmApplicationDidBecomeActive(application)
    }

    func applicationWillTerminate(application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }

    
    //GCM-----------------------------------------------
    // [START register_for_remote_notifications]
    func gcmApplication(application: UIApplication, didFinishLaunchingWithOptions launchOptions:
        [NSObject: AnyObject]?) {
        // [START_EXCLUDE]
        // Configure the Google context: parses the GoogleService-Info.plist, and initializes
        // the services that have entries in the file
        var configureError:NSError?
        GGLContext.sharedInstance().configureWithError(&configureError)
        assert(configureError == nil, "Error configuring Google services: \(configureError)")
        gcmSenderID = GGLContext.sharedInstance().configuration.gcmSenderID
        // [END_EXCLUDE]
        // Register for remote notifications
        if #available(iOS 8.0, *) {
            let settings: UIUserNotificationSettings =
                UIUserNotificationSettings(forTypes: [.Alert, .Badge, .Sound], categories: nil)
            application.registerUserNotificationSettings(settings)
            application.registerForRemoteNotifications()
        } else {
            // Fallback
            let types: UIRemoteNotificationType = [.Alert, .Badge, .Sound]
            application.registerForRemoteNotificationTypes(types)
        }
        
        // [END register_for_remote_notifications]
        // [START start_gcm_service]
        let gcmConfig = GCMConfig.defaultConfig()
        gcmConfig.receiverDelegate = self
        GCMService.sharedInstance().startWithConfig(gcmConfig)
        // [END start_gcm_service]
    }
    
    func subscribeToTopic() {
        // If the app has a registration token and is connected to GCM, proceed to subscribe to the
        // topic
        if(registrationToken != nil && connectedToGCM) {
            GCMPubSub.sharedInstance().subscribeWithToken(self.registrationToken, topic: subscriptionTopic,
                                                          options: nil, handler: {(error:NSError?) -> Void in
                                                            if let error = error {
                                                                // Treat the "already subscribed" error more gently
                                                                if error.code == 3001 {
                                                                    print("Already subscribed to \(self.subscriptionTopic)")
                                                                } else {
                                                                    print("Subscription failed: \(error.localizedDescription)");
                                                                }
                                                            } else {
                                                                self.subscribedToTopic = true;
                                                                NSLog("Subscribed to \(self.subscriptionTopic)");
                                                            }
            })
        }
    }
    
    // [START connect_gcm_service]
    func gcmApplicationDidBecomeActive( application: UIApplication) {
        // Connect to the GCM server to receive non-APNS notifications
        GCMService.sharedInstance().connectWithHandler({(error:NSError?) -> Void in
            if let error = error {
                print("Could not connect to GCM: \(error.localizedDescription)")
            } else {
                self.connectedToGCM = true
                print("Connected to GCM")
                // [START_EXCLUDE]
                self.subscribeToTopic()
                // [END_EXCLUDE]
            }
        })
    }
    // [END connect_gcm_service]
    
    // [START disconnect_gcm_service]
    func gcmApplicationDidEnterBackground(application: UIApplication) {
        GCMService.sharedInstance().disconnect()
        // [START_EXCLUDE]
        self.connectedToGCM = false
        // [END_EXCLUDE]
    }
    // [END disconnect_gcm_service]
    
    // [START receive_apns_token]
    func application( application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken
        deviceToken: NSData ) {
        //print("gcm token \(deviceToken)")
        // [END receive_apns_token]
        // [START get_gcm_reg_token]
        // Create a config and set a delegate that implements the GGLInstaceIDDelegate protocol.
        let instanceIDConfig = GGLInstanceIDConfig.defaultConfig()
        instanceIDConfig.delegate = self
        // Start the GGLInstanceID shared instance with that config and request a registration
        // token to enable reception of notifications
        GGLInstanceID.sharedInstance().startWithConfig(instanceIDConfig)
        registrationOptions = [kGGLInstanceIDRegisterAPNSOption:deviceToken,
                               kGGLInstanceIDAPNSServerTypeSandboxOption:true]
        GGLInstanceID.sharedInstance().tokenWithAuthorizedEntity(gcmSenderID,
                                                                 scope: kGGLInstanceIDScopeGCM, options: registrationOptions, handler: registrationHandler)
        // [END get_gcm_reg_token]
    }
    
    // [START receive_apns_token_error]
    func application( application: UIApplication, didFailToRegisterForRemoteNotificationsWithError
        error: NSError ) {
        print("Registration for remote notification failed with error: \(error.localizedDescription)")
        // [END receive_apns_token_error]
        let userInfo = ["error": error.localizedDescription]
        NSNotificationCenter.defaultCenter().postNotificationName(
            registrationKey, object: nil, userInfo: userInfo)
    }
    
    // [START ack_message_reception]
    func application( application: UIApplication,
                      didReceiveRemoteNotification userInfo: [NSObject : AnyObject]) {
        print("Notification received out app: \(userInfo)")

        // This works only if the app started the GCM service
        GCMService.sharedInstance().appDidReceiveMessage(userInfo);
        // Handle the received message
        // [START_EXCLUDE]
        NSNotificationCenter.defaultCenter().postNotificationName(messageKey, object: nil,
                                                                  userInfo: userInfo)
        // [END_EXCLUDE]
    }
    
    func application( application: UIApplication,
                      didReceiveRemoteNotification userInfo: [NSObject : AnyObject],
                                                   fetchCompletionHandler handler: (UIBackgroundFetchResult) -> Void) {
        
        //GCM example process
        print("Notification received in app: \(userInfo)")
        // This works only if the app started the GCM service
        GCMService.sharedInstance().appDidReceiveMessage(userInfo);
        // Handle the received message
        // Invoke the completion handler passing the appropriate UIBackgroundFetchResult value
        // [START_EXCLUDE]
        if ( application.applicationState == UIApplicationState.Inactive || application.applicationState == UIApplicationState.Background ){
            //print("opened from a push notification when the app was on background")
            Logger.log("out app touch notification")
            NSNotificationCenter.defaultCenter().postNotificationName(messageKey, object: nil, userInfo: userInfo)
            //print("send notification")
            //NSNotificationCenter.defaultCenter().postNotificationName("testFailMove", object: nil)
        }else{
            //print("opened from a push notification when the app was on foreground")
            Logger.log("in app get notification")
            NSNotificationCenter.defaultCenter().postNotificationName(messageKeyInApp, object: nil, userInfo: userInfo)
        }
        handler(UIBackgroundFetchResult.NoData);
        // [END_EXCLUDE]
        
    }
    // [END ack_message_reception]
    
    func registrationHandler(registrationToken: String!, error: NSError!) {
        print("registrationHandler")
        if (registrationToken != nil) {
            self.registrationToken = registrationToken
            print("Registration Token: \(registrationToken)")
            //update drive gcm token to server
            DeviceManager.gcmToken = registrationToken
            self.subscribeToTopic()
            let userInfo = ["registrationToken": registrationToken]
            NSNotificationCenter.defaultCenter().postNotificationName(
                self.registrationKey, object: nil, userInfo: userInfo)
        } else {
            print("Registration to GCM failed with error: \(error.localizedDescription)")
            let userInfo = ["error": error.localizedDescription]
            NSNotificationCenter.defaultCenter().postNotificationName(
                self.registrationKey, object: nil, userInfo: userInfo)
        }
    }
    
    // [START on_token_refresh]
    func onTokenRefresh() {
        print("onTokenRefresh")
        // A rotation of the registration tokens is happening, so the app needs to request a new token.
        print("The GCM registration token needs to be changed.")
        GGLInstanceID.sharedInstance().tokenWithAuthorizedEntity(gcmSenderID,
                                                                 scope: kGGLInstanceIDScopeGCM, options: registrationOptions, handler: registrationHandler)
    }
    // [END on_token_refresh]
    
    // [START upstream_callbacks]
    func willSendDataMessageWithID(messageID: String!, error: NSError!) {
        print("willSendDataMessageWithID")
        if (error != nil) {
            // Failed to send the message.
        } else {
            // Will send message, you can save the messageID to track the message
        }
    }
    
    func didSendDataMessageWithID(messageID: String!) {
        print("didSendDataMessageWithID")
        // Did successfully send message identified by messageID
    }
    // [END upstream_callbacks]
    
    func didDeleteMessagesOnServer() {
        print("didDeleteMessagesOnServer")
        // Some messages sent to this device were deleted on the GCM server before reception, likely
        // because the TTL expired. The client should notify the app server of this, so that the app
        // server can resend those messages.
    }


}

