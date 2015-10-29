<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Management Country</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->
    <div class="well">
        <div class="row">
            <div class="col-sm-3">
                <div class="form-group" style="margin-top: 32px;text-align: right;">
                    <label class="control-label" style="margin-bottom: 0px;">Created Date</label>
                </div>
            </div>
            <div class="col-sm-3">
                <div class="form-group">
                    <label class="control-label">From</label>
                    <div >
                        <input type='text' class="form-control" id='CreateDateFrom' placeholder="From" />
                    </div>
                </div>
            </div>
            <div class="col-sm-3">
                <div class="form-group">
                    <label class="control-label">To</label>
                    <div >
                        <input type='text' class="form-control" id='CreateDateTo' placeholder="From" />
                    </div>
                </div>

            </div>
            <div class="col-sm-3">
                <button type="button" id="button-filter" name="button-filter" class="btn btn-primary pull-right" style="margin-top:24px"><i class="fa fa-search"></i> Filter</button>

            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <button type="button" id="openAddMapping" name="addCode">Add New Country</button>
                </div>
                <!-- /.panel-heading -->
                <div class="panel-body">
                    <div class="dataTable_wrapper">
                        <div class="table-responsive">
                            <table class="table table-striped table-bordered table-hover table-responsive dt-responsive display nowrap" id="dataTables-example" cellspacing="0">
                                <thead>
                                <tr>
                                    <th>TYPE</th>
                                    <th>INDEX TYPE</th>
                                    <th>IPA</th>
                                    <th>ARPABET</th>
                                    <th>Date Created</th>
                                    <th>Color</th>
                                    <th>VOICE</th>
                                    <th></th>
                                </tr>
                                </thead>
                                <tbody>

                                </tbody>
                            </table>
                        </div>

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


<div id="add" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <div class="row">
                    <div class="col-xs-12 col-md-10 col-md-offset-1">

                        <h1 align="center">Add Country</h1>
                        <form name="add" class="form-horizontal"
                              style="margin-top: 25px" id="addform">
                            <div class="form-group">

                                <div>
                                    <label class="col-xs-4  col-sm-3 control-label ">Country :</label>
                                    <div class="col-xs-8  col-sm-9">
                                        <input  type="text" id="country_name" name="country_name" class=" form-control" style="padding-left: 0px; margin-bottom: 5px;">
                                    </div>
                                </div>

                                <div>
                                    <label class="col-xs-4  col-sm-3 control-label ">Description:</label>
                                    <div class="col-xs-8  col-sm-9">
                                        <textarea   type="text" id="add-description" name="description" rows="3" cols="50" class=" form-control" style="padding-left: 0px;margin-bottom: 5px;"></textarea>
                                    </div>
                                </div>

                                <div>
                                    <label class="col-xs-4  col-sm-3 control-label ">Image :</label>
                                    <div class="col-xs-8  col-sm-9">
                                        <input type="file" id="image" name="image" class="form-control" style="padding-left: 0px; margin-bottom: 5px;">
                                    </div>
                                </div>

                                <div id="container-add-lesson">
                                    <label class="col-xs-4  col-sm-3 control-label ">Course :</label>
                                    <img style="display:block" class="loading-lesson loading" src="http://i.imgur.com/m1fR7ef.gif"/>
                                    <select style="display:none" name="course" class="form-control" id="select-course">
                                        <option>1</option>
                                        <option>2</option>
                                        <option>3</option>
                                        <option>4</option>
                                        <option>5</option>
                                    </select>
                                </div>
                                <div>
                                    <label class="col-xs-4  col-sm-3 control-label ">Default:</label>
                                    <div class="col-xs-8  col-sm-9" style="margin-top: 8px;">
                                        <input type="checkbox" name="default" id="default">
                                    </div>
                                </div>

                            </div>
                            <div class="modal-footer">
                                <button type="button" name="submitForm" id="submitForm" class="btn btn-default" value="yes" >Yes</button>
                                <button type="button" name="cancel" id="cancel" class="btn btn-default" data-dismiss="modal" value="Close" >Close</button>

                            </div>

                        </form>
                    </div>
                </div>
            </div>


        </div>
    </div>
</div>

<div id="deletes" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">&times;</button>
                <h1 class="modal-title" align="center">Delete Mapping</h1>
            </div>
            <form name="form-delete" >
                <div class="modal-body">
                    <input type="hidden" id="iddelete" name="iddelete">
                    <h3>Do you want to delete ?</h3>
                </div>
                <div class="modal-footer">
                    <button type="button" name="YesDelete" id="deleteItems" class="btn btn-default" >Yes</button>
                    <button type="button" name="closedelete" id="closedelete" class="btn btn-default" data-dismiss="modal" value="Close" >Close</button>
                </div>
            </form>
        </div>
    </div>
</div>
<script src="<%=request.getContextPath() %>/js/Lession/data/manage_country_data.js"></script>
<script src="<%=request.getContextPath() %>/js/Lession/ui/manage_country_ui.js"></script>
<script src="<%=request.getContextPath() %>/js/Lession/manage_country.js"></script>

