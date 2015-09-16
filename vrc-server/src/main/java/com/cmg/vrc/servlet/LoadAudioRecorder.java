package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.RecorderDAO;
import com.cmg.vrc.data.jdo.RecordedSentence;
import com.cmg.vrc.util.FileHelper;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
//             response.setContentLength();
            response.setContentType("audio/wav");
            String id=request.getParameter("id");
            try{
                RecordedSentence recordedSentence=recorderDAO.getById(id);
                String tmpDir = FileHelper.getTmpSphinx4DataDir().getAbsolutePath();
                String fileName=recordedSentence.getFileName();
                String acount=recordedSentence.getAccount();
                String path=tmpDir+'\\'+acount+'\\'+fileName;
                FileInputStream in = new FileInputStream(path);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0){
                    out.write(buffer, 0, length);
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
}
