/**
 * Created by CMGT400 on 10/8/2015.
 */
function loadAudio(){
    $('.cp-jplayer').each(function() {
        var id = $(this).attr('id');
        var audioUrl = $(this).attr('audioUrl');
        new CirclePlayer("#" + id,
            {
                mp3: audioUrl,
                wav: audioUrl
            }, {
                cssSelectorAncestor: '#' + id + 's'
            });
        new CirclePlayer("#" + id + 't',
            {
                mp3: audioUrl,
                wav: audioUrl
            }, {
                cssSelectorAncestor: '#' + id + 'st'
            });

    });

}
function buildTable(){

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
            "url": servletName,
            "type": "POST",
            "dataType": "json",
            "data": {
                loadData: "list",
                CreateDateFrom: $("#CreateDateFrom").val(),
                CreateDateTo: $("#CreateDateTo").val()
            }
        },

        "columns": [{
            "sWidth": "10%",
            "data": "type",
            "sDefaultContent": ""
        },{
            "sWidth": "5%",
            "data": "indexingType",
            "bSortable": false,
            "sDefaultContent": ""
        },{
            "sWidth": "5%",
            "data": "ipa",
            "bSortable": false,
            "sDefaultContent": ""
        },{
            "sWidth": "10%",
            "data": "arpabet",
            "bSortable": false,
            "sDefaultContent": ""
        }, {
            "sWidth": "25%",
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
            "sWidth": "5%",
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
            "sWidth": "5%",
            "data": null,
            "bSortable": false,
            "sDefaultContent":"",
            "mRender": function (data, type, full) {
                if (typeof data.mp3UrlShort!='undefined' && data.mp3UrlShort!=null && data.mp3UrlShort.length>0) {
                    var audioUrl = data.mp3UrlShort;
                    $divs = $('<div id="' + data.id + "t" + '" class="cp-jplayer">' + '</div>' +
                    '<div class="prototype-wrapper"> ' +
                    '<div id="' + data.id + "ts" + '" class="cp-container">' +
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
            "sWidth": "30%",
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

