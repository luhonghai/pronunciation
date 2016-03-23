<div id="popupCourse" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="display: none;">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
        <div class="contain-arrow"><label id="arrowCourse" class="modal-title"
                                          style="text-align: left;">
        </label></div>

        <h2 id='titlePopupCourse' class="modal-title">course management</h2>
        <h4 id="validateMsgCourse" class="modal-title validateMsg"
            style="text-align: center;font-weight: 200;color:red;display:none;">Enter your level
          name</h4>
      </div>
      <div class="modal-body">
        <form class="form-horizontal" id="addCourse" name="addform">
          <div class="form-group">
            <label class="control-label col-md-4 lbl_addForm" for="courseName">name:</label>

            <div class="col-md-8">
              <input type="text" class="form-control" id="courseName" name="name"
                     placeholder="Course Name">
            </div>
          </div>
          <div class="form-group">
            <label class="control-label col-md-4 lbl_addForm" for="courseDescription">description:</label>

            <div class="col-md-8">
                            <textarea rows="2" class="form-control" id="courseDescription" name="description"
                                      placeholder="Course description"></textarea>
            </div>
          </div>
          <div class="form-group contain-button">
            <div class="col-md-5">
              <input type="hidden" class="action">
              <input type="hidden" class="idHidden">
              <img id="helpAddCourse" class="helpInfor" src="/images/popup/help_50_50.png" width="36px"
                   height="36px"/>
            </div>
            <div class="col-md-2">
              <img id="btnDeleteCourse" src="/images/popup/trash_50x50.gif" width="36px" height="36px"/>
            </div>
            <div class="col-md-5">
              <img id="btnSaveCourse" style="float: right" src="/images/popup/Save_50x50.gif" width="36px"
                   height="36px"/>
            </div>
          </div>
        </form>
      </div>
      <!-- End of Modal body -->
    </div>
    <!-- End of Modal content -->
  </div>
  <!-- End of Modal dialog -->
</div>


<div id="popupLevel" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="display: none;">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
        <div class="contain-arrow"><label id="arrow" class="modal-title"
                                          style="text-align: left;">
        </label></div>

        <h2 id='titlePopup' class="modal-title">Add Level</h2>
        <h4 id="validateLvMsg" class="modal-title validateMsg"
            style="text-align: center;font-weight: 200;color:red;display:none;">Enter your level
          name</h4>
      </div>
      <div class="modal-body">
        <form class="form-horizontal" id="addLevel" name="addform">
          <div class="form-group">
            <label class="control-label col-md-4 lbl_addForm" for="lvName">name:</label>

            <div class="col-md-8">
              <input type="text" class="form-control" id="lvName" name="name"
                     placeholder="Level name">
            </div>
          </div>
          <div class="form-group">
            <label class="control-label col-md-4 lbl_addForm" for="lvDesc">description:</label>

            <div class="col-md-8">
                            <textarea rows="2" class="form-control" id="lvDesc" name="description"
                                      placeholder="Level description"></textarea>
            </div>
          </div>
          <div class="form-group contain-button">
            <div class="col-md-5">
              <input type="hidden" class="action">
              <input type="hidden" class="idHidden">
              <img id="helpAddLevel" class="helpInfor" src="/images/popup/help_50_50.png" width="36px"
                   height="36px"/>
            </div>
            <div class="col-md-2">
              <img id="btnDeleteLevel" src="/images/popup/trash_50x50.gif" width="36px" height="36px"/>
            </div>
            <div class="col-md-5">
              <img id="btnSaveLevel" style="float: right" src="/images/popup/Save_50x50.gif" width="36px"
                   height="36px"/>
            </div>
          </div>
        </form>
      </div>
      <!-- End of Modal body -->
    </div>
    <!-- End of Modal content -->
  </div>
  <!-- End of Modal dialog -->
</div>


<div id="popupObjective" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="display: none;">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
        <div class="contain-arrow"><label id="arrowObj" class="modal-title"
                                          style="text-align: left;">
        </label></div>

        <h2 id='titlePopupObj' class="modal-title">Add Objective</h2>
        <h4 id="validateObjMsg" class="modal-title validateMsg"
            style="text-align: center;font-weight: 200;color:red;display:none;">Enter your level
          name</h4>
      </div>
      <div class="modal-body">
        <form class="form-horizontal" id="addObj" name="addform">
          <div class="form-group">
            <label class="control-label col-md-4 lbl_addForm" for="objName">name:</label>

            <div class="col-md-8">
              <input type="text" class="form-control" id="objName" name="name"
                     placeholder="Objective name">
            </div>
          </div>
          <div class="form-group">
            <label class="control-label col-md-4 lbl_addForm" for="objDesc">description:</label>

            <div class="col-md-8">
                            <textarea rows="2" class="form-control" id="objDesc" name="description"
                                      placeholder="Objective description"></textarea>
            </div>
          </div>
          <div class="form-group contain-button">
            <div class="col-md-5">
              <input type="hidden" class="action">
              <input type="hidden" class="idHidden">
              <img id="helpAddObj" class="helpInfor" src="/images/popup/help_50_50.png" width="36px"
                   height="36px"/>
            </div>
            <div class="col-md-2">
              <img id="btnDeleteObj" src="/images/popup/trash_50x50.gif" width="36px" height="36px"/>
            </div>
            <div class="col-md-5">
              <img id="btnSaveObj" style="float: right" src="/images/popup/Save_50x50.gif" width="36px"
                   height="36px"/>
            </div>
          </div>
        </form>
      </div>
      <!-- End of Modal body -->
    </div>
    <!-- End of Modal content -->
  </div>
  <!-- End of Modal dialog -->
