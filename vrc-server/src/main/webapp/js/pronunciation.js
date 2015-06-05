var myTable;
function listScore(){
    myTable=$('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
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
                return '<button type="button" id="maps" latitude=' + data.latitude +' class="btn btn-info btn-sm" longitude=' + data.longitude +'>' + '<i class="fa fa-map-marker "></i>' + '</button>';
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
    });

}

var mapOptions;
var map;
var marker;

function mapss(latitude,longitude) {
    mapOptions = {
        scaleControl: true,
        center: new google.maps.LatLng(latitude, longitude),
        zoom: 5,
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
    $('#mapDetail').modal('show');
}
function refreshMap() {
    google.maps.event.trigger(map, 'resize');

}
function maps() {
    $('#mapDetail').on("show.bs.modal", function() {
        setTimeout(refreshMap, 300);
    });

    $(document).on("click", "#maps", function () {
        var latitude = $(this).attr('latitude');
        var longitude = $(this).attr('longitude');
        var x=parseFloat(latitude);
        var y=parseFloat(longitude);
        mapss(x,y);
    });
}




$(document).ready(function(){
    maps();
    filter();
    listScore();

});

