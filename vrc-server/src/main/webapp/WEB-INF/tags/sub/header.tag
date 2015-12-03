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

				<%

					if (session.getAttribute("role")==null){
						return;
					}
					if(session.getAttribute("role").equals(1)){
				%>

				<li>
					<a href="#"><i class="fa fa-user-secret"></i> Admin</a>
					<ul>
						<li><a href="AdminManage.jsp">Manage User</a></li>
						<li><a href="AdminManageTranscription.jsp">Manage Transcription</a></li>
						<li><a href="AdminManageRecorder.jsp">Manage Recording</a></li>
						<li><a href="LanguageModel.jsp">Language model</a></li>
						<li><a href="Dictionary.jsp">Dictionary management</a></li>
						<li><a href="AcousticModel.jsp">Acoustic model</a></li>
					</ul>
				</li>
				<%
					}
				%>

				<li>
					<a href="ClientCodeManage.jsp"><i class="fa fa-user"></i> Client Code </a>
				</li>

				<li>
					<a href="dashboard.jsp"><i class="fa fa-dashboard fa-fw"></i> Dashboard </a>
				</li>

				<li>
					<a href="ManageFeedbacks.jsp" ><i class="glyphicon glyphicon-comment"></i> Feedback </a>
				</li>
				<li>
					<a href="#"><i class="fa fa-folder"></i> Lessons</a>
					<ul>

						<li>
							<a href="management_ipa_map.jsp" ><i class="fa fa-file-word-o"></i> Ipa Map Arpabet Management </a>
						</li>

						<li>
							<a href="ManagementWord.jsp" ><i class="fa fa-file-word-o"></i> Word Management </a>
						</li>

						<li>
							<a href="ManagementQuestion.jsp" ><i class="fa fa-question-circle"></i> Question Management </a>
						</li>
						<li>
							<a href="ManagementLessons.jsp" ><i class="fa fa-list"></i> Lessons Management </a>
						</li>
						<li>
							<a href="ManagementLevel.jsp" ><i class="fa fa-list-alt"></i> Level Management </a>
						</li>
						<li>
							<a href="ManagementCourse.jsp" ><i class="fa fa-table"></i> Course Management </a>
						</li>
						<li>
							<a href="management_country.jsp" ><i class="fa fa-file-word-o"></i> Language Management </a>
						</li>
						<li>
							<a href="Database.jsp" ><i class="fa fa-database"></i> Database Management  </a>
						</li>
					</ul>
				</li>



				<li>
					<a href="LicenseCode.jsp"><i class="glyphicon glyphicon-euro"></i> Licence Code </a>
				</li>

				<li>
					<a href="PronunciationPhoneme.jsp"><i class="glyphicon glyphicon-heart"></i> Phoneme Score </a>
				</li>

				<li>
					<a href="TestPage.jsp"><i class="fa fa-stethoscope"></i> Test Page </a>
				</li>

				<li>
					<a href="AllUser.jsp"><i class="fa fa-bar-chart-o fa-fw"></i> Total user </a>

					<!-- /.nav-second-level -->
				</li>

				<li>
					<a href="SystemSetting.jsp"><i class="fa fa-edit fa-fw"></i> Setting </a>
				</li>

				<li>
					<a href="PronunciationScore.jsp"><i class="glyphicon glyphicon-heart"></i> Word Score </a>
				</li>





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


