/**
 * Created by lantb on 2016-04-14.
 */
var color_class_score = "#558ED5";
var color_student_score = "#17375E";
var previousPoint = null;
function convertDate(date){
    var year = date.split("-")[0];
    var month = date.split("-")[1];
    var day = date.split("-")[2];
    var tmp = day + "/" + month +"/"+year;
    return tmp;
}
function drawReport(data,ticks,sdates,edates){
    var dataset = [
        {
            data: data,
            color:  color_student_score,
            points: { fillColor: "#FF0000", show: true },
            lines: { show: true }
        }
    ];
    var ticksDate = [];
    var options = {
        series: {
            shadowSize: 5
        },
        xaxis: {
          mode: "time",
          axisLabel: "Time",
          tickSize : [16,"month"],
          timeformat: "%d/%m/%y",
          tick : ticks,
          min : new Date(sdates).getTime(),
          max : new Date(edates).getTime(),
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
    var min = convertDate($("#report-popup").attr("sdate"));
    var max = convertDate($("#report-popup").attr("edate"));
    var data = plot.getData();
    var axes = plot.getAxes();
    var offset = plot.getPlotOffset();
    var bottom = axes.yaxis.p2c(0)+offset.top;
    var min_series = data[0];
    var min_d = (min_series.data[0]);
    var minx = offset.left + axes.xaxis.p2c(min_d[0]);
    var miny = offset.top + axes.yaxis.p2c(min_d[1]);
    ctx.textAlign = 'center';
    ctx.fillText(min, offset.left +10,bottom +plot.height()/20);

    var max_series = data[data.length - 1];
    var max_d = (max_series.data[max_series.data.length-1]);
    var maxx = offset.left + axes.xaxis.p2c(max_d[0]);
    ctx.textAlign = 'center';
    ctx.fillText(max, offset.left + plot.width() - 10,bottom +plot.height()/20);
}
function showToolTip(x, y, date,score, bcolor){
    if(parseInt(score) >= 0 && parseInt(score) < 45){
        var contents = "";
        $('<div id="flot-tooltip">' + score + '</div>').css({
            top: y,
            left: x,
            'border-color': "red",
            'color' : 'white',
            'background-color': "red",
            'z-index' : 20000
        }).appendTo("body").show();
    }else if(parseInt(score) > 44 && parseInt(score) < 80){
        var contents = "";
        $('<div id="flot-tooltip">' + score + '</div>').css({
            top: y,
            left: x,
            'border-color': "#FF8C00",
            'color' : 'white',
            'background-color': "#FF8C00",
            'z-index' : 20000
        }).appendTo("body").show();
    }else {
        var contents = "";
        $('<div id="flot-tooltip">' + score + '</div>').css({
            top: y,
            left: x,
            'border-color': "#006400",
            'color' : 'white',
            'background-color': "#006400",
            'z-index' : 20000
        }).appendTo("body").show();
    }

}

function mouseOverChart(){
    $("#placeholder").bind("plothover", function (event, pos, item) {
        if (item) {
            if (previousPoint != item.datapoint) {
                y = item.datapoint[1];
                d = item.datapoint[0];
                z = item.series.color;
                $("#flot-tooltip").remove();
                showToolTip(item.pageX, item.pageY,
                    d,y , z);
            }
        } else {
            $("#flot-tooltip").remove();
            previousPoint = null;
        }
    });

    function convertDateToString(date){
        var d = new Date(date);
        var year = d.getFullYear();
        var month = d.getMonth() + 1;
        if(month < 10){
            month = "0"+month;
        }
        var day = d.getDate();
        if(day < 10) {
            day = "0"+day;
        }
        console.log(day + "/" +month +"/" +year);
        var tmp = day + "/" +month +"/" +year;
        return tmp;
    }
}
