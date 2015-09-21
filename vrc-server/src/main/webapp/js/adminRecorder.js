var myTable;

function listTranscriptionRecorder(){

    myTable = $('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "responsive": true,
        "bProcessing": true,
        "bServerSide": true,
        "fnDrawCallback": function( oSettings ) {
                   loadAudio();
        },

        "ajax": {
            "url": "RecorderServlet",
            "type": "POST",
            "dataType": "json",
            "data": {
                action: "listbyadmin",
                accounts:$("#listaccount").val(),
                sentence: $("#sentence").val(),
                account:$("#account").val(),
                dateFrom:$("#dateFrom").val(),
                dateTo:$("#dateTo").val(),
                status:$("#status").val()
            }
        },

        "columns": [{
            "sWidth": "20%",
            "data": "account",
            "sDefaultContent": ""

        }, {
            "sWidth": "25%",
            "data": "sentence",
            "bSortable": false,
            "sDefaultContent": ""
        },{
            "sWidth": "3%",
            "data": null,
            "bSortable": false,
            "sDefaultContent":"",
            "mRender": function (data, type, full) {
                var audioUrl =CONTEXT_PATH + "/LoadAudioRecorder?id=" + data.id;
                $divs=$('<div id="'+data.id+'" class="cp-jplayer">' + '</div>' +
                '<div class="prototype-wrapper"> ' +
                    '<div id="'+data.id+"s"+'" class="cp-container">' +
                        '<div class="cp-buffer-holder"> ' +
                            '<div class="cp-buffer-1">' + '</div>' +
                            '<div class="cp-buffer-2">' + '</div>' +
                        '</div>' +
                        '<div class="cp-progress-holder">' +
                             '<div class="cp-progress-1">' + '</div>' +
                             '<div class="cp-progress-2">' + '</div>' +
                        '</div>' +
                        '<div class="cp-circle-control">' + '</div>' +
                        '<ul class="cp-controls">' +
                            '<li>'+'<a class="cp-play" tabindex="1">' + 'play' + '</a>' + '</li>' +
                            '<li>'+'<a class="cp-pause" style="display:none;" tabindex="1">' + 'pause' + '</a>' + '</li>' +
                        '</ul>' +
                    '</div>');
                $divs.attr("audioUrl", audioUrl);
                return $("<div/>").append($divs).html();
                //return '<i class="fa fa-file-audio-o fa-2x"></i>';
            }
        }, {
            "sWidth": "17%",
            "data": "modifiedDate",
            "sDefaultContent": ""
        }, {
            "sWidth": "5%",
            "data": null,
            "bSortable": false,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                console.log(data);
                if (data.status == 1) {
                    return '<span style="background-color: orange;color: white; padding:5px;">'+"Pending"+'</span>';
                }
                if (data.status == 2) {
                    return '<span style="background-color: red;color: white; padding:5px;">'+"Reject"+'</span>';
                }
                if (data.status == 3) {
                    return '<span style="background-color: green;color: white; padding:5px;">'+"Approved"+'</span>';
                }
                if (data.status == 4) {
                    return '<span style="background-color: darkgray;color: white; padding:5px;">'+"Locked"+'</span>';
                }
            }
        },{
            "sWidth": "30%",
            "data": null,
            "bSortable": false,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                if (data.status == 1) {
                    $button = $('<button type="button" style="margin-right:10px;background-color: red;color: white;" id="rejects" class="btn btn-sm" ' + full[0] + '>' + ' Reject' + '</button>' + '<button type="button" style="margin-right:10px;background-color: green;color: white;" id="approveds" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Approve' + '</button>' + '<button type="button" id="lockeds" style="background-color: darkgray;color: white;" class="btn btn-sm" ' + full[0] + '>' + ' Lock' + '</button>');
                    $button.attr("id-column", data.id);
                    $button.attr("account", data.account);
                    return $("<div/>").append($button).html();
                }
                if (data.status == 2) {
                    $button = $('<button type="button" style="margin-right:10px;background-color: green;color: white;" id="approveds" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Approve' + '</button>' + '<button type="button" id="lockeds" style="background-color: darkgray;color: white;" class="btn btn-sm" ' + full[0] + '>' + ' Lock' + '</button>');
                    $button.attr("id-column", data.id);
                    $button.attr("account", data.account);
                    return $("<div/>").append($button).html();
                }
                if (data.status == 3) {
                    $button = $('<button type="button" style="margin-right:10px;background-color: red;color: white;" id="rejects" class="btn btn-sm" ' + full[0] + '>' + ' Reject' + '<button type="button" id="lockeds" style="background-color: darkgray;color: white;" class="btn btn-sm" ' + full[0] + '>' + ' Lock' + '</button>');
                    $button.attr("id-column", data.id);
                    $button.attr("account", data.account);
                    return $("<div/>").append($button).html();
                }
                if (data.status == 4) {
                    $button = $('<button type="button" style="margin-right:10px;background-color: red;color: white;" id="rejects" class="btn btn-sm" ' + full[0] + '>' + ' Reject' + '</button>' + '<button type="button" style="margin-right:10px;background-color: green;color: white;" id="approveds" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Approve' + '</button>');
                    $button.attr("id-column", data.id);
                    $button.attr("account", data.account);
                    return $("<div/>").append($button).html();
                }
            }
        }]

    });


}

