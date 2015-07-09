<%@ page import="com.cmg.vrc.data.dao.impl.UserDAO" %>
<%@ page import="com.cmg.vrc.data.jdo.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8">
  <meta charset="utf-8">
  <title>Reset password</title>
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
  <!--[if lt IE 9]>
  <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
  <![endif]-->
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.0.1/sweetalert.min.css">
  <!-- Latest compiled and minified JavaScript -->
  <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
  <script src="http://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.0.1/sweetalert.min.js"></script>
</head>
<body>
<div id="xModal" class="modal show" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <img class="img-responsive" style="margin: 0 auto;" alt="accenteasy logo" src="http://s3-ap-southeast-1.amazonaws.com/com-accenteasy-bbc-accent-prod/images/accenteasy_icon_text.png"/>
      </div>
      <div class="modal-body">
        <%
          String code = request.getParameter("code");
          User user = null;
          if (code != null && code.length() > 0) {
            UserDAO userDAO = new UserDAO();
            try {
              user = userDAO.getUserByResetPasswordCode(code);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        %>
        <%
          if (user != null) {
        %>
        <div id="reset-form">
         <h2 style="text-align: center">please enter your new password</h2>
          <input type="hidden" id="code" name="code" value="<%=code%>"/>
          <div class="form-group">
            <label for="password">new password</label>
            <input type="password" id="password" name="password" class="form-control input-lg">
          </div>
          <div class="form-group">
            <label for="password1">confirm password</label>
            <input type="password" id="password1" name="password1" class="form-control input-lg">
          </div>
          <div class="form-group">
            <button type="button" id="reset" name="reset" class="btn btn-primary btn-lg btn-block btn-reset">reset</button>
          </div>
        </div>
        <%} else {%>
          <p>Invalid reset password request.</p>
        <%}%>
      </div>
    </div>
  </div>
</div>
<script>
  $(document).ready(function(){
    $("#reset").click(function() {
      var p1 = $("#password").val();
      var p2 = $("#password1").val();
      if (p1.length >= 6 && p2.length >= 6) {
        if (p1 == p2) {
          $.ajax({
            url: "<%=request.getContextPath()%>/ResetPasswordHandler",
            type: "POST",
            data: {
              code: $("#code").val(),
              action: "reset",
              password: p1
            },
            success: function (data) {
              if (data.indexOf("success") != -1) {
                swal("Reset successfully!", "Please login with your new password.", "success");
                $("#reset-form").html("Reset successfully! Please login with your new password.");
              } else {
                swal("Could not reset!", data, "error");
              }
            },
            error: function (e) {
              swal("Could not reset!", "Could not connect to server", "error");
            }
          });
        } else {
          swal("Invalid password!", "Both passwords must match", "error");
          $("#password").val("");
          $("#password1").val("");
        }
      } else {
        swal("Invalid password!", "Passwords must be at least 6 characters in length", "error");
        $("#password").val("");
        $("#password1").val("");
      }
    });
  });
</script>
</body>
</html>