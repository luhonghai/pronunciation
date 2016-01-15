//
//  TestNewScreenViewController.swift
//  SwiftSidebarMenu
//
//  Created by CMGVN on 1/8/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import UIKit

class TestNewScreenViewController: UIViewController {

    var timer:NSTimer!
    var number:Int!
    
    @IBOutlet weak var imgDog: UIImageView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //btnViewOutlet.setTitle("enter", forState: UIControlState)
        // Do any additional setup after loading the view.
        number = 1
        timer = NSTimer.scheduledTimerWithTimeInterval(0.5, target: self, selector: Selector("launchingImage"), userInfo: nil, repeats: true)
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
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
