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
        color: #376092;
        font-size: 14px;
        font-family : "Helvetica Neue",Helvetica,Arial,sans-serif;
    }
    #helpReportModal{
       color: rgb(149, 127, 127);
    }

    #helpReportModal .modal-body{
        width : 500px;
    }
    #helpReportModal .modal-header{
        border-bottom: transparent;
        padding-bottom: 0px;
        text-align: center;
    }
    #helpReportModal .modal-title{
        font-weight : 700;
    }
    #helpReportModal .modal-content{
        border-radius : 20px;
    }

    #helpReportModal p{
        color: rgb(149, 127, 127);
        font-size: 14px;
        font-family : "Helvetica Neue",Helvetica,Arial,sans-serif;
    }
    #helpMyClassModal p{
        font-size: 14px;
        font-family : "Helvetica Neue",Helvetica,Arial,sans-serif;
    }

    .header-company {
        color: #A6A6A6;
        font-weight: 200;
        font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
        font-size: 14px;
        border-bottom-color: transparent;
        margin : 10px 0px;
    }

</style>
<%String company= StringUtil.isNull(session.getAttribute(SessionUtil.ATT_CPNAME), "").toString();%>
<div id="page-wrapper">
     <div class="row">
         <div class="col-sm-12">
             <h4 class="page-header header-company"><%=company%> >
                 reports</h4>
         </div>
     </div>
    <div class="row">
        <div class="col-sm-12">
            <p>Select a report type from the side menu to view scores for a student and compare with other students in their classes.</p>
            <p>Lessons-view scores for phonemes and words in all lessons that the student has completed in the last 3 months.</p>
            <p>Phonemes-view the scores for a specific phoneme over a selected time period.</p>
        </div>
    </div>
</div>
<!-- /#wrapper -->

<div id="helpReportModal" class="modal fade">
    <div class="modal-dialog" style="width:500px">
        <div class="modal-content">
            <div class="modal-header">
                <h2 class="modal-title">report</h2>
            </div>
            <div class="modal-body">
                <div>
                    <p>Select a report type from the side menu to view scores for a student and compare with other students in their classes.</p>
                    <p><strong>lessons</strong></p>
                    <p>Select a student from the drop down list to view charts with scores for phonemes or words in all lessons that they have completed in the last 3 months.</p>
                    <p>Detail for the most recent lesson will be displayed at the top of the page.</p>
                    <p><img src="">Select to expand the chart to fill page view.</p>
                    <p>Only the phonemes or words used in the lesson will be displayed on the chart. If you wish to check overall scores for specific phonemes for a student, you will need to select the 'phonemes' report type from the side menu.</p>
                    <p>Class averages are only displayed on the chart when the student has a score to compare. If the student has a zero score, it is assumed that they have not attempted to pronounce the phoneme in the lesson.</p>
                    <%--<p><strong style="color: red;">phonemes</strong></p>
                    <p style="color: red;">1.Select a student.</p>
                    <p style="color: red;">2.Select a phoneme.</p>
                    <p style="color: red;">3.Select a time period.</p>--%>
                </div>
            </div>
        </div>
    </div>
</div>


<script src="<%=request.getContextPath() %>/js/merchant/reports.js"></script>




