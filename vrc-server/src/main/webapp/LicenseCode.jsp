<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:main pageTitle="Wholesale delivery system" index="0">
  <div id="page-wrapper">
    <div class="row">
      <div class="col-lg-12">
        <h1 class="page-header">License Code</h1>
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
              <option value="Yes">Yes</option>
              <option value="No">No</option>

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
            <button type="button" id="addCode" name="addCode">Add Code</button>

             </div>
          <!-- /.panel-heading -->
          <div class="panel-body">
            <div class="dataTable_wrapper">
              <table class="table table-striped table-bordered table-hover table-responsive" id="dataTables-example">
                <thead>
                <tr>
                  <th>Account</th>
                  <th>IMEI</th>
                  <th>Code</th>
                  <th>Activated Date</th>
                  <th>Activation</th>
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
  <div id="addCode1" class="modal fade">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"
                  aria-hidden="true">&times;</button>
          <h2 class="modal-title" align="center">Add Code</h2>
        </div>
        <form name="form-delete">
          <div class="modal-body">
            <h4>Do you want to Add Code?</h4>
          </div>
          <div class="modal-footer">
            <button type="button" name="Yes" id="Yes"
                    class="btn btn-default" >Yes</button>

            <button type="button" class="btn btn-primary"
                    data-dismiss="modal">Cancel</button>
          </div>
        </form>
      </div>
    </div>
  </div>

</t:main>
<script src="<%=request.getContextPath() %>/js/licenseCode.js"></script>