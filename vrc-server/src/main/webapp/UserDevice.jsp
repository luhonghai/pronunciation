<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="x" tagdir="/WEB-INF/tags/sub"%>

<t:main pageTitle="Wholesale delivery system" index="0">
  <x:userDevice pageTitle="java"></x:userDevice>
  <div id="page-wrapper">
    <div class="row">
      <div class="col-lg-12">
        <h1 class="page-header">User Device</h1>
      </div>
      <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->

    <div class="well">
      <div class="row">
        <div class="col-sm-3">
          <div class="form-group">
            <label class="control-label">Model</label>
            <input type="text" name="filter-model" id="model" class="form-control" placeholder="Model">
          </div>
          <div class="form-group">
            <label class="control-label">osVersion</label>
            <input type="text" name="filter-osVersion" id="osVersion" class="form-control" placeholder="osVersion">
          </div>
        </div>
        <div class="col-sm-3">
          <div class="form-group">
            <label class="control-label">osApiLevel</label>
            <input type="text" name="filter-osApiLevel" id="osApiLevel" class="form-control" placeholder="osApiLevel">
          </div>
          <div class="form-group">
            <label class="control-label">Device Name</label>
            <input type="text" name="filter-osVersion" id="deviceName" class="form-control" placeholder="Device Name">
          </div>
        </div>
        <div class="col-sm-3">
          <div class="form-group">
            <label class="control-label">Date From</label>
            <div>
              <input type='text' class="form-control" id="dateFrom1" placeholder="From"/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">Emei</label>
            <input type="text" name="filter-emeisearch" id="emeisearch" class="form-control" placeholder="Emei">

            </select>
          </div>
        </div>
        <div class="col-sm-3">
          <div class="form-group">
            <label class="control-label">Date To</label>
            <div >

              <input type='text' class="form-control" id="dateTo1" placeholder="To" />

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
              <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                <thead>
                <tr>
                  <th>Model</th>
                  <th>OS Version</th>
                  <th>OS ApiLevel</th>
                  <th>Device Name</th>
                  <th>Emei</th>
                  <th>Attached Date</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
              </table>
            </div>
            <!-- /.table-responsive -->

          </div>
          <!-- /.panel-body -->
        </div>
        <!-- /.panel -->
      </div>
      <!-- /.col-lg-12 -->
    </div>
  </div>
  <!-- /#page-wrapper -->

</t:main>
<script src="<%=request.getContextPath() %>/js/userDevice.js"></script>
