<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="x" tagdir="/WEB-INF/tags/sub"%>

<t:main pageTitle="Wholesale delivery system" index="0">
   <x:java pageTitle="java"></x:java>
    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">User management</h1>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->
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
                                    <th>Username</th>
                                    <th>Emei</th>
                                    <th>Appversion</th>
                                    <th>Map</th>
                                    <th>Time</th>
                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                                </div>
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

    <%--#popup detail emei--%>

    <div id="emeimodal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <div class="row">
                        <div class="col-xs-12 col-md-10 col-md-offset-1">

                            <h1 align="center">Imei Detail</h1>
                            <form name="feedback" class="form-horizontal"
                                  style="margin-top: 25px" id="feedback">

                                <div class="form-group">

                                    <label class="col-xs-4  col-sm-3 ">Imei:</label>
                                    <label id="emeipopup" name="emeipopup" class="col-xs-8  col-sm-9  "></label>

                                </div>
                                <div class="form-group">
                                    <label class="col-xs-4  col-sm-3 ">Devicename:</label>
                                    <label id="devicenamepopup" name="devicenamepopup" class="col-xs-8  col-sm-9  "></label>

                                </div>

                                <div class="form-group">
                                    <label class="col-xs-4  col-sm-3 ">Model:</label>
                                    <label id="modelpopup" name="modelpopup" class="col-xs-8  col-sm-9  "></label>

                                </div>s
                                <div class="form-group">

                                    <label class="col-xs-4  col-sm-3 ">Osversion:</label>
                                    <label id="osversionpopup" name="osversionpopup" class="col-xs-8  col-sm-9 "></label>

                                </div>

                                <div class="form-group">

                                    <label class="col-xs-4  col-sm-3 ">Osapilevel:</label>
                                    <label id="osapilevelpopup" name="osapilevelpopup" class="col-xs-8  col-sm-9  "></label>

                                </div>
                                <div class="form-group">

                                    <label class="col-xs-4  col-sm-4 ">Attached Date:</label>
                                    <label id="attacheddatepopup" name="attacheddatepopup" class="col-xs-8  col-sm-8  "></label>

                                </div>

                                <div class="modal-footer">
                                    <button type="button" name="close" id="closepopup" class="btn btn-default" data-dismiss="modal"value="Close" >Close</button>

                                </div>


                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>


</t:main>
<script src="<%=request.getContextPath() %>/js/manageUser.js"></script>