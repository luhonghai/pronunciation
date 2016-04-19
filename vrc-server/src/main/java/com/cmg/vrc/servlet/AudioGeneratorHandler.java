package com.cmg.vrc.servlet;

import com.cmg.vrc.processor.CustomFFMPEGLocator;
import com.cmg.vrc.properties.Configuration;
import com.cmg.vrc.util.UUIDGenerator;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;

/**
 * Created by luhonghai on 3/29/16.
 */
public class AudioGeneratorHandler extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        OutputStream out = response.getOutputStream();
        response.setHeader("Pragma", "private");
        response.setHeader("Cache-Control", "private, must-revalidate");
        response.setHeader("Accept-Ranges", "bytes");
        String url=request.getParameter("url");
        File tmpFile = new File(FileUtils.getTempDirectory(), UUIDGenerator.generateUUID() + ".raw.wav");
        File tmpFileWav = new File(FileUtils.getTempDirectory(), UUIDGenerator.generateUUID() + ".tmp.wav");
        try {
            FileUtils.copyURLToFile(new URL(url), tmpFile);
            AudioAttributes audio = new AudioAttributes();
            audio.setChannels(1);
            audio.setSamplingRate(16000);
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("wav");
            attrs.setAudioAttributes(audio);
            logger.info("Origin file path :" + tmpFile.getAbsolutePath());
            String env = Configuration.getValue(Configuration.SYSTEM_ENVIRONMENT);
            Encoder encoder;
            if (env.equalsIgnoreCase("prod") || env.equalsIgnoreCase("sat")
                    || env.equalsIgnoreCase("int")
                    || env.equalsIgnoreCase("aws")) {
                encoder = new Encoder(new CustomFFMPEGLocator());
            } else {
                encoder = new Encoder(new CustomFFMPEGLocator.MacFFMPEGLocator());
            }
            try {
                encoder.encode(tmpFile, tmpFileWav, attrs);
            } catch (Exception e) {
                // ingore
            }
            response.setContentType("audio/wav");
            InputStream in = new FileInputStream(tmpFileWav);
            try {
                response.setContentLength(in.available());
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) != -1) {
                    out.write(buffer, 0, length);
                }
                out.flush();
                out.close();
            } finally {
                IOUtils.closeQuietly(in);
            }
        } catch (Exception e) {
            //
        } finally {
            if (tmpFile.exists()) {
                try {
                    FileUtils.forceDelete(tmpFile);
                } catch (Exception e) {}
            }
            if (tmpFileWav.exists()) {
                try {
                    FileUtils.forceDelete(tmpFileWav);
                } catch (Exception e) {}
            }
        }
    }
}
