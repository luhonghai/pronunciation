var myTable;
var avg=0;
var tong=0;
var lenght=0;
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
            "sWidth": "15%",
            "data": "phoneme",
            "sDefaultContent":"",
            "bSortable": false
        }, {
            "sWidth": "10%",
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
                if(data!=null){
                    tong=tong+data.score;
                    lenght=lenght+1;
                    avg=tong/lenght;
                }
                $("#avg").text(avg);

                if (data.serverTime != 0) {
                    return new Date(data.serverTime);
                }
            }
        }, {
            "sWidth": "10%",
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
        //format:'YYYY-MM-DD hh:mm:ss.SSS'
    });
}

function filter(){
    $(document).on("click","#buttonFilter",function(){
         avg=0;
         tong=0;
         lenght=0;
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

