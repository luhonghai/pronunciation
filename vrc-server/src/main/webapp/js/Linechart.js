$(document).ready(function(){

    drawChart();

});
function drawChart(){
    var Data = {
        labels : ["1","2","3","4","5","6"],
        datasets : [
            {
                fillColor : "rgba(172,194,132,0.4)",
                strokeColor : "#ACC26D",
                pointColor : "#fff",
                pointStrokeColor : "#9DB86D",
                data : [30,80,50,60,40,90]
            }
        ]
    }
    if ($("#word1").length > 0) {
        var buyer = document.getElementById('word1').getContext('2d');
        new Chart(buyer).Line(Data);
    }
}