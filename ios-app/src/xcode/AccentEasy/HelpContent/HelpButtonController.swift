//
//  HelpButtonController.swift
//  AccentEasy
//
//  Created by Hai Lu on 4/8/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit
import BEMCheckBox

protocol HelpButtonDelegate {
    func onHelpButtonShow()
    func onHelpButtonClose(neverShowAgain: Bool)
}

class HelpButtonController : UIView, BEMCheckBoxDelegate {
    @IBOutlet private var contentView:UIView?
    // other outlets
    @IBOutlet weak var checkbox: BEMCheckBox!
    
    @IBOutlet weak var btnContinue: UIButton!
    
    @IBOutlet weak var imgHelpContext: UIImageView!
    
    @IBAction func clickContinue(sender: AnyObject) {
        doContinue()
    }
    @IBAction func btnContinueGrayTouchUp(sender: AnyObject) {
        doContinue()
    }
    
    @IBAction func clickContinueLabel(sender: AnyObject) {
        doContinue()
    }
    @IBAction func swipeHelpContext(sender: AnyObject) {
        doContinue()
    }
    
    var delegate: HelpButtonDelegate?
    
    var timer: NSTimer!
    
    func doContinue() {
        Logger.log("click continue")
        self.close()
        delegate?.onHelpButtonClose(self.checkbox.on)
    }
    
    @IBAction func clickNSALabel(sender: AnyObject) {
        checkbox.setOn(!checkbox.on, animated: true)
        checkNSA()
    }
    
    override init(frame: CGRect) { // for using CustomView in code
        super.init(frame: frame)
        self.commonInit()
    }
    
    required init?(coder aDecoder: NSCoder) { // for using CustomView in IB
        super.init(coder: aDecoder)
        self.commonInit()
    }
    
    private func commonInit() {
        NSBundle.mainBundle().loadNibNamed("HelpButtonController", owner: self, options: nil)
        guard let content = contentView else { return }
        content.frame = self.bounds
        content.autoresizingMask = [.FlexibleHeight, .FlexibleWidth]
        checkbox.onAnimationType = BEMAnimationType.Fill
        checkbox.offAnimationType = BEMAnimationType.Fill
        self.addSubview(content)
        self.userInteractionEnabled = true
        checkbox.delegate = self
    }
    
    func didTapCheckBox(checkBox: BEMCheckBox) {
        checkNSA()
    }
    
    func checkNSA() {
        if checkbox.on {
            resetTimer()
        } else {
            clearTimer()
        }
    }
    
    func close() {
        self.removeFromSuperview()
    }
    
    func show(rect: CGRect) {
        if let app = UIApplication.sharedApplication().delegate as? AppDelegate, let window = app.window {
            self.frame = rect
            window.addSubview(self)
            self.contentView?.frame = rect
            self.btnContinue.layer.cornerRadius = self.btnContinue.frame.width / 2
            self.clipsToBounds = true
            delegate?.onHelpButtonShow()
        }
    }
    
    func clearTimer() {
        if timer != nil {
            timer.invalidate()
            timer = nil
        }
    }
    
    func resetTimer() {
        clearTimer()
        timer = NSTimer.scheduledTimerWithTimeInterval(2, target: self, selector: Selector("doContinue"), userInfo: nil, repeats: false)
    }
}
