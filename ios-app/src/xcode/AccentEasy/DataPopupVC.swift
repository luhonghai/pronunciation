//
//  DataPopupVC.swift
//  AccentEasy
//
//  Created by CMGVN on 4/7/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class DataPopupVC: UIViewController {
    
    let dateFormatter = NSDateFormatter()
    var dateSelected:NSDate!
    var delegate:LSPopupVCDelegate?

    @IBOutlet weak var datePicker: UIDatePicker!
    @IBOutlet weak var lblTitle: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
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
    
    @IBAction func datePickerChanged(sender: AnyObject) {
        setDateAndTime()
    }
    
    func setDateAndTime() {
        dateFormatter.dateStyle = NSDateFormatterStyle.ShortStyle
        //lblTitle.text = dateFormatter.stringFromDate(datePicker.date)
        dateSelected = datePicker.date
    }

    @IBAction func closePopup(sender: AnyObject) {
        delegate?.closePopup(self)
    }
    
    @IBAction func updateDate(sender: AnyObject) {
        if dateSelected != nil {
            delegate?.updateBirthday!(dateSelected)
        }
        delegate?.closePopup(self)
    }
    

}
