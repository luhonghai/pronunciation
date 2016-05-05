<%@ tag import="com.cmg.merchant.util.SessionUtil" %>
<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@ tag import="com.cmg.vrc.data.dao.impl.ClassDAO" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<style>


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
    #grap-class-average, .color-class-help{
        width : 10px;
        height : 30px;
        border : 1px solid transparent;
        background-color: #558ED5;
    }

    #grap-student-score, .color-student-help{
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

    .student-avg-score{
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
    .class-avg-score{
        margin-top: 5px;
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
    #drawPhonemes{
        width: 300px;
        height: 300px;
    }
    #drawPhonemes img{
        width: 300px;
        height: 300px;
        padding-bottom: 10px;
    }
    #drawWord{
        width: 300px;
        height: 300px;
    }
    #drawWord img{
        width: 300px;
        height: 300px;
        padding-bottom: 10px;
        margin-left : 10px
    }

    .glyphicon-refresh-animate {
        -animation: spin .7s infinite linear;
        -ms-animation: spin .7s infinite linear;
        -webkit-animation: spinw .7s infinite linear;
        -moz-animation: spinm .7s infinite linear;
    }

    @keyframes spin {
        from { transform: scale(1) rotate(0deg);}
        to { transform: scale(1) rotate(360deg);}
    }

    @-webkit-keyframes spinw {
        from { -webkit-transform: rotate(0deg);}
        to { -webkit-transform: rotate(360deg);}
    }

    @-moz-keyframes spinm {
        from { -moz-transform: rotate(0deg);}
        to { -moz-transform: rotate(360deg);}
    }
    .student-username{
        padding-bottom : 25px;
        padding-top : 25px;
    }
    .class-avg-text , .student-avg-text{
        padding-top : 5px;
    }
    #holder-chart{
        box-sizing: border-box;
        width: 100%;
        height: 600px;
        padding: 20px 15px 15px 15px;
        margin: 15px auto 30px auto;
        border: 1px solid #ddd;
        background: #fff;
        background: linear-gradient(#f6f6f6 0, #fff 50px);
        background: -o-linear-gradient(#f6f6f6 0, #fff 50px);
        background: -ms-linear-gradient(#f6f6f6 0, #fff 50px);
        background: -moz-linear-gradient(#f6f6f6 0, #fff 50px);
        background: -webkit-linear-gradient(#f6f6f6 0, #fff 50px);
        box-shadow: 0 3px 10px rgba(0,0,0,0.15);
        -o-box-shadow: 0 3px 10px rgba(0,0,0,0.1);
        -ms-box-shadow: 0 3px 10px rgba(0,0,0,0.1);
        -moz-box-shadow: 0 3px 10px rgba(0,0,0,0.1);
        -webkit-box-shadow: 0 3px 10px rgba(0,0,0,0.1);
        overflow-x: auto;
    }
    #placeholder{
        width: 100%;
        height: 500px;
        font-size: 14px;
        line-height: 1.2em;
    }
    .legend table, .legend > div {
        height: 82px !important;
        opacity: 1 !important;
        right: -55px; top: 10px;
        width: 116px !important;
        display:none
    }
    .legend table {
        border: 1px solid #555;
        padding: 5px;
        display:none
    }
    #flot-tooltip {
        font-size: 12px;
        font-family: Verdana, Arial, sans-serif; position: absolute;
        display: none; border: 2px solid;
        padding: 2px; background-color: #FFF;
        opacity: 0.8; -moz-border-radius: 5px;
        -webkit-border-radius: 5px;
        -khtml-border-radius: 5px;
        border-radius: 5px;
        z-index : 100000;
    }
    .flot-x-axis div.flot-tick-label {
        /* Rotate Axis Labels */
        transform:  translateX(60%) rotate(60deg); /* CSS3 */
        transform-origin: 0 0;

        -ms-transform:  translateX(60%) rotate(60deg); /* IE */
        -ms-transform-origin: 0 0;

        -moz-transform:  translateX(60%) rotate(60deg); /* Firefox */
        -moz-transform-origin: 0 0;

        -webkit-transform: translateX(60%) rotate(60deg); /* Safari and Chrome */
        -webkit-transform-origin: 0 0;

        -o-transform:  translateX(60%) rotate(60deg); /* Opera */
        -o-transform-origin: 0 0;
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
                    <img id="loadInfo" src="/images/teacher/report_tick48x48.gif"
                         width="36px" height="36px" title="click here to run the reports"
                         style="cursor: pointer;display: block;"/>
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

<div id="report-popup" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
     style="display: none;color:#957F7F">
    <div class="modal-dialog" style="width:90%">
        <div class="modal-content" style="border-radius:20px;background-color: #F2F2F2;">
            <div class="modal-header" style="border-bottom: transparent;">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-sm-4">
                        <div class="col-sm-12">
                            <p class="student-username"></p>
                        </div>
                        <div class="col-sm-12">
                            <p><strong>course:</strong></p>
                            <label id="lbl_course" style='font-weight: 200;'>Course</label>
                        </div>
                        <div class="col-sm-12">
                            <p><strong>level:</strong></p>
                            <label id="lbl_lv" style='font-weight: 200;'>Level</label>
                        </div>
                        <div class="col-sm-12">
                            <p><strong>objective:</strong></p>
                            <label id="lbl_obj" style='font-weight: 200;'>Objective</label>
                        </div>
                        <div class="col-sm-12">
                            <p><strong>lesson:</strong></p>
                            <label id="lbl_lesson" style='font-weight: 200;'>Lesson</label>
                        </div>
                        <div class="col-sm-12">
                            <p><strong>completion date:</strong></p>
                            <label id="lbl_date_completed" style='font-weight: 200;'>date completed</label>
                        </div>
                        <div class="col-sm-12">
                            <div class="col-sm-4">
                                <div class="student-avg-score">80</div>
                            </div>
                            <div class="col-sm-2">
                                <div class="color-student-help"></div>
                            </div>
                            <div class="col-sm-6 student-avg-text">student score</div>
                        </div>
                        <div class="col-sm-12">
                            <div class="col-sm-4">
                                <div class="class-avg-score">80</div>
                            </div>
                            <div class="col-sm-2">
                                <div class="color-class-help"></div>
                            </div>
                            <div class="col-sm-6 class-avg-text">class average</div>
                        </div>
                    </div>
                    <div id="container" class="col-sm-8">
                        <div id="holder-chart">
                            <div id="placeholder">
                            </div>
                        </div>
                    <%--    <div id="tip"></div>--%>
                    </div>
                </div>
            </div>
            <!-- End of Modal body -->
        </div>
        <!-- End of Modal content -->
    </div>
    <!-- End of Modal dialog -->
</div>
<!-- /#wrapper -->


<script src="<%=request.getContextPath() %>/js/merchant/report/draw-report.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/report/reportsLessons.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/flotbar/jquery.flot.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/flotbar/jquery.flot.axislabels.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/flotbar/jquery.flot.categories.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/flotbar/jquery.flot.orderBars.js"></script>





