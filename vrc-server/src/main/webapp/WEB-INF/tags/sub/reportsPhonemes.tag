<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%String company=session.getAttribute("company").toString();%>
<div id="page-wrapper">
     <div class="row">
         <h3><%=company%></h3>>reports
         <img src="" alt="Help page" style="width:30px;height:30px;float: right;">
     </div>
    <div>

    </div>
</div>
<!-- /#wrapper -->

<div id="helpReportModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <div class="row">
                    <div class="col-xs-12 col-md-10 col-md-offset-1">

                    </div>
                </div>
            </div>


        </div>
    </div>
</div>


<script src="<%=request.getContextPath() %>/js/licensedStudents.js"></script>




