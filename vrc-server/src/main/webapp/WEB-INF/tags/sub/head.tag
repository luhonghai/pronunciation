<%@tag description="Head" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
        <title>BBC Accent</title>

        <!-- Bootstrap Core CSS -->
        <link href="<%=request.getContextPath()%>/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/bower_components/bootstrap-colorpicker-master/dist/css/bootstrap-colorpicker.min.css" rel="stylesheet">



        <link href="<%=request.getContextPath()%>/bower_components/bootstrap-glyphicons-master/css/bootstrap.icon-large.min.css" rel="stylesheet">

        <!-- MetisMenu CSS -->
        <link href="<%=request.getContextPath()%>/bower_components/metisMenu/dist/metisMenu.min.css" rel="stylesheet">

        <!-- DataTables CSS -->
        <link href="<%=request.getContextPath()%>/bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.css" rel="stylesheet">

        <!-- DataTables Responsive CSS -->
        <link href="<%=request.getContextPath()%>/bower_components/datatables-responsive/css/dataTables.responsive.css" rel="stylesheet">
        <%--<link href="bower_components/datatables/media/css/jquery.dataTables.min.css" rel="stylesheet">--%>

        <!-- Custom CSS -->
        <link href="<%=request.getContextPath()%>/dist/css/sb-admin-2.css" rel="stylesheet">

        <link href="<%=request.getContextPath()%>/bower_components/circleplayer-master/css/not.the.skin.css" rel="stylesheet">

        <link href="<%=request.getContextPath()%>/bower_components/circleplayer-master/circle.skin/circle.player.css" rel="stylesheet">

        <!-- Custom Fonts -->
        <link href="<%=request.getContextPath()%>/bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.0.1/sweetalert.min.css">

        <link href="http://hayageek.github.io/jQuery-Upload-File/4.0.6/uploadfile.css" rel="stylesheet">
        <script src="<%=request.getContextPath() %>/bower_components/jquery/dist/jquery.min.js"></script>
        <%--<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.18/jquery-ui.js"></script>--%>
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
        <script>
                var CONTEXT_PATH = "<%=request.getContextPath()%>";
        </script>
</head>