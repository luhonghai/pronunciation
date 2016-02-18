//
//  AnalyzingViewController.swift
//  AccentEasy
//
//  Created by Hai Lu on 17/02/2016.
//  Copyright © 2016 Hai Lu. All rights reserved.
//

import Foundation
import EZAudio

class AnalyzingViewController: UIViewController, EZAudioPlayerDelegate, EZMicrophoneDelegate, EZRecorderDelegate {
    
    let kAudioFilePath: String = "test.m4a"
    var isRecording: Bool = false
    var microphone: EZMicrophone!
    var player: EZAudioPlayer!
    var recorder: EZRecorder!
    
    @IBOutlet var currentTimeLabel: UILabel!
    
    @IBOutlet var recordingAudioPlot: AnalyzingView!
    
    @IBOutlet var microphoneSwitch: UISwitch!
    
    @IBOutlet var microphoneStateLabel: UILabel!
    
    @IBOutlet var  playingAudioPlot: EZAudioPlot!
    
    @IBOutlet var playButton: UIButton!
    
    @IBOutlet var playingStateLabel: UILabel!
    
    @IBOutlet var recordingStateLabel: UILabel!
    
    @IBOutlet var recordSwitch: UISwitch!

    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
    
    override func preferredStatusBarStyle() -> UIStatusBarStyle
    {
        return UIStatusBarStyle.LightContent;
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //
        // Setup the AVAudioSession. EZMicrophone will not work properly on iOS
        // if you don't do this!
        //
        let session: AVAudioSession = AVAudioSession.sharedInstance()
        do {
            try session.setCategory(AVAudioSessionCategoryPlayAndRecord)
        } catch {
            
        }
        do {
            try session.setActive(true)
        } catch {
            
        }
        
        self.recordingAudioPlot.shouldFill = false
        self.recordingAudioPlot.shouldMirror = false
        
        //
        // Customizing the audio plot that'll show the playback
        //
        self.playingAudioPlot.color = UIColor.whiteColor()
        self.playingAudioPlot.plotType = EZPlotType.Buffer
        self.playingAudioPlot.shouldFill = true
        self.playingAudioPlot.shouldMirror = false
        self.playingAudioPlot.gain = 2.5
        
        // Create an instance of the microphone and tell it to use this view controller instance as the delegate
        self.microphone = EZMicrophone(delegate: self)
        self.player = EZAudioPlayer(delegate: self)
        
        //
        // Override the output to the speaker. Do this after creating the EZAudioPlayer
        // to make sure the EZAudioDevice does not reset this.
        //
        do {
            try session.overrideOutputAudioPort(AVAudioSessionPortOverride.Speaker)
        } catch {
            
        }
        
        //
        // Initialize UI components
        //
        self.microphoneStateLabel.text = "Microphone On"
        self.recordingStateLabel.text = "Not Recording"
        self.playingStateLabel.text = "Not Playing"
        self.playButton.enabled = false
        
        //
        // Setup notifications
        //
        self.setupNotifications()
        
        self.microphone.startFetchingAudio()
    }
    
