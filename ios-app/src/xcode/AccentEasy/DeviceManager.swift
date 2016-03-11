//
//  DeviceManager.swift
//  AccentEasy
//
//  Created by Hai Lu on 3/11/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation
import SystemConfiguration

class DeviceManager {
    
    class func isConnectedToNetwork() -> Bool {
        var zeroAddress = sockaddr_in()
        zeroAddress.sin_len = UInt8(sizeofValue(zeroAddress))
        zeroAddress.sin_family = sa_family_t(AF_INET)
        let defaultRouteReachability = withUnsafePointer(&zeroAddress) {
            SCNetworkReachabilityCreateWithAddress(nil, UnsafePointer($0))
        }
        var flags = SCNetworkReachabilityFlags()
        if !SCNetworkReachabilityGetFlags(defaultRouteReachability!, &flags) {
            return false
        }
        let isReachable = (flags.rawValue & UInt32(kSCNetworkFlagsReachable)) != 0
        let needsConnection = (flags.rawValue & UInt32(kSCNetworkFlagsConnectionRequired)) != 0
        return (isReachable && !needsConnection)
    }
    
    class func imei() -> String {
        let newUniqueID = CFUUIDCreate(kCFAllocatorDefault)
        let newUniqueIDString = CFUUIDCreateString(kCFAllocatorDefault, newUniqueID);
        let guid = newUniqueIDString as NSString
        return guid.lowercaseString
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
        return UIDevice.currentDevice().name
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
