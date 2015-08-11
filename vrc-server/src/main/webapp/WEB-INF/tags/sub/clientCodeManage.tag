<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<div id="page-wrapper">
  <div class="row">
    <div class="col-lg-12">
      <h1 class="page-header">Client Code Manage</h1>
    </div>
    <!-- /.col-lg-12 -->
  </div>
  <!-- /.row -->
  <div class="well">
    <div class="row">
      <div class="col-sm-3">
        <div class="form-group">
          <label class="control-label">CompanyName</label>
          <input type="text" name="filter-companyname" id="companyname" class="form-control" placeholder="CompanyName">
        </div>
      </div>
      <div class="col-sm-3">
        <div class="form-group">
          <label class="control-label">ContactName</label>
          <input type="text" name="filter-contactname" id="contactname" class="form-control" placeholder="ContactName">
        </div>
      </div>
      <div class="col-sm-3">
        <div class="form-group">
          <label class="control-label">Email</label>
          <input type="text" name="filter-email" id="email" class="form-control" placeholder="Email">
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
          <button type="button" id="adduser" name="addUser">Add Company</button>

        </div>
        <!-- /.panel-heading -->
        <div class="panel-body">
          <div class="dataTable_wrapper">
            <div class="table-responsive">
              <table class="table table-striped table-bordered table-hover table-responsive dt-responsive display nowrap" id="dataTables-example" cellspacing="0">
                <thead>
                <tr>
                  <th>CompanyName</th>
                  <th>ContactName</th>
                  <th>Email</th>
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

            <h1 align="center">Add User</h1>
            <form name="add" class="form-horizontal"
                  style="margin-top: 25px" id="addform">

              <div class="form-group">
                <p id="UserNameExitAdd" class="UserNameExitAdd" style="color:red;margin-left:50px;display: none;">Company name exits</p>
                <label class="col-xs-4  col-sm-3 control-label ">CompanyName:</label>
                <div class="col-xs-8  col-sm-9">
                  <input  type="text" id="addcompanyname" name="addcompanyname" class=" form-control" style="padding-left: 0px;">
                  <p id="addcompanynames" class="addcompanynames" style="color:red; display: none;">Required field to enter data</p>
                  </div>
              </div>
              <div class="form-group">
                <label class="col-xs-4  col-sm-3 control-label">ContactName:</label>
                <div class="col-xs-8  col-sm-9">
                  <input type="text" id="addcontactname" name="addcontactname" class="col-xs-8  col-sm-9 form-control" style="padding-left: 0px;">
                  <p id="addcontactnames" class="addcontactnames" style="color:red; display: none;">Required field to enter data</p>
                </div>

              </div>
              <div class="form-group">
                <label class="col-xs-4  col-sm-3 control-label">Email:</label>
                <div class="col-xs-8  col-sm-9">
                  <input type="text" id="addemail" name="addemail" class="col-xs-8  col-sm-9 form-control" style="padding-left: 0px;">
                  <p id="nameadds" class="nameadd" style="color:red; display: none;">Required field to enter data</p>
                  <p id="nameaddsemail" class="nameaddemail" style="color:red; display: none;">Email format wrong!</p>
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

            <h1 align="center">Edit User</h1>
            <form name="Edit" class="form-horizontal"
                  style="margin-top: 25px" id="editform">
              <input type="hidden" id="idedit" name="idedit">

              <div class="form-group">
                <label class="col-xs-4  col-sm-3 control-label">CompanyName:</label>
                <div class="col-xs-8  col-sm-9">
                  <input  type="text" id="editcompanyname" name="editcompanyname" class=" form-control" style="padding-left: 0px;" disabled>
                </div>

              </div>
              <div class="form-group">
                <label class="col-xs-4  col-sm-3 control-label">ContactName:</label>
                <div class="col-xs-8  col-sm-9">
                  <input  type="text" id="editcontactname" name="editcontactname" class=" form-control" style="padding-left: 0px;">
                </div>

              </div>
              <div class="form-group">
                <label class="col-xs-4  col-sm-3 control-label">Email:</label>
                <div class="col-xs-8  col-sm-9">
                  <input  type="text" id="editemail" name="editemail" class=" form-control" style="padding-left: 0px;">
                  <p id="nameedits" class="nameadd" style="color:red; display: none;">Required field to enter data</p>
                  <p id="nameeditsemail" class="nameaddemail" style="color:red; display: none;">Email format wrong!</p>

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
        <h1 class="modal-title" align="center">Delete Item</h1>
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




