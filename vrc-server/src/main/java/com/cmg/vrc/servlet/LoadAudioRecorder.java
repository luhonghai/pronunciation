package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.RecorderDAO;
import com.cmg.vrc.data.jdo.RecordedSentence;
import com.cmg.vrc.util.FileHelper;
import com.google.gson.Gson;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

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

            RecordedSentence recordedSentence=recorderDAO.getById(id);
            String tmpDir = FileHelper.getTmpSphinx4DataDir().getAbsolutePath();
            String fileName=recordedSentence.getFileName();
            String acount=recordedSentence.getAccount();
            String path= tmpDir + File.separator + acount + File.separator + fileName;
            File audioFile = new File(path);
            File target = audioFile;
        //    log("Audio path: " + audioFile);
        //    parseWave(audioFile);
            if (!StringUtils.isEmpty(type) && type.equalsIgnoreCase("mp3")) {
                File mp3Audio = new File(path + ".mp3");
                if (!mp3Audio.exists()) {
                    AudioAttributes audio = new AudioAttributes();
                    audio.setCodec("libmp3lame");
                    audio.setBitRate(new Integer(128000));
                    audio.setChannels(new Integer(2));
                    audio.setSamplingRate(new Integer(44100));
                    EncodingAttributes attrs = new EncodingAttributes();
                    attrs.setFormat("mp3");
                    attrs.setAudioAttributes(audio);
                    Encoder encoder = new Encoder();
                    encoder.encode(audioFile, mp3Audio, attrs);
                }
                target = mp3Audio;
                response.setContentType("audio/mpeg");
            } else {
                response.setContentType("audio/wav");
            }
            FileInputStream in = new FileInputStream(target);
            response.setContentLength((int) target.length());
            byte[] buffer = new byte[4096];
            int length;
            while ((length = in.read(buffer)) != -1){
                out.write(buffer, 0, length);
                out.flush();
            }
            in.close();
            out.flush();

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
