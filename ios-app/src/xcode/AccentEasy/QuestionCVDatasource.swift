//
//  QuestionCVDatasource.swift
//  AccentEasy
//
//  Created by CMGVN on 3/29/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import Foundation

@objc protocol QuestionCVDatasourceDelegate {
    optional func selectedCellQuestionInDetail(cellIndex:Int)
    optional func backToMain()
}

extension QuestionCVDatasourceDelegate{
    
}

class QuestionCVDatasource: NSObject, UICollectionViewDataSource, UICollectionViewDelegate {
    
    var delegateMain:QuestionCVDatasourceDelegate?
    var delegateDetail:QuestionCVDatasourceDelegate?
    
    var arrQuestions = [AEQuestion]()
    
    var selectedQuestion: AEQuestion!
    
    func setSelected(index: Int) {
        selectedQuestion = arrQuestions[index]
    }
    
    func numberOfSectionsInCollectionView(collectionView: UICollectionView) -> Int {
        return 1
    }
    
    func collectionView(collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return arrQuestions.count
    }
    
    func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCellWithReuseIdentifier("questionCell", forIndexPath: indexPath) as! QuestionCVCell
        
        cell.lblQuestion.layer.cornerRadius = cell.lblQuestion.frame.size.height/2
        cell.lblQuestion.clipsToBounds = true
        
        let question = arrQuestions[indexPath.item]
        
        if !question.recorded && question.enabled {
            cell.lblQuestion.text = "Q\(indexPath.item+1)"
            cell.lblQuestion.backgroundColor = ColorHelper.APP_PURPLE
        } else if question.recorded && question.enabled {
            let averageScore:Int = Int(question.listScore.average)
            cell.lblQuestion.text = String(averageScore)
            cell.lblQuestion.backgroundColor = questionCVChangeColor(averageScore)
        } else {
            cell.lblQuestion.text = "Q\(indexPath.item+1)"
            cell.lblQuestion.backgroundColor = ColorHelper.APP_GRAY
            cell.userInteractionEnabled = false
        }
        
        return cell
    }
    
    func questionCVChangeColor(score:Int) -> UIColor{
        if score < 45 {
            //color < 45 red
            return ColorHelper.APP_RED
        } else if score >= 45 && score < 80 {
            // 45 <= color < 80 orange
            return ColorHelper.APP_ORANGE
        } else {
            //color >= 80 green
            return ColorHelper.APP_GREEN
        }
        
    }
    
    // MARK: - UICollectionViewDelegate protocol
    func collectionView(collectionView: UICollectionView, didSelectItemAtIndexPath indexPath: NSIndexPath) {
        // handle tap events
        Logger.log("You selected cell #\(indexPath.item)!")
        delegateMain?.selectedCellQuestionInDetail?(indexPath.item)
        delegateDetail?.backToMain?()
        //self.navigationController?.popViewControllerAnimated(true)
    }
    
}