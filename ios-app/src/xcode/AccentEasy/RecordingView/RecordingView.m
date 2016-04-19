//
//
//  Created by Hai Lu on 26/1/16.
//  Copyright (c) 2016 CMG. All rights reserved.
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files (the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//  THE SOFTWARE.

#import "RecordingView.h"

//------------------------------------------------------------------------------
#pragma mark - Constants
//------------------------------------------------------------------------------

UInt32 const kEZAudioPlotMaxHistoryBufferLength = 8192;
UInt32 const kEZAudioPlotDefaultHistoryBufferLength = 512;
UInt32 const EZAudioPlotDefaultHistoryBufferLength = 512;
UInt32 const EZAudioPlotDefaultMaxHistoryBufferLength = 8192;
float const radio = 1 / (float) 0.8;

//------------------------------------------------------------------------------
#pragma mark - RecordingView (Implementation)
//------------------------------------------------------------------------------

@implementation RecordingView

//------------------------------------------------------------------------------
#pragma mark - Dealloc
//------------------------------------------------------------------------------

- (void)dealloc
{
    [EZAudioUtilities freeHistoryInfo:self.historyInfo];
    free(self.points);
}

//------------------------------------------------------------------------------
#pragma mark - Initialization
//------------------------------------------------------------------------------

- (id)init
{
    self = [super init];
    if (self)
    {
        [self initPlot];
    }
    return self;
}

- (id)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self)
    {
        [self initPlot];
    }
    return self;
}

#if TARGET_OS_IPHONE
- (id)initWithFrame:(CGRect)frameRect
#elif TARGET_OS_MAC
- (id)initWithFrame:(NSRect)frameRect
#endif
{
    self = [super initWithFrame:frameRect];
    if (self)
    {
        [self initPlot];
    }
    return self;
}

#if TARGET_OS_IPHONE
- (void)layoutSubviews
{
    [super layoutSubviews];
    [CATransaction begin];
    [CATransaction setDisableActions:YES];
    self.waveformLayer.frame = self.bounds;
    self.innerCircleLayer.frame = self.bounds;
    self.outerCircleLayer.frame = self.bounds;
    [self redraw];
    [CATransaction commit];
}
#elif TARGET_OS_MAC
- (void)layout
{
    [super layout];
    [CATransaction begin];
    [CATransaction setDisableActions:YES];
    self.waveformLayer.frame = self.bounds;
    self.innerCircleLayer.frame = self.bounds;
    self.outerCircleLayer.frame = self.bounds;
    [self redraw];
    [CATransaction commit];
}
#endif

- (UIColor *)getUIColorObjectFromHexString:(NSString *)hexStr alpha:(CGFloat)alpha
{
    // Convert hex string to an integer
    unsigned int hexint = [self intFromHexString:hexStr];
    
    // Create color object, specifying alpha as well
    UIColor *color =
    [UIColor colorWithRed:((CGFloat) ((hexint & 0xFF0000) >> 16))/255
                    green:((CGFloat) ((hexint & 0xFF00) >> 8))/255
                     blue:((CGFloat) (hexint & 0xFF))/255
                    alpha:alpha];
    
    return color;
}

- (unsigned int)intFromHexString:(NSString *)hexStr
{
    unsigned int hexInt = 0;
    
    // Create scanner
    NSScanner *scanner = [NSScanner scannerWithString:hexStr];
    
    // Tell scanner to skip the # character
    [scanner setCharactersToBeSkipped:[NSCharacterSet characterSetWithCharactersInString:@"#"]];
    
    // Scan hex value
    [scanner scanHexInt:&hexInt];
    
    return hexInt;
}

