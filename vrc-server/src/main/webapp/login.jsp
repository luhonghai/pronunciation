<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title> Login </title>
    <meta name="generator" content="Bootply" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
    <!--[if lt IE 9]>
    <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <link href="css/styles.css" rel="stylesheet">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">

    <!-- Latest compiled and minified JavaScript -->
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>


    <script>
        var CONTEXT_PATH = '<%=request.getContextPath()%>';
    </script>
    <script src="<%=request.getContextPath() %>/js/login.js"></script>

</head>
<body>
<!--login modal-->
<div id="loginModal" class="modal show" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <img class="img-responsive" style="margin: 0 auto;" alt="accenteasy logo" src="http://s3-ap-southeast-1.amazonaws.com/com-accenteasy-bbc-accent-prod/images/accenteasy_icon_text.png"/>
            </div>
            <div class="modal-body">
                <form >
                    <div id="result" style="margin-bottom: 10px;"></div>
                    <div class="form-group">
                        <input type="text" id="account" name="account" class="form-control input-lg" placeholder="Email">
                    </div>
                    <div class="form-group">
                        <input type="password" id="password" name="password" class="form-control input-lg" placeholder="Password">
                    </div>
                    <div class="form-group">
                        <button type="button" id="login" name="login" class="btn btn-primary btn-lg btn-block">Sign In</button>

                    </div>
                </form>
            </div>
            <div class="modal-footer" style="text-align: center; font-size: 10px;color: grey">
                 Version <%=com.cmg.vrc.properties.Configuration.getValue(com.cmg.vrc.properties.Configuration.PROJECT_VERSION)%> &copy; 2015 Claybourne McGregor Consulting Ltd (C-MG)
            </div>
        </div>
    </div>
</div>


</body>
</html>