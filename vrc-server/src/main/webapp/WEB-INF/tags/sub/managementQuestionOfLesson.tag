<%@ tag import="com.cmg.lesson.data.jdo.word.WordCollection" %>
<%@ tag import="com.cmg.lesson.services.word.WordCollectionService" %>
<%@ tag import="com.cmg.lesson.services.question.QuestionService" %>
<%@ tag import="com.cmg.lesson.data.jdo.question.Question" %>
<%@ tag import="com.cmg.lesson.services.lessons.LessonCollectionService" %>
<%@ tag import="com.cmg.lesson.data.jdo.lessons.LessonCollection" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<style>
  #addform,
  #editform{
    margin-top: 25px;
  }

  #addWord{
    width:95%;
  }

  .add-word-group{
    text-align: right;
    border-top: 1px solid #e5e5e5;
    padding-top: 10px;
  }

  .group-phoneme-weight{
    overflow-x: auto;
    overflow-y: hidden;
  }

  .group-phoneme-weight input{
    padding-left: 0px;
    width: 30px;
    text-align: center;
  }

  .group-phoneme-weight input[readonly],
  .group-phoneme-weight input[disabled]{
    background-color: #eee;
  }

  #listPhonmes input,
  #listPhonmesEdit input{
    margin: 5px 5px 5px 0;
  }

  #listWeight input,
  #listWeightEdit input{
    margin: 0px 5px 5px 0;
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
        LessonCollectionService lcr = new LessonCollectionService();
        LessonCollection lc = lcr.getById(id);
        if(lc!=null){
          name = lc.getName();
        }
      %>
      <h1 class="page-header">Question Of Lesson: <%=name%></h1>
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
          <button type="button" id="add-word-of-question" name="addCode">Add Question</button>
          <span class="back-wrapper"><a href="#" onclick="goBack()">Back to Management Lesson</a></span>
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
                  <th>Question</th>
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

            <h1 align="center">Add Word</h1>
            <form name="add" class="form-horizontal" id="addform">

              <div class="form-group">

                <div class="row">
                  <div class="col-xs-4  col-sm-3">
                    <div class="row"><label class="control-label ">Word:</label></div>
                  </div>
                  <div class="col-xs-5  col-sm-6">
                    <div class="row"><input  type="text" id="addWord" name="addWord" class=" form-control"></div>
                  </div>
                  <div class="col-xs-3  col-sm-2">
                    <div class="row"><button type="button" name="loadPhonemes" id="loadPhonemes" class="btn btn-default" value="yes" >Load Phonemes</button></div>
                  </div>
                </div>

                <div class="row">
                  <div class="col-xs-4 col-sm-3">
                    <div class="row"><label class="control-label phoneme-lable"></label></div>
                    <div class="row"><label class="control-label weight-lable"></label></div>
                  </div>
                  <div class="col-xs-8 col-sm-9 group-phoneme-weight">
                    <div class="row" id="listPhonmes"></div>
                    <div class="row" id="listWeight"></div>
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



<div id="edits" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <div class="row">
          <div class="col-xs-10 col-xs-offset-1">

            <h1 align="center">Edit Word</h1>
            <form name="Edit" class="form-horizontal"
                  style="margin-top: 25px" id="editform">
              <input type="hidden" id="idedit" name="idedit">

              <div class="form-group">
                <div class="row">
                  <div class="col-xs-4  col-sm-3">
                    <div class="row"><label class="control-label ">Word:</label></div>
                  </div>
                  <div class="col-xs-5  col-sm-6">
                    <div class="row"><input  type="text" id="editWord" name="editWord" class=" form-control"></div>
                  </div>
                  <div class="col-xs-3  col-sm-2">
                    <div class="row"></div>
                  </div>
                </div>

                <div class="row">
                  <div class="col-xs-4 col-sm-3">
                    <div class="row"><label class="control-label ">Phonemes:</label></div>
                    <div class="row"><label class="control-label ">WeightPhonemes:</label></div>
                  </div>
                  <div class="col-xs-8 col-sm-9 group-phoneme-weight">
                    <div class="row" id="listPhonmesEdit"></div>
                    <div class="row" id="listWeightEdit"></div>
                  </div>
                </div>
              </div>
              <div class="modal-footer">
                <button type="button" name="yesedit" id="yesedit" class="btn btn-default" value="yes" >Submit</button>
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
        <h1 class="modal-title" align="center">Delete Word</h1>
      </div>
      <form name="form-delete" >
        <div class="modal-body">
          <input type="hidden" id="iddelete" name="iddelete">
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
<script src="<%=request.getContextPath() %>/js/Lession/managementQuestionOfLesson.js"></script>