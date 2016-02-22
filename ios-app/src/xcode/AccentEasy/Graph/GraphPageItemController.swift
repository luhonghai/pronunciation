//
//  GraphPageItemController.swift
//  AccentEasy
//
//  Created by CMG on 04/02/2016.
//  Copyright © 2016 Hoang Nguyen. All rights reserved.
//

import UIKit

class GraphPageItemController: UIViewController, ChartViewDelegate {

    @IBOutlet var chartView: LineChartView!
    
    var itemIndex: Int = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        initChart()
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
        var des: String = ""
        switch (itemIndex) {
        case 0:
            des = "HELLO"
            break
        case 1:
            des = "HH"
            break
        case 2:
            des = "AX"
            break
        case 3:
            des = "L"
            break
        case 4:
            des = "OW"
            break
        default:
            break
        }
        chartView.descriptionText = des
        chartView.descriptionTextColor = ColorHelper.APP_LIGHT_GRAY
        chartView.descriptionFont = UIFont.boldSystemFontOfSize(25.0)
    }
    
    func setDataCount(count: Int, range: Double) {
        var xVals = [String]()
        for index in 0...count {
            xVals.append("\(index)")
        }
        var yVals = [ChartDataEntry]()
        var lastVal: Double = 0
        for index in 0...count {
            let mult = range + 1
            var val = arc4random_uniform(UInt32(mult)) + 3
            val = max(0, val)
            val = min(100, val)
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
        
        self.chartView.data = chartData
    }
    
    func chartValueSelected(chartView: ChartViewBase, entry: ChartDataEntry, dataSetIndex: Int, highlight: ChartHighlight) {
         NSLog("chartValueSelected");
    }
    
    func chartValueNothingSelected(chartView: ChartViewBase) {
        NSLog("chartValueNothingSelected");
    }
}