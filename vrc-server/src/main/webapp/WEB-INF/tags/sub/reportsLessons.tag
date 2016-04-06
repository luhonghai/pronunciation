<%@ tag import="com.cmg.merchant.util.SessionUtil" %>
<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@ tag import="com.cmg.vrc.data.dao.impl.ClassDAO" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<style>
    #canvas{
    background: #ffffff;
    box-shadow:5px 5px 5px #ccc;
    border:3px solid #eee;
    margin-bottom:10px;}
    #tip {
    position: absolute;
    display: none;
    background-color:red;
    border-radius: 4px;
    top:0;
    left:0;}
    #canvasWord{
        background: #ffffff;
        box-shadow:5px 5px 5px #ccc;
        border:3px solid #eee;
        margin-bottom:10px;}
    #tipWord {
        position: absolute;
        display: none;
        background-color:red;
        border-radius: 4px;
        top:0;
        left:0;}
    .header-company {
        color: #A6A6A6;
        font-weight: 200;
        font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
        font-size: 14px;
        border-bottom-color: transparent;
        margin : 10px 0px;
    }
    .well label {
        font-weight: 200;
        font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
        font-size: 14px;
        padding-top: 5px;
    }
    .well .row{
        padding-bottom : 20px;
    }
    #grap-class-average{
        width : 10px;
        height : 30px;
        border : 1px solid transparent;
        background-color: #558ED5;
    }

    #grap-student-score{
        width : 10px;
        height : 30px;
        border : 1px solid transparent;
        background-color: #17375E;
    }
    #info label{
        color :#376092
    }
    .grap{
        border: 1px;
        background-color: #F2F2F2;
        border-radius: 5px;
        padding-top: 10px;
        margin-bottom: 20px;
    }
    .scoreStudent{
        margin: 30px 0px;
        background-color: #17375E;
        width: 70px;
        height: 70px;
        border-radius: 45px;
        text-align: center;
        line-height: 70px;
        color: white;
        font-size: 20px;
        font-weight: 600;
    }
    .scoreClass{
        margin-top: 25px;
        margin-left: 5px;
        background-color: #558ED5;
        width: 60px;
        height: 60px;
        border-radius: 45px;
        text-align: center;
        line-height: 60px;
        color: white;
        font-size: 20px;
        font-weight: 500;
    }
</style>
<%
    ClassDAO dao = new ClassDAO();
    String company = StringUtil.isNull(session.getAttribute(SessionUtil.ATT_CPNAME), "").toString();
    String student = StringUtil.isNull(request.getParameter("name"),"").toString();
    String idClass = StringUtil.isNull(request.getParameter("idClass"),"").toString();
    String className = StringUtil.isNull(dao.getById(idClass).getClassName(),"").toString();
%>
<div id="page-wrapper">
    <input id="studentName" type="hidden" value="<%=student%>">
    <input id="class-id" type="hidden" value="<%=idClass%>">
    <input id="class-name" type="hidden" value="<%=className%>">
    <div class="row">
        <div class="col-sm-12">
            <h4 class="page-header header-company"><%=company%> >
                reports > lessons</h4>
        </div>
    </div>
    <div class="row well">
        <div id="contain-disabled-value" class="row">
            <div class="col-sm-4">
                <div class="col-sm-3">
                    <label>class:</label>
                </div>
                <div class="col-sm-9">
                    <select class="form-control" id="listClass" disabled="disabled">
                    </select>
                </div>
            </div>
            <div class="col-sm-4">
                <div class="col-sm-3">
                    <label>student:</label>
                </div>
                <div class="col-sm-9">
                    <select class="form-control" id="listUser" disabled="disabled">
                    </select>
                </div>
            </div>
            <div class="col-sm-4">
                <div class="col-sm-3">
                    <label>course:</label>
                </div>
                <div class="col-sm-9">
                    <select class="form-control" id="listCourse">
                    </select>
                </div>
            </div>
        </div>
        <div id="contain-course-value" class="row">
            <div class="col-sm-4">
                <div class="col-sm-3">
                    <label>level:</label>
                </div>
                <div class="col-sm-9">
                    <select class="form-control" id="listLevel">
                    </select>
                </div>
            </div>
            <div class="col-sm-4">
                <div class="col-sm-3">
                    <label>objective:</label>
                </div>
                <div class="col-sm-9">
                    <select class="form-control" id="listObj">
                    </select>
                </div>
            </div>
            <div class="col-sm-4">
                <div class="col-sm-3">
                    <img id="loadInfo" src="/images/popup/accepted_48x48.gif"
                         width="36px" height="36px" title="click here to run the reports"
                         style="cursor: pointer;margin-left: 5px; background-color: #33CC33;border-radius: 45px;display: block;"/>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-6">
                <div class="col-sm-1">
                    <div id="grap-class-average">
                    </div>
                </div>
                <div class="col-sm-4">
                    <label>class average</label>
                </div>
                <div class="col-sm-1">
                    <div id="grap-student-score">
                    </div>
                </div>
                <div class="col-sm-4">
                    <label>student score</label>
                </div>
            </div>
        </div>
    </div>
    <div id="draw" class="row" style="padding-bottom: 20px">

    </div>
</div>
<!-- /#wrapper -->



<script src="<%=request.getContextPath() %>/js/reportsLessons.js"></script>
<script src="<%=request.getContextPath() %>/js/draw-report.js"></script>




