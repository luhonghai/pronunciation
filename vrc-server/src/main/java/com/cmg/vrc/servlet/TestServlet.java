package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.FeedbackDAO;
import com.cmg.vrc.data.jdo.Feedback;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class TestServlet  {
    public static void main(String[] args) {
        FeedbackDAO feedbackDAO = new FeedbackDAO();
//        try {
//            feedbackDAO.deleteAll();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Feedback feedback = new Feedback();

        try {
            Feedback abc=feedbackDAO.getById("b3d4069a-4cb1-4595-be87-fe6020069cbc");
            System.out.print(abc.getAccount());
        }catch (Exception e){
            e.printStackTrace();
        }


        try {
            feedbackDAO.put(feedback);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            List<Feedback> list = feedbackDAO.listAll();
            if (list != null && list.size() > 0) {
                for (Feedback f : list) {
                    System.out.println( "Feedback account: " + f.getAccount());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
