<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@ tag import="com.cmg.merchant.common.Constant" %>
<%@ tag import="com.cmg.merchant.util.SessionUtil" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%
    String company = (String)  StringUtil.isNull(session.getAttribute(SessionUtil.ATT_CPNAME), "").toString();;
%>
<link rel="stylesheet" type="text/css"
      href="<%=request.getContextPath() %>/bower_components/AJAX_PROCESS_BAR/dist/css/jquery.progresstimer.min.css"
      media="all">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/maincourse.css" media="all">
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h4 style="margin-top: 10px;border-bottom: transparent" class="page-header header-company"><%=company%> >
                courses > my courses</h4>
        </div>
        <!-- /.col-lg-12 -->
    </div>

    <!-- /.row header-->
    <div class="row">
        <div class="col-lg-4 pull-left">
            <a id="open_popup" data-toggle="modal" class="btn btn-info"
               style="background-color: #E46C0A;border-color: #E46C0A">
                add course<span class="glyphicon glyphicon-plus" style="padding-left: 5px"></span>
            </a>
        </div>
        <div class="col-lg-8 pull-right">
            <div class="input-group" id="adv-search" style="float:right">
                <input id="suggestCourseHeader" type="text" autocomplete="off" class="form-control suggestCourse"
                       data-provide="typeahead" placeholder="Search course"/>

                <div class="input-group-btn">
                    <div class="btn-group" role="group">
                        <div class="dropdown dropdown-lg">
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-expanded="false"><span class="caret"></span></button>
                            <div id="containSearchDetail" class="dropdown-menu dropdown-menu-right" role="menu">
                                <form class="form-horizontal" role="form">
                                    <div class="form-group">
                                        <label for="suggestCompany">company</label>
                                        <input id="suggestCompany" autocomplete="off" placeholder="Enter company name"
                                               class="form-control suggestCompany" type="text"/>
                                    </div>
                                    <div class="form-group">
                                        <label for="suggestCourse">course title</label>
                                        <input id="suggestCourse" autocomplete="off" placeholder="Enter course title"
                                               class="form-control suggestCourse" type="text"/>
                                    </div>
                                    <div class="form-group" style="margin-bottom: 0px;">
                                        <label>created date range</label>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-sm-6" style="padding-left: 0px;">
                                            <label for="fromdate" class="sr-only"></label>
                                            <input id="fromdate" class="form-control input-group-lg reg_name"
                                                   type="text"
                                                   name="fromdate" title="From Date" placeholder="Date From">
                                        </div>
                                        <div class="col-sm-6" style="padding-right: 0px;">
                                            <label for="todate" class="sr-only"></label>
                                            <input id="todate" class="form-control input-group-lg reg_name" type="text"
                                                   name="todate" title="To Date" placeholder="Date To">
                                        </div>

                                    </div>
                                    <button id="btnSearchDetail" type="button"
                                            class="btn btn-primary"><span class="glyphicon glyphicon-search"
                                                                          aria-hidden="true"></span></button>
                                </form>
                            </div>
                        </div>
                        <button id="btnSearchHeader" type="button" class="btn btn-primary"><span
                                class="glyphicon glyphicon-search" aria-hidden="true"></span></button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- /.row -->
    <div class="row">
        <div class="col-lg-12" style="padding-top: 20px">
            <label class="welcome">Select from the list below to view the content of courses that have
                been shared, or select the 'add course' button above to create a bespoke course of your own.</label>
        </div>
    </div>

    <div class="row">
        <div id="content-courses" class="col-lg-12" style="padding-top:20px">
        </div>
        <div id="first-process" class="col-lg-4" style="padding-top: 20px"></div>
        <div id="process-bar" class="col-lg-4" style="padding-top: 20px"></div>
        <div id="last-process" class="col-lg-4" style="padding-top: 20px"></div>
    </div>
    <!-- /#page-wrapper -->


    <div id="add" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
         style="display: none;">
        <div class="modal-dialog" style="width:500px">
            <div class="modal-content">
                <div class="modal-header" style="border-bottom: transparent;padding-bottom: 0px;text-align: center">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h5 class="modal-title" style="text-align: center;font-weight: 200;font-size:25px">add course</h5>
                    <h4 id="validateCourseMsg" class="modal-title validateMsg"
                        style="text-align: center;font-weight: 200;color:red;display:none;"></h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="addCourseForm" name="addform">
                        <div class="form-group">
                            <label style="padding-top:7px;" class="control-label col-md-4 lbl_addForm"
                                   for="name">name:</label>

                            <div class="col-md-8">
                                <input type="text" class="form-control" id="name" name="name" placeholder="Course name">
                            </div>
                        </div>
                        <div class="form-group" style="margin-top:10px">
                            <label class="control-label col-md-4 lbl_addForm" style="padding-top:0px" for="description">description:</label>

                            <div class="col-md-8">
                                <textarea rows="2" class="form-control" id="description" name="description"
                                          placeholder="Description"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-4 lbl_addForm" for="shareall">share all:</label>

                            <div class="col-md-6">
                                <input type="checkbox" class="form-control cbstyle"
                                       id="shareall" checked name="share" value="<%=Constant.SHARE_ALL%>">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-4 lbl_addForm" for="mycompany">share - my
                                company:</label>

                            <div class="col-md-6">
                                <input type="checkbox" style="padding-top: 10px" class="form-control cbstyle"
                                       id="mycompany" name="share" value="<%=Constant.SHARE_IN_COMPANY%>">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-4 lbl_addForm" for="private">private:</label>

                            <div class="col-md-6">
                                <input type="checkbox" style="padding-top: 10px" class="form-control cbstyle"
                                       id="private" name="share" value="<%=Constant.SHARE_PRIVATE%>">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-md-6">
                                <img id="helpAddCourse" style="cursor: pointer" src="/images/popup/help_50_50.png"
                                     width="36px" height="36px"/>
                            </div>
                            <div class="col-md-6">
                                <img style="float: right;cursor: pointer" id="btnSaveCourse"
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
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h2 class="modal-title" style="font-weight: 700;">add course</h2>
                </div>
                <div class="modal-body">

                </div>
                <!-- End of Modal body -->
            </div>
            <!-- End of Modal content -->
        </div>
        <!-- End of Modal dialog -->
    </div>


</div>
<script src="<%=request.getContextPath() %>/js/merchant/suggestion/typeahead.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/suggestion/suggestion.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/mycourses/action.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/mycourses/ui.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/mycourses/data.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/data/popupdata.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/validate/validateform.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/mycourses/ajax.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/AJAX_PROCESS_BAR/dist/js/jquery.progresstimer.min.js"></script>
<!-- /#wrapper -->



