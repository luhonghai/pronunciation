<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="x" tagdir="/WEB-INF/tags/sub"%>

<t:main pageTitle="Wholesale delivery system" index="0">
   <x:java pageTitle="java"></x:java>
    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">User Manage</h1>
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
                            <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                <thead>
                                <tr>
                                    <th>Username</th>
                                    <th>Emei</th>
                                    <th>Appversion</th>
                                    <th>Map</th>
                                    <th>Time</th>
                                    <th></th>
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

    <div id="mapDetail" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <div class="row">
                        <div class="col-xs-12 col-md-10 col-md-offset-1">

                            <h1 align="center">Map Detail</h1>
                            <form name="map" class="form-horizontal"
                                  style="margin-top: 25px" id="feedback">
                                <div id="map"></div>


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
<script src="<%=request.getContextPath() %>/js/manageUser.js"></script>