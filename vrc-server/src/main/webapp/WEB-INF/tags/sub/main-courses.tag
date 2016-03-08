<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@ tag import="com.cmg.merchant.common.Constant" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%
  String company = (String) StringUtil.isNull(request.getSession().getAttribute("company"),"CMG");
%>
<style>
  .welcome{
    color : #376092;
    font-weight: 200;
  }
  .header-company{
    color: #A6A6A6
  }

  .dropdown.dropdown-lg .dropdown-menu {
    margin-top: -1px;
    padding: 6px 20px;
  }
  .input-group-btn .btn-group {
    display: flex !important;
  }
  .btn-group .btn {
    border-radius: 0;
    margin-left: -1px;
  }
  .btn-group .btn:last-child {
    border-top-right-radius: 4px;
    border-bottom-right-radius: 4px;
  }
  .btn-group .form-horizontal .btn[type="submit"] {
    border-top-left-radius: 4px;
    border-bottom-left-radius: 4px;
  }
  .form-horizontal .form-group {
    margin-left: 0;
    margin-right: 0;
  }
  .form-group .form-control:last-child {
    border-top-left-radius: 4px;
    border-bottom-left-radius: 4px;
  }

  .cbstyle{
    box-shadow: inset 0 1px 1px rgba(255, 250, 250, 0.075) !important;
    width: 10%;
  }
  .lbl_addForm{
    font-weight: 200;
  }

  .container-course{
    margin-bottom: 5px;
  }

  .lbl_cname{
    font-weight: 200;
  }
  .lbl_cinfor{
    font-weight: 200;
    font-size: 10px;
    padding-left: 15px;
  }


  @media screen and (min-width: 768px) {
    #adv-search {
      width: 500px;
      margin: 0 auto;
    }
    .dropdown.dropdown-lg {
      position: static !important;
    }
    .dropdown.dropdown-lg .dropdown-menu {
      min-width: 500px;
    }
  }

</style>
<div id="page-wrapper">
  <div class="row">
    <div class="col-lg-12">
      <h4 class="page-header header-company"><%=company%> > all courses</h4>
    </div>
    <!-- /.col-lg-12 -->
  </div>

  <!-- /.row header-->
  <div class="row">
    <div class="col-lg-4 pull-left">
      <a href="#add" data-toggle="modal" class="btn btn-info" style="background-color: #E46C0A;border-color: #E46C0A">
        add course<span class="glyphicon glyphicon-plus" style="padding-left: 5px"></span>
      </a>
    </div>
    <div class="col-lg-8 pull-right">
      <div class="input-group" id="adv-search" style="float:right">
        <input id="suggestCourseHeader" type="text" class="form-control suggestCourse" data-provide="typeahead" placeholder="Search course" />
        <div class="input-group-btn">
          <div class="btn-group" role="group">
            <div class="dropdown dropdown-lg">
              <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><span class="caret"></span></button>
              <div class="dropdown-menu dropdown-menu-right" role="menu">
                <form class="form-horizontal" role="form">
                  <div class="form-group">
                    <label for="suggestCompany">Company</label>
                    <input id="suggestCompany" placeholder="Enter company name" class="form-control suggestCompany" type="text" />
                  </div>
                  <div class="form-group">
                    <label for="suggestCourse">Course title</label>
                    <input id="suggestCourse" placeholder="Enter course title" class="form-control suggestCourse" type="text" />
                  </div>
                  <div class="form-group">
                    <label>created date range</label>
                  </div>
                  <div class="form-group">
                      <div class="col-sm-6" style="padding-left: 0px;">
                        <label for="fromdate" class="sr-only"></label>
                        <input id="fromdate" class="form-control input-group-lg reg_name" type="text"
                               name="fromdate" title="From Date" placeholder="Date From">
                      </div>
                      <div class="col-sm-6" style="padding-right: 0px;">
                        <label for="todate" class="sr-only"></label>
                        <input id="todate" class="form-control input-group-lg reg_name" type="text"
                               name="todate" title="To Date" placeholder="Date To">
                      </div>

                  </div>
                  <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></button>
                </form>
              </div>
            </div>
            <button type="button" class="btn btn-primary"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></button>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- /.row -->
  <div class="row">
    <div class="col-lg-12" style="padding-top: 20px">
      <label class="welcome">Double click to select from the list below to view the content of courses that have been shared, or select the  ‘add course’ button above to create a bespoke course of your own.</label>
    </div>
  </div>

