//
//  BottomTabBarController.swift
//  AccentEasy
//
//  Created by Minh Kelly on 2/25/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation
import RDVTabBarController

class BottomTabBarController: RDVTabBarController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "loadList:",name:"showChart", object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "loadTabbar:",name:"loadTabbar", object: nil)
        updateTabbar()
    }
    
    func updateTabbar() {
        let firstController = self.storyboard!.instantiateViewControllerWithIdentifier("graphController") as! GraphPageViewController
        
        let secondController = self.storyboard!.instantiateViewControllerWithIdentifier("historyController") as! HistoryTableController
        
        let thirdController = self.storyboard!.instantiateViewControllerWithIdentifier("tipController") as! TipsViewController
        
        self.viewControllers = GlobalData.getInstance().isOnLessonMain ? [
            firstController,
            secondController
            ] : [
            firstController,
            secondController,
            thirdController
        ]
        let tabBar = self.tabBar
        tabBar.backgroundColor = UIColor.whiteColor()
        let tabBarHeight: CGFloat = 50
        tabBar.frame = CGRectMake(0, -300, CGRectGetWidth(tabBar.frame), tabBarHeight)
        var index = 0
        for item in tabBar.items as! [RDVTabBarItem] {
            item.setBackgroundSelectedImage(UIImage(named: "tab-background-selected.png"), withUnselectedImage: UIImage(named: "tab-background-unselected.png"))
            var selectedImg = ""
            var unselectedImg = ""
            switch (index) {
            case 0:
                selectedImg = "p_graph_blue.png"
                unselectedImg = "p_graph_blue_blur.png"
                break
            case 1:
                selectedImg = "p_history_blue.png"
                unselectedImg = "p_history_blue_blur.png"
                break
            case 2:
                selectedImg = "p_tip_blue.png"
                unselectedImg = "p_tip_blue_blur.png"
                break
            default:
                break
            }
            item.setFinishedSelectedImage(ImageHelper.imageWithImage(UIImage(named: selectedImg)!, scaledToSize: CGSize(width: tabBarHeight - 2, height: tabBarHeight - 2)),
                withFinishedUnselectedImage: ImageHelper.imageWithImage(UIImage(named: unselectedImg)!, scaledToSize: CGSize(width: tabBarHeight - 2, height: tabBarHeight - 2)))
            index++
        }
    }
    
    func loadTabbar(notification: NSNotification){
        updateTabbar()
    }
    
    func loadList(notification: NSNotification){
        //load data here
        //let indexChart = notification.object as? String
        //loadTable(word)
        //tabBar.selectedItem = tabBar.items[0] as! RDVTabBarItem
        self.selectedIndex = 0
        //self.tabBarController?.selectedIndex = 0
        //self.tabBarController?.selectedViewController = self.tabBarController!.viewControllers![0]
    }
    
}