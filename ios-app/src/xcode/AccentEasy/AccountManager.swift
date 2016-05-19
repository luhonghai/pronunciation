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

class InvitationDataResponse: Mappable {
    var status:Bool!
    var message:String!
    var data:[InvitationData]!
    
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
    
    static var currentUsername = ""
    
    static var currentProfile = [String: UserProfile]()
    
    class func updateProfile(profile: UserProfile) {
        let userDefaults = NSUserDefaults()
        userDefaults.setObject(JSONHelper.toJson(profile), forKey: profile.username)
        userDefaults.setObject(profile.username, forKey: Login.KeyUserProfile)
        currentUsername = profile.username
        currentProfile[profile.username] = profile
    }
    
    class func currentUser(username: String = "") -> UserProfile {
        let userDefaults = NSUserDefaults()
        var keyForUserProfile: String?
        if username.isEmpty {
            if currentUsername.isEmpty {
                keyForUserProfile = userDefaults.objectForKey(Login.KeyUserProfile) as? String
            } else {
                keyForUserProfile = currentUsername
            }
            
        } else {
            keyForUserProfile = username
            currentUsername = keyForUserProfile!
        }
        
        if (keyForUserProfile != nil) {
            if currentProfile.indexForKey(keyForUserProfile!) == nil {
                let rawString = userDefaults.objectForKey(keyForUserProfile!)
                if rawString != nil && !(rawString as! String).isEmpty {
                    currentProfile[keyForUserProfile!] = Mapper<UserProfile>().map(rawString as! String)!
                } else {
                    return UserProfile()
                }

            }
            return currentProfile[keyForUserProfile!]!
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
                        //userProfile.selectedCountry = result.data.selectedCountry
                        userProfile.englishProficiency = result.data.englishProficiency
                        userProfile.country = result.data.country
                        userProfile.dob = result.data.dob
                        userProfile.gender = result.data.gender
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
                Logger.log("update profile response: \(res.text)")
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
    
    //feedback.setDescription(storePara.get(DeviceInfoCommon.FEEDBACK_DESCRIPTION));
    
    class func sendFeedBack(userProfile: UserProfile, description: String, completion:(userProfile: UserProfile, success: Bool, message: String) -> Void) {
        userProfile.deviceInfo = DeviceManager.deviceInfo()
        let client = Client()
            .baseUrl(FileHelper.getAccentEasyBaseUrl())
            .onError({e in
                completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
            });
        
        //client.post("/VoiceRecordHandler").field("country", "countryId").field("profile",).field("word", weakSelf!.selectedWord.word).attach("imageKey", "/Volumes/DATA/AccentEasy/pronunciation/ios-app/src/xcode/AccentEasy/fixed_6a11adce-13bb-479e-bcbc-13a7319677f9_raw.wav"))
        
        client.post("/FeedbackHandler").field("profile",JSONHelper.toJson(userProfile)).field("Feedback description",description).attach("imageKey", "/Volumes/DATA/AccentEasy/pronunciation/ios-app/src/xcode/AccentEasy/No-image-found.gif")
            .end({(res:Response) -> Void in
                Logger.log("send feed back: \(res.text)")
                if(res.error) { // status of 2xx
                    completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
                }
                else {
                    var message = "thank you for your feedback we appreciate your feedback and will continue to strive to make our application better \n thank you!"
                    var success = true
                    if res.text != "Done" {
                        success = false
                        message = DEFAULT_ERROR_MESSAGE
                    }
                    completion(userProfile: userProfile, success: success, message: message)
                }
            })
    }
      
    class func fetchCourses(userProfile: UserProfile, completion:(userProfile: UserProfile, success: Bool, message: String) -> Void) {
        userProfile.deviceInfo = DeviceManager.deviceInfo()
        let client = Client()
            .baseUrl(FileHelper.getAccentEasyBaseUrl())
            .onError({e in
                completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
            });
        
        client.post("/SyncCoursePerStudentServlet").type("form").send(["profile":JSONHelper.toJson(userProfile)
            , "action": "listAllCourse"])
            .end({(res:Response) -> Void in
                Logger.log("fetch course response: \(res.text)")
                if(res.error) { // status of 2xx
                    completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
                }
                else {
                    let response: AECourseResponse = JSONHelper.fromJson(res.text!)
                    let session = AECourseSession()
                    session.username = userProfile.username
//                    // DUMY SECTION
//                    let c1 = AECourse()
//                    c1.name = "course 1"
//                    c1.idString = "8b473661-6347-4864-a707-6037b7fdd59b"
//                    c1.version = 2
//                    c1.dbURL = "https://s3-ap-southeast-1.amazonaws.com/com-accenteasy-bbc-accent-dev/database_data/8b473661-6347-4864-a707-6037b7fdd59b-v2.zip"
//                    c1.imageURL = ""
//                    session.courses.append(c1)
//                    let c2 = AECourse()
//                    c2.name = "course 2"
//                    c2.idString = "82e9496e-fd94-44db-9f97-0dc27dc54543"
//                    c2.version = 3
//                    c2.dbURL = "https://s3-ap-southeast-1.amazonaws.com/com-accenteasy-bbc-accent-dev/database_data/82e9496e-fd94-44db-9f97-0dc27dc54543-v2.zip"
//                    c2.imageURL = "http://www.arenaandheri.co.in/demo_files/icon-calendar.png"
//                    session.courses.append(c2)
//                    session.selectedCourse = c1
                    // Remove after merge code
                    session.courses = response.data
                    if DatabaseHelper.updateCourses(session) {
                        userProfile.courseSession = session
                        completion(userProfile: userProfile, success: true, message: "success")
                        
                    } else {
                        completion(userProfile: userProfile, success: false, message: "could not fetch course databases")
                    }
                }
            })
    }
    
    class func getInvitationData(userProfile: UserProfile, completion:(userProfile: UserProfile, success: Bool, message: String) -> Void) {
        userProfile.deviceInfo = DeviceManager.deviceInfo()
        let client = Client()
            .baseUrl(FileHelper.getAccentEasyBaseUrl())
            .onError({e in Logger.log(e)
                Logger.log("run in getInvitationData")
                completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
            });
        client.post("/InvitationServlet").type("form").send(["profile":JSONHelper.toJson(userProfile), "action":"getdata" ])
            .end({(res:Response) -> Void in
                if(res.error) {
                    completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
                } else {
                    Logger.log("getInvitationData response: \(res.text)")
                    if let result:InvitationDataResponse = JSONHelper.fromJson(res.text!) as InvitationDataResponse {
                        if result.message == nil {
                            result.message = ""
                        }
                        var currentUser = AccountManager.currentUser()
                        //print(result.data)
                        if  result.data != nil && result.data.count != 0{
                            //print("run in update data")
                            //userProfile.token = result.data.token
                            //Logger.log("login token \(userProfile.token)")
                            currentUser.invitationData = result.data
                            AccountManager.updateProfile(currentUser)
                        } else {
                            Logger.log("\(result.message)")
                            //fake data
                            //var fakeData = "{\"invitationData\":[{\"id\":\"id\ ",\"studentName\":\"hoang.nguyen1@c-mg.com\",\"teacherName\":\"teacher1@c-mg.com\",\"firstTeacherName\":\"hoang1\",\"lastTeacherName\":\"nguyen1\",\"companyName\":\"cmg cmg cmg\",\"status\":\"accept\"},{\"id\":\"id\",\"studentName\":\"hoang.nguyen2@c-mg.com\",\"teacherName\":\"teacher2@c-mg.com\",\"firstTeacherName\":\"hoang2\",\"lastTeacherName\":\"nguyen2\",\"companyName\":\"cmg cmg cmg\",\"status\":\"reject\"},{\"id\":\"id\",\"studentName\":\"hoang.nguyen3@c-mg.com\",\"teacherName\":\"teacher3@c-mg.com\",\"firstTeacherName\":\"hoang3\",\"lastTeacherName\":\"nguyen3\",\"companyName\":\"cmg cmg cmg\",\"status\":\"reject\"},{\"id\":\"id\",\"studentName\":\"hoang.nguyen4@c-mg.com\",\"teacherName\":\"teacher4@c-mg.com\",\"firstTeacherName\":\"hoang4\",\"lastTeacherName\":\"nguyen4\",\"companyName\":\"cmg cmg cmg\",\"status\":\"reject\"},{\"id\":\"id\",\"studentName\":\"hoang.nguyen5@c-mg.com\",\"teacherName\":\"teacher5@c-mg.com\",\"firstTeacherName\":\"hoang5\",\"lastTeacherName\":\"nguyen5\",\"companyName\":\"cmg cmg cmg\",\"status\":\"accept\"},{\"id\":\"id\",\"studentName\":\"hoang.nguyen6@c-mg.com\",\"teacherName\":\"teacher6@c-mg.com\",\"firstTeacherName\":\"hoang6\",\"lastTeacherName\":\"nguyen6\",\"companyName\":\"cmg cmg cmg\",\"status\":\"accept\"},{\"id\":\"id\",\"studentName\":\"hoang.nguyen7@c-mg.com\",\"teacherName\":\"teacher7@c-mg.com\",\"firstTeacherName\":\"hoang7\",\"lastTeacherName\":\"nguyen7\",\"companyName\":\"cmg cmg cmg\",\"status\":\"pending\"},{\"id\":\"id\",\"studentName\":\"hoang.nguyen8@c-mg.com\",\"teacherName\":\"teacher8@c-mg.com\",\"firstTeacherName\":\"hoang8\",\"lastTeacherName\":\"nguyen8\",\"companyName\":\"cmg cmg cmg\",\"status\":\"pending\"},{\"id\":\"id\",\"studentName\":\"hoang.nguyen9@c-mg.com\",\"teacherName\":\"teacher9@c-mg.com\",\"firstTeacherName\":\"hoang9\",\"lastTeacherName\":\"nguyen9\",\"companyName\":\"cmg cmg cmg\",\"status\":\"pending\"},{\"id\":\"id\",\"studentName\":\"hoang.nguyen10@c-mg.com\",\"teacherName\":\"teacher10@c-mg.com\",\"firstTeacherName\":\"hoang10\",\"lastTeacherName\":\"nguyen10\",\"companyName\":\"cmg cmg cmg\",\"status\":\"accept\"},{\"id\":\"id\",\"studentName\":\"hoang.nguyen11@c-mg.com\",\"teacherName\":\"teacher11@c-mg.com\",\"firstTeacherName\":\"hoang11\",\"lastTeacherName\":\"nguyen11\",\"companyName\":\"cmg cmg cmg\",\"status\":\"reject\"},{\"id\":\"id\",\"studentName\":\"hoang.nguyen12@c-mg.com\",\"teacherName\":\"teacher12@c-mg.com\",\"firstTeacherName\":\"hoang12\",\"lastTeacherName\":\"nguyen12\",\"companyName\":\"cmg cmg cmg\",\"status\":\"pending\"}]}"
                            //let inviData = Mapper<UserProfile>().map(fakeData)!
                            //currentUser.invitationData = inviData.invitationData
                            //AccountManager.updateProfile(currentUser)
                            
                            var invitationData = [InvitationData]()
                            currentUser.invitationData = invitationData
                            AccountManager.updateProfile(currentUser)
                        }
                        completion(userProfile: currentUser, success: result.status, message:  result.message)
                    } else {
                        completion(userProfile: userProfile, success: false, message:  DEFAULT_ERROR_MESSAGE)
                        
                    }
                    
                }
            })
        
    }
    
    
    class func updateRejectData(userProfile: UserProfile, id: String, completion:(userProfile: UserProfile, success: Bool, message: String) -> Void) {
        userProfile.deviceInfo = DeviceManager.deviceInfo()
        let client = Client()
            .baseUrl(FileHelper.getAccentEasyBaseUrl())
            .onError({e in Logger.log(e)
                Logger.log("run in updateRejectData")
                completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
            });
        client.post("/InvitationServlet").type("form").send(["profile":JSONHelper.toJson(userProfile), "action":"updatereject", "id": id])
            .end({(res:Response) -> Void in
                if(res.error) {
                    completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
                } else {
                    Logger.log("updateRejectData response: \(res.text)")
                    if let result:InvitationDataResponse = JSONHelper.fromJson(res.text!) as InvitationDataResponse {
                        if  ((result.status != nil) && result.status != nil) {
                            Logger.log("updateRejectData \(id) ok")
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
    
    class func updateAcceptData(userProfile: UserProfile, id: String, completion:(userProfile: UserProfile, success: Bool, message: String) -> Void) {
        userProfile.deviceInfo = DeviceManager.deviceInfo()
        let client = Client()
            .baseUrl(FileHelper.getAccentEasyBaseUrl())
            .onError({e in Logger.log(e)
                Logger.log("run in updateAcceptData")
                completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
            });
        client.post("/InvitationServlet").type("form").send(["profile":JSONHelper.toJson(userProfile), "action":"updateaccept", "id": id])
            .end({(res:Response) -> Void in
                if(res.error) {
                    completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
                } else {
                    Logger.log("updateAcceptData response: \(res.text)")
                    if let result:InvitationDataResponse = JSONHelper.fromJson(res.text!) as InvitationDataResponse {
                        if  ((result.status != nil) && result.status != nil) {
                            Logger.log("updateAcceptData \(id) ok")
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

    class func updateDeleteData(userProfile: UserProfile, id: String, completion:(userProfile: UserProfile, success: Bool, message: String) -> Void) {
        userProfile.deviceInfo = DeviceManager.deviceInfo()
        let client = Client()
            .baseUrl(FileHelper.getAccentEasyBaseUrl())
            .onError({e in Logger.log(e)
                Logger.log("run in deleteData")
                completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
            });
        client.post("/InvitationServlet").type("form").send(["profile":JSONHelper.toJson(userProfile), "action":"updateDeleteData", "id": id])
            .end({(res:Response) -> Void in
                if(res.error) {
                    completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
                } else {
                    Logger.log("deleteData response: \(res.text)")
                    if let result:InvitationDataResponse = JSONHelper.fromJson(res.text!) as InvitationDataResponse {
                        if  ((result.status != nil) && result.status != nil) {
                            Logger.log("deleteData \(id) ok")
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
    
    class func fetchGooglePlusInfo(accessToken:String, userProfile: UserProfile, completion:(userProfile: UserProfile, success: Bool, message: String) -> Void) {
        let client = Client()
            .baseUrl("https://www.googleapis.com")
            .onError({e in Logger.log(e)
                completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
            });
        client.get("/oauth2/v3/userinfo?access_token=\(accessToken)").end({(res:Response) -> Void in
                if(res.error) {
                    completion(userProfile: userProfile, success: false, message: DEFAULT_ERROR_MESSAGE)
                } else {
                    do {
                        print("Google user info response \(res.text)")
                        let userData = try NSJSONSerialization.JSONObjectWithData(res.data!, options:[]) as? [String:AnyObject]
                        if let gender = userData!["gender"] as? String {
                            Logger.log("User \(userProfile.username) gender \(gender)")
                            userProfile.gender = gender == "male"
                        }
                    } catch {
                        NSLog("Account Information could not be loaded")
                    }
                    completion(userProfile: userProfile, success: true, message: "success")
                }
            })
    }


}