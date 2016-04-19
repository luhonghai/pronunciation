//
//  FeedbackVC.swift
//  AccentEasy
//
//  Created by CMGVN on 4/10/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class FeedbackVC: UIViewController, UITextViewDelegate {
    
    @IBOutlet weak var menuButton: UIBarButtonItem!
    @IBOutlet weak var btnOpenLink: UIButton!
    @IBOutlet weak var textView: UITextView!

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        textView.delegate = self
        
        //menu
        if self.revealViewController() != nil {
            menuButton.target = self.revealViewController()
            menuButton.action = "revealToggle:"
            self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
        }
        
        //menu style
        navigationItem.title = "feedback"
        setNavigationBarTransparent()
        
        //under line for text view
        /*let bottomLine = CALayer()
        bottomLine.frame = CGRectMake(0.0, textView.frame.height - 1, textView.frame.width, 1.0)
        bottomLine.backgroundColor = ColorHelper.APP_BLUE_SKY.CGColor
        //tviewDescription.borderStyle = UITextBorderStyle.None
        textView.layer.addSublayer(bottomLine)*/
        
        //self.textView.delegate = self
        textView.text = "description"
        textView.textColor = UIColor.lightGrayColor()
        
        //textView.becomeFirstResponder()
        //textView.selectedTextRange = textView.textRangeFromPosition(textView.beginningOfDocument, toPosition: textView.beginningOfDocument)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    
    func textViewDidBeginEditing(textView: UITextView) {
        if textView.textColor == UIColor.lightGrayColor() {
            textView.text = nil
            textView.textColor = UIColor.blackColor()
        }
    }
    
    func textViewDidEndEditing(textView: UITextView) {
        if textView.text.isEmpty {
            textView.text = "description"
            textView.textColor = UIColor.lightGrayColor()
        }
    }
    
    /**
     * Called when the user click on the view (outside the UITextField).
     */
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    /*
    func textView(textView: UITextView, shouldChangeTextInRange range: NSRange, replacementText text: String) -> Bool {
        
        // Combine the textView text and the replacement text to
        // create the updated text string
        let currentText:NSString = textView.text
        let updatedText = currentText.stringByReplacingCharactersInRange(range, withString:text)
        
        // If updated text view will be empty, add the placeholder
        // and set the cursor to the beginning of the text view
        if updatedText.isEmpty {
            
            textView.text = "Placeholder"
            textView.textColor = UIColor.lightGrayColor()
            
            textView.selectedTextRange = textView.textRangeFromPosition(textView.beginningOfDocument, toPosition: textView.beginningOfDocument)
            
            return false
        }
            
            // Else if the text view's placeholder is showing and the
            // length of the replacement string is greater than 0, clear
            // the text view and set its color to black to prepare for
            // the user's entry
        else if textView.textColor == UIColor.lightGrayColor() && !text.isEmpty {
            textView.text = nil
            textView.textColor = UIColor.blackColor()
        }
        
        return true
    }*/
    
    /*
    func textViewDidChangeSelection(textView: UITextView) {
        if self.view.window != nil {
            if textView.textColor == UIColor.lightGrayColor() {
                textView.selectedTextRange = textView.textRangeFromPosition(textView.beginningOfDocument, toPosition: textView.beginningOfDocument)
            }
        }
    }*/
    
    
    func setNavigationBarTransparent() {
        self.navigationController?.navigationBar.setBackgroundImage(UIImage(), forBarMetrics: UIBarMetrics.Default)
        self.navigationController?.navigationBar.shadowImage = UIImage()
        self.navigationController?.navigationBar.translucent = true
        self.navigationController?.view.backgroundColor = UIColor.clearColor()
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    
    @IBAction func btnOpenUrl(sender: AnyObject) {
        let url = NSURL(string: "http://www.accenteasy.com/useraccounts/TnC")!
        UIApplication.sharedApplication().openURL(url)
    }
    
    func showLoadding(){
        //show watting..
        let text = "Please wait..."
        self.showWaitOverlayWithText(text)
    }
    
    func hidenLoadding(){
        // Remove watting
        self.removeAllOverlays()
    }
    
    @IBAction func sendButtonTouchUp(sender: AnyObject) {
        DeviceManager.doIfConnectedToNetwork { () -> Void in
            self.sendFeedBack()
        }
    }
    
    func sendFeedBack(){
        /*
         data.put("acc", profile.getUsername());
         data.put("action", "request");
         data.put("imei", new DeviceUuidFactory(context).getDeviceUuid().toString());
         */
        showLoadding()
        let description = textView.text
        
        if description.isEmpty || description==nil || description == "description" {
            dispatch_async(dispatch_get_main_queue(),{
                SweetAlert().showAlert("", subTitle: "please enter your message", style: AlertStyle.Warning)
                self.hidenLoadding()
            })
            
            return
        }
        
        let profile = AccountManager.currentUser()
        
        AccountManager.sendFeedBack(profile, description: description) { (userProfile, success, message) -> Void in
            dispatch_async(dispatch_get_main_queue(),{
                if success {
                    SweetAlert().showAlert("successfully submitted!", subTitle: message, style: AlertStyle.Success, buttonTitle: "OK") {(isOk) -> Void in
                        //if isOk == true {
                            //self.performSegueWithIdentifier("ResetPassGoToLogin", sender: self)
                        //}
                        self.hidenLoadding()
                        self.textView.text = ""
                    }
                } else {
                    self.hidenLoadding()
                    AccountManager.showError("", message: message)
                }
            })
        }
    }


}
