<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%String company=session.getAttribute("company").toString();%>
<div id="page-wrapper">
  <div class="row">
    <h3 style="float: left;"><%=company%></h3><p style="margin-top: 25px;">>classes</p>
  </div>
  <div>
    <p>Your 'my classes' page is the place where you can bring together your courses and students to create classes.</p>
  </div>

  <div>

  </div>
  <div>
    <p>Make sure that you have created or added the courses you wish to use in ‘my courses’ and that the students are available on your ‘my students’ page. You can add and remove courses and students to and from classes at any time while they are available on these pages.</p>
  </div>


</div>
<!-- /#wrapper -->

<script src="<%=request.getContextPath() %>/js/class.js"></script>




