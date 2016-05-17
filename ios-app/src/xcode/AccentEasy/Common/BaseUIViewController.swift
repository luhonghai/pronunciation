//
//  BaseUIViewController.swift
//  AccentEasy
//
//  Created by CMGVN on 5/12/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation


class BaseUIViewController: UIViewController {
    
    var baseNotification: BaseNotification!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        baseNotification = BaseNotification(viewController: self)
    }
    
    deinit {
        baseNotification.clean()
    }
}

class BaseNotification: NSObject, InvitationPopupDelegate {
    
    var viewController: UIViewController!
    
    init(viewController: UIViewController) {
        super.init()
        
        self.viewController = viewController
        /*test gcm*/
        let appDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
        //NSNotificationCenter.defaultCenter().addObserver(self, selector: "updateRegistrationStatus:", name: appDelegate.registrationKey, object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "showReceivedMessage:", name: appDelegate.messageKey, object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "showReceivedMessageInApp:", name: appDelegate.messageKeyInApp, object: nil)
    }
    
    /*func updateRegistrationStatus(notification: NSNotification) {
     if let info = notification.userInfo as? Dictionary<String,String> {
     if let error = info["error"] {
     print("Error registering!");
     showAlert("Error registering with GCM", message: error)
     } else if let _ = info["registrationToken"] {
     print("Registered!")
     let message = "Check the xcode debug console for the registration token that you " +
     " can use with the demo server to send notifications to your device"
     showAlert("Registration Successful!", message: message)
     }
     } else {
     print("Software failure. Guru meditation.")
     }
     }*/
    
    func showReceivedMessage(notification: NSNotification) {
        print("run in showReceivedMessage")
        viewController.showLoadding("Loading data...")
        getInvitationData()
        /*if let info = notification.userInfo as? Dictionary<String,AnyObject> {
         print (info)
         if let aps = info["aps"] as? Dictionary<String, String> {
         print (aps)
         
         dispatch_async(dispatch_get_main_queue(), {
         delay(1) {
         self.showAlert("Message received", message: aps["alert"]!)
         }
         })
         
         }
         } else {
         print("Software failure. Guru meditation.")
         }*/
    }
    
    func getInvitationData() {
        weak var weakSelf = viewController
        AccountManager.getInvitationData(AccountManager.currentUser()) { (userProfile, success, message) in
            dispatch_async(dispatch_get_main_queue(),{
                weakSelf!.hidenLoadding()
                if success {
                    //let invitationMainVC:InvitationMainVC = InvitationMainVC(nibName:"InvitationMainVC", bundle: nil)
                    let nextController = weakSelf!.storyboard?.instantiateViewControllerWithIdentifier("InvitationMainVC") as! InvitationMainVC
                    //self.navigationController?.popToRootViewControllerAnimated(false)
                    weakSelf!.navigationController?.pushViewController(nextController, animated: false)
                    //self.revealViewController().pushFrontViewController(invitationMainVC, animated: true)
                } else {
                    AccountManager.showError(message: message)
                }
            })
        }
    }
    
    
    func showReceivedMessageInApp(notification: NSNotification) {
        print("run in showReceivedMessageInApp")
        //open popup
        let invitationNotificationPopup:InvitationNotificationPopup = InvitationNotificationPopup(nibName: "InvitationNotificationPopup", bundle: nil)
        invitationNotificationPopup.delegate = self
        viewController.presentpopupViewController(invitationNotificationPopup, animationType: .Fade, completion: {() -> Void in })
        
        
        /*if let info = notification.userInfo as? Dictionary<String,AnyObject> {
         print (info)
         if let aps = info["aps"] as? Dictionary<String, String> {
         print (aps)
         
         dispatch_async(dispatch_get_main_queue(), {
         delay(1) {
         self.showAlert("Message received", message: aps["alert"]!)
         }
         })
         
         }
         } else {
         print("Software failure. Guru meditation.")
         }*/
    }
    
    func closePopup(sender: AnyObject) {
        viewController.dismissPopupViewController(.Fade)
    }
    
    func invitationNotificationPopupTouchOK(sender: AnyObject) {
        viewController.dismissPopupViewController(.Fade)
        //load
        viewController.showLoadding("Loading data...")
        getInvitationData()
    }
    
    /*func showAlert(title:String, message:String) {
     if #available(iOS 8.0, *) {
     let alert = UIAlertController(title: title,
     message: message, preferredStyle: .Alert)
     let dismissAction = UIAlertAction(title: "Dismiss", style: .Destructive, handler: nil)
     alert.addAction(dismissAction)
     self.presentViewController(alert, animated: true, completion: nil)
     } else {
     // Fallback on earlier versions
     let alert = UIAlertView.init(title: title, message: message, delegate: nil,
     cancelButtonTitle: "Dismiss")
     alert.show()
     }
     }*/
    
    func clean() {
        Logger.log("run in deinit")
        NSNotificationCenter.defaultCenter().removeObserver(self)

    }

}