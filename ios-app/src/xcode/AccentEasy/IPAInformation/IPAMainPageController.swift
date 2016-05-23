//
//  IPAMainPageController.swift
//  AccentEasy
//
//  Created by Hai Lu on 16/03/2016.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit
import SloppySwiper

class IPAMainPageController: BaseUIViewController {

    @IBOutlet weak var btnConsonant: UIButton!
    @IBOutlet weak var btnRP: UIButton!
    @IBOutlet weak var btnVowel: UIButton!
    
    @IBOutlet weak var menuButton: UIBarButtonItem!
    
    var swiper: SloppySwiper!
    
    @IBAction func clickRP(sender: AnyObject) {
        Logger.log("clickRP")
        let ipaChart = self.storyboard?.instantiateViewControllerWithIdentifier("IPARPController") as! IPARPController
        self.navigationController?.pushViewController(ipaChart, animated: true)

    }
    
    @IBAction func clickVowel(sender: AnyObject) {
        Logger.log("clickVowel")
        let ipaChart = self.storyboard?.instantiateViewControllerWithIdentifier("IPAChartController") as! IPAChartController
        ipaChart.selectedType = IPAMapArpabet.VOWEL
        self.navigationController?.pushViewController(ipaChart, animated: true)
    }
    @IBAction func clickConsonant(sender: AnyObject) {
        Logger.log("clickConsonant")
        let ipaChart = self.storyboard?.instantiateViewControllerWithIdentifier("IPAChartController") as! IPAChartController
        ipaChart.selectedType = IPAMapArpabet.CONSONANT
        self.navigationController?.pushViewController(ipaChart, animated: true)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        swiper = SloppySwiper(navigationController: self.navigationController)
        self.navigationController?.delegate = swiper
        // Do any additional setup after loading the view.
        btnRP.layer.cornerRadius = 5
        btnVowel.layer.cornerRadius = 5
        btnConsonant.layer.cornerRadius = 5
        if self.revealViewController() != nil {
            menuButton.target = self.revealViewController()
            menuButton.action = "revealToggle:"
            self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
        }
        
        setNavigationBarTransparent()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func setNavigationBarTransparent() {
        self.navigationController?.navigationBar.setBackgroundImage(UIImage(), forBarMetrics: UIBarMetrics.Default)
        self.navigationController?.navigationBar.shadowImage = UIImage()
        self.navigationController?.navigationBar.translucent = true
        self.navigationController?.view.backgroundColor = UIColor.clearColor()
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
