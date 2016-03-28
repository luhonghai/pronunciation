<%@ tag import="com.cmg.merchant.util.SessionUtil" %>
<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<style>
    .title{
        font-weight : 600;
        font-family : "Helvetica Neue",Helvetica,Arial,sans-serif;
        font-size : 16px;
        text-align: center;
    }
    #page-wrapper p{
        font-size: 14px;
        font-family : "Helvetica Neue",Helvetica,Arial,sans-serif;
    }
    #helpReportModal p{
        font-size: 14px;
        font-family : "Helvetica Neue",Helvetica,Arial,sans-serif;
    }
    #helpMyClassModal p{
        font-size: 14px;
        font-family : "Helvetica Neue",Helvetica,Arial,sans-serif;
    }

</style>
<%String company= StringUtil.isNull(session.getAttribute(SessionUtil.ATT_CPNAME), "").toString();%>
<div id="page-wrapper">
     <div class="row" style="color:lightgrey; margin-left: 0px;">
         <h4 style="float: left;"><%=company%></h4><p style="margin-top: 10px;">>reports</p>
     </div>
    <div>
        <p>Select a report type from the side menu to view scores for a student and compare with other students in their classes.</p>
        <p>Lessons-view scores for phonemes and words in all lessons that the student has completed in the last 3 months.</p>
        <p>Phonemes-view the scores for a specific phoneme over a selected time period.</p>
    </div>
</div>
<!-- /#wrapper -->

<div id="helpReportModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <div class="row">
                    <div class="col-xs-12 col-md-10 col-md-offset-1">

                        <h1 align="center" class="title">report</h1>
                        <p>Select a report type from the side menu to view scores for a student and compare with other students in their classes.</p>
                        <p><strong>lessons</strong></p>
                        <p>Select a student from the drop down list to view charts with scores for phonemes or words in all lessons that they have completed in the last 3 months.</p>
                        <p>Detail for the most recent lesson will be displayed at the top of the page.</p>
                        <img src=""><p>Select to expand the chart to fill page view.</p>
                        <p>Only the phonemes or words used in the lesson will be displayed on the chart. If you wish to check overall scores for specific phonemes for a student, you will need to select the 'phonemes' report type from the side menu.</p>
                        <p>Class averages are only displayed on the chart when the student has a score to compare. If the student has a zero score, it is assumed that they have not attempted to pronounce the phoneme in the lesson.</p>
                        <p><strong style="color: red;">phonemes</strong></p>
                        <p style="color: red;">1.Select a student.</p>
                        <p style="color: red;">2.Select a phoneme.</p>
                        <p style="color: red;">3.Select a time period.</p>
                    </div>
                </div>
            </div>


        </div>
    </div>
</div>


<script src="<%=request.getContextPath() %>/js/reports.js"></script>




