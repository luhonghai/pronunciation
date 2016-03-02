//
//  TestNewScreenViewController.swift
//  SwiftSidebarMenu
//
//  Created by CMGVN on 1/8/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit
import FBSDKCoreKit
import FBSDKLoginKit

class TestNewScreenViewController: UIViewController {

    var timer:NSTimer!
    var number:Int!
    
    @IBOutlet weak var imgDog: UIImageView!
    @IBOutlet weak var activityIndicatorView: UIActivityIndicatorView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //btnViewOutlet.setTitle("enter", forState: UIControlState)
        // Do any additional setup after loading the view.
        number = 1
        timer = NSTimer.scheduledTimerWithTimeInterval(0.5, target: self, selector: Selector("launchingImage"), userInfo: nil, repeats: true)
        
        dispatch_async(dispatch_get_main_queue(), {
            
            SweetAlert().showAlert("validation successful!", subTitle: "your acount has already been activated. please login with your email address and password.", style: AlertStyle.Success, buttonTitle: "Ok") {(isOk) -> Void in
                if isOk == true {
                    print("ok")
                }
            }
            
            
            /*SweetAlert().showAlert("Are you sure?", subTitle: "You file will permanently delete!", style: AlertStyle.Warning, buttonTitle:"No, cancel plx!", buttonColor:UIColor.blueColor() , otherButtonTitle:  "Yes, delete it!", otherButtonColor: UIColor.redColor()) { (isOtherButton) -> Void in
                if isOtherButton == true {
                    
                    self.activityIndicatorView.startAnimating()
                    
                    SweetAlert().showAlert("Cancelled!", subTitle: "Your imaginary file is safe", style: AlertStyle.Error)
                }
                else {
                    SweetAlert().showAlert("Deleted!", subTitle: "Your imaginary file has been deleted!", style: AlertStyle.Success)
                }
            }*/
            
            
        /*SweetAlert().showAlert("Are you sure?", subTitle: "You file will permanently delete!", style: AlertStyle.Warning, buttonTitle:"Cancel", buttonColor:UIColor.blueColor() , otherButtonTitle:  "Yes, delete it!", otherButtonColor: UIColor.redColor()) { (isOtherButton) -> Void in
            if isOtherButton == true {
                
                print("Cancel Button  Pressed")
            }
            else {
                SweetAlert().showAlert("Deleted!", subTitle: "Your imaginary file has been deleted!", style: AlertStyle.Success)
            }
        }*/
        })
        
    }
    
    func launchingImage(){
        let imgSwap:String = "sl_dog_"+String(number)+".png"
        imgDog.image = UIImage(named: imgSwap)
        number = number + 1
        if number == 4{
            number=1
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    
    @IBAction func logoutTapped(sender: AnyObject) {
        let loginManage = FBSDKLoginManager()
        loginManage.logOut()
        
        let loginPage = self.storyboard?.instantiateViewControllerWithIdentifier("LoginHomeVC") as! LoginHomeVC
        let loginPageNav = UINavigationController(rootViewController: loginPage)
        
        let appDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
        
        appDelegate.window?.rootViewController = loginPageNav
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