function dateFrom(){
    $('#dateFrom').datetimepicker({
        format: 'DD/MM/YYYY'
    });
}
function dateTo(){
    $('#dateTo').datetimepicker({
        format: 'DD/MM/YYYY'
    });
}

function searchAdvanted(){
    $(document).on("click","#buttonFilter", function(){
        myTable.fnSettings().ajax = {
            "url": "RecorderServlet",
            "type": "POST",
            "dataType": "json",
            "data": {
                action: "listbyadmin",
                accounts:$("#listaccount").val(),
                sentence: $("#sentence").val(),
                account:$("#account").val(),
                dateFrom:$("#dateFrom").val(),
                dateTo:$("#dateTo").val(),
                status:$("#status").val()
            }
        };
        $("tbody").html("");
        myTable.fnDraw();



    });
}

function loadNumber(){
    var $selected=$("#listaccount");
    $('#listaccount option[value!="-1"]').remove();
    $.ajax({
        "url":"RecorderServletNumber",
        "type": "POST",
        "dataType":"json",
        "data":{
            loadNumber:"load"
        },
        success:function(data){
            var items=data.recordedSentences;

            $(items).each(function(){
                var newOption = '<option value="' + this.account + '">' + this.account + '</option>';
                $selected.append(newOption);
            });
            $("#listaccount").append($("#listaccount option").remove().sort(function(a, b) {
                var at = $(a).text(), bt = $(b).text();
                return (at > bt)?1:((at < bt)?-1:0);
            }));
            $selected.prepend("<option value=''></option>").val('');

            $("#awaiting").text(data.waiting);
            $("#pending").text(data.pending);
            $("#reject").text(data.reject);
            $("#approved").text(data.approved);
            $("#locked").text(data.locke);
            $("#all").text(data.allsentence);



        }
    });
}

function selected(){
    $(document).on("change","#listaccount", function(){
        var account=$("#listaccount").val();
        loadNumber();

        myTable.fnSettings().ajax = {
            "url": "RecorderServlet",
            "type": "POST",
            "dataType": "json",
            "data": {
                action: "listbyadmin",
                accounts:$("#listaccount").val(),
                sentence: $("#sentence").val(),
                account:$("#account").val(),
                dateFrom:$("#dateFrom").val(),
                dateTo:$("#dateTo").val(),
                status:$("#status").val()
            }
        };
        $("tbody").html("");
        myTable.fnDraw();

    });


}

