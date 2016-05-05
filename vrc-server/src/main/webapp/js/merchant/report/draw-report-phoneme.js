/**
 * Created by lantb on 2016-04-14.
 */
var color_class_score = "#558ED5";
var color_student_score = "#17375E";
function drawReport(data){
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
            timeformat:"%d/%m/%Y %H:%M:%S",
            tickSize : [12, "month"],
            axisLabel: "Time",
            //min : new Date(combineMinDate()).getTime(),
            //max : new Date(combineMaxDate()).getTime()
        },
        yaxis: {
            min : 0,
            max : 100
        },
        grid: {
            hoverable: true,
            borderWidth: 3,
            mouseActiveRadius: 50,
            backgroundColor: { colors: ["#ffffff", "#EDF5FF"] },
            axisMargin: 10
        }, hooks: {
            draw: [raw]
        }
    };
   var chart =  $.plot($("#placeholder"), dataset, options);
}
function raw(plot, ctx){
    var data = plot.getData();
    var axes = plot.getAxes();
    var offset = plot.getPlotOffset();
    var bottom = axes.yaxis.p2c(0)+offset.top;
    var min = $("#dateFrom").val();
    var min_series = data[0];
    var min_d = (min_series.data[0]);
    var minx = offset.left + axes.xaxis.p2c(min_d[0]);
    var miny = offset.top + axes.yaxis.p2c(min_d[1]);
    ctx.textAlign = 'center';
    console.log(minx);console.log(miny);console.log(plot);
    ctx.fillText(min, minx,bottom +plot.height()/20);

    var max = $("#dateTo").val();
    var max_series = data[data.length - 1];
    var max_d = (max_series.data[max_series.data.length-1]);
    var maxx = offset.left + axes.xaxis.p2c(max_d[0]);
    /*console.log(maxx); console.log(max_series);*/
    ctx.textAlign = 'center';
    ctx.fillText(max, maxx,bottom +plot.height()/20);

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