//
//  AccountManager.swift
//  AccentEasy
//
//  Created by Hai Lu on 3/9/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

class LoginToken: Mappable {
    var token:String!
    
    required init?(_ map: Map) {
        
    }
    
    required init(){}
    
    // Mappable
    func mapping(map: Map) {
        token  <= map["token"]
    }
}

class AuthResponse: Mappable {
    var status:Bool!
    var message:String!
    var data:LoginToken!
    
    required init?(_ map: Map) {
        
    }
    
    required init(){}
    
    // Mappable
    func mapping(map: Map) {
        status    <= map["status"]
        message   <= map["message"]
        data   <= map["data"]
    }
}

class ProfileResponse: Mappable {
    var status:Bool!
    var message:String!
    var data:UserProfile!
    
    required init?(_ map: Map) {
        
    }
    
    required init(){}
    
    // Mappable
    func mapping(map: Map) {
        status    <= map["status"]
        message   <= map["message"]
        data   <= map["data"]
    }
}

class AccountManager {
    
    class func updateProfile(profile: UserProfile) {
        let userDefaults = NSUserDefaults()
        userDefaults.setObject(JSONHelper.toJson(profile), forKey: profile.username)
        userDefaults.setObject(profile.username, forKey: Login.KeyUserProfile)
    }
    
    class func currentUser(username: String = "") -> UserProfile {
        print ("run in currentUser")
        let userDefaults = NSUserDefaults()
        var keyForUserProfile = userDefaults.objectForKey(Login.KeyUserProfile)
        if !username.isEmpty {
            keyForUserProfile = username
        }
        if (keyForUserProfile != nil) {
            let rawString = userDefaults.objectForKey(keyForUserProfile! as! String)
            if rawString != nil && !(rawString as! String).isEmpty {
                return Mapper<UserProfile>().map(rawString as! String)!
            }
        }
        return UserProfile()
    }
    
    class func logout() {
        let p = currentUser()
        p.isLogin = false
        updateProfile(p)
    }
    
    
    class func showError(title: String = "could not connect to server", message: String = DEFAULT_ERROR_MESSAGE){
        dispatch_async(dispatch_get_main_queue(),{
            SweetAlert().showAlert(title, subTitle: message, style: AlertStyle.Error)
        })
    }
    
    class func auth(userProfile: UserProfile, isCheck: Bool = false, completion:(userProfile: UserProfile, success: Bool, message: String) -> Void) {
        userProfile.deviceInfo = DeviceManager.deviceInfo()
        let client = Client()
            .baseUrl(FileHelper.getAccentEasyBaseUrl())
            .onError({e in Logger.log(e)
                Logger.log("run in auth")
                completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
            });
        client.post("/AuthHandler").type("form").send(["profile":JSONHelper.toJson(userProfile), "check": String(isCheck),"imei": DeviceManager.imei()])
            .end({(res:Response) -> Void in
                if(res.error) {
                    completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
                } else {
                    Logger.log("auth response: \(res.text)")
                    if let result: AuthResponse = JSONHelper.fromJson(res.text!) as AuthResponse {
                        if  result.data != nil {
                            userProfile.token = result.data.token
                            Logger.log("login token \(userProfile.token)")
                        } else {
                            Logger.log("\(result.message)")
                        }
                        completion(userProfile: userProfile, success: result.status, message:  result.message)
                    } else {
                        completion(userProfile: userProfile, success: false, message:  DEFAULT_ERROR_MESSAGE)

                    }
                    
                }
            })

    }
    
    class func register(userProfile: UserProfile, completion:(userProfile: UserProfile, success: Bool, message: String) -> Void) {
        userProfile.deviceInfo = DeviceManager.deviceInfo()
        let client = Client()
            .baseUrl(FileHelper.getAccentEasyBaseUrl())
            .onError({e in
                completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
        });
        client.post("/RegisterHandler").type("form").send(["version_code" : DeviceManager.appVersionCode(),"profile": JSONHelper.toJson(userProfile),"lang_prefix": DeviceManager.languagePrefix(),"imei": DeviceManager.imei()])
            .end({(res:Response) -> Void in
                Logger.log(res)
                if(res.error) { // status of 2xx
                    completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
                }
                else {
                    Logger.log("register response: \(res.text)")
                    let result: ProfileResponse = JSONHelper.fromJson(res.text!)
                    completion(userProfile: userProfile, success: result.status, message: result.message)
                }
            })
    }
    
    class func fetchProfile(userProfile: UserProfile, completion:(userProfile: UserProfile, success: Bool, message: String) -> Void) {
        userProfile.deviceInfo = DeviceManager.deviceInfo()
        let client = Client()
            .baseUrl(FileHelper.getAccentEasyBaseUrl())
            .onError({e in
                completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
            });
        