function reject(){
    $(document).on("click","#rejects", function(){
        var id=$(this).attr('id-column');
        var accounts=$(this).attr('account');
        var $button=$('<button type="button" style="margin-right:10px;background-color: green;color: white;" id="approveds" class="btn btn-info btn-sm">' + ' Approve' + '</button>' + '<button type="button" id="lockeds" style="background-color: darkgray;color: white;" class="btn btn-sm">' + ' Lock' + '</button>');
        $button.attr("id-column", id);
        $button.attr("account", accounts);
        var $row = $(this).closest("tr");
        var account=$("#listaccount").val();
        $.ajax({
            "url": "ChangeStatusRecorder",
            "type": "POST",
            "dataType": "text",
            "data": {
                reject: "reject",
                id:id

            },
            success:function(data) {
                if (data == "success") {
                    //$el.hide();
                    $row.find('td:eq(5)').html("")
                    $row.find('td:eq(5)').html($button);
                    $row.find('td:eq(4)').html("<span>Reject</span>");
                    $row.find('td:eq(4) span').css({"background-color": "red","color": "white", "padding": "5px"});
                }

            }
        });

        $.ajax({
            "url":"RecorderServletNumber",
            "type": "POST",
            "dataType":"json",
            "data":{
                account:account,
                loadNumberAccount:"load"
            },
            success:function(data){
                $("#awaiting").text(data.waiting);
                $("#pending").text(data.pending);
                $("#reject").text(data.reject);
                $("#approved").text(data.approved);
                $("#locked").text(data.locke);
                $("#all").text(data.allsentence);



            }
        });



    });
}
function approved(){
    $(document).on("click","#approveds", function(){
        var id=$(this).attr('id-column');
        var accounts=$(this).attr('account');
        var $button=$('<button type="button" style="margin-right:10px;background-color: red;color: white;" id="rejects" class="btn btn-sm">' + ' Reject' + '<button type="button" id="lockeds" style="background-color: darkgray;color: white;" class="btn btn-sm">' + ' Lock' + '</button>');
        $button.attr("id-column", id);
        $button.attr("account", accounts);
        var $row = $(this).closest("tr");
        var account=$("#listaccount").val();
       $.ajax({
            "url": "ChangeStatusRecorder",
            "type": "POST",
            "dataType": "text",
            "data": {
                approved: "approved",
                id:id

            },
            success:function(data){
                if (data == "success") {
                    $row.find('td:eq(5)').html("")
                    $row.find('td:eq(5)').html($button);
                    $row.find('td:eq(4)').html("<span>Approved</span>");
                    $row.find('td:eq(4) span').css({"background-color": "green","color": "white", "padding": "5px"})
                }

            }
        });

        $.ajax({
            "url":"RecorderServletNumber",
            "type": "POST",
            "dataType":"json",
            "data":{
                account:account,
                loadNumberAccount:"load"
            },
            success:function(data){
                $("#awaiting").text(data.waiting);
                $("#pending").text(data.pending);
                $("#reject").text(data.reject);
                $("#approved").text(data.approved);
                $("#locked").text(data.locke);
                $("#all").text(data.allsentence);



            }
        });




    });
}
function locked(){
    $(document).on("click","#lockeds", function(){
        var $row = $(this).closest("tr");
        var id=$(this).attr('id-column');
        var account=$("#listaccount").val();
        $.ajax({
            "url": "ChangeStatusRecorder",
            "type": "POST",
            "dataType": "text",
            "data": {
                locked: "locked",
                id:id

            },
            success:function(data){
                if (data == "success") {
                    $row.find('td:eq(4)').html("<span>Locked</span>");
                    $row.find('td:eq(4) span').css({"background-color": "darkgray","color": "white", "padding": "5px"});
                    $row.find('td:eq(5)').html("")
                }

            }
        });
        $.ajax({
            "url":"RecorderServletNumber",
            "type": "POST",
            "dataType":"json",
            "data":{
                account:account,
                loadNumberAccount:"load"
            },
            success:function(data){
                $("#awaiting").text(data.waiting);
                $("#pending").text(data.pending);
                $("#reject").text(data.reject);
                $("#approved").text(data.approved);
                $("#locked").text(data.locke);
                $("#all").text(data.allsentence);



            }
        });


    });
}

function loadAudio(){
    $('.cp-jplayer').each(function() {
        var id = $(this).attr('id');
        var audioUrl = $(this).attr('audioUrl');
        new CirclePlayer("#" + id,
            {
                m4a: audioUrl
            }, {
                cssSelectorAncestor: '#' + id + 's'
            });

    });
}
$(document).ready(function(){

    reject();
    approved();
    locked();
    dateFrom();
    dateTo();
    selected();
    loadNumber();
    listTranscriptionRecorder();
    searchAdvanted();
});



