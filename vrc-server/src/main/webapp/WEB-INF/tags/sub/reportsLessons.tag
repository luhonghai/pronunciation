<%@ tag import="com.cmg.merchant.util.SessionUtil" %>
<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%String company= StringUtil.isNull(session.getAttribute(SessionUtil.ATT_CPNAME), "").toString();%>
<div id="page-wrapper">
     <div class="row" style="color:lightgrey; margin-left: 0px;">
         <h3 style="float: left;"><%=company%></h3><p style="margin-top: 25px;">>reports>lessons</p>
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
        <span type="button" id="loadInfo" title="click here to run the reports" style="color:lightgreen;cursor: pointer;margin-left: 5px;" class="fa fa-check-circle fa-2x"> </span>

    </div>
</div>
<!-- /#wrapper -->



<script src="<%=request.getContextPath() %>/js/reportsLessons.js"></script>




