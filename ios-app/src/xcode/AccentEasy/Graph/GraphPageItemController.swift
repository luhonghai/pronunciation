//
//  GraphPageItemController.swift
//  AccentEasy
//
//  Created by CMG on 04/02/2016.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class GraphPageItemController: UIViewController, ChartViewDelegate {
    
    @IBOutlet var chartView: LineChartView!
    
    var data = ""

    var isWord = true
    
    var itemIndex = 0
    
    var freestyleAdapter: FreeStyleDBAdapter!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        freestyleAdapter = FreeStyleDBAdapter()
        if (!data.isEmpty) {
            chartView.hidden = false
            initChart()
        } else {
            chartView.hidden = true
        }
    }
    
    func initChart() {
        chartView.rightAxis.enabled = false
        chartView.legend.enabled = false
        chartView.leftAxis.drawAxisLineEnabled = false
        chartView.leftAxis.drawGridLinesEnabled = false
        chartView.leftAxis.drawLabelsEnabled = false
        chartView.rightAxis.drawAxisLineEnabled = false
        chartView.rightAxis.drawGridLinesEnabled = false
        chartView.rightAxis.drawLabelsEnabled = false
        chartView.xAxis.drawAxisLineEnabled = false
        chartView.xAxis.drawGridLinesEnabled = false
        chartView.xAxis.drawLabelsEnabled = false
        chartView.xAxis.enabled = false
        chartView.drawGridBackgroundEnabled = false
        chartView.dragEnabled = false
        chartView.pinchZoomEnabled = false
        chartView.scaleXEnabled = false
        chartView.scaleYEnabled = false
        chartView.highlightPerTapEnabled = false
        chartView.doubleTapToZoomEnabled = false
        addLimitLine()
        
        updateChartDescription()
        setDataCount(30, range: 100)
    }
    
    func addLimitLine() {
        let ll100 = ChartLimitLine(limit: 100, label: "")
        ll100.lineWidth = 1.0
        ll100.lineColor = ColorHelper.APP_GRAY
        
        let ll50 = ChartLimitLine(limit: 50, label: "")
        ll50.lineWidth = 1.0
        ll50.lineColor = ColorHelper.APP_GRAY
        
        let ll0 = ChartLimitLine(limit: 0, label: "")
        ll0.lineWidth = 1.0
        ll0.lineColor = ColorHelper.APP_GRAY
        
        let leftAxis = chartView.leftAxis
        leftAxis.removeAllLimitLines()
        leftAxis.addLimitLine(ll100)
        leftAxis.addLimitLine(ll50)
        leftAxis.addLimitLine(ll0)
        leftAxis.customAxisMax = 100
        leftAxis.customAxisMin = 0
        leftAxis.drawLimitLinesBehindDataEnabled = true
        
    }
    
    func updateChartDescription() {
        let des: String = data.uppercaseString
        chartView.descriptionText = des
        chartView.descriptionTextColor = ColorHelper.APP_LIGHT_GRAY
        chartView.descriptionFont = UIFont.boldSystemFontOfSize(25.0)
    }
    
    func setDataCount(count: Int, range: Double) {
        weak var weakSelf = self
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)) {
            var data = [Int]()
            let username = Login.getCurrentUser().username
            do {
                weakSelf!.freestyleAdapter.changeDbFile((GlobalData.getInstance().isOnLessonMain ? DatabaseHelper.getLessonUserHistoryDatabaseFile() : DatabaseHelper.getFreeStyleDatabaseFile())!)
                if (weakSelf!.isWord) {
                    let histories = try weakSelf!.freestyleAdapter.listPronunciationScore(weakSelf!.data, limit: count, username: username)
                    if !histories.isEmpty {
                        for history in histories.reverse() {
                            data.append(history.score)
                        }
                    }
                } else {
                    let histories = try weakSelf!.freestyleAdapter.listPhonemeScore(weakSelf!.data, limit: count, username: username)
                    if !histories.isEmpty {
                        for history in histories.reverse() {
                            data.append(history.score)
                        }
                    }
                }
            } catch {
                
            }
            var xVals = [String]()
            for index in 0...count {
                xVals.append("\(index)")
            }
            var yVals = [ChartDataEntry]()
            var lastVal: Double = 0
            let skipIndex = count - data.count - 1
            //Logger.log("data count \(data.count). Skip index: \(skipIndex)")
            for index in 0...count {
                var val:Int = -1
                if (index > skipIndex + 1) {
                    let i:Int = index - skipIndex - 2
                    // Logger.log("index \(i)")
                    val = data[i]
                }
                if index == count {
                    lastVal = Double(val)
                }
                let dataEntry = ChartDataEntry(value: Double(val), xIndex: index)
                yVals.append(dataEntry)
            }
            
            let ds = LineChartDataSet(yVals: yVals, label: "")
            var chartColor: UIColor = UIColor.whiteColor()
            if lastVal >= 80 {
                chartColor = ColorHelper.APP_GREEN
            } else if lastVal >= 45 {
                chartColor = ColorHelper.APP_ORANGE
            } else {
                chartColor = ColorHelper.APP_RED
            }
            ds.setColor(chartColor)
            ds.setCircleColor(chartColor)
            ds.lineWidth = 2.0
            ds.circleRadius = 4.0
            ds.drawCircleHoleEnabled = false
            ds.drawValuesEnabled = false
            let chartData = LineChartData(xVals: xVals, dataSet: ds)
            dispatch_async(dispatch_get_main_queue(),{
                if weakSelf != nil && weakSelf!.chartView != nil {
                    weakSelf!.chartView.data = chartData
                }
            })

        }
    
    }
    
    func chartValueSelected(chartView: ChartViewBase, entry: ChartDataEntry, dataSetIndex: Int, highlight: ChartHighlight) {
         NSLog("chartValueSelected");
    }
    
    func chartValueNothingSelected(chartView: ChartViewBase) {
        NSLog("chartValueNothingSelected");
    }
}