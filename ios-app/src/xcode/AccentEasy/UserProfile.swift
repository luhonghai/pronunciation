//
//  UserProfile.swift
//  AccentEasy
//
//  Created by CMGVN on 1/15/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

public class UserProfile: Mappable {
    public static let TYPE_EASYACCENT:String = "easyaccent"
    public static let TYPE_FACEBOOK:String = "facebook"
    public static let TYPE_GOOGLE_PLUS:String = "googleplus"
    
    public static let HELP_INIT:Int = 0
    public static let HELP_SKIP:Int = 1
    public static let HELP_NEVER:Int = 2
    //typealias HELP_INIT = Int
    //var so5:alias = 5
    
    public var username:String!
    public var firstName:String!
    public var lastName:String!
    public var name:String!
    public var loginType:String!
    public var profileImage:String!
    public var password:String!
    public var isSetup:Bool = false;
    public var nativeEnglish:Bool = true
    public var gender:Bool = true
    public var dob:String = "01/01/1900";
    public var country:String = "GB";
    public var englishProficiency:Int = 5
    public var time:CLong!
    public var duration:CLong!
    public var location:UserLocation!
    public var deviceInfo:DeviceInfo!
    public var uuid:String!
    public var helpStatus:Int = 0
    public var helpStatusLesson:Int = 0
    public var isLogin:Bool = false
    public var lastSelectedMenuItem:String!
    public var licenseCode:String!
    public var selectedCountry:AECountry!
    public var token:String!
    public var additionalToken:String!
    public var isActivatedLicence:Bool!
    public var isSubscription:Bool!
    public var isExpired:Bool!
    public var licenseData = [LicenseData]()
    
    public var courseSession = AECourseSession()
    
    /*private String ;
    private List<LicenseData> ;*/
    
    public class UserLocation {
        public var latitude:Double!
        public var longitude:Double!
        
    }
    
    public func getSelectedCourseId() -> String {
        return courseSession.selectedCourse.idString
    }
    
    public class DeviceInfo: Mappable{
        public var appVersion:String!
        public var appName:String!
        public var model:String!
        public var osVersion:String!
        public var osApiLevel:String!
        public var deviceName:String!
        public var emei:String!
        public var gcmId:String!
        
        required public init?(_ map: Map) {
            
        }
        
        required public init(){
            
        }
        
        // Mappable
        public func mapping(map: Map) {
            appVersion    <= map["appVersion"]
            appName   <= map["appName"]
            model      <= map["model"]
            osVersion       <= map["osVersion"]
            osApiLevel  <= map["osApiLevel"]
            deviceName  <= map["deviceName"]
            emei     <= map["emei"]
            gcmId     <= map["gcmId"]
        }

        
    }
    
    public class LicenseData {
        public var code:String!
        /**
        private UserProfile.DeviceInfo deviceInfo;
        
        
        public DeviceInfo getDeviceInfo() {
        return deviceInfo;
        }
        
        public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
        }
        
        @Override
        public String toString() {
        return code
        + (deviceInfo != null ? (" (" + deviceInfo.getModel() + " " + deviceInfo.getDeviceName() + ")") : "");
        }*/
    }
    
    required public init?(_ map: Map) {
        
    }
    
    required public init(){
        
    }
    
    // Mappable
    public func mapping(map: Map) {
        username    <= map["username"]
        firstName   <= map["firstName"]
        lastName      <= map["lastName"]
        name       <= map["name"]
        loginType  <= map["loginType"]
        profileImage  <= map["profileImage"]
        password     <= map["password"]
        isSetup    <= map["isSetup"]
        gender    <= map["gender"]
        dob    <= map["dob"]
        country    <= map["country"]
        englishProficiency    <= map["englishProficiency"]
        time    <= map["time"]
        duration    <= map["duration"]
        location    <= map["location"]
        deviceInfo    <= map["deviceInfo"]
        uuid    <= map["uuid"]
        helpStatus    <= map["helpStatus"]
        helpStatusLesson    <= map["helpStatusLesson"]
        isLogin    <= map["isLogin"]
        lastSelectedMenuItem    <= map["lastSelectedMenuItem"]
        licenseCode    <= map["licenseCode"]
        selectedCountry    <= map["selectedCountry"]
        token    <= map["token"]
        additionalToken    <= map["additionalToken"]
        isActivatedLicence    <= map["isActivatedLicence"]
        isSubscription    <= map["isSubscription"]
        isExpired    <= map["isExpired"]
        licenseData    <= map["licenseData"]
    }

}
