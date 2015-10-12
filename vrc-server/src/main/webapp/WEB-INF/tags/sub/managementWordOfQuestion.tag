<%@ tag import="com.cmg.lesson.data.jdo.word.WordCollection" %>
<%@ tag import="com.cmg.lesson.services.word.WordCollectionService" %>
<%@ tag import="com.cmg.lesson.services.question.QuestionService" %>
<%@ tag import="com.cmg.lesson.data.jdo.question.Question" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<div id="page-wrapper">
  <div class="row">
    <div class="col-lg-12">
      <h1 class="page-header">Management Word Of Question</h1>
    </div>
    <%String name="";%>
    <!-- /.col-lg-12 -->
    <div class="col-lg-12">
      <%
        String id = request.getParameter("id");
        QuestionService ser = new QuestionService();
        Question wc = ser.getById(id);
        if(wc!=null){
          name = wc.getName();
        }
      %>
      <h3 id="questionId"><%=name%></h3>
    </div>
  </div>
  <!-- /.row -->

  <div class="row">
    <div class="col-lg-12">
      <div class="panel panel-default">
        <div class="panel-heading">
          <button type="button" id="addUser" name="addCode">Add New Word</button>


        </div>
        <!-- /.panel-heading -->
        <div class="panel-body">
          <div class="dataTable_wrapper">
            <div class="table-responsive">
              <table class="table table-striped table-bordered table-hover table-responsive dt-responsive display nowrap" id="dataTables-example" cellspacing="0">
                <thead>
                <tr>
                  <th>Word</th>
                  <th>Pronunciation</th>
                  <th>Audio</th>
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

            <h1 align="center">Add Word</h1>
            <form name="add" class="form-horizontal"
                  style="margin-top: 25px" id="addform">

              <div class="form-group">
                <div class="row">
                  <label class="col-xs-4  col-sm-3 control-label ">Word:</label>
                  <div class="col-xs-5  col-sm-6">
                    <input  type="text" id="addWord" name="addWord" class=" form-control" style="padding-left: 0px; margin-bottom: 5px;">
                  </div>
                  <div class="col-xs-3  col-sm-2">
                    <button type="button" name="loadPhonemes" id="loadPhonemes" class="btn btn-default" value="yes" >Load Phonemes</button>
                  </div>
                </div>

                <div class="row">
                  <div class="col-xs-3">
                    <div class="row"><label class="control-label ">Phonemes:</label></div>
                    <div class="row"><label class="control-label ">WeightPhonemes:</label></div>
                  </div>
                  <div class="col-xs-6">
                    <div class="row" id="listPhonmes">
                      <input id="" type="text" style="padding-left: 0px;margin-bottom: 5px;width: 30px;" name="addsentence" >
                      <input id="" type="text" style="padding-left: 0px;margin-bottom: 5px;width: 30px;" name="addsentence" >
                      <input id="" type="text" style="padding-left: 0px;margin-bottom: 5px;width: 30px;" name="addsentence" >
                      <input id="" type="text" style="padding-left: 0px;margin-bottom: 5px;width: 30px;" name="addsentence" >
                      <input id="" type="text" style="padding-left: 0px;margin-bottom: 5px;width: 30px;" name="addsentence" >
                      <input id="" type="text" style="padding-left: 0px;margin-bottom: 5px;width: 30px;" name="addsentence" >
                    </div>

                    <div class="row" id="listWeight">
                      <input id="" type="text" style="padding-left: 0px;margin-bottom: 5px;width: 30px;" name="addsentence" >
                      <input id="" type="text" style="padding-left: 0px;margin-bottom: 5px;width: 30px;" name="addsentence" >
                      <input id="" type="text" style="padding-left: 0px;margin-bottom: 5px;width: 30px;" name="addsentence" >
                      <input id="" type="text" style="padding-left: 0px;margin-bottom: 5px;width: 30px;" name="addsentence" >
                      <input id="" type="text" style="padding-left: 0px;margin-bottom: 5px;width: 30px;" name="addsentence" >
                      <input id="" type="text" style="padding-left: 0px;margin-bottom: 5px;width: 30px;" name="addsentence" >
                    </div>
                  </div>
                </div>
              </div>
              <div class="modal-footer">
                <button type="button" name="yesadd" id="yesadd" class="btn btn-default" value="yes" >Submit</button>
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

            <h1 align="center">Edit Word</h1>
            <form name="Edit" class="form-horizontal"
                  style="margin-top: 25px" id="editform">
              <input type="hidden" id="idedit" name="idedit">

              <div class="form-group">
                <div>
                  <label class="col-xs-4  col-sm-3 control-label ">Word:</label>
                  <div class="col-xs-8  col-sm-9">
                    <input  type="text" id="editWord" name="addsentence" class=" form-control" disabled style="padding-left: 0px; margin-bottom: 5px;">
                  </div>
                </div>
                <div>
                  <label class="col-xs-4  col-sm-3 control-label ">Pronunciation:</label>
                  <div class="col-xs-8  col-sm-9">
                    <input  type="text" id="editPronunciation" name="addsentence" class=" form-control" disabled style="padding-left: 0px; margin-bottom: 5px;">
                  </div>
                </div>
                <div>
                  <label class="col-xs-4  col-sm-3 control-label ">Definition:</label>
                  <div class="col-xs-8  col-sm-9">
                    <textarea type="text" id="editDifinition" name="addsentence" rows="3" cols="50" class=" form-control" style="padding-left: 0px; margin-bottom: 5px;"></textarea>
                  </div>
                </div>
                <div>
                  <label class="col-xs-4  col-sm-3 control-label ">Mp3 Url:</label>
                  <div class="col-xs-8  col-sm-9">
                    <input  type="text" id="editPath" name="addsentence" class=" form-control" style="padding-left: 0px; margin-bottom: 5px;">
                  </div>
                </div>
                <div>
                  <label class="col-xs-4  col-sm-3 control-label ">Phonemes:</label>
                  <div class="col-xs-8  col-sm-9">
                    <input  type="text" id="editPhoneme" name="addsentence" class=" form-control" style="padding-left: 0px; margin-bottom: 5px;">
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
<script src="<%=request.getContextPath() %>/js/Lession/managementWordOfQuestion.js"></script>