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
      <h1 class="page-header">Admin Manage</h1>
    </div>
    <!-- /.col-lg-12 -->
  </div>
  <!-- /.row -->
  <div class="well">
    <div class="row">
      <div class="col-sm-3">
        <div class="form-group">
          <label class="control-label">UserName</label>
          <input type="text" name="filter-username" id="username" class="form-control" placeholder="UserName">
        </div>
      </div>
      <div class="col-sm-3">
        <div class="form-group">
          <label class="control-label">FirstName</label>
          <input type="text" name="filter-firstname" id="firstname" class="form-control" placeholder="FirstName">
        </div>
      </div>
      <div class="col-sm-3">
        <div class="form-group">
          <label class="control-label">LastName</label>
          <input type="text" name="filter-firstname" id="lastname" class="form-control" placeholder="lastName">
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
          <button type="button" id="addUser" name="addCode">Add User</button>

        </div>
        <!-- /.panel-heading -->
        <div class="panel-body">
          <div class="dataTable_wrapper">
            <table class="table table-striped table-bordered table-hover table-responsive" id="dataTables-example">
              <thead>
              <tr>
                <th>UserName</th>
                <th>FirstName</th>
                <th>LastName</th>
                <th>Role</th>
                <th></th>
              </tr>
              </thead>
              <tbody>

              </tbody>
            </table>

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
                <p id="UserNameExitAdd" class="UserNameExitAdd" style="color:red;margin-left:50px;display: none;">User name exits</p>
                <label class="col-xs-4  col-sm-3 control-label ">UserName:</label>
                <div class="col-xs-8  col-sm-9">
                  <input  type="text" id="addusername" name="addusername" class=" form-control" style="padding-left: 0px;">
                  <p id="nameadds" class="nameadd" style="color:red; display: none;">Required field to enter data</p>
                  <p id="nameaddsemail" class="nameaddemail" style="color:red; display: none;">Email format wrong!</p>
                </div>
              </div>
              <div class="form-group">
                <label class="col-xs-4  col-sm-3 control-label">FirstName:</label>
                <div class="col-xs-8  col-sm-9">
                  <input type="text" id="addfirstname" name="addfirstname" class="col-xs-8  col-sm-9 form-control" style="padding-left: 0px;">
                </div>

              </div>
              <div class="form-group">
                <label class="col-xs-4  col-sm-3 control-label">LastName:</label>
                <div class="col-xs-8  col-sm-9">
                  <input type="text" id="addlastname" name="addlastname" class="col-xs-8  col-sm-9 form-control" style="padding-left: 0px;">
                </div>

              </div>
              <div class="form-group">

                <label class="col-xs-4  col-sm-3 control-label">Password:</label>
                <div class="col-xs-8  col-sm-9">
                  <input type="password" id="addpassword" name="addpassword" class="col-xs-8  col-sm-9 form-control" style="padding-left: 0px;">
                  <p id="passadds" class="passadd" style="color:red; display: none">Required field to enter data</p>
                </div>

              </div>
              <div class="form-group">
                <label class="col-xs-4  col-sm-3 control-label">Role:</label>
                <div class="col-xs-4  col-sm-5">
                  <select name="addrole" id="addrole" class="form-control" required="required">
                    <option value="Admin">Admin</option>
                    <option value="User">User</option>
                  </select>
                  <p id="roleadds" class="roleadd" style="color:red; display: none">Required field to enter data</p>
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
                <p id="UserNameExitUpdate" class="UserNameExitUpdate" style="color:red;margin-left:50px;display: none;">User name exits</p>
                <label class="col-xs-4  col-sm-3 control-label">UserName:</label>
                <div class="col-xs-8  col-sm-9">
                  <input  type="text" id="editusername" name="editusername" class=" form-control" style="padding-left: 0px;">
                  <p id="nameeditsemail" class="nameeditemail" style="color:red; display: none;">Email format wrong!</p>
                </div>
              </div>
              <div class="form-group">
                <label class="col-xs-4  col-sm-3 control-label">FirstName:</label>
                <div class="col-xs-8  col-sm-9">
                  <input  type="text" id="editfirstname" name="editfirstname" class=" form-control" style="padding-left: 0px;">
                </div>

              </div>
              <div class="form-group">
                <label class="col-xs-4  col-sm-3 control-label">LastName:</label>
                <div class="col-xs-8  col-sm-9">
                  <input  type="text" id="editlastname" name="editlastname" class=" form-control" style="padding-left: 0px;">
                </div>

              </div>
              <div class="form-group">
                <label class="col-xs-4  col-sm-3 control-label">Password:</label>
                <div class="col-xs-8  col-sm-9">
                  <input  type="password" id="editpassword" name="editpassword" class=" form-control" style="padding-left: 0px;">

                </div>
              </div>
              <div class="form-group">
                <label class="col-xs-4  col-sm-3 control-label">Role:</label>
                <div class="col-xs-4  col-sm-5">
                  <select name="editrole" id="editrole" class="form-control" required="required">
                    <option value="Admin">Admin</option>
                    <option value="User">User</option>
                  </select>
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




