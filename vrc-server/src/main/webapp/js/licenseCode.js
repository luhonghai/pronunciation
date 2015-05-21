var myTable;
function listLicenseCode(){

    myTable=$('#dataTables-example').dataTable({
                "retrieve": true,
                "destroy": true,
                "bProcessing": true,
                "bServerSide": true,
                "ajax": {
                    "url": "LicenseCodes",
                    "type": "POST",
                    "dataType":"json",
                    "data":{
                        list:"list"
                    }
                },
                "columns": [{
                    "sWidth": "30%",
                    "data": "account",
                    "sDefaultContent":""

                }, {
                    "sWidth": "25%",
                    "data": "code",
                    "sDefaultContent":""
                }, {
                    "sWidth": "30%",
                    "data": "activatedDate",
                    "sDefaultContent":""
                }, {
                    "data": null,
                    "bSortable": false,
                    "mRender": function (data, type, full) {

                        return '<button type="button" id="detail" id-column=' + data.id + ' class="showArchieved" ' + full[0] + '>' + ' <span class="glyphicon glyphicon-ok-circle"></span>' + ' </button>';
                    }
                }]
            });

}

function activated(){
    $(document).on("click","#detail",function(){
        var $el = $(this);
        $el.find('span').toggleClass('glyphicon-remove-sign glyphicon-ok-circle');
        $el.toggleClass('showArchieved');

    });
}

function search(){
    $(document).on("click","#button-filter", function(){

        var account=$("#account").val();
        var code=$("#code").val();
        var Acti=$("#Acti").val();
        var dateFrom=$("#dateFrom").text();
        var dateTo=$("#dateTo").text();
        $.ajax({
            url:"",
            type:"POST",
            dataType:"json",
            data:{
                filter:"button-filter",
                account:account,
                code:code,
                Acti:Acti,
                dateFrom:dateFrom,
                dateTo:dateTo
            },
            success:function(result){



            },
            error:function(e){
                alert("error"+e);
            }

        });

    });
}

function add(){
    $(document).on("click","#addCode",function() {
        $("#addCode1").modal('show');
    });
}

function dateFrom(){
    $('#dateFrom').datetimepicker();
}
function dateTo(){
    $('#dateTo').datetimepicker();
}

function addCode(){
    $(document).on("click","#Yes",function(){
        $.ajax({
            url:"LicenseCodes",
            type:"POST",
            dataType:"text",
            data:{
                addCode:"addCode"
            },
            success:function(result){
               if(result=="success"){
                   $("tbody").html("");
                   myTable.fnDraw();
                   $("#addCode1").modal('hide');

               }
            },
            error:function(){
                alert("error");
            }

        });

    });
}


$(document).ready(function(){
    search();
    add();
    addCode();
    listLicenseCode();
    activated();
    dateFrom();
    dateTo();
});