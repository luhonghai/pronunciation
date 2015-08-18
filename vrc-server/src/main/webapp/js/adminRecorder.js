var myTable;

function listTranscriptionRecorder(){

    myTable = $('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "responsive": true,
        "bProcessing": true,
        "bServerSide": true,

        "ajax": {
            "url": "RecorderServlet",
            "type": "POST",
            "dataType": "json",
            "data": {
                action: "listbyadmin",
                sentence: $("#sentence").val(),
                account:$("#account").val(),
                dateFrom:$("#dateFrom").val(),
                dateTo:$("#dateTo").val(),
                status:$("#status").val()
            }
        },

        "columns": [{
            "sWidth": "15%",
            "data": "account",
            "sDefaultContent": ""

        }, {
            "sWidth": "30%",
            "data": "sentence",
            "bSortable": false,
            "sDefaultContent": ""
        },{
            "sWidth": "5%",
            "data": null,
            "bSortable": false,
            "sDefaultContent":"",
            "mRender": function (data, type, full) {
                return '<i class="fa fa-file-audio-o fa-2x"></i>';
            }
        }, {
            "sWidth": "20%",
            "data": "modifiedDate",
            "sDefaultContent": ""
        }, {
            "sWidth": "15%",
            "data": "status",
            "sDefaultContent": ""
        },{
            "sWidth": "15%",
            "data": null,
            "bSortable": false,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                $button = $('<button type="button" style="margin-right:10px" id="edit" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Edit' + '</button>' + '<button type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>');
                $button.attr("id-column", data.id);
                $button.attr("first", data.firstName);
                $button.attr("last", data.lastName);
                $button.attr("role", data.role);
                return $("<div/>").append($button).html();
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

function searchAdvanted(){
    $(document).on("click","#button-filter", function(){
        myTable.fnSettings().ajax = {
            "url": "RecorderServlet",
            "type": "POST",
            "dataType": "json",
            "data": {
                action: "listbyadmin",
                sentence: $("#sentence").val(),
                account:$("#account").val(),
                dateFrom:$("#dateFrom").val(),
                dateTo:$("#dateTo").val(),
                status:$("#status").val()
            }
        };
        $("tbody").html("");
        myTable.fnDraw();


    });
}

function loadNumber(){
    var $selected=$("#listaccount");
    $('#listaccount option[value!="-1"]').remove();
    $.ajax({
        "url":"RecorderServletNumber",
        "type": "POST",
        "dataType":"json",
        "data":{
            loadNumber:"load"
        },
        success:function(data){
            var items=data.recordedSentences;
            $(items).each(function(){
                var newOption = '<option value="' + this.account + '">' + this.account + '</option>';
                $selected.append(newOption);
            });

            $("#awaiting").text(data.waiting);
            $("#pending").text(data.pending);
            $("#reject").text(data.reject);
            $("#approved").text(data.approved);
            $("#locked").text(data.locke);
            $("#all").text(data.allsentence);



        }
    });
}

function selected(){
    $(document).on("change","#listaccount", function(){
        var account=$("#listaccount").val();
        $.ajax({
            "url":"RecorderServletNumber",
            "type": "POST",
            "dataType":"json",
            "data":{
                account:account,
                loadNumberAccount:"load"
            },
            success:function(data){
                $("#awaiting").text(data.waiting);
                $("#pending").text(data.pending);
                $("#reject").text(data.reject);
                $("#approved").text(data.approved);
                $("#locked").text(data.locke);
                $("#all").text(data.allsentence);



            }
        });

    });

}


$(document).ready(function(){
    var roleAdmin=$("#role").val();
    dateFrom();
    dateTo();
    selected();
    loadNumber();
    listTranscriptionRecorder();
    searchAdvanted();
});



