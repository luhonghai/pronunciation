<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%String teacherName=session.getAttribute("username").toString();%>
<div id="page-wrapper">
  <div class="row">
    <div class="col-lg-12">
      <input type="hidden" id="teacher" value="<%=teacherName%>">
      <h1 class="page-header">send invite to students</h1>
    </div>
    <!-- /.col-lg-12 -->
  </div>
  <div class="form-group">
    <label style="font-weight: 500;font-size: 200%">
      student list:
    </label>
  </div>
  <div style="border-style: ridge; border-width: 1px;">
    <div class="row" style="padding: 10px; margin-right: 0px;margin-left: 0px;padding-bottom: 0px;">
      <div class="form-group">
        <textarea type="text" name="listmail" id="listmail" class="form-control" rows="10" required="required" placeholder="List Mail"></textarea>
      </div>
    </div>
  </div>
  <div class="row" style="margin-top: 20px;">
    <div class="col-sm-2">
      <button id="send" class="btn">Send</button>
    </div>
  </div>

  <!-- /.row -->

  <!-- /#page-wrapper -->

</div>
<!-- /#wrapper -->


<script src="<%=request.getContextPath() %>/js/mailuser.js"></script>




