var myTable;
function listScore(){
    myTable=$('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "responsive": true,
        "bProcessing": true,
        "bServerSide": true,
        "ajax": {
            "url": "Pronunciations",
            "type": "POST",
            "dataType":"json",
            "data":{
                list:"list",
                username:$("#username").val(),
                word:$("#word").val(),
                uuid:$("#uuid").val()
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
        }, {
            "sWidth": "25%",
            "data": null,
            "bSortable": false,
            "sDefaultContent":"",
            "mRender": function (data, type, full) {
                if(data.uuid!=null) {
                    return '<i type="button" emeis='+data.uuid+' id="emei"  class="fa fa-mobile fa-2x"  style="color: red; margin-right:10px;">'+'</i>' +  data.uuid;
                }
            }
        },{
            "sWidth": "20%",
            "data": null,
            "bSortable": true,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                if (data.time != 0) {
                    return new Date(data.time);
                }
            }
        },{
            "sWidth": "5%",
            "data": null,
            "bSortable": false,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                if (data.latitude != 0 && data.longitude != 0) {
                    return '<button type="button" scouse=' + data.score + ' id="maps" latitude=' + data.latitude + ' class="btn btn-info btn-sm" longitude=' + data.longitude + '>' + '<i class="fa fa-map-marker "></i>' + '</button>';
                }
            }
        } ]
    });

}

function filter(){
    $(document).on("click","#buttonFilter",function(){
        myTable.fnSettings().ajax = {
            "url": "Pronunciations",
            "type": "POST",
            "dataType":"json",
            "data": {
                list: "list",
                username: $("#username").val(),
                word: $("#word").val(),
                uuid: $("#uuid").val()
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
            word:$("#word").val(),
            uuid:$("#uuid").val(),
            search:$(".table-responsive input[type=search]").val()
        },
        success:function(data){
            if(data.status==true) {
                drawChart(data.sc);
                google.setOnLoadCallback(drawChart);

            }
            if(data.status==false){
                $("#dashboard").append("<b style='font-size: 25px; color: red;'>Dữ liệu quá lớn</b>");
            }
            if(data.mess=="Error Sever")
            {
                $("#dashboard").append("<b style='font-size: 25px; color: red;'>Error.</b>");
            }

        },
        error:function(e){
            alert(e);
        }

    });
}

google.load('visualization', '1', {packages: ['controls', 'charteditor']});
function drawChart(sc) {
    var data = new google.visualization.DataTable();
    data.addColumn('datetime', 'date');
    data.addColumn('number', 'Score');
    for(j=0;j<sc.length;j++){
        data.addRow([new Date(sc[j][0]), sc[j][1]]);
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

function detailemei(){
    $(document).on("click", "#emei", function () {
        $('#emeimodal').modal('show');
        var emei=$("#emei").attr('emeis');
        $.ajax({
            url:"Pronunciations",
            type:"POST",
            dataType:"json",
            data:{
                emei:emei,
                detailmodal:"detailmodal"
            },
            success:function(data){
                if(data!=null) {
                    $("#emeipopup").text(data.imei);
                    $("#devicenamepopup").text(data.deviceName);
                    $("#modelpopup").text(data.model);
                    $("#osversionpopup").text(data.osVersion);
                    $("#osapilevelpopup").text(data.osApiLevel);
                    $("#attacheddatepopup").text(data.attachedDate);
                }else {
                    $("#emeipopup").text("");
                    $("#devicenamepopup").text("");
                    $("#modelpopup").text("");
                    $("#osversionpopup").text("");
                    $("#osapilevelpopup").text("");
                    $("#attacheddatepopup").text("");
                }
            },
            error:function(){
                alert("error");
            }

        });



    });
}

$(document).ready(function(){

    detailemei();
    maps();
    filter();
    listScore();
    drawMap();
    search();
});

