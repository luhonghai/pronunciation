function listFeedback(){
    var data1;
    $.ajax({
        url: "../Feedbacks",
        type: "POST",
        dataType: "text",
        data:{
          list:"list"
        },
        success :function(result){

            data1=$.parseJSON(result);
            $('#dataTables-example').dataTable({

                "responsive": true,
                "bJQueryUI": true,
                "bDestroy": true,
                "processing": true,
                "serverSide": true,
                "iTotalDisplayRecords":3,
                "iTotalRecords":3,
                "aaData": data1,


                "aoColumns": [{
                    "mData": "account",
                    "sDefaultContent":""

                }, {
                    "sWidth": "20%",
                    "mData": "imei",
                    "sDefaultContent":""
                }, {
                    "sWidth": "20%",
                    "mData": "appVersion",
                    "sDefaultContent":""
                }, {
                    "sWidth": "20%",
                    "mData": "osVersion",
                    "sDefaultContent":""
                }, {
                    "sWidth": "20%",
                    "mData": "createdDate",
                    "sDefaultContent":""
                }, {
                    "mData": null,

                    "bSortable": false,
                    "mRender": function (data, type, full) {
                        //  console.log(data);
                        return '<button type="button" id="detail" id-column=' + data.id + ' class="btn btn-info btn-sm" ' + full[0] + '>' + 'Detail' + '</button>';
                    }

                }]



            });


        }


    });


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
         var data1;
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

                 data1=$.parseJSON(result);
                 $('#dataTables-example').dataTable({
                     "bJQueryUI": true,
                     "bDestroy": true,
                     "responsive": true,
                     "iTotalDisplayRecords":3,
                     "iTotalRecords":3,
                     "aaData": data1,
                     "bProcessing": true,



                     "aoColumns": [{
                         "mData": "account",
                         "sDefaultContent":""

                     }, {
                         "sWidth": "20%",
                         "mData": "imei",
                         "sDefaultContent":""
                     }, {
                         "sWidth": "20%",
                         "mData": "appVersion",
                         "sDefaultContent":""
                     }, {
                         "sWidth": "20%",
                         "mData": "osVersion",
                         "sDefaultContent":""
                     }, {
                         "sWidth": "20%",
                         "mData": "createdDate",
                         "sDefaultContent":""
                     }, {
                         "mData":null,
                         "bSortable": false,
                         "mRender": function(data, type, full) {
                             console.log(data);
                             return '<button type="button" id="detail" id-column='+data.id+' class="btn btn-info btn-sm" ' + full[0] + '>' + 'Detail' + '</button>';
                         }
                     }
                         ]


                 });
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

});
