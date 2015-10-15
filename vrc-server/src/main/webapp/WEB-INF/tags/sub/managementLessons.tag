<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>

<div id="page-wrapper">
  <div class="row">
    <div class="col-lg-12">
      <h1 class="page-header">Management Lessons</h1>
    </div>
    <!-- /.col-lg-12 -->
  </div>
  <!-- /.row -->
  <div class="well">
    <div class="row">
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

      </div>
      <div class="col-sm-3">
        <button type="button" id="button-filter" name="button-filter" class="btn btn-primary pull-right" style="margin-top:24px"><i class="fa fa-search"></i> Filter</button>

      </div>
    </div>
  </div>
  <div class="row">
    <div class="col-lg-12">
      <div class="panel panel-default">
        <div class="panel-heading">
          <button type="button" id="openAddLesson" name="addCode">Add New Lesson</button>


        </div>
        <!-- /.panel-heading -->
        <div class="panel-body">
          <div class="dataTable_wrapper">
            <div class="table-responsive">
              <table class="table table-striped table-bordered table-hover table-responsive dt-responsive display nowrap" id="dataTables-example" cellspacing="0">
                <thead>
                <tr>
                  <th>Lessons</th>
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


<div id="add" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <div class="row">
          <div class="col-xs-12 col-md-10 col-md-offset-1">

            <h1 align="center">Add Lesson</h1>
            <form name="add" class="form-horizontal"
                  style="margin-top: 25px" id="addform">

              <div class="form-group">

                <label class="col-xs-4  col-sm-3 control-label ">Lesson:</label>
                <div class="col-xs-8  col-sm-9">
                  <input  type="text" id="addLesson" name="addLesson" class=" form-control" style="padding-left: 0px;">
                </div>
              </div>
              <div class="modal-footer">
                <button type="button" name="yesadd" id="yesadd" class="btn btn-default" value="yes" >Yes</button>
                <button type="button" name="closeadd" id="closeadd" class="btn btn-default" data-dismiss="modal" value="Close" >Close</button>

              </div>

            </form>
          </div>
        </div>
      </div>


    </div>
  </div>
</div>



<div id="edits" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <div class="row">
          <div class="col-xs-12 col-md-10 col-md-offset-1">

            <h1 align="center">Edit Lesson</h1>
            <form name="Edit" class="form-horizontal"
                  style="margin-top: 25px" id="editform">
              <input type="hidden" id="idedit" name="idedit">

              <div class="form-group">
                <label class="col-xs-4  col-sm-3 control-label">Lesson:</label>
                <div class="col-xs-8  col-sm-9">
                  <input  type="text" id="editLesson" name="editLesson" class=" form-control" style="padding-left: 0px;">
                </div>

              </div>
              <div class="modal-footer">
                <button type="button" name="yesedit" id="yesedit" class="btn btn-default" value="yes" >Yes</button>
                <button type="button" name="closeedit" id="closeedit" class="btn btn-default" data-dismiss="modal" value="Close" >Close</button>

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


<script src="<%=request.getContextPath() %>/js/Lession/managementLessons.js"></script>

