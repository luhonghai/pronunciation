//
//  OurItem3ViewController.swift
//  SwiftSidebarMenu
//
//  Created by CMGVN on 1/7/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class OurItem3ViewController: UIViewController, LSPopupVCDelegate{

    @IBOutlet weak var menuButton: UIBarButtonItem!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        if self.revealViewController() != nil {
            menuButton.target = self.revealViewController()
            menuButton.action = "revealToggle:"
            self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
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
    
    @IBAction func showLS(sender: AnyObject) {
        self.displayViewController(.Fade)
    }
    
    func displayViewController(animationType: SLpopupViewAnimationType) {

        let lSPopupVC:LSPopupVC = LSPopupVC(nibName:"LSPopupVC", bundle: nil)
        
        lSPopupVC.delegate = self
        
        self.presentpopupViewController(lSPopupVC, animationType: animationType, completion: { () -> Void in
            
        })
    }
    
    func closePopup(sender: AnyObject) {
        self.dismissPopupViewController(.Fade)
    }


}
