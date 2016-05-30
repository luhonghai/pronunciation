//
//  IPAPopupVC.swift
//  AccentEasy
//
//  Created by CMGVN on 3/2/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import UIKit

protocol IPAPopupViewControllerDelegate {
    func pressPlayExample(sender: IPAPopupVC?)
    func pressShowChart(sender: IPAPopupVC?)
    func pressClosePopup(sender: IPAPopupVC?)
    func goToViewController(sender: IPAPopupVC?, vcId: String)
}

class IPAPopupVC: UIViewController {
    enum PopupMode {
        case CHART_VIDEO
        case TIP_CHART
        case TIP_VIDEO
    }
    var popupMode = PopupMode.CHART_VIDEO
    var selectedIPA: IPAMapArpabet!
    var score = -1
    var delegate:IPAPopupViewControllerDelegate?
    var isShow:Bool = false
    
    @IBOutlet weak var bgView: UIView!
    @IBOutlet weak var lblScore: UILabel!
    @IBOutlet weak var lblIPA: UILabel!
    @IBOutlet weak var btnPlayExample: UIButton!
    @IBOutlet weak var btnShowChart: UIButton!
    @IBOutlet weak var btnTop: UIButton!
    
    var graphPopup: GraphPageItemController!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        bgView.layer.cornerRadius = 5
        btnPlayExample.layer.cornerRadius = btnPlayExample.frame.size.width/2
        btnShowChart.layer.cornerRadius = btnShowChart.frame.size.width/2
        btnTop.layer.cornerRadius = btnTop.frame.size.width/2
        // Do any additional setup after loading the view.
        if selectedIPA != nil {
            if selectedIPA.youtubeVideoId != nil && !selectedIPA.youtubeVideoId.isEmpty {
                popupMode = .TIP_VIDEO
            } else {
                popupMode = .TIP_CHART
            }
            updateIPA()
        }
        var pImage = UIImage(named: "ic_play.png")!
        pImage = ImageHelper.imageWithImage(image: pImage, w: 40, h: 40)
        
        var sImage = UIImage(named: "ic_graph.png")!
        sImage = ImageHelper.imageWithImage(image: sImage, w: 30, h: 30)
        
        var vImage = UIImage(named: "ic_youtube.png")!
        vImage = ImageHelper.imageWithImage(image: vImage, w: 40, h: 40)
        
        var tImage = UIImage(named: "ic_lesson_tip")!
        tImage = ImageHelper.imageWithImage(image: tImage, w: 40, h: 40)
        
        switch popupMode {
        case .CHART_VIDEO:
            btnPlayExample.setImage(pImage, forState: UIControlState.Normal)
            btnShowChart.setImage(sImage, forState: UIControlState.Normal)
            btnTop.setImage(vImage, forState: UIControlState.Normal)
            break
        case .TIP_CHART:
            btnPlayExample.setImage(pImage, forState: UIControlState.Normal)
            btnShowChart.setImage(sImage, forState: UIControlState.Normal)
            btnTop.setImage(tImage, forState: UIControlState.Normal)
            break
        case .TIP_VIDEO:
            btnPlayExample.setImage(pImage, forState: UIControlState.Normal)
            btnShowChart.setImage(tImage, forState: UIControlState.Normal)
            btnTop.setImage(vImage, forState: UIControlState.Normal)
            break
        }
    }
    
    func setSelectedIPA(ipa: IPAMapArpabet, score: Int) {
        self.selectedIPA = ipa
        self.score = score
        updateIPA()
    }
    
    func updateIPA() {
        var cellColor:UIColor = ColorHelper.APP_LIGHT_GRAY
        if score >= 80 {
            cellColor = ColorHelper.APP_GREEN
        } else if score >= 45 {
            cellColor = ColorHelper.APP_ORANGE
        } else if score >= 0 {
            cellColor = ColorHelper.APP_RED
        }
        if bgView != nil {
            bgView.backgroundColor = cellColor
        }
        self.lblScore.text = score >= 0 ? "\(score)%" : ""
        self.lblIPA.text = selectedIPA.ipa
        self.btnPlayExample.tintColor = cellColor
        self.btnShowChart.tintColor = cellColor
        self.btnTop.tintColor = cellColor
        
        self.btnTop.hidden = (popupMode == .CHART_VIDEO) && (selectedIPA.youtubeVideoId == nil || selectedIPA.youtubeVideoId.isEmpty)
    }
    
    override func viewDidDisappear(animated: Bool) {
        super.viewDidDisappear(animated)
        isShow = false
        if popup != nil && popup.isShowing {
            self.dismissPopupViewController(SLpopupViewAnimationType.Fade)
        }
        if graphPopup != nil && graphPopup.isShow {
            self.dismissPopupViewController(SLpopupViewAnimationType.Fade)
        }
    }
    
    private func scaleTo(image image: UIImage, w: CGFloat, h: CGFloat) -> UIImage {
        let newSize = CGSize(width: w, height: h)
        UIGraphicsBeginImageContextWithOptions(newSize, false, 0.0)
        image.drawInRect(CGRect(x: 0, y: 0, width: newSize.width, height: newSize.height))
        let newImage: UIImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return newImage
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
    
    @IBAction func btnPlayExampleTouchUp(sender: AnyObject) {
        self.delegate?.pressPlayExample(self)
    }
    
    @IBAction func btnTopTouchUp(sender: AnyObject) {
        switch popupMode {
        case .CHART_VIDEO, .TIP_VIDEO:
            let youtubePlayer = YouTubePlayer()
            youtubePlayer.show(selectedIPA.youtubeVideoId)
            break
        case .TIP_CHART:
            // Show tip
            pressShowTip(self)
            break
        }
    }
    
    @IBAction func btnShowChartTouchUp(sender: AnyObject) {
        switch popupMode {
        case .CHART_VIDEO:
                self.delegate?.pressShowChart(self)
            break
        case .TIP_CHART:
            graphPopup = (delegate as! UIViewController).storyboard!.instantiateViewControllerWithIdentifier("GraphItemController") as! GraphPageItemController
            graphPopup.isWord = false
            //graphPopup.view.backgroundColor = UIColor.clearColor()
            graphPopup.data = selectedIPA.arpabet
            graphPopup.resizeFrame = true
            self.presentpopupViewController(graphPopup, animationType: SLpopupViewAnimationType.Fade, completion: { () -> Void in
                
            })
            break
        case .TIP_VIDEO:
                // Show tip
            pressShowTip(self)
            break
        }
    }
    
    var popup:IPAInfoPopup!

    func pressShowTip(sender: IPAPopupVC?) {
        delegate?.pressPlayExample(self)
        popup = IPAInfoPopup(nibName:"IPAInfoPopup", bundle: nil)
        popup.selectedIpa = selectedIPA
        popup.delegate = delegate
        popup.isShowing = true
        self.presentpopupViewController(popup, animationType: SLpopupViewAnimationType.Fade, completion: { () -> Void in
            
        })
    }
    
    func closeTipPopup() {
        weak var weakSelf = self
        if weakSelf != nil &&
            ((popup != nil && popup.isShowing) || (graphPopup != nil && graphPopup.isShow)) {
            weakSelf!.dismissPopupViewController(SLpopupViewAnimationType.Fade)
        }
    }
    @IBAction func clickOutside(sender: AnyObject) {
        weak var weakSelf = self
        if weakSelf != nil && popup != nil && popup.isShowing {
            weakSelf!.dismissPopupViewController(SLpopupViewAnimationType.Fade)
        } else {
            delegate?.pressClosePopup(self)
        }
    }
}
