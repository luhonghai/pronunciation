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
}

class IPAPopupVC: UIViewController {

    var delegate:IPAPopupViewControllerDelegate?
    var isShow:Bool = false
    
    @IBOutlet weak var lblScore: UILabel!
    @IBOutlet weak var lblIPA: UILabel!
    @IBOutlet weak var btnPlayExample: UIButton!
    @IBOutlet weak var btnShowChart: UIButton!
    @IBOutlet weak var btnTop: UIButton!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        btnPlayExample.layer.cornerRadius = btnPlayExample.frame.size.width/2
        btnShowChart.layer.cornerRadius = btnShowChart.frame.size.width/2
        btnTop.layer.cornerRadius = btnTop.frame.size.width/2
        // Do any additional setup after loading the view.
        
        var pImage = UIImage(named: "ic_play.png")!
        pImage = ImageHelper.imageWithImage(image: pImage, w: 40, h: 40)
        btnPlayExample.setImage(pImage, forState: UIControlState.Normal)
        
        var sImage = UIImage(named: "ic_graph.png")!
        sImage = ImageHelper.imageWithImage(image: sImage, w: 30, h: 30)
        btnShowChart.setImage(sImage, forState: UIControlState.Normal)
    }
    
    override func viewDidDisappear(animated: Bool) {
        isShow = false
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
    
    
    @IBAction func btnShowChartTouchUp(sender: AnyObject) {
        self.delegate?.pressShowChart(self)
    }

    

}
