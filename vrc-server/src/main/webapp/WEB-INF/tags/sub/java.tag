<%@tag description="Java" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%
    String user=request.getParameter("username");
%>
<input type="hidden" name="user" id="user" value="<%=user%>">

