var myTable;

function listMaping(){

        myTable = $('#dataTables-example').dataTable({
            "retrieve": true,
            "destroy": true,
            "responsive": true,
            "bProcessing": true,
            "bServerSide": true,

            "ajax": {
                "url": "SendMailUser",
                "type": "POST",
                "dataType": "json",
                "data": {
                    action: "list"
                }
            },

            "columns": [{
                "sWidth": "25%",
                "data": "studentName",
                "sDefaultContent": ""

            },{
                "data": null,
                "bSortable": false,
                "sDefaultContent": "",
                "mRender": function (data, type, full) {
                    var $button;
                    if(data.status="accept"){
                        $button = $('<button type="button" id="check" class="btn btn-info btn-sm" >' + '<i class="fa fa-trash" style="color: #ff0000"></i>' + '</button>');
                    }else{
                        $button = $('<button type="button" id="check" class="btn btn-info btn-sm" >' + '<i class="fa fa-check-circle" style="color: #00CC00"></i>' + '</button>');
                    }
                    $button.attr("id-column", data.id);
                    $button.attr("status",data.status);
                    $button.attr("studentname", data.studentName);
                    return $("<div/>").append($button).html();
                }
            }]

        });


}
function accept(){
    $(document).on("click","#check", function(){
        var id=$(this).attr('id-column');
        var status=$(this).atrr('status');
        var studentName=$(this).attr('studentname');
        if(status=="accept"){
            swal({
                title: "Are you sure you want to remove this student?",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, delete it!",
                cancelButtonText: "No, cancel!",
                closeOnConfirm: false,
                closeOnCancel: false
            },
            function(isConfirm){
                if (isConfirm) {
                    $.ajax({
                        "url": "SendMailUser",
                        "type": "POST",
                        "dataType":"text",
                        "data": {
                            action: "deleted",
                            studentName:studentName,
                            id:id
                        },
                        success:function(data){
                            if(data=="success"){
                                swal("Deleted!", "Delete success", "success");
                            }
                        },
                        error:function(e){
                            swal("Error!", "Could not connect to server", "error");
                        }

                    });
                }
            });

        }else{
            swal({
                    title: "Are you sure you want to remove this student?",
                    type: "info",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "Accept",
                    cancelButtonText: "Reject",
                    closeOnConfirm: false,
                    closeOnCancel: false
                },
                function(isConfirm){
                    if (isConfirm) {
                        $.ajax({
                            "url": "SendMailUser",
                            "type": "POST",
                            "dataType":"text",
                            "data": {
                                action: "accept",
                                id:id
                            },
                            success:function(data){
                                if(data=="success"){
                                    swal("Good job!", "success")
                                }
                            },
                            error:function(e){
                                swal("Error!", "Could not connect to server", "error");
                            }

                        });
                    } else {
                        $.ajax({
                            "url": "SendMailUser",
                            "type": "POST",
                            "dataType":"text",
                            "data": {
                                action: "reject",
                                id:id
                            },
                            success:function(data){
                                if(data=="success"){
                                    swal("Good job!", "success")
                                }
                            },
                            error:function(e){
                                swal("Error!", "Could not connect to server", "error");
                            }

                        });
                    }
                });

        }
    });
}



$(document).ready(function(){
    accept();
    listMaping();
});

