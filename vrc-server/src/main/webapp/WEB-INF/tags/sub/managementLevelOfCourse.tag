<%@ tag import="com.cmg.lesson.data.jdo.course.Course" %>
<%@ tag import="com.cmg.lesson.dao.course.CourseDAO" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>

<style>
.panel .row{
  margin-left: 0px;
  margin-right: 0px;
}

.row-button{
  margin-bottom: 15px;
}
</style>>

<div id="page-wrapper">
  <div class="row">
    <div class="col-lg-12">
      <%
        String name="";
        String id = request.getParameter("id");
        CourseDAO courseDAO = new CourseDAO();
        Course wc = courseDAO.getById(id);
        if(wc!=null){
          name = wc.getName();
        }
      %>
      <h1 class="page-header">Course: <%=name%></h1>
      <input type="hidden" id="idCourse" name="iddelete" value="<%=id%>">
    </div>
    <!-- /.col-lg-12 -->
    <%--<div class="col-lg-12">--%>
      <%--<span id="id-question"></span>--%>
    <%--</div>--%>
  </div>
  <!-- /.row -->

  <div class="row">
    <div class="col-lg-12">
      <div class="panel panel-default">
        <div class="panel-heading">
          <span class="back-wrapper"><a href="#" onclick="goBack()">Back to Management Question</a></span>
          <script>
            function goBack() {
              window.history.back();
            }
          </script>
        </div>
        <!-- /.panel-heading -->
          <div class="panel-body">
            <div id="contain_level_add" class="row">
            <div class="col-sm-2" style="padding-top: 5px;text-align: left;width : 50px">
              <label style="vertical-align: middle" class="control-label">Level</label>
            </div>
            <div class="col-sm-4">
              <select name="level" id="level" class="form-control" required="required">

              </select>
            </div>
              <div class="col-sm-3">
                <div class="row"><button type="button" name="addlevel" id="addlevel" class="btn btn-default" value="yes" >Add Level</button></div>
              </div>
            </div>
            <div class="panel-group" id="accordion" style="margin-top: 30px;">


            </div>

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


<div id="add-objective" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <div class="row">
          <div class="col-xs-12 col-md-10 col-md-offset-1">

            <h1 align="center">Add Objective</h1>
            <form name="add-objective" class="form-horizontal"
                  style="margin-top: 25px" id="add-objective-form">

              <div class="form-group">

                <div>
                  <label class="col-xs-4  col-sm-3 control-label ">Objective:</label>
                  <div class="col-xs-8  col-sm-9">
                    <input  type="text" id="add-objective-name" name="add-objective-name" class=" form-control" style="padding-left: 0px; margin-bottom: 5px;">
                  </div>
                </div>
                <div>
                  <label class="col-xs-4  col-sm-3 control-label ">Description:</label>
                  <div class="col-xs-8  col-sm-9">
                    <textarea   type="text" id="add-description" name="add-description" rows="3" cols="50" class=" form-control" style="padding-left: 0px;margin-bottom: 5px;"></textarea>
                  </div>
                </div>
                <div id="container-add-lesson">
                  <label class="col-xs-4  col-sm-3 control-label ">Lessons:</label>
                  <img class="col-xs-8  col-sm-9">
                    <img class="loading-lesson loading" src="http://i.imgur.com/m1fR7ef.gif"/>
                    <select style="display:none" multiple class="form-control" id="select-lesson">
                      <option>1</option>
                      <option>2</option>
                      <option>3</option>
                      <option>4</option>
                      <option>5</option>
                    </select>
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


<div id="edit-objective" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <div class="row">
          <div class="col-xs-12 col-md-10 col-md-offset-1">

            <h1 align="center">Edit Objective</h1>
            <form name="edit-objective" class="form-horizontal"
                  style="margin-top: 25px" id="edit-objective-form">

              <div class="form-group">

                <div>
                  <label class="col-xs-4  col-sm-3 control-label ">Objective:</label>
                  <div class="col-xs-8  col-sm-9">
                    <input  type="text" id="edit-objective-name" name="edit-objective-name" class=" form-control" style="padding-left: 0px; margin-bottom: 5px;">
                  </div>
                </div>
                <div>
                  <label class="col-xs-4  col-sm-3 control-label ">Description:</label>
                  <div class="col-xs-8  col-sm-9">
                    <textarea   type="text" id="edit-description" name="add-description" rows="3" cols="50" class=" form-control" style="padding-left: 0px;margin-bottom: 5px;"></textarea>
                  </div>
                </div>
                <div id="container-edit-lesson">
                  <label class="col-xs-4  col-sm-3 control-label ">Lessons:</label>
                  <img class="col-xs-8  col-sm-9">
                  <img class="loading-lesson loading" src="http://i.imgur.com/m1fR7ef.gif"/>
                  <select style="display:none" multiple class="form-control" id="select-lesson-edit">
                    <option>1</option>
                    <option>2</option>
                    <option>3</option>
                    <option>4</option>
                    <option>5</option>
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

<!--popup delete lesson-->
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
          <input type="hidden" id="id-lesson-delete" name="iddelete"/>
          <input type="hidden" id="id-objective-delete" name="iddelete"/>
          <h3>Do you want to delete ?</h3>
        </div>
        <div class="modal-footer">
          <button type="button" name="YesDelete" id="deleteItems" class="btn btn-default" >Submit</button>
          <button type="button" name="closedelete" id="closedelete" class="btn btn-default" data-dismiss="modal" value="Close" >Close</button>
        </div>
      </form>
    </div>
  </div>
</div>

<div id="delete-objective" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">&times;</button>
        <h1 class="modal-title" align="center">Delete Objective</h1>
      </div>
      <form name="form-delete" >
        <div class="modal-body">
          <input type="hidden" id="delete-objective-id" name="iddelete"/>
          <h3>Do you want to delete ?</h3>
        </div>
        <div class="modal-footer">
          <button type="button" name="YesDelete" id="deleteItems-obj" class="btn btn-default" >Submit</button>
          <button type="button" name="closedelete" id="closedelete-obj" class="btn btn-default" data-dismiss="modal" value="Close" >Close</button>
        </div>
      </form>
    </div>
  </div>
</div>

<script src="<%=request.getContextPath() %>/js/Lession/ui/managementLevelOfCourseUI.js"></script>
<script src="<%=request.getContextPath() %>/js/Lession/data/managementLevelOfCourseData.js"></script>
<script src="<%=request.getContextPath() %>/js/Lession/managementLevelOfCourse.js"></script>