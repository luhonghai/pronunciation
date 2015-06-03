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
                        list:"list",
                        account:$("#account1").val() ,
                        code:$("#code1").val() ,
                        Acti:$("#Acti").val() ,
                        dateFrom:$("#dateFrom1").val(),
                        dateTo: $("#dateTo1").val()
                    }
                },
                "columns": [{
                    "sWidth": "25%",
                    "data": "account",
                    "sDefaultContent":""

                },
                    {
                        "sWidth": "20%",
                        "data": "imei",
                        "sDefaultContent":""
                    }
                    , {
                    "sWidth": "20%",
                    "data": "code",
                    "sDefaultContent":""
                }, {
                    "sWidth": "25%",
                    "data": "activatedDate",
                    "sDefaultContent":""
                }, {
                    "data": null,
                    "bSortable": false,
                    "mRender": function (data, type, full) {
                        if (data.isActivated == true) {
                            return '<button type="button" id="detail" name='+data.isActivated+'  id-column=' + data.id + ' class="showArchieved" ' + full[0] + '>' + ' <span class="glyphicon glyphicon-remove-sign"></span>' + ' </button>';
                        }else if(data.isActivated==false){
                            return '<button type="button" id="detail" name='+data.isActivated+' id-column=' + data.id + ' class="showArchieved" ' + full[0] + '>' + ' <span class="glyphicon glyphicon-ok-circle"></span>' + ' </button>';
                        }
                    }
                }]
            });

}

function activated(){
    $(document).on("click","#detail",function(){
        var idd=$(this).attr('name');
        var id=$(this).attr('id-column');
        var acti;
        var $el = $(this);
        $el.find('span').toggleClass('glyphicon-remove-sign glyphicon-ok-circle');
        $el.toggleClass('showArchieved');
        if(idd=="true"){
            acti=false;
        }
        if(idd=="false"){
            acti=true;
        }
        $.ajax({
            "url": "LicenseCodes",
            "type": "POST",
            "dataType":"text",
            "data": {
                activated: "activated",
                id:id,
                acti:acti
            },
            success:function(data){
                if(data=="success"){
                    $("tbody").html("");
                    myTable.fnDraw();
                }
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
    $('#dateFrom1').datetimepicker({
        format: 'DD/MM/YYYY'
    });
}
function dateTo(){
    $('#dateTo1').datetimepicker({
        format: 'DD/MM/YYYY'
    });
}

function filter(){
    $(document).on("click","#buttonFilter",function(){
        myTable.fnSettings().ajax = {
            "url": "LicenseCodes",
            "type": "POST",
            "dataType":"json",
            "data":{
                list:"list",
                account:$("#account1").val() ,
                code:$("#code1").val() ,
                Acti:$("#Acti").val() ,
                dateFrom:$("#dateFrom1").val(),
                dateTo: $("#dateTo1").val()
            }
        };
        $("tbody").html("");
        myTable.fnDraw();
    });

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

function searchAll(){

}

$(document).ready(function(){
    filter();

    add();
    addCode();
    listLicenseCode();
    activated();
    dateFrom();
    dateTo();
});