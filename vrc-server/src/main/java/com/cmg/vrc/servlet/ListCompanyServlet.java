package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.ClientCodeDAO;
import com.cmg.vrc.data.dao.impl.LicenseCodeCompanyDAO;
import com.cmg.vrc.data.jdo.ClientCode;
import com.cmg.vrc.data.jdo.LicenseCodeCompany;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by CMGT400 on 8/25/2015.
 */
public class ListCompanyServlet  extends HttpServlet {

    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("listCompany") != null) {
            try {
                List<LicenseCodeCompany> licenseCodeCompanies =licenseCodeCompanyList();
                Gson gson = new Gson();
                String company = gson.toJson(licenseCodeCompanies);
                response.getWriter().write(company);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
    public List<LicenseCodeCompany> licenseCodeCompanyList(){
        List<LicenseCodeCompany> list=null;
        LicenseCodeCompanyDAO licenseCodeCompanyDAO=new LicenseCodeCompanyDAO();
        int number=0;
        try {
            List<LicenseCodeCompany> licenseCodeCompanies = licenseCodeCompanyDAO.listAll();
            for (int i = 0; i < licenseCodeCompanies.size(); i++) {
                for (int j = i + 1; j < licenseCodeCompanies.size(); j++) {
                    if (licenseCodeCompanies.get(i).getCompany().equals(licenseCodeCompanies.get(j).getCompany())){
                        licenseCodeCompanies.remove(j);
                        j=j-1;
                    }
                }
            }
            list=licenseCodeCompanies;
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
