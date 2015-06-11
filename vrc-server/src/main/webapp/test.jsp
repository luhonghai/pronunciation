<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.cmg.vrc.dictionary.OxfordDictionaryWalker" %>
<%@ page import="javax.mail.MessagingException" %>
<%@ page import="com.cmg.vrc.data.jdo.Admin" %>
<%@ page import="com.cmg.vrc.data.dao.impl.AdminDAO" %>
<%@ page import="com.cmg.vrc.util.StringUtil" %>
<html>
<body>
<table>
<%
    Admin admin = new Admin();
    admin.setUserName("admin");
    admin.setPassword(StringUtil.md5("admincmg@3f"));
    AdminDAO adminDAO = new AdminDAO();
    try {
adminDAO.put(admin);} catch (Exception e) {
    e.printStackTrace();
}
    %>
  <p>done</p>
</body>
</html>