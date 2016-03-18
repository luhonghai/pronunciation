//
//  FreeStyleVC.swift
//  AccentEasy
//
//  Created by CMGVN on 3/4/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class FreeStyleVC: UIViewController, UIScrollViewDelegate, SWRevealViewControllerDelegate{

    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var menuButton: UIBarButtonItem!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        revealViewController().delegate = self
        
        //self.scrollView.delegate = self


        // Do any additional setup after loading the view.
        
        let vc0 = FSMainSwipeVC(nibName: "FSMainSwipeVC", bundle: nil)
        
        self.addChildViewController(vc0)
        self.scrollView.addSubview(vc0.view)
        vc0.didMoveToParentViewController(self)
        
        //----
        let vc1 = FSDetailSwipeVC(nibName: "FSDetailSwipeVC", bundle:nil)
        
        var frame1 = vc1.view.frame
        frame1.origin.x = self.view.frame.size.width
        vc1.view.frame = frame1
        
        self.addChildViewController(vc1)
        self.scrollView.addSubview(vc1.view)
        vc1.didMoveToParentViewController(self)
        
        self.scrollView.contentSize = CGSizeMake(self.view.frame.size.width * 2, self.view.frame.size.height - 66);
        
        scrollView.scrollEnabled = false
        
        // Do any additional setup after loading the view.
        if self.revealViewController() != nil {
            menuButton.target = self.revealViewController()
            menuButton.action = "revealToggle:"
            self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
            
        }
        
    }
    
    func revealController(revealController: SWRevealViewController!, willMoveToPosition position: FrontViewPosition) {
        let tagId = 4207868
        
        if revealController.frontViewPosition == FrontViewPosition.Right {
            Logger.log("right")
            let lock = self.view.viewWithTag(tagId)
            UIView.animateWithDuration(0.25, animations: {
                lock?.alpha = 0
                }, completion: {(finished: Bool) in
                    lock?.removeFromSuperview()
                }
            )
            lock?.removeFromSuperview()
        } else if revealController.frontViewPosition == FrontViewPosition.Left {
            Logger.log("left")
            let lock = UIView(frame: self.view.bounds)
            lock.autoresizingMask = [.FlexibleWidth, .FlexibleHeight ]
            lock.tag = tagId
            lock.alpha = 0
            //lock.backgroundColor = UIColor.blackColor()
            lock.addGestureRecognizer(UITapGestureRecognizer(target: self.revealViewController(), action: "revealToggle:"))
            self.view.addSubview(lock)
            UIView.animateWithDuration(0.75, animations: {
                lock.alpha = 0.333
                }
            )
        }
    }
    
    var isShow:Bool = true
    
    @IBAction func nextPageTapped(sender: AnyObject) {
        //self.scrollView.contentSize = CGSizeMake(self.view.frame.size.width * 2, self.view.frame.size.height - 66);
        if isShow {
            let text = "Please wait..."
            self.showWaitOverlayWithText(text)
            isShow = false
        }else{
            self.removeAllOverlays()
            isShow = true
        }
        
    }
    
    override func viewDidLayoutSubviews() {
        Logger.log("hoang")
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func scrollViewDidScroll(scrollView: UIScrollView) {
        //Logger.log("scroll")
    }
    
    func scrollViewWillEndDragging(scrollView: UIScrollView, withVelocity velocity: CGPoint, targetContentOffset: UnsafeMutablePointer<CGPoint>) {
        Logger.log("dsadasdsa")
    }
    
    func scrollViewWillBeginDragging(scrollView: UIScrollView) {
        Logger.log(scrollView.subviews.indexOf(scrollView))
    }
    
    func scrollViewDidEndDragging(scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        //Logger.log("aaaa")
        //Logger.log(decelerate)
    }
    
    func scrollViewDidEndDecelerating(scrollView: UIScrollView) {
        //Logger.log("bbb")
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
