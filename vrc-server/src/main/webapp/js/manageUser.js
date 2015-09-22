var myTable;
function user(){
    myTable=$('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "responsive": true,
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
            "data": null,
            "bSortable": false,
            "sDefaultContent":"",
            "mRender": function (data, type, full) {
                if(data.imei!=null) {
                    return '<i type="button" emeis='+data.imei+' id="emei"  class="fa fa-mobile fa-2x"  style="color: red; margin-right:10px;">'+'</i>' +  data.imei;
                }
            }
        }, {
            "sWidth": "20%",
            "data": "appVersion",
            "sDefaultContent":""
        }, {
            "data": null,
            "bSortable": false,
            "sDefaultContent":"",
            "mRender": function (data, type, full) {
                if (data.latitude != 0 && data.longitude != 0) {
                    return '<button type="button" id="maps" latitude=' + data.latitude + ' class="btn btn-info btn-sm" longitude=' + data.longitude + '>' + '<i class="fa fa-map-marker "></i>' + '</button>';
                }
            }
        }, {
            "sWidth": "20%",
            "data": "time",
            "sDefaultContent":""
        }]
    });

}

function detail(){
    $(document).on("click", "#emei", function () {
        $('#emeimodal').modal('show');
        var emei=$("#emei").attr('emeis');
        $.ajax({
            url:"UserManage",
            type:"POST",
            dataType:"json",
            data:{
                emei:emei,
                detail:"detail"
            },
            success:function(data){

                $("#emeipopup").text(data.imei);
                $("#devicenamepopup").text(data.deviceName);
                $("#modelpopup").text(data.model);
                $("#osversionpopup").text(data.osVersion);
                $("#osapilevelpopup").text(data.osApiLevel);
                $("#attacheddatepopup").text(data.attachedDate);
            },
            error:function(){
                alert("error");
            }

        });



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
}
function refreshMap() {
    //google.maps.event.trigger(map, 'resize');
    var latitude = $('#mapDetail').attr('latitude');
    var longitude = $('#mapDetail').attr('longitude');
    var x=parseFloat(latitude);
    var y=parseFloat(longitude);
    mapss(x,y);
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

$(document).ready(function(){
    detail();
    maps();
    user();
});
