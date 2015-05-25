<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.cmg.vrc.dictionary.OxfordDictionaryWalker" %>
<%@ page import="javax.mail.MessagingException" %>
<html>
<body>
<table>
<%
  Map<String, String> env =System.getenv();
  Iterator<String> keys = env.keySet().iterator();
  while (keys.hasNext()) {
    String key = keys.next();
    %>
  <table>
    <tr>
      <td><%=key%></td>
      <td><%=env.get(key)%></td>
    </tr>

  <hr/>
  <%
  }
%>
  </table>
  <%
    try {
OxfordDictionaryWalker.generateDictionary();} catch (MessagingException e) {
    e.printStackTrace();
}
  %>
  <p>done</p>
</body>
</html>