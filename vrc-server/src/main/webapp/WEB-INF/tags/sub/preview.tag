<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/preview.css" media="all">

<div id="preview-popup" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" style="width:500px">
        <div class="modal-content">
            <div class="modal-header" style="border-bottom: transparent;padding-bottom: 0px;text-align: center">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="form-group">
                        <div class="col-md-2" style="text-align: center;">
                        </div>
                        <div class="col-md-8" id="mobile-view">
                            <div id="contain-data">
                                <div class='screen-detail' id="screen-level" style="display :none">
                                    <div class="header">
                                        <label>Pronunciation English - level</label>
                                    </div>
                                    <div class="body-screen">
                                        <div class='container-selection'>
                                            <a class="btn selection">
                                                <label class="name">Level 1</label>
                                                <div class="circle"></div>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                                <div class='screen-detail' id="screen-objective" style="display:none">
                                    <div class="header">
                                        <img src="/images/treeview/unpublished_button.gif"
                                             style="float:left;width:20px;height:20px;">
                                        <label style="float:right">Level 1</label>
                                    </div>
                                    <div class="big-circle center-block"></div>
                                    <div class="body-screen">
                                        <div class='container-selection'>
                                            <a class="btn selection">
                                                <label class="name">My Objectives</label>

                                                <div class="circle"></div>
                                            </a>
                                        </div>
                                    </div>
                                    <div class="footer-screen">
                                        <div class='container-selection'>
                                            <a class="btn selection">
                                                <label class="name">test</label>
                                                <div class="circle"></div>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                                <div class='screen-detail' id="screen-lesson" style="display:block">
                                    <div class="header">
                                        <img src="/images/treeview/unpublished_button.gif"
                                             style="float:left;width:20px;height:20px;">
                                        <div style="float:right"><marquee>Level 1 - My Objective</marquee></div>
                                    </div>
                                    <div class="body-screen">
                                        <div class='container-selection'>
                                            <a class="btn selection">
                                                <label class="name">Word ending in /t/</label>
                                                <div class="circle"></div>
                                            </a>
                                        </div>
                                    </div>
                                    <div class="footer-screen">
                                        <div class='container-selection'>
                                            <a class="btn selection selection-popup-obj">
                                                <label class="name">objectives</label>
                                                <div class="circle circle-question">?</div>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-2">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-12">
                            <label>Select the relevant buttons to navigate through and preview the course
                                content.</label>
                            <label>Use the 'back' <img src='/images/treeview/unpublished_button.gif'
                                                       style="width:24px;height: 24px"> button to return to the
                                previous screen when applicable.
                                In the lessons and test you will only be able to view the page and see randomised words
                                for each question , you will not be able to record or view tips.
                                .</label>
                        </div>
                    </div>
                </form>
            </div>
            <!-- End of Modal body -->
        </div>
        <!-- End of Modal content -->
    </div>
    <!-- End of Modal dialog -->
</div>

<script src="<%=request.getContextPath() %>/js/merchant/preview/ui.js"></script>
