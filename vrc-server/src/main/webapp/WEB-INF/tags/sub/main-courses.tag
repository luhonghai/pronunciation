<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@ tag import="com.cmg.merchant.common.Constant" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%
  String company = (String) StringUtil.isNull(request.getSession().getAttribute("companyName"),"CMG");
%>
<style>
  .welcome{
    color : #376092;
    font-weight: 200;
  }
  .header-company{
    color: #A6A6A6
  }

  .dropdown.dropdown-lg .dropdown-menu {
    margin-top: -1px;
    padding: 6px 20px;
  }
  .input-group-btn .btn-group {
    display: flex !important;
  }
  .btn-group .btn {
    border-radius: 0;
    margin-left: -1px;
  }
  .btn-group .btn:last-child {
    border-top-right-radius: 4px;
    border-bottom-right-radius: 4px;
  }
  .btn-group .form-horizontal .btn[type="submit"] {
    border-top-left-radius: 4px;
    border-bottom-left-radius: 4px;
  }
  .form-horizontal .form-group {
    margin-left: 0;
    margin-right: 0;
  }
  .form-group .form-control:last-child {
    border-top-left-radius: 4px;
    border-bottom-left-radius: 4px;
  }

  .cbstyle{
    box-shadow: inset 0 1px 1px rgba(255, 250, 250, 0.075) !important;
    width: 10%;
  }
  .lbl_addForm{
    font-weight: 200;
  }

  .container-course{
    margin-bottom: 5px;
  }

  .lbl_cname{
    font-weight: 200;
  }
  .lbl_cinfor{
    font-weight: 200;
    font-size: 10px;
    padding-left: 15px;
  }

  ul.typeahead{
    width : 100%;
  }
  @media screen and (min-width: 768px) {
    #adv-search {
      width: 500px;
      margin: 0 auto;
    }
    .dropdown.dropdown-lg {
      position: static !important;
    }
    .dropdown.dropdown-lg .dropdown-menu {
      min-width: 500px;
    }
  }

</style>
<div id="page-wrapper">
  <div class="row">
    <div class="col-lg-12">
      <h4 class="page-header header-company"><%=company%> > all courses</h4>
    </div>
    <!-- /.col-lg-12 -->
  </div>

  <!-- /.row header-->
  <div class="row">
    <div class="col-lg-4 pull-left">
      <a href="#add" data-toggle="modal" class="btn btn-info" style="background-color: #E46C0A;border-color: #E46C0A">
        add course<span class="glyphicon glyphicon-plus" style="padding-left: 5px"></span>
      </a>
    </div>
    <div class="col-lg-8 pull-right">
      <div class="input-group" id="adv-search" style="float:right">
        <input id="suggestCourse" type="text" class="form-control suggestCourse" data-provide="typeahead" placeholder="Search course" />
        <div class="input-group-btn">
          <div class="btn-group" role="group">
            <div class="dropdown dropdown-lg">
              <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><span class="caret"></span></button>
              <div class="dropdown-menu dropdown-menu-right" role="menu">
                <form class="form-horizontal" role="form">
                  <div class="form-group">
                    <label for="contain">Company</label>
                    <input id="suggestCompany" placeholder="Enter company name" class="form-control suggestCompany" type="text" />
                  </div>
                  <div class="form-group">
                    <label for="contain">Course title</label>
                    <input placeholder="Enter course title" class="form-control suggestCourse" type="text" />
                  </div>
                  <div class="form-group">
                    <label>created date range</label>
                  </div>
                  <div class="form-group">
                      <div class="col-sm-6" style="padding-left: 0px;">
                        <label for="fromdate" class="sr-only"></label>
                        <input id="fromdate" class="form-control input-group-lg reg_name" type="text"
                               name="fromdate" title="From Date" placeholder="Date From">
                      </div>
                      <div class="col-sm-6" style="padding-right: 0px;>
                        <label for="todate" class="sr-only"></label>
                        <input id="todate" class="form-control input-group-lg reg_name" type="text"
                               name="todate" title="To Date" placeholder="Date To">
                      </div>

                  </div>
                  <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></button>
                </form>
              </div>
            </div>
            <button type="button" class="btn btn-primary"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></button>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- /.row -->
  <div class="row">
    <div class="col-lg-12" style="padding-top: 20px">
      <label class="welcome">Double click to select from the list below to view the content of courses that have been shared, or select the  ‘add course’ button above to create a bespoke course of your own.</label>
    </div>
  </div>

