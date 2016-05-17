//
//  YouTubePlayer.swift
//  AccentEasy
//
//  Created by Hai Lu on 5/4/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit
import youtube_ios_player_helper

class YouTubePlayer: UIView {
    
    @IBOutlet weak var playerView: YTPlayerView!
    
    var isShowing = false
    
    @IBOutlet var contentView: UIView!
    
    override init(frame: CGRect) { // for using CustomView in code
        super.init(frame: frame)
        self.commonInit()
    }
    
    @IBAction func tapBackground(sender: AnyObject) {
        playerView.stopVideo()
        close()
    }
    required init?(coder aDecoder: NSCoder) { // for using CustomView in IB
        super.init(coder: aDecoder)
        self.commonInit()
    }
    
    private func commonInit() {
        NSBundle.mainBundle().loadNibNamed("YouTubePlayer", owner: self, options: nil)
        guard let content = contentView else { return }
        content.frame = self.bounds
        content.autoresizingMask = [.FlexibleHeight, .FlexibleWidth]
        self.addSubview(content)
        //self.userInteractionEnabled = false
    }
    
    func close() {
        self.removeFromSuperview()
        isShowing = false
    }
    
    func show(videoId: String) {
        show(UIScreen.mainScreen().applicationFrame, videoId: videoId)
    }
    
    func show(rect: CGRect, videoId: String) {
        if let app = UIApplication.sharedApplication().delegate as? AppDelegate, let window = app.window {
            self.frame = rect
            window.addSubview(self)
            self.contentView?.frame = rect
            self.clipsToBounds = true
            var playerVar = [NSObject: AnyObject]()
            playerVar["playsinline"] = 1
            playerVar["autoplay"] = 1
            self.playerView.loadWithVideoId(videoId, playerVars: playerVar)
            isShowing = true
        }
    }
}
