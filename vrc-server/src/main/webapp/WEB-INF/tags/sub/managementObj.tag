<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>

<div id="page-wrapper">
  <div class="row">
    <div class="col-lg-12">
      <h1 class="page-header">Objective Management</h1>
    </div>
    <!-- /.col-lg-12 -->
  </div>
  <!-- /.row -->
  <div class="well">
    <div class="row">
      <div class="col-sm-3">
        <div class="form-group">
          <label class="control-label">Description</label>
          <input type="text" name="filter-obj" id="description" class="form-control" placeholder="Title">
        </div>
      </div>
      <div class="col-sm-3">
        <div class="form-group" style="margin-top: 32px;text-align: right;">
          <label class="control-label" style="margin-bottom: 0px;">Created Date</label>
        </div>
      </div>
      <div class="col-sm-3">
        <div class="form-group">
          <label class="control-label">From</label>
          <div >
            <input type='text' class="form-control" id='CreateDateFrom' placeholder="From" />
          </div>
        </div>
      </div>
      <div class="col-sm-3">
        <div class="form-group">
          <label class="control-label">To</label>
          <div >
            <input type='text' class="form-control" id='CreateDateTo' placeholder="From" />
          </div>
        </div>
          <button type="button" id="button-filter" name="button-filter" class="btn btn-primary pull-right" style="margin-top:24px"><i class="fa fa-search"></i> Filter</button>

      </div>

    </div>
  </div>
  <div class="row">
    <div class="col-lg-12">
      <div class="panel panel-default">
        <div class="panel-heading">
          <button type="button" id="openAddObjective" name="addCode">Add New Objective</button>


        </div>
        <!-- /.panel-heading -->
        <div class="panel-body">
          <div class="dataTable_wrapper">
            <div class="table-responsive">
              <table class="table table-striped table-bordered table-hover table-responsive dt-responsive display nowrap" id="dataTables-example" cellspacing="0">
                <thead>
                <tr>
                  <th>Name</th>
                  <th>Description</th>
                  <th>Created date</th>
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

<div id="add-objective" class="modal fade in" aria-hidden="false" style="display: none;">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <div class="row">
          <div class="col-xs-12 col-md-11 col-md-offset-1">
            <h1 align="center">Add Objective</h1>
            <form name="add-objective" class="form-horizontal" style="margin-top: 25px" id="add-objective-form">
              <div class="form-group">
                <div>
                  <label class="col-xs-4  col-sm-4 control-label ">Objective:</label>
                  <div class="col-xs-8  col-sm-8">
                    <input type="text" id="add-objective-name" name="add-objective-name" class=" form-control" style="padding-left: 0px; margin-bottom: 5px;">
                  </div>
                </div>
                <div>
                  <label class="col-xs-4  col-sm-4 control-label ">Description:</label>
                  <div class="col-xs-8  col-sm-8">
                    <textarea type="text" id="add-description" name="add-description" rows="3" cols="50" class=" form-control" style="padding-left: 0px;margin-bottom: 5px;"></textarea>
                  </div>
                </div>
              </div>
              <div class="modal-footer">
                <input type="hidden" id="id-obj" class=" form-control">
                <input type="hidden" id="action" class=" form-control">
                <button type="button" name="yesadd" id="yesadd" class="btn btn-default" value="yes" id_level="cd3360ec-ab41-4127-bfdd-0417d7452932">Yes</button>
                <button type="button" name="closeadd" id="closeadd" class="btn btn-default" data-dismiss="modal" value="Close">Close</button>
              </div>
            </form>
          </div>
        </div>
      </div>


    </div>
  </div>
</div>

<div id="deletes" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">&times;</button>
        <h1 class="modal-title" align="center">Delete Objective</h1>
      </div>
      <form name="form-delete" >
        <div class="modal-body">
          <h4 id="label_check_delete">Please wait when we check the mapping data of this objective!</h4>
        </div>
        <div class="modal-footer">
          <button type="button" name="YesDelete" id="deleteItems" class="btn btn-default" >Yes</button>
          <button type="button" name="closedelete" id="closedelete" class="btn btn-default" data-dismiss="modal" value="Close" >Close</button>
        </div>
      </form>
    </div>
  </div>
</div>

<script src="<%=request.getContextPath() %>/js/Lession/managementObjective.js"></script>

