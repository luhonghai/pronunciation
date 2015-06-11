<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.cmg.vrc.dictionary.OxfordDictionaryWalker" %>
<%@ page import="javax.mail.MessagingException" %>
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

    %>
  <p>done</p>
</body>
</html>