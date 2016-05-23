//
//  LicenceVC.swift
//  AccentEasy
//
//  Created by CMGVN on 4/10/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class LicenceVC: BaseUIViewController {

    @IBOutlet weak var menuButton: UIBarButtonItem!
    @IBOutlet weak var webView: UIWebView!

    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        //menu
        if self.revealViewController() != nil {
            menuButton.target = self.revealViewController()
            menuButton.action = "revealToggle:"
            self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
        }
        
        //show content
        let appversion = "release " + DeviceManager.appVersionCode()
        let filePath = NSBundle.mainBundle().pathForResource("license", ofType: "html")
        let contentData = NSFileManager.defaultManager().contentsAtPath(filePath!)
        let htmlString = NSString(data: contentData!, encoding: NSUTF8StringEncoding) as? String
        let replacedHtmlContent = htmlString?.stringByReplacingOccurrencesOfString("%APP_VERSION%", withString: appversion)
        webView.loadHTMLString(replacedHtmlContent! , baseURL: nil)
        
        //navigation bar
        navigationItem.title = "licence"
        setNavigationBarTransparent()
    }
    
    override func viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews();
        webView.scrollView.contentInset = UIEdgeInsetsZero;
    }
    
    func setNavigationBarTransparent() {
        self.navigationController?.navigationBar.setBackgroundImage(UIImage(), forBarMetrics: UIBarMetrics.Default)
        self.navigationController?.navigationBar.shadowImage = UIImage()
        self.navigationController?.navigationBar.translucent = true
        self.navigationController?.view.backgroundColor = UIColor.clearColor()
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