- (void)initPlot
{
    self.shouldCenterYAxis = YES;
    self.shouldOptimizeForRealtimePlot = YES;
    self.gain = 1.0;
    self.plotType = EZPlotTypeBuffer;
    self.shouldMirror = NO;
    self.shouldFill = NO;
    
    // Setup history window
    [self resetHistoryBuffers];
    
    self.waveformLayer = [RecordingViewWaveformLayer layer];
    self.waveformLayer.frame = self.bounds;
    self.waveformLayer.lineWidth = 1.0f;
    self.waveformLayer.fillColor = nil;
    self.waveformLayer.backgroundColor = nil;
    self.waveformLayer.opaque = YES;
    
    self.innerCircleLayer = [RecordingViewCircleLayer layer];
    self.innerCircleLayer.frame = self.bounds;
    self.innerCircleLayer.lineWidth = 1.0f;
    self.innerCircleLayer.fillColor = [self getUIColorObjectFromHexString:@"#EC2221" alpha:.9].CGColor;
    self.innerCircleLayer.backgroundColor = nil;
    self.innerCircleLayer.opaque = YES;
    
    self.outerCircleLayer = [RecordingViewCircleLayer layer];
    self.outerCircleLayer.frame = self.bounds;
    self.outerCircleLayer.lineWidth = 1.0f;
    self.outerCircleLayer.fillColor = [self getUIColorObjectFromHexString:@"#FFDDBE" alpha:.9].CGColor;
    self.outerCircleLayer.backgroundColor = nil;
    self.outerCircleLayer.opaque = YES;
    
#if TARGET_OS_IPHONE
    self.color = [UIColor colorWithHue:0 saturation:1.0 brightness:1.0 alpha:1.0];
#elif TARGET_OS_MAC
    self.color = [NSColor colorWithCalibratedHue:0 saturation:1.0 brightness:1.0 alpha:1.0];
    self.wantsLayer = YES;
    self.layerContentsRedrawPolicy = NSViewLayerContentsRedrawOnSetNeedsDisplay;
#endif
    self.backgroundColor = nil;
    [self.layer addSublayer: self.outerCircleLayer];
    [self.layer addSublayer:self.innerCircleLayer];
    [self.layer addSublayer:self.waveformLayer];
    
    //
    // Allow subclass to initialize plot
    //
    [self setupPlot];
    
    self.points = calloc(EZAudioPlotDefaultMaxHistoryBufferLength, sizeof(CGPoint));
    self.pointCount = [self initialPointCount];
    [self redraw];
}

//------------------------------------------------------------------------------

- (void)setupPlot
{
    //
    // Override in subclass
    //
}

//------------------------------------------------------------------------------
#pragma mark - Setup
//------------------------------------------------------------------------------

- (void)resetHistoryBuffers
{
    //
    // Clear any existing data
    //
    if (self.historyInfo)
    {
        [EZAudioUtilities freeHistoryInfo:self.historyInfo];
    }
    
    self.historyInfo = [EZAudioUtilities historyInfoWithDefaultLength:[self defaultRollingHistoryLength]
                                                        maximumLength:[self maximumRollingHistoryLength]];
}

//------------------------------------------------------------------------------
#pragma mark - Setters
//------------------------------------------------------------------------------

- (void)setBackgroundColor:(id)backgroundColor
{
    [super setBackgroundColor:backgroundColor];
    self.layer.backgroundColor = [backgroundColor CGColor];
}

//------------------------------------------------------------------------------

- (void)setColor:(id)color
{
    [super setColor:color];
    self.waveformLayer.strokeColor = [color CGColor];
    if (self.shouldFill)
    {
        self.waveformLayer.fillColor = [color CGColor];
    }
}

//------------------------------------------------------------------------------

- (void)setShouldOptimizeForRealtimePlot:(BOOL)shouldOptimizeForRealtimePlot
{
    _shouldOptimizeForRealtimePlot = shouldOptimizeForRealtimePlot;
    if (shouldOptimizeForRealtimePlot && !self.displayLink)
    {
        self.displayLink = [EZAudioDisplayLink displayLinkWithDelegate:self];
        [self.displayLink start];
    }
    else
    {
        [self.displayLink stop];
        self.displayLink = nil;
    }
}

//------------------------------------------------------------------------------

- (void)setShouldFill:(BOOL)shouldFill
{
    [super setShouldFill:shouldFill];
    self.waveformLayer.fillColor = shouldFill ? [self.color CGColor] : nil;
}

//------------------------------------------------------------------------------
#pragma mark - Drawing
//------------------------------------------------------------------------------

- (void)clear
{
    if (self.pointCount > 0)
    {
        [self resetHistoryBuffers];
        float data[self.pointCount];
        memset(data, 0, self.pointCount * sizeof(float));
        [self setSampleData:data length:self.pointCount];
        [self redraw];
    }
}

//------------------------------------------------------------------------------

