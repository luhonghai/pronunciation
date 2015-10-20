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
<script src="<%=request.getContextPath() %>/bower_components/moment-master/min/moment.min.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/bootstrap-colorpicker-master/dist/js/bootstrap-colorpicker.min.js"></script>

<script src="<%=request.getContextPath() %>/bower_components/circleplayer-master/js/jquery.grab.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/circleplayer-master/js/jquery.jplayer.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/circleplayer-master/js/mod.csstransforms.min.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/circleplayer-master/js/circle.player.js"></script>


<script src="<%=request.getContextPath() %>/bower_components/bootstrap-datetimepicker-master/buildd/js/bootstrap-datetimepicker.min.js"></script>
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<%--<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>--%>
<%--&lt;%&ndash;<script>&ndash;%&gt;--%>
	<%--$(document).ready(function() {--%>
		<%--$('#dataTables-example').DataTable({--%>
			<%--responsive: true--%>
		<%--});--%>
	<%--});--%>
<%--</script>--%>
<!-- Custom Theme JavaScript -->

<script type="text/javascript" src="//www.google.com/jsapi"></script>
<script src="http://hayageek.github.io/jQuery-Upload-File/4.0.6/jquery.uploadfile.min.js"></script>
<%--<script src="<%=request.getContextPath() %>/js/jsapi.js"></script>--%>
<script src="<%=request.getContextPath() %>/js/Chart.min.js"></script>

