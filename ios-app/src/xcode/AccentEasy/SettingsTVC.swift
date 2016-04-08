//
//  SettingsTVC.swift
//  AccentEasy
//
//  Created by CMGVN on 4/6/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class SettingsTVC: UITableViewController, LSPopupVCDelegate {

    var userProfile:UserProfile!
    
    @IBOutlet weak var menuButton: UIBarButtonItem!
    @IBOutlet weak var lblLanguage: UILabel!
    @IBOutlet weak var lblProficiency: UILabel!
    @IBOutlet weak var lblCountry: UILabel!
    @IBOutlet weak var lblBirthday: UILabel!
    @IBOutlet weak var imgvMale: UIImageView!
    @IBOutlet weak var imgvFemale: UIImageView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
        
        if self.revealViewController() != nil {
            menuButton.target = self.revealViewController()
            menuButton.action = "revealToggle:"
            self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
        }
        userProfile = AccountManager.currentUser()
        loadData()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func loadData(){
        if userProfile.selectedCountry != nil {
            lblLanguage.text = userProfile.selectedCountry.name
        }
        lblProficiency.text = arrProficiency[userProfile.englishProficiency]
        print("--------")
        print(userProfile.country)
        print(arrCountry[arrCountryValue.indexOf(userProfile.country)!])
        lblCountry.text = arrCountry[arrCountryValue.indexOf(userProfile.country)!]
        //lblCountry.text = userProfile.country
        lblBirthday.text = userProfile.dob
        gender = userProfile.gender
        drawGender(gender)
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        AccountManager.updateProfile(userProfile) { (userProfile, success, message) in
            dispatch_async(dispatch_get_main_queue(),{
                if success {
                    AccountManager.updateProfile(userProfile)
                } else {
                    AccountManager.showError("could not fetch user data")
                }
            })
        }
    }

    // MARK: - Table view data source

    /*
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 0
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return 0
    }
    */

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

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    
    let arrProficiency = ["Level 1/10", "Level 2/10", "Level 3/10", "Level 4/10", "Level 5/10", "Level 6/10", "Level 7/10", "Level 8/10", "Level 9/10", "Level 10/10"]
    let arrCountry = ["Andorra", "United Arab Emirates", "Afghanistan", "Antigua and Barbuda", "Anguilla", "Albania", "Armenia", "Angola", "Antarctica", "Argentina", "American Samoa", "Austria", "Australia", "Aruba", "Åland Islands", "Azerbaijan", "Bosnia and Herzegovina", "Barbados", "Bangladesh", "Belgium", "Burkina Faso", "Bulgaria", "Bahrain", "Burundi", "Benin", "Saint Barthélemy", "Bermuda", "Brunei Darussalam", "Bolivia, Plurinational State of", "Bonaire, Sint Eustatius and Saba", "Brazil", "Bahamas", "Bhutan", "Bouvet Island", "Botswana", "Belarus", "Belize", "Canada", "Cocos (Keeling) Islands", "Congo, the Democratic Republic of the", "Central African Republic", "Congo", "Switzerland", "Côte d\'Ivoire", "Cook Islands", "Chile", "Cameroon", "China", "Colombia", "Costa Rica", "Cuba", "Cabo Verde", "Curaçao", "Christmas Island", "Cyprus", "Czech Republic", "Germany", "Djibouti", "Denmark", "Dominica", "Dominican Republic", "Algeria", "Ecuador", "Estonia", "Egypt", "Western Sahara", "Eritrea", "Spain", "Ethiopia", "Finland", "Fiji", "Falkland Islands (Malvinas)", "Micronesia, Federated States of", "Faroe Islands", "France", "Gabon", "United Kingdom", "Grenada", "Georgia", "French Guiana", "Guernsey", "Ghana", "Gibraltar", "Greenland", "Gambia", "Guinea", "Guadeloupe", "Equatorial Guinea", "Greece", "South Georgia and the South Sandwich Islands", "Guatemala", "Guam", "Guinea-Bissau", "Guyana", "Hong Kong", "Heard Island and McDonald Islands", "Honduras", "Croatia", "Haiti", "Hungary", "Indonesia", "Ireland", "Israel", "Isle of Man", "India", "British Indian Ocean Territory", "Iraq", "Iran, Islamic Republic ofIran)", "Iceland", "Italy", "Jersey", "Jamaica", "Jordan", "Japan", "Kenya", "Kyrgyzstan", "Cambodia", "Kiribati", "Comoros", "Saint Kitts and Nevis", "Korea, Democratic People\'s Republic of", "Korea, Republic of", "Kuwait", "Cayman Islands", "Kazakhstan", "Lao People\'s Democratic Republic", "Lebanon", "Saint Lucia", "Liechtenstein", "Sri Lanka", "Liberia", "Lesotho", "Lithuania", "Luxembourg", "Latvia", "Libya", "Morocco", "Monaco", "Moldova, Republic of", "Montenegro", "Saint Martin (French part)", "Madagascar", "Marshall Islands", "Macedonia, the former Yugoslav Republic of", "Mali", "Myanmar", "Mongolia", "Macao", "Northern Mariana Islands", "Martinique", "Mauritania", "Montserrat", "Malta", "Mauritius", "Maldives", "Malawi", "Mexico", "Malaysia", "Mozambique", "Namibia", "New Caledonia", "Niger", "Norfolk Island", "Nigeria", "Nicaragua", "Netherlands", "Norway", "Nepal", "Nauru", "Niue", "New Zealand", "Oman", "Panama", "Peru", "French Polynesia", "Papua New Guinea", "Philippines", "Pakistan", "Poland", "Saint Pierre and Miquelon", "Pitcairn", "Puerto Rico", "Palestine, State of", "Portugal", "Palau", "Paraguay", "Qatar", "Réunion", "Romania", "Serbia", "Russian Federation", "Rwanda", "Saudi Arabia", "Solomon Islands", "Seychelles", "Sudan", "Sweden", "Singapore", "Saint Helena, Ascension and Tristan da Cunha", "Slovenia", "Svalbard and Jan Mayen", "Slovakia", "Sierra Leone", "San Marino", "Senegal", "Somalia", "Suriname", "South Sudan", "Sao Tome and Principe", "El Salvador", "Sint Maarten (Dutch part)", "Syrian Arab Republic", "Swaziland", "Turks and Caicos Islands", "Chad", "French Southern Territories", "Togo", "Thailand", "Tajikistan", "Tokelau", "Timor-Leste", "Turkmenistan", "Tunisia", "Tonga", "Turkey", "Trinidad and Tobago", "Tuvalu", "Taiwan, Province of China", "Tanzania, United Republic of", "Ukraine", "Uganda", "United States Minor Outlying Islands", "United States", "Uruguay", "Uzbekistan", "Holy See (Vatican City State)", "Saint Vincent and the Grenadines", "Venezuela, Bolivarian Republic of", "Virgin Islands, British", "Virgin Islands, U.S.", "Viet Nam", "Vanuatu", "Wallis and Futuna", "Samoa", "Yemen", "Mayotte", "South Africa", "Zambia", "Zimbabwe"]
    
    let arrCountryValue = [ "AD", "AE", "AF", "AG", "AI", "AL", "AM", "AO", "AQ", "AR", "AS", "AT", "AU", "AW", "AX", "AZ", "BA", "BB", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BL", "BM", "BN", "BO", "BQ", "BR", "BS", "BT", "BV", "BW", "BY", "BZ", "CA", "CC", "CD", "CF", "CG", "CH", "CI", "CK", "CL", "CM", "CN", "CO", "CR", "CU", "CV", "CW", "CX", "CY", "CZ", "DE", "DJ", "DK", "DM", "DO", "DZ", "EC", "EE", "EG", "EH", "ER", "ES", "ET", "FI", "FJ", "FK", "FM", "FO", "FR", "GA", "GB", "GD", "GE", "GF", "GG", "GH", "GI", "GL", "GM", "GN", "GP", "GQ", "GR", "GS", "GT", "GU", "GW", "GY", "HK", "HM", "HN", "HR", "HT", "HU", "ID", "IE", "IL", "IM", "IN", "IO", "IQ", "IR", "IS", "IT", "JE", "JM", "JO", "JP", "KE", "KG", "KH", "KI", "KM", "KN", "KP", "KR", "KW", "KY", "KZ", "LA", "LB", "LC", "LI", "LK", "LR", "LS", "LT", "LU", "LV", "LY", "MA", "MC", "MD", "ME", "MF", "MG", "MH", "MK", "ML", "MM", "MN", "MO", "MP", "MQ", "MR", "MS", "MT", "MU", "MV", "MW", "MX", "MY", "MZ", "NA", "NC", "NE", "NF", "NG", "NI", "NL", "NO", "NP", "NR", "NU", "NZ", "OM", "PA", "PE", "PF", "PG", "PH", "PK", "PL", "PM", "PN", "PR", "PS", "PT", "PW", "PY", "QA", "RE", "RO", "RS", "RU", "RW", "SA", "SB", "SC", "SD", "SE", "SG", "SH", "SI", "SJ", "SK", "SL", "SM", "SN", "SO", "SR", "SS", "ST", "SV", "SX", "SY", "SZ", "TC", "TD", "TF", "TG", "TH", "TJ", "TK", "TL", "TM", "TN", "TO", "TR", "TT", "TV", "TW", "TZ", "UA", "UG", "UM", "US", "UY", "UZ", "VA", "VC", "VE", "VG", "VI", "VN", "VU", "WF", "WS", "YE", "YT", "ZA", "ZM", "ZW"]
    
    var gender:Bool = true

    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        //CODE TO BE RUN ON CELL TOUCH
        //Logger.log(indexPath.row)
        let rowMenu:Int = indexPath.row
        switch rowMenu{
        case 1:
            Logger.log(rowMenu)
            //select language
            //open popup language
            let lSPopupVC:LSPopupVC = LSPopupVC(nibName:"LSPopupVC", bundle: nil)
            lSPopupVC.delegate = self
            self.presentpopupViewController(lSPopupVC, animationType: .Fade, completion: { () -> Void in
                
            })
        case 2:
            Logger.log(rowMenu)
            //select Proficiency
            //open popup Proficiency
            let englishProficiencyPopupVC:EnglishProficiencyPopupVC = EnglishProficiencyPopupVC(nibName:"EnglishProficiencyPopupVC", bundle: nil)
            englishProficiencyPopupVC.delegate = self
            englishProficiencyPopupVC.titlePopup = "english proficiency"
            englishProficiencyPopupVC.arrShow = arrProficiency
            self.presentpopupViewController(englishProficiencyPopupVC, animationType: .Fade, completion: { () -> Void in
                
            })

        case 3:
            Logger.log(rowMenu)
            //select Country
            //open popup Country
            let englishProficiencyPopupVC:EnglishProficiencyPopupVC = EnglishProficiencyPopupVC(nibName:"EnglishProficiencyPopupVC", bundle: nil)
            englishProficiencyPopupVC.delegate = self
            englishProficiencyPopupVC.titlePopup = "country of birth"
            englishProficiencyPopupVC.arrShow = arrCountry
            englishProficiencyPopupVC.arrValue = arrCountryValue
            self.presentpopupViewController(englishProficiencyPopupVC, animationType: .Fade, completion: { () -> Void in
                
            })
        case 4:
            Logger.log(rowMenu)
            //select Birthday
            //open popup Birthday
            let datePopupVC:DataPopupVC = DataPopupVC(nibName:"DataPopupVC", bundle: nil)
            datePopupVC.delegate = self
            self.presentpopupViewController(datePopupVC, animationType: .Fade, completion: { () -> Void in
                
            })

        case 5:
            Logger.log(rowMenu)
            //select male
            gender = !gender
            drawGender(gender)
            
        case 6:
            Logger.log(rowMenu)
            //select female
            gender = !gender
            drawGender(gender)
            
        case 7:
            Logger.log(rowMenu)
        case 8:
            Logger.log(rowMenu)
        default:
            Logger.log("case default")
        }
        
    }
    
    func drawGender(isMale: Bool) {
        if isMale {
            imgvMale.image = UIImage(named: "checked_checkbox")
            imgvFemale.image = UIImage(named: "unchecked_checkbox")
            
        } else {
            imgvMale.image = UIImage(named: "unchecked_checkbox")
            imgvFemale.image = UIImage(named: "checked_checkbox")
        }
        print(isMale)
        userProfile.gender = isMale
        updateSettings(userProfile)
    }
    
    func updateLanguage(language:String){
        lblLanguage.text = language
    }
    
    func updateProficiency(proficiency:String){
        lblProficiency.text = proficiency
        print(proficiency)
        print(arrProficiency.indexOf(proficiency))
        userProfile.englishProficiency = arrProficiency.indexOf(proficiency)!
        updateSettings(userProfile)
    }
    
    func updateCountry(country:String){
        lblCountry.text = country
        print(arrCountryValue[arrCountry.indexOf(country)!])
        
        userProfile.country = arrCountryValue[arrCountry.indexOf(country)!]
        
        print(userProfile.country)

        updateSettings(userProfile)
    }
    
    func updateBirthday(birthday:NSDate){
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "dd/MM/yyyy"
        lblBirthday.text = dateFormatter.stringFromDate(birthday)
        userProfile.dob = dateFormatter.stringFromDate(birthday)
        updateSettings(userProfile)
    }
    
    func updateSettings(userProfile: UserProfile) {
        AccountManager.updateProfile(userProfile)
    }
    
    func closePopup(sender: AnyObject) {
        self.dismissPopupViewController(.Fade)
    }

}
