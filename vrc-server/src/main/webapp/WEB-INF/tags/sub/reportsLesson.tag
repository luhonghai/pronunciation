<%@ tag import="com.cmg.merchant.util.SessionUtil" %>
<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%String company= StringUtil.isNull(session.getAttribute(SessionUtil.ATT_CPNAME), "").toString();%>
<style>
    .title{
        font-weight : 600;
        font-family : "Helvetica Neue",Helvetica,Arial,sans-serif;
        font-size : 16px;
        text-align: center;
    }
    #page-wrapper label{
        color: #333;
        font-size: 14px;
        font-family : "Helvetica Neue",Helvetica,Arial,sans-serif;
        font-weight: 200;
    }
    .header-company {
        color: #A6A6A6;
        font-weight: 200;
        font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
        font-size: 14px;
        border-bottom-color: transparent;
        margin : 10px 0px;
    }

    #selection-filter{
        padding-left: 0px;
    }
    #selection-filter label{
        font-weight: 200;
        padding-top : 5px;
    }

</style>
<div id="page-wrapper">
    <div class="row">
        <div class="col-sm-12">
            <h4 style="padding-bottom: 0px" class="page-header header-company"><%=company%> >
                reports</h4>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-12">
            <h2 style="font-size: 20px;font-weight: 500;color:#17375E;margin-top:0px;" class="header">reports</h2>
        </div>
        <div id="notification" class="col-sm-12">
            <label style="color : #376092">You had not created any class, to create a class :</label>
                <a href="my-classes.jsp"> click here</a>
        </div>
        <div id="selection-filter" class="col-sm-12">
            <div class="col-sm-1"><label>classes:</label></div>
            <div class="col-sm-3">
                <select class="form-control" id="listClass">
                </select>
            </div>
            <div class="col-sm-1"><label>student:</label></div>
            <div class="col-sm-3">
                <select class="form-control" id="listUsers">
                </select>
            </div>
            <div class="col-sm-2">
                <img id="loadInfo" src="/images/teacher/report_tick48x48.gif"
                     width="36px" height="36px" title="click here to run the reports"
                     style="cursor: pointer;margin-left:5px" disabled/>
            </div>
        </div>
        <div id="first-process" class="col-lg-4" style="padding-top: 20px"></div>
        <div id="process-bar" class="col-lg-4" style="padding-top: 20px"></div>
        <div id="last-process" class="col-lg-4" style="padding-top: 20px"></div>
    </div>
</div>
<!-- /#wrapper -->
<script src="<%=request.getContextPath() %>/js/merchant/report/reportsLesson.js"></script>
