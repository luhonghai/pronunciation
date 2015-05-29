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
            <label class="control-label">Account</label>
            <input type="text" name="filter-account" id="account1" class="form-control" placeholder="Account">
          </div>
          <div class="form-group">
            <label class="control-label">Code</label>
            <input type="text" name="filter-code" id="code1" class="form-control" placeholder="Code">
          </div>
        </div>
        <div class="col-sm-4">
          <div class="form-group">
            <label class="control-label">Date From</label>
            <div>
              <input type='text' class="form-control" id="dateFrom1" placeholder="From"/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">Activated</label>
            <select name="Acti" id="Acti" class="form-control" required="required">
              <option value="All">All</option>c
              <option value="isActivated">isActivated</option>
              <option value="Activated">Activated</option>

            </select>
          </div>
        </div>
        <div class="col-sm-4">
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
                  <th>ID</th>
                  <th>User Name</th>
                  <th>Word</th>
                  <th>Score</th>
                  <th>Date</th>
                  <th></th>
                </tr>
                </thead>
                <tbody>
                <tr class="odd gradeX">
                  <td>1</td>
                  <td>nambui</td>
                  <td>hello</td>
                  <td>70</td>
                  <td>10-12-2014</td>
                  <td><a href="UserManage.jsp" class="btn btn-default btn-xs edit"><i class="glyphicon glyphicon-triangle-right"></i></a></td>

                </tr>
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

</t:main>
