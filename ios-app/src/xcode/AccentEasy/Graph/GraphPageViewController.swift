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
    
    var arpabets = Array<String>()
    
    var word: String!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.dataSource = self;
        
        lessonDbAdapter = WordCollectionDbApdater()
        
        let firstController = self.storyboard!.instantiateViewControllerWithIdentifier("GraphItemController") as! GraphPageItemController
        let startingViewControllers: NSArray = [firstController]
        self.setViewControllers(startingViewControllers as? [UIViewController], direction: UIPageViewControllerNavigationDirection.Forward, animated: false, completion: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "loadWord:",name:"loadGraph", object: nil)
        
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "loadList:",name:"showChart", object: nil)
    }
    
    func loadList(notification: NSNotification){
        weak var weakSelf = self
        dispatch_async(dispatch_get_main_queue(),{
            //load data here
            let indexChart = notification.object as! Int
            //loadTable(word)
            weakSelf!.loadViewController(indexChart + 1)
        })
    }
    
    func loadViewController(index: Int) {
        do {
            weak var weakSelf = self
            let controller = self.getItemController(index)
            if (controller != nil) {
                let startingViewControllers: NSArray = [controller!]
                self.dataSource = nil
                self.dataSource = self
                self.setViewControllers(startingViewControllers as? [UIViewController], direction: UIPageViewControllerNavigationDirection.Forward, animated: false) { (finished) in
                    
                }

//                self.setViewControllers(startingViewControllers as? [UIViewController], direction: UIPageViewControllerNavigationDirection.Forward, animated: true) { (finished) in
//                    if finished {
//                        dispatch_async(dispatch_get_main_queue(),{
//                            weakSelf!.setViewControllers(startingViewControllers as? [UIViewController], direction: UIPageViewControllerNavigationDirection.Forward, animated: false, completion: nil)
//                        })
//                    }
//                }
            }
        } catch {
            
        }
    }
    
    
    func loadWord(notification: NSNotification){
        weak var weakSelf = self
        dispatch_async(dispatch_get_main_queue(),{
            //load data here
            let word = notification.object as! String
            weakSelf!.dataSource = nil
            do {
                let wc = try weakSelf!.lessonDbAdapter.findByWord(word)
                Logger.log("Load graph for word \(wc.word). Pronunciation: \(wc.getArpabetList())")
                weakSelf!.word = word
                weakSelf!.arpabets = wc.getArpabetList()
                weakSelf!.dataSource = weakSelf!
                weakSelf!.loadView()
                weakSelf!.loadViewController(0)
            } catch {
                
            }
        })
        
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