//
//  AnalyzingView.swift
//  AccentEasy
//
//  Created by Hai Lu on 17/02/2016.
//  Copyright © 2016 Hai Lu. All rights reserved.
//

import Foundation
import EZAudio


protocol AnalyzingDelegate {
    func onStopRecording()
    func onAnimationMax()
    func onAnimationMin()
}

public class AnalyzingView: EZPlot, EZAudioDisplayLinkDelegate {
    var kFontSize:CGFloat!
    let kMaxRecordingTime:Double = 4000
    let kMinRecordingTime:Double = 1000
    let kPitchTimeOut:Double = 1000
    let kAcceptedRadioPitch:CGFloat = 0.09
    
    let kEZAudioPlotMaxHistoryBufferLength:Int32 = 8192
    let kEZAudioPlotDefaultHistoryBufferLength:Int32 = 512
    let EZAudioPlotDefaultHistoryBufferLength:Int32 = 512
    let EZAudioPlotDefaultMaxHistoryBufferLength:Int32 = 8192
    let radio:CGFloat = (1 / 0.8)
    
    var shouldOptimizeForRealtimePlot: Bool = true
    var shouldCenterYAxis: Bool = true
    var waveformLayer: RecordingViewWaveformLayer!
    var innerCircleLayer: RecordingViewCircleLayer!
    var outerCircleLayer: RecordingViewCircleLayer!
    var scoreLayer: LCTextLayer!
    var loadingLayer: CALayer!
    
    var displayLink: EZAudioDisplayLink!
    var historyInfo: UnsafeMutablePointer<EZPlotHistoryInfo>!
    var points: UnsafeMutablePointer<CGPoint>!
    var pointCount: Int!
    var lastMaxTime: Double!
    var lastMax: CGFloat!
    var lastMaxValue: CGFloat!
    
    
    
    var recordingTimestamp: Double!
    var checkClose = false
    
    var score: Int = 0
    var originScore: Int = 0
    
    var type: AnalyzingType = AnalyzingType.DEFAULT
    
    var delegate: AnalyzingDelegate!
    
    var isRecording:Bool = false
    
    var animationState = AnimationState.DEFAULT
    
    var lastUpdateAnimationTime: Double = 0.0
    
    public init() {
        super.init(frame:CGRectZero)
        self.initPlot()
    }
    
    public override init(frame: CGRect) {
        super.init(frame: frame)
        self.initPlot()
    }
    
