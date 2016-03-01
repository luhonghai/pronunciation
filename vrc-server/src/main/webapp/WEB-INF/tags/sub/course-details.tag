<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@ tag import="com.cmg.merchant.common.Constant" %>
<%@ tag import="com.cmg.merchant.services.CourseServices" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%
    CourseServices cServices = new CourseServices();
    String company = (String) StringUtil.isNull(request.getSession().getAttribute("companyName"), "CMG");
    String idCourse = (String) StringUtil.isNull(request.getParameter("idCourse"), "66b3510d-8964-47a0-8c33-72dc14f8dded");
    String nameOfCourse = (String) StringUtil.isNull(cServices.getCourseName(idCourse), "");
%>
<script>
    var idCourse = "<%=idCourse%>";
    var nameOfCourse = "<%=nameOfCourse%>";
    var loadCourse = "<%=Constant.TARGET_LOAD_COURSE%>";
    var action_add_level = "<%=Constant.ACTION_ADD_LEVEL%>";
    var action_edit_level = "<%=Constant.ACTION_EDIT_LEVEL%>";
    var action_delete_level = "<%=Constant.ACTION_DELETE_LEVEL%>";
    var action_add_obj = "<%=Constant.ACTION_ADD_OBJ%>";
    var action_delete_obj = "<%=Constant.ACTION_DELETE_OBJ%>";
    var action_edit_obj = "<%=Constant.ACTION_EDIT_OBJ%>";
    var action_add_test = "<%=Constant.ACTION_ADD_TEST%>";
    var action_edit_test = "<%=Constant.ACTION_EDIT_TEST%>";
    var action_delete_test = "<%=Constant.ACTION_DELETE_TEST%>";
</script>
<style>
    .welcome {
        color: #376092;
        font-weight: 200;
    }

    .header-company {
        color: #A6A6A6
    }

    .aciTree {
        width: 100% !important;
        height: 100% !important;
    }
</style>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/bower_components/aciTree/css/aciTree.css"
      media="all">
<link rel="stylesheet" type="text/css"
      href="<%=request.getContextPath() %>/bower_components/aciTree/css/jquery.contextMenu.css" media="all">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/tree.css" media="all">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/coursedetail.css" media="all">
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h4 class="page-header header-company"><%=company%> > course administration > <%=nameOfCourse%>
            </h4>
        </div>
    </div>

    <!-- /.row -->
    <div class="row">
        <div class="col-lg-12" style="padding-top: 20px">
            <div id="tree" class="aciTree">

            </div>
        </div>
    </div>

</div>

<div id="popupLevel" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="display: none;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <label id="arrow" class="modal-title"
                    style="text-align: left;font-weight: 200"><%=nameOfCourse%>
                </label>
                <h2 id='titlePopup' class="modal-title" style="text-align: center;font-weight: 200">Add Level</h2>
                <h4 id="validateLvMsg" class="modal-title validateMsg"
                    style="text-align: center;font-weight: 200;color:red;display:none;">Enter your level
                    name</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="addLevel" name="addform">
                    <div class="form-group">
                        <label class="control-label col-md-4 lbl_addForm" for="lvName">Name:</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="lvName" name="name"
                                   placeholder="Level name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-4 lbl_addForm" for="lvDesc">Description:</label>

                        <div class="col-md-6">
                            <textarea rows="3" class="form-control" id="lvDesc" name="description"
                                      placeholder="Level description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-5">
                            <input type="hidden" class="action">
                            <input type="hidden" class="idHidden">
                            <input type="button" style="float:left;cursor: pointer" class="helpBtnPopUp">
                        </div>
                        <div class="col-md-2">
                            <input type="button" id="btnDeleteLevel" style="float:left;cursor:pointer;display:none;" class="deleteBtnPopUp">
                        </div>
                        <div class="col-md-5">
                            <input type="button" id="btnSaveLevel" style="float:right;cursor:pointer" class="saveBtnPopUp">
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
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <label id="arrowObj" class="modal-title"
                    style="text-align: left;font-weight: 200"><%=nameOfCourse%>
                </label>
                <h2 id='titlePopupObj' class="modal-title" style="text-align: center;font-weight: 200">Add Objective</h2>
                <h4 id="validateObjMsg" class="modal-title validateMsg"
                    style="text-align: center;font-weight: 200;color:red;display:none;">Enter your level
                    name</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="addObj" name="addform">
                    <div class="form-group">
                        <label class="control-label col-md-4 lbl_addForm" for="objName">Name:</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="objName" name="name"
                                   placeholder="Objective name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-4 lbl_addForm" for="objDesc">Description:</label>

                        <div class="col-md-6">
                            <textarea rows="3" class="form-control" id="objDesc" name="description"
                                      placeholder="Objective description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-5">
                            <input type="hidden" class="action">
                            <input type="hidden" class="idHidden">
                            <input type="button" id="btnHelpObj" style="float:left;cursor: pointer" class="helpBtnPopUp">
                        </div>
                        <div class="col-md-2">
                            <input type="button" id="btnDeleteObj" style="float:left;cursor:pointer;display:none;" class="deleteBtnPopUp">
                        </div>
                        <div class="col-md-5">
                            <input type="button" id="btnSaveObj"  style="float:right;cursor:pointer" class="saveBtnPopUp">
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
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <label id="arrowTest" class="modal-title"
                       style="text-align: left;font-weight: 200"><%=nameOfCourse%>
                </label>
                <h2 id='titlePopupTest' class="modal-title" style="text-align: center;font-weight: 200">Add Test</h2>
                <h4 id="validateTestMsg" class="modal-title validateMsg"
                    style="text-align: center;font-weight: 200;color:red;display:none;">Enter your level
                    name</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="addObj" name="addform">
                    <div class="form-group">
                        <label class="control-label col-md-4 lbl_addForm" for="percent">Pass %:</label>
                        <div class="col-md-6">
                            <input type="text" class="form-control" id="percent" name="percent"
                                   placeholder="Percent pass">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-5">
                            <input type="hidden" class="action">
                            <input type="hidden" class="idHidden">
                            <input type="button" id="btnHelpTest" style="float:left;cursor: pointer" class="helpBtnPopUp">
                        </div>
                        <div class="col-md-2">
                            <input type="button" id="btnDeleteTest" style="float:left;cursor:pointer;display:none;" class="deleteBtnPopUp">
                        </div>
                        <div class="col-md-5">
                            <input type="button" id="btnSaveTest"  style="float:right;cursor:pointer" class="saveBtnPopUp">
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
<script src="<%=request.getContextPath() %>/js/merchant/coursedetail/data.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/coursedetail/validateform.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/coursedetail/action.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/coursedetail/treeApi.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/coursedetail/ajax.js"></script>

