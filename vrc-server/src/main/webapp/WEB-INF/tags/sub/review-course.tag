<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@ tag import="com.cmg.merchant.common.Constant" %>
<%@ tag import="com.cmg.merchant.services.CourseServices" %>
<%@ tag import="com.cmg.merchant.util.SessionUtil" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%
    CourseServices cServices = new CourseServices();
    String company = (String) StringUtil.isNull(request.getSession().getAttribute(SessionUtil.ATT_CPNAME), "CMG");
    String idCourse = (String) StringUtil.isNull(request.getSession().getAttribute(SessionUtil.ATT_COURSE_ID), "66b3510d-8964-47a0-8c33-72dc14f8dded");
    String nameOfCourse = (String) StringUtil.isNull(cServices.getCourseName(idCourse), "");
%>
<script>
    var idCourse = "<%=idCourse%>";
    var nameOfCourse = "<%=nameOfCourse%>";
    var loadCourse = "<%=Constant.TARGET_LOAD_COURSE%>";
    var action_edit_level = "<%=Constant.ACTION_EDIT_LEVEL%>";
    var action_edit_obj = "<%=Constant.ACTION_EDIT_OBJ%>";
    var action_edit_test = "<%=Constant.ACTION_EDIT_TEST%>";
</script>
<link rel="stylesheet" type="text/css"
      href="<%=request.getContextPath() %>/bower_components/AJAX_PROCESS_BAR/dist/css/jquery.progresstimer.min.css"
      media="all">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/bower_components/aciTree/css/aciTree.css"
      media="all">
<link rel="stylesheet" type="text/css"
      href="<%=request.getContextPath() %>/bower_components/aciTree/css/jquery.contextMenu.css" media="all">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/tree.css" media="all">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/coursedetail.css" media="all">
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h4 class="page-header header-company">review course > <%=company%> > <%=nameOfCourse%>
            </h4>
        </div>
    </div>

    <!-- /.row -->
    <div class="row">
        <div class="col-lg-12">
            <div id="tree" class="aciTree">

            </div>
        </div>
    </div>
    <div class="row">
        <div id="first-process" class="col-lg-2" style="padding-top: 20px"></div>
        <div id="process-bar" class="col-lg-8" style="padding-top: 20px"></div>
        <div id="last-process" class="col-lg-2" style="padding-top: 20px"></div>
    </div>
    <div class="row">
        <div class="col-lg-11">
            <img style="float:right" src="/images/treeview/preview_button.gif" width="50px" height="50px"/>
        </div>
        <div class="col-lg-1">
            <img style="float:right" src="/images/treeview/duplicated_button.gif" width="50px" height="50px"/>
        </div>
    </div>
</div>
<div id="popupLevel" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="display: none;">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <label id="arrow" class="modal-title"
                       style="text-align: left;"><%=nameOfCourse%>
                </label>
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
                            <input type="text" disabled class="form-control" id="lvName" name="name"
                                   placeholder="Level name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-4 lbl_addForm" for="lvDesc">description:</label>
                        <div class="col-md-8">
                            <textarea disabled rows="2" class="form-control" id="lvDesc" name="description"
                                      placeholder="Level description"></textarea>
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
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <label id="arrowObj" class="modal-title"
                       style="text-align: left;"><%=nameOfCourse%>
                </label>
                <h2 id='titlePopupObj' class="modal-title">Add Objective</h2>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="addObj" name="addform">
                    <div class="form-group">
                        <label class="control-label col-md-4 lbl_addForm" for="objName">name:</label>

                        <div class="col-md-8">
                            <input type="text" disabled class="form-control" id="objName" name="name"
                                   placeholder="Objective name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-4 lbl_addForm" for="objDesc">description:</label>

                        <div class="col-md-8">
                            <textarea rows="2" disabled class="form-control" id="objDesc" name="description"
                                      placeholder="Objective description"></textarea>
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
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <label id="arrowTest" class="modal-title"
                       style="text-align: left;"><%=nameOfCourse%>
                </label>
                <h2 id='titlePopupTest' class="modal-title">Add Test</h2>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="addTest" name="addform">
                    <div class="form-group">
                        <label class="control-label col-md-3 lbl_addForm" for="percent">pass %:</label>
                        <div class="col-md-8">
                            <input disabled type="text" class="form-control" id="percent" name="percent"
                                   placeholder="Percent pass">
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


<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.aciPlugin.min.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.aciSortable.min.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.aciTree.dom.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.aciTree.core.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.aciTree.utils.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.aciTree.selectable.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.aciTree.sortable.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.contextMenu.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/reviewcourse/data.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/reviewcourse/action.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/reviewcourse/treeApi.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/reviewcourse/ajax.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/AJAX_PROCESS_BAR/dist/js/jquery.progresstimer.min.js"></script>

