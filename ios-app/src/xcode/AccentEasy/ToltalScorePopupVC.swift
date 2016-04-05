//
//  ToltalScorePopupVC.swift
//  AccentEasy
//
//  Created by CMGVN on 3/31/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit


protocol ToltalScorePopupVCDelegate {
    func closeToltaScorelPopup(sender: ToltalScorePopupVC)
}

class ToltalScorePopupVC: UIViewController {

    var delegate: ToltalScorePopupVCDelegate?
    var lessonTitle: String!
    var toltalScore: Int!
    
    @IBOutlet weak var btnClose: UIButton!
    @IBOutlet weak var tvTitle: UITextView!
    @IBOutlet weak var viewAnalyzing: AnalyzingView!
    
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

        tvTitle.textColor = ColorHelper.APP_PURPLE
        tvTitle.text = "Your overall score for \(lessonTitle) is"
        tvTitle.vAlignMiddle()
        
        viewAnalyzing.showScore(toltalScore, showAnimation: true)
        
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
        delegate?.closeToltaScorelPopup(self)
    }
    
}
