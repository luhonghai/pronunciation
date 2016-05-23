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
    var willClose:Bool = false
    var willCheckLogin:Bool = true
    var currentDate:NSDate = NSDate()
    var startSecond:Double!
    var isShowLoadding = false
    
    var currentUser: UserProfile!
    
    @IBOutlet weak var imgDog: UIImageView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //get minute
        startSecond = currentDate.timeIntervalSince1970
        
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
                let lessonDBAdapter = LessonDBAdapter()
                lessonDBAdapter.prepare()
                if !success {
                    //TODO show alert no database found
                    Logger.log("error checkDatabaseVersion")
                    dispatch_async(dispatch_get_main_queue(),{
                        
                        SweetAlert().showAlert("Khong tai duoc CSDL", subTitle: "check internet di em oi", style: AlertStyle.Error, buttonTitle: "Ok") {(isOk) -> Void in
                            //if isOk == true {
                                //self.performSegueWithIdentifier("ConfirmRegisterGoToLogin", sender: self)
                            //}
                            //UIControl().sendAction(Selector("suspend"), to: UIApplication.sharedApplication(), forEvent: nil)
                            exit(0)
                        }
                    })
                } else {
                    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)) {
                        let wordCollectionDb = WordCollectionDbApdater()
                        wordCollectionDb.prepare()
                    }
                }
                weakSelf!.willClose = true
            }
        }
        
    }
    
    override func viewDidDisappear(animated: Bool) {
        if isShowLoadding {
            hidenLoadding()
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

        currentDate = NSDate()
        
        if currentDate.timeIntervalSince1970 - startSecond < 4 {
            return
        }
        
        if (currentDate.timeIntervalSince1970 - startSecond >= 10) && !isShowLoadding{
            showLoadding("installing...")
            isShowLoadding = true
        }

        nextScreen = nextScreen + 1
        if self.willClose {
            if currentUser.isLogin {
                if willCheckLogin {
                    willCheckLogin = false
                    AccountManager.auth(currentUser, isCheck: true, completion: { (userProfile, success, message) -> Void in
                        dispatch_async(dispatch_get_main_queue(),{
                            if success {
                                weakSelf!.fetchCourses(weakSelf!.currentUser)
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
    
    func fetchCourses(userProfile: UserProfile) {
        weak var weakSelf = self
        AccountManager.fetchCourses(userProfile) { (userProfile, success, message) -> Void in
            //TODO show error message
            if success {
                
            }
            AccountManager.updateProfile(userProfile)
            weakSelf!.getInvitationData()
        }
    }
    
    func getInvitationData() {
        weak var weakSelf = self
        AccountManager.getInvitationData(AccountManager.currentUser()) { (userProfile, success, message) in
            dispatch_async(dispatch_get_main_queue(),{
                weakSelf!.gotoMainPage()
            })
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
