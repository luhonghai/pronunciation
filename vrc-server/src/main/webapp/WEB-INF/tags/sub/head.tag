<%@tag description="Head" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>BBC Accent</title>

        <!-- Bootstrap Core CSS -->
        <link href="bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

        <link href="bower_components/bootstrap-glyphicons-master/css/bootstrap.icon-large.min.css" rel="stylesheet">

        <!-- MetisMenu CSS -->
        <link href="bower_components/metisMenu/dist/metisMenu.min.css" rel="stylesheet">

        <!-- DataTables CSS -->
        <link href="bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.css" rel="stylesheet">

        <!-- DataTables Responsive CSS -->
        <link href="bower_components/datatables-responsive/css/dataTables.responsive.css" rel="stylesheet">
        <%--<link href="bower_components/datatables/media/css/jquery.dataTables.min.css" rel="stylesheet">--%>


        <!-- Custom CSS -->
        <link href="dist/css/sb-admin-2.css" rel="stylesheet">

        <!-- Custom Fonts -->
        <link href="bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->
        <style>
                /*@media (min-width: 768px) and (max-width: 970px) {*/
                        /*.form-horizontal .control-label {*/
                                /*padding-top: 0px;*/
                        /*}*/
                /*}*/
                /*th, td { white-space: nowrap; }*/
                /*div.dataTables_wrapper {*/
                        /*width: 800px;*/
                        /*margin: 0 auto;*/
                /*}*/
        </style>

        <%
                if (session.getAttribute("username") == null || session.getAttribute("password") == null){
                        response.sendRedirect("login.jsp");
                }
        %>
        <%--<%--%>
                <%--if(session.getAttribute("role").equals("admin")){--%>

                <%--}--%>
                <%--if(session.getAttribute("role").equals("user")){--%>

                <%--}--%>

        <%--%>--%>

        <script>
                var CONTEXT_PATH = "<%=request.getContextPath()%>";
        </script>
</head>