<div class="row">
  <div id="content-courses" class="col-lg-12" style="padding-top:20px;padding-left: 0px">
    <div class="col-lg-12 pull-left" class="container-course">
      <a class="btn btn-info" style="background-color: #558ED5;border-color: #E46C0A">
        <label class="lbl_cname">Aa Course for Vietnamese Students</label><label class="lbl_cinfor">created by CMG 03/12/2015</label>
      </a>
    </div>
    <div class="col-lg-12 pull-left" class="container-course">
      <a class="btn btn-info" style="background-color: #558ED5;border-color: #E46C0A">
        <label class="lbl_cname">Aa Course for Singapore Students</label><label class="lbl_cinfor">created by CMG 03/12/2015></label>
      </a>
    </div>
    <div class="col-lg-12 pull-left" class="container-course">
      <a class="btn btn-info" style="background-color: #17375E;border-color: #E46C0A">
        <label class="lbl_cname">Aa Course for Thai Students</label> <label class="lbl_cinfor">created by  BiZZ 06/11/2015</label>
      </a>
    </div>
  </div>
</div>
  <!-- /#page-wrapper -->


<div id="add" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h2 class="modal-title" style="text-align: center;font-weight: 200">Add Course</h2>
        <h4 id="validateCourseMsg" class="modal-title validateMsg"
            style="text-align: center;font-weight: 200;color:red;display:none;"></h4>
      </div>
      <div class="modal-body">
        <form class="form-horizontal" id="addCourseForm" name="addform">
          <div class="form-group">
            <label class="control-label col-md-4 lbl_addForm" for="name">Name:</label>
            <div class="col-md-6">
              <input type="text" class="form-control" id="name" name="name" placeholder="Course name">
            </div>
          </div>
          <div class="form-group">
            <label class="control-label col-md-4 lbl_addForm" for="description">Description:</label>
            <div class="col-md-6">
              <textarea rows="3" class="form-control" id="description" name="description" placeholder="Description"></textarea>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label col-md-4 lbl_addForm" for="shareall">Share all:</label>
            <div class="col-md-6">
              <input type="checkbox" class="form-control cbstyle"
                     id="shareall" checked name="share" value="<%=Constant.SHARE_ALL%>">
            </div>
          </div>
          <div class="form-group">
            <label class="control-label col-md-4 lbl_addForm" for="mycompany">Share in my company:</label>
            <div class="col-md-6">
              <input type="checkbox" style="padding-top: 10px" class="form-control cbstyle"
                     id="mycompany" name="share" value="<%=Constant.SHARE_IN_COMPANY%>">
            </div>
          </div>
          <div class="form-group">
            <label class="control-label col-md-4 lbl_addForm" for="private">Private:</label>
            <div class="col-md-6">
              <input type="checkbox" style="padding-top: 10px" class="form-control cbstyle"
                     id="private" name="share" values="<%=Constant.SHARE_PRIVATE%>">
            </div>
          </div>
          <div class="form-group">
            <div class="col-md-6">
              <img id="helpAddCourse" style="cursor: pointer"  src="/images/popup/Help_50x50.gif" width="36px" height="36px"/>
            </div>
            <div class="col-md-6">
              <img style="float: right;cursor: pointer" id="btnSaveCourse" src="/images/popup/Save_50x50.gif" width="36px" height="36px"/>
            </div>
          </div>
        </form>
      </div><!-- End of Modal body -->
    </div><!-- End of Modal content -->
  </div><!-- End of Modal dialog -->
</div>


</div>
<script src="<%=request.getContextPath() %>/js/merchant/suggestion/typeahead.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/suggestion/suggestion.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/maincourses/action.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/maincourses/ui.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/maincourses/data.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/maincourses/validateform.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/maincourses/ajax.js"></script>
<!-- /#wrapper -->



