function listFeedback(){

            $('#dataTables-example').dataTable({
                "bProcessing": true,
                "bServerSide": true,

                "ajax": {
                    "url": "Feedbacks",
                    "type": "POST",
                    "dataType":"json",
                    "data":{
                        list:"list"
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
    $('#DateFrom').datetimepicker();
}
function dateTo(){
    $('#DateTo').datetimepicker();
}

function detail(){
    $(document).on("click","#detail", function(){
        $("#feedback1").modal('show');
        var idd=$(this).attr('id-column');
        $.ajax({
            url:"../Feedbacks",
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

         var account=$("#account").val();
         var imei=$("#imei").val();
         var appVersion=$("#appversion").val();
         var osVersion=$("#osversion").val();
         var date=$("#createddate").val();
         $.ajax({
             url:"",
             type:"POST",
             dataType:"json",
             data:{
                 filter:"button-filter",
                 account:account,
                 imei:imei,
                 appVersion:appVersion,
                 osVersion:osVersion,
                 date:date


             },
             success:function(result){



             },
             error:function(e){
                 alert("error"+e);
             }

         });

     });
 }
$(document).ready(function(){
    listFeedback();
    detail();
    searchAdvanted();
    dateFrom();
    dateTo();

});
