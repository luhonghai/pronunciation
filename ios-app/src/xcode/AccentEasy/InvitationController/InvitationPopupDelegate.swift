//
//  InvitationPopupDelegate.swift
//  AccentEasy
//
//  Created by CMGVN on 4/26/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

@objc protocol InvitationPopupDelegate {
    optional func closePopup(sender: AnyObject)
    optional func inviAcceptPopupVCTouchOK(sender: AnyObject)
    optional func invitationPopupVCTouchOK(sender: AnyObject)
    optional func invitationPopupVCTouchReject(sender: AnyObject)
    optional func invitationNotificationPopupTouchOK(sender: AnyObject)
}