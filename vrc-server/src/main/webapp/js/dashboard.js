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
            url: CONTEXT_PATH + "/awsservice",
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
                        url: CONTEXT_PATH + "/awsservice",
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
                        url: CONTEXT_PATH + "/awsservice",
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



function drawMap(){

    $.ajax({
        "url": "Pronunciationss",
        "type": "POST",
        "dataType":"json",
        "data":{
            draws:"draws"
        },
        success:function(data){
            if(data.status==true) {
                if(data.sc!=null){
                    drawChart(data.sc);
                }
                google.setOnLoadCallback(drawChart);

            }
            if(data.mess=="error")
            {
                $("#dashboard").append("<b style='font-size: 25px; color: red;'>Error.</b>");
            }

        },
        error:function(e){
            swal("Error!", "Could not connect to server", "error");
        }

    });
}

google.load('visualization', '1', {packages: ['controls', 'charteditor']});
function drawChart(sc) {
    var data = new google.visualization.DataTable();
    data.addColumn('datetime', 'date');
    data.addColumn('number', 'Score');
    if(sc!=null && sc.length>0) {
        for (j = 0; j < sc.length; j++) {
            data.addRow([new Date(sc[j][0]), sc[j][1]]);
        }
    }



    var dash = new google.visualization.Dashboard(document.getElementById('dashboard'));

    var control = new google.visualization.ControlWrapper({
        controlType: 'ChartRangeFilter',
        containerId: 'control_div',
        options: {
            filterColumnIndex: 0,
            ui: {
                chartOptions: {
                    height: 50,
                    width: 450,
                    chartArea: {
                        width: '80%'
                    }
                },
                chartView: {
                    columns: [0, 1]
                }
            }
        }
    });

    var chart = new google.visualization.ChartWrapper({
        chartType: 'LineChart',
        containerId: 'drawchart'
    });

    function setOptions (wrapper) {
        // sets the options on the chart wrapper so that it draws correctly
        wrapper.setOption('height', 200);
        wrapper.setOption('width', 450);
        wrapper.setOption('chartArea.width', '80%');
        // the chart editor automatically enables animations, which doesn't look right with the ChartRangeFilter
        wrapper.setOption('animation.duration', 0);

    }

    setOptions(chart);

    dash.bind([control], [chart]);
    dash.draw(data);
}

$(document).ready(function(){
    var role=$("#roles").val();
    getCouse();
    drawMap();
    if(role=="1"){
        $("#title-server").show();
        loadEnvironments();
    }
});