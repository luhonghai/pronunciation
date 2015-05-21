<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:main pageTitle="Wholesale delivery system" index="0">
    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">Feedback Manage</h1>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->
        <div class="well">
            <div class="row">
                <div class="col-sm-3">
                    <div class="form-group">
                        <label class="control-label">Account</label>
                        <input type="text" name="filter-account" id="account" class="form-control" placeholder="Account">
                    </div>
                    <div class="form-group">
                        <label class="control-label">IMEI</label>
                        <input type="text" name="filter-imei" id="imei" class="form-control" placeholder="IMEI">
                    </div>

                </div>
                <div class="col-sm-3">
                    <div class="form-group">
                        <label class="control-label">App Version</label>
                        <input type="text" name="filter-appversion" id="appversion" class="form-control" placeholder="App Version">
                    </div>
                    <div class="form-group">
                        <label class="control-label">OS Version</label>
                        <input type="text" name="filter-osversion" id="osversion" class="form-control" placeholder="OS Version">
                    </div>

                </div>
                <div class="col-sm-3">
                    <div class="form-group">
                        <label class="control-label">Date From</label>
                        <div >
                            <input type='text' class="form-control" id='DateFrom' placeholder="From" />

                        </div>
                    </div>


                </div>
                <div class="col-sm-3">
                    <div class="form-group">
                        <label class="control-label">Date To</label>
                        <div >
                            <input type='text' class="form-control" id='DateTo' placeholder="To"/>

                        </div>
                    </div>
                    <button type="button" id="button-filter" name="button-filter" class="btn btn-primary pull-right" style="margin-top:24px"><i class="fa fa-search"></i> Filter</button>

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
                                    <th>Account</th>
                                    <th>IMEI</th>
                                    <th>App Version</th>
                                    <th>OS Version</th>
                                    <th>Created Date</th>
                                    <th></th>
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


    <div id="feedback1" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <div class="row">
                        <div class="col-xs-12 col-md-10 col-md-offset-1">

                            <h1 align="center">Feedback Detail</h1>
                            <form name="feedback" class="form-horizontal"
                                  style="margin-top: 25px" id="feedback">

                                <div class="form-group">

                                    <label class="col-xs-4  col-sm-3  control-label">Account:</label>
                                    <label id="accountmd" name="accountmd" class="col-xs-4  col-sm-3  "></label>

                                </div>
                                <div class="form-group">
                                    <label class="col-xs-4  col-sm-3  control-label">Description:</label>
                                    <label id="descriptionmd" name="descriptionmd" class="col-xs-4  col-sm-3  "></label>

                                </div>

                                <div class="form-group">
                                    <label class="col-xs-4  col-sm-3  control-label">IMEI:</label>
                                    <label id="IMEImd" name="IMEImd" class="col-xs-4  col-sm-3  "></label>

                                </div>
                                <div class="form-group">

                                    <label class="col-xs-4  col-sm-3  control-label">Device Name:</label>
                                    <label id="deviceNamemd" name="deviceNamemd" class="col-xs-4  col-sm-3 "></label>

                                </div>

                                <div class="form-group">

                                    <label class="col-xs-4  col-sm-3  control-label">App Version:</label>
                                    <label id="appVersionmd" name="appVersionmd" class="col-xs-4  col-sm-3  "></label>

                                </div>
                                <div class="form-group">

                                    <label class="col-xs-4  col-sm-3  control-label">OS Version:</label>
                                    <label id="OSVersionmd" name="OSVersionmd" class="col-xs-4  col-sm-3  "></label>

                                </div>
                                <div class="form-group">

                                    <label class="col-xs-4  col-sm-3  control-label">OS Api Level:</label>
                                    <label id="OSApiLevelmd" name="OSApiLevelmd" class="col-xs-4  col-sm-3"></label>

                                </div>
                                <div class="form-group">

                                    <label class="col-xs-4  col-sm-3  control-label">Stack Trace:</label>
                                    <textarea id="StackTracemd" name="StackTracemd" class="col-xs-4  col-sm-8  "></textarea>


                                </div>

                                <div class="form-group">

                                    <label class="col-xs-4  col-sm-3  control-label">Screenshoot:</label>

                                    <label id="Screenshootmd" name="Screenshootmd" class="col-xs-4  col-sm-3 "></label>

                                </div>



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
<script src="<%=request.getContextPath() %>/js/feedback.js"></script>