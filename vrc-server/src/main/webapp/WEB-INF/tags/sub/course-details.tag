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
                <h4 id="arrow" class="modal-title"
                    style="text-align: left;font-weight: 200"><%=nameOfCourse%>
                </h4>
                <h2 id='titlePopup' class="modal-title" style="text-align: center;font-weight: 200">Add Level</h2>
                <h4 id="validateLvMsg" class="modal-title .validateLvMsg"
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
                            <img style="float:left;cursor: pointer"
                                 src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAFhElEQVRIS32WW2wUVRzGv3POzM7eeqellaittRdusS2ReMMWUjCEthTkATSRbSQmyJPGJwmB+iJBE4g+mrCNAlZIqBINWkxaEIOEVApSpFIpWHrvdre7vezu7Mwx58zsttjgZGdndvfs9/v+3//sOUvwP0d549eFpmn6OCfVhNAKQpAJQgCQEAjpJgQXYSotd77ddv9xMmL0okMIGwnD7/W4asqKC5CV4YXL5URcN+SpGyamIlFMReYwPhmGric6uYKmO62LQYsAJfUnfV635l9bUYy83AwMjscxOqkjEjUxG+PSjKYSqAzQFEClwFRkGpNTQQBmU09rQ8tCx48ASrac8GdneX2bqldjMmzg5r05zMU5kBpF5u9FVASgBHCJJ9NEMDyKREJvud26pSkJSX1VOM/O8PiFeN9gHHcHoyJr+ah7MRt76pYiP0sB51yevQNRHP9pApd7ZiSfEYBzEzORMZgJvann1GZZiQSIzF2a2l9fW4m7C8TT3AxH9haiqsQN0xTCwqgFSJ6HT4+h/XokKSUhs6FhKCxR1N2y+b4ElNSd6Njw8soaRfPg6p8zKedet4K2j0rhcVIp+Ki4cMwRmTWw/eOBBTECemwWsUiw89ZXteuJcO/WHP11G6vQcT28IHMrnsoSDz7b9xT87QG0XJiEqOrTPQUozldlRQKy74tR9I/q8xAOTE+OgJpmESltOHlozaqig+60bNy4F7UH2c0kBIQSFOSoGAkZIHZjXyh3o3lXbgqw69gIxsMGmGg2AArAiM0gPhdqJiX1pzo2rltd82ACGA0aFkAKWVOEEoJ4giNuyokCTgmO7M7DuhVuCRgJJfDm52P2788CMM6hGAnMzEx0ktKG1mDDxqrMa3/NYUbOc3vuEYKYbs19TgiYQsEYwZbn07B/R47dZKD5bAhX+uK2p2QFHF4A44HhECnd2spfq34OV+7EUs2N6hzTc4YlzKglrlAp/uHr2dI5wHHhVgzH2qdljFbRFkD0JdcBPHg4DFLW+A2veWkVuvp0CQhOJxCN85RoUnxZrorj+/LhdRIJ+OOhjv1nI6CMggqADbEAQIGbo/fukACcDr6ydnnm7QEDD8d1GCZAbceKuKqW+7drM7G7Ji3l8J0vwwjMApSReYisgMvlY6mH48bNwRAp23amY1XZ0zW9g8BEBGCKnbeEMAlQVIamDel461WRLDAd49jdEoE0oFBpSFZhR+R1cLh5HDd7xjpJ+fYzh3Kysg9GTQ+GQvN5y2hUBsUGVBU7UVGkSbeBWY5f7hmpzyWEURkTOPBEOsfkWBh9fwebSXljW6Gi0f6szHzcD1BwakGEsLw6BIQhw6tgb60XSzMZLvUZuPzAhOoQFVomxFgBcDDg2SUcHReHMD0XLbLWoh1tHWnenJqY4UIwbgsnK3BYEb27KR1b17hSK/H738UR0qmEJE2IfhTnAmNDEXT9HujsOrpivQXY2VbIoPZrWh6mdQadLIjHruCD+nTUrnRKgMj6wI9xDM1SqJoCVWMStCyHIdtp4tz3/4RM06j87fBya7GzIOd8jDj8hC5BjDBANNh2LxxWFjlxoDEN6W6GrgETRy/FbXEFDifDk3kK8jMo2n8exmQg2nTtk9L55ToJWbHzBz+hDp9Bc2AqCqhosF2BuMo4pFvbtabA42F4Jl9Bmoug89dxTIUSLVcPFy/ecJKQlW+c94GqfoNlgDtccKi2sIOCinuNwe1S4PUw5KQzLMlUMB6Ioqc3HNJ1vJd0vmhHW7iPVvjOFyYMxQ+m1hBHGpyaEx6nBreHwe1mcLkZODERjScwPDqHaAydqkaaROYLdVI72n/fTL6u8HUUmhQ+Tlg1pbQClGUSSkEIDYGybkLJRZOipftY5WP/tvwLMRJAhxNK6h8AAAAASUVORK5CYII=">
                        </div>
                        <div class="col-md-2">
                            <img id="btnDeleteLevel" style="float:left;cursor:pointer;display:none;" class="deleteBtnPopUp">
                        </div>
                        <div class="col-md-5">
                            <img id="btnSaveLevel" style="float:right;cursor:pointer" class="saveBtnPopUp">
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
                <h4 id="arrowObj" class="modal-title"
                    style="text-align: left;font-weight: 200"><%=nameOfCourse%>
                </h4>
                <h2 id='titlePopupObj' class="modal-title" style="text-align: center;font-weight: 200">Add Objective</h2>
                <h4 id="validateObjMsg" class="modal-title .validateLvMsg"
                    style="text-align: center;font-weight: 200;color:red;display:none;">Enter your level
                    name</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="addObj" name="addform">
                    <div class="form-group">
                        <label class="control-label col-md-4 lbl_addForm" for="objName">Name:</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="objName" name="name"
                                   placeholder="Level name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-4 lbl_addForm" for="objDesc">Description:</label>

                        <div class="col-md-6">
                            <textarea rows="3" class="form-control" id="objDesc" name="description"
                                      placeholder="Level description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-5">
                            <input type="hidden" class="action">
                            <input type="hidden" class="idHidden">
                            <img id="btnHelpObj" style="float:left;cursor: pointer" class="saveBtnPopUp">
                        </div>
                        <div class="col-md-2">
                            <img id="btnDeleteObj" style="float:left;cursor:pointer;display:none;" class="deleteBtnPopUp">
                        </div>
                        <div class="col-md-5">
                            <img id="btnSaveObj" style="float:right;cursor:pointer" class="saveBtnPopUp">
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

