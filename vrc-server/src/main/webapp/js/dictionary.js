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
            "url": CONTEXT_PATH + "/dictionary",
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
            "sWidth": "20%",
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
            "sWidth": "20%",
            "bSortable": true,
            "data": "createdDate",
            "sDefaultContent": ""
        },
            {
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

$(document).ready(function(){
    drawTable();
    $("#btnPopup").click(function() {
        $("#popupGenerate").modal("show");
    });
    $("#btnGenerate").click(function() {

    });
    $("#fileuploader").uploadFile({
        url:CONTEXT_PATH + "dictionary_upload",
        fileName:"dictionary",
        maxFileCount: 1,
        allowedTypes: "dic,dict",
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
                "url": CONTEXT_PATH + "/dictionary",
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
                "url": CONTEXT_PATH + "/dictionary",
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

});
