<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
  if (session.getAttribute("username") == null || session.getAttribute("password") == null){
    response.sendRedirect("login.jsp");
  }
  session.invalidate();
  response.sendRedirect("login.jsp");
%>