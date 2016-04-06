//
//  LessonTipPopupVC.swift
//  AccentEasy
//
//  Created by CMGVN on 4/2/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

protocol LessonTipPopupVCDelegate {
    func closeLessonTipPopup(sender: LessonTipPopupVC)
}

class LessonTipPopupVC: UIViewController {
    
    var delegate: LessonTipPopupVCDelegate?
    var contentPopup: String!
    
    @IBOutlet weak var btnClose: UIButton!
    @IBOutlet weak var tvContent: UITextView!

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        //style popup
        self.view.layer.cornerRadius = 5
        self.view.backgroundColor = ColorHelper.APP_LIGHT_BLUE
        
        var pImage = UIImage(named: "ic_close_dialog.png")!
        pImage = ImageHelper.imageWithImage(image: pImage, w: 25, h: 25)
        btnClose.layer.cornerRadius = btnClose.frame.size.width/2
        btnClose.setImage(pImage, forState: UIControlState.Normal)
        btnClose.tintColor = ColorHelper.APP_PURPLE
        
        tvContent.textColor = ColorHelper.APP_PURPLE
        if contentPopup == nil {
            tvContent.text = "Say the word and check your score for each phonneme. You can repeat the lesson to practise. Make sure that you sound the consonants in the middle and at the end of the words."
        } else {
            tvContent.text = contentPopup
        }
        tvContent.vAlignMiddle()
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
        delegate?.closeLessonTipPopup(self)
    }
}
