//
//  TestFailPopupVC.swift
//  AccentEasy
//
//  Created by CMGVN on 4/8/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

@objc protocol TestPopupDelegate {
    optional func closeTestFailPopup(sender: AnyObject)
    optional func closeTestPassPopup(sender: AnyObject)
}

class TestFailPopupVC: UIViewController {
    
    var delegate: TestPopupDelegate?
    var toltalScore:Int!
    var passScore:Int!
    
    @IBOutlet weak var btnClose: UIButton!
    @IBOutlet weak var aviewToltalScore: AnalyzingView!
    @IBOutlet weak var aviewPassScore: AnalyzingView!
    

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        //style popup
        btnClose.tintColor = ColorHelper.APP_PURPLE
        
        aviewPassScore.showScore(passScore, showAnimation: true)
        aviewToltalScore.showScore(toltalScore, showAnimation:  true)
    }
    
    override func viewDidDisappear(animated: Bool) {
        //pop view
        //let viewControllers: [UIViewController] = self.navigationController!.viewControllers as [UIViewController];
        //self.navigationController!.popToViewController(viewControllers[viewControllers.count - 3], animated: false);
        
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        NSNotificationCenter.defaultCenter().postNotificationName("testFailMove", object: nil)
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

    @IBAction func btnCloseTouchUp(sender: AnyObject) {
        delegate?.closeTestFailPopup!(self)
    }
    
}
