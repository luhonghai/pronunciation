<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/preview.css" media="all">
<div id="preview-popup" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" style="width:400px">
        <div class="modal-content">
            <div class="modal-header" style="border-bottom: transparent;padding-bottom: 10px;">
                <button id="close-popup-preview" type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                <h2 class="modal-title" style="font-weight: 700;font-size: 18px"></h2>
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
                                        <marquee>Pronunciation English - level</marquee>
                                    </div>
                                    <div style="max-height: 350px" class="body-screen">
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
                                        <img back="screen-level" class="slide-back" src="/images/popup/preview_back.gif"
                                             style="float:left;width:20px;height:20px;">
                                        <div class="title" style="text-align: right;float:right;width:150px">
                                            <label>Level 1</label>
                                        </div>
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
                                <div class='screen-detail' id="screen-lesson" style="display:none">
                                    <div class="header">
                                        <img back="screen-objective" class="slide-back" src="/images/popup/preview_back.gif"
                                             style="float:left;width:20px;height:20px;">
                                        <div class="title" style="float:right;width:150px">
                                            <marquee>Level 1 - My Objective</marquee>
                                        </div>
                                    </div>
                                    <div style="max-height: 300px" class="body-screen">
                                        <div class='container-selection'>
                                            <a class="btn selection">
                                                <label class="name">Word ending in /t/</label>
                                                <div class="circle"></div>
                                            </a>
                                        </div>
                                    </div>
                                    <div style="display: none" class="body-popup-obj">
                                        <div id="close-popup-obj" style="text-align: right;padding:2px 5px 0px 0px;cursor:pointer" class='container-selection'>
                                            <label>x</label>
                                        </div>
                                        <div style="padding: 0px 10px;" class='container-selection'>
                                            <label class="obj-description">x</label>
                                        </div>
                                    </div>
                                    <div class="footer-screen">
                                        <div class='container-selection'>
                                            <a class="btn selection selection-popup-obj">
                                                <label class="name">objectives</label>
                                                <div id="circle-popup-obj" class="circle circle-question">?</div>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                                <div class='screen-detail' id="screen-question" style="display:none">
                                    <div style="padding:0px 5px 0px 5px" class="header">
                                        <img back="screen-lesson" class="slide-back" src="/images/popup/preview_back.gif"
                                             style="float:left;width:20px;height:20px;">
                                    </div>
                                    <div class="body-screen">
                                        <div class="col-sm-6 contain-head-circle">
                                            <div class="circle circle-left"></div>
                                        </div>
                                        <div class="col-sm-6 contain-head-circle">
                                            <div class="circle circle-right"></div>
                                        </div>
                                        <div class="col-sm-12">
                                            <div class="big-circle center-block"></div>
                                        </div>
                                        <div class="col-sm-12 contain-word">
                                            <label></label>
                                        </div>
                                        <div class="col-sm-12 description-question">
                                            <label></label>
                                        </div>
                                        <div class="col-sm-12 contain-circle-right-label">
                                            <div class="circle circle-right-label"></div>
                                        </div>
                                    </div>
                                    <div class="footer-screen">
                                        <div class="contain-question">
                                            <div class="circle circle-question">Q1</div>
                                            <div class="circle circle-question">Q2</div>
                                            <div class="circle circle-question">Q3</div>
                                            <div class="circle circle-question">Q4</div>
                                            <div class="circle circle-question">Q5</div>
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
                            <label>Use the 'back' <img src='/images/popup/preview_back.gif'
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


<script src="<%=request.getContextPath() %>/js/merchant/preview/ajax.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/preview/ui.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/preview/data.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/preview/action.js"></script>