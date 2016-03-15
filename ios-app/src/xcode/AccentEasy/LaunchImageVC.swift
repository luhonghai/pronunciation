//
//  LaunchImageVC.swift
//  AccentEasy
//
//  Created by CMGVN on 1/14/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class LaunchImageVC: UIViewController {
    
    var timer:NSTimer!
    var number:Int!
    var nextScreen:Int!
    var willClose = false
    var willCheckLogin = true
    
    var currentUser: UserProfile!
    
    @IBOutlet weak var imgDog: UIImageView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        currentUser = AccountManager.currentUser()
        // Do any additional setup after loading the view.
        number = 1
        nextScreen = 0
        timer = NSTimer.scheduledTimerWithTimeInterval(0.5, target: self, selector: Selector("launchingImage"), userInfo: nil, repeats: true)
        
        weak var weakSelf = self;
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)) {
            DatabaseHelper.checkDatabaseVersion() {(success) -> Void in
                print (success)
                let freestyleDbAdapter = FreeStyleDBAdapter()
                freestyleDbAdapter.prepare()
                if !success {
                    //TODO show alert no database found
                    print ("error checkDatabaseVersion")
                }
                weakSelf!.willClose = true
            }
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func launchingImage(){
        weak var weakSelf = self
        let imgSwap:String = "sl_dog_"+String(number)+".png"
        imgDog.image = UIImage(named: imgSwap)
        number = number + 1
        if number == 4{
            number=1
        }
        nextScreen = nextScreen + 1
        if self.willClose {
            if currentUser.isLogin {
                if willCheckLogin {
                    willCheckLogin = false
                    AccountManager.auth(currentUser, isCheck: true, completion: { (userProfile, success, message) -> Void in
                        dispatch_async(dispatch_get_main_queue(),{
                            if success {
                                weakSelf!.gotoMainPage()
                            } else {
                                weakSelf!.gotoLoginPage()
                            }
                        })
                    })
                }
            } else {
               gotoLoginPage()
            }
        }
    }
    
    func gotoMainPage() {
        clearTimer()
        self.performSegueWithIdentifier("gotoMainPage", sender: self)
        
    }
    
    func clearTimer() {
        if timer != nil {
            timer.invalidate()
            timer = nil
        }
    }
    
    func gotoLoginPage() {
        clearTimer()
        self.performSegueWithIdentifier("GoToLogin", sender: self)
    }

    

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        GlobalData.getInstance().isShowLogin = false
    }
    

}
