var avg;
function listStudents(){
        $.ajax({
            url: "ReportsLessons",
            type: "POST",
            dataType: "json",
            data: {
                action: "listStudent"
            },
            success: function (data) {
                if(data.message=="success"){
                    $("#listUsers").empty();
                    if(data.listStudent!=null && data.listStudent.length>0){
                        var items=data.listStudent;
                        $(items).each(function(){
                            $("#listUsers").append('<option value="' + this.studentName + '">' + this.studentName + '</option>');
                        });
                    }
                    $('#listUsers').multiselect('destroy');
                    $('#listUsers').multiselect({ enableFiltering: true, buttonWidth: '200px'});
                    $('#listUsers').multiselect('refresh');
                }else{
                    swal("Error!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
}
function loadInfo(){
    $(document).on("click","#loadInfo",function(){
        var studentName=$('#listUsers option:selected').val();
        var phoneme=$("#listPhonemes").val();
        var dateFrom=$("#dateFrom").val();
        var dateTo=$("#dateTo").val();
        $.ajax({
            url: "ReportsLessons",
            type: "POST",
            dataType: "json",
            data: {
                action: "loadInfo",
                studentName:studentName,
                phoneme:phoneme,
                dateFrom:dateFrom,
                dateTo:dateTo
            },
            success: function (data) {
                if(data.message=="success"){
                    drawChart(data.sc);
                    google.setOnLoadCallback(drawChart);
                    $("#avg").text(avg);
                }else{
                    swal("Error!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    })
}
function dateFrom(){
    $('#dateFrom').datetimepicker({
        format: 'DD/MM/YYYY'
    });
}
function dateTo(){
    $('#dateTo').datetimepicker({
        format: 'DD/MM/YYYY'
    });
}

google.load('visualization', '1', {packages: ['controls', 'charteditor']});
function drawChart(sc) {
    var sum=0;
    var data = new google.visualization.DataTable();
    data.addColumn('datetime', 'date');
    data.addColumn('number', 'Score');
    if(sc!=null && sc.length>0) {
        for (j = 0; j < sc.length; j++) {
            data.addRow([new Date(sc[j][0]), sc[j][1]]);
            sum += sc[j][1];
        }
        avg=sum/(sc.length);
    }else{
        avg=0;
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
                    width: $("#width").width(),
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
        wrapper.setOption('height', 400);
        wrapper.setOption('width', $("#width").width());
        wrapper.setOption('chartArea.width', '80%');
        // the chart editor automatically enables animations, which doesn't look right with the ChartRangeFilter
        wrapper.setOption('animation.duration', 0);

    }

    setOptions(chart);

    dash.bind([control], [chart]);
    dash.draw(data);
}

$(document).ready(function(){
    $('#help-icons').show();
    dateFrom();
    dateTo();
    loadInfo();
    listStudents();
});

