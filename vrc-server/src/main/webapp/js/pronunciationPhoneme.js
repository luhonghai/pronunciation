var myTable;
var avg;
function listScore(){
    myTable=$('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "responsive": true,
        "bProcessing": true,
        "bServerSide": true,
        "ajax": {
            "url": "PronunciationPhoneme",
            "type": "POST",
            "dataType":"json",
            "data":{
                list:"list",
                username:$("#username").val(),
                phoneme:$("#phoneme").val(),
                score:$("#score").val(),
                type:$("#type").val(),
                country:$("#country").val(),
                dateFrom:$("#dateFrom").val(),
                dateTo:$("#dateTo").val()


            }
        },

        "columns": [{
            "sWidth": "25%",
            "data": "username",
            "sDefaultContent":""

        },{
            "sWidth": "20%",
            "data": "phonemes",
            "sDefaultContent":""
        }, {
            "sWidth": "5%",
            "data": "score",
            "sDefaultContent":""
        }, {
            "sWidth": "20%",
            "data": "country",
            "sDefaultContent":""
        },{
            "sWidth": "20%",
            "data": null,
            "bSortable": true,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                if (data.serverTime != 0) {
                    return new Date(data.serverTime);
                }
            }
        }, {
            "sWidth": "5%",
            "data": "type",
            "sDefaultContent":""
        }]
    });

}

function dateFrom(){
    $('#dateFrom').datetimepicker({
        format: 'YYYY/MM/DD'
    });
}
function dateTo(){
    $('#dateTo').datetimepicker({
        format: 'YYYY/MM/DD'
    });
}

function filter(){
    $(document).on("click","#buttonFilter",function(){
        myTable.fnSettings().ajax = {
            "url": "PronunciationPhoneme",
            "type": "POST",
            "dataType":"json",
            "data": {
                list:"list",
                username:$("#username").val(),
                phoneme:$("#phoneme").val(),
                score:$("#score").val(),
                type:$("#type").val(),
                country:$("#country").val(),
                dateFrom:$("#dateFrom").val(),
                dateTo:$("#dateTo").val()
            }
        };
        $("tbody").html("");
        myTable.fnDraw();

    });

}

$(document).ready(function(){


    dateFrom();
    dateTo();
    filter();
    listScore();
});

