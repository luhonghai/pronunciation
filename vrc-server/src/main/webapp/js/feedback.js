var myTable;
function listFeedback(){

    myTable=$('#dataTables-example').dataTable({
                "retrieve": true,
                "destroy": true,
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
                    "data": "account",
                    "sDefaultContent":""

                }, {
                    "sWidth": "20%",
                    "data": "imei",
                    "sDefaultContent":""
                }, {
                    "sWidth": "20%",
                    "data": "appVersion",
                    "sDefaultContent":""
                }, {
                    "sWidth": "20%",
                    "data": "osVersion",
                    "sDefaultContent":""
                }, {
                    "sWidth": "20%",
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

function detail(){
    $(document).on("click","#detail", function(){
        $("#feedback1").modal('show');
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
                $("#Screenshootmd").text(data.screenshoot);


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
    searchAdvanted();
    dateFrom();
    dateTo();

});
