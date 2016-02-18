var myTable;

function listLicensedStudent(){

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
                    action: "listLicensedStudents"
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
                    return $("<div/>").append($checkbox).html();

                }
            },{
                "sWidth": "75%",
                "data": "studentName",
                "sDefaultContent": ""

            }]

        });


}


function addStudent(){
    $(document).on("click","#addStudents",function(){
        var listStudent = [];
        $(this).closest('table').find('td :checkbox:checked').each(function(i){
            listStudent[i] = $(this).attr('id-column');
        });

        var obj={
            listStudent:listStudent
        }
        $.ajax({
            "url": "SendMailUser",
            "type": "POST",
            "dataType":"text",
            "data": {
                action: "addStudents",
                listStudent:JSON.stringify(obj)
            },
            success:function(data){
                if(data=="success"){
                    myTable.fnDraw();
                    swal("Success!", "Add success", "success");
                }
            },
            error:function(e){
                swal("Error!", "Could not connect to server", "error");
            }

        });
    })
}




$(document).ready(function(){
    $('#selectAll').click(function (e) {
        $(this).closest('table').find('td input:checkbox').prop('checked', this.checked);
    });
    addStudent();
    listLicensedStudent();
});

