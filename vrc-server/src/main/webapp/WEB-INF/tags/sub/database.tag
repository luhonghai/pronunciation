<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%
  String role=session.getAttribute("role").toString();
  String id = session.getAttribute("id").toString();
%>
<input type="hidden" name="ids" id="ids" value="<%=id%>">
<input type="hidden" name="role" id="role" value="<%=role%>">
<%
  if (session.getAttribute("role")==null){
    return;
  }
  if(session.getAttribute("role").equals(1) || session.getAttribute("role").equals(2) ){
%>
<style>
  ::-webkit-scrollbar {
    -webkit-appearance: none;
    width: 7px;
  }
  ::-webkit-scrollbar-thumb {
    border-radius: 4px;
    background-color: rgba(0,0,0,.5);
    -webkit-box-shadow: 0 0 1px rgba(255,255,255,.5);
  }
</style>
<div id="page-wrapper">
  <div class="row">
    <div class="col-lg-12">
      <h1 class="page-header">Database management</h1>
    </div>
    <!-- /.col-lg-12 -->
  </div>
  <div class="row">
    <div class="col-lg-12">
      <div class="panel panel-default">
        <div class="panel-heading">
          <button type="button" id="btnPopup" name="addCode" class="btn btn-primary" disabled="disabled">Upload</button>
          &nbsp;&nbsp;&nbsp;
          <button type="button" id="btnPopupGenerate" name="btnPopupGenerate" class="btn btn-primary" disabled="disabled">Generate</button>
          &nbsp;&nbsp;&nbsp;
          <button type="button" id="btnStop" name="btnStop" class="btn btn-danger" style="display: none">Force stop</button>
        </div>
        <!-- /.panel-heading -->
        <div class="panel-body">
          <div style="margin: 4px;">
            <a id="btnLatestRunningLog" href="<%=request.getContextPath()%>/database?action=latest_log" class="btn btn-primary btn-xs" target="_blank">Latest running log</a>
          </div>
          <div id="generate-log" style="margin: 10px; max-height: 400px; overflow-y: scroll"></div>
          <div class="dataTable_wrapper">
            <div class="table-responsive">
            <table class="table table-striped table-bordered table-hover table-responsive dt-responsive display nowrap" id="dataTables-example" cellspacing="0">
              <thead>
              <tr>
                <th>Version</th>
                <th>Admin</th>
                <th>File name</th>
                <th>Type</th>
                <th>File change</th>
                <th>Title Notification</th>
                <th>Created Date</th>
                <th>Selected Date</th>
                <th>Select version</th>
              </tr>
              </thead>
              <tbody>

              </tbody>
            </table>
              </div>

          </div>
          <!-- /.panel-body -->
        </div>
        <!-- /.panel -->
      </div>
      <!-- /.col-lg-12 -->
    </div>

  </div>
  <!-- /#page-wrapper -->

</div>
<!-- /#wrapper -->

<div id="popupGenerateAction" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <div class="row">
          <div class="col-xs-12 col-md-10 col-md-offset-1">
            <h1 align="center">Generate database</h1>
            <p>New database version will be generated by all lesson tables</p>
            <select id="selType">
                <option value="1" selected="selected">New</option>
              <option value="0">Old</option>
            </select>
            <label class="control-label">Title Notification:</label>
            <textarea type="text" id="notification" name="lessonChange" rows="2" cols="50" class=" form-control" style="padding-left: 0px;margin-bottom: 5px;"></textarea>
            <label class="control-label">Lesson change:</label>
            <textarea type="text" id="lessonChange" name="lessonChange" rows="3" cols="50" class=" form-control" style="padding-left: 0px;margin-bottom: 5px;"></textarea>
            <div class="modal-footer">
              <button type="button" name="yesadd" id="btnGenerate" class="btn btn-primary" value="yes" >Generate</button>
              <button type="button" name="closeadd" class="btn btn-default" data-dismiss="modal" value="Close" >Close</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<div id="popupGenerate" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <div class="row">
          <div class="col-xs-12 col-md-10 col-md-offset-1">
              <h1 align="center">Upload database</h1>
              <div id="fileuploader">Upload</div>
              <div class="modal-footer">
                <button type="button" name="closeadd" class="btn btn-default" data-dismiss="modal" value="Close" >Close</button>
              </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<%
  }
%>
<%
  if (session.getAttribute("role")==null){
    return;
  }
  if(session.getAttribute("role").equals(3) || session.getAttribute("role").equals(4)){
%>
<div id="page-wrapper">
  <div class="row">
    <h2 style="text-align: center; color: red;">You do not have access to this page!</h2>
  </div>
</div>
<%
  }
%>


<script src="<%=request.getContextPath() %>/js/database.js"></script>
