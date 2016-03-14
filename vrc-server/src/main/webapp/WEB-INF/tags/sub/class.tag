<%@ tag import="com.cmg.merchant.util.SessionUtil" %>
<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<style>
  .title{
    font-size: 16px;
  }
  .textNormal{
    font-size: 14px;
  }
</style>
<%String company= StringUtil.isNull(session.getAttribute(SessionUtil.ATT_CPNAME),"").toString();%>
<div id="page-wrapper">
  <div class="row" style="color:lightgrey; margin-left: 0px;padding-top: 10px;">
    <p class="title" style="float: left;"><%=company%></p><p class="textNormal">>classes</p>
  </div>
  <div>
    <p class="textNormal">Your 'my classes' page is the place where you can bring together your courses and students to create classes.</p>
  </div>

  <div>

  </div>
  <div>
    <p class="textNormal">Make sure that you have created or added the courses you wish to use in ‘my courses’ and that the students are available on your ‘my students’ page. You can add and remove courses and students to and from classes at any time while they are available on these pages.</p>
  </div>


</div>
<!-- /#wrapper -->

<script src="<%=request.getContextPath() %>/js/class.js"></script>




