//
//  Login.swift
//  AccentEasy
//
//  Created by CMGVN on 1/27/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

public class Login {
    
    public static let KeyUserProfile:String = "KeyUserProfile"
    public static let KeyRegisterUser:String = "KeyRegisterUser"
    public static let KeyIsShowLogin:String = "KeyIsShowLogin"
    
    public static func showError(){
        dispatch_async(dispatch_get_main_queue(),{
            SweetAlert().showAlert("Login Failed!", subTitle: "sorry our engineers are just upgrading the server, please try again", style: AlertStyle.Error)
            
        })
    }
    
    public static let IS_DEBUG:Bool = true
    
    class func getTestUserProfile() -> UserProfile {
        let user = UserProfile()
        user.username = "luhonghai@gmail.com"
        user.name = "Hai Lu"
        user.loginType = UserProfile.TYPE_GOOGLE_PLUS
        user.token = "6a3fddb4-79ed-472e-83e9-c899e5db8634"
        user.licenseCode = "8FvYvh"
        user.isActivatedLicence = true
        user.isLogin = true
        user.isSubscription = true
        user.profileImage = "https://en.gravatar.com/userimage/43514054/ee7d72e67f6b776a9b03a6361f2d0517.png?size=320"
        let deviceInfo = UserProfile.DeviceInfo()
        deviceInfo.emei = "2a38d560-c9d5-3b42-8ac7-7b642b92339f"
        user.deviceInfo = deviceInfo
        return user
    }
    
    class func getCurrentUser() -> UserProfile {
        if IS_DEBUG {
            return getTestUserProfile()
        } else {
            let userDefaults = NSUserDefaults()
            let keyForUserProfile:String? = userDefaults.objectForKey(Login.KeyUserProfile) as! String
            if (keyForUserProfile != nil) {
                let rawString = userDefaults.objectForKey(keyForUserProfile!) as! String
                return Mapper<UserProfile>().map(rawString)!
            } else {
                return UserProfile()
            }
        }
    }
}
