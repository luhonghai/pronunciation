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
    
    @IBOutlet weak var lblScore: UILabel!
    @IBOutlet weak var lblIPA: UILabel!
    @IBOutlet weak var btnPlayExample: UIButton!
    @IBOutlet weak var btnShowChart: UIButton!
    @IBOutlet weak var viewShowChart: UIView!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        btnPlayExample.layer.cornerRadius = btnPlayExample.frame.size.width/2
        viewShowChart.layer.cornerRadius = viewShowChart.frame.size.width/2
        // Do any additional setup after loading the view.
        
        var pImage = UIImage(named: "ic_play.png")!
        pImage = ImageHelper.imageWithImage(image: pImage, w: 40, h: 40)
        btnPlayExample.setImage(pImage, forState: UIControlState.Normal)
        
        var sImage = UIImage(named: "ic_graph.png")!
        sImage = ImageHelper.imageWithImage(image: sImage, w: 30, h: 30)
        btnShowChart.setImage(sImage, forState: UIControlState.Normal)

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
