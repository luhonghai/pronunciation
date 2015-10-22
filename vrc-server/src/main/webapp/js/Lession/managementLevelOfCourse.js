/**
 * Created by CMGT400 on 10/5/2015.
 */
var myTable;
var servletName="ManagementLevelOfCourseServlet";

function listLevel(){
    var $selected=$("#level");
    var $listLevel=$("#accordion");
    var id=$("#idCourse").val();
    $('#level option[value!="-1"]').remove();
    $.ajax({
        "url": servletName,
        "type": "POST",
        "dataType":"json",
        "data": {
            action: "listLevel",
            id:id
        },
        success:function(data){
            var items=data.dataforDropdown;
            var listLevel=data.data;
            $(items).each(function(){
                var newOption = '<option color="'+this.color+'" id="'+this.id+'" value="' + this.name + '">' + this.name + '</option>';
                $selected.append(newOption);
            });
            $("#level").append($("#level option").remove().sort(function(a, b) {
                var at = $(a).text(), bt = $(b).text();
                return (at > bt)?1:((at < bt)?-1:0);
            }));
            $(listLevel).each(function(){
                var newOption = '  <div class="panel panel-default"> ' +
                        '<div class="panel-heading" style="background-color: '+this.color+'"> ' +
                            '<div class="row">' +
                                '<div class="col-sm-3">' +
                                    '<h4 class="panel-title"> ' +
                                    '<button class="btn btn-default" data-toggle="collapse" data-target="#'+this.id+'">' +
                                    ''+this.name+' </button> ' +
                                    '</h4> ' +
                                '</div>' +
                                '<div class="col-sm-2 pull-right"><button type="button" name="removeLevel" id_lv="'+this.id+'" class="btn btn-default removelv" value="yes" >Remove Level</button></div>' +
                            '</div>' +
                        '</div>' +
                        '<div id="'+this.id+'" class="panel-collapse collapse"> ' +
                            '<div class="panel-body">' +
                                '<div class="row">' +
                                    '<div class="col-sm-2"><button type="button" name="createObject" id="createObject" class="btn btn-default" value="yes" >Create Object</button></div>' +
                                    '<div class="col-sm-2 pull-right"><button type="button" name="createTest" id="createTest" class="btn btn-default" value="yes" >Create Test</button></div>' +
                               '</div>' +
                                '<div id="testAndObject">' +
                                '</div>'+
                            '</div> ' +
                        '</div> ' +
                    '</div>';
                $listLevel.append(newOption);
            });


        }

    });
}

function addLevel(){
    $(document).on("click","#addlevel", function(){
        var idLevel=$("#level option:selected").attr('id');
        var idCourse=$("#idCourse").val();

        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "json",
            data: {
                action: "addLevel",
                idLevel:idLevel,
                idCourse:idCourse
            },
            success: function (data) {
                if(data.message.indexOf("success")!=-1){
                    $("#accordion").empty();
                    listLevel();
                }else{
                    swal("Error!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });


    });

}
function removeLevel(){
    $(document).on("click",".removelv", function(){
        var idLevel=$(this).attr('id_lv');
        var idCourse=$("#idCourse").val();

        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "json",
            data: {
                action: "delete",
                idLevel:idLevel,
                idCourse:idCourse
            },
            success: function (data) {
                if(data.message.indexOf("success")!=-1){
                    $("#accordion").empty();
                    listLevel();
                }else{
                    swal("Error!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });


    });

}

    //load phonemes click



$(document).ready(function(){
    removeLevel();
    addLevel();
    listLevel();
});


