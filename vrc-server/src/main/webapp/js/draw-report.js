var item;
var canvas ;
var context ;
var Val_Max;
var Val_Min;
var sections;
var xScale;
var yScale;
var y;
var width_column;
var graph;
var stepSize ;
var mang=[];
var items=[];
var x;

function init(itemName,itemValue,itemValue1) {
    // intialize values for each variables

    sections = itemValue.length;
    Val_Max = 100;
    stepSize = 10;
    var columnSize = 30;
    var rowSize = 30;
    var margin = 5;
    var header = "Score %" ;
    var phoneme="phoneme";
    width_column=0.2;
    //
    var tipCanvas = document.getElementById("tip");
    //var tipCtx = tipCanvas.getContext("2d");

    canvas = document.getElementById("canvas");
    context = canvas.getContext("2d");
    context.fillStyle = "#000;"

    yScale = (canvas.height - columnSize - margin*3) / (Val_Max);
    xScale = (canvas.width - rowSize) / (sections + 1);
    x=(rowSize+margin)/xScale;
    context.strokeStyle="#000;"; // background black lines
    context.beginPath();
    context.moveTo(rowSize,columnSize + (yScale * 10 * stepSize));
    context.lineTo(canvas.width,columnSize + (yScale * 10 * stepSize));
    context.stroke();
    context.beginPath();
    context.moveTo(rowSize,columnSize);
    context.lineTo(rowSize,columnSize + (yScale * 10 * stepSize));
    context.stroke();
    // column names
    context.font = "19 pt Arial;"
    context.fillText(header, 0,columnSize - margin*2);
    context.fillText(phoneme, canvas.width-2*rowSize,canvas.height - margin);
    // draw lines in the background
    context.font = "16 pt Helvetica"
    var count =  0;
    for (scale=Val_Max;scale>=0;scale = scale - stepSize) {
        y = columnSize + (yScale * count * stepSize);
        context.fillText(scale, margin,y);

        count++;
    }


    // print names of each data entry
    context.font = "20 pt Verdana";
    context.textBaseline="bottom";
    for (i=0;i<sections;i++) {
        computeHeight(itemValue[i]);
        context.fillText(itemName[i], xScale * (i*0.2+x),canvas.height);
    }

    // shadow for graph's bar lines with color and offset


    //context.shadowColor = 'rgba(128,128,128, 0.5)';

    //shadow offset along X and Y direction
    //context.shadowOffsetX = 9;
    //context.shadowOffsetY = 3;

    // translate to bottom of graph  inorder to match the data
    context.translate(0,canvas.height - margin*3);
    context.scale(xScale,-1 * yScale);

    // draw each graph bars
    for (i=0;i<sections;i++) {
        var h=itemValue[i];
        var g=itemValue1[i];
        if(h<g){
            context.fillStyle='#000066';
            context.fillRect(i*0.2+x, 0, width_column, h);
            mang.push({
                x1:(i*0.2+x)*xScale,
                y1: canvas.height - margin*3-h*yScale,
                x2: (i*0.2+x)*xScale+width_column*xScale,
                y2: canvas.height - margin*3
            })
            items.push(h);

            context.fillStyle='#3366ff';
            context.fillRect(i*0.2+x, 0+h, width_column, g-h);
            mang.push({
                x1:(i*0.2+x)*xScale,
                y1: canvas.height - margin*3-h*yScale-g*yScale,
                x2: (i*0.2+x)*xScale+width_column*xScale,
                y2: canvas.height - margin*3-h*yScale
            })
            items.push(g);
        }else{
            context.fillStyle='#3366ff';
            context.fillRect(i*0.2+x, 0, width_column, g);
            mang.push({
                x1:(i*0.2+x)*xScale,
                y1: canvas.height - margin*3-g*yScale,
                x2: (i*0.2+x)*xScale+width_column*xScale,
                y2: canvas.height - margin*3
            })
            items.push(g);
            context.fillStyle='#000066';
            context.fillRect(i*0.2+x, 0+g, width_column, h-g);
            mang.push({
                x1:(i*0.2+x)*xScale,
                y1: canvas.height - margin*3-g*yScale-h*yScale,
                x2: (i*0.2+x)*xScale + width_column*xScale,
                y2: canvas.height - margin*3-g*yScale
            })
            items.push(h);
        }

    }

    $("#tip, #canvas").mousemove(function(e){
        computeMouseOverEvent(e);

    });


}

function computeMouseOverEvent(e) {
    if(handleMouseMove(e)){
        var left = parseInt(e.clientX);
        var top = parseInt(e.clientY) - $("#tip").height();
        if(Number(item)>80){
            $("#tip").css('background-color','green');
        }else if(Number(item)<45){
            $("#tip").css('background-color','red');
        }else{
            $("#tip").css('background-color','orange');
        }
        $("#tip").text(item);
        $("#tip").css('color','white');
        $("#tip").css('width','35');
        $("#tip").css('height','20');
        $("#tip").css('text-align','center');
        $("#tip").css('top',top);
        $("#tip").css('left', left);
        $("#tip").show();
    } else {
        $("#tip").hide();
    }
}

function computeHeight(value) {
    y = canvas.height - value * yScale ;
}
function handleMouseMove(e) {
    mouseX = parseInt(e.clientX) - $(canvas).offset().left;
    mouseY = parseInt(e.clientY) - $(canvas).offset().top;
    //console.log("===============================");
    for(var i=0;i<mang.length;i++){
        //console.log("--------" + i + " --------");
        //console.log(mang[i]);
        if(mang[i].x1<=mouseX && mang[i].x2>=mouseX && mang[i].y1<=mouseY && mang[i].y2>=mouseY){
            item=items[i];
            return true;
        }
    }
    //console.log(mouseX + " | " + mouseY);
    return false;



}
function draw(Course,Level,Obj,name,date,idLesson){
    var $html=$("<div id='"+idLesson+"' class='col-sm-12 grap'> " +
    "<div id='info' class='col-sm-2'> " +
    "<p><strong>course:</strong></p> " +
    "<label style='font-weight: 200;'>"+Course+"</label> " +
    "<p><strong>level:</strong></p> " +
    "<label style='font-weight: 200;'>"+Level+"</label> " +
    "<p><strong>objective:</strong></p> " +
    "<label style='font-weight: 200;'>"+Obj+"</label> " +
    "<p><strong>lesson:</strong></p> " +
    "<label style='font-weight: 200;'>"+name+"</label> " +
    "<p><strong>completion date:</strong></p> " +
    "<label style='font-weight: 200;'>"+date+"</label> " +
    "</div> " +
    "<div id='score' class='col-sm-2'> " +
    "<div class='row'><div class='col-sm-6 scoreStudent' title='student score'><label>100</label></div></div>" +
    "<div class='row'><div class='col-sm-8 scoreClass' title='class average score'><label>100</label></div></div>" +
    "</div>" +
    "<div id='drawPhonemes' class='col-sm-4'> " +
    "<canvas id='canvas' height='300' width='300'></canvas> " +
    "<div id='tip'>Tooltips!</div> " +
    "</div> " +
    "<div id='drawWord' class='col-sm-4'> " +
    "<canvas id='canvasWord' height='300' width='300'></canvas> " +
    "<div id='tipWord'>Tooltips!</div> " +
    "<div id='tipWord'>Tooltips!</div> " +
    "</div> " +
    "</div>");
    return $html;
}