    func setupNotifications() {
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "playerDidChangePlayState:", name: EZAudioPlayerDidChangePlayStateNotification, object: self.player)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "playerDidReachEndOfFile:", name: EZAudioPlayerDidReachEndOfFileNotification, object: self.player)
    }
    
    //------------------------------------------------------------------------------
    // Notifications
    //------------------------------------------------------------------------------
    
    func playerDidChangePlayState(notification: NSNotification)
    {
        weak var weakSelf = self
        dispatch_async(dispatch_get_main_queue(), {
            let player = notification.object as! EZAudioPlayer
            let isPlaying = player.isPlaying
            if (isPlaying) {
                weakSelf!.recorder.delegate = nil
            }
            weakSelf!.playingStateLabel.text = isPlaying ? "Playing" : "Not Playing"
            weakSelf!.playingAudioPlot.hidden = !isPlaying
        });
    }
    
    //------------------------------------------------------------------------------
    
    func playerDidReachEndOfFile(notification: NSNotification)
    {
        weak var weakSelf = self;
        dispatch_async(dispatch_get_main_queue(), {
            weakSelf!.playingAudioPlot.clear()
        });
    }
    
    
    @IBAction func playFile(sender: AnyObject)
    {
        self.microphone.stopFetchingAudio()
        
        self.isRecording = false;
        self.recordingAudioPlot.switchType(AnalyzingType.DEFAULT)
        self.recordingStateLabel.text = "Not Recording";
        self.recordSwitch.on = false;
        
        if (self.recorder != nil)
        {
            self.recorder.closeAudioFile()
        }
        
        let audioFile: EZAudioFile = EZAudioFile(URL: self.testFilePathURL())
        self.player.playAudioFile(audioFile)
    }
    
    //------------------------------------------------------------------------------
    
    @IBAction func toggleMicrophone(sender: UISwitch)
    {
        self.player.pause()
        if sender.on
        {
            self.microphone.stopFetchingAudio()
        }
        else
        {
            self.microphone.startFetchingAudio()
        }
    }
    
    //------------------------------------------------------------------------------
    
    @IBAction func toggleRecording(sender: UISwitch)
    {
        self.player.pause()
        if sender.on
        {
            self.recordingAudioPlot.clear()
            self.microphone.startFetchingAudio()
            self.recorder = EZRecorder(URL: self.testFilePathURL(), clientFormat: self.microphone.audioStreamBasicDescription(), fileType: EZRecorderFileType.M4A, delegate: self)
            self.playButton.enabled = true;
            self.recordingAudioPlot.switchType(AnalyzingType.RECORDING)
        } else {
            self.recordingAudioPlot.showScore(90)
        }
        self.isRecording = sender.on
        self.recordingStateLabel.text = self.isRecording ? "Recording" : "Not Recording";
    }

    
    func microphone(microphone: EZMicrophone!, changedPlayingState isPlaying: Bool) {
        self.microphoneStateLabel.text = isPlaying ? "Microphone On" : "Microphone Off"
        self.microphoneSwitch.on = isPlaying
    }
    
    func microphone(microphone: EZMicrophone!, hasAudioReceived buffer: UnsafeMutablePointer<UnsafeMutablePointer<Float>>, withBufferSize bufferSize: UInt32, withNumberOfChannels numberOfChannels: UInt32) {
        weak var weakSelf = self
        dispatch_async(dispatch_get_main_queue(), {
           // NSLog("hasAudioReceived. size %d", bufferSize)
            weakSelf!.recordingAudioPlot.updateBuffer(buffer[0], withBufferSize:bufferSize)
        })
    }
    
    func microphone(microphone: EZMicrophone!, hasBufferList bufferList: UnsafeMutablePointer<AudioBufferList>, withBufferSize bufferSize: UInt32, withNumberOfChannels numberOfChannels: UInt32) {
        if self.isRecording {
            self.recorder.appendDataFromBufferList(bufferList, withBufferSize:bufferSize)
        }
    }
    
    func recorderDidClose(recorder: EZRecorder!) {
        recorder.delegate = nil
    }
    
    func recorderUpdatedCurrentTime(recorder: EZRecorder!) {
        weak var weakSelf = self
        let formattedCurrentTime: String = recorder.formattedCurrentTime
        dispatch_async(dispatch_get_main_queue(), {
            weakSelf!.currentTimeLabel.text = formattedCurrentTime;
        });
    }
    
    func audioPlayer(audioPlayer: EZAudioPlayer!, playedAudio buffer: UnsafeMutablePointer<UnsafeMutablePointer<Float>>, withBufferSize bufferSize: UInt32, withNumberOfChannels numberOfChannels: UInt32, inAudioFile audioFile: EZAudioFile!) {
        weak var weakSelf = self
        dispatch_async(dispatch_get_main_queue(), {
            weakSelf!.playingAudioPlot.updateBuffer(buffer[0],withBufferSize:bufferSize)
        });
    }
    
    func audioPlayer(audioPlayer: EZAudioPlayer!, updatedPosition framePosition: Int64, inAudioFile audioFile: EZAudioFile!) {
        weak var weakSelf = self
        dispatch_async(dispatch_get_main_queue(), {
            weakSelf!.currentTimeLabel.text = audioPlayer.formattedCurrentTime
        });
    }
    
    
    func testFilePathURL() -> NSURL
    {
        return NSURL(fileURLWithPath: FileHelper.getFilePath(kAudioFilePath))
    }
    
}