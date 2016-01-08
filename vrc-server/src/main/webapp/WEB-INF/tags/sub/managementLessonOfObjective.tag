<%@ tag import="com.cmg.lesson.data.jdo.word.WordCollection" %>
<%@ tag import="com.cmg.lesson.services.word.WordCollectionService" %>
<%@ tag import="com.cmg.lesson.services.question.QuestionService" %>
<%@ tag import="com.cmg.lesson.data.jdo.question.Question" %>
<%@ tag import="com.cmg.lesson.services.objectives.ObjectiveService" %>
<%@ tag import="com.cmg.lesson.data.jdo.objectives.objective" %>
<%@ tag import="com.cmg.lesson.data.jdo.objectives.Objective" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<style>
  #addform,
  #editform{
    margin-top: 25px;
  }

  #addQuestion {
    width:95%;
  }

  .add-word-group{
    text-align: right;
    border-top: 1px solid #e5e5e5;
    padding-top: 10px;
  }

  .back-wrapper{
    display: inline-block;
    float: right;
  }

  .back-wrapper a{
    font-size: 17px;
  }

  #id-question{
    display: none;
  }
</style>

<div id="page-wrapper">
  <div class="row">
    <div class="col-lg-12">
      <%
        String name="";
        String id = request.getParameter("id");
        ObjectiveService lcr = new ObjectiveService();
        Objective lc = lcr.getById(id);
        if(lc!=null){
          name = lc.getName();
        }
      %>
      <h1 class="page-header"><%=name%></h1>
    </div>
    <!-- /.col-lg-12 -->
    <div class="col-lg-12">
    </div>
  </div>
  <!-- /.row -->

  <div class="row">
    <div class="col-lg-12">
      <div class="panel panel-default">
        <div class="panel-heading">
          <label for="select-question">Select Lessons:</label>
          <select class="form-control" id="select-lesson">
          </select>
          <button type="button" id="add-word-of-question" name="addCode">Add Lessons</button>
          <span class="back-wrapper"><a href="#" onclick="goBack()">Back to previous page</a></span>
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
                  <th>Index</th>
                  <th>Lessons</th>
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
          <div class="col-xs-10 col-xs-offset-1">

            <h1 align="center">Change Index of Lesson</h1>
            <form name="add" class="form-horizontal" id="addform">
              <div class="form-group">
                <div>
                  <label class="col-xs-4  col-sm-4 control-label ">Index:</label>
                  <div class="col-xs-8  col-sm-8">
                    <input type="text" id="index" name="index-lessons" class=" form-control" style="padding-left: 0px; margin-bottom: 5px;">
                  </div>
                </div>
              </div>
              <div class="form-group">
                <div class="row add-word-group">
                  <button type="button" name="yesadd" id="yesadd" class="btn btn-default" value="yes" >Submit</button>
                  <button type="button" name="closeadd" id="closeadd" class="btn btn-default" data-dismiss="modal" value="Close" >Close</button>
                </div>
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
        <h1 class="modal-title" align="center">Remove Lessons</h1>
      </div>
      <form name="form-delete" >
        <div class="modal-body">
          <input type="hidden" id="iddelete" name="iddelete">
          <h3>Do you want to remove ?</h3>
        </div>
        <div class="modal-footer">
          <button type="button" name="YesDelete" id="deleteItems" class="btn btn-default" >Submit</button>
          <button type="button" name="closedelete" id="closedelete" class="btn btn-default" data-dismiss="modal" value="Close" >Close</button>
        </div>
      </form>
    </div>
  </div>
</div>
<script src="<%=request.getContextPath() %>/js/Lession/managementLessonOfObjective.js"></script>