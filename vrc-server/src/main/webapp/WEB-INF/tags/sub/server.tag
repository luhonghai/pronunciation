<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%
  String role = null;
  if(session.getAttribute("username") != null && session.getAttribute("password") == null && session.getAttribute("role").equals(1)){
    role="1";
  }
  if(session.getAttribute("username") != null && session.getAttribute("password") == null && session.getAttribute("role").equals(2)){
    role="2";
  }

%>
<input type="hidden" name="role" id="role" value="<%=role%>">

