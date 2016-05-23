var myTable;

function drawTable(){
    myTable = $('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "responsive": true,
        "bProcessing": true,
        "bServerSide": true,
        "order": [[ 0, "desc" ]],
        "ajax": {
            "url": CONTEXT_PATH + "/database",
            "type": "POST",
            "dataType": "json",
            "data": {
                action: "list"
            }
        },

        "columns": [{
            "sWidth": "8%",
            "data": "version",
            "bSortable": true,
            "sDefaultContent": ""

        }, {
            "sWidth": "20%",
            "data": "admin",
            "bSortable": true,
            "sDefaultContent": ""
        }, {
            "data": null,
            "sWidth": "15%",
            "bSortable": true,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                var $btn = $(document.createElement("button"));
                $btn.attr("type", "button");
                $btn.attr("data-id", data.id);
                $btn.attr("data-loading-text", "downloading...");
                $btn.attr("class", "btn btn-primary btn-xs btn-download-lm");
                $btn.attr("autocomplete", "off");
                $btn.html(data.fileName);
                return $("<div/>").append($btn).html();
            }
        }, {
            "data": null,
            "sWidth": "10%",
            "bSortable": true,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                if (typeof data.type != 'undefined' && data.type != null && data.type == 1) {
                    return "new";
                } else {
                    return "";
                }
            }
        },{
            "sWidth": "15%",
            "bSortable": false,
            "data": "lessonChange",
            "sDefaultContent": ""
        }, {
            "sWidth": "20%",
            "bSortable": false,
            "data": "titleNotification",
            "sDefaultContent": ""
        }, {
            "sWidth": "20%",
            "bSortable": true,
            "data": "createdDate",
            "sDefaultContent": ""
        },{
                "sWidth": "20%",
                "bSortable": true,
                "data": "selectedDate",
                "sDefaultContent": ""
            },
            {
                "data": null,
                "sWidth": "12%",
                "bSortable": false,
                "sDefaultContent": "",
                "mRender": function (data, type, full) {
                    if (!data.selected) {
                        var $btn = $(document.createElement("button"));
                        $btn.attr("type", "button");
                        $btn.attr("data-id", data.id);
                        $btn.attr("data-loading-text", "downloading...");
                        $btn.attr("class", "btn btn-primary btn-xs btn-select");
                        $btn.attr("autocomplete", "off");
                        $btn.html("set default");
                        return $("<div/>").append($btn).html();
                    } else {
                        return $("<div/>").append("default version").html();
                    }
                }
            }]

    });
}

var drawCount = 0;
var checkStatusTimeout;
var latestStatus;

function checkStatus() {
    $.ajax({
        "url": CONTEXT_PATH + "/database",
        type: "GET",
        dataType: "json",
        data: {
            action: "status",
            draw: drawCount++,
            lines: 15
        },
        success: function (data) {
            if (data != null && typeof data != 'undefined') {
                var $popup = $("#btnPopup");
                var $popupGenerate = $("#btnPopupGenerate");
                var $btnStop = $("#btnStop");
                if (data.running) {
                    $popup.prop("disabled","disabled");
                    $popupGenerate.prop("disabled","disabled");
                    $btnStop.show();
                } else {
                    $popup.prop("disabled",false);
                    $popupGenerate.prop("disabled",false);
                    $btnStop.hide();
                }
                if (data.stopping) {
                    $btnStop.prop("disabled","disabled");
                } else {
                    $btnStop.prop("disabled",false);
                }
                var $log = $("#generate-log");
                if (typeof data.latestLog != 'undefined' && data.latestLog.length > 0) {
                    var lines = data.latestLog.split("\n");
                    var logHTMl = [];
                    logHTMl.push("<span>...</span><br/>");
                    for (var i = lines.length - 1; i >= 0; i--) {
                        var span = document.createElement("span");
                        var text = lines[i];
                        if (text.indexOf("ERROR") != -1) {
                            $(span).css("color", "red");
                        }
                        $(span).text(text);
                        logHTMl.push($("<div/>").append(span).append("<br/>").html());
                    }
                    $log.html(logHTMl.join(""));
                } else {
                    $log.html("");
                }
                $("#generate-log").animate({ scrollTop: $('#generate-log')[0].scrollHeight}, 100);
                if (typeof latestStatus != 'undefined' && latestStatus != null) {
                    if (!data.running && latestStatus.running) {
                        myTable.fnDraw();
                    }
                }
            }
            latestStatus = data;
            checkStatusTimeout = setTimeout(checkStatus, 1000);
        },
        error: function (data) {
            checkStatusTimeout = setTimeout(checkStatus, 1000);
        }

    });
}


