<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%
    String id = session.getAttribute("id").toString();
%>
<input type="hidden" name="ids" id="ids" value="<%=id%>">

