<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:main pageTitle="Wholesale delivery system" index="0">

  <div id="page-wrapper">
    <div class="row">
      <div class="col-lg-12">
        <h1 class="page-header">Pronunciation Score</h1>
      </div>
      <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->
    <div class="well">
      <div class="row">
        <div class="col-sm-4">
          <div class="form-group">
            <label class="control-label">UserName</label>
            <input type="text" name="filter-username" id="username" class="form-control" placeholder="UserName">
          </div>
        </div>
        <div class="col-sm-4">
          <div class="form-group">
            <label class="control-label">Word</label>
            <input type="text" name="filter-word" id="word" class="form-control" placeholder="Word">
          </div>
        </div>
        <div class="col-sm-4">
          <div class="form-group">
            <label class="control-label">uuid</label>
            <input type="text" name="filter-uuid" id="uuid" class="form-control" placeholder="uuid">
          </div>
          <button type="button" id="buttonFilter" name="button-filter" class="btn btn-primary pull-right" style="margin-top:24px"><i class="fa fa-search"></i> Filter</button>
        </div>

      </div>
    </div>
    <div class="row">
      <div class="col-lg-12">
        <div class="panel panel-default">
          <div class="panel-heading">

          </div>
          <!-- /.panel-heading -->
          <div class="panel-body">
            <div class="dataTable_wrapper">
              <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                <thead>
                <tr>
                  <th>User Name</th>
                  <th>Word</th>
                  <th>Score</th>
                  <th>uuid</th>
                  <th>Map</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
              </table>

            </div>
            <!-- /.panel-body -->
          </div>
          <!-- /.panel -->
        </div>
        <!-- /.col-lg-12 -->
      </div>

    </div>
    <!-- /#page-wrapper -->

  </div>
  <!-- /#wrapper -->
  <!-- /#page-wrapper -->
  <div id="mapDetail" class="modal fade">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <div class="row">
            <div class="col-xs-12 col-md-10 col-md-offset-1">

              <h1 align="center">Map Detail</h1>
              <form name="map" class="form-horizontal"
                    style="margin-top: 25px" id="us">
                <div id="map" style="width: 480px; height: 480px;"></div>


                <div class="modal-footer">
                  <button type="button" name="close" id="close" class="btn btn-default" data-dismiss="modal"value="Close" >Close</button>

                </div>

              </form>
            </div>
          </div>
        </div>


      </div>
    </div>
  </div>

  <div class="row" style="margin-top: 50px;">
    <div class="col-md-4" id="mappronunciation" style="width: 400px; height: 200px;"></div>
    <div class="col-md-4 col-md-offset-2">
      <canvas id="buyers" style="width: 400px; height: 200px;"></canvas>

    </div>
  </div>

</t:main>
<script src="<%=request.getContextPath() %>/js/pronunciation.js"></script>