//
//  MenuTVC.swift
//  AccentEasy
//
//  Created by CMGVN on 1/27/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import UIKit
import ObjectMapper
import FBSDKCoreKit
import FBSDKLoginKit

class MenuTVC: UITableViewController {

    var userProfileSaveInApp:NSUserDefaults!
    var JSONStringUserProfile:String!
    var userProfile = UserProfile()
    //@IBOutlet weak var userAvata: UIImageView!
    //@IBOutlet weak var imgUserAvata: UIImageView!
    @IBOutlet weak var imgUserAvata: UIImageView!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var lblEmail: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
        
        //load User profile
        userProfileSaveInApp = NSUserDefaults()
        let keyForUserProfile:String = userProfileSaveInApp.objectForKey(Login.KeyUserProfile) as! String
        JSONStringUserProfile = userProfileSaveInApp.objectForKey(keyForUserProfile) as! String
        userProfile = Mapper<UserProfile>().map(JSONStringUserProfile)!
        lblName.text = userProfile.name
        lblEmail.text = userProfile.username
        
        //imgUserAvata.
        imgUserAvata.layer.cornerRadius = imgUserAvata.frame.size.width/2
        imgUserAvata.clipsToBounds = true
        
        if (userProfile.profileImage != nil && userProfile.profileImage != "") {
            let url = NSURL(string: userProfile.profileImage + "&width=320&height=320")
            let data = NSData(contentsOfURL: url!) //make sure your image in this url does exist, otherwise unwrap in a if let check
            imgUserAvata.image = UIImage(data: data!)
        }
        
        
        /*if let filePath = NSBundle.mainBundle().pathForResource("imageName", ofType: "jpg"), image = UIImage(contentsOfFile: filePath) {
            
            imgUserAvata.contentMode = .ScaleAspectFit
            imgUserAvata.image = image
        }*/
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    /*override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        //return 0
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return 0
    }*/

    /*
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("reuseIdentifier", forIndexPath: indexPath)

        // Configure the cell...

        return cell
    }
    */

    /*
    // Override to support conditional editing of the table view.
    override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
        if editingStyle == .Delete {
            // Delete the row from the data source
            tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: .Fade)
        } else if editingStyle == .Insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(tableView: UITableView, moveRowAtIndexPath fromIndexPath: NSIndexPath, toIndexPath: NSIndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(tableView: UITableView, canMoveRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    /*override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        
        //get select rown in table view
        //let selectedIndex = self.tableView.indexPathForCell(sender as! UITableViewCell)
        //var chon = selectedIndex?.row
        //print(chon)
    }*/
    
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        //CODE TO BE RUN ON CELL TOUCH
        //print(indexPath.row)
        let rowMenu:Int = indexPath.row
        switch rowMenu{
            case 1:
                print(rowMenu)
            case 2:
                print(rowMenu)
            case 3:
                print(rowMenu)
            case 4:
                print(rowMenu)
            case 5:
                print(rowMenu)
            case 6:
                print(rowMenu)
            case 7:
                print(rowMenu)
            case 8:
                print(rowMenu)
                //logout  row
                if userProfile.loginType == UserProfile.TYPE_GOOGLE_PLUS {
                    GIDSignIn.sharedInstance().signOut()
                } else if userProfile.loginType == UserProfile.TYPE_FACEBOOK {
                    //Remove FB Data
                    let fbManager = FBSDKLoginManager()
                    fbManager.logOut()
                    FBSDKAccessToken.setCurrentAccessToken(nil)
                } else {
                }
                
                dispatch_async(dispatch_get_main_queue(),{
                    self.performSegueWithIdentifier("MenuGoToLogin", sender: self)
                })
            default:
                print("case default")
        }
        
    }
    

}
