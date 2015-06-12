var envTimer;

function getCouse(){
    $.ajax({
        url:"Dashboard",
        type:"POST",
        dataType:"json",
        data:{
            list:"list"
        },
        success:function(result){
            $("#feedback").text(result.x);
            $("#user").text(result.y);
            $("#pronunciation").text(result.z);
            $("#license").text(result.t);
        }
    });
}

function loadEnvironments(){

        var template = Handlebars.compile($("#environment-entry-template").html());
        var $container = $("#aws-environments");
        $.ajax({
            url: CONTEXT_PATH + "awsservice",
            type: "GET",
            dataType: "json",
            data: {
                target: "environment",
                action: "list"
            },
            success: function (data) {
                if (typeof data != 'undefined' && data != null && data.status) {
                    var items = data.data;
                    if (typeof items != 'undefined' && items.length > 0) {
                        $container.empty();
                        for (var i = 0; i < items.length; i++) {
                            var item = items[i];
                            item.health = item.health.toLowerCase();
                            if (item.status.indexOf("Ready") != -1) {
                                item.statusColor = "info";
                                item.statusAction = "";
                            } else if (item.status.indexOf("Terminated") != -1) {
                                item.statusColor = "danger";
                                item.statusAction = "disabled=disabled";
                            } else {
                                item.statusColor = "warning";
                                item.statusAction = "disabled=disabled";
                            }
                            $container.append(template(item));
                        }
                        updateEnvButtonEvent();
                    }
                } else {

                }
                updateEnvTimer();
            },
            error: function (e) {
                updateEnvTimer();
            }
        });

}

function updateEnvButtonEvent() {
    $(".btn-restart").on("click", function(){
        var self = this;
        swal({
            title: "Are you sure?",
            text: "",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#f0ad4e",
            confirmButtonText: "Yes, restart!",
            cancelButtonText: "Cancel",
            closeOnConfirm: true,
            closeOnCancel: true
        },
            function(isConfirm){
                if (isConfirm) {
                    var envName = $(self).attr("env-name");
                    $.ajax({
                        url: CONTEXT_PATH + "awsservice",
                        type:"GET",
                        dataType:"json",
                        data:{
                            target: "environment",
                            action: "restart",
                            data: envName
                        },
                        success:function(data){
                            swal("Request successfully!", "Please wait while system is updating.", "success");
                            updateEnvTimer(true);
                        },
                        error: function(e) {
                            updateEnvTimer(true);
                        }
                    });

                } else {

                } });


    });

    $(".btn-rebuild").on("click", function(){
        var self = this;
        swal({
                title: "Are you sure?",
                text: "",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#d9534f",
                confirmButtonText: "Yes, rebuild!",
                cancelButtonText: "Cancel",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function(isConfirm){
                if (isConfirm) {
                    var envName = $(self).attr("env-name");
                    $.ajax({
                        url: CONTEXT_PATH + "awsservice",
                        type:"GET",
                        dataType:"json",
                        data:{
                            target: "environment",
                            action: "rebuild",
                            data: envName
                        },
                        success:function(data){
                            swal("Request successfully!", "Please wait while system is updating.", "success");
                            updateEnvTimer(true);
                        },
                        error: function(e) {
                            updateEnvTimer(true);
                        }
                    });
                } else {

                } });

    });
}

function updateEnvTimer(doNow) {
    if (typeof envTimer != 'undefined' && envTimer != null) {
        clearTimeout(envTimer);
    }
    if (typeof doNow != 'undefined' && doNow) {
        loadEnvironments();
    } else {
        setTimeout(loadEnvironments, 5000);
    }
}



$(document).ready(function(){
    var role=$("#roles").val();
    getCouse();
    if(role=="1"){
        $("#title-server").show();
        loadEnvironments();
    }
});