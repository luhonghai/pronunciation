<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
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
                        <a href="ManageFeedbacks.jsp">
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
                                    <div>Total User!</div>
                                </div>
                            </div>
                        </div>
                        <a href="AllUser.jsp">
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
                                    <div>Pronunciation!</div>
                                </div>
                            </div>
                        </div>
                        <a href="PronunciationScore.jsp">
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
                                    <div>License Code!</div>
                                </div>
                            </div>
                        </div>
                        <a href="LicenseCode.jsp">
                            <div class="panel-footer">
                                <span class="pull-left">View Details</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
            </div>
            <div class="row" style="margin-top: 50px;">
                <div class="col-md-4" id="geochart1" style="width: 400px; height: 200px;"></div>
                <div class="col-md-4 col-md-offset-2">
                    <canvas id="buyers" style="width: 400px; height: 200px;"></canvas>

                </div>
            </div>

            <!-- /.row -->
        </div>

</t:main>
<script src="<%=request.getContextPath() %>/js/dashboard.js"></script>
<script src="<%=request.getContextPath() %>/js/geochart.js"></script>