- (void)redraw
{
    EZRect frame = [self.waveformLayer frame];
    CGPathRef path = [self createPathWithPoints:self.points
                                     pointCount:self.pointCount
                                         inRect:frame];
    
    CGPathRef innerCirclePath = [self createInnerCirclePathWithPoints:self.points
                                                pointCount:self.pointCount
                                                    inRect:frame];

    CGPathRef outerCirclePath = [self createOuterCirclePathWithPoints:self.points
                                                           pointCount:self.pointCount
                                                               inRect:frame];
    
    if (self.shouldOptimizeForRealtimePlot)
    {
        [CATransaction begin];
        [CATransaction setDisableActions:YES];
        self.waveformLayer.path = path;
        self.innerCircleLayer.path = innerCirclePath;
        self.outerCircleLayer.path = outerCirclePath;
        [CATransaction commit];
    }
    else
    {
        self.waveformLayer.path = path;
        self.innerCircleLayer.path = innerCirclePath;
        self.outerCircleLayer.path = outerCirclePath;
    }
    CGPathRelease(path);
}

- (CGPathRef)createInnerCirclePathWithPoints:(CGPoint *)points
                                  pointCount:(UInt32)pointCount
                                      inRect:(EZRect)rect
{
    CGFloat width = MIN(rect.size.width, rect.size.height);
    return [[UIBezierPath bezierPathWithOvalInRect:CGRectMake(width / 2 - radio * width/4, width / 2 - radio * width/4, radio * width/2, radio * width/2)] CGPath];
}

- (CGPathRef)createOuterCirclePathWithPoints:(CGPoint *)points
                                  pointCount:(UInt32)pointCount
                                      inRect:(EZRect)rect;
{
    CGFloat timeout = 1000;
    CGFloat width = MIN(rect.size.width, rect.size.height);
    CGFloat maxY = 0.0;
    for (int i = 0; i < pointCount; i++)
    {
        maxY = MAX(points[i].y, maxY);
    }
    maxY = MIN(maxY * self.gain, 1.0);
    long long currentTime = (long long)([[NSDate date] timeIntervalSince1970] * 1000.0);
    if (maxY > self.lastMax) {
        self.lastMaxTime = currentTime;
        self.lastMax = maxY;
        self.lastMaxValue = maxY;
    }
    maxY = self.lastMax;
    CGFloat scaleX =(1.0 - maxY) * (width / 2 - radio * width/4);
    CGFloat scaleWidth = width - scaleX * 2;
    long long scaleTime = currentTime - self.lastMaxTime;
    if (scaleTime >= timeout) {
        self.lastMax = 0;
        self.lastMaxValue = 0;
    } else {
        self.lastMax = (1 - (scaleTime / timeout)) * self.lastMaxValue;
        //NSLog(@"Last max %f - %f - %f", (double)scaleTime, self.lastMax, self.lastMaxValue);
    }
    return [[UIBezierPath bezierPathWithOvalInRect:CGRectMake(scaleX, scaleX, scaleWidth, scaleWidth)] CGPath];
}

//------------------------------------------------------------------------------

- (CGPathRef)createPathWithPoints:(CGPoint *)points
                       pointCount:(UInt32)pointCount
                           inRect:(EZRect)rect
{
    CGMutablePathRef path = NULL;
    CGFloat width = radio * MIN(rect.size.width, rect.size.height) / 2;
    if (pointCount > 0)
    {
        path = CGPathCreateMutable();
        double xscale = (width) / ((float)self.pointCount);
        double halfHeight = floor(width / 2.0);
        int deviceOriginFlipped = [self isDeviceOriginFlipped] ? -1 : 1;
        CGAffineTransform xf = CGAffineTransformIdentity;
        CGFloat translateY = 0.0f;
        if (!self.shouldCenterYAxis)
        {
#if TARGET_OS_IPHONE
            translateY = rect.size.width / 2 + width / 2;
#elif TARGET_OS_MAC
            translateY = 0.0f;
#endif
        }
        else
        {
            translateY = rect.size.width / 2 + rect.origin.y;
        }
        xf = CGAffineTransformTranslate(xf, rect.size.width / 2 - radio * rect.size.width / 4, translateY);
        double yScaleFactor = halfHeight;
        if (!self.shouldCenterYAxis)
        {
            yScaleFactor = 2.0 * halfHeight;
        }
        xf = CGAffineTransformScale(xf, xscale, deviceOriginFlipped * yScaleFactor);
        CGPathAddLines(path, &xf, self.points, self.pointCount);
        if (self.shouldMirror)
        {
            xf = CGAffineTransformScale(xf, 1.0f, -1.0f);
            CGPathAddLines(path, &xf, self.points, self.pointCount);
        }
        if (self.shouldFill)
        {
            CGPathCloseSubpath(path);
        }
    }
    return path;
}

//------------------------------------------------------------------------------
#pragma mark - Update
//------------------------------------------------------------------------------

