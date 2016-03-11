//
//  DeviceManager.swift
//  AccentEasy
//
//  Created by Hai Lu on 3/11/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

class DeviceManager {
    class func imei() -> String {
        return "1236534534534"
    }
    
    class func appVersion() -> String {
        return "500000"
    }
    
    class func appVersionCode() -> String {
        return "500000"
    }
    
    class func languagePrefix() -> String {
        return "AE"
    }
    
    class func deviceName() -> String {
        return "iPhone"
    }
    
    class func deviceInfo() -> UserProfile.DeviceInfo {
        let deviceInfo = UserProfile.DeviceInfo()
        deviceInfo.appName = appVersion()
        deviceInfo.appVersion = appVersionCode()
        deviceInfo.emei = imei()
        deviceInfo.deviceName = deviceName()
        deviceInfo.model = ""
        deviceInfo.osApiLevel = ""
        deviceInfo.osVersion = ""
        return deviceInfo
    }
}
