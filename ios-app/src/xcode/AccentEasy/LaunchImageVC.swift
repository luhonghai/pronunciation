//
//  LaunchImageVC.swift
//  AccentEasy
//
//  Created by CMGVN on 1/14/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class LaunchImageVC: UIViewController {

    var userProfileSaveInApp:NSUserDefaults!
    
    var timer:NSTimer!
    var number:Int!
    var nextScreen:Int!
    var willClose = false
    
    @IBOutlet weak var imgDog: UIImageView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        number = 1
        nextScreen = 0
        timer = NSTimer.scheduledTimerWithTimeInterval(0.5, target: self, selector: Selector("launchingImage"), userInfo: nil, repeats: true)
        
        userProfileSaveInApp = NSUserDefaults()
        weak var weakSelf = self;
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)) {
            DatabaseHelper.checkDatabaseVersion() {(success) -> Void in
                let freestyleDbAdapter = FreeStyleDBAdapter()
                freestyleDbAdapter.prepare()
                if !success {
                    //TODO show alert no database found
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
        let imgSwap:String = "sl_dog_"+String(number)+".png"
        imgDog.image = UIImage(named: imgSwap)
        number = number + 1
        if number == 4{
            number=1
        }
        nextScreen = nextScreen + 1
        if self.willClose {
            timer.invalidate()
            timer = nil
            self.performSegueWithIdentifier("GoToLogin", sender: self)
            //self.dismissViewControllerAnimated(true, completion: nil)
        }
    }

    

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        userProfileSaveInApp.setObject(false, forKey: Login.KeyIsShowLogin)
    }
    

}
