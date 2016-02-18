<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<div id="page-wrapper">
  <div class="row">
    <div class="col-lg-12">
      <h1 class="page-header">Manage Mapping Studnet</h1>
    </div>
    <!-- /.col-lg-12 -->
  </div>
  <!-- /.row -->
  <div class="row">
    <div class="col-lg-12">
      <div class="panel panel-default">
        <div class="panel-heading">
        </div>
        <!-- /.panel-heading -->
        <div class="panel-body">
          <div class="dataTable_wrapper">
            <div class="table-responsive">
            <table class="table table-striped table-bordered table-hover table-responsive dt-responsive display nowrap" id="dataTables-example" cellspacing="0">
              <thead>
              <tr>
                <th><input id="selectAll" type="checkbox" style="width: 30px;height: 30px;"><i id="deleteStudents" class="fa fa-times fa-3x" style="color: #ff0000;margin-left: 10px;" title="Delete student(s) from your list students"></i></th>
                <th>StudentName</th>
                <th>Status</th>

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

<script src="<%=request.getContextPath() %>/js/myStudents.js"></script>




