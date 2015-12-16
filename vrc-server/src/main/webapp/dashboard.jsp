<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" tagdir="/WEB-INF/tags/content"%>
<t:main pageTitle="Dashboard" index="0">

       <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Dashboard</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row" style="margin-top: 30px;">
                <div class="col-lg-3 col-md-3 col-sm-6">
                    <div class="panel panel-primary">
                        <div class="panel-heading">
                            <div class="row">

                                <div class="col-xs-9 text-right">
                                    <div class="huge">
                                        <label id="feedback"></label>
                                    </div>
                                    <div>Feedbacks!</div>
                                </div>
                            </div>
                        </div>
                        <a href="feedbacks-manage.jsp">
                            <div class="panel-footer">
                                <span class="pull-left">View Details</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
                <div class="col-md-3 col-sm-6">
                    <div class="panel panel-green">
                        <div class="panel-heading">
                            <div class="row">

                                <div class="col-xs-9 text-right">
                                    <div class="huge">
                                        <label id="user"></label>
                                    </div>
                                    <div>Total user!</div>
                                </div>
                            </div>
                        </div>
                        <a href="total-user.jsp">
                            <div class="panel-footer">
                                <span class="pull-left">View Details</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
                <div class=" col-md-3 col-sm-6">
                    <div class="panel panel-yellow">
                        <div class="panel-heading">
                            <div class="row">

                                <div class="col-xs-9 text-right">
                                    <div class="huge">
                                        <label id="pronunciation"></label>
                                    </div>
                                    <div>Word Score!</div>
                                </div>
                            </div>
                        </div>
                        <a href="pronunciation-score.jsp">
                            <div class="panel-footer">
                                <span class="pull-left">View Details</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
                <div class="col-md-3 col-sm-6">
                    <div class="panel panel-red">
                        <div class="panel-heading">
                            <div class="row">

                                <div class="col-xs-9 text-right">
                                    <div class="huge">
                                        <label id="license"></label>
                                    </div>
                                    <div>Licence Code!</div>
                                </div>
                            </div>
                        </div>
                        <a href="license-code.jsp">
                            <div class="panel-footer">
                                <span class="pull-left">View Details</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
            </div>

            <div class="row" style="margin-top: 30px;">
                <div class="col-sm-4" id="geochart1" style="width: 400px; height: 200px;"></div>
                <div id="dashboard" class="pull-right col-sm-6">
                    <div id="drawchart"></div>
                    <div id="control_div"></div>
                </div>

            </div>
           <hr/>
           <h2 id="title-server" style="display: none">Application server</h2>
           <c:aws_control_dashboard></c:aws_control_dashboard>
            <!-- /.row -->
        </div>

</t:main>
<script src="<%=request.getContextPath() %>/js/dashboard.js"></script>
<script src="<%=request.getContextPath() %>/js/geochart.js"></script>