$(document).ready(function(){
    drawTable();
    $("#btnPopup").click(function() {
        $("#popupGenerate").modal("show");
        $("#lessonChange").empty();
        $("#notification").empty();

    });
    $("#btnPopupGenerate").click(function() {
        $("#popupGenerateAction").modal("show");
        $("#lessonChange").val("");
    });
    $("#btnStop").click(function() {
        $("#btnStop").prop("disabled","disabled");
        $.ajax({
            "url": CONTEXT_PATH + "/database",
            type: "GET",
            dataType: "text",
            data: {
                action: "stop"
            },
            success: function (data) {
                if (data.indexOf("done") != -1) {
                    $("#btnStop").prop("disabled",false);
                    $("#btnStop").hide();
                } else {
                    $("#btnStop").prop("disabled",false);
                }
            },
            error: function () {
                $("#btnStop").prop("disabled",false);
                swal("Error!", "Could not connect to server", "error");
            }
        });
    });
    $("#btnGenerate").click(function() {
        var $popup = $("#btnPopup");
        var $popupGenerate = $("#btnPopupGenerate");
        $popup.prop("disabled","disabled");
        $popupGenerate.prop("disabled","disabled");
        var lessonChange=$("#lessonChange").val();
        var notification=$("#notification").val();
        $("#popupGenerateAction").modal("hide");
        var $log = $("#generate-log");
        if(lessonChange!=null && lessonChange.length>0 && notification!=null && notification.length>0) {
            $log.html("Preparing. Please wait...");
            $.ajax({
                "url": CONTEXT_PATH + "/database",
                type: "GET",
                dataType: "text",
                data: {
                    action: "load",
                    lessonChange: lessonChange,
                    notification:notification,
                    type: $("#selType").val()
                },
                success: function (data) {
                    if (data.indexOf("done") != -1) {

                    } else {
                        $popup.prop("disabled", false);
                        $popupGenerate.prop("disabled", false);
                    }
                },
                error: function () {
                    $popup.prop("disabled", false);
                    $popupGenerate.prop("disabled", false);
                    swal("Error!", "Could not connect to server", "error");
                }

            });
        }else{
            swal("Warning!", "You can write lesson change and title notification.", "warning");
        }
    });
    $("#fileuploader").uploadFile({
        url:CONTEXT_PATH + "database_upload",
        fileName:"database",
        maxFileCount: 1,
        allowedTypes: "zip",
        onSuccess:function(files,data,xhr,pd)
        {
            myTable.fnDraw();
            $("#popupGenerate").modal("hide");
        }
    });
    $(document).click(function(e) {
        var $target = $(e.target);
        if ($target.hasClass("btn-download-lm")) {
            $target.removeClass("btn-danger");
            $target.addClass("btn-primary");
            $target.prop("disabled", "disabled");
            var dataId = $target.attr("data-id");
            $.ajax({
                "url": CONTEXT_PATH + "/database",
                type: "GET",
                dataType: "text",
                data: {
                    action: "link_generate",
                    id: dataId
                },
                success: function (data) {
                    if (data.indexOf("http") != -1) {
                        $('<iframe>').attr("id", "d-" + dataId)
                            .hide()
                            .attr('src', data)
                            .appendTo('body');
                    }
                    $target.button('reset');
                    $target.prop("disabled", false);
                },
                error: function () {
                    $target.addClass("btn-danger");
                    $target.removeClass("btn-primary");
                    $target.button('reset');
                    $target.prop("disabled", false);
                    swal("Error!", "Could not connect to server", "error");
                }

            });
        } else if ($target.hasClass("btn-select")) {
            $('.btn-select').prop("disabled","disabled");
            $target.removeClass("btn-danger");
            $target.addClass("btn-primary");
            var dataId = $target.attr("data-id");
            $.ajax({
                "url": CONTEXT_PATH + "/database",
                type: "POST",
                dataType: "text",
                data: {
                    action: "select",
                    id: dataId
                },
                success: function (data) {
                    $('.btn-select').prop("disabled", false);
                    myTable.fnDraw();
                    $target.button('reset');
                },
                error: function () {
                    $target.addClass("btn-danger");
                    $target.removeClass("btn-primary");
                    $target.button('reset');
                    $('.btn-select').prop("disabled", false);
                    swal("Error!", "Could not connect to server", "error");
                }

            });
        }
    });
    checkStatus();
});
