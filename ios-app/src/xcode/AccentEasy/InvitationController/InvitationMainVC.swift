//
//  InvitationMainVC.swift
//  AccentEasy
//
//  Created by CMGVN on 4/26/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class InvitationTableViewCell: UITableViewCell {
    
    @IBOutlet weak var lblTitle: UILabel!
    
    @IBOutlet weak var imgvIcon: UIImageView!
    
    @IBOutlet weak var btnIcon: UIButton!
    
    @IBOutlet weak var bg: UIView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
    }
    
    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
}

class InvitationMainVC: UIViewController, UITableViewDataSource, UITableViewDelegate, UIGestureRecognizerDelegate, InvitationPopupDelegate{
    
    var arrShow = ["hoang","nguyen","minh","hoang","nguyen","minh","hoang","nguyen","minh"]
    //var fakeData = "{\"invitationData\":[{\"name\":\"hoang.nguyen1\",\"status\":\"accept\"},{\"name\":\"hoang.nguyen2\",\"status\":\"accept\"},{\"name\":\"hoang.nguyen3\",\"status\":\"delete\"},{\"name\":\"hoang.nguyen4\",\"status\":\"delete\"},{\"name\":\"hoang.nguyen5\",\"status\":\"delete\"},{\"name\":\"hoang.nguyen6\",\"status\":\"delete\"},{\"name\":\"hoang.nguyen7\",\"status\":\"accept\"},{\"name\":\"hoang.nguyen8\",\"status\":\"accept\"},{\"name\":\"hoang.nguyen9\",\"status\":\"pending\"},{\"name\":\"hoang.nguyen10\",\"status\":\"pending\"},{\"name\":\"hoang.nguyen11\",\"status\":\"pending\"},{\"name\":\"hoang.nguyen12\",\"status\":\"accept\"},{\"name\":\"hoang.nguyen13\",\"status\":\"delete\"},{\"name\":\"hoang.nguyen14\",\"status\":\"accept\"},{\"name\":\"hoang.nguyen15\",\"status\":\"pending\"}]}"
    var userProfile:UserProfile =  UserProfile()
    var countInvitation:Int=0

    @IBOutlet weak var menuButton: UIBarButtonItem!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var btnTeacher: UIButton!
    @IBOutlet weak var btnStudent: UIButton!
    @IBOutlet weak var btnChallenge: UIButton!
    @IBOutlet weak var btnHelp: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        //menu
        if self.revealViewController() != nil {
            menuButton.target = self.revealViewController()
            menuButton.action = "revealToggle:"
            self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
        }
        
        //table invitation
        tableView.delegate = self
        tableView.dataSource = self
        
        userProfile = AccountManager.currentUser()
        
        //userProfile = Mapper<UserProfile>().map(fakeData)!
        
        print(userProfile.invitationData)
        
        /*var data:InvitationData = InvitationData()
        for index in 0...10 {
            data.name = "hoang.nguyen \(index)"
            data.status = "accept"
            userProfile.invitationData.append(data)
        }*/
        
        //caculate count invitation and sort array
        sortTable()
        
        //show popup overview or popup new invitation
        if countInvitation == 0 {
            //show overview popup
            let inviPopupOverviewVC:InviPopupOverviewVC = InviPopupOverviewVC(nibName: "InviPopupOverviewVC", bundle: nil)
            inviPopupOverviewVC.delegate = self
            self.presentpopupViewController(inviPopupOverviewVC, animationType: .Fade, completion: {() -> Void in })
        }else {
            //show new invitation popup
            let newInvitationPopupVC:NewInvitationPopupVC = NewInvitationPopupVC(nibName: "NewInvitationPopupVC", bundle: nil)
            newInvitationPopupVC.delegate = self
            newInvitationPopupVC.numerInvitation = countInvitation
            self.presentpopupViewController(newInvitationPopupVC, animationType: .Fade, completion: {() -> Void in })
        }
        
