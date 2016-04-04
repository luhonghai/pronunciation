<%@ tag import="com.cmg.merchant.util.SessionUtil" %>
<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<% String company= StringUtil.isNull(session.getAttribute(SessionUtil.ATT_CPNAME),"").toString();%>
<div id="page-wrapper">
     <div class="row" style="color:lightgrey; margin-left: 0px;">
         <h3><%=company%></h3>
     </div>
    <div>
        <p>Welcome to the accenteasy teacher's console.</p>
        <p>Choose your option from the menu to create, manage and share courses, set up student access and classes or to view reports</p>
    </div>
</div>
<!-- /#wrapper -->
<script src="<%=request.getContextPath() %>/js/welcomeTeacher.js"></script>




