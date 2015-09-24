<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html> <!--<![endif]-->
<%@tag description="Main screen" pageEncoding="UTF-8" %>
<%@taglib prefix="include" tagdir="/WEB-INF/tags/sub" %>
<%@attribute name="pageTitle" required="true" %>
<%@attribute name="index"%>
    <include:head pageTitle="<%=pageTitle%>">
    </include:head>
<body>
<%

    if (session.getAttribute("username") == null || session.getAttribute("password") == null){
        response.sendRedirect("login.jsp");
    } else {

%>
	<include:header index="<%=index %>"></include:header>
	<jsp:doBody/>
    <include:footer></include:footer>
</body>
</html>
<%}%>