</div>


<div id="popupTest" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="display: none;">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
        <div class="contain-arrow"><label id="arrowTest" class="modal-title"
                                          style="text-align: left;">
        </label></div>

        <h2 id='titlePopupTest' class="modal-title">Add Test</h2>
        <h4 id="validateTestMsg" class="modal-title validateMsg"
            style="text-align: center;font-weight: 200;color:red;display:none;">Enter your level
          name</h4>
      </div>
      <div class="modal-body">
        <form class="form-horizontal" id="addTest" name="addform">
          <div class="form-group">
            <label class="control-label col-md-3 lbl_addForm" for="percent">pass %:</label>

            <div class="col-md-8">
              <input type="text" onkeypress="return isNumberKey(event,this)" class="form-control"
                     id="percent" name="percent"
                     placeholder="Percent pass">
            </div>
          </div>
          <div class="form-group contain-button">
            <div class="col-md-5">
              <input type="hidden" class="action">
              <input type="hidden" class="idHidden">
              <img id="helpAddTest" class="helpInfor" src="/images/popup/help_50_50.png" width="36px"
                   height="36px"/>
            </div>
            <div class="col-md-2">
              <img id="btnDeleteTest" src="/images/popup/trash_50x50.gif" width="36px" height="36px"/>
            </div>
            <div class="col-md-5">
              <img style="float: right" id="btnSaveTest" src="/images/popup/Save_50x50.gif" width="36px"
                   height="36px"/>
            </div>
          </div>
        </form>
      </div>
      <!-- End of Modal body -->
    </div>
    <!-- End of Modal content -->
  </div>
  <!-- End of Modal dialog -->
</div>


<div id="popupLesson" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="display: none;">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                aria-hidden="true">x</span></button>
        <div class="contain-arrow"><label id="arrowLesson" class="modal-title"
                                          style="text-align: left; padding-left: 15px;">
        </label></div>

        <h2 id='titlePopupLesson' class="modal-title">add lesson</h2>
        <h4 id="validateLessonMsg" class="modal-title validateMsg"
            style="text-align: center;font-weight: 200;color:red;display:none;">Enter your lesson
          name</h4>
      </div>
      <div class="modal-body">
        <form class="form-horizontal" id="addLesson" name="addform">
          <div class="form-group">
            <label class="control-label col-md-4 lbl_addForm">name:</label>

            <div class="col-md-8">
              <input type="text" class="form-control" id="lessonName" name="lessonName"
                     placeholder="Lesson name">
            </div>
          </div>
          <div class="form-group">
            <label class="control-label col-md-4 lbl_addForm">description:</label>

            <div class="col-md-8">
                            <textarea rows="2" class="form-control" id="lessonDesc" name="description"
                                      placeholder="Lesson description"></textarea>
            </div>
          </div>

          <div class="form-group" >
            <label class="control-label col-md-4 lbl_addForm">type:</label>

            <div class="col-md-8">
              <select id="lessonType" class="form-control" style="padding: 2px; width: 150px;;">
                <option value="accuracy">accuracy</option>
                <option value="pace" disabled="disabled">pace</option>
                <option value="power" disabled="disabled">power</option>
                <option value="pitch" disabled="disabled">pitch</option>
              </select>
            </div>
          </div>

          <div class="form-group">
            <label class="control-label col-md-2 lbl_addForm">lesson details:</label>
            <img src="/images/popup/p_menu_lesson.png" class="control col-md-2"
                 style="width:50px;height: 50px;padding-left: 0px;padding-right: 0px;margin-left: 12px;">

            <div class="col-md-8">
                            <textarea rows="3" class="form-control" id="lessonDetail" name="details"
                                      placeholder="Lesson details"></textarea>
            </div>
          </div>
          <div class="form-group contain-button">
            <div class="col-md-5">
              <input type="hidden" class="action">
              <input type="hidden" class="idHidden">
              <img id="helpAddLesson" class="helpInfor" src="/images/popup/help_50_50.png" width="36px"
                   height="36px"/>
            </div>
            <div class="col-md-2">
              <img id="btnDeleteLesson" src="/images/popup/trash_50x50.gif" width="36px" height="36px"/>
            </div>
            <div class="col-md-5">
              <img style="float: right" id="btnSaveLesson" src="/images/popup/Save_50x50.gif" width="36px"
                   height="36px"/>
            </div>
          </div>
        </form>
      </div>
      <!-- End of Modal body -->
    </div>
    <!-- End of Modal content -->
  </div>
  <!-- End of Modal dialog -->
