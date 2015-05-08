<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:main pageTitle="Wholesale delivery system" index="0">


        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">System Setting</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row" style="margin-top: 30px;">
                <div class="col-md-4">
                    <input class="form-control" name="noAccessMessage" type="text" id="noAccessMessage" placeholder="noAccessMessage">
                </div>
                <div class="col-md-8">
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" value="true">
                            Registration
                        </label>
                    </div>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <div class="row" style="margin-top: 20px;">
                <div class="col-md-offset-5">
                <button id="save" class="btn">Save</button>
                </div>
            </div>
            <!-- /.row -->
        </div>
</t:main>
