<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.cmg.vrc.dictionary.OxfordDictionaryWalker" %>
<%@ page import="javax.mail.MessagingException" %>
<<<<<<< HEAD
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
=======
<%@ page import="com.cmg.vrc.data.dao.impl.AdminDAO" %>
<%@ page import="com.cmg.vrc.data.jdo.Admin" %>
<%@ page import="com.cmg.vrc.util.StringUtil" %>
<html>
<body>
<%--<table>--%>
<%--<%--%>
  <%--Map<String, String> env =System.getenv();--%>
  <%--Iterator<String> keys = env.keySet().iterator();--%>
  <%--while (keys.hasNext()) {--%>
  <%--String key = keys.next();--%>
    <%--%>--%>
  <%--<table>--%>
    <%--<tr>--%>
      <%--<td><%=key%></td>--%>
      <%--<td><%=env.get(key)%></td>--%>
    <%--</tr>--%>

  <%--<hr/>--%>
  <%--<%--%>
  <%--}--%>
<%--%>--%>
  <%--</table>--%>
  <%--<%--%>
    <%--try {--%>
<%--OxfordDictionaryWalker.generateDictionary();} catch (MessagingException e) {--%>
    <%--e.printStackTrace();--%>
<%--}--%>
  <%--%>--%>
  <%
    AdminDAO adminDAO=new AdminDAO();
    double count=adminDAO.getCount();
    Admin admin=new Admin();
    try {
       if(count==0){
       admin.setPassword(StringUtil.md5("12345"));
       admin.setUserName("company@c-mg.com");
       admin.setRole(1);
       admin.setLastName("");
       admin.setFirstName("");
       adminDAO.put(admin);
       }

     }catch (Exception e){
         e.printStackTrace();;
        }

>>>>>>> 1cd3b969443c99f373fc5ada66b8ad1b3f8c6c27
    %>
  <p>done</p>
</body>
</html>