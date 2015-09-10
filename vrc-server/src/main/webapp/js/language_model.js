var myTable;

function drawTable(){
    myTable = $('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "responsive": true,
        "bProcessing": true,
        "bServerSide": true,

        "ajax": {
            "url": CONTEXT_PATH + "languagemodel",
            "type": "POST",
            "dataType": "json",
            "data": {
                action: "list"
            }
        },

        "columns": [{
            "sWidth": "5%",
            "data": "version",
            "sDefaultContent": ""

        }, {
            "sWidth": "20%",
            "data": "admin",
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
            "data": "createdDate",
            "sDefaultContent": ""
        },
            {
                "sWidth": "20%",
                "data": "selectedDate",
                "sDefaultContent": ""
            }]

    });
}

$(document).ready(function(){
    drawTable();
    $("#btnPopup").click(function() {
       $("#popupGenerate").modal("show");
    });
    $("#btnGenerate").click(function() {
        var $popup = $("#btnPopup");
        $popup.attr("disabled","disabled");
        $("#popupGenerate").modal("hide");
        var $log = $("#generate-log");
        $log.html("Generating. Please wait...");
        $log.load(CONTEXT_PATH + "languagemodel?action=load",function(){
            $popup.attr("disabled","");
            myTable.fnDraw();
        });
    });
    $(document).click(function(e) {
       var $target = $(e.target);
        if ($target.hasClass("btn-download-lm")) {
            $target.removeClass("btn-danger");
            $target.addClass("btn-primary");
            var dataId = $target.attr("data-id");
            $.ajax({
                "url": CONTEXT_PATH + "languagemodel",
                type: "GET",
                dataType: "text",
                data: {
                    add: "link_generate",
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
                },
                error: function () {
                    $target.addClass("btn-danger");
                    $target.removeClass("btn-primary");
                    $target.button('reset');
                }

            });
        }
    });

});
