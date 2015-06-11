<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="x" tagdir="/WEB-INF/tags/sub"%>
<t:main pageTitle="Wholesale delivery system" index="0">

  <div id="page-wrapper">
    <div class="row">
      <div class="col-lg-12" style="text-align: center">
        <h1 class="page-header">Restart Server</h1>
      </div>
      <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->
    <div class="row" style="margin-top: 20px;">
      <div class="col-sm-2">
        <button id="restart" class="btn">Restart</button>
      </div>
    </div>
    <!-- /.row -->
  </div>
</t:main>
<script src="<%=request.getContextPath() %>/js/restartServer.js"></script>
