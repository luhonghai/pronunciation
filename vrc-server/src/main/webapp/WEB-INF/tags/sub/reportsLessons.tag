<%@ tag import="com.cmg.merchant.util.SessionUtil" %>
<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<style>
    /*#page-wrapper div{*/
        /*padding-left: 0px;*/
    /*}*/
    /*#graphs div{*/
        /*padding-right: 0px;*/
    /*}*/
</style>
<%String company= StringUtil.isNull(session.getAttribute(SessionUtil.ATT_CPNAME), "").toString();%>
<div id="page-wrapper">
     <div class="row" style="color:lightgrey;">
         <h4 style="float: left;"><%=company%></h4><p style="margin-top: 10px;"> > reports > lessons</p>
     </div>
    <div class="well">
        <div class="row">
            <div class="col-sm-4">
                <div class="form-group">
                    <p style="float:left; margin-top: 10px";>student:</p>
                    <select style="display:none; float: left;margin-left: 3px;" only class="form-control" id="listUsers">
                        <option>1</option>
                        <option>2</option>
                        <option>3</option>
                        <option>4</option>
                        <option>5</option>
                    </select>
                </div>
                <div class="form-group" style="display:none;">
                    <p style="float:left; margin-top: 10px";>objective:</p>
                    <select style="display:none; float: left;margin-left: 3px;" only class="form-control" id="listObjective">
                        <option>1</option>
                        <option>2</option>
                        <option>3</option>
                        <option>4</option>
                        <option>5</option>
                    </select>
                </div>
            </div>

            <div class="col-sm-4">
                <div class="form-group" style="margin-top: 40px;text-align: right;">
                    <p style="float:left; margin-top: 10px";>course:</p>
                    <select style="display:none; float: left;margin-left: 3px;" only class="form-control" id="listCourse">
                        <option>1</option>
                        <option>2</option>
                        <option>3</option>
                        <option>4</option>
                        <option>5</option>
                    </select>
                </div>
            </div>
            <div class="col-sm-4">
                <div class="form-group" style="display:none;">
                    <p style="float:left; margin-top: 10px";>level:</p>
                    <select style="display:none; float: left;margin-left: 3px;" only class="form-control" id="listLevel">
                        <option>1</option>
                        <option>2</option>
                        <option>3</option>
                        <option>4</option>
                        <option>5</option>
                    </select>
                </div>
                <div class="form-group">
                    <img id="loadInfo" src="/images/popup/accepted_48x48.gif" width="36px" height="36px" title="click here to run the reports" style="cursor: pointer;margin-left: 5px;"/>
                </div>
            </div>
        </div>
    </div>
    <%--<div class="row" style="text-align: right; display: none;">--%>
            <%--<div style="background-color: #558ED5;width: 15px;height: 30px;float: left"></div>--%>
            <%--<p style="float:left ">class average</p>--%>
            <%--<div style="background-color: #17375E;width: 15px;height: 30px;float: left"></div>--%>
            <%--<p>student score</p>--%>
    <%--</div>--%>
    <div id="graphs" class="row" style="margin-top: 15px;">
            <div id="info" class="col-sm-3">
                <p><strong>course:</strong></p>
                <label id="course"></label>
                <p><strong>level:</strong></p>
                <label id="level"></label>
                <p><strong>objective:</strong></p>
                <label id="objective"></label>
                <p><strong>lesson:</strong></p>
                <label id="lesson"></label>
                <p><strong>completion date:</strong></p>
                <label id="date"></label>
            </div>
            <div id="score" class="col-sm-1">
                <div style="margin:10px 0px;background-color: #17375E;width: 60px;height: 60px; border-radius:45px; text-align:center;line-height:60px;color:white;font-size:20px;font-weight : 600;">100</div>
                <div style="margin-left:5px;background-color: #558ED5;width: 50px;height: 50px; border-radius:45px; text-align:center;line-height:50px;color:white;font-size:20px;font-weight : 500;">100</div>
            </div>
            <div id="drawPhonemes" class="col-sm-4">
                <p><strong>course:</strong></p>
            </div>
            <div id="drawWord" class="col-sm-4">
                <p><strong>course:</strong></p>
            </div>
    </div>
</div>
<!-- /#wrapper -->



<script src="<%=request.getContextPath() %>/js/reportsLessons.js"></script>





