<%@tag description="Header" pageEncoding="UTF-8" %>
<%@attribute name="index"%>

<!-- Header -->

<!--[if lt IE 7]>
<!-- Navigation -->

<nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
	<div style="height:70px;">
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

	<ul class="nav navbar-top-links navbar-right">
		<li class="dropdown">
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="fa fa-user fa-fw"></i>  <i class="fa fa-caret-down"></i>
			</a>
			<ul class="dropdown-menu dropdown-user">

				<li class="divider"></li>
				<li><a href="logout.jsp"><i class="fa fa-sign-out fa-fw"></i> Logout</a>
				</li>
			</ul>
			<!-- /.dropdown-user -->
		</li>
		<!-- /.dropdown -->
	</ul>
	<!-- /.navbar-top-links -->
	</div>
	<div class="navbar-default sidebar" role="navigation">
		<div class="sidebar-nav navbar-collapse">
			<ul class="nav" id="side-menu">

				<li>
					<a href="dashboard.jsp"><i class="fa fa-dashboard fa-fw"></i> Dashboard </a>
				</li>
				<li>
					<a href="AllUser.jsp"><i class="fa fa-bar-chart-o fa-fw"></i> Total User </a>

					<!-- /.nav-second-level -->
				</li>
				<li>
					<a href="ManageFeedbacks.jsp" ><i class="glyphicon glyphicon-comment"></i> Feedback </a>
				</li>
				<li>
					<a href="PronunciationScore.jsp"><i class="glyphicon glyphicon-heart"></i> Pronunciation Score </a>
				</li>
				<li>
					<a href="LicenseCode.jsp"><i class="glyphicon glyphicon-euro"></i> License Code </a>
				</li>
				<li>
					<a href="SystemSetting.jsp"><i class="fa fa-edit fa-fw"></i> Setting </a>
				</li>
				<li>
					<a href="AdminManage.jsp"><i class="fa fa-user-secret"></i> Admin</a>
				</li>



			</ul>
		</div>
		<!-- /.sidebar-collapse -->
	</div>
	<!-- /.navbar-static-side -->
</nav>
<!-- /Header -->


