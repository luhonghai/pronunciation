var myTable;
function listUsers(){

    myTable=$('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "bProcessing": true,
        "bServerSide": true,
        "sScrollX": "100%",
        "bScrollCollapse": true,
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
                    return '<i class="fa fa-google-plus-square fa-2x"  style="color:#CC0000" ></i>';
                }
                if(data.loginType=="facebook"){
                    return '<i class="fa fa-facebook-square fa-2x"  style="color:#3366CC"></i>';
                }
                if(data.loginType=="easyaccent"){
                    //return '<i src="accenteasy.com/favicon.ico"></i>';
                    return '<img src="http://www.accenteasy.com/favicon.ico">';

                }
            }
        }, {
            "data": null,
            "bSortable": true,
            "sDefaultContent":"",
            "mRender": function (data, type, full) {
                if(data.gender==true) {
                    return '<i class="fa fa-male fa-2x" style="color:#0088CC"></i>';
                }
                if(data.gender==false){
                    return '<i class="fa fa-female fa-2x" style="color:#FF3399" ></i>';
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
                    return '<span type="button" id="detail" style="color:#FF0000" name='+data.isActivated+'  id-column=' + data.id + ' class="fa fa-times-circle fa-2x showArchieved" ' + full[0] + '>' + ' </span>';
                }else if(data.isActivated==false){
                    return '<span type="button" id="detail" style="color:#00CC00" name='+data.isActivated+' id-column=' + data.id + ' class="fa fa-check-circle fa-2x showArchieved" ' + full[0] + '>' + ' </span>';
                }
            }
        },{
            "data": null,
            "bSortable": false,
            "sDefaultContent":"",
            "mRender": function (data, type, full) {
                return '<button type="button" id="user" user=' + data.username + '>' + ' <i class="fa fa-chevron-circle-right"></i>' + ' </button>';
            }
        },]
    });

}

function user(){
    $(document).on("click","#user",function() {
        var user = $(this).attr('user');
        window.location.href = "UserManage.jsp?username=" + user;
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

function button(){
    var visible=$("#roles").val();
    if(visible=="1"){
        $("#server").show();
    }
}
$(document).ready(function(){
    button();
    user();
    filter();
    listUsers();
    activated();
    dateFrom();
    dateTo();
});
