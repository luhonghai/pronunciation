<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%String company=session.getAttribute("company").toString();%>
<div id="page-wrapper">
    <div class="row" style="color:lightgrey; margin-left: 0px;">
        <h3 style="float: left;"><%=company%></h3><p style="margin-top: 25px;">>reports>lessons</p>
    </div>
    <div>
        <div class="row" style="padding-left:15px;">
            <p style="margin-top: 10px; float: left;">student:</p>
            <select style="display:none; float: left;margin-left: 3px;" only class="form-control" id="listUsers">
                <option>1</option>
                <option>2</option>
                <option>3</option>
                <option>4</option>
                <option>5</option>
            </select>
        </div>
        <div class="row" style="padding-left:15px;">
            <p style="float:left; margin-top: 10px";>phoneme:</p>
            <select style="margin-top: 5px;padding: 5px;border-radius:3px; " id="listPhonemes">
                <option value="B">b</option>
                <option value="CH">tʃ</option>
                <option value="D">d</option>
                <option value="DH">ð</option>
                <option value="F">f</option>
                <option value="G">g</option>
                <option value="JH">dʒ</option>
                <option value="HH">h</option>
                <option value="K">k</option>
                <option value="L">l</option>
                <option value="M">m</option>
                <option value="N">n</option>
                <option value="NG">ŋ</option>
                <option value="P">p</option>
                <option value="R">r</option>
                <option value="S">s</option>
                <option value="SH">ʃ</option>
                <option value="T">t</option>
                <option value="TH">θ</option>
                <option value="V">v</option>
                <option value="W">w</option>
                <option value="Y">j</option>
                <option value="Z">z</option>
                <option value="ZH">ʒ</option>
                <option value="AA">ɑː</option>
                <option value="AE">a</option>
                <option value="AH">ʌ</option>
                <option value="AO">ɔː</option>
                <option value="AW">aʊ</option>
                <option value="AX">ə</option>
                <option value="AY">ʌɪ</option>
                <option value="EA">ɛː</option>
                <option value="EH">ɛ</option>
                <option value="ER">əː</option>
                <option value="EY">eɪ</option>
                <option value="IA">ɪə</option>
                <option value="IH">ɪ</option>
                <option value="IY">iː</option>
                <option value="OH">ɒ</option>
                <option value="OW">əʊ</option>
                <option value="OY">ɔɪ</option>
                <option value="UA">ʊə</option>
                <option value="UH">ʊ</option>
                <option value="UW">uː</option>


            </select>
        </div>
        <div class="row">
            <div class="col-sm-1">
                <p style="float:left; margin-top: 10px";>from:</p>
            </div>
            <div class="col-sm-3">
                <input type='text' style="float: left;" class="form-control" id="dateFrom" placeholder="From"/>
            </div>
            <div class="col-sm-1">
                <p style="float:left; margin-top: 10px";>to:</p>
            </div>
            <div class="col-sm-3">
                <input type='text' style="float: left;" class="form-control" id="dateTo" placeholder="To"/>
            </div>
            <div class="col-sm-4">
                <label></label>
            </div>
        </div>


        <span type="button" id="loadInfo" title="click here to run the report" style="color:lightgreen;cursor: pointer;margin-left: 5px;" class="fa fa-check-circle fa-2x"> </span>

    </div>
    <div class="row" id="scoreavg" style="margin-bottom: -10px; margin-top: 20px">

        <label class="col-sm-2" style="text-align: right; margin-left: 40px">Score average: </label>
        <label class="col-sm-2" id="avg" style="padding-left: 0px;"></label>
    </div>
    <div class="row" style="margin-top: 5px;">
        <div id="dashboard">
            <div id="drawchart"></div>
            <div id="control_div"></div>
        </div>
    </div>
</div>
<!-- /#wrapper -->

<div id="helpReportModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <div class="row">
                    <div class="col-xs-12 col-md-10 col-md-offset-1">

                    </div>
                </div>
            </div>


        </div>
    </div>
</div>


<script src="<%=request.getContextPath() %>/js/reportsPhonemes.js"></script>




