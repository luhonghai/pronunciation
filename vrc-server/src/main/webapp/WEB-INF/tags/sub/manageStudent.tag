<%@ tag import="com.cmg.vrc.data.dao.impl.ClassDAO" %>
<%@ tag import="com.cmg.vrc.data.jdo.ClassJDO" %>
<%@tag description="managementStudent" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%
  String teacherName=session.getAttribute("username").toString();
  String name="";
  String idClass = request.getParameter("idClass");
  ClassDAO classDAO=new ClassDAO();
  ClassJDO classJDO = classDAO.getById(idClass);
  if(classJDO!=null){
    name = classJDO.getClassName();
  }
%>
<input type="hidden" value="<%=idClass%>" id="idClasst">
<div id="page-wrapper">
  <div class="row">
    <div class="col-lg-12">
      <h1 class="page-header"><%=name%></h1>
    </div>
    <!-- /.col-lg-12 -->
  </div>

  <div class="well">
    <div class="row">
      <div class="col-sm-3">
        <div class="form-group">
          <label class="control-label">Student Name</label>
          <input type="text" name="filter-student" id="student" class="form-control" placeholder="Student Name">
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
  <!-- /.row -->
  <div class="row">
    <div class="col-lg-12">
      <div class="panel panel-default">
        <div class="panel-heading">
          <button type="button" id="addUser" name="addCode" value="<%=teacherName%>">Add Student</button>
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
                <th>StudentName</th>
                <th>Created Date</th>
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
            <h1 align="center">Add Student</h1>
            <form name="add" class="form-horizontal"
                  style="margin-top: 25px" id="addform">
              <div id="container-add-student"  style="height:150px;">
                <label class="col-xs-4  col-sm-4 control-label ">Student:</label>
                <div class="col-xs-8  col-sm-8" style="padding-left: 0px;">
                  <img class="loading-lesson loading" src="http://i.imgur.com/m1fR7ef.gif"/>
                  <select style="display:none;" multiple class="form-control" id="select-student">
                    <option>1</option>
                    <option>2</option>
                    <option>3</option>
                    <option>4</option>
                    <option>5</option>
                  </select>
                </div>
              </div>


              <div class="modal-footer">
                <button type="button" name="yesadd" id="yesadd" class="btn btn-default">Yes</button>
                <button type="button" name="closeadd" id="closeadd" class="btn btn-default" data-dismiss="modal" value="Close" >Close</button>
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
        <h2 style="font-weight: 700;font-size:18px" class="modal-title" align="center">Delete Student</h2>
      </div>
      <form name="form-delete" >
        <div class="modal-body">
          <input type="hidden" id="iddelete"  name="iddelete">
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

<script src="<%=request.getContextPath() %>/js/student.js"></script>