    public required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        self.initPlot()
    }
    
    deinit {
        EZAudioUtilities.freeHistoryInfo(historyInfo)
        free(self.points);
        self.displayLink.stop()
    }
    
    public func refreshLayout() {
        scoreLayer.frame = self.bounds
        let rect = getLoadingFrame(inRect: self.frame)
        self.loadingLayer.frame = rect
    }
    
    func initPlot() {
        self.displayLink = EZAudioDisplayLink(delegate: self)
        self.displayLink.start()
        
        self.gain = 2.0
        self.plotType = EZPlotType.Buffer
        self.shouldMirror = false
        self.shouldFill = false
        
        // Setup history window
        self.resetHistoryBuffers()
        
        scoreLayer = LCTextLayer()
        
        scoreLayer.foregroundColor = UIColor.whiteColor().CGColor
        scoreLayer.frame = self.bounds
        scoreLayer.alignmentMode = kCAAlignmentCenter
        scoreLayer.contentsScale = UIScreen.mainScreen().scale
        
        self.waveformLayer = RecordingViewWaveformLayer()
        self.waveformLayer.frame = self.bounds
        self.waveformLayer.lineWidth = 2.6
        self.waveformLayer.strokeColor = UIColor.whiteColor().CGColor
        self.waveformLayer.fillColor = nil
        self.waveformLayer.backgroundColor = nil
        self.waveformLayer.opaque = true
        
        self.innerCircleLayer = RecordingViewCircleLayer()
        self.innerCircleLayer.frame = self.bounds
        self.innerCircleLayer.lineWidth = 1.0
        
        self.innerCircleLayer.backgroundColor = nil
        self.innerCircleLayer.opaque = true
        
        self.outerCircleLayer = RecordingViewCircleLayer()
        self.outerCircleLayer.frame = self.bounds
        self.outerCircleLayer.lineWidth = 1.0
        self.outerCircleLayer.backgroundColor = nil
        self.outerCircleLayer.opaque = true
        
        self.color = UIColor(hue: 0, saturation: 1.0, brightness: 1.0, alpha: 1.0)
        self.backgroundColor = nil
        
        self.loadingLayer = CALayer()
        self.loadingLayer.backgroundColor = UIColor.clearColor().CGColor
        let rect = getLoadingFrame(inRect: self.frame)
        self.loadingLayer.frame = rect
        self.loadingLayer.contents = ImageHelper.imageWithImage(UIImage(named: "p_hour_glass.png")!, scaledToSize: CGSize(width: CGRectGetWidth(rect), height: CGRectGetHeight(rect))).CGImage
        
        
        self.layer.addSublayer(self.outerCircleLayer)
        self.layer.addSublayer(self.innerCircleLayer)
        self.layer.addSublayer(self.waveformLayer)
        self.layer.addSublayer(self.scoreLayer)
        self.layer.addSublayer(self.loadingLayer)
        //
        // Allow subclass to initialize plot
        //
        self.setupPlot()
        self.points = UnsafeMutablePointer<CGPoint>(calloc(Int(EZAudioPlotDefaultMaxHistoryBufferLength), sizeof(CGPoint)));
        self.pointCount = self.initialPointCount()
        self.redraw()

    }
    
    func switchType(_type: AnalyzingType) {
        switch _type {
        case .DEFAULT, .DISABLE:
            animationState = AnimationState.DEFAULT
            break
        case .ANALYZING, .SEARCHING:
            self.score = 0
            self.originScore = 100
            animationState = AnimationState.WAIT_FOR_MAX
            break
        case .RECORDING:
            animationState = AnimationState.DEFAULT
            break
        case .SHOW_SCORE:
            break
        }
        self.type = _type
        self.redraw()
    }
    
    func showScore(_score: Int, showAnimation: Bool = true) {
        if animationState != .WAIT_FOR_MIN {
            self.score = 0
        }
        if showAnimation {
            animationState = AnimationState.WAIT_FOR_MAX
        } else {
            self.score = _score
            animationState = AnimationState.DEFAULT
        }
        self.originScore = _score
        
        switchType(AnalyzingType.SHOW_SCORE)
    }
    
    func resetHistoryBuffers() {
        //
        // Clear any existing data
        //
        if self.historyInfo != nil
        {
            EZAudioUtilities.freeHistoryInfo(self.historyInfo)
        }
        
        self.historyInfo = EZAudioUtilities.historyInfoWithDefaultLength(
            self.defaultRollingHistoryLength(),
            maximumLength: self.maximumRollingHistoryLength())}
    
    override public func layoutSubviews() {
        super.layoutSubviews()
        CATransaction.begin()
        CATransaction.setDisableActions(true)
        self.waveformLayer.frame = self.bounds
        self.innerCircleLayer.frame = self.bounds
        self.outerCircleLayer.frame = self.bounds
        kFontSize = self.frame.width * 64 / 200
        scoreLayer.fontSize = kFontSize
        scoreLayer.font = UIFont.boldSystemFontOfSize(kFontSize)
        refreshLayout()
        self.redraw()
        CATransaction.commit()
    }
    
    func setupPlot() {
        
    }
    //------------------------------------------------------------------------------
    // pragma mark - Drawing
    //------------------------------------------------------------------------------
    
    public override func clear() {
        super.clear()
        recordingTimestamp =  NSDate().timeIntervalSince1970 * 1000.0
        checkClose = false
        isRecording = true
        if self.pointCount > 0 {
            self.resetHistoryBuffers()
            let data = UnsafeMutablePointer<Float>.alloc(self.pointCount)
            data.initializeFrom(Repeat(count: self.pointCount, repeatedValue: 0))
            self.setSampleData(data,length:Int32(self.pointCount))
            self.redraw()
        }
    }
    
    //------------------------------------------------------------------------------
    
    private func redraw()
    {
        let currentTime: Double = NSDate().timeIntervalSince1970 * 1000.0
        let updateTime = 100.0
        let step = 4
        if currentTime - lastUpdateAnimationTime > updateTime {
            if (self.score >= self.originScore && animationState == .WAIT_FOR_MAX) {
                self.score = self.originScore
                delegate?.onAnimationMax()
                if type == .SHOW_SCORE {
                    animationState = .DEFAULT
                } else if type == .ANALYZING || type == .SEARCHING {
                    animationState = .WAIT_FOR_MIN
                }
            } else if (self.score <= 0 && animationState == .WAIT_FOR_MIN) {
                self.score = 0
                delegate?.onAnimationMin()
                if type == .ANALYZING {
                    animationState = .WAIT_FOR_MAX
                } else if type == .SEARCHING {
                    switchType(.DEFAULT)
                }
            }
            CATransaction.begin()
            CATransaction.setDisableActions(true)
            if (self.score > 9) {
                scoreLayer.string = String(self.score)
            } else if (self.score >= 0) {
                scoreLayer.string = "0\(self.score)"
            }
            CATransaction.commit()
            switch(animationState) {
            case .DEFAULT:
                break
            case .WAIT_FOR_MAX:
                self.score += step
                break
            case .WAIT_FOR_MIN:
                self.score -= step
                break
            }
            
            lastUpdateAnimationTime = currentTime
        }
        
        self.updateState()
        let frame: EZRect = self.waveformLayer.frame
        
        let path: CGPathRef = self.createPathWithPoints(self.points, pointCount:self.pointCount, inRect:frame)
        
        let innerCirclePath: CGPathRef = self.createInnerCirclePathWithPoints(self.points, pointCount:self.pointCount, inRect:frame)
        
        let outerCirclePath: CGPathRef = self.createOuterCirclePathWithPoints(self.points, pointCount:self.pointCount, inRect:frame)
        
        if self.shouldOptimizeForRealtimePlot {
            CATransaction.begin()
            CATransaction.setDisableActions(true)
            self.waveformLayer.path = path;
            self.innerCircleLayer.path = innerCirclePath;
            self.outerCircleLayer.path = outerCirclePath;
            CATransaction.commit()
        } else {
            self.waveformLayer.path = path;
            self.innerCircleLayer.path = innerCirclePath;
            self.outerCircleLayer.path = outerCirclePath;
        }
        
    }
    
    private func updateState() {
        switch (type) {
        case .DEFAULT:
            self.outerCircleLayer.fillColor = Multimedia.colorWithHexString("#FFDDBE").CGColor
            self.innerCircleLayer.fillColor = Multimedia.colorWithHexString("#FFB1AD").CGColor
            self.waveformLayer.hidden = true;
            self.innerCircleLayer.hidden = false;
            self.outerCircleLayer.hidden = true;
            self.scoreLayer.hidden = true;
            self.loadingLayer.hidden = true
            break;
        case .DISABLE:
            self.outerCircleLayer.fillColor = ColorState.STATE_GRAY_COLOR.outerBackground.CGColor
            self.innerCircleLayer.fillColor = ColorState.STATE_GRAY_COLOR.innerBackground.CGColor
            self.waveformLayer.hidden = true;
            self.innerCircleLayer.hidden = false;
            self.outerCircleLayer.hidden = true;
            self.scoreLayer.hidden = true;
            self.loadingLayer.hidden = true
            break
        case .RECORDING:
            self.outerCircleLayer.fillColor = Multimedia.colorWithHexString("#FFDDBE").CGColor
            self.innerCircleLayer.fillColor = Multimedia.colorWithHexString("#EC2221").CGColor
            self.waveformLayer.hidden = false;
            self.innerCircleLayer.hidden = false;
            self.outerCircleLayer.hidden = false;
            self.scoreLayer.hidden = true;
            self.loadingLayer.hidden = true
            break;
        case .SHOW_SCORE, .ANALYZING, .SEARCHING:
            var fromColorState: ColorState!
            var toColorState: ColorState!
            let radio = CGFloat(score) / 100.0
            if (radio <= 0.5) {
                fromColorState = ColorState.STATE_RED_COLOR
                toColorState = ColorState.STATE_ORANGE_COLOR
            } else {
                fromColorState = ColorState.STATE_ORANGE_COLOR
                toColorState = ColorState.STATE_GREEN_COLOR
            }
            self.outerCircleLayer.fillColor = ColorHelper.generateGradientColor(toColorState.outerBackground, color2: fromColorState.outerBackground, radio: radio).CGColor
            self.innerCircleLayer.fillColor = ColorHelper.generateGradientColor(toColorState.innerBackground, color2: fromColorState.innerBackground, radio: radio).CGColor
            
            self.waveformLayer.hidden = true;
            self.innerCircleLayer.hidden = false;
            self.outerCircleLayer.hidden = false;
            if type == .SEARCHING {
                self.scoreLayer.hidden = true;
                self.loadingLayer.hidden = false
            } else {
                self.scoreLayer.hidden = false;
                self.loadingLayer.hidden = true
            }
            break;
        }
    }
    
    private func getLoadingFrame(inRect rect: EZRect) -> CGRect {
        let width: CGFloat = min(rect.size.width, rect.size.height);
        let radio = self.radio * 0.8
        return CGRectMake(width / 2 - radio * width/4, width / 2 - radio * width/4, radio * width/2, radio * width/2)
    }
        
    private func createInnerCirclePathWithPoints(points: UnsafeMutablePointer<CGPoint>,
        pointCount: Int,
        inRect rect: EZRect) -> CGPathRef
    {
        let width: CGFloat = min(rect.size.width, rect.size.height);
        return UIBezierPath(roundedRect: CGRectMake(width / 2 - radio * width/4, width / 2 - radio * width/4, radio * width/2, radio * width/2),
            cornerRadius: radio * width/4).CGPath
    }
        
    private func createOuterCirclePathWithPoints(points: UnsafeMutablePointer<CGPoint>,
        pointCount: Int,
        inRect rect: EZRect) -> CGPathRef
    {
        let timeout: Double = kPitchTimeOut
        let width: CGFloat = min(rect.size.width, rect.size.height);
        var maxY: CGFloat = 0.0;
        for i in 0...pointCount
        {
            maxY = max(points[i].y, maxY);
        }
        maxY = min(maxY * CGFloat(self.gain), 1.0);
        
        let currentTime: Double = NSDate().timeIntervalSince1970 * 1000.0
        if (maxY > self.lastMax) {
            self.lastMaxTime = currentTime;
            self.lastMax = maxY;
            self.lastMaxValue = maxY;
            if (maxY > kAcceptedRadioPitch) {
                checkClose = false
            }
        }
        maxY = self.lastMax;
        if (type == .SHOW_SCORE || type == .ANALYZING || type == .SEARCHING) {
            maxY = CGFloat(score) / 100.0
        }
        let scaleX: CGFloat = (1.0 - maxY) * (width / 2 - radio * width/4);
        let scaleWidth: CGFloat = width - scaleX * 2;
        let scaleTime: Double = currentTime - self.lastMaxTime;
        
        if scaleTime >= Double(timeout) {
            self.lastMax = 0;
            self.lastMaxValue = 0;
            if (recordingTimestamp != nil && currentTime - recordingTimestamp > kMinRecordingTime) {
                checkClose = true
            }
        } else {
            self.lastMax = (1 - CGFloat(scaleTime / timeout)) * self.lastMaxValue;
            //NSLog(@"Last max %f - %f - %f", (double)scaleTime, self.lastMax, self.lastMaxValue);
        }
        if recordingTimestamp != nil && ((currentTime - recordingTimestamp > kMaxRecordingTime) || checkClose) {
            if type == AnalyzingType.RECORDING {
                NSLog("Auto stop recording")
                delegate!.onStopRecording()
            }
        }
        return UIBezierPath(roundedRect: CGRectMake(scaleX, scaleX, scaleWidth, scaleWidth),
            cornerRadius: scaleWidth/2).CGPath
    }
    
    //------------------------------------------------------------------------------
    
    private func createPathWithPoints(points: UnsafeMutablePointer<CGPoint>,
        pointCount: Int,
        inRect rect: EZRect) -> CGPathRef
    {
        let path: CGMutablePathRef = CGPathCreateMutable()
        let width: CGFloat = radio * min(rect.size.width, rect.size.height) / 2
        if (pointCount > 0)
        {
            let xscale: Double = Double(width) / Double(self.pointCount)
            let halfHeight: Double = floor(Double(width) / 2.0)
            let deviceOriginFlipped:Int = self.isDeviceOriginFlipped() ? -1 : 1
            var xf: CGAffineTransform = CGAffineTransformIdentity
            var translateY: CGFloat = 0.0
            if !self.shouldCenterYAxis {
                translateY = rect.size.width / 2 + width / 2
            } else {
                translateY = rect.size.width / 2 + rect.origin.y
            }
            xf = CGAffineTransformTranslate(xf, rect.size.width / 2 - radio * rect.size.width / 4, translateY);
            var yScaleFactor: Double = halfHeight;
            if !self.shouldCenterYAxis
            {
                yScaleFactor = 2.0 * halfHeight;
            }
            xf = CGAffineTransformScale(xf, CGFloat(xscale), CGFloat(Double(deviceOriginFlipped) * yScaleFactor));
            CGPathAddLines(path, &xf, self.points, self.pointCount);
            if (self.shouldMirror) {
                xf = CGAffineTransformScale(xf, 1.0, -1.0);
                CGPathAddLines(path, &xf, self.points, self.pointCount);
            }
            if (self.shouldFill) {
                CGPathCloseSubpath(path);
            }
        }
        return path;
    }
    
    //------------------------------------------------------------------------------
    // Update
    //------------------------------------------------------------------------------
    
    public override func updateBuffer(buffer: UnsafeMutablePointer<Float>, withBufferSize bufferSize:UInt32)
    {
        // append the buffer to the history
        EZAudioUtilities.appendBufferRMS(buffer, withBufferSize:bufferSize, toHistoryInfo:self.historyInfo)
        // copy samples
        switch (self.plotType)
        {
            case EZPlotType.Buffer:
                self.setSampleData(buffer, length:Int32(bufferSize))
                break;
            case EZPlotType.Rolling:
                self.setSampleData(self.historyInfo[0].buffer, length:self.historyInfo[0].bufferSize)
                break;
        }
        
        // update drawing
        if !self.shouldOptimizeForRealtimePlot
        {
            self.redraw()
        }
    }
    
    //------------------------------------------------------------------------------
    
    func setSampleData(data: UnsafeMutablePointer<Float>, length: Int32)
    {
        let points = self.points;
        for i in 0...Int(length) {
            points[i].x = CGFloat(i)
            points[i].y = CGFloat(data[i] * self.gain)
        }
        points[0].y = 0.0
        points[length - 1].y = 0
        self.pointCount = Int(length)
    }
    
    //------------------------------------------------------------------------------
    // Adjusting History Resolution
    //------------------------------------------------------------------------------
    
    func rollingHistoryLength() -> Int32
    {
        return self.historyInfo[0].bufferSize
    }
    
    //------------------------------------------------------------------------------
    
    func setRollingHistoryLength(historyLength: Int32) -> Int32
    {
        self.historyInfo[0].bufferSize = min(EZAudioPlotDefaultMaxHistoryBufferLength, historyLength);
        return self.historyInfo[0].bufferSize;
    }
    
    //------------------------------------------------------------------------------
    // Subclass
    //------------------------------------------------------------------------------
    
    func defaultRollingHistoryLength() -> Int32
    {
        return EZAudioPlotDefaultHistoryBufferLength;
    }
    
    //------------------------------------------------------------------------------
    
    func initialPointCount() -> Int
    {
        return 100;
    }
    
    //------------------------------------------------------------------------------
    
    func maximumRollingHistoryLength() -> Int32
    {
        return EZAudioPlotDefaultMaxHistoryBufferLength;
    }
    
    //------------------------------------------------------------------------------
    // Utility
    //------------------------------------------------------------------------------
    
    func isDeviceOriginFlipped() -> Bool
    {
        return true
    }

    public func displayLinkNeedsDisplay(displayLink: EZAudioDisplayLink!) {

        self.redraw()
    }
}