        //menu style
        navigationItem.title = "invitations"
        setNavigationBarTransparent()
    }
    
    func setNavigationBarTransparent() {
        self.navigationController?.navigationBar.setBackgroundImage(UIImage(), forBarMetrics: UIBarMetrics.Default)
        self.navigationController?.navigationBar.shadowImage = UIImage()
        self.navigationController?.navigationBar.translucent = true
        self.navigationController?.view.backgroundColor = UIColor.clearColor()
    }
    
    func sortTable(){
        //caculate count invitation and sort array
        for invitation in userProfile.invitationData {
            if invitation.status == InvitationStatus.pending {
                countInvitation += 1
                invitation.groupId = 1
            } else if invitation.status == InvitationStatus.reject {
                invitation.groupId = 2
            } else {
                invitation.groupId = 3
            }
        }
        userProfile.invitationData.sortInPlace({ $0.groupId < $1.groupId })
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
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        Logger.log("Invitation table row \(userProfile.invitationData.count)")
        return userProfile.invitationData.count
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        //let obj = objectives[indexPath.row]
        let identifier = "InvitationCell"
        var cell: InvitationTableViewCell! = tableView.dequeueReusableCellWithIdentifier(identifier) as! InvitationTableViewCell
        
        var invitationCellData = userProfile.invitationData[indexPath.row]
        //cell.lblScore.layer.cornerRadius = cell.lblScore.frame.width / 2
        //cell.lblScore.layer.masksToBounds = true
        //cell.bg.layer.cornerRadius = 5
        //let tapGusture = UITapGestureRecognizer(target: self, action: Selector("tapItem:"))
        //tapGusture.numberOfTapsRequired = 1
        //cell.bg.tag = indexPath.row
        //cell.bg.addGestureRecognizer(tapGusture)
        
        cell.lblTitle.text = invitationCellData.firstTeacherName + " " + invitationCellData.lastTeacherName + ", " + invitationCellData.companyName
        
        var bImage:UIImage!
        if invitationCellData.status == InvitationStatus.accept {
            bImage = UIImage(named: "tick.png")
            bImage = ImageHelper.imageWithImage(image: bImage!, w: 30, h: 30)
            cell.btnIcon.setImage(bImage, forState: UIControlState.Normal)
            
        } else if invitationCellData.status == InvitationStatus.pending {
            bImage = UIImage(named: "invite button.png")
            bImage = ImageHelper.imageWithImage(image: bImage!, w: 30, h: 30)
            cell.btnIcon.setImage(bImage, forState: UIControlState.Normal)
            
        } else {
            bImage = UIImage(named: "x grey.png")
            bImage = ImageHelper.imageWithImage(image: bImage!, w: 30, h: 30)
            cell.btnIcon.setImage(bImage, forState: UIControlState.Normal)
        }
        
        //add touch button icon
        cell.btnIcon.tag = indexPath.row
        cell.btnIcon.addTarget(self, action: "touchCellIcon:", forControlEvents: .TouchUpInside)
        
        //add swipe left

        print(cell.contentView.tag)
        if cell.contentView.tag == -1 {
            print("add swipeGesture")
            let swipeGesture = UISwipeGestureRecognizer(target: self, action: Selector("swipeCell:"))
            swipeGesture.direction = UISwipeGestureRecognizerDirection.Left
            swipeGesture.delegate = self
            cell.contentView.addGestureRecognizer(swipeGesture)

        }
        cell.contentView.tag = indexPath.row
        
        return cell
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        Logger.log(indexPath.row)
    }

    /*@IBAction func logAction(sender: UIButton) {
        print(sender.tag)
    }*/
    
    func touchCellIcon(sender: UIButton) {
        print(sender.tag)
        let indexCell = sender.tag
        var invitationCellData = userProfile.invitationData[indexCell]
        
        if invitationCellData.status == InvitationStatus.accept {
            //show popup when click icon accept
            let inviAcceptPopupVC:InviAcceptPopupVC = InviAcceptPopupVC(nibName: "InviAcceptPopupVC", bundle: nil)
            inviAcceptPopupVC.delegate = self
            inviAcceptPopupVC.indexSelected = indexCell
            self.presentpopupViewController(inviAcceptPopupVC, animationType: .Fade, completion: {() -> Void in })
            
        } else if invitationCellData.status == InvitationStatus.pending {
            //show popup when click icon invitation
            let invitationPopupVC:InvitationPopupVC = InvitationPopupVC(nibName: "InvitationPopupVC", bundle: nil)
            invitationPopupVC.delegate = self
            invitationPopupVC.firstTeacherName = invitationCellData.firstTeacherName
            invitationPopupVC.lastTeacherName = invitationCellData.lastTeacherName
            invitationPopupVC.companyName = invitationCellData.companyName
            invitationPopupVC.indexSelected = indexCell
            self.presentpopupViewController(invitationPopupVC, animationType: .Fade, completion: {() -> Void in })
            
        } else {

        }

    }
    
    func swipeCell(sender: UISwipeGestureRecognizer) {
        let row: Int = sender.view!.tag
        Logger.log("swipe row id \(row)")
        
        var invitationCellData = userProfile.invitationData[row]
        
        if invitationCellData.status == InvitationStatus.accept {
            
            
        } else if invitationCellData.status == InvitationStatus.pending {
            
            
        } else {
            //remove
            updateDeleteData(row)
        }


    }
    
    func gestureRecognizer(gestureRecognizer: UIGestureRecognizer, shouldRecognizeSimultaneouslyWithGestureRecognizer otherGestureRecognizer: UIGestureRecognizer) -> Bool {
        return true
    }
    
    
    
    @IBAction func btnHelpTouchUp(sender: AnyObject) {
        let inviHelpPopupVC:InviHelpPopupVC = InviHelpPopupVC(nibName: "InviHelpPopupVC", bundle: nil)
        inviHelpPopupVC.delegate = self
        self.presentpopupViewController(inviHelpPopupVC, animationType: .Fade, completion: {() -> Void in })
    }
    
    func closePopup(sender: AnyObject) {
        self.dismissPopupViewController(.Fade)
    }
    
    func showLoadding(){
        //show watting..
        let text = "Please wait..."
        self.showWaitOverlayWithText(text)
    }
    
    func hidenLoadding(){
        // Remove watting
        self.removeAllOverlays()
    }
    
    //touch ok in InviAcceptPopupVC
    func inviAcceptPopupVCTouchOK(sender: AnyObject){
        Logger.log(sender)
        let index = sender as! Int
        let id = userProfile.invitationData[index].id
        weak var weakSelf = self
        AccountManager.updateRejectData(AccountManager.currentUser(), id: id) { (userProfile, success, message) in
            dispatch_async(dispatch_get_main_queue(), {
                weakSelf!.dismissPopupViewController(.Fade)
                if success {
                    userProfile.invitationData[index].status = InvitationStatus.reject
                    weakSelf!.sortTable()
                    weakSelf!.tableView.reloadData()
                } else {
                    AccountManager.showError("could not update", message: message)
                }

            })
            
        }
    }
    
    //touch ok in InvitationPopupVC
    func invitationPopupVCTouchOK(sender: AnyObject){
        Logger.log(sender)
        let index = sender as! Int
        let id = userProfile.invitationData[index].id
        weak var weakSelf = self
        AccountManager.updateAcceptData(AccountManager.currentUser(), id: id) { (userProfile, success, message) in
            dispatch_async(dispatch_get_main_queue(), {
                weakSelf!.dismissPopupViewController(.Fade)
                if success {
                    userProfile.invitationData[index].status = InvitationStatus.accept
                    weakSelf!.sortTable()
                    weakSelf!.tableView.reloadData()
                } else {
                    AccountManager.showError("could not update", message: message)
                }
                
            })
            
        }
    }
    
    //swipe left remove
    func updateDeleteData(index: Int){
        Logger.log(index)
        let id = userProfile.invitationData[index].id
        weak var weakSelf = self
        weakSelf!.showLoadding()
        DeviceManager.showLockScreen()
        AccountManager.updateDeleteData(AccountManager.currentUser(), id: id) { (userProfile, success, message) in
            dispatch_async(dispatch_get_main_queue(), {
                DeviceManager.hideLockScreen()
                weakSelf!.hidenLoadding()
                if success {
                    //userProfile.invitationData[index].status = InvitationStatus.accept
                    userProfile.invitationData.removeAtIndex(index)
                    weakSelf!.sortTable()
                    weakSelf!.tableView.reloadData()
                } else {
                    AccountManager.showError("could not update", message: message)
                }
                
            })
            
        }
    }
}
