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
          <label class="control-label">Name</label>
          <input type="text" name="filter-obj" id="object_name_filter" class="form-control" placeholder="Title">
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
          <button type="button" id="openAddLesson" name="addCode">Add New Objective</button>


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

<div id="add-objective" class="modal fade in" aria-hidden="false" style="display: block;">
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
                <div id="container-add-lesson">
                  <label class="col-xs-4  col-sm-4 control-label ">Lessons:</label>
                  <div class="col-xs-8  col-sm-8" style="padding-left: 0px;">
                    <img class="loading-lesson loading" src="http://i.imgur.com/m1fR7ef.gif" style="display: none;">
                    <select style="display:none;" multiple="" class="form-control" id="select-lesson"><option value="00fe1ec4-b18d-4e2d-8cb5-52782a1ad40b">test5</option><option value="252813ef-f433-49d7-b700-f6c369fc7836">test9</option><option value="4fa3c0b2-081d-42e5-a5c8-3e1e29f390c1">test7</option><option value="61759d9f-6674-413a-b3f6-20fd02aab734">test4</option><option value="8cc6dda7-cf5e-42b1-9b31-8f745568c59b">test6</option><option value="a28ab2a2-a8e2-4683-8a35-aadcf6afc8b0">test8</option><option value="ad5f6fc2-b4f8-47d1-96bc-1258b328f4e8">test2</option><option value="f5484e0d-9e91-44d2-9e2b-c846331c8d5a">test3</option></select><div class="btn-group" style="width: 200px; padding-left: 14px;"><button type="button" class="multiselect dropdown-toggle btn btn-default" data-toggle="dropdown" title="None selected" style="width: 200px; overflow: hidden; text-overflow: ellipsis;"><span class="multiselect-selected-text">None selected</span> <b class="caret"></b></button><ul class="multiselect-container dropdown-menu"><li class="multiselect-item filter" value="0"><div class="input-group"><span class="input-group-addon"><i class="glyphicon glyphicon-search"></i></span><input class="form-control multiselect-search" type="text" placeholder="Search"><span class="input-group-btn"><button class="btn btn-default multiselect-clear-filter" type="button"><i class="glyphicon glyphicon-remove-circle"></i></button></span></div></li><li><a tabindex="0"><label class="checkbox"><input type="checkbox" value="00fe1ec4-b18d-4e2d-8cb5-52782a1ad40b"> test5</label></a></li><li><a tabindex="0"><label class="checkbox"><input type="checkbox" value="252813ef-f433-49d7-b700-f6c369fc7836"> test9</label></a></li><li><a tabindex="0"><label class="checkbox"><input type="checkbox" value="4fa3c0b2-081d-42e5-a5c8-3e1e29f390c1"> test7</label></a></li><li><a tabindex="0"><label class="checkbox"><input type="checkbox" value="61759d9f-6674-413a-b3f6-20fd02aab734"> test4</label></a></li><li><a tabindex="0"><label class="checkbox"><input type="checkbox" value="8cc6dda7-cf5e-42b1-9b31-8f745568c59b"> test6</label></a></li><li><a tabindex="0"><label class="checkbox"><input type="checkbox" value="a28ab2a2-a8e2-4683-8a35-aadcf6afc8b0"> test8</label></a></li><li><a tabindex="0"><label class="checkbox"><input type="checkbox" value="ad5f6fc2-b4f8-47d1-96bc-1258b328f4e8"> test2</label></a></li><li><a tabindex="0"><label class="checkbox"><input type="checkbox" value="f5484e0d-9e91-44d2-9e2b-c846331c8d5a"> test3</label></a></li></ul></div>
                  </div>
                </div>
              </div>
              <div class="modal-footer">
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
        <h1 class="modal-title" align="center">Delete Lesson</h1>
      </div>
      <form name="form-delete" >
        <div class="modal-body">
          <input type="hidden" id="iddelete" name="iddelete">
          <h3>Do you want to delete ?</h3>
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

