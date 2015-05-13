/**
 * Created by CMGT400 on 5/4/2015.
 */


//$(document).ready(function(){
    google.load("visualization", "1", {packages: ["geochart"]});
    google.setOnLoadCallback(drawRegionsMap);

    function drawRegionsMap() {

        var data = google.visualization.arrayToDataTable([
            ['Country', 'Popularity'],
            ['Germany', 200],
            ['United States', 300],
            ['Brazil', 400],
            ['Canada', 500],
            ['France', 600],
            ['RU', 700],
            ['cu', 700],
            ['Tanzania', 900]

        ]);

        var options = {};

        var chart = new google.visualization.GeoChart(document.getElementById("geochart1"));

        chart.draw(data, options);

    }
//});
$(document).ready(function(){


    var buyerData = {
        labels : ["January","February","March","April","May","June"],
        datasets : [
            {
                fillColor : "rgba(172,194,132,0.4)",
                strokeColor : "#ACC26D",
                pointColor : "#fff",
                pointStrokeColor : "#9DB86D",
                data : [203,156,589,251,305,247]
            }
        ]
    }
    if($("#buyers").length>0) {
        var buy = document.getElementById('buyers').getContext('2d');
        new Chart(buy).Line(buyerData);
    }
});