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

  .aciTree {
    width: 100% !important;
    height: 100% !important;
  }
</style>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/bower_components/aciTree/css/aciTree.css" media="all">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/bower_components/aciTree/css/demo.css" media="all">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/bower_components/aciTree/css/jquery.contextMenu.css" media="all">
<div id="page-wrapper">
  <div class="row">
    <div class="col-lg-12">
      <h4 class="page-header header-company"><%=company%> > all courses > course sample</h4>
    </div>
    <!-- /.col-lg-12 -->
  </div>

  <!-- /.row -->
  <div class="row">
    <div class="col-lg-12" style="padding-top: 20px">
        <div id="tree4" class="aciTree"><div>
    </div>
  </div>


  <!-- /#page-wrapper -->
</div>
<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.aciPlugin.min.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.aciSortable.min.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.aciTree.dom.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.aciTree.core.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.aciTree.utils.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.aciTree.selectable.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.aciTree.sortable.js"></script>
    <script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.contextMenu.js"></script>
<!-- /#wrapper -->
    <script type="text/javascript">

      $(function() {
        // listen for the events
        $('#tree4').on('acitree', function(event, api, item, eventName, options) {
          switch (eventName) {
            case 'checkdrop':
              if (options.isContainer) {
                // mark the drop target as invalid
                return false;
              } else {
                if (options.before === null) {
                  // container creation is disabled
                  return false;
                }
                // check to have the same parent
                var target = api.itemFrom(options.hover);
                if (!api.sameParent(item, target)) {
                  // mark the drop target as invalid
                  return false;
                }

                var itemData = api.itemData(item);
                var label = itemData['label'];
                if(label == 'Test'){
                  return false;
                }
              }
              break;
          }
        });

       $('#tree4').contextMenu({
          selector: '.aciTreeLine',
          build: function(element) {
            var api = $('#tree4').aciTree('api');
            var item = api.itemFrom(element);
            var menu = {
            };
            menu['copy'] = {
              name: 'Copy',
              callback: function() {
                alert("copy");
              }
            };
            menu['paste'] = {
              name: 'Paste',
              callback: function() {
                alert("paste");
              }
            };
            return {
              autoHide: true,
              items: menu
            };
          }
        });


       /* $('#tree4').on('acitree', function(event, api, item, eventName, options) {
          if (eventName == 'selected'){
            // do something when a item is selected
            var itemData = api.itemData(item);
            alert('You just selected the item with the ID: ' + api.getId(item) + '\n' +
                    'also the custom property [label] equals: ' + itemData['label']);
          }
        });*/


        var context = "<%=request.getContextPath() %>";
        // init the tree
        $('#tree4').aciTree({
          ajax: {
            url: context + '/js/merchant/course.json'
          },
          sortable: true
        });

        $('#tree4').removeClass("aciTree0");
        $('#tree4').addClass("aciTreeArrow");
        $('#tree4').addClass("aciTreeNoBranches");
        $('#tree4').addClass("aciTreeBig");


      });
    </script>
