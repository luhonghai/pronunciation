<%@tag description="Header" pageEncoding="UTF-8" %>
<%@attribute name="index"%>

<!-- Header -->

<nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
	<div style="height:50px;">
	<div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
			<span class="sr-only">Toggle navigation</span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
		</button>
		<div class="col-xs-9 col-sm-11">
			<img class="img-responsive" style="margin: 0 auto;" alt="accenteasy logo" src="http://s3-ap-southeast-1.amazonaws.com/com-accenteasy-bbc-accent-prod/images/accenteasy_icon_text.png"/>
		</div>
	</div>
	<!-- /.navbar-header -->


	<!-- /.navbar-top-links -->
	</div>
	<div class="navbar-default sidebar" role="navigation">
		<div class="sidebar-nav navbar-collapse">
			<ul class="nav" id="side-menu">
				<% String role=session.getAttribute("role").toString();

				if(role.equals("1") || role.equals("2")){

					if (session.getAttribute("role")==null){
						return;
					}
					if(session.getAttribute("role").equals(1)){
				%>

				<li>
					<a href="#"><i class="fa fa-user-secret"></i> Admin</a>
					<ul>
						<li><a href="admin-manage.jsp">Manage User</a></li>
						<li><a href="admin-manage-transcription.jsp">Manage Transcription</a></li>
						<li><a href="admin-manage-recorder.jsp">Manage Recording</a></li>
						<li><a href="language-model.jsp">Language model</a></li>
						<li><a href="dictionary-manage.jsp">Dictionary management</a></li>
						<li><a href="acoustic-model.jsp">Acoustic model</a></li>
					</ul>
				</li>
				<%
					}
				%>

				<li>
					<a href="client-code-manage.jsp"><i class="fa fa-user"></i> Client Code </a>
				</li>

				<li>
					<a href="dashboard.jsp"><i class="fa fa-dashboard fa-fw"></i> Dashboard </a>
				</li>

				<li>
					<a href="feedbacks-manage.jsp" ><i class="glyphicon glyphicon-comment"></i> Feedback </a>
				</li>
				<li>
					<a href="#"><i class="fa fa-folder"></i> Lessons</a>
					<ul>

						<li>
							<a href="management_ipa_map.jsp" ><i class="fa fa-file-word-o"></i> Ipa Map Arpabet Management </a>
						</li>

						<li>
							<a href="word-management.jsp" ><i class="fa fa-file-word-o"></i> Word Management </a>
						</li>

						<li>
							<a href="question-management.jsp" ><i class="fa fa-question-circle"></i> Question Management </a>
						</li>
						<li>
							<a href="lessons-management.jsp" ><i class="fa fa-list"></i> Lessons Management </a>
						</li>
						<li>
							<a href="level-management.jsp" ><i class="fa fa-list-alt"></i> Level Management </a>
						</li>
						<li>
							<a href="course-management.jsp" ><i class="fa fa-table"></i> Course Management </a>
						</li>
						<li>
							<a href="management_country.jsp" ><i class="fa fa-language"></i> Language Management </a>
						</li>
						<li>
							<a href="database-manage.jsp" ><i class="fa fa-database"></i> Database Management  </a>
						</li>
					</ul>
				</li>



				<li>
					<a href="license-code.jsp"><i class="glyphicon glyphicon-euro"></i> Licence Code </a>
				</li>

				<li>
					<a href="pronunciation-phoneme.jsp"><i class="glyphicon glyphicon-heart"></i> Phoneme Score </a>
				</li>

				<li>
					<a href="test-page.jsp"><i class="fa fa-stethoscope"></i> Test Page </a>
				</li>

				<li>
					<a href="total-user.jsp"><i class="fa fa-bar-chart-o fa-fw"></i> Total user </a>

					<!-- /.nav-second-level -->
				</li>

				<li>
					<a href="system-setting.jsp"><i class="fa fa-edit fa-fw"></i> Setting </a>
				</li>

				<li>
					<a href="pronunciation-score.jsp"><i class="glyphicon glyphicon-heart"></i> Word Score </a>
				</li>
				<%}else {
					if(role.equals("3")){%>
						<li>
						<a href="pronunciation-score.jsp"><i class="fa fa-users"></i> Teacher Management </a>
						</li>
					<%}else {%>
						<li>
							<a href="class.jsp"><i class="fa fa-slideshare"></i> Class Management </a>
						</li>
					<%}
				}%>





				<li>
					<a href="logout.jsp" style="color: red"><i class="fa fa-sign-out fa-fw"></i> Logout</a>
				</li>
				<%--<li class="dropdown">--%>
					<%--<span id="dLabel" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="padding-top:10px; padding-bottom: 10px; padding-left: 15px; display: block; position: relative;">--%>
						<%--<i class="fa fa-user fa-fw" style="color: #3276b1;"></i>  <i class="fa fa-caret-down" style="color: #3276b1;"></i>--%>
					<%--</span>--%>
					<%--<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">--%>

						<%--<li class="divider"></li>--%>
						<%--<li><a href="logout.jsp" style="color: red"><i class="fa fa-sign-out fa-fw"></i> Logout</a>--%>
						<%--</li>--%>
					<%--</ul>--%>
					<%--<!-- /.dropdown-user -->--%>
				<%--</li>--%>

			</ul>
		</div>
		<!-- /.sidebar-collapse -->
	</div>
	<!-- /.navbar-static-side -->
</nav>
<!-- /Header -->


