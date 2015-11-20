var myTable;
var avg;
function listScore(){
    myTable=$('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "responsive": true,
        "bProcessing": true,
        "bServerSide": true,
        "order": [[ 3, "desc" ]],
        "ajax": {
            "url": "Pronunciations",
            "type": "POST",
            "dataType":"json",
            "data":{
                list:"list",
                username:$("#username").val(),
                word:$("#word").val(),
                score:$("#score").val(),
                type:$("#type").val(),
                dateFrom:$("#dateFrom").val(),
                dateTo:$("#dateTo").val()


            }
        },

        "columns": [{
            "sWidth": "25%",
            "data": "username",
            "sDefaultContent":""

        },{
            "sWidth": "15%",
            "data": "word",
            "sDefaultContent":""
        }, {
            "sWidth": "10%",
            "data": "score",
            "sDefaultContent":""
        },{
            "sWidth": "20%",
            "data": null,
            "bSortable": true,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                if (data.serverTime != 0) {
                    return new Date(data.serverTime);
                }
            }
        }, {
            "sWidth": "10%",
            "data": "type",
            "sDefaultContent":""
        },{
            "sWidth": "5%",
            "data": null,
            "bSortable": false,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                console.log(data);
                if (data.latitude != 0 && data.longitude != 0) {
                    return '<button type="button" scouse=' + data.score + ' id="maps" latitude=' + data.latitude + ' class="btn btn-info btn-sm" longitude=' + data.longitude + '>' + '<i class="fa fa-map-marker "></i>' + '</button>';
                }
            }
        } ]
    });

}

function dateFrom(){
    $('#dateFrom').datetimepicker({
        format: 'YYYY/MM/DD'
    });
}
function dateTo(){
    $('#dateTo').datetimepicker({
        format: 'YYYY/MM/DD'
    });
}

function filter(){
    $(document).on("click","#buttonFilter",function(){
        myTable.fnSettings().ajax = {
            "url": "Pronunciations",
            "type": "POST",
            "dataType":"json",
            "data": {
                list:"list",
                username:$("#username").val(),
                word:$("#word").val(),
                score:$("#score").val(),
                type:$("#type").val(),
                dateFrom:$("#dateFrom").val(),
                dateTo:$("#dateTo").val()
            }
        };
        $("tbody").html("");
        myTable.fnDraw();
        drawMap();

    });

}

var mapOptions;
var map;
var marker;

function mapss(latitude,longitude) {
    mapOptions = {
        scaleControl: true,
        center: new google.maps.LatLng(latitude, longitude),
        zoom: 15,
        MapTypeId:google.maps.MapTypeId.ROADMAP
    };

    map = new google.maps.Map(document.getElementById('map'), mapOptions);

    marker = new google.maps.Marker({
        map: map,
        content :  map.getCenter().toUrlValue(),
        position: map.getCenter(),
        disableAutoPan: true
    });
    marker.setMap(map);
    //map.setCenter(new google.maps.LatLng(latitude, longitude));
    //$('#mapDetail').modal('show');
}
function refreshMap() {
    var latitude = $('#mapDetail').attr('latitude');
    var longitude = $('#mapDetail').attr('longitude');
    var x=parseFloat(latitude);
    var y=parseFloat(longitude);
    mapss(x,y);
    // google.maps.event.trigger(map, 'resize');

}
function maps() {
    $('#mapDetail').on("show.bs.modal", function() {
        setTimeout(refreshMap, 200);
    });

    $(document).on("click", "#maps", function () {
        var latitude = $(this).attr('latitude');
        var longitude = $(this).attr('longitude');

        $('#mapDetail').attr("latitude", latitude);
        $('#mapDetail').attr("longitude", longitude);
        $('#mapDetail').modal('show');

    });
}
function search(){
    $('#dataTables-example').on( 'search.dt', function () {
        drawMap();
    });
}

function drawMap(){

    $.ajax({
        "url": "Pronunciationss",
        "type": "POST",
        "dataType":"json",
        "data":{
            draw:"draw",
            username:$("#username").val(),
            country:$("#country").val(),
            score:$("#score").val(),
            type:$("#type").val(),
            dateFrom:$("#dateFrom").val(),
            dateTo:$("#dateTo").val(),
            search:$(".table-responsive input[type=search]").val(),
            count:$(".table-responsive .input-sm").val()
        },
        success:function(data){
            if(data.status==true) {
                drawChart(data.sc);
                google.setOnLoadCallback(drawChart);
                $("#avg").text(avg);

            }
            if(data.status==false){
                $("#dashboard").append("<b style='font-size: 25px; color: red;'>Dữ liệu quá lớn</b>");
            }
            if(data.mess=="Error Sever")
            {
                swal("Error!", "Could not connect to server", "error");
            }

        },
        error:function(e){
            swal("Error!", "Could not connect to server", "error");
        }

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

//function detailemei(){
//    $(document).on("click", "#emei", function () {
//        $('#emeimodal').modal('show');
//        var emei=$("#emei").attr('emeis');
//        $.ajax({
//            url:"Pronunciations",
//            type:"POST",
//            dataType:"json",
//            data:{
//                emei:emei,
//                detailmodal:"detailmodal"
//            },
//            success:function(data){
//                if(data!=null) {
//                    $("#emeipopup").text(data.imei);
//                    $("#devicenamepopup").text(data.deviceName);
//                    $("#modelpopup").text(data.model);
//                    $("#osversionpopup").text(data.osVersion);
//                    $("#osapilevelpopup").text(data.osApiLevel);
//                    $("#attacheddatepopup").text(data.attachedDate);
//                }else {
//                    $("#emeipopup").text("");
//                    $("#devicenamepopup").text("");
//                    $("#modelpopup").text("");
//                    $("#osversionpopup").text("");
//                    $("#osapilevelpopup").text("");
//                    $("#attacheddatepopup").text("");
//                }
//            },
//            error:function(){
//                swal("Error!", "Could not connect to server", "error");
//            }
//
//        });
//
//
//
//    });
//}

$(document).ready(function(){

    //detailemei();
    dateFrom();
    dateTo();
    maps();
    filter();
    listScore();
    drawMap();
    search();
});

