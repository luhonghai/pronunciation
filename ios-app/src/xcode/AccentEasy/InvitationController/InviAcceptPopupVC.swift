//
//  InviAcceptPopupVC.swift
//  AccentEasy
//
//  Created by CMGVN on 4/26/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class InviAcceptPopupVC: UIViewController {

    var delegate:InvitationPopupDelegate?
    @IBOutlet weak var btnClose: UIButton!
    @IBOutlet weak var btnReject: UIButton!

    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        //style popup
        self.view.layer.cornerRadius = 5
        //self.view.backgroundColor = ColorHelper.APP_LIGHT_BLUE
        //self.view.backgroundColor = UIColor(patternImage: UIImage(named: "global_bg.png")!)
        
        var pImage = UIImage(named: "ic_close_dialog.png")!
        pImage = ImageHelper.imageWithImage(image: pImage, w: 25, h: 25)
        btnClose.layer.cornerRadius = btnClose.frame.size.width/2
        btnClose.setImage(pImage, forState: UIControlState.Normal)
        btnClose.tintColor = ColorHelper.APP_PURPLE
        
        var rImage = UIImage(named: "ic_close_dialog.png")!
        rImage = ImageHelper.imageWithImage(image: rImage, w: 50, h: 50)
        btnReject.layer.cornerRadius = btnReject.frame.size.width/2
        btnReject.setImage(rImage, forState: UIControlState.Normal)
        btnReject.tintColor = ColorHelper.APP_RED_MAGENTA

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
    @IBAction func btnClose(sender: AnyObject) {
        delegate?.closePopup(self)
    }

}
