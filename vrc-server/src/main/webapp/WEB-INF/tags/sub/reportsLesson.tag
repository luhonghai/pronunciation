<%@ tag import="com.cmg.merchant.util.SessionUtil" %>
<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%String company= StringUtil.isNull(session.getAttribute(SessionUtil.ATT_CPNAME), "").toString();%>

<div id="page-wrapper">
    <div class="row" style="color:lightgrey; margin-left: 0px;">
        <h4 style="float: left;"><%=company%></h4><p style="margin-top: 10px;"> > reports > lessons</p>
    </div>
    <div>
        <p style="float:left; margin-top: 10px";>student:</p>
        <select style="display:none; float: left;margin-left: 3px;" only class="form-control" id="listUsers">
            <option>1</option>
            <option>2</option>
            <option>3</option>
            <option>4</option>
            <option>5</option>
        </select>
        <img id="loadInfo" src="/images/popup/accepted_48x48.gif" width="36px" height="36px" title="click here to run the reports" style="cursor: pointer;margin-left: 5px; background-color: green;border-radius: 45px;"/>
        <%--<span type="button" id="loadInfo" title="click here to run the reports" style="color:lightgreen;cursor: pointer;margin-left: 5px;" class="fa fa-check-circle fa-2x"> </span>--%>

    </div>
    <div id="graphs">

    </div>
</div>
<!-- /#wrapper -->
<script src="<%=request.getContextPath() %>/js/reportsLesson.js"></script>