class RecordingViewWaveformLayer : CAShapeLayer {
    
    override func actionForKey(event: String) -> CAAction? {

        if event == "path" {
            if CATransaction.disableActions() {
                return nil;
            } else {
                let animation = CABasicAnimation()
                animation.timingFunction = CATransaction.animationTimingFunction()
                animation.duration = CATransaction.animationDuration()
                return animation;
            }
        }
        return super.actionForKey(event)
    }
}

class RecordingViewCircleLayer : CAShapeLayer {
    var origin: CGPoint?
    var radius: CGFloat?
    var locations: [CGFloat]?
    var colors: [UIColor]?
    
    override func drawInContext(ctx: CGContext) {
        super.drawInContext(ctx)
        if let colors = self.colors {
            if let locations = self.locations {
                if let origin = self.origin {
                    if let radius = self.radius {
                        var colorSpace: CGColorSpaceRef?
                        var components = [CGFloat]()
                        for i in 0 ..< colors.count {
                            let colorRef = colors[i].CGColor
                            let colorComponents = CGColorGetComponents(colorRef)
                            let numComponents = CGColorGetNumberOfComponents(colorRef)
                            if colorSpace == nil {
                                colorSpace = CGColorGetColorSpace(colorRef)
                            }
                            for j in 0 ..< numComponents {
                                var componentIndex: Int = numComponents * i + j
                                let component = colorComponents[j]
                                components.append(component)
                            }
                        }
                        
                        if let colorSpace = colorSpace {
                            let gradient = CGGradientCreateWithColorComponents(colorSpace, components, locations, locations.count)
                            CGContextDrawRadialGradient(ctx, gradient, origin, CGFloat(0), origin, radius, CGGradientDrawingOptions.DrawsAfterEndLocation)
                        }
                    }
                }
            }
        }
    }
}

