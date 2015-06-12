
<script id="environment-entry-template" type="text/x-handlebars-template">
    <div class="col-md-3 col-sm-6 aws-env-item">
        <div class="panel panel-{{health}}">
            <div class="panel-heading">
                <div class="row">
                    <div class="text-left aws-detail">
                        <h3>{{environmentName}}</h3>
                        <hr/>
                        <p><b>Environment tier:</b> <span>{{tier.name}}</span></p>
                        <p><b>Running versions:</b> <span>{{versionLabel}}</span></p>
                        <p><b>Last modified:</b> <span>{{dateUpdated}}</span></p>
                    </div>
                </div>
            </div>
            <div class="panel-footer">
                <button title="Environment status" class="pull-left btn-status btn btn-{{statusColor}} btn-sm" disabled="disabled">{{status}}</button>
                <span class="pull-right">
                    <button title="Restart Tomcat application server" {{statusAction}} env-id="{{environmentId}}" env-name="{{environmentName}}" type="button" class="btn btn-warning btn-sm btn-restart">
                        <span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>
                        Restart</button>
                    <button title="Rebuild server instance" {{statusAction}} type="button" env-id="{{environmentId}}" env-name="{{environmentName}}" class="btn btn-danger btn-sm btn-rebuild">
                        <span class="glyphicon glyphicon-retweet" aria-hidden="true"></span>
                        Rebuild</button>
                </span>
                <div class="clearfix"></div>
            </div>
        </div>
    </div>
</script>
<%
    String role=null;
    if (session.getAttribute("role")==null){
        return;
    }
    if(session.getAttribute("role").equals(1)){
       role="1";
    }
    if(session.getAttribute("role").equals(2)){
      role="2";
    }
%>
<input type="hidden" id="roles" value="<%=role%>">

<div id="aws-environments" class="row" style="margin-top: 30px;">

</div>
