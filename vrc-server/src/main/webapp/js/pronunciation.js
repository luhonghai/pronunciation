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
            "sWidth": "30%",
            "data": "username",
            "sDefaultContent":""

        },{
            "sWidth": "20%",
            "data": "word",
            "sDefaultContent":""
        }, {
            "sWidth": "15%",
            "data": "score",
            "sDefaultContent":""
        }, {
            "sWidth": "25%",
            "data": "uuid",
            "sDefaultContent":""
        },{
            "data": null,
            "bSortable": false,
            "mRender": function (data, type, full) {
                return '<button type="button" scouse='+data.score+' id="maps" latitude=' + data.latitude +' class="btn btn-info btn-sm" longitude=' + data.longitude +'>' + '<i class="fa fa-map-marker "></i>' + '</button>';
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
        //var x=parseFloat(latitude);
        //var y=parseFloat(longitude);
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
        "url": "Pronunciations",
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
            if(data.recordsTotal<10000) {
                drawChart(data.sc);

            }
            if(data.recordsTotal>=10000){
                $("#drawchart").append("<b>Dữ liệu quá lớn</b>")
            }

        },
        error:function(e){
            alert(e);
        }

    });
}

google.load('visualization', '1', {packages: ['corechart', 'line']});
google.setOnLoadCallback(drawChart);

function drawChart(sc) {
    var data = new google.visualization.DataTable();
    data.addColumn('number', 'X');
    data.addColumn('number', 'Score');
    data.addRows(sc);

    var options = {
        hAxis: {
            title: ''
        },
        vAxis: {
            title: 'score'
        },
        width: 550,
        height: 200
    };

    var chart = new google.visualization.LineChart(document.getElementById('drawchart'));

    chart.draw(data, options);
}


$(document).ready(function(){
    maps();
    filter();
    listScore();
    drawMap();
    search();
});

