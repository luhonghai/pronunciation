//
//  IPAInfoPopup.swift
//  AccentEasy
//
//  Created by Hai Lu on 16/03/2016.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit



class IPAInfoPopup: UIViewController {

    @IBOutlet weak var txtDescription: UITextView!
    @IBOutlet weak var imgTip: UIImageView!
    @IBOutlet weak var lblTitle: UILabel!
    @IBOutlet weak var lblSubTitle: UILabel!
    
    var delegate: IPAPopupViewControllerDelegate?
    
    @IBOutlet weak var btnClose: UIButton!
    @IBOutlet weak var btnPlay: UIButton!
    
    @IBAction func clickClose(sender: AnyObject) {
        delegate!.pressShowChart(nil)
    }
    @IBAction func doPlayFile(sender: AnyObject) {
        delegate!.pressPlayExample(nil)
    }
    
    var selectedIpa: IPAMapArpabet!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.layer.cornerRadius = 5
        view.backgroundColor = ColorHelper.APP_LIGHT_BLUE
        
        var pImage = UIImage(named: "ic_close_dialog.png")!
        pImage = ImageHelper.imageWithImage(pImage, scaledToSize: CGSize(width: 25, height: 25)).imageWithRenderingMode(UIImageRenderingMode.AlwaysTemplate)
        btnClose.layer.cornerRadius = btnClose.frame.size.width/2
        btnClose.setImage(pImage, forState: UIControlState.Normal)
        btnClose.tintColor = ColorHelper.APP_PURPLE
        btnPlay.layer.cornerRadius = btnPlay.frame.width / 2

        lblTitle.text = "\(selectedIpa.ipa)"
        lblSubTitle.text = "<\(selectedIpa.arpabet)> \(selectedIpa.words)"
        txtDescription.text = selectedIpa.tip
        txtDescription.textColor = ColorHelper.APP_PURPLE
        imgTip.clipsToBounds = true
//        imgTip.tintColor = ColorHelper.APP_PURPLE
        if selectedIpa!.imgTongue != nil && !selectedIpa!.imgTongue.isEmpty {
            weak var weakSelf = self
            let url = NSURL(string: selectedIpa!.imgTongue)
            imgTip.load(url!, placeholderImage: UIImage(named: "p_tip_blue.png")?.imageWithRenderingMode(UIImageRenderingMode.AlwaysTemplate), completion: { (status) -> Void in
                if let image = weakSelf!.imgTip.image {
                    weakSelf!.imgTip.image = image.imageWithRenderingMode(UIImageRenderingMode.AlwaysTemplate)
                    weakSelf!.imgTip.tintColor = ColorHelper.APP_PURPLE
                }
            })
        }
    }
    
    override func viewDidAppear(animated: Bool) {
       
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
