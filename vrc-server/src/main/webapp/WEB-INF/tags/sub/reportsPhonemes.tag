<%@ tag import="com.cmg.merchant.util.SessionUtil" %>
<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%String company = StringUtil.isNull(session.getAttribute(SessionUtil.ATT_CPNAME), "").toString();%>
<style>

    .header-company {
        color: #A6A6A6;
        font-weight: 200;
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
        font-size: 14px;
        border-bottom-color: transparent;
        margin: 10px 0px;
    }
    #loadInfo{
        cursor: pointer;
        width: 36px;
        height : 36px;
        display: block;
        background-color: transparent;
        border-color: transparent;
        padding : 0px;
    }
    .row label {
        font-weight: 200;
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
        font-size: 14px;
        padding-top: 5px;
    }

    .row {
        padding-bottom: 20px;
    }

    #grap-class-average, .color-class-help {
        width: 10px;
        height: 30px;
        border: 1px solid transparent;
        background-color: #558ED5;
    }

    #grap-student-score, .color-student-help {
        width: 10px;
        height: 30px;
        border: 1px solid transparent;
        background-color: #17375E;
    }

    #info label {
        color: #376092
    }

    .grap {
        border: 1px;
        background-color: #F2F2F2;
        border-radius: 5px;
        padding-top: 10px;
        margin-bottom: 20px;
    }

    .scoreStudent {
        margin: 30px 0px;
        background-color: #17375E;
        width: 70px;
        height: 70px;
        border-radius: 45px;
        text-align: center;
        line-height: 70px;
        color: white;
        font-size: 20px;
        font-weight: 600;
    }


    .student-avg-score {
        background-color: #17375E;
        width: 70px;
        height: 70px;
        border-radius: 45px;
        text-align: center;
        line-height: 70px;
        color: white;
        font-size: 20px;
        font-weight: 600;
        margin-top : 20px;
    }




    .student-username{
        padding-top: 25px;
    }

    .phoneme{
        padding-top: 25px;
    }

    .class-avg-text, .student-avg-text {
        padding-top: 5px;
    }

    #holder-chart {
        box-sizing: border-box;
        width: 100%;
        height: 600px;
        padding: 20px 15px 15px 15px;
        margin: 15px auto 30px auto;
        border: 1px solid #ddd;
        background: #fff;
        background: linear-gradient(#f6f6f6 0, #fff 50px);
        background: -o-linear-gradient(#f6f6f6 0, #fff 50px);
        background: -ms-linear-gradient(#f6f6f6 0, #fff 50px);
        background: -moz-linear-gradient(#f6f6f6 0, #fff 50px);
        background: -webkit-linear-gradient(#f6f6f6 0, #fff 50px);
        box-shadow: 0 3px 10px rgba(0, 0, 0, 0.15);
        -o-box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
        -ms-box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
        -moz-box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
        -webkit-box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
        overflow-x: auto;
    }

    #placeholder {
        width: 100%;
        height: 500px;
        font-size: 14px;
        line-height: 1.2em;
    }

    .legend table, .legend > div {
        height: 82px !important;
        opacity: 1 !important;
        right: -55px;
        top: 10px;
        width: 116px !important;
        display: none
    }

    .legend table {
        border: 1px solid #555;
        padding: 5px;
        display: none
    }

    #flot-tooltip {
        font-size: 12px;
        font-family: Verdana, Arial, sans-serif;
        position: absolute;
        display: none;
        border: 2px solid;
        padding: 2px;
        background-color: #FFF;
        opacity: 0.8;
        -moz-border-radius: 5px;
        -webkit-border-radius: 5px;
        -khtml-border-radius: 5px;
        border-radius: 5px;
        z-index: 100000;
    }

    .flot-x-axis div.flot-tick-label {
        /* Rotate Axis Labels */
        transform: translateX(60%) rotate(60deg); /* CSS3 */
        transform-origin: 0 0;

        -ms-transform: translateX(60%) rotate(60deg); /* IE */
        -ms-transform-origin: 0 0;

        -moz-transform: translateX(60%) rotate(60deg); /* Firefox */
        -moz-transform-origin: 0 0;

        -webkit-transform: translateX(60%) rotate(60deg); /* Safari and Chrome */
        -webkit-transform-origin: 0 0;

        -o-transform: translateX(60%) rotate(60deg); /* Opera */
        -o-transform-origin: 0 0;
    }
</style>
<div id="page-wrapper">
    <div class="row">
        <div class="col-sm-12">
            <h4 class="page-header header-company"><%=company%> >
                reports > lessons</h4>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-1"><label style="margin-top: 10px; float: left;">student:</label></div>
        <div class="col-sm-11">
            <select style="display:none; float: left;margin-left: 3px;" only class="form-control" id="listUsers">
            </select>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-1">
            <label>phoneme:</label>
        </div>
        <div class="col-sm-11">
            <select style="margin-top: 5px;padding: 5px;border-radius:3px; " id="listPhonemes">
            </select>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-1">
            <label>from:</label>
        </div>
        <div class="col-sm-2">
            <input type='text' class="form-control" id="dateFrom" placeholder="From"/>
        </div>
        <div class="col-sm-1">
            <label>to:</label>
        </div>
        <div class="col-sm-2">
            <input type='text' style="float: left;" class="form-control" id="dateTo" placeholder="To"/>
        </div>
        <div class="col-sm-6">
            <img id="loadInfo" disabled="disabled" src="/images/teacher/report_tick48x48.gif"
                  title="click here to run the reports" class="btn btn-info"
                 style="cursor: pointer;display: block;"/>
        </div>
    </div>
</div>
<!-- /#wrapper -->

<div id="helpReportModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
            </div>
        </div>
    </div>
</div>

<div id="report-popup" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
     style="display: none;color:#957F7F">
    <div class="modal-dialog" style="width:90%">
        <div class="modal-content" style="border-radius:20px;background-color: #F2F2F2;">
            <div class="modal-header" style="border-bottom: transparent;">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-sm-2">
                        <div class="col-sm-12">
                            <div class="student-username">lan.ta@c-mg.com</div>
                        </div>
                        <div class="col-sm-12">
                            <div class="phoneme">phoneme : t</div>
                        </div>
                        <div class="col-sm-12">
                            <div>top score</div>
                        </div>
                        <div class="col-sm-12">
                            <div class="student-avg-score">80</div>
                        </div>
                    </div>
                    <div id="container" class="col-sm-10">
                        <div id="holder-chart">
                            <div id="placeholder">
                            </div>
                        </div>
                        <%--    <div id="tip"></div>--%>
                    </div>
                </div>
            </div>
            <!-- End of Modal body -->
        </div>
        <!-- End of Modal content -->
    </div>
    <!-- End of Modal dialog -->
</div>
<script src="<%=request.getContextPath() %>/bower_components/flotbar/jquery.flot.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/flotbar/jquery.flot.time.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/flotbar/jshashtable-2.1.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/flotbar/jquery.numberformatter-1.2.3.min.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/flotbar/jquery.flot.symbol.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/flotbar/jquery.flot.axislabels.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/report/reportsPhonemes.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/report/draw-report-phoneme.js"></script>




