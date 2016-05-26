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
            timeformat:"%d/%m/%Y",
            tickSize : [12, "month"],
            axisLabel: "Time",
            //min : min_x - 1000,
            //max : max_x + 1000
        },
        yaxis: {
            min : 0,
            max : 100
        },
        grid: {
            hoverable: true,
            borderWidth: 1,
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
    var min = $("#report-popup").attr("sdate");
    var max = $("#report-popup").attr("edate");
    var data = plot.getData();
    var axes = plot.getAxes();
    var offset = plot.getPlotOffset();
    var bottom = axes.yaxis.p2c(0)+offset.top;
    /*var min = $("#dateFrom").val();*/
    var min_series = data[0];
    var min_d = (min_series.data[0]);
    var minx = offset.left + axes.xaxis.p2c(min_d[0]);
    var miny = offset.top + axes.yaxis.p2c(min_d[1]);
    ctx.textAlign = 'center';
    //console.log(minx);console.log(miny);console.log(plot);
    ctx.fillText(min, offset.left,bottom +plot.height()/20);

  /*  var max = $("#dateTo").val();*/
    var max_series = data[data.length - 1];
    var max_d = (max_series.data[max_series.data.length-1]);
    var maxx = offset.left + axes.xaxis.p2c(max_d[0]);
    /*console.log(maxx); console.log(max_series);*/
    ctx.textAlign = 'center';
    ctx.fillText(max, offset.left + plot.width(),bottom +plot.height()/20);


}
function showToolTip(x, y, contents, z){
    $('<div id="flot-tooltip">' + contents + '</div>').css({
        top: y,
        left: x,
        'border-color': z,
        'z-index' : 20000
    }).appendTo("body").show();
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
