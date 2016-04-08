//
//  ScatterChartData.swift
//  Charts
//
//  Created by Daniel Cohen Gindi on 26/2/15.
//
//  Copyright 2015 Daniel Cohen Gindi & Philipp Jahoda
//  A port of MPAndroidChart for iOS
//  Licensed under Apache License 2.0
//
//  https://github.com/danielgindi/ios-charts
//

import UIKit

public class ScatterChartData: BarLineScatterCandleBubbleChartData
{
    public override init()
    {
        super.init()
    }
    
    public override init(xVals: [String?]?, dataSets: [IChartDataSet]?)
    {
        super.init(xVals: xVals, dataSets: dataSets)
    }
    
    public override init(xVals: [NSObject]?, dataSets: [IChartDataSet]?)
    {
        super.init(xVals: xVals, dataSets: dataSets)
    }
    
    /// - returns: the maximum shape-size across all DataSets.
    public func getGreatestShapeSize() -> CGFloat
    {
        var max = CGFloat(0.0)
        
        for set in _dataSets
        {
            let scatterDataSet = set as? IScatterChartDataSet
            
            if (scatterDataSet == nil)
            {
                Logger.log("ScatterChartData: Found a DataSet which is not a ScatterChartDataSet")
            }
            else
            {
                let size = scatterDataSet!.scatterShapeSize
                
                if (size > max)
                {
                    max = size
                }
            }
        }
        
        return max
    }
}