<%@ tag import="com.cmg.merchant.util.SessionUtil" %>
<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<% String company= StringUtil.isNull(session.getAttribute(SessionUtil.ATT_CPNAME),"").toString();%>
<style>
    .header-company {
        color: #A6A6A6;
        font-weight: 200;
        font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
        font-size: 14px;
    }
    p{
        color: #376092;
        font-weight: 200;
        font-size: 14px;
        font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
        font-size: 14px;
    }

</style>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h4 style="border-bottom: transparent" class="header-company">
                <%=company%>
            </h4>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-12">
            <p>Welcome to the accenteasy teacher's console.</p>
            <p>Choose your option from the menu to create, manage and share courses, set up student access and classes or to view reports</p>
        </div>
    </div>
</div>
<!-- /#wrapper -->
<script src="<%=request.getContextPath() %>/js/welcomeTeacher.js"></script>




