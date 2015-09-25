package com.cmg.vrc.servlet;

import com.amazonaws.services.s3.model.S3Object;
import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.dao.impl.RecorderDAO;
import com.cmg.vrc.data.jdo.RecordedSentence;
import com.cmg.vrc.processor.CustomFFMPEGLocator;
import com.cmg.vrc.properties.Configuration;
import com.cmg.vrc.util.AWSHelper;
import com.cmg.vrc.util.FileHelper;
import com.cmg.vrc.util.UUIDGenerator;
import com.google.gson.Gson;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by CMGT400 on 9/8/2015.
 */
public class LoadAudioRecorder extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RecorderDAO recorderDAO=new RecorderDAO();
        OutputStream out = response.getOutputStream();

        response.setHeader("Pragma", "private");
        response.setHeader("Cache-Control", "private, must-revalidate");
        response.setHeader("Accept-Ranges", "bytes");
        String id=request.getParameter("id");
        String type = request.getParameter("type");
        try{
            AWSHelper awsHelper = new AWSHelper();
            RecordedSentence recordedSentence=recorderDAO.getById(id);
            String fileName=recordedSentence.getFileName();
            String acount=recordedSentence.getAccount();

            String audioKey = Constant.FOLDER_REOCORDED_VOICES_AMT + "/" + acount + "/" + fileName;
            String targetAudio = audioKey;
            String audioMp3Key = audioKey + ".mp3";
        //    log("Audio path: " + audioFile);
        //    parseWave(audioFile);
            if (!StringUtils.isEmpty(type) && type.equalsIgnoreCase("mp3")) {
                if (awsHelper.getS3Object(audioMp3Key) == null) {
                    File audioFile = new File(FileHelper.getTmpSphinx4DataDir(), acount + File.separator + fileName);
                    if (!audioFile.exists()) {
                        awsHelper.download(audioKey, audioFile);
                    } else {
                        if (awsHelper.getS3Object(audioKey) == null)
                            awsHelper.upload(audioKey, audioFile);
                    }
                    File mp3Audio = new File(FileHelper.getTmpSphinx4DataDir(), acount + File.separator + fileName + ".mp3");
                    if (!mp3Audio.exists()) {
                        AudioAttributes audio = new AudioAttributes();
                        audio.setCodec("libmp3lame");
                        audio.setBitRate(new Integer(128000));
                        audio.setChannels(new Integer(2));
                        audio.setSamplingRate(new Integer(44100));
                        EncodingAttributes attrs = new EncodingAttributes();
                        attrs.setFormat("mp3");
                        attrs.setAudioAttributes(audio);
                        String env = Configuration.getValue(Configuration.SYSTEM_ENVIRONMENT);
                        Encoder encoder;
                        if (env.equalsIgnoreCase("prod") || env.equalsIgnoreCase("sat")
                                || env.equalsIgnoreCase("int")
                                || env.equalsIgnoreCase("aws")) {
                            encoder = new Encoder(new CustomFFMPEGLocator());
                        } else {
                            encoder =new Encoder();
                        }
                        encoder.encode(audioFile, mp3Audio, attrs);
                    }
                    if (mp3Audio.exists()) {
                        awsHelper.upload(audioMp3Key, mp3Audio);
                        try {
                            FileUtils.forceDelete(mp3Audio);
                            FileUtils.forceDelete(audioFile);
                        } catch (Exception e) {

                        }
                    }
                }
                targetAudio = audioMp3Key;
                response.setContentType("audio/mpeg");
            } else {
                response.setContentType("audio/wav");
            }
            log("Stream audio from S3: " + targetAudio);
            S3Object s3Object =  awsHelper.getS3Object(targetAudio);
            InputStream in = s3Object.getObjectContent();
            response.setContentLength((int) s3Object.getObjectMetadata().getContentLength());
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) != -1){
                out.write(buffer, 0, length);
            }
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    void parseWave(File file)
            throws IOException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes = new byte[4];

            // read first 4 bytes
            // should be RIFF descriptor
            if(in.read(bytes) < 0) {
                return;
            }

            printDescriptor(bytes);

            // first subchunk will always be at byte 12
            // there is no other dependable constant
            in.skip(8);

            for(;;) {
                // read each chunk descriptor
                if(in.read(bytes) < 0) {
                    break;
                }

                printDescriptor(bytes);

                // read chunk length
                if(in.read(bytes) < 0) {
                    break;
                }

                // skip the length of this chunk
                // next bytes should be another descriptor or EOF
                in.skip(
                        (bytes[0] & 0xFF)
                                | (bytes[1] & 0xFF) << 8
                                | (bytes[2] & 0xFF) << 16
                                | (bytes[3] & 0xFF) << 24
                );
            }

            log("end of file");

        } finally {
            if(in != null) {
                in.close();
            }
        }
    }

    void printDescriptor(byte[] bytes)
            throws IOException {
        log(
                "found '" + new String(bytes, "US-ASCII") + "' descriptor"
        );
    }
}
