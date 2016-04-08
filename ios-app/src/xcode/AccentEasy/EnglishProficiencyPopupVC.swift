//
//  EnglishProficiencyPopupVC.swift
//  AccentEasy
//
//  Created by CMGVN on 4/6/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit


class EnglishProficiencyPopupVC: UIViewController, UITableViewDataSource, UITableViewDelegate {

    var arrShow = ["null value"]
    var arrValue = ["null value"]
    var selectedIndex:NSIndexPath?
    var delegate:LSPopupVCDelegate?
    var titlePopup:String?
    
    @IBOutlet weak var tableEnglishProficiency: UITableView!
    @IBOutlet weak var lblTitle: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        //set delegate for table
        tableEnglishProficiency.dataSource = self
        tableEnglishProficiency.delegate = self
        lblTitle.text = titlePopup
        
        let cellNib = UINib(nibName: "SettingPopupCell", bundle: nil)
        tableEnglishProficiency.registerNib(cellNib, forCellReuseIdentifier: "cell")
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
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }

    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return arrShow.count
    }
    
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        
        // let cell = tableView.dequeueReusableCellWithIdentifier("reuseIdentifier", forIndexPath: indexPath) as! UITableViewCell
        
        let cell:SettingPopupCell = tableView.dequeueReusableCellWithIdentifier("cell", forIndexPath: indexPath) as! SettingPopupCell
        /*let identifier = "cell"
        var cell: SettingPopupCell! = tableView.dequeueReusableCellWithIdentifier(identifier) as! SettingPopupCell
        if cell == nil{
            cell = SettingPopupCell(style: .Default, reuseIdentifier: identifier)
        }*/

        
        cell.lblTitle.text=arrShow[indexPath.row]
        
        cell.selectionStyle = UITableViewCellSelectionStyle.None;
        //cell.btnRadio.backgroundColor = UIColor.blackColor()
        
        if (selectedIndex == indexPath) {
            //cell.btnRadio.backgroundColor = UIColor.redColor()
            //cell.imgvRadio.setImage(UIImage(named: "Radio_Button-Checked-128"),forState:UIControlState.Normal)
            cell.imgvRadio.image = UIImage(named: "Radio_Button-Checked-128")
        } else {
            //cell.btnRadio.backgroundColor = UIColor.blueColor()
            //cell.viewRadio.setImage(UIImage(named: "Radio_Button-128"),forState:UIControlState.Normal)
            cell.imgvRadio.image = UIImage(named: "Radio_Button-128")
        }
        
        
        // Configure the cell...
        
        return cell
        
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        Logger.log(indexPath.row)
        
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
        let row = indexPath.row
        selectedIndex = indexPath
        tableView.reloadData()
        
        delegate?.closePopup(self)
        if titlePopup!.containsString("proficiency")  {
            delegate?.updateProficiency!(arrShow[row])
        }else if titlePopup!.containsString("country"){
            delegate?.updateCountry!(arrShow[row])
        }
        
    }

    @IBAction func btnCancelTouchUp(sender: AnyObject) {
        delegate?.closePopup(self)
    }
}
