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
  if(session.getAttribute("role").equals(1)){
%>
<div id="page-wrapper">
  <div class="row">
    <div class="col-lg-12">
      <h1 class="page-header">Manage Recording</h1>
    </div>
    <!-- /.col-lg-12 -->
  </div>
  <!-- /.row -->
  <div class="well">
    <div class="row">
      <div class="col-sm-3">
        <div class="form-group">
          <label class="control-label">Sentence</label>
          <input type="text" name="filter-sentence" id="sentences" class="form-control" placeholder="Sentence">
        </div>
        <div class="form-group">
          <label class="control-label">Status</label>
          <select name="status" id="status" class="form-control" required="required">
            <option value="6">All</option>
            <option value="1">pending</option>
            <option value="2">rejected</option>
            <option value="3">approved</option>
            <option value="4">locked</option>

          </select>
        </div>
      </div>

      <div class="col-sm-3">
        <div class="form-group" style="margin-top: 40px;text-align: right;">
          <label class="control-label" style="margin-bottom: 0px;">Recorded Date</label>
        </div>
        <div class="form-group">
          <label class="control-label">Account</label>
          <input type="text" name="filter-account" id="account" class="form-control" placeholder="Account">
        </div>
      </div>
      <div class="col-sm-3">
        <div class="form-group">
          <label class="control-label">From</label>
          <div>
            <input type='text' class="form-control" id="dateFrom" placeholder="From"/>
          </div>
        </div>
      </div>
      <div class="col-sm-3">
        <div class="form-group">
          <label class="control-label">To</label>
          <div >

            <input type='text' class="form-control" id="dateTo" placeholder="To" />

          </div>
        </div>
        <button type="button" id="buttonFilter" name="button-filter" class="btn btn-primary pull-right" style="margin-top:24px"><i class="fa fa-search"></i> Filter</button>
      </div>

    </div>
  </div>
  <div class="row">
    <div class="col-lg-12">
      <div class="panel panel-default">
        <div class="panel-heading">
          <div class="row">
            <label style="background-color: orange;color: white; padding-left: 5px;">Pending:</label>
            <label id="pending" style="padding-right: 10px;"></label>
            <label style="background-color: red;color: white; padding-left: 5px;">Rejected:</label>
            <label id="reject" style="padding-right: 10px;"></label>
            <label style="background-color: green;color: white; padding-left: 5px;">Approved:</label>
            <label id="approved" style="padding-right: 10px;"></label>
            <label style="background-color: darkgray;color: white; padding-left: 5px;">Locked:</label>
            <label id="locked" style="padding-right: 10px;"></label>
            <div class="pull-right" style="margin-right: 20px">
            <label style="background-color: white;color: black; padding-left: 5px;">Number Account:</label>
            <label id="numberAccount"></label>
            <label style="background-color: white;color: black; padding-left: 5px;">Sentence:</label>
            <label id="all"></label>
            </div>
          </div>
          <div class="row" style="padding-top: 10px;">
            <label class="col-sm-3" style="padding-left: 0px;">Select user:</label>
            <div class="col-sm-6 pull-right">
              <select name="listaccount" id="listaccount" class="form-control" required="required">

              </select>
            </div>
          </div>
        </div>
        <!-- /.panel-heading -->
        <div class="panel-body">
          <div class="dataTable_wrapper">
            <div class="table-responsive">
              <table class="table table-striped table-bordered table-hover table-responsive dt-responsive display nowrap" id="dataTables-example" cellspacing="0">
                <thead>
                <tr>
                  <th>Account</th>
                  <th>Sentence</th>
                  <th>Audio</th>
                  <th>Recorded Date</th>
                  <th>Status</th>
                  <th></th>
                  <th></th>
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

<%
  }
%>
<%
  if (session.getAttribute("role")==null){
    return;
  }
  if(session.getAttribute("role").equals(2)){
%>
<div id="page-wrapper">
  <div class="row">
    <h2 style="text-align: center; color: red;">You do not have access to this page!</h2>
  </div>
</div>
<%
  }
%>

<script src="<%=request.getContextPath() %>/js/adminRecorder.js"></script>



