//
//  GraphPageViewController.swift
//  AccentEasy
//
//  Created by CMG on 04/02/2016.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class GraphPageViewController: UIPageViewController, UIPageViewControllerDataSource {
    
    let count = 5;
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.dataSource = self;
        let firstController = getItemController(0)!
        let startingViewControllers: NSArray = [firstController]
        self.setViewControllers(startingViewControllers as? [UIViewController], direction: UIPageViewControllerNavigationDirection.Forward, animated: false, completion: nil)
    }
    
    func pageViewController(pageViewController: UIPageViewController, viewControllerBeforeViewController viewController: UIViewController) -> UIViewController? {
        
        let itemController = viewController as! GraphPageItemController
        
        if itemController.itemIndex > 0 {
            return getItemController(itemController.itemIndex-1)
        }
        
        return nil
    }
    
    func pageViewController(pageViewController: UIPageViewController, viewControllerAfterViewController viewController: UIViewController) -> UIViewController? {
        
        let itemController = viewController as! GraphPageItemController
        
        if itemController.itemIndex+1 < count {
            return getItemController(itemController.itemIndex+1)
        }
        
        return nil
    }
    
    private func getItemController(itemIndex: Int) -> GraphPageItemController? {
        
        if itemIndex < count {
            let pageItemController = self.storyboard!.instantiateViewControllerWithIdentifier("GraphItemController") as! GraphPageItemController
            pageItemController.itemIndex = itemIndex
            return pageItemController
        }
        
        return nil
    }
    
    // MARK: - Page Indicator
    
    func presentationCountForPageViewController(pageViewController: UIPageViewController) -> Int {
        return count
    }
    
    func presentationIndexForPageViewController(pageViewController: UIPageViewController) -> Int {
        return 0
    }

}