- (void)updateBuffer:(float *)buffer withBufferSize:(UInt32)bufferSize
{
    // append the buffer to the history
    [EZAudioUtilities appendBufferRMS:buffer
                       withBufferSize:bufferSize
                        toHistoryInfo:self.historyInfo];
    
    // copy samples
    switch (self.plotType)
    {
        case EZPlotTypeBuffer:
            [self setSampleData:buffer
                         length:bufferSize];
            break;
        case EZPlotTypeRolling:
            
            [self setSampleData:self.historyInfo->buffer
                         length:self.historyInfo->bufferSize];
            break;
        default:
            break;
    }
    
    // update drawing
    if (!self.shouldOptimizeForRealtimePlot)
    {
        [self redraw];
    }
}

//------------------------------------------------------------------------------

- (void)setSampleData:(float *)data length:(int)length
{
    CGPoint *points = self.points;
    for (int i = 0; i < length; i++)
    {
        points[i].x = i;
        points[i].y = data[i] * self.gain;
    }
    points[0].y = points[length - 1].y = 0.0f;
    self.pointCount = length;
}

//------------------------------------------------------------------------------
#pragma mark - Adjusting History Resolution
//------------------------------------------------------------------------------

- (int)rollingHistoryLength
{
    return self.historyInfo->bufferSize;
}

//------------------------------------------------------------------------------

- (int)setRollingHistoryLength:(int)historyLength
{
    self.historyInfo->bufferSize = MIN(EZAudioPlotDefaultMaxHistoryBufferLength, historyLength);
    return self.historyInfo->bufferSize;
}

//------------------------------------------------------------------------------
#pragma mark - Subclass
//------------------------------------------------------------------------------

- (int)defaultRollingHistoryLength
{
    return EZAudioPlotDefaultHistoryBufferLength;
}

//------------------------------------------------------------------------------

- (int)initialPointCount
{
    return 100;
}

//------------------------------------------------------------------------------

- (int)maximumRollingHistoryLength
{
    return EZAudioPlotDefaultMaxHistoryBufferLength;
}

//------------------------------------------------------------------------------
#pragma mark - Utility
//------------------------------------------------------------------------------

- (BOOL)isDeviceOriginFlipped
{
    BOOL isDeviceOriginFlipped = NO;
#if TARGET_OS_IPHONE
    isDeviceOriginFlipped = YES;
#elif TARGET_OS_MAC
#endif
    return isDeviceOriginFlipped;
}

//------------------------------------------------------------------------------
#pragma mark - EZAudioDisplayLinkDelegate
//------------------------------------------------------------------------------

- (void)displayLinkNeedsDisplay:(EZAudioDisplayLink *)displayLink
{
    [self redraw];
}

//------------------------------------------------------------------------------

@end

////------------------------------------------------------------------------------
#pragma mark - RecordingViewWaveformLayer (Implementation)
////------------------------------------------------------------------------------

@implementation RecordingViewWaveformLayer

- (id<CAAction>)actionForKey:(NSString *)event
{
    if ([event isEqualToString:@"path"])
    {
        if ([CATransaction disableActions])
        {
            return nil;
        }
        else
        {
            CABasicAnimation *animation = [CABasicAnimation animation];
            animation.timingFunction = [CATransaction animationTimingFunction];
            animation.duration = [CATransaction animationDuration];
            return animation;
        }
        return nil;
    }
    return [super actionForKey:event];
}

@end

@implementation RecordingViewCircleLayer

//    - (instancetype)init
//    {
//        self = [super init];
//        if (self) {
//            [self setNeedsDisplay];
//        }
//        return self;
//    }
//
- (void)drawInContext:(CGContextRef)ctx
{
    
    size_t gradLocationsNum = 2;
    CGFloat gradLocations[2] = {0.0f, 1.0f};
    CGFloat gradColors[8] = {0.0f,0.3f,0.3f,0.3f,0.0f,0.4f,0.4f,0.7f};
    CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
    CGGradientRef gradient = CGGradientCreateWithColorComponents(colorSpace, gradColors, gradLocations, gradLocationsNum);
    CGColorSpaceRelease(colorSpace);
    
    CGPoint gradCenter= CGPointMake(self.bounds.size.width/2, self.bounds.size.height/2);
    float gradRadius = MIN(self.bounds.size.width , self.bounds.size.height) ;
    
    CGContextDrawRadialGradient (ctx, gradient, gradCenter, 0, gradCenter, gradRadius, kCGGradientDrawsAfterEndLocation);
    
    
    CGGradientRelease(gradient);
}
@end