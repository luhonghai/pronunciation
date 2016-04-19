/**
 * Created by lantb on 2016-04-14.
 */
var color_class_score = "#558ED5";
var color_student_score = "#17375E";
function drawReport(data){
    var data1 = [
        //[gd(2013, 1, 2), 40], [gd(2013, 1, 3), 59], [gd(2013, 1, 4), 60]
    ];
    var dataset = [
        {
            data: data,
            color:  color_student_score,
            points: { fillColor: "#FF0000", show: true },
            lines: { show: true }
        }
    ];

    var options = {
        series: {
            shadowSize: 5
        },
        xaxis: {
            mode: "time",
            timeformat: "%d/%m/%Y",
            tickSize : [1, "month"],
            min : new Date(combineMinDate()).getTime(),
            max : new Date(combineMaxDate()).getTime()
        },
        yaxis: {
            min : 0
        },
        grid: {
            hoverable: true,
            borderWidth: 3,
            mouseActiveRadius: 50,
            backgroundColor: { colors: ["#ffffff", "#EDF5FF"] },
            axisMargin: 20
        }
    };
    $.plot($("#placeholder"), dataset, options);
}

function showToolTip(x, y, contents, z){
    $('<div id="flot-tooltip">' + contents + '</div>').css({
        top: y - 100,
        left: x - 280,
        'border-color': z,
    }).appendTo("#holder-chart").show();
}

function mouseOverChart(){
    $("#placeholder").bind("plothover", function (event, pos, item) {
        if (item) {
            if (previousPoint != item.datapoint) {
                y = item.datapoint[1];
                z = item.series.color;
                $("#flot-tooltip").remove();
                showToolTip(item.pageX, item.pageY,
                  y , z);
            }
        } else {
            $("#flot-tooltip").remove();
            previousPoint = null;
        }
    });
}



function combineMinDate(){
    var dateMin =  $("#dateFrom").val();
    var d = dateMin.split("/")[0];
    var m = dateMin.split("/")[1];
    var y = dateMin.split("/")[2];
    var min = y + "-" + m +"-"+d;
    console.log(min);
    return min;
}

function combineMaxDate(){
    var dateMax =  $("#dateTo").val();
    var d = dateMax.split("/")[0];
    var m = dateMax.split("/")[1];
    var y = dateMax.split("/")[2];
    var max = y + "-" + m +"-"+d;
    console.log(max);
    return max;
}