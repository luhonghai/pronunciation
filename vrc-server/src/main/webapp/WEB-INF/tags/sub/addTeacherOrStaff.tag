<%@ tag import="com.cmg.vrc.data.jdo.ClientCode" %>
<%@ tag import="com.cmg.vrc.data.dao.impl.ClientCodeDAO" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<div id="page-wrapper">
  <div class="row">
    <div class="col-lg-12">
      <h1 class="page-header">Teacher or Staff Manage</h1>
    </div>
    <!-- /.col-lg-12 -->
  </div>
  <!-- /.row -->
  <% String idCompany = request.getParameter("idCompany");
    ClientCode clientCode=new ClientCode();
    ClientCodeDAO clientCodeDAO=new ClientCodeDAO();
    clientCode=clientCodeDAO.getById(idCompany);
    String company=clientCode.getCompanyName();
  %>
  <input type="hidden" id="idCompany" value="<%=idCompany%>">
  <input type="hidden" id="company" value="<%=company%>" name="roledelete">
  <div class="row">
    <div class="col-lg-12">
      <div class="panel panel-default">
        <div class="panel-heading">
          <button type="button" id="addUser" name="addCode">Add User</button>
          <span class="back-wrapper" style="display: inline-block;   float: right; font-size: 17px;"><a href="#" onclick="goBack()">Back to previous page</a></span>
          <script>
            function goBack() {
              window.history.back();
            }
          </script>
        </div>
        <!-- /.panel-heading -->
        <div class="panel-body">
          <div class="dataTable_wrapper">
            <div class="table-responsive">
            <table class="table table-striped table-bordered table-hover table-responsive dt-responsive display nowrap" id="dataTables-example" cellspacing="0">
              <thead>
              <tr>
                <th>UserName</th>
                <th>Role</th>
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
            <input type="hidden" id="company" value="<%=company%>" name="roledelete">
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
                    <option value="Staff">Staff</option>
                    <option value="Teacher">Teacher</option>
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
            <input type="hidden" id="roleold" name="roleold">
            <form name="Edit" class="form-horizontal"
                  style="margin-top: 25px" id="editform">
              <input type="hidden" id="idedit" name="idedit">

              <div class="form-group">
                <label class="col-xs-4  col-sm-3 control-label">UserName:</label>
                <div class="col-xs-8  col-sm-9">
                  <input  type="text" id="editUserName" name="editUserName" class="form-control"  readonly style="padding-left: 0px;">
                </div>

              </div>
              <div class="form-group">
                <label class="col-xs-4  col-sm-3 control-label">Password:</label>
                <div class="col-xs-8  col-sm-9">
                  <input  type="password" id="editpassword" name="editpassword" class=" form-control" style="padding-left: 0px;">
                  <p id="passedit" name="passedit" style="color:red; display: none">Required field to enter data</p>
                </div>
              </div>
              <div class="form-group">
                <label class="col-xs-4  col-sm-3 control-label">Role:</label>
                <div class="col-xs-4  col-sm-5">
                  <select name="editrole" id="editrole" class="form-control" required="required">
                    <option value="Staff">Staff</option>
                    <option value="Teacher">Teacher</option>
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
          <input type="hidden" id="roledelete" name="roledelete">
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

<div id="teacher" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">&times;</button>
        <label class="modal-title" align="center" style="font-size: 250%">Mapping Teacher or Staff to Company</label>
      </div>
      <form name="form-delete" >
        <div class="modal-body">
          <div id="container-add-company" style="height:150px;">
            <input type="hidden" id="fullNames" name="fullNames">
            <input type="hidden" id="firstNames" name="firstNames">
            <input type="hidden" id="lastNames" name="lastNames">
            <input type="hidden" id="passwords" name="passwords">
            <input type="hidden" id="roles" name="roles">
            <label class="col-xs-4  col-sm-4 control-label ">Company:</label>
            <div class="col-xs-8  col-sm-8" style="padding-left: 0px;">
              <img class="loading-lesson loading" src="http://i.imgur.com/m1fR7ef.gif"/>
              <select style="display:none;" multiple class="form-control" id="select-company">
                <option>1</option>
                <option>2</option>
                <option>3</option>
                <option>4</option>
                <option>5</option>

              </select>
            </div>
          </div>

        </div>
        <div class="modal-footer">
          <button type="button" name="addteacher" id="addteacher" class="btn btn-default" >Yes</button>
          <button type="button" name="closeteacer" id="closeteacer" class="btn btn-default" data-dismiss="modal" value="Close" >Close</button>
        </div>
      </form>
    </div>
  </div>
</div>


<script src="<%=request.getContextPath() %>/js/addTeacherOrStaff.js"></script>




