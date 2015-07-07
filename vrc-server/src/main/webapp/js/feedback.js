var myTable;
function listFeedback(){

    myTable=$('#dataTables-example').dataTable({
                "retrieve": true,
                "destroy": true,
                "responsive": true,
                "bProcessing": true,
                "bServerSide": true,

                "ajax": {
                    "url": "Feedbacks",
                    "type": "POST",
                    "dataType":"json",
                    "data":{
                        list:"list",
                        account:$("#account").val(),
                        imei:$("#imei").val(),
                        appVersion:$("#appversion").val(),
                        osVersion:$("#osversion").val(),
                        dateFrom:$("#DateFrom").val(),
                        dateTo: $("#DateTo").val()
                    }
                },

                "columns": [{
                    "sWidth": "25%",
                    "data": "account",
                    "sDefaultContent":""

                }, {
                    "sWidth": "25%",
                    "data": null,
                    "bSortable": false,
                    "sDefaultContent":"",
                    "mRender": function (data, type, full) {
                        if(data.imei!=null) {
                            return '<i type="button" emeis='+data.imei+' id="emei"  class="fa fa-mobile fa-2x"  style="color: red; margin-right:10px;">'+'</i>' +  data.imei;
                        }
                    }
                }, {
                    "sWidth": "15%",
                    "data": "appVersion",
                    "sDefaultContent":""
                }, {
                    "sWidth": "15%",
                    "data": "osVersion",
                    "sDefaultContent":""
                }, {
                    "sWidth": "15%",
                    "data": "createdDate",
                    "sDefaultContent":""
                }, {
                    "data": null,

                    "bSortable": false,
                    "mRender": function (data, type, full) {
                        //  console.log(data);
                        return '<button type="button" id="detail" id-column=' + data.id + ' class="btn btn-info btn-sm" ' + full[0] + '>' + '  !  ' + '</button>';
                    }

                }]



            });




}

function dateFrom(){
    $('#DateFrom').datetimepicker({
        format: 'DD/MM/YYYY'
    });
}
function dateTo(){
    $('#DateTo').datetimepicker({
        format: 'DD/MM/YYYY'
    });
}

function detailemei(){
    $(document).on("click", "#emei", function () {
        $('#emeimodal').modal('show');
        var emei=$("#emei").attr('emeis');
        $.ajax({
            url:"Feedbacks",
            type:"POST",
            dataType:"json",
            data:{
                emei:emei,
                detailmodal:"detailmodal"
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

function detail(){
    $(document).on("click","#detail", function(){
        $("#feedback1").modal('show');

        $("#accountmd").text("");
        $("#descriptionmd").text("");
        $("#IMEImd").text("");
        $("#deviceNamemd").text("");
        $("#appVersionmd").text("");
        $("#OSVersionmd").text("");
        $("#OSApiLevelmd").text("");
        $("#StackTracemd").val("");
        $("#Screenshootmd").attr("src", "");

        var idd=$(this).attr('id-column');
        $.ajax({
            url:"Feedbacks",
            type:"POST",
            dataType:"json",
            data:{
                id:idd,
                detail:"detail"
            },
            success:function(data){

                $("#accountmd").text(data.account);
                $("#descriptionmd").text(data.description);
                $("#IMEImd").text(data.imei);
                $("#deviceNamemd").text(data.deviceName);
                $("#appVersionmd").text(data.appVersion);
                $("#OSVersionmd").text(data.osVersion);
                $("#OSApiLevelmd").text(data.osApiLevel);
                $("#StackTracemd").val(data.stackTrace);
                if (data.screenshoot.length > 0)
                    $("#Screenshootmd").attr("src", data.screenshoot);

            },
            error:function(){
                alert("error");
            }

        });

    });
}
 function searchAdvanted(){
     $(document).on("click","#button-filter", function(){
         myTable.fnSettings().ajax = {
             "url": "Feedbacks",
             "type": "POST",
             "dataType":"json",
             "data":{
                 list:"list",
                 account:$("#account").val(),
                 imei:$("#imei").val(),
                 appVersion:$("#appversion").val(),
                 osVersion:$("#osversion").val(),
                 dateFrom:$("#DateFrom").val(),
                 dateTo: $("#DateTo").val()
             }
         };
         $("tbody").html("");
         myTable.fnDraw();


     });
 }

$(document).ready(function(){

    listFeedback();
    detail();
    detailemei();
    searchAdvanted();
    dateFrom();
    dateTo();

});
