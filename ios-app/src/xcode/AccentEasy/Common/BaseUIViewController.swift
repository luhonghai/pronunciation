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
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        baseNotification = BaseNotification(viewController: self)
    }
    
    override func viewDidDisappear(animated: Bool) {
        super.viewDidDisappear(animated)
        if baseNotification != nil {
            Logger.log("run in BaseUIViewController deinit")
            baseNotification.clean()
        }
    }
}

class BaseNotification: NSObject, InvitationPopupDelegate {
    
    var viewController: UIViewController!
    var invitationMessage = InvitationMessage()
    var title:String = ""
    
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
        Logger.log("run in showReceivedMessage")
        //parser notification return
        getNotificationMessage(notification)
        viewController.showLoadding("Loading data...")
        
        if invitationMessage.type == 1 {
            //invitation process
            Logger.log("notification invitation process")
            getInvitationData()
        } else if invitationMessage.type == 2 {
            //update course process
            Logger.log("notification update course process")
            getCourseData()
        } else {
            Logger.log("notification type null")
        }
        
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
                    Logger.log("is invitation page \(GlobalData.getInstance().isInvitationPage)")
                    //if !GlobalData.getInstance().isInvitationPage {
                        //let invitationMainVC:InvitationMainVC = InvitationMainVC(nibName:"InvitationMainVC", bundle: nil)
                        let nextController = weakSelf!.storyboard?.instantiateViewControllerWithIdentifier("InvitationMainVC") as! InvitationMainVC
                        //self.navigationController?.popToRootViewControllerAnimated(false)
                        weakSelf!.navigationController?.pushViewController(nextController, animated: false)
                        //self.revealViewController().pushFrontViewController(invitationMainVC, animated: true)
                    //}
                   
                } else {
                    AccountManager.showError(message: message)
                }
            })
        }
    }
    
    func getCourseData() {
        weak var weakSelf = viewController
        AccountManager.fetchCourses(AccountManager.currentUser()) { (userProfile, success, message) -> Void in
            dispatch_async(dispatch_get_main_queue(),{
                //TODO show error message
                AccountManager.updateProfile(userProfile)
                weakSelf!.hidenLoadding() 
                if success {
                    Logger.log("move to lesson page")
                    //Logger.log("is invitation page \(GlobalData.getInstance().isInvitationPage)")
                    //if !GlobalData.getInstance().isInvitationPage {
                    //let invitationMainVC:InvitationMainVC = InvitationMainVC(nibName:"InvitationMainVC", bundle: nil)
                    let nextController = weakSelf!.storyboard?.instantiateViewControllerWithIdentifier("CoursesViewController") as! CoursesViewController
                    //self.navigationController?.popToRootViewControllerAnimated(false)
                    weakSelf!.navigationController?.pushViewController(nextController, animated: false)
                    //self.revealViewController().pushFrontViewController(invitationMainVC, animated: true)
                    //}
                } else {
                    AccountManager.showError(message: message)
                }
            })
        }
    }

    func getNotificationMessage(notification: NSNotification){
        if let info = notification.userInfo as? Dictionary<String,AnyObject> {
            print (info)
            if let data = info["data"] as? String{
                invitationMessage = Mapper<InvitationMessage>().map(data)!
                title = (invitationMessage.content)!
            }
            
            
            /*if let aps = info["aps"] as? Dictionary<String, AnyObject> {
             //print (aps)
             if let alert = aps["alert"] as? Dictionary<String, AnyObject> {
             //print (alert)
             //print (alert["body"])
             if let message = alert["body"] as? String {
             title = message
             }
             }
             
             }*/
        }
    }
    
    
    func showReceivedMessageInApp(notification: NSNotification) {
        Logger.log("run in showReceivedMessageInApp")
        //parser notification return
        getNotificationMessage(notification)
        //open popup
        let invitationNotificationPopup:InvitationNotificationPopup = InvitationNotificationPopup(nibName: "InvitationNotificationPopup", bundle: nil)
        invitationNotificationPopup.message = title
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
        Logger.log("invitationNotificationPopupTouchOK")
        viewController.dismissPopupViewController(.Fade)
        //load
        viewController.showLoadding("Loading data...")
        
        if invitationMessage.type == 1 {
            //invitation process
            Logger.log("notification invitation process")
            getInvitationData()
        } else if invitationMessage.type == 2 {
            //update course process
            Logger.log("notification update course process")
            getCourseData()
        } else {
            Logger.log("notification type null")
        }

        
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