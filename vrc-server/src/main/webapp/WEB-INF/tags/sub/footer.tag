<%@ tag import="com.cmg.vrc.properties.Configuration" %>
<%@tag description="Footer" pageEncoding="UTF-8" %>
	    <div class="footer" style="height: 100px;">
	    	<div class="container">
		    	<div class="row">
		    		<div class="col-md-12">
						<div style="text-align: center; font-size: 15px;color: grey">
							Version <%=Configuration.getValue(Configuration.PROJECT_VERSION)%> &copy; 2015 Claybourne McGregor Consulting Ltd (C-MG)
						</div>
		    		</div>
		    	</div>
		    </div>
	    </div>

        <!-- Javascripts -->
<!-- jQuery -->
<script src="<%=request.getContextPath() %>/bower_components/jquery/dist/jquery.min.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="<%=request.getContextPath() %>/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
<script src="http://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.0.1/sweetalert.min.js"></script>
<script src="<%=request.getContextPath() %>/js/handlebars-v3.0.3.js"></script>
<!-- Metis Menu Plugin JavaScript -->
<script src="<%=request.getContextPath() %>/bower_components/metisMenu/dist/metisMenu.min.js"></script>

<!-- DataTables JavaScript -->
<script src="<%=request.getContextPath() %>/bower_components/datatables/media/js/jquery.dataTables.min.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.min.js"></script>


<!-- Custom Theme JavaScript -->
<script src="<%=request.getContextPath() %>/dist/js/sb-admin-2.js"></script>

<script src="<%=request.getContextPath() %>/bower_components/moment-master/min/moment.min.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/raphael/raphael-min.js"></script>

<script src="<%=request.getContextPath() %>/bower_components/bootstrap-datetimepicker-master/buildd/js/bootstrap-datetimepicker.min.js"></script>
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>
<%--<script>--%>
	<%--$(document).ready(function() {--%>
		<%--$('#dataTables-example').DataTable({--%>
			<%--responsive: true--%>
		<%--});--%>
	<%--});--%>
<%--</script>--%>
<!-- Custom Theme JavaScript -->

<script type="text/javascript" src="//www.google.com/jsapi"></script>
<%--<script src="<%=request.getContextPath() %>/js/jsapi.js"></script>--%>
<script src="<%=request.getContextPath() %>/js/Chart.min.js"></script>
