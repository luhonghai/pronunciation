<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<link href="<%=request.getContextPath() %>/bower_components/bootstrap-fileinput-master/css/fileinput.min.css" type="text/css" rel="stylesheet"/>

<t:main pageTitle="Wholesale delivery system" index="0">
    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12" style="text-align: center">
                <h1 class="page-header">Test Page</h1>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->
        <div class="row" style="margin-top: 30px;">
            <form name="test" class="form-horizontal" id="testform">
                <div class="row">
                    <div class="col-sm-2">
                          <label class="control-label">Text :</label>
                    </div>
                    <div class="col-sm-5">
                          <input type="text" name="word" id="word" class="form-control" placeholder="Word">
                    </div>
                </div>
                <div class="row" style="margin-top: 10px">
                    <div class="col-sm-2">
                         <label class="control-label">Audio :</label>
                    </div>
                    <div class="col-sm-5">
                        <div id="wrap-audio-add"><input type="file" id="audio" name="audio" class="form-control" style="padding-left: 0px; margin-bottom: 5px;"></div>
                    </div>
                </div>
                <div class="row" style="margin-top: 10px; padding-left: 15px">
                    <button type="button" name="test" id="test" class="btn btn-default btn-info" style="padding-left: 20px; padding-right: 20px">Test</button>
                </div>

            </form>
            <%--<div id="loadScore" class="group-phoneme-weight">--%>
                <%--<div class="row" id="alphabet"></div>--%>
                <%--<div class="row" id="score"></div>--%>
            <%--</div>--%>
            <div id="loadFrame" class="group-phoneme-weight" style="margin-top: 20px; margin-left: 15px; overflow-x: auto;overflow-y: hidden;">
                <div class="row" id="alphabets"></div>
                <div class="row" id="listAlpabet"></div>
                <div class="row" id="count"></div>
                <div class="row" id="totalScore"></div>
            </div>
            <div id="bestPhonemes" style="margin-top: 20px; margin-left: 15px;">
                <div class="row" id="bestPhoneme"></div>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->
    </div>

</t:main>
<script src="<%=request.getContextPath() %>/js/testPage.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/bootstrap-fileinput-master/js/fileinput.min.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/bootstrap-fileinput-master/js/fileinput_locale_uk.js"></script>