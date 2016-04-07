<%@ tag import="com.cmg.merchant.util.SessionUtil" %>
<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<style>

  .textNormal{
    color: #376092;
    font-weight: 200;
    font-size: 14px;
    font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
    font-size: 14px;
  }
   .header-company {
     color: #A6A6A6;
     font-weight: 200;
     font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
     font-size: 14px;
   }

   .modal-content{
     -webkit-border-radius: 20px;
     -moz-border-radius: 20px;
     border-radius: 20px;
   }
   .modal-header {
     border-bottom: transparent;
     padding-bottom: 0px;
     text-align: center;
   }

   .modal-title {
     font-weight: 700;
   }
  #helpClass{
    color : #957F7F;
  }
  #helpClass p{
    color : #957F7F !important;
  }
</style>
<%String company= StringUtil.isNull(session.getAttribute(SessionUtil.ATT_CPNAME),"").toString();%>
<div id="page-wrapper">
  <div class="row">
    <div class="col-lg-12">
      <h4 class="header-company"><%=company%> > classes</h4>
    </div>
  </div>
  <div style="padding-bottom: 20px;">
    <p class="textNormal">Your 'my classes' page is the place where you can bring together your courses and students to create classes.</p>
  </div>

  <div class="row">
      <div align="center"><img src="/images/teacher/my_classes80x80.gif" title="my classes"></div>
      <div  class="row">
        <label class="col-sm-4"></label>
        <div class="col-sm-2"  style="text-align: center"><img src="/images/teacher/arrow30x60.gif" style="-ms-transform: rotate(45deg);-webkit-transform: rotate(45deg);transform: rotate(45deg);"></div>
        <div class="col-sm-2"  style="text-align: center"><img src="/images/teacher/arrow30x60.gif"style="-ms-transform: rotate(-45deg);-webkit-transform: rotate(-45deg);transform: rotate(-45deg);"></div>
        <label class="col-sm-4"></label>
      </div>
      <div class="row">
        <label class="col-sm-2"></label>
        <div class="col-sm-4"  style="text-align: center"><img src="/images/teacher/my_students80x80.gif" title="my students" style="text-align: center"></div>
        <div class="col-sm-4"  style="text-align: center"><img src="/images/teacher/my_courses80x80.gif" title="my courses"></div>
        <label class="col-sm-2"></label>
      </div>
    
  </div>
  <div style="padding-top: 30px;">
    <p class="textNormal">Make sure that you have created or added the courses you wish to use in ‘my courses’ and that the students are available on your ‘my students’ page. You can add and remove courses and students to and from classes at any time while they are available on these pages.</p>
  </div>


</div>

<div id="helpClass" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content modal-body-help">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h2 class="modal-title">classes</h2>
      </div>
      <div class="modal-body">
        <p class="textNormal">Select ‘my classes’ to  create, edit and delete classes.</p>
        <p class="textNormal">After naming your class and allocating courses, you can assign the relevant students and the lessons will be sent to their phones.</p>
      </div>


    </div>
  </div>
</div>

<!-- /#wrapper -->

<script src="<%=request.getContextPath() %>/js/class.js"></script>




