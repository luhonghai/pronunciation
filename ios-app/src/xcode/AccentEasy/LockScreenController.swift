//
//  LockScreenController.swift
//  AccentEasy
//
//  Created by Hai Lu on 22/04/2016.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class LockScreenController: UIView {
    
    var isShowing = false
    
    @IBOutlet var contentView: UIView!
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    
    override init(frame: CGRect) { // for using CustomView in code
        super.init(frame: frame)
        self.commonInit()
    }
    
    required init?(coder aDecoder: NSCoder) { // for using CustomView in IB
        super.init(coder: aDecoder)
        self.commonInit()
    }
    
    private func commonInit() {
        NSBundle.mainBundle().loadNibNamed("LockScreenController", owner: self, options: nil)
        guard let content = contentView else { return }
        content.frame = self.bounds
        content.autoresizingMask = [.FlexibleHeight, .FlexibleWidth]
        self.addSubview(content)
        //self.userInteractionEnabled = false
    }

    func close() {
        self.removeFromSuperview()
        isShowing = false
    }
    
    func show(rect: CGRect) {
        if let app = UIApplication.sharedApplication().delegate as? AppDelegate, let window = app.window {
            self.frame = rect
            window.addSubview(self)
            self.contentView?.frame = rect
            self.clipsToBounds = true
            isShowing = true
        }
    }
}