</div>


<div id="popupQuestion" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="display: none;">
  <div class="modal-dialog modal-sm">
    <%--<div class="modal-content modal-question-word">--%>
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                aria-hidden="true">x</span></button>
        <div class="contain-arrow"><label id="arrowQuestion" class="modal-title"
                                          style="text-align: left;">
        </label></div>

        <h2 align="center" id='titlePopupQuestion' class="modal-title">question management</h2>
        <h4 id="validateQuestionMsg" class="modal-title validateMsg"
            style="text-align: center;font-weight: 200;color:red;display:none;"></h4>
      </div>
      <div class="modal-body">
        <form class="form-horizontal" id="addQuestion" name="addform">
          <div class="form-group">
            <label class="control-label">Please add the words that you want to be used for this question. If
              you add more than one word they will be randomised to give variety to the student.Double
              click on a word to edit or delete</label>
          </div>
          <div class="form-group">
            <a id="btnAddWord"
               style="background-color: orange;color: white;border-radius: 3px; padding: 5px 5px; text-decoration: none;">
              <img src="/images/teacher/invite_students_48x48.gif" style="width: 24px;height: 24px;"> add
              word <i class="fa fa-plus"></i>
            </a>

            <div id="listWord">

            </div>

          </div>
          <div class="form-group contain-button">
            <div class="col-md-5" style="padding-left: 0px;">
              <input type="hidden" class="action">
              <input type="hidden" class="idHidden">
              <img id="helpAddQuestion" class="helpInfor" src="/images/popup/help_50_50.png" width="36px"
                   height="36px"/>
            </div>
            <div class="col-md-2">
              <img id="btnDeleteQuestion" src="/images/popup/trash_50x50.gif" width="36px" height="36px"/>
            </div>
            <div class="col-md-5" style="padding-right: 0px;">
              <img style="float: right" id="btnSaveQuestion" src="/images/popup/Save_50x50.gif"
                   width="36px" height="36px"/>

            </div>
          </div>
        </form>
      </div>
      <!-- End of Modal body -->
    </div>
    <!-- End of Modal content -->
  </div>
  <!-- End of Modal dialog -->
</div>


<div id="popupQuestionTest" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="display: none;">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                aria-hidden="true">x</span></button>
        <h2 align="center" id='titlePopupTestWord' class="modal-title">add test question</h2>
        <h4 id="validateQuestionForTestMsg" class="modal-title validateMsg"
            style="text-align: center;font-weight: 200;color:red;display:none;"></h4>
      </div>
      <div class="modal-body">
        <form class="form-horizontal" id="addTestWord" name="addform">
          <div class="form-group">
            <label class="control-label col-md-3 lbl_addForm" for="explanation">explanation:</label>
            <div class="col-md-9">
              <input type="text" class="form-control" id="explanation" name="name" style="width:100%">
            </div>
          </div>
          <div class="form-group">
            <label class="control-label col-md-3 lbl_addForm" for="courseDescription">type:</label>
            <div class="col-md-8">
              <select id="testType" class="form-control" style="padding: 2px; width: 150px;;">
                <option value="accuracy">accuracy</option>
                <option value="pace" disabled="disabled">pace</option>
                <option value="power" disabled="disabled">power</option>
                <option value="pitch" disabled="disabled">pitch</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <div class="col-md-12">
              <label class="text-description control-label">Please add the words that you want to be used for this
                question. If you add more than one word they will be randomised to give variety to the
                student.</label>
            </div>
          </div>
          <div class="form-group">
            <div class="col-md-12">
              <a id="btnAddWordTest"
                 style="background-color: orange;color: white;border-radius: 3px; padding: 5px 5px; text-decoration: none;">
                <img src="/images/teacher/invite_students_48x48.gif" style="width: 24px;height: 24px;">
                add test word <i class="fa fa-plus"></i>
              </a>
            </div>
          </div>
          <div class="form-group">
            <div class="col-md-12">
              <div id="listWordTests">

              </div>
            </div>
          </div>
          <div class="form-group contain-button">
            <div class="col-md-5" style="padding-left: 0px;">
              <input type="hidden" class="action">
              <input type="hidden" class="idHidden">
              <img id="helpAddTestWord" class="helpInfor" src="/images/popup/help_50_50.png" width="36px"
                   height="36px"/>
            </div>
            <div class="col-md-2">
              <img id="btnDeleteTestWord" style="display:none" src="/images/popup/trash_50x50.gif" width="36px" height="36px"/>
            </div>
            <div class="col-md-5" style="padding-right: 0px;">
              <img style="float: right" id="btnSaveTestWord" src="/images/popup/Save_50x50.gif"
                   width="36px" height="36px"/>
            </div>
          </div>
        </form>
      </div>
      <!-- End of Modal body -->
    </div>
    <!-- End of Modal content -->
  </div>
  <!-- End of Modal dialog -->
