//
//  GraphPageViewController.swift
//  AccentEasy
//
//  Created by CMG on 04/02/2016.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class GraphPageViewController: UIPageViewController, UIPageViewControllerDataSource {
    
    var lessonDbAdapter: WordCollectionDbApdater!
    
    var freestyleDbAdapter: FreeStyleDBAdapter!
    
    var arpabets = Array<String>()
    
    var word: String!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.dataSource = self;
        
        lessonDbAdapter = WordCollectionDbApdater()
        freestyleDbAdapter = FreeStyleDBAdapter()
        
        let firstController = self.storyboard!.instantiateViewControllerWithIdentifier("GraphItemController") as! GraphPageItemController
        let startingViewControllers: NSArray = [firstController]
        self.setViewControllers(startingViewControllers as? [UIViewController], direction: UIPageViewControllerNavigationDirection.Forward, animated: false, completion: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "loadWord:",name:"loadGraph", object: nil)
    }
    
    func loadWord(notification: NSNotification){
        //load data here
        let word = notification.object as! String
        self.dataSource = nil
        do {
            let wc = try lessonDbAdapter.findByWord(word)
            print("Load graph for word \(wc.word). Pronunciation: \(wc.getArpabetList())")
            self.word = word
            arpabets = wc.getArpabetList()
            self.dataSource = self
            self.loadView()
            let firstController = getItemController(0)
            if (firstController != nil) {
                let startingViewControllers: NSArray = [firstController!]
                self.setViewControllers(startingViewControllers as? [UIViewController], direction: UIPageViewControllerNavigationDirection.Forward, animated: true, completion: nil)
            }
        } catch {
            
        }
        
    }
    
    func pageViewController(pageViewController: UIPageViewController, viewControllerBeforeViewController viewController: UIViewController) -> UIViewController? {
        let itemController = viewController as? GraphPageItemController
        if itemController != nil && itemController!.itemIndex > 0 {
            return getItemController(itemController!.itemIndex-1)
        }
        return nil
    }
    
    func pageViewController(pageViewController: UIPageViewController, viewControllerAfterViewController viewController: UIViewController) -> UIViewController? {
        
        let itemController = viewController as? GraphPageItemController
        if itemController != nil && itemController!.itemIndex + 1 < (arpabets.count + 1) {
            return getItemController(itemController!.itemIndex+1)
        }
        return nil
    }
    
    private func getItemController(itemIndex: Int) -> GraphPageItemController? {
        if (arpabets.count == 0) {
            return nil
        }
        if itemIndex < (arpabets.count + 1) {
            let pageItemController = self.storyboard!.instantiateViewControllerWithIdentifier("GraphItemController") as! GraphPageItemController
            pageItemController.itemIndex = itemIndex
            if (itemIndex == 0) {
                pageItemController.isWord = true
                pageItemController.data = word
            } else {
                pageItemController.isWord = false
                pageItemController.data = arpabets[itemIndex - 1]
            }
            return pageItemController
        }
        
        return nil
    }
    
    // MARK: - Page Indicator
    
    func presentationCountForPageViewController(pageViewController: UIPageViewController) -> Int {
        return arpabets.count == 0 ? 0 : (arpabets.count + 1)
    }
    
    func presentationIndexForPageViewController(pageViewController: UIPageViewController) -> Int {
        return 0
    }

}