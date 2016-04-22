//
//  DeviceManager.swift
//  AccentEasy
//
//  Created by Hai Lu on 3/11/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation
import SystemConfiguration
import FBSDKShareKit
import SafariServices
import AVFoundation

class DeviceManager {
    
    class func shareApp(controller: UIViewController, title: String, message: String, url: NSURL) {
        let profile = AccountManager.currentUser()
        let shareMessage = "\(message) \(url.absoluteString)"
        switch(profile.loginType) {
        case UserProfile.TYPE_EASYACCENT:
            var data = Array<AnyObject>()
            data.append(shareMessage)
            data.append(url)
            let shareController = UIActivityViewController(activityItems: data, applicationActivities: nil)
            controller.presentViewController(shareController, animated: true, completion: nil)
            break
        case UserProfile.TYPE_FACEBOOK:
            let fbShare = FBSDKShareLinkContent()
            fbShare.contentDescription = shareMessage
            fbShare.contentTitle = title
            fbShare.contentURL = url
            FBSDKShareDialog.showFromViewController(controller, withContent: fbShare, delegate: nil)
            break
        case UserProfile.TYPE_GOOGLE_PLUS:
            let urlComponents = NSURLComponents(string: "https://plus.google.com/share")
            urlComponents?.queryItems = [NSURLQueryItem(name: "url", value: url.absoluteString)]
            if #available(iOS 9.0, *) {
                let shareController = SFSafariViewController(URL: (urlComponents?.URL)!)
                controller.presentViewController(shareController, animated: true, completion: nil)
            } else {
                UIApplication.sharedApplication().openURL((urlComponents?.URL)!)
            }
            break
        default:
            break
        }
    }
    
    class func doIfConnectedToNetwork(completion:() -> Void) {
        if isConnectedToNetwork() {
            completion()
        } else {
            dispatch_async(dispatch_get_main_queue(),{
                SweetAlert().showAlert("no network available", subTitle: "sorry but your internet connection does not appear to be working", style: AlertStyle.Error)
            })
        }
    }
    
    class func setNavigationBarTransparent(vc: UIViewController) {
        vc.navigationController?.navigationBar.setBackgroundImage(UIImage(), forBarMetrics: UIBarMetrics.Default)
        vc.navigationController?.navigationBar.shadowImage = UIImage()
        vc.navigationController?.navigationBar.translucent = true
        vc.navigationController?.view.backgroundColor = UIColor.clearColor()
    }
    
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
        if let text = NSBundle.mainBundle().infoDictionary?["CFBundleVersion"] as? String {
            return text
        }
        return "500000"
    }
    
    class func appVersionCode() -> String {
        //First get the nsObject by defining as an optional anyObject
        let nsObject: AnyObject? = NSBundle.mainBundle().infoDictionary!["CFBundleShortVersionString"]
        
        //Then just cast the object as a String, but be careful, you may want to double check for nil
        let version = nsObject as! String
        return version
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
    
    class func requestMicrophonePermission(completion:() -> Void) {
        let session: AVAudioSession = AVAudioSession.sharedInstance()
        if (session.respondsToSelector("requestRecordPermission:")) {
            AVAudioSession.sharedInstance().requestRecordPermission({(granted: Bool)-> Void in
                if granted {
                    Logger.log("microphone granted")
                } else {
                    Logger.log("microphone not granted")
                }
                try! session.setCategory(AVAudioSessionCategoryPlayAndRecord)
                try! session.setActive(true)
                try! session.overrideOutputAudioPort(AVAudioSessionPortOverride.Speaker)
                completion()
            })
        } else {
            completion()
        }
    }
    
    class func showLockScreen() {
        NSNotificationCenter.defaultCenter().postNotificationName("showLockScreen", object: nil)
    }
    
    class func hideLockScreen() {
        NSNotificationCenter.defaultCenter().postNotificationName("hideLockScreen", object: nil)
    }
}
