//
//  Login.swift
//  AccentEasy
//
//  Created by CMGVN on 1/27/16.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import Foundation

public class Login {
    public static let KeyUserProfile:String = "KeyUserProfile"
    
    class func getTestUserProfile() -> UserProfile {
        let user = UserProfile()
        user.username = "luhonghai@gmail.com"
        user.name = "Hai Lu"
        user.loginType = UserProfile.TYPE_GOOGLE_PLUS
        user.token = ""
        user.licenseCode = "8FvYvh"
        user.isActivatedLicence = true
        user.isSubscription = true
        user.profileImage = "https://en.gravatar.com/userimage/43514054/ee7d72e67f6b776a9b03a6361f2d0517.png?size=320"
        return user
    }
}
