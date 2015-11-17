<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<link href="<%=request.getContextPath() %>/bower_components/bootstrap-fileinput-master/css/fileinput.min.css" type="text/css" rel="stylesheet"/>
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Management IPA mapping with Arpabet</h1>
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
                    <button type="button" id="openAddMapping" name="addCode">Add New Mapping</button>
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

                        <h1 align="center">Add Mapping</h1>
                        <form name="add" class="form-horizontal"
                              style="margin-top: 25px" id="addform">

                            <div class="form-group">

                                <div class="row">
                                    <label class="col-xs-4  col-sm-4 control-label ">IPA:</label>
                                    <div class="col-xs-8  col-sm-8">
                                        <input  type="text" id="ipa" name="ipa" class=" form-control" style="padding-left: 0px; margin-bottom: 5px;">
                                    </div>
                                </div>
                                <div class="row">
                                    <label class="col-xs-4  col-sm-4 control-label ">ARPABET:</label>
                                    <div class="col-xs-8  col-sm-8">
                                        <input  type="text" id="arpabet" name="arpabet" class=" form-control" style="padding-left: 0px; margin-bottom: 5px;">
                                    </div>
                                </div>
                                <div class="row">
                                    <label class="col-xs-4  col-sm-4 control-label ">Description:</label>
                                    <div class="col-xs-8  col-sm-8">
                                        <textarea   type="text" id="description" name="description" rows="3" cols="50" class=" form-control" style="padding-left: 0px;margin-bottom: 5px;"></textarea>
                                    </div>
                                </div>
                                <div class="row">
                                    <label class="col-xs-4  col-sm-4 control-label ">Tip:</label>
                                    <div class="col-xs-8  col-sm-8">
                                        <textarea  type="text" id="tip" name="tip" rows="3" cols="50" class=" form-control" style="padding-left: 0px;margin-bottom: 5px;"></textarea>
                                    </div>
                                </div>
                                <div class="row">
                                    <label class="col-xs-4  col-sm-4 control-label ">Words:</label>
                                    <div class="col-xs-8  col-sm-8">
                                        <input type="text" id="words" name="words" class=" form-control" style="padding-left: 0px; margin-bottom: 5px;">
                                    </div>
                                </div>
                                <div class="row">
                                    <label class="col-xs-4  col-sm-4 control-label ">Color:</label>
                                    <div class="col-xs-8  col-sm-8">
                                        <input type="text" id="addColor" name="addColor" class=" form-control" style="padding-left: 0px; margin-bottom: 5px;">
                                    </div>
                                </div>
                                <div class="row">
                                    <label class="col-xs-4  col-sm-4 control-label ">Index:</label>
                                    <div class="col-xs-8  col-sm-8">
                                        <input type="text" id="index_type" onkeypress="return isNumberKey(event,this)" name="index_type" class=" form-control" style="padding-left: 0px; margin-bottom: 5px;">
                                    </div>
                                </div>
                                <div class="row">
                                    <label class="col-xs-4  col-sm-4 control-label ">MP3 URL:</label>
                                    <div class="col-xs-8  col-sm-8">
                                        <input type="text" id="mp3" name="mp3" class=" form-control" style="padding-left: 0px; margin-bottom: 5px;">
                                    </div>
                                </div>
                                <div class="row">
                                    <label class="col-xs-4  col-sm-4 control-label ">Text tongue:</label>
                                    <div class="col-xs-8  col-sm-8">
                                        <input type="text" id="tongueText" name="tongueText" class=" form-control" style="padding-left: 0px; margin-bottom: 5px;">
                                    </div>
                                </div>

                                <div class="row" style="margin-bottom: 5px;">
                                    <label class="col-xs-4  col-sm-4 control-label " style="text-align: left;">Tongue image:</label>
                                    <div class="col-xs-8  col-sm-8">
                                        <div id="wrap-imgTongue-edit"><img src="" id="imgTongue-edit"/> <a href="#" id="btn-imgTongue-edit" style="margin: 0 0 5px 10px;">Edit image</a></div>
                                        <div id="wrap-imgTongue-add"><input type="file" id="imageTongue" name="imageTongue" class="form-control" style="padding-left: 0px; margin-bottom: 5px;"></div>
                                    </div>
                                </div>
                                <div class="row">
                                    <label class="col-xs-4  col-sm-4 control-label ">Text lips:</label>
                                    <div class="col-xs-8  col-sm-8">
                                        <input type="text" id="lipsText" name="lipsText" class=" form-control" style="padding-left: 0px; margin-bottom: 5px;">
                                    </div>
                                </div>

                                <div class="row" style="margin-bottom: 5px;">
                                    <label class="col-xs-4  col-sm-4 control-label ">Lips image:</label>
                                    <div class="col-xs-8  col-sm-8">
                                        <div id="wrap-imgLips-edit"><img src="" id="imgLips-edit"/> <a href="#" id="btn-imgLips-edit" style="margin: 0 0 5px 10px;">Edit image</a></div>
                                        <div id="wrap-imgLips-add"><input type="file" id="imageLips" name="imageLips" class="form-control" style="padding-left: 0px; margin-bottom: 5px;"></div>
                                    </div>
                                </div>

                                <div class="row">
                                    <label class="col-xs-4  col-sm-4 control-label ">Text jaw:</label>
                                    <div class="col-xs-8  col-sm-8">
                                        <input type="text" id="jawText" name="jawText" class=" form-control" style="padding-left: 0px; margin-bottom: 5px;">
                                    </div>
                                </div>

                                <div class="row" style="margin-bottom: 5px;">
                                    <label class="col-xs-4  col-sm-4 control-label ">Jaw image:</label>
                                    <div class="col-xs-8  col-sm-8">
                                        <div id="wrap-imgJaw-edit"><img src="" id="imgJaw-edit"/> <a href="#" id="btn-imgJaw-edit" style="margin: 0 0 5px 10px;">Edit image</a></div>
                                        <div id="wrap-imgJaw-add"><input type="file" id="imageJaw" name="imageJaw" class="form-control" style="padding-left: 0px; margin-bottom: 5px;"></div>
                                    </div>
                                </div>


                                <div class="row">
                                    <label class="col-xs-4  col-sm-4 control-label ">Type:</label>
                                    <div class="col-xs-8  col-sm-8">
                                        <select name="type" id="type">
                                            <option value="vowel">vowel</option>
                                            <option value="consonant">consonant</option>
                                        </select>
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
<script src="<%=request.getContextPath() %>/bower_components/bootstrap-fileinput-master/js/fileinput.min.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/bootstrap-fileinput-master/js/fileinput_locale_uk.js"></script>
<script src="<%=request.getContextPath() %>/js/Lession/data/manage_ipa_map_data.js"></script>
<script src="<%=request.getContextPath() %>/js/Lession/ui/manage_ipa_map_ui.js"></script>
<script src="<%=request.getContextPath() %>/js/Lession/manage_ipa_map.js"></script>

