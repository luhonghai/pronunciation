<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@ tag import="com.cmg.merchant.common.Constant" %>
<%@ tag import="com.cmg.merchant.services.CourseServices" %>
<%@ tag import="com.cmg.merchant.util.SessionUtil" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%
    CourseServices cServices = new CourseServices();
    String company = (String) StringUtil.isNull(request.getSession().getAttribute(SessionUtil.ATT_CPNAME), "CMG");
    String idCourse = (String) StringUtil.isNull(request.getParameter("idCourse"), "66b3510d-8964-47a0-8c33-72dc14f8dded");
    String nameOfCourse = (String) StringUtil.isNull(cServices.getCourseName(idCourse), "");
%>
<script>
    var idCourse = "<%=idCourse%>";
    var nameOfCourse = "<%=nameOfCourse%>";
    var loadCourse = "<%=Constant.TARGET_LOAD_COURSE%>";
    var action_edit_course = "<%=Constant.ACTION_EDIT_COURSE%>";
    var action_edit_level = "<%=Constant.ACTION_EDIT_LEVEL%>";
    var action_edit_obj = "<%=Constant.ACTION_EDIT_OBJ%>";
    var action_edit_test = "<%=Constant.ACTION_EDIT_TEST%>";
    var action_edit_lesson = "<%=Constant.ACTION_EDIT_LESSON%>";
    var action_edit_question = "<%=Constant.ACTION_EDIT_QUESTION%>";
    var action_edit_question_test = "<%=Constant.ACTION_EDIT_QUESTION_TEST%>";
</script>
<style>
    .contain-button{
        display : none !important;
    }
    .fa-minus-circle{
        display : none !important;
    }
    #btnAddWord{
        display : none !important;
    }
    #btnAddWordTest{
        display : none !important;
    }
</style>
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
            <div id="process-bar" class="center-block" style="padding-top: 20px;width:400px"></div>
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
            <img id="copyCourse" style="float:right;cursor: pointer;" src="/images/treeview/duplicated_button.gif" width="50px" height="50px"/>
        </div>
    </div>
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
<script src="<%=request.getContextPath() %>/js/merchant/data/popupdata.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/reviewcourse/action.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/reviewcourse/treeApi.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/treeUtil/util.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/reviewcourse/ui.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/reviewcourse/ajax.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/AJAX_PROCESS_BAR/dist/js/jquery.progresstimer.min.js"></script>

