<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@ tag import="com.cmg.merchant.common.Constant" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%
  String company = (String) StringUtil.isNull(request.getSession().getAttribute("companyName"),"CMG");
  String idCourse = (String)StringUtil.isNull(request.getParameter("idCourse"),"66b3510d-8964-47a0-8c33-72dc14f8dded");
%>
<script type="javascript">
    var loadCourse = "<%=Constant.TARGET_LOAD_COURSE%>";
    var servlet = "/TreeLoadServlet";
    var idCourse= "<%=idCourse%>";
</script>
<style>
  .welcome{
    color : #376092;
    font-weight: 200;
  }
  .header-company{
    color: #A6A6A6
  }

  .aciTree {
    width: 100% !important;
    height: 100% !important;
  }
</style>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/bower_components/aciTree/css/aciTree.css" media="all">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/bower_components/aciTree/css/demo.css" media="all">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/bower_components/aciTree/css/jquery.contextMenu.css" media="all">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/tree.css" media="all">
<div id="page-wrapper">
  <div class="row">
    <div class="col-lg-12">
      <h4 class="page-header header-company"><%=company%> > all courses > course sample</h4>
    </div>
  </div>

  <!-- /.row -->
  <div class="row">
    <div class="col-lg-12" style="padding-top: 20px">
        <div id="tree" class="aciTree">
            <div>
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
<script src="<%=request.getContextPath() %>/js/merchant/coursedetail/treeApi.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/coursedetail/action.js"></script>
</div></div>