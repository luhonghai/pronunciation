var myTable;
function listLicenseCode(){

    myTable=$('#dataTables-example').DataTable({
                "retrieve": true,
                "destroy": true,
                "responsive": true,
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
                    "sWidth": "30%",
                    "data": "account",
                    "sDefaultContent":""

                }, {
                    "sWidth": "25%",
                    "data": null,
                    "bSortable": false,
                    "sDefaultContent":"",
                    "mRender": function (data, type, full) {
                        if(data.imei!=null) {
                            return '<i type="button" emeis='+data.imei+' id="emei"  class="fa fa-mobile fa-2x"  style="color: red; margin-right:10px;">'+'</i>' +  data.imei;
                        }
                    }
                },{
                    "sWidth": "25%",
                    "data": null,
                    "bSortable": false,
                    "sDefaultContent":"",
                    "mRender": function (data, type, full) {
                            return '<p style="font-family:tahoma">'+data.code+'</p>';
                    }
                }, {
                    "sWidth": "20%",
                    "data": "activatedDate",
                    "sDefaultContent":""
                }, {
                    "sWidth": "5%",
                    "data": null,
                    "bSortable": false,
                    "sDefaultContent": "",
                    "mRender": function (data, type, full) {
                        if (data.isActivated == true) {
                            return '<span type="button" id="detail" style="color:#FF0000" title="Click to deactivate" name='+data.isActivated+'  id-column=' + data.id + ' class="fa fa-times-circle fa-2x" ' + full[0] + '>' + ' </span>';
                        }else if(data.isActivated==false){
                            return '<span type="button" id="detail" style="color:#00CC00" title="Click to activate" name='+data.isActivated+' id-column=' + data.id + ' class="fa fa-check-circle fa-2x" ' + full[0] + '>' + ' </span>';
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
        var cl=$(this).attr("class");
        //$el.find('span').toggleClass('glyphicon-remove-sign glyphicon-ok-circle');
        //$el.toggleClass('showArchieved');
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
                    if(cl=="fa fa-times-circle fa-2x"){
                        $el.attr('class','fa fa-check-circle fa-2x');
                        $el.attr('name','false');
                        $el.attr('title','Click to activate');

                        $el.css('color','#00CC00');


                    }
                    if(cl=="fa fa-check-circle fa-2x"){
                        $el.attr('class','fa fa-times-circle fa-2x');
                        $el.attr('title','Click to deactivate');
                        $el.attr('name','true');
                        $el.css('color','#FF0000');
                    }
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
        var newRow;
        $.ajax({
            url:"LicenseCodes",
            type:"POST",
            dataType:"text",
            data:{
                addCode:"addCode"
            },
            success:function(result){
                newRow= "<tr>" +
                    "<td>" + "" + "</td>" +
                    "<td>" + "" + "</td>" +
                    "<td>" + result.code + "</td>" +
                    "<td>" + "" + "</td>" +
                    "<td>" + '<span type="button" id="detail" style="color:#00CC00" name='+result.isActivated+' id-column=' + result.id + ' class="fa fa-check-circle fa-2x" ' + '>' + ' </span>' + "</td>" +"" +
                    "</tr>";
              // myTable.row.add($(newRow)).draw();
                myTable.Rows.InsertAt(newRow, 0);

                $("#addCode1").modal('hide');

               //if(result=="success"){
               //    $("tbody").html("");
               //    myTable.fnDraw();
               //    $("#addCode1").modal('hide');
               //
               //}
            },
            error:function(){
                alert("error");
            }

        });

    });
}

function detailemei(){
    $(document).on("click", "#emei", function () {
        $('#emeimodal').modal('show');
        var emei=$("#emei").attr('emeis');
        $.ajax({
            url:"LicenseCodes",
            type:"POST",
            dataType:"json",
            data:{
                emei:emei,
                detailmodal:"detailmodal"
            },
            success:function(data){

                $("#emeipopup").text(data.imei);
                $("#devicenamepopup").text(data.deviceName);
                $("#modelpopup").text(data.model);
                $("#osversionpopup").text(data.osVersion);
                $("#osapilevelpopup").text(data.osApiLevel);
                $("#attacheddatepopup").text(data.attachedDate);
            },
            error:function(){
                alert("error");
            }

        });



    });
}



$(document).ready(function(){
    filter();
    detailemei();

    add();
    addCode();
    listLicenseCode();
    activated();
    dateFrom();
    dateTo();
});