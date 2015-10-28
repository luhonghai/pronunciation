/**
 * Created by CMGT400 on 10/8/2015.
 */
function buildTable(){
    myTable = $('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "responsive": true,
        "bProcessing": true,
        "bServerSide": true,

        "ajax": {
            "url": servletName,
            "type": "POST",
            "dataType": "json",
            "data": {
                action: "list",
                CreateDateFrom: $("#CreateDateFrom").val(),
                CreateDateTo: $("#CreateDateTo").val()
            }
        },

        "columns": [{
            "sWidth": "10%",
            "data": "type",
            "sDefaultContent": ""
        },{
            "sWidth": "10%",
            "data": "indexingType",
            "bSortable": false,
            "sDefaultContent": ""
        },{
            "sWidth": "10%",
            "data": "ipa",
            "bSortable": false,
            "sDefaultContent": ""
        },{
            "sWidth": "10%",
            "data": "arpabet",
            "bSortable": false,
            "sDefaultContent": ""
        }, {
            "sWidth": "20%",
            "data": "dateCreated",
            "sDefaultContent": ""
        }, {
            "sWidth": "5%",
            "data": null,
            "bSortable": false,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
            return '<label type="text" style="background-color: ' + data.color + '; margin-right:10px; width:100px; height:30px;">'+'</label>';
            }
        },{
            "sWidth": "3%",
            "data": null,
            "bSortable": false,
            "sDefaultContent":"",
            "mRender": function (data, type, full) {
                if (typeof data.mp3Url!='undefined' && data.mp3Url!=null && data.mp3Url.length>0) {
                    var audioUrl = data.mp3Url;
                    $divs = $('<div id="' + data.id + '" class="cp-jplayer">' + '</div>' +
                        '<div class="prototype-wrapper"> ' +
                        '<div id="' + data.id + "s" + '" class="cp-container">' +
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
                        '<li>' + '<a class="cp-play" tabindex="1">' + 'play' + '</a>' + '</li>' +
                        '<li>' + '<a class="cp-pause" style="display:none;" tabindex="1">' + 'pause' + '</a>' + '</li>' +
                        '</ul>' +
                        '</div>');
                    $divs.attr("audioUrl", audioUrl);
                    return $("<div/>").append($divs).html();
                    //return '<i class="fa fa-file-audio-o fa-2x"></i>';
                }
            }
        },{
            "sWidth": "20%",
            "data": null,
            "bSortable": false,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                $button = $('<button type="button" style="margin-right:10px" id="edit" class="btn btn-info btn-sm editMap">' + 'Edit' + '</button>' + '<button style="margin-right:10px" type="button" id="delete" class="btn btn-info btn-sm">' + ' Delete' + '</button>');
                $button.attr("id-column", data.id);
                return $("<div/>").append($button).html();
            }
        }]

    });


}
