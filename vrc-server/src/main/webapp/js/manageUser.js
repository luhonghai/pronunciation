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
                return '<button type="button" id="map" latitude=' + data.latitude +' class="btn btn-info btn-sm" longitude=' + data.longitude +'>' + '<i class="fa fa-map-marker "></i>' + '</button>';
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

function mapdetail(){
    $(document).on("click","#map",function() {
        var latitude=$(this).attr('latitude');
        var longitude=$(this).attr('longitude');
        $("#mapDetail").modal('show');
        map(latitude,longitude);
    });
}

function map(lat,long){
    var myLatlng = new google.maps.LatLng(lat,long);
    var myOptions = {
        zoom: 4,
        center: myLatlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    map = new google.maps.Map($('#map'), myOptions);
    var marker = new google.maps.Marker({
        position: myLatlng,
        map: map,
        title:"Map detail"

});
}


$(document).ready(function(){
    mapdetail();
    userDevice();
    user();
});
