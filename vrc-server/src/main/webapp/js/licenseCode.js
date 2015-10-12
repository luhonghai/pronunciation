var myTable;
function listLicenseCode(){

    myTable=$('#dataTables-example').dataTable({
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
                        company:$("#companys").val(),
                        account:$("#account1").val() ,
                        Acti:$("#Acti").val() ,
                        dateFrom:$("#dateFrom1").val(),
                        dateTo: $("#dateTo1").val(),
                        dateFrom2:$("#dateFrom2").val(),
                        dateTo2: $("#dateTo2").val()
                    }
                },
                "columns": [{
                    "sWidth": "15%",
                    "data": "account",
                    "sDefaultContent":""

                }, {
                    "sWidth": "15%",
                    "data": null,
                    "bSortable": false,
                    "sDefaultContent":"",
                    "mRender": function (data, type, full) {
                        if(data.imei!=null) {
                            return '<i type="button" emeis='+data.imei+' id="emei"  class="fa fa-mobile fa-2x"  style="color: red; margin-right:10px;">'+'</i>' +  data.imei;
                        }
                    }
                },{
                    "sWidth": "5%",
                    "data": null,
                    "bSortable": false,
                    "sDefaultContent":"",
                    "mRender": function (data, type, full) {
                            return '<p style="font-family:tahoma">'+data.code+'</p>';
                    }
                },{
                    "sWidth": "15%",
                    "data": "company",
                    "sDefaultContent":""
                }, {
                    "sWidth": "20%",
                    "data": "createdDate",
                    "sDefaultContent":""
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
        var $selected=$("#company");
        $('#company option[value!="-1"]').remove();
        $.ajax({
            "url": "LicenseCodes",
            "type": "POST",
            "dataType":"json",
            "data": {
                listCompany: "listCompany"
            },
            success:function(data){
                var items=data;
                $(items).each(function(){
                    var newOption = '<option value="' + this.companyName + '">' + this.companyName + '</option>';
                    $selected.append(newOption);
                });
                $("#company").append($("#company option").remove().sort(function(a, b) {
                    var at = $(a).text(), bt = $(b).text();
                    return (at > bt)?1:((at < bt)?-1:0);
                }));


            }

        });
        $("#addCode1").modal('show');
    });
}

function dateFrom(){
    $('#dateFrom1').datetimepicker({
        format: 'YYYY/MM/DD'
    });
}
function dateTo(){
    $('#dateTo1').datetimepicker({
        format: 'YYYY/MM/DD'
    });
}

function dateFrom1(){
    $('#dateFrom2').datetimepicker({
        format: 'YYYY/MM/DD'
    });
}
function dateTo1(){
    $('#dateTo2').datetimepicker({
        format: 'YYYY/MM/DD'
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
                company:$("#companys").val(),
                account:$("#account1").val() ,
                Acti:$("#Acti").val() ,
                dateFrom:$("#dateFrom1").val(),
                dateTo: $("#dateTo1").val(),
                dateFrom2:$("#dateFrom2").val(),
                dateTo2: $("#dateTo2").val()
            }
        };
        $("tbody").html("");
        myTable.fnDraw();
    });

}

function addCode(){
    $(document).on("click","#Yes",function(){
        var newRow;
        var company=$("#company").val();
        var number=$("#numberoflicense").val();
        $.ajax({
            url:"LicenseCodes",
            type:"POST",
            dataType:"text",
            data:{
                addCode:"addCode",
                company:company,
                number:number
            },
            success:function(result){

               if(result=="success"){
                   $("tbody").html("");
                   $("#companys").empty();
                   listCompany();
                   myTable.fnDraw();
                   $("#addCode1").modal('hide');

               }
            },
            error:function(){
                swal("Error!", "Could not connect to server", "error");
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
                swal("Error!", "Could not connect to server", "error");
            }

        });



    });
}

function selected(){
    $(document).on("change","#companys", function(){
        myTable.fnSettings().ajax = {
            "url": "LicenseCodes",
            "type": "POST",
            "dataType":"json",
            "data":{
                list:"list",
                company:$("#companys").val(),
                account:$("#account1").val() ,
                Acti:$("#Acti").val() ,
                dateFrom:$("#dateFrom1").val(),
                dateTo: $("#dateTo1").val(),
                dateFrom2:$("#dateFrom2").val(),
                dateTo2: $("#dateTo2").val()
            }
        };
        $("tbody").html("");
        myTable.fnDraw();
    });

}

function listCompany(){
        var $selected=$("#companys");
        //$('#companys option[value!="-1"]').remove();
        $.ajax({
            "url": "ListCompanyServlet",
            "type": "POST",
            "dataType":"json",
            "data": {
                listCompany: "listCompany"
            },
            success:function(data){
                var items=data;
                $(items).each(function(){
                    var newOption = '<option value="' + this.company + '">' + this.company + '</option>';
                    $selected.append(newOption);
                });
                $("#companys").append($("#companys option").remove().sort(function(a, b) {
                    var at = $(a).text(), bt = $(b).text();
                    return (at > bt)?1:((at < bt)?-1:0);
                }));
                $selected.prepend("<option value=''></option>").val('');


            }

        });

}



$(document).ready(function(){
    listCompany();
    selected();
    filter();
    detailemei();
    add();
    addCode();
    listLicenseCode();
    activated();
    dateFrom();
    dateTo();
    dateFrom1();
    dateTo1();
});