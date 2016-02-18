<%@tag description="Header" pageEncoding="UTF-8" %>
<%@attribute name="index"%>

<!-- Header -->

<nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
	<div style="height:50px;">
	<div class="navbar-header" style="width:100%;">
		<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
			<span class="sr-only">Toggle navigation</span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
		</button>
		<div class="col-xs-9 col-sm-10">
			<img class="img-responsive" style="margin: 0 auto;float:left;height:50px" alt="accenteasy logo" src="http://s3-ap-southeast-1.amazonaws.com/com-accenteasy-bbc-accent-prod/images/accenteasy_icon_text.png"/>
		</div>
		<div class="col-xs-3 col-sm-2">
			<img id="help-icons" class="img-responsive" style="display:none;margin: 0 auto;float:right;height:50px" alt="accenteasy logo" src="data:image/png;base64,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"/>
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
							<a href="objective-management.jsp" ><i class="fa fa-list"></i> Objective Management </a>
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
					if(role.equals("3") || role.equals("4")){
						if(role.equals("3")){
				%>
						<li>
						<a href="teacher-management.jsp"><i class="fa fa-users"></i> Teacher Management </a>
						</li>
					<%}%>
						<li>
							<a href="class.jsp"><i class="fa fa-slideshare"></i> Student Management </a>
							<ul>
								<li>
									<a href="class.jsp"><i class="fa fa-envelope-o"></i>  Class Management </a>
								</li>
								<li>
									<a href="my-students.jsp"><i class="fa fa-envelope-o"></i>  My Students </a>
								</li>
								<li>
									<a href="licensed-students.jsp"><i class="fa fa-envelope-o"></i>  Licensed Students </a>
								</li>
								<li>
									<a href="mail-user.jsp"><i class="fa fa-envelope-o"></i>  Send invitation </a>
								</li>
							</ul>
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