</div>


<div id="addWordModal" class="modal fade" aria-hidden="true" style="display: none;">
  <div class="modal-dialog">
    <div class="modal-content modal-question-word">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                aria-hidden="true">x</span></button>
        <div id="contain-arrow"><label id="arrowWord" class="modal-title"></label></div>
        <input type="hidden" id="idLesson">
        <input type="hidden" id="AddOrEditWord">
        <h2 align="center">add word</h2>
        <h4 id="validateWordMsg" class="modal-title validateMsg"
            style="text-align: center;font-weight: 200;color:red;display:none;">Enter your word</h4>
      </div>
      <div class="modal-body">
        <form name="add" class="form-horizontal" id="addWords">
          <div class="form-group">
            <label>Please add the words that you want to be used for this
              question. If you add more than one word they will be randomised to give variety to the
              student.
            </label>
          </div>
          <div class="form-group">
            <div class="col-xs-3  col-sm-2" style="padding:0px">
              <label class="control-label">Word:</label>
            </div>
            <div class="col-xs-5  col-sm-6">
              <input type="text" id="addWord" name="addWord" class=" form-control">
            </div>
            <div class="col-xs-4  col-sm-4">
              <button type="button" name="loadPhonemes" id="loadPhonemes" class="btn btn-default"
                      style="background-color: lightgreen;" value="yes">Load Phonemes
              </button>
            </div>
          </div>
          <div class="form-group">
            <div class="col-xs-3 col-sm-2" style="padding:0px">
              <label class="control-label phoneme-lable"></label>
            </div>
            <div class="col-xs-5  col-sm-6 group-phoneme-weight">
              <div class="row" id="listPhonmes"></div>
            </div>
          </div>
          <div class="form-group">
            <div class="col-xs-3 col-sm-2" style="padding:0px">
              <label class="control-label ipa-lable"></label>
            </div>
            <div class="col-xs-5  col-sm-6 group-phoneme-weight">
              <div class="row" id="listIpa"></div>
            </div>
          </div>
          <div class="form-group">
            <div class="col-xs-3 col-sm-2" style="padding:0px">
              <label class="control-label weight-lable"></label>
            </div>
            <div class="col-xs-5  col-sm-6 group-phoneme-weight">
              <div class="row" id="listWeight"></div>
            </div>
          </div>
          <div class="form-group">
            <div class="col-xs-12 col-sm-12" style="padding:0px">
              <label id="wordModal1" style="display: none">If you would like to
                allocate an increased percentage of the score to a particular phoneme then please give
                that phoneme a higher "weight."
              </label>
            </div>
            <div class="col-xs-12 col-sm-12" style="padding:0px">
              <label id="wordModal2" style="display: none">E.g. v = 1,<br>
                ɛ = 1,<br>
                s = 1,<br>
                t = 3 , this will allocate 50% of the score to this last phoneme /t/.
              </label>
            </div>
          </div>
          <div class="form-group contain-button">
            <div class="col-md-6" style="padding-left: 0px;">
              <input type="hidden" class="action">
              <input type="hidden" class="idHidden">
              <img id="helpAddWord" style="padding-left: 0px;" src="/images/popup/help_50_50.png"
                   width="36px" height="36px"/>

            </div>
            <div class="col-md-6" style="padding-right: 0px;">
              <img style="float: right;padding-right: 0px;" id="btnSaveWord"
                   src="/images/popup/Save_50x50.gif" width="36px" height="36px"/>

            </div>
          </div>
        </form>
      </div>
      <!-- End of Modal body -->
    </div>
    <!-- End of Modal content -->
  </div>
  <!-- End of Modal dialog -->
</div>

<div id="help-popup" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
     style="display: none;color:#957F7F">
  <div class="modal-dialog" style="width:500px">
    <div class="modal-content">
      <div class="modal-header" style="border-bottom: transparent;padding-bottom: 0px;text-align: center">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
        <h2 class="modal-title" style="font-weight: 700;">preview and/or duplicate a course</h2>
      </div>
      <div class="modal-body">

      </div>
      <!-- End of Modal body -->
    </div>
    <!-- End of Modal content -->
  </div>
  <!-- End of Modal dialog -->
</div>