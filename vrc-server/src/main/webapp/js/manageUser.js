var myTable;
function user(){
    myTable=$('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "bProcessing": true,
        "bServerSide": true,
        "ajax": {
            "url": "UserManage",
            "type": "POST",
            "dataType":"json",
            "data":{
                list:"list",
                user:$("#user").val()

            }
        },

        "columns": [{
            "sWidth": "25%",
            "data": "username",
            "sDefaultContent":""

        },{
            "sWidth": "25%",
            "data": "emei",
            "sDefaultContent":""
        }, {
            "sWidth": "20%",
            "data": "appVersion",
            "sDefaultContent":""
        }, {
            "data": null,
            "bSortable": false,
            "mRender": function (data, type, full) {
                return '<button type="button" id="maps" latitude=' + data.latitude +' class="btn btn-info btn-sm" longitude=' + data.longitude +'>' + '<i class="fa fa-map-marker "></i>' + '</button>';
            }
        }, {
            "sWidth": "20%",
            "data": "time",
            "sDefaultContent":""
        }, {
            "data": null,
            "bSortable": false,
            "mRender": function (data, type, full) {
                return '<button type="button" id="user"  emei=' + data.emei + '>' + ' <i class="fa fa-chevron-circle-right"></i>' + ' </button>';
            }
        }]
    });

}
function userDevice(){
    $(document).on("click","#user",function() {
        var emei = $(this).attr('emei');
        window.location.href = "UserDevice.jsp?emei=" + emei;
    });

}
var mapOptions;
var map;
var marker;

function mapss(latitude,longitude) {
     mapOptions = {
        scaleControl: true,
        center: new google.maps.LatLng(latitude, longitude),
        zoom: 4
    };

     map = new google.maps.Map(document.getElementById('map'), mapOptions);

     marker = new google.maps.Marker({
        map: map,
        position: map.getCenter()
    });
    marker.setMap(map);

    $('#mapDetail').modal('show', google.maps.event.trigger(map, 'resize'));
    // infowindow = new google.maps.InfoWindow();
    //google.maps.event.addListener(marker, 'click', function () {
    //    infowindow.open(map, marker);
    //});
}

function maps() {
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
    userDevice();
    user();
});
