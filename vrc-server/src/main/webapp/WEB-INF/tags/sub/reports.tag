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



<script src="<%=request.getContextPath() %>/js/merchant/reports.js"></script>




