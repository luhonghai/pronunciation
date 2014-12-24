/*
*      _______                       _____   _____ _____  
*     |__   __|                     |  __ \ / ____|  __ \ 
*        | | __ _ _ __ ___  ___  ___| |  | | (___ | |__) |
*        | |/ _` | '__/ __|/ _ \/ __| |  | |\___ \|  ___/ 
*        | | (_| | |  \__ \ (_) \__ \ |__| |____) | |     
*        |_|\__,_|_|  |___/\___/|___/_____/|_____/|_|     
*                                                         
* -------------------------------------------------------------
*
* TarsosDSP is developed by Joren Six at IPEM, University Ghent
*  
* -------------------------------------------------------------
*
*  Info: http://0110.be/tag/TarsosDSP
*  Github: https://github.com/JorenSix/TarsosDSP
*  Releases: http://0110.be/releases/TarsosDSP/
*  
*  TarsosDSP includes modified source code by various authors,
*  for credits and info, see README.
* 
*/

package com.cmg.android.voicerecorder.dsp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioRecord;
import android.os.Environment;

import com.cmg.android.voicerecorder.AppLog;

import org.apache.commons.io.FileUtils;

import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.TarsosDSPAudioInputStream;

public class AndroidAudioInputStream implements TarsosDSPAudioInputStream{
    private static final int RECORDER_BPP = 16;

    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private static final String AUDIO_RECORDER_OUTPUT_FILE = "record_temp.wav";
    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";

	private File tmpFile;
	private final AudioRecord underlyingStream;
	private final TarsosDSPAudioFormat format;
    private final Context context;
    private final int bufferSize;

    private int read = 0;
    private FileOutputStream os;
	public AndroidAudioInputStream(Context context, AudioRecord underlyingStream, TarsosDSPAudioFormat format, int bufferSize){
        this.context = context;
		this.underlyingStream = underlyingStream;
		this.format = format;
        this.bufferSize = bufferSize;

        deleteTempFile();
        String filename = getTempFilename();
        AppLog.logString("Prepare for new record " + filename);
        try {
            os = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void skip(long bytesToSkip) throws IOException {
		throw new IOException("Can not skip in audio stream");
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
        read = underlyingStream.read(b,off,len);
        try {
            os.write(b);
        } catch (IOException ex) {

        }
		return read;
	}

	@Override
	public void close() throws IOException {
        try {
            underlyingStream.stop();
            underlyingStream.release();
        } catch (Exception ex) {
           // ex.printStackTrace();
        }
        if (os != null)  {
            try {
                os.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        AppLog.logString("Create WAV file " + getFilename());
        copyWaveFile(getTempFilename(),getFilename());
        deleteTempFile();
        AppLog.logString("Delete cached file");
	}

	@Override
	public TarsosDSPAudioFormat getFormat() {
		return format;
	}

	@Override
	public long getFrameLength() {
		return -1;
	}


    public void deleteTempFile() {
        if (tmpFile != null && tmpFile.exists()) {
            tmpFile.delete();
        }
    }

    private void copyWaveFile(String inFilename,String outFilename){
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = (long) format.getSampleRate();
        int channels = format.getChannels();
        long byteRate = RECORDER_BPP * (long) format.getSampleRate() * channels/8;

        byte[] data = new byte[(bufferSize / 2) * format.getFrameSize()];
        try {
            File inFile = new File(inFilename);
            if (!inFile.exists()) return;
            in = new FileInputStream(inFile);
            out = new FileOutputStream(outFilename);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;

            AppLog.logString("File size: " + totalDataLen);

            writeWaveFileHeader(out, totalAudioLen, totalDataLen,
                    longSampleRate, channels, byteRate);

            while(in.read(data) != -1){
                out.write(data);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception ex) {

            }
            try  {
                out.close();
            } catch (Exception ex) {

            }
        }
    }

    private void writeWaveFileHeader(
            FileOutputStream out, long totalAudioLen,
            long totalDataLen, long longSampleRate, int channels,
            long byteRate) throws IOException {

        byte[] header = new byte[44];

        header[0] = 'R';  // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f';  // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16;  // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1;  // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8);  // block align
        header[33] = 0;
        header[34] = RECORDER_BPP;  // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        out.write(header, 0, 44);
    }

    public String getFilename(){
        File file = new File(getTmpDir());
        if(!file.exists()){
            file.mkdirs();
        }
        return (file.getAbsolutePath() + "/" + AUDIO_RECORDER_OUTPUT_FILE);
    }

    public void clearOldRecord() {
        File file = new File(getFilename());
        if (file.exists()) {
            try {
                FileUtils.forceDelete(file);
            } catch (Exception ex) {

            }
        }
    }

    private String getTempFilename(){
        File file = new File(getTmpDir());
        if(!file.exists()){
            file.mkdirs();
        }
        tmpFile = new File(file,AUDIO_RECORDER_TEMP_FILE);

//        if(tempFile.exists())
//            tempFile.delete();

        return (file.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_FILE);
    }


    private String getTmpDir() {
        PackageManager m = context.getPackageManager();
        String s = context.getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(s, 0);
            return p.applicationInfo.dataDir + File.separator + AUDIO_RECORDER_FOLDER;
        } catch (PackageManager.NameNotFoundException e) {
            return Environment.getExternalStorageDirectory().getPath() + File.separator + AUDIO_RECORDER_FOLDER;
        }
    }

}
