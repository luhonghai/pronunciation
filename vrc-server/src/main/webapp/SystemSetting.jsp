<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="x" tagdir="/WEB-INF/tags/sub"%>
<t:main pageTitle="Wholesale delivery system" index="0">
    <x:appDetail pageTitle="appDetail"></x:appDetail>

        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12" style="text-align: center">
                    <h1 class="page-header">System Setting</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <input type="hidden" id="id" name="id">
            <div class="row" style="margin-top: 30px;">
                <div class="col-sm-4">
                    <div class="form-group">
                        <textarea type="text" name="noAccessMessage" id="noAccessMessage" class="form-control" rows="3" required="required" placeholder="noAccessMessage"></textarea>
                        <%--<input class="form-control" name="noAccessMessage" type="text" id="noAccessMessage" placeholder="noAccessMessage">--%>
                    </div>
                    <div class="form-group">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox"  value="true" id="checkbox">
                                Registration
                            </label>
                        </div>
                    </div>

                </div>
                <!-- /.col-lg-12 -->
            </div>
            <div class="row" style="margin-top: 20px;">
                <div class="col-sm-2">
                <button id="save" class="btn">Save</button>
                </div>
            </div>
            <!-- /.row -->
        </div>
</t:main>
<script src="<%=request.getContextPath() %>/js/appDetail.js"></script>
