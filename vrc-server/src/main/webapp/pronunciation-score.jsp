<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:main pageTitle="Wholesale delivery system" index="0">

  <div id="page-wrapper">
    <div class="row">
      <div class="col-lg-12">
        <h1 class="page-header">Word Score</h1>
      </div>
      <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->
    <div class="well">
      <div class="row">
        <div class="col-sm-3">
          <div class="form-group">
            <label class="control-label">UserName</label>
            <input type="text" name="filter-username" id="username" class="form-control" placeholder="UserName">
          </div>
          <div class="form-group">
            <label class="control-label">Word</label>
            <input type="text" name="filter-word" id="word" class="form-control" placeholder="Word">
          </div>
        </div>
        <div class="col-sm-3">
          <div class="form-group" style="margin-top: 40px;text-align: right;">
            <label class="control-label" style="margin-bottom: 0px;">Recorded Date</label>
          </div>
          <div class="form-group">
            <label class="control-label">Score</label>
            <select name="score" id="score" class="form-control" required="required">
              <option value="3"></option>
              <option value="1">0-50</option>
              <option value="2">51-100</option>
            </select>
          </div>
        </div>
        <div class="col-sm-3">
          <div class="form-group">
            <label class="control-label">From</label>
            <div>
              <input type='text' class="form-control" id="dateFrom" placeholder="From"/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">Type</label>
            <select name="type" id="type" class="form-control" required="required">
              <option value=""></option>
              <option value="F">F</option>
              <option value="Q">Q</option>
              <option value="T">T</option>
            </select>
          </div>
        </div>
        <div class="col-sm-3">
          <div class="form-group">
            <label class="control-label">To</label>
            <div >
              <input type='text' class="form-control" id="dateTo" placeholder="To" />
            </div>
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
              <div class="table-responsive">
              <table class="table table-striped table-bordered table-hover dt-responsive display nowrap" id="dataTables-example" cellspacing="0">
                <thead>
                <tr>
                  <th>User Name</th>
                  <th>Word</th>
                  <th>Score</th>
                  <th>Date</th>
                  <th>Type</th>
                  <th>Map</th>
                  <th></th>
                </tr>
                </thead>
                <tbody>
                </tbody>
              </table>
                </div>

            </div>
            <!-- /.panel-body -->
          </div>
          <!-- /.panel -->
        </div>
        <!-- /.col-lg-12 -->
      </div>

    </div>
    <div class="row" id="scoreavg" style="margin-bottom: -10px; margin-top: 20px">

      <label class="col-sm-2" style="text-align: right; margin-left: 40px">Score average: </label>
      <label class="col-sm-2" id="avg" style="padding-left: 0px;"></label>
    </div>
    <div class="row" style="margin-top: 5px;">
      <div id="dashboard">
        <div id="drawchart"></div>
        <div id="control_div"></div>
      </div>
    </div>
    <!-- /#page-wrapper -->

  </div>
  <!-- /#wrapper -->
  <!-- /#page-wrapper -->
  <div id="mapDetail" class="modal fade">
    <div class="modal-dialog"  style="padding: 0px; position: static;">
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



</t:main>
<script src="<%=request.getContextPath() %>/js/pronunciation.js"></script>