        client.post("/userprofile").type("form").send(["profile": JSONHelper.toJson(userProfile), "action":"get"])
            .end({(res:Response) -> Void in
                Logger.log("fetch profile response: \(res.text)")
                if(res.error) { // status of 2xx
                    completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
                }
                else {
                    let result: ProfileResponse = JSONHelper.fromJson(res.text!)
                    if result.data != nil {
                        userProfile.name = result.data.name
                    }
                    completion(userProfile: userProfile, success: result.status, message: result.message)
                }
            })
        
        
    }
    
    class func updateProfile(userProfile: UserProfile, completion:(userProfile: UserProfile, success: Bool, message: String) -> Void) {
        userProfile.deviceInfo = DeviceManager.deviceInfo()
        let client = Client()
            .baseUrl(FileHelper.getAccentEasyBaseUrl())
            .onError({e in
                completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
            });
        
        client.post("/userprofile").type("form").send(["profile": JSONHelper.toJson(userProfile), "action":"update"])
            .end({(res:Response) -> Void in
                Logger.log("fetch profile response: \(res.text)")
                if(res.error) { // status of 2xx
                    completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
                }
                else {
                    let result: ProfileResponse = JSONHelper.fromJson(res.text!)
                    if result.data != nil {
                        userProfile.name = result.data.name
                    }
                    completion(userProfile: userProfile, success: result.status, message: result.message)
                }
            })
        
        
    }

    
    class func activate(code: String, userProfile: UserProfile, completion:(userProfile: UserProfile, success: Bool, message: String) -> Void) {
        Logger.log("run in active code")
        userProfile.deviceInfo = DeviceManager.deviceInfo()
        Logger.log("run in active code2")
        let client = Client()
            .baseUrl(FileHelper.getAccentEasyBaseUrl())
            .onError({e in
                Logger.log("error")
                completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
            });
        
        Logger.log("device info \(JSONHelper.toJson(userProfile.deviceInfo))")
        
        Logger.log("------------")
        Logger.log(code)
        Logger.log(DeviceManager.appVersion())
        Logger.log(userProfile.username)
        Logger.log(DeviceManager.languagePrefix())
        Logger.log(DeviceManager.imei())
        Logger.log("------------")
        
        client.post("/activate").type("form").send(["acc":code,"version_code" : DeviceManager.appVersion(),"user":userProfile.username,"lang_prefix": DeviceManager.languagePrefix(),"imei": DeviceManager.imei()])
            .end({(res:Response) -> Void in
                Logger.log("activate register code response: \(res.text)")
                if(res.error) { // status of 2xx
                    completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
                }
                else {
                    var message = "your acount has already been activated. please login with your email address and password."
                    var success = true
                    if res.text != "success" {
                        message = res.text!
                        success = false
                    }
                    completion(userProfile: userProfile, success: success, message: message)
                }
            })
    }
    
    class func resendCode(userProfile: UserProfile, completion:(userProfile: UserProfile, success: Bool, message: String) -> Void) {
        userProfile.deviceInfo = DeviceManager.deviceInfo()
        let client = Client()
            .baseUrl(FileHelper.getAccentEasyBaseUrl())
            .onError({e in
                completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
            });
        
        client.post("/activate").type("form").send(["version_code" : DeviceManager.appVersion(),"profile": JSONHelper.toJson(userProfile),"lang_prefix": DeviceManager.languagePrefix(),"imei": DeviceManager.imei()])
            .end({(res:Response) -> Void in
                Logger.log("resend code response: \(res.text)")
                if(res.error) { // status of 2xx
                    completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
                }
                else {
                    var message = "please check message in your email \(userProfile.username)"
                    var success = true
                    if res.text != "success" {
                        message = res.text!
                        success = false
                    }
                    completion(userProfile: userProfile, success: success, message: message)
                }
            })
    }
    
    class func resetPassword(userProfile: UserProfile, completion:(userProfile: UserProfile, success: Bool, message: String) -> Void) {
        userProfile.deviceInfo = DeviceManager.deviceInfo()
        let client = Client()
            .baseUrl(FileHelper.getAccentEasyBaseUrl())
            .onError({e in
                completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
            });
        
        client.post("/ResetPasswordHandler").type("form").send(["acc":userProfile.username,"action":"request","imei": DeviceManager.imei()])
            .end({(res:Response) -> Void in
                Logger.log("reset password response: \(res.text)")
                if(res.error) { // status of 2xx
                    completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
                }
                else {
                    var message = "please check message in your email \(userProfile.username)"
                    var success = true
                    if res.text != "success" {
                        message = res.text!
                        success = false
                    }
                    completion(userProfile: userProfile, success: success, message: message)
                }
            })
    }
}