public enum AnimationState : Int {
    case DEFAULT
    
    case WAIT_FOR_MAX
    
    case WAIT_FOR_MIN
}

public enum AnalyzingType : Int {
    
    case DEFAULT
    
    case DISABLE
    
    case RECORDING
    
    case SEARCHING
    
    case ANALYZING
    
    case SHOW_SCORE
}

public class ColorState {
    static let STATE_GREEN_COLOR = ColorState(
            innerBorder: Multimedia.colorWithHexString("#71b038"),
            innerBackground: Multimedia.colorWithHexString("#9cca7c"),
        outerBorder: Multimedia.colorWithHexString("#a4ea96"),
        outerBackground: Multimedia.colorWithHexString("#d6f6d1"))
    
    static let STATE_ORANGE_COLOR = ColorState(
        innerBorder: Multimedia.colorWithHexString("#ff916a"),
        innerBackground: Multimedia.colorWithHexString("#ffbfa1"),
        outerBorder: Multimedia.colorWithHexString("#ffc993"),
        outerBackground: Multimedia.colorWithHexString("#ffe9d2"))
    
    static let STATE_RED_COLOR = ColorState(
        innerBorder: Multimedia.colorWithHexString("#ff4848"),
        innerBackground: Multimedia.colorWithHexString("#ff9c9c"),
        outerBorder: Multimedia.colorWithHexString("#ffd9d9"),
        outerBackground: Multimedia.colorWithHexString("#ffe7e7"))
    
