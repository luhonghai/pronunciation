//
//  LoginHomeVC.swift
//  AccentEasy
//
//  Created by CMGVN on 1/13/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import UIKit

class LoginHomeVC: UIViewController {
    
    var showLog:Int!

    @IBOutlet weak var btnLoginFB: UIButton!
    @IBOutlet weak var btnLoginAC: UIButton!
    @IBOutlet weak var btnAltLogin: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        /*let imgBg = UIImage(named: "sl_background.png")
        let imgBgView = UIImageView(image: imgBg)
        imgBgView.contentMode = UIViewContentMode.ScaleAspectFill
        self.view.backgroundColor = UIColor(patternImage: imgBg!)*/
        showLog = 1

    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func tappedAltLogin(sender: AnyObject) {
        showLog = showLog + 1
        if showLog == 2 {
            btnLoginFB.hidden = false
        } else if showLog == 3{
            btnLoginAC.hidden = false
            btnAltLogin.hidden = true
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
