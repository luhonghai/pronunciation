var myTable;

function listAdmin(){

        myTable = $('#dataTables-example').dataTable({
            "retrieve": true,
            "destroy": true,
            "responsive": true,
            "bProcessing": true,
            "bServerSide": true,

            "ajax": {
                "url": "ManagementTeacherOrStaff",
                "type": "POST",
                "dataType": "json",
                "data": {
                    list: "list"
                }
            },

            "columns": [{
                "sWidth": "75%",
                "data": "company",
                "sDefaultContent": ""

            }, {
                "data": null,
                "bSortable": false,
                "sDefaultContent": "",
                "mRender": function (data, type, full) {
                    $button = $('<a href="add-merchant-or-staff.jsp?idCompany='+ data.idCompany +'" type="button" style="margin-right:10px" id="edit" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Add Teacher Or Staff' + '</a>');
                    $button.attr("id-column", data.id);
                    return $("<div/>").append($button).html();
                }
            }]

        });


}

$(document).ready(function(){
    listAdmin();
});

