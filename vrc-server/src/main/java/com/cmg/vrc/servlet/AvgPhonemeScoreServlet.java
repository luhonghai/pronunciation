package com.cmg.vrc.servlet;

import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.data.dao.impl.PhonemeScoreDAO;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by cmg on 13/05/2016.
 */
@WebServlet(name = "AvgPhonemeScoreServlet", urlPatterns = {"/avg_phoneme_score"})
public class AvgPhonemeScoreServlet extends HttpServlet {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        String profile = request.getParameter("profile");
        ResponseData<Map<String, Integer>> responseData = new ResponseData<>();
        try {
            if (profile != null && profile.length() > 0) {

                UserProfile userProfile = gson.fromJson(profile, UserProfile.class);
                logger.info("Get avg phoneme score for user " + userProfile.getUsername());
                PhonemeScoreDAO phonemeScoreDAO = new PhonemeScoreDAO();
                responseData.setData(phonemeScoreDAO.getAvgPhonemeScore(userProfile.getUsername()));
                logger.info("Phone score size " + responseData.getData().size());
                responseData.setStatus(true);
                responseData.setMessage("success");
            } else {
                responseData.setMessage("Invalid parameter");
                responseData.setStatus(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setStatus(false);
            responseData.setMessage("Error: " + e.getMessage());
        } finally {
            response.getWriter().write(gson.toJson(responseData));
        }
    }
}