    static let STATE_GRAY_COLOR = ColorState(
        innerBorder: Multimedia.colorWithHexString("#868686"),
        innerBackground: Multimedia.colorWithHexString("#929292"),
        outerBorder: Multimedia.colorWithHexString("#9e9e9e"),
        outerBackground: Multimedia.colorWithHexString("#aaaaaa"))
    
    var innerBorder: UIColor
    var innerBackground: UIColor
    var outerBorder: UIColor
    var outerBackground: UIColor
    
    init(innerBorder: UIColor, innerBackground: UIColor, outerBorder: UIColor, outerBackground: UIColor) {
        self.innerBorder = innerBorder
        self.innerBackground = innerBackground
        self.outerBorder = outerBorder
        self.outerBackground = outerBackground
    }
}

class LCTextLayer : CATextLayer {
    
    // REF: http://lists.apple.com/archives/quartz-dev/2008/Aug/msg00016.html
    // CREDIT: David Hoerl - https://github.com/dhoerl
    // USAGE: To fix the vertical alignment issue that currently exists within the CATextLayer class. Change made to the yDiff calculation.
    
    override init() {
        super.init()
    }
    
    override init(layer: AnyObject) {
        super.init(layer: layer)
    }
    
    required init(coder aDecoder: NSCoder) {
        super.init(layer: aDecoder)
    }
    
    override func drawInContext(ctx: CGContext) {
        let height = self.bounds.size.height
        let fontSize = self.fontSize
        let yDiff = (height-fontSize)/2 - fontSize/10
        
        CGContextSaveGState(ctx)
        CGContextTranslateCTM(ctx, 0.0, yDiff)
        super.drawInContext(ctx)
        CGContextRestoreGState(ctx)
    }
}