<div class="row">
  <div id="content-courses" class="col-lg-12" style="padding-top:20px;padding-left: 0px">
    <div class="col-lg-12 pull-left" class="container-course">
      <a class="btn btn-info" style="background-color: #558ED5;border-color: #E46C0A">
        <label class="lbl_cname">Aa Course for Vietnamese Students</label><label class="lbl_cinfor">created by CMG 03/12/2015</label>
      </a>
    </div>
    <div class="col-lg-12 pull-left" class="container-course">
      <a class="btn btn-info" style="background-color: #558ED5;border-color: #E46C0A">
        <label class="lbl_cname">Aa Course for Singapore Students</label><label class="lbl_cinfor">created by CMG 03/12/2015></label>
      </a>
    </div>
    <div class="col-lg-12 pull-left" class="container-course">
      <a class="btn btn-info" style="background-color: #17375E;border-color: #E46C0A">
        <label class="lbl_cname">Aa Course for Thai Students</label> <label class="lbl_cinfor">created by  BiZZ 06/11/2015</label>
      </a>
    </div>
  </div>
</div>
  <!-- /#page-wrapper -->


<div id="add" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h4 class="modal-title" style="text-align: center;font-weight: 200">Add Course</h4>
      </div>
      <div class="modal-body">
        <form class="form-horizontal" id="addCourseForm" name="addform">
          <div class="form-group">
            <label class="control-label col-md-4 lbl_addForm" for="name">Name:</label>
            <div class="col-md-6">
              <input type="text" class="form-control" id="name" name="name" placeholder="Course name">
            </div>
          </div>
          <div class="form-group">
            <label class="control-label col-md-4 lbl_addForm" for="description">Description:</label>
            <div class="col-md-6">
              <textarea rows="3" class="form-control" id="description" name="description" placeholder="Description"></textarea>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label col-md-4 lbl_addForm" for="shareall">Share all:</label>
            <div class="col-md-6">
              <input type="checkbox" class="form-control cbstyle"
                     id="shareall" checked name="share" value="<%=Constant.SHARE_ALL%>">
            </div>
          </div>
          <div class="form-group">
            <label class="control-label col-md-4 lbl_addForm" for="mycompany">Share in my company:</label>
            <div class="col-md-6">
              <input type="checkbox" style="padding-top: 10px" class="form-control cbstyle"
                     id="mycompany" name="share" value="<%=Constant.SHARE_IN_COMPANY%>">
            </div>
          </div>
          <div class="form-group">
            <label class="control-label col-md-4 lbl_addForm" for="private">Private:</label>
            <div class="col-md-6">
              <input type="checkbox" style="padding-top: 10px" class="form-control cbstyle"
                     id="private" name="share" values="<%=Constant.SHARE_PRIVATE%>">
            </div>
          </div>
          <div class="form-group">
            <div class="col-md-6">
              <img style="float:left;cursor: pointer" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAFhElEQVRIS32WW2wUVRzGv3POzM7eeqellaittRdusS2ReMMWUjCEthTkATSRbSQmyJPGJwmB+iJBE4g+mrCNAlZIqBINWkxaEIOEVApSpFIpWHrvdre7vezu7Mwx58zsttjgZGdndvfs9/v+3//sOUvwP0d549eFpmn6OCfVhNAKQpAJQgCQEAjpJgQXYSotd77ddv9xMmL0okMIGwnD7/W4asqKC5CV4YXL5URcN+SpGyamIlFMReYwPhmGric6uYKmO62LQYsAJfUnfV635l9bUYy83AwMjscxOqkjEjUxG+PSjKYSqAzQFEClwFRkGpNTQQBmU09rQ8tCx48ASrac8GdneX2bqldjMmzg5r05zMU5kBpF5u9FVASgBHCJJ9NEMDyKREJvud26pSkJSX1VOM/O8PiFeN9gHHcHoyJr+ah7MRt76pYiP0sB51yevQNRHP9pApd7ZiSfEYBzEzORMZgJvann1GZZiQSIzF2a2l9fW4m7C8TT3AxH9haiqsQN0xTCwqgFSJ6HT4+h/XokKSUhs6FhKCxR1N2y+b4ElNSd6Njw8soaRfPg6p8zKedet4K2j0rhcVIp+Ki4cMwRmTWw/eOBBTECemwWsUiw89ZXteuJcO/WHP11G6vQcT28IHMrnsoSDz7b9xT87QG0XJiEqOrTPQUozldlRQKy74tR9I/q8xAOTE+OgJpmESltOHlozaqig+60bNy4F7UH2c0kBIQSFOSoGAkZIHZjXyh3o3lXbgqw69gIxsMGmGg2AArAiM0gPhdqJiX1pzo2rltd82ACGA0aFkAKWVOEEoJ4giNuyokCTgmO7M7DuhVuCRgJJfDm52P2788CMM6hGAnMzEx0ktKG1mDDxqrMa3/NYUbOc3vuEYKYbs19TgiYQsEYwZbn07B/R47dZKD5bAhX+uK2p2QFHF4A44HhECnd2spfq34OV+7EUs2N6hzTc4YlzKglrlAp/uHr2dI5wHHhVgzH2qdljFbRFkD0JdcBPHg4DFLW+A2veWkVuvp0CQhOJxCN85RoUnxZrorj+/LhdRIJ+OOhjv1nI6CMggqADbEAQIGbo/fukACcDr6ydnnm7QEDD8d1GCZAbceKuKqW+7drM7G7Ji3l8J0vwwjMApSReYisgMvlY6mH48bNwRAp23amY1XZ0zW9g8BEBGCKnbeEMAlQVIamDel461WRLDAd49jdEoE0oFBpSFZhR+R1cLh5HDd7xjpJ+fYzh3Kysg9GTQ+GQvN5y2hUBsUGVBU7UVGkSbeBWY5f7hmpzyWEURkTOPBEOsfkWBh9fwebSXljW6Gi0f6szHzcD1BwakGEsLw6BIQhw6tgb60XSzMZLvUZuPzAhOoQFVomxFgBcDDg2SUcHReHMD0XLbLWoh1tHWnenJqY4UIwbgsnK3BYEb27KR1b17hSK/H738UR0qmEJE2IfhTnAmNDEXT9HujsOrpivQXY2VbIoPZrWh6mdQadLIjHruCD+nTUrnRKgMj6wI9xDM1SqJoCVWMStCyHIdtp4tz3/4RM06j87fBya7GzIOd8jDj8hC5BjDBANNh2LxxWFjlxoDEN6W6GrgETRy/FbXEFDifDk3kK8jMo2n8exmQg2nTtk9L55ToJWbHzBz+hDp9Bc2AqCqhosF2BuMo4pFvbtabA42F4Jl9Bmoug89dxTIUSLVcPFy/ecJKQlW+c94GqfoNlgDtccKi2sIOCinuNwe1S4PUw5KQzLMlUMB6Ioqc3HNJ1vJd0vmhHW7iPVvjOFyYMxQ+m1hBHGpyaEx6nBreHwe1mcLkZODERjScwPDqHaAydqkaaROYLdVI72n/fTL6u8HUUmhQ+Tlg1pbQClGUSSkEIDYGybkLJRZOipftY5WP/tvwLMRJAhxNK6h8AAAAASUVORK5CYII=">
            </div>
            <div class="col-md-6">
              <img style="float:right;cursor:pointer" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAEUUlEQVRIS7XWe2wURRzA8e+99u4orRRqBQttgQo20ICk2OvJQ5CChSpWRCRBjEqMghIM8iYUIUAMRDFIUESqJhgJUhBBFIEij96V1x8oigkKpaWvu2t7d33c7d7tmF0IssY/LjFMMsluMvv7zG92dn5r4h430z2Ozx1g67eLulKcvRyYTLQFQrjyJpCakg6IhObQ4K/huv9XBIJwZyAyb+pGp/bgHeCLHzeIcSNLcNqSaahvJLNPDnZJH5NQCwSbsHczE413UnnuIC8WLdZj3wE+/2G9eNI9nfZoC02Nfh7KGIbD3g0hEsugua0Okz1CiiOdI549zJr4L6D88DoxqXAabV2NNDf5GdR3BA4pCVXEE8qgubUW7F30TMrgiKeC2ZOWGDPYeWitKHKX4u+4ga+xhdzMR5GsDuJqLDGgrRbh6CC9+wCOevbxUvEyI7Dj4BoxofBpmsJ/4m9qJTezAJvVjpooEKxDlcL0SRnEMe8BXp68wgh8+t27YnzhFOqDV3RgSJYbi8lKXCSWga+tFtXRTt8eQzjuPcSrU1Yage0HysQ4VzE3Wn8h0BxkSKYbs9mCEosmtET+0E2Eo53snsOp9H7PnKfKjMDH+1eJxwsnci1wkZbmIEOzRqMKFSUWSQgIhBvA2cHAtHxOeI7w2tTVRmDbvpVijOsJrvrO0uILkZc1mvu63Z9QcG3Qb7VV4OxkUHohP3uP8voza4zA1ooVYoxrHFeaTtPqCyNZnZhN5oSBWFwmuZeD3AfGcMpbyRvPrjUCH32zTIxyjeVyQ2XCQf9r4NAHx3Pae5J509YZgS17lgp3wWNcqv8JyaIdSYnPXoNUEUOJywzLmERV9WnefG6DEfhw92Lhdrm5WHsQh607jXUtpCcNNExSOzbu7k2tNURjnTidDjL6pxFR2hnRrwTPWQ/zp79nBDZ//Y5wFRRw7sZ+HLZkbl5vpmzmXj2gqgri8RixWAxF0bqCrCjsOr4OX+dVrBYbA3IziChhRmaV4q2uZsGMjUbg/a8WigJXPt5re3SgvsZH2Yz9LNg6AavNzKoX9upBFVlGlhVkWaaieiMt8jWsFonswb11oLD/81RXn+ftmZuMwPryufoSXQ1UEYr4uXndx/LSvSzdWYxVMrNwyi5kRQsuE41qPcrhS1toi9XoGfR/uDedcpDRObM4673AotmbjcCibZNP9JIGjx3+SB4NoT/wXKhkSeluln9Wgs1uYX5xuR48Eo0SjUSIRCIc+307oXgdNqtEv5w0OqJBivJewXvmPKvnfWIEtLf51gdFp1JNOaPy8/NpqK+nZNQc4vG4vvbx2K13ELt9r11f/usMsujAYrGQPbAfHZGgvilOnqhi07IvDYAdSJUkKXXq3OHlpvbUApMwk9ojDaGqt3eOqhdPof6zk8LtIQQqJrOZ5O5JeoaYTXQpQaVix0mt3oY0RauLSYB0d4X7X1/brYe1StV1z/8q/gbObAFIajwRCAAAAABJRU5ErkJggg==">

            </div>
          </div>
        </form>
      </div><!-- End of Modal body -->
    </div><!-- End of Modal content -->
  </div><!-- End of Modal dialog -->
</div>


</div>
<script src="<%=request.getContextPath() %>/js/merchant/typeahead.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/suggestion.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/maincourses/action.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/maincourses/ui.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/maincourses/data.js"></script>
<!-- /#wrapper -->



