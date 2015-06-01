<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:main pageTitle="Wholesale delivery system" index="0">

    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">User Manage</h1>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <div class="well">
            <div class="row">
                <div class="col-sm-3">
                    <div class="form-group">
                        <label class="control-label">UserName</label>
                        <input type="text" name="filter-username" id="username" class="form-control" placeholder="UserName">
                    </div>
                    <div class="form-group">
                        <label class="control-label">Full Name</label>
                        <input type="text" name="filter-fullname" id="fullname" class="form-control" placeholder="Full Name">
                    </div>
                </div>
                <div class="col-sm-3">
                    <div class="form-group">
                        <label class="control-label">Gender</label>
                        <select name="gender" id="gender" class="form-control" required="required">
                            <option value="All">All</option>c
                            <option value="Man">Man</option>
                            <option value="WoMan">Woman</option>

                        </select>
                    </div>
                    <div class="form-group">
                        <label class="control-label">Country</label>
                        <input type="text" name="filter-country" id="country" class="form-control" placeholder="Country">
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
                        <label class="control-label">Activated</label>
                        <select name="Acti" id="Acti" class="form-control" required="required">
                            <option value="All">All</option>c
                            <option value="isActivated">isActivated</option>
                            <option value="Activated">Activated</option>

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
                                    <th>UserName</th>
                                    <th>Full Name</th>
                                    <th>Login type</th>
                                    <th>Gender</th>
                                    <th>Date Of Birth</th>
                                    <th>Country</th>
                                    <th>Activation code</th>
                                    <th>Activation</th>
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
    <!-- /#page-wrapper -->

</t:main>
<script src="<%=request.getContextPath() %>/js/users.js"></script>
