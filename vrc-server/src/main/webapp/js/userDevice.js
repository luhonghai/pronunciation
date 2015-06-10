var myTable;
function listUserDevice(){
    myTable=$('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "bProcessing": true,
        "bServerSide": true,
        "ajax": {
            "url": "UserDevice",
            "type": "POST",
            "dataType":"json",
            "data":{
                list:"list",
                emei:$("#emei").val(),
                model:$("#model").val(),
                osVersion:$("#osVersion").val(),
                osApiLevel:$("#osApiLevel").val(),
                deviceName:$("#deviceName").val(),
                emeisearch:$("#emeisearch").val(),
                dateFrom:$("#dateFrom1").val(),
                dateTo:$("#dateTo1").val()
            }
        },

        "columns": [{
            "sWidth": "15%",
            "data": "model",
            "sDefaultContent":""

        },{
            "sWidth": "20%",
            "data": "osVersion",
            "sDefaultContent":""
        }, {
            "sWidth": "15%",
            "data": "osApiLevel",
            "sDefaultContent":""
        }, {
            "sWidth": "15%",
            "data": "deviceName",
            "sDefaultContent":""
        }, {
            "sWidth": "17%",
            "data": "emei",
            "sDefaultContent":""
        }, {
            "sWidth": "18%",
            "data": "attachedDate",
            "sDefaultContent":""
        }]
    });

}

function filter(){
    $(document).on("click","#buttonFilter",function(){
        myTable.fnSettings().ajax = {
            "url": "UserDevice",
            "type": "POST",
            "dataType":"json",
            "data":{
                list:"list",
                user:$("#user").val(),
                model:$("#model").val(),
                osVersion:$("#osVersion").val(),
                osApiLevel:$("#osApiLevel").val(),
                deviceName:$("#deviceName").val(),
                emei:$("#emei").val(),
                dateFrom:$("#dateFrom1").val(),
                dateTo:$("#dateTo1").val()
            }
        };
        $("tbody").html("");
        myTable.fnDraw();
    });

}

function button(){
    var visible=$("#roles").val();
    if(visible=="1"){
        $("#server").show();
    }
}
$(document).ready(function(){
    button();
    filter();
    listUserDevice();

});
