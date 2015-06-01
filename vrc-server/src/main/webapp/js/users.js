var myTable;
function listUsers(){

    myTable=$('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "bProcessing": true,
        "bServerSide": true,
        "scrollX": "100%",
        "scrollCollapse": true,
        "sScrollXInner": "140%",
        "ajax": {
            "url": "Users",
            "type": "POST",
            "dataType":"json",
            "data":{
                list:"list",
                username:$("#username").val() ,
                fullname:$("#fullname").val() ,
                gender:$("#gender").val() ,
                country:$("#country").val() ,
                Acti:$("#Acti").val() ,
                dateFrom:$("#dateFrom1").val(),
                dateTo: $("#dateTo1").val()
            }
        },
        "columns": [{

            "data": "username",
            "sDefaultContent":""

        }, {
            "data": "name",
            "sDefaultContent":""
        },{
            "data": null,
            "bSortable": false,
            "sDefaultContent":"",
            "mRender": function (data, type, full) {
                if(data.loginType=="googleplus") {
                    return '<i class="fa fa-google-plus-square fa-2x"></i>';
                }
                if(data.loginType=="facebook"){
                    return '<i class="fa fa-facebook-square fa-2x"></i>';
                }
                if(data.loginType=="easyaccent"){
                    return '<i class=""></i>';

                }
            }
        }, {
            "data": null,
            "bSortable": false,
            "sDefaultContent":"",
            "mRender": function (data, type, full) {
                if(data.gender==true) {
                    return '<i class="fa fa-male fa-2x"></i>';
                }
                if(data.gender==false){
                    return '<i class="fa fa-female fa-2x"></i>';
                }
            }
        }, {
            "data": "dob",
            "sDefaultContent":""
        }, {
            "data": "country",
            "sDefaultContent":""
        }, {
            "data": "activationCode",
            "sDefaultContent":""
        }, {
            "data": null,
            "bSortable": false,
            "sDefaultContent":"",
            "mRender": function (data, type, full) {
                if (data.isActivated == true) {
                    return '<button type="button" id="detail" name='+data.isActivated+'  id-column=' + data.id + ' class="showArchieved" ' + full[0] + '>' + ' <span class="glyphicon glyphicon-remove-sign" ></span>' + ' </button>';
                }else if(data.isActivated==false){
                    return '<button type="button" id="detail" name='+data.isActivated+' id-column=' + data.id + ' class="showArchieved" ' + full[0] + '>' + ' <span class="glyphicon glyphicon-ok-circle"></span>' + ' </button>';
                }
            }
        },{
            "data": null,
            "bSortable": false,
            "sDefaultContent":"",
            "mRender": function (data, type, full) {
                return '<a href="UserManage.jsp" class="btn btn-default btn-xs"><i class="fa fa-chevron-circle-right fa-2x"></i></a>';
            }
        },]
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
            "url": "Users",
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
            },
            error:function(e){
                alert(e);
            }

        });


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
            "url": "Users",
            "type": "POST",
            "dataType":"json",
            "data":{
                list:"list",
                username:$("#username").val() ,
                fullname:$("#fullname").val() ,
                gender:$("#gender").val() ,
                country:$("#country").val() ,
                Acti:$("#Acti").val() ,
                dateFrom:$("#dateFrom1").val(),
                dateTo: $("#dateTo1").val()
            }
        };
        $("tbody").html("");
        myTable.fnDraw();
    });

}


$(document).ready(function(){
    filter();
    listUsers();
    activated();
    dateFrom();
    dateTo();
});
