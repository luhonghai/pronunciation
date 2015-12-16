<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:main pageTitle="Wholesale delivery system" index="0">

  <div id="page-wrapper">
    <div class="row">
      <div class="col-lg-12">
        <h1 class="page-header">Phoneme Score</h1>
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
            <label class="control-label">Phoneme</label>
            <input type="text" name="filter-phoneme" id="phoneme" class="form-control" placeholder="Phoneme">
          </div>
        </div>
        <div class="col-sm-3">
          <div class="form-group" style="margin-top: 40px;text-align: right;">
            <label class="control-label" style="margin-bottom: 0px;">Recorded Date</label>
          </div>
          <div class="form-group">
            <label class="control-label">Country</label>
            <input type="text" name="filter-country" id="country" class="form-control" placeholder="Country">
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
          <div class="form-group">
            <label class="control-label">Score</label>
            <select name="score" id="score" class="form-control" required="required">
              <option value="3"></option>
              <option value="1">0-50</option>
              <option value="2">51-100</option>
            </select>
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
                  <th>Phoneme</th>
                  <th>Score</th>
                  <th>Country</th>
                  <th>Date</th>
                  <th>Type</th>
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
    <!-- /#page-wrapper -->

  </div>
  <!-- /#wrapper -->
  <!-- /#page-wrapper -->
   <%--Imei popup datail--%>


</t:main>
<script src="<%=request.getContextPath() %>/js/pronunciationPhoneme.js"></script>