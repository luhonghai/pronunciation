<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>

<div id="page-wrapper">
  <div class="row">
    <div class="col-lg-12">
      <h1 class="page-header">Teacher and Staff Management</h1>
    </div>
    <!-- /.col-lg-12 -->
  </div>
  <!-- /.row -->


  <!-- /#page-wrapper -->

</div>
<!-- /#wrapper -->


<div id="add" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <div class="row">
          <div class="col-xs-12 col-md-10 col-md-offset-1">

            <h1 align="center">Add Question</h1>
            <form name="add" class="form-horizontal"
                  style="margin-top: 25px" id="addform">

              <div class="form-group">

                <label class="col-xs-4  col-sm-3 control-label ">Question:</label>
                <div class="col-xs-8  col-sm-9">
                  <input  type="text" id="addquestion" name="addquestion" class=" form-control" style="padding-left: 0px;">
                </div>
              </div>
              <div class="form-group">

                <label class="col-xs-4  col-sm-3 control-label ">Description:</label>
                <div class="col-xs-8  col-sm-9">
                  <input  type="text" id="description" name="description" class=" form-control" style="padding-left: 0px;">
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

            <h1 align="center">Edit Question</h1>
            <form name="Edit" class="form-horizontal"
                  style="margin-top: 25px" id="editform">
              <input type="hidden" id="idedit" name="idedit">

              <div class="form-group">
                <label class="col-xs-4  col-sm-3 control-label">Question:</label>
                <div class="col-xs-8  col-sm-9">
                  <input  type="text" id="editquestion" name="editquestion" class=" form-control" style="padding-left: 0px;">
                </div>

              </div>
              <div class="form-group">
                <label class="col-xs-4  col-sm-3 control-label">Description:</label>
                <div class="col-xs-8  col-sm-9">
                  <input  type="text" id="editDescription" name="editDescription" class=" form-control" style="padding-left: 0px;">
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
        <h1 class="modal-title" align="center">Delete Question</h1>
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


<script src="<%=request.getContextPath() %>/js/Lession/managementQuestion.js"></script>

