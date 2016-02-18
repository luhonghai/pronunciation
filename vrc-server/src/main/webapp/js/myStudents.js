var myTable;

function listMyStudent(){

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
                    action: "listMyStudents"
                }
            },

            "columns": [{
                "data": null,
                "bSortable": false,
                "sDefaultContent": "",
                "mRender": function (data, type, full) {
                    $checkbox = $('<input type="checkbox" id="check">');
                    $checkbox.attr("id-column", data.id);
                    $checkbox.attr("status",data.status);
                    $checkbox.attr("licence", data.licence);
                    $checkbox.attr("studentName", data.studentName);
                    return $("<div/>").append($checkbox).html();

                }
            },{
                "sWidth": "50%",
                "data": "studentName",
                "sDefaultContent": ""

            },{
                "sWidth": "25%",
                "data": null,
                "bSortable": false,
                "sDefaultContent": "",
                "mRender": function (data, type, full) {
                    if (data.status == "pending") {
                        return '<span class="btn btn-sm" style="background-color: orange;color: white; padding:5px; cursor: default;">'+"Pending"+'</span>';
                    }else if (data.status == "reject") {
                        return '<span class="btn btn-sm" style="background-color: red;color: white; padding:5px; cursor: default;">'+"Rejected"+'</span>';
                    }else{
                        return '<span class="btn btn-sm" style="background-color: green;color: white; padding:5px; cursor: default;">'+"Accept"+'</span>';
                    }
                }
            }]

        });


}

function deleteStudent(){
    $(document).on("click","#deleteStudents",function(){
        var listStudent = [];
        $(this).closest('table').find('td :checkbox:checked').each(function(i){
            listStudent.push({
                id:$(this).attr('id-column'),
                licence:$(this).attr('licence'),
                studentName:$(this).attr('studentName')
            });
        });
        var obj={
            listStudent:listStudent
        }
        swal({
            title: "Are you sure that you want to remove the following students?",
            text: "This action will remove the students from your classes and they will not longer have access to any associated courses.If you remove unlicensed students you will need to invite them again to add to classes in the future",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "Yes, delete it!",
            closeOnConfirm: false
        },
        function(){
            $.ajax({
                "url": "SendMailUser",
                "type": "POST",
                "dataType":"text",
                "data": {
                    action: "deleteStudent",
                    listStudent:JSON.stringify(obj)
                },
                success:function(data){
                    if(data=="success"){
                        myTable.fnDraw();
                        swal("Success!", "Delete success", "success");
                    }
                },
                error:function(e){
                    swal("Error!", "Could not connect to server", "error");
                }

            });
        });



    })
}



$(document).ready(function(){
    $('#selectAll').click(function (e) {
        $(this).closest('table').find('td input:checkbox').prop('checked', this.checked);
    });
   deleteStudent();
    listMyStudent();
});

