//
//  LaunchImageVC.swift
//  AccentEasy
//
//  Created by CMGVN on 1/14/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import UIKit

class LaunchImageVC: UIViewController {

    var timer:NSTimer!
    var number:Int!
    var nextScreen:Int!
    
    @IBOutlet weak var imgDog: UIImageView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        number = 1
        nextScreen = 0
        timer = NSTimer.scheduledTimerWithTimeInterval(0.5, target: self, selector: Selector("launchingImage"), userInfo: nil, repeats: true)
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
        if nextScreen == 10 {
            self.performSegueWithIdentifier("GoToLogin", sender: self)
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
