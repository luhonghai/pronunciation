/**
 * Created by CMGT400 on 10/8/2015.
 */
var myTable;
var servletName="ManagementObjective";
var lessonName;
var idLesson;

function listLessons(){

    myTable = $('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "responsive": true,
        "bProcessing": true,
        "bServerSide": true,

        "ajax": {
            "url": servletName,
            "type": "POST",
            "dataType": "json",
            "data": {
                list: "list",
                lesson: $("#lesson").val(),
                CreateDateFrom: $("#CreateDateFrom").val(),
                CreateDateTo: $("#CreateDateTo").val()
            }
        },

        "columns": [
           {
                "sWidth": "15%",
                "data": "name",
                "bSortable": false,
                "sDefaultContent": ""
            }, {
                "sWidth": "15%",
                "data": "description",
                "bSortable": false,
                "sDefaultContent": ""
             }, {
                "sWidth": "15%",
                "data": "dateCreated",
                "sDefaultContent": ""
            }, {
                "sWidth": "25%",
                "data": null,
                "bSortable": false,
                "sDefaultContent": "",
                "mRender": function (data, type, full) {
                    $button = $('<button type="button" style="margin-right:5px" id="edit" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Edit' + '</button>' + '<button style="margin-right:5px" type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>');
                    $button.attr("id-column", data.id);
                    $button.attr("name", data.name);
                    $button.attr("description", data.description);
                    return $("<div/>").append($button).html();
                }
        }]

    });
}

function dateFrom(){
    $('#CreateDateFrom').datetimepicker({
        format: 'DD/MM/YYYY'
    });
}

function dateTo(){
    $('#CreateDateTo').datetimepicker({
        format: 'DD/MM/YYYY'
    });
}



$(document).ready(function(){
    dateFrom();
    dateTo();

});


