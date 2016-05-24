//
//  LSPopupVC.swift
//  AccentEasy
//
//  Created by CMGVN on 3/15/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

@objc protocol LSPopupVCDelegate {
    func closePopup(sender: AnyObject)
    optional func updateLanguage(language:String)
    optional func updateProficiency(proficiency:String)
    optional func updateCountry(country:String)
    optional func updateBirthday(birthday:NSDate)
}

class LSPopupVC: UIViewController, UITableViewDataSource, UITableViewDelegate{

    var arrShow = ["hoang","nguyen","minh","hoang","nguyen","minh","hoang","nguyen","minh"]
    var delegate:LSPopupVCDelegate?
    var arrCountryData = [AECountry]()
    var adapter : WordCollectionDbApdater!
    var isSettingPage:Bool = false
    
    @IBOutlet weak var LSTableView: UITableView!
    @IBOutlet weak var btnClose: UIButton!
    @IBOutlet weak var viewBg: UIView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        adapter = WordCollectionDbApdater()
        // Do any additional setup after loading the view.
        LSTableView.dataSource = self
        LSTableView.delegate = self
        
        //style popup
        self.view.layer.cornerRadius = 5
        self.view.backgroundColor = ColorHelper.APP_LIGHT_BLUE
        //self.view.backgroundColor = UIColor(patternImage: UIImage(named: "global_bg.png")!)
        LSTableView.backgroundColor = ColorHelper.APP_LIGHT_BLUE
        var bImage = UIImage(named: "global_bg.png")
        bImage = ImageHelper.imageWithImage(image: bImage!, w: 90, h: 90)
        viewBg.backgroundColor = UIColor(patternImage: bImage!)
        
        var pImage = UIImage(named: "ic_close_dialog.png")!
        pImage = ImageHelper.imageWithImage(image: pImage, w: 25, h: 25)
        btnClose.layer.cornerRadius = btnClose.frame.size.width/2
        btnClose.setImage(pImage, forState: UIControlState.Normal)
        btnClose.tintColor = ColorHelper.APP_PURPLE
        
        //load data for language selection
        
        do {
            arrCountryData = try adapter.getAllCountries()
            Logger.log(arrCountryData)
        }catch (let e as NSError){
            Logger.log(e)
        }
        //Logger.log(arrCountryData[0].idString)
        
        let cellNib = UINib(nibName: "LSPopupCell", bundle: nil)
        LSTableView.registerNib(cellNib, forCellReuseIdentifier: "cell")

        
    }
    
    func validateCountry() {
        if let user:UserProfile = AccountManager.currentUser() {
            if user.selectedCountry == nil {
                do {
                    user.selectedCountry = try adapter.getDefaultCountry()
                    
                } catch {
                    
                }
                AccountManager.updateProfile(user)
            }
        }
        NSNotificationCenter.defaultCenter().postNotificationName("loadLevel", object: nil)
    }
    
    override func viewDidDisappear(animated: Bool) {
        super.viewDidDisappear(animated)
        // Update default selected country
        Logger.log("check country")
        validateCountry()
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
    
    // MARK: - Table view data source
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return arrCountryData.count
    }
    
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        
        let identifier = "cell"
        var cell: LSPopupCell! = tableView.dequeueReusableCellWithIdentifier(identifier) as! LSPopupCell
        if cell == nil{
            cell = LSPopupCell(style: .Default, reuseIdentifier: identifier)
        }
        cell?.cellCountryName.text = arrCountryData[indexPath.row].name
        cell.backgroundColor = UIColor.clearColor()
        
        let url = NSURL(string: arrCountryData[indexPath.row].imageURL)
        
        //var flagImage : UIImage = UIImage(named: "global_bg.png")!
        //flagImage = ImageHelper.imageWithImage(image: flagImage, w: 30, h: 30)
        //cell.cellFlag!.image = flagImage
        
        
        //var cellImg : UIImageView = UIImageView(frame: CGRectMake(0, 0, 30, 30))
        //cellImg.load(url)
        cell.cellFlag?.load(url!)
       //cell.cellFlag?.translatesAutoresizingMaskIntoConstraints = false
        //cell.cellFlag.autoresizingMask = UIViewAutoresizing.None
        //cell.cellFlag.clipsToBounds = true
        //cell.cellCountryName.translatesAutoresizingMaskIntoConstraints = false
        //cell.addSubview(cellImg)
        
        
        return cell
        
    }
    
    /*func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        
        let identifier = "cellidentifier"
        var cell: UITableViewCell! = tableView.dequeueReusableCellWithIdentifier(identifier)
        if cell == nil{
            cell = UITableViewCell(style: .Subtitle, reuseIdentifier: identifier)
        }
        
        
        cell.frame = CGRectMake(0, 0, 130, 30)
        cell?.textLabel!.text = arrCountryData[indexPath.row].name
        cell.backgroundColor = UIColor.clearColor()
        
        let url = NSURL(string: arrCountryData[indexPath.row].imageURL)
    
        
        var flagImage : UIImage = UIImage(named: "global_bg.png")!
        //flagImage = ImageHelper.imageWithImage(image: flagImage, w: 30, h: 30)
        cell.imageView?.image = flagImage
        
        
        //var cellImg : UIImageView = UIImageView(frame: CGRectMake(0, 0, 30, 30))
        //cellImg.load(url)

        
        cell.imageView?.load(url)
        //cell.imageView!.frame = CGRectMake(0,0,30,30);

        return cell

        
    }*/

    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        //Logger.log(indexPath.row)
        //let indexPath = tableView.indexPathForSelectedRow!
        //let currentCell = tableView.cellForRowAtIndexPath(indexPath)! as UITableViewCell
        //Logger.log(currentCell.textLabel!.text)
        
        //save language selection
        let userProfile:UserProfile = AccountManager.currentUser()
        userProfile.selectedCountry = arrCountryData[indexPath.row]
        AccountManager.updateProfile(userProfile)
        delegate?.closePopup(self)
        if isSettingPage {
            delegate?.updateLanguage!(userProfile.selectedCountry.name)
        }
    }

    @IBAction func btnCloseTouchUp(sender: AnyObject) {
        delegate?.closePopup(self)
    }

}
