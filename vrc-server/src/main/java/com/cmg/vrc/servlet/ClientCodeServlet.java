package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.ClientCodeDAO;
import com.cmg.vrc.data.jdo.ClientCode;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by CMGT400 on 8/7/2015.
 */
public class ClientCodeServlet extends HttpServlet {
    class client{
        public int draw;
        public Double recordsTotal;
        public Double recordsFiltered;

        List<ClientCode> data;
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClientCodeDAO clientCodeDAO = new ClientCodeDAO();
        ClientCode ad = new ClientCode();
        if (request.getParameter("list") != null) {
            ClientCodeServlet.client client = new client();
            String s = request.getParameter("start");
            String l = request.getParameter("length");
            String d = request.getParameter("draw");
            String search = request.getParameter("search[value]");
            String column = request.getParameter("order[0][column]");
            String oder = request.getParameter("order[0][dir]");
            int start = Integer.parseInt(s);
            int length = Integer.parseInt(l);
            int col = Integer.parseInt(column);
            int draw = Integer.parseInt(d);
            String company = request.getParameter("companyname");
            String contact = request.getParameter("contactname");
            String email = request.getParameter("email");
            Double count;

            try {
                if(search.length()>0||company.length()>0||contact.length()>0||email.length()>0){
                    count=clientCodeDAO.getCountSearch(search,company,contact,email);
                }else {
                    count = clientCodeDAO.getCount();
                }
                client.draw = draw;
                client.recordsTotal = count;
                client.recordsFiltered = count;
                client.data = clientCodeDAO.listAll(start, length, search, col, oder,company,contact,email);

                Gson gson = new Gson();
                String admins = gson.toJson(client);
                response.getWriter().write(admins);

            } catch (Exception e) {
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }

        if(request.getParameter("add")!=null){
            String company = request.getParameter("companyname");
            String contact = request.getParameter("contactname");
            String email = request.getParameter("email");
            try{
                ClientCode a=clientCodeDAO.getUserName(company);
                if(a!=null) {
                    response.getWriter().write("error");
                } else {
                    ad.setCompanyName(company);
                    ad.setContactName(contact);
                    ad.setEmail(email);
                    clientCodeDAO.put(ad);
                    response.getWriter().write("success");

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        if(request.getParameter("edit")!=null){
            String id=request.getParameter("id");
            String contact = request.getParameter("contact");
            String email = request.getParameter("email");
            try{

                    ClientCode admi = clientCodeDAO.getById(id);
                    if (contact.length()>0) {
                        admi.setContactName(contact);
                    }
                    if (email.length()>0) {
                        admi.setEmail(email);
                    }
                    clientCodeDAO.put(admi);
                    response.getWriter().write("success");
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        if(request.getParameter("delete")!=null){
            String id=request.getParameter("id");
            try {
            clientCodeDAO.delete(id);
                response.getWriter().write("success");
            }catch (Exception e){
                e.printStackTrace();
            }
        }



    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
