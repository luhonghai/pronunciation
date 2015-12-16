var myTable;
function listUsers(){

    myTable=$('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "responsive": true,
        "bProcessing": true,
        "bServerSide": true,
        "bScrollCollapse": true,
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
            "sWidth": "25%",
            "data": "username",
            "sDefaultContent":""

        }, {
            "sWidth": "20%",
            "data": "name",
            "sDefaultContent":""
        },{
            "sWidth": "4%",
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
            "sWidth": "13%",
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
            "sWidth": "7%",
            "data": "dob",
            "sDefaultContent":""
        },{
            "sWidth": "7%",
            "data": "createdDate",
            "sDefaultContent":""
        }, {
            "sWidth": "10%",
            "data": "country",
            "sDefaultContent":""
        }, {
            "sWidth": "5%",
            "data": "activationCode",
            "sDefaultContent":""
        }, {
            "sWidth": "2%",
            "data": null,
            "bSortable": false,
            "sDefaultContent":"",
            "mRender": function (data, type, full) {
                if (data.isActivated == true) {
                    return '<span type="button" id="detail" style="color:#FF0000" title="Click to deactivate" name='+data.isActivated+'  id-column=' + data.id + ' class="fa fa-times-circle fa-2x" ' + full[0] + '>' + ' </span>';
                }else if(data.isActivated==false){
                    return '<span type="button" id="detail" style="color:#00CC00" title="Click to activate" name='+data.isActivated+' id-column=' + data.id + ' class="fa fa-check-circle fa-2x" ' + full[0] + '>' + ' </span>';
                }
            }
        },{
            "sWidth": "2%",
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
        window.location.href = "user-manage.jsp?username=" + user;
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
            },
            error:function(e){
                swal("Error!", "Could not connect to server", "error");
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

    user();
    filter();
    listUsers();
    activated();
    dateFrom();
    dateTo();
});
