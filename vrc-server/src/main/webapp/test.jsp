<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.cmg.vrc.dictionary.OxfordDictionaryWalker" %>
<%@ page import="javax.mail.MessagingException" %>
<%@ page import="com.cmg.vrc.data.jdo.Admin" %>
<%@ page import="com.cmg.vrc.data.dao.impl.AdminDAO" %>
<%@ page import="com.cmg.vrc.util.StringUtil" %>
<html>
<body>
  <%
    AdminDAO adminDAO=new AdminDAO();
      double count= 0;
      try {
          count = adminDAO.getCount();
      } catch (Exception e) {
          e.printStackTrace();
      }
      Admin admin=new Admin();
    try {
       if(count==0){
       admin.setPassword(StringUtil.md5("admincmg@3f"));
       admin.setUserName("admin");
       admin.setRole(1);
       admin.setLastName("Admin");
       admin.setFirstName("CMG");
       adminDAO.put(admin);
       }

     }catch (Exception e){
         e.printStackTrace();
        }
    %>
  <p>done</p>


</body>
</html>