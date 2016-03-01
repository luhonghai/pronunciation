<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%
  String company = (String) StringUtil.isNull(request.getSession().getAttribute("company"),"");
%>
<style>
  .welcome{
    color : #376092;
    font-weight: 200;
  }
  .header-company{
    color: #A6A6A6
  }

</style>
<div id="page-wrapper">
  <div class="row">
    <div class="col-lg-12">
      <h1 class="page-header"><%=company%></h1>
    </div>
    <!-- /.col-lg-12 -->
  </div>

  <!-- /.row -->
  <div class="row">
    <div class="col-lg-12">
      <label class="welcome">Welcome to accenteasy course and student management.</label>
    </div>
  </div>

  <!-- /.row -->
  <div class="row">
    <div class="col-lg-12">
      <label class="welcome">Choose your option from the menu to create, manage and share courses, or to set up student access and classes.</label>
    </div>
  </div>
  <!-- /#page-wrapper -->

</div>
<!-- /#wrapper -->




