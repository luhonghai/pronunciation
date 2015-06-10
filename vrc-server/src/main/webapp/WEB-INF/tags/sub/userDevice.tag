<%@tag description="userDevice" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%
  String emei=request.getParameter("emei");
%>
<input type="hidden" name="emei" id="emei" value="<%=emei%>">

