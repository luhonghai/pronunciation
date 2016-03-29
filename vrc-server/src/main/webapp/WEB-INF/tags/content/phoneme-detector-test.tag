<style>
    .modal-dialog-fixed {
        margin: 30px auto;
        width: 800px;
    }

    .modal-dialog-fixed {
        position: relative;
        width: auto;
        margin: 10px;
    }
</style>
<div id="phonemeDetectorDialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog-fixed modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Phoneme breakdown</h4>
            </div>
            <div id="phonemeDetectorContainer" class="modal-body">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<script>
    var nbPhoneme;
    var beepPhonemes;
    var cGreen = '#579e11';
    var cOrange = '#ff7548';
    var cRed = '#ff3333';
    var cPurple = '#7030a0';
    var cBlue = '#003da7';
    function isBeepPhonemes(phoneme, testPhoneme) {
        var test = beepPhonemes[phoneme];
        return (typeof test != 'undefined'
        && test != null
        && test.length > 0
        && test.toUpperCase() == testPhoneme.toUpperCase());
    }

    function testNbPhoneme(phoneme, testPhoneme) {
        var returnVal = 0;
        if (phoneme == testPhoneme) {
            returnVal = 1;
        }
        var mBeepPhoneme = beepPhonemes[testPhoneme];
        var mPhoneme = testPhoneme;
        if (typeof mBeepPhoneme != 'undefined' && mBeepPhoneme != null && mBeepPhoneme.length > 0) {
            mPhoneme = mBeepPhoneme;
        }
        if (typeof nbPhoneme != 'undefined' && nbPhoneme != null) {
            var phonemes = nbPhoneme[mPhoneme.toUpperCase()];
            if (typeof phonemes != 'undefined' && phonemes != null && phonemes.length > 0) {
                for (var i = 0; i < phonemes.length; i++) {
                    if (phoneme.toUpperCase() == phonemes[i].toUpperCase()) {
                        returnVal = 2;
                        break;
                    }
                }
            }
        }
        return returnVal;
    }
    $(document).ready(function() {
        console.log("Init LineWithLine");
        Chart.types.Line.extend({
            name: "LineWithLine",
            draw: function () {
                Chart.types.Line.prototype.draw.apply(this, arguments);

                var lines = this.options.limitLines;



                for (var i = lines.length; --i >= 0;) {

                    var xStart = Math.round(this.scale.xScalePaddingLeft);
                    var linePositionY = this.scale.calculateY(lines[i].value);

                    this.chart.ctx.fillStyle = lines[i].color ? lines[i].color : this.scale.textColor;
                    this.chart.ctx.font = this.scale.font;
                    this.chart.ctx.textAlign = "left";
                    this.chart.ctx.textBaseline = "top";

                    if (this.scale.showLabels && lines[i].label) {
                        this.chart.ctx.fillText(lines[i].label, xStart + 5, linePositionY);
                    }

                    this.chart.ctx.lineWidth = this.scale.gridLineWidth;
                    this.chart.ctx.strokeStyle = lines[i].color ? lines[i].color : this.scale.gridLineColor;

                    if (this.scale.showHorizontalLines) {
                        this.chart.ctx.beginPath();
                        this.chart.ctx.moveTo(xStart, linePositionY);
                        this.chart.ctx.lineTo(this.scale.width, linePositionY);
                        this.chart.ctx.stroke();
                        this.chart.ctx.closePath();
                    }

                    this.chart.ctx.lineWidth = this.lineWidth;
                    this.chart.ctx.strokeStyle = this.lineColor;
                    this.chart.ctx.beginPath();
                    this.chart.ctx.moveTo(xStart - 5, linePositionY);
                    this.chart.ctx.lineTo(xStart, linePositionY);
                    this.chart.ctx.stroke();
                    this.chart.ctx.closePath();
                }

                var hLines = this.options.limitHorizontalLines;
                for(var i = 0; i < hLines.length; i++) {
                    var index = hLines[i].value;
                    var point = this.datasets[0].points[index];
                    var scale = this.scale;
                    // draw line
                    this.chart.ctx.beginPath();
                    this.chart.ctx.moveTo(point.x, scale.startPoint + 24);
                    this.chart.ctx.strokeStyle = hLines[i].color;
                    this.chart.ctx.lineTo(point.x, scale.endPoint);
                    this.chart.ctx.stroke();
                    // write label
                    this.chart.ctx.textAlign = 'center';
                    this.chart.ctx.fillText(hLines[i].label, point.x, scale.startPoint + 12);
                }
            }
        });
    });
    function detectPhoneme(url, word) {
        $.ajax({
            url:"PhonemeDetector",
            type:"POST",
            data:{
                url:url,
                word:word
            },
            success:function(data){
                drawPhonemeBreakDown(JSON.parse(data), word, url);
            },
            error:function(){
                swal("Error!", "Could not connect to server", "error");
            }

        });
    }

    function drawPhonemeBreakDown(result, word, url) {
        var data = {
            labels: [],
            datasets:[]
        };
        var ampSet = {
            label: "Max amplitude",
            fillColor: "rgba(31,113,128,0.1)",
            strokeColor: "rgba(31,113,128,0.5)",
            pointColor: "rgba(31,113,128,.5)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(31,113,128,.5)",
            data: []
        };
        var scoreSet = {
            label: "Phoneme score",
            fillColor: "rgba(112,48,160,0.1)",
            strokeColor: "rgba(112,48,160,.5)",
            pointColor: "rgba(112,48,160,.5)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(112,48,160,.5)",
            data: []
        };

        var frequencyeSet = {
            label: "Frequency",
            fillColor: "rgba(0,61,167,0.1)",
            strokeColor: "rgba(0,61,167,.5)",
            pointColor: "rgba(0,61,167,.5)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(0,61,167,.5)",
            data: []
        };
        var options = {
            limitLines: [],
            limitHorizontalLines: []
        };

        var phonemeScores = result.data.phonemeScores;
        var index = 0;
        for (var i = 0; i < phonemeScores.length; i++) {
            var phonemeScore = phonemeScores[i];
            var name = phonemeScore.name;
            var totalScore = Math.floor(phonemeScore.totalScore);
            var phonemes = phonemeScore.phonemes;
            if (phonemes.length > 0) {
                for (var j = 0; j < phonemes.length; j++) {
                    var phoneme = phonemes[j];
                    var type = phoneme.type;
                    var pName = phoneme.name;

                    var extras = phoneme.extras;
                    if (extras.length > 0) {
                        for (var k = 0; k < extras.length; k++) {
                            var extra = extras[k];

                            // Append label
                            data.labels.push(name + " [" + pName + "] - " + extra.collectTime + "ms");
                            // Append score
                            scoreSet.data.push(totalScore);
                            // Append amp
                            ampSet.data.push(extra.maxAmp);
                            // Append frequency
                            frequencyeSet.data.push(extra.frequency);
                            index++;
                        }
                    }


                }
                var color = '';
                if (totalScore >= 80) {
                    color = '#579e11';
                } else if (totalScore >= 45) {
                    color = '#ff7548';
                } else {
                    color = '#ff3333';
                }
                options.limitHorizontalLines.push({
                    label: name,
                    color: color,
                    value: index - 1
                })
            } else {
                // Append label
                //data.labels.push(name + "[]");
                // Append score
                //scoreSet.data.push(totalScore);
                // Append amp
                //ampSet.data.push(0);
                // Append frequency
                //frequencyeSet.data.push(0);
            }

        }
        var avgScore = result.data.score;
        var totalAmp = 0;
        var totalFreq = 0;
        for (var i = 0; i < ampSet.data.length; i++) {
            totalAmp += ampSet.data[i];
        }
        var avgAmp = totalAmp / ampSet.data.length;
        for (var i = 0; i< frequencyeSet.data.length; i++) {
            totalFreq += frequencyeSet.data[i];
        }
        var avgFreq = totalFreq / frequencyeSet.data.length;

        data.datasets.push(ampSet);
        data.datasets.push(frequencyeSet);
        data.datasets.push(scoreSet);
        options.limitLines = [
            {
                label: "",//"Avg score",
                value: avgScore,
                color: 'rgba(112,48,160, .8)'
            },
            {
                label: "",//"Avg amplitude",
                value: avgAmp,
                color: 'rgba(31,113,128, .8)'
            },
            {
                label: "",//"Avg frequency",
                value: avgFreq,
                color: 'rgba(0,61,167, .8)'
            }
        ];

        var $container = $("#phonemeDetectorContainer");
        var $canvas = $('<canvas id="phonemeDetectorChart" width="' + ($container.width() - 60) + '" height="400"></canvas>');
        $container.empty();
        $container.append("<p><b>Word: </b>" + word + "</p>");

        var $player = $('<div id="jquery_jplayer_1" class="cp-jplayer"></div>'
                +'<div id="cp_container_1" class="cp-container">'
                +'<div class="cp-buffer-holder">'
                +'<div class="cp-buffer-1"></div>'
                +'<div class="cp-buffer-2"></div>'
                +'</div>'
                +'<div class="cp-progress-holder">'
                +'<div class="cp-progress-1"></div>'
                +'<div class="cp-progress-2"></div>'
                +'</div>'
                +'<div class="cp-circle-control"></div>'
                +'<ul class="cp-controls">'
                +'<li><a class="cp-play" tabindex="1">play</a></li>'
                +'<li><a class="cp-pause" style="display:none;" tabindex="1">pause</a></li>'
                +'</ul>'
                +'</div>');
        $container.append($player);

        new CirclePlayer("#jquery_jplayer_1",
                {
                    wav: CONTEXT_PATH + "/audio?url=" + encodeURIComponent(url)
                }, {
                    cssSelectorAncestor: "#cp_container_1",
                    keyEnabled: true
                });

        $container.append("<p><b style='color: #1f7180'>Avg amplitude: </b>" + Math.floor(avgAmp) + "</p>");
        $container.append("<p><b style='color: #003da7'>Avg frequency: </b>" + Math.floor(avgFreq) + " Hz</p>");
        $container.append("<p><b style='color: #7030a0'>Avg score: </b>" +  Math.floor(avgScore) + "</p>");

        $container.append("<hr/>");
        $container.append("<h3>General chart</h3>");
        $container.append($canvas);

        $container.append("<hr/>");
        $container.append("<h3>Breakdown</h3>");
        var $div = $('<div><div id="loadFrame" class="group-phoneme-weight" style="margin-top: 20px; margin-left: 15px; overflow-x: auto;overflow-y: hidden;">'
                +'<div class="row" id="alphabets"></div>'
                +'<div class="row" id="listAlpabet"></div>'
                +'<div class="row" id="count"></div>'
                +'<div class="row" id="totalScore"></div>'
                +'</div>'
                +'<div id="bestPhonemes" style="margin-top: 20px; margin-left: 15px;">'
                +'<div class="row" id="bestPhoneme"></div>'
                +'</div></div>');
        $container.append($div);
        generateBreakDown(result, word, url);



        var ctx = $("#phonemeDetectorChart").get(0).getContext("2d");
        new Chart(ctx).LineWithLine(data, options);
    }

    function generateBreakDown(data, word, url) {
        nbPhoneme = data.neighbourPhones;
        beepPhonemes = data.beepPhonemes;
        if (data.data != null) {
            var result = data.data.phonemeScores;
            var number=0;
            var bestPhoneme = data.data.rawBestPhonemes;
            for (var i = 0; i < result.length; i++) {
                var score = result[i].totalScore;
                var listFrame = result[i].phonemes;
                number=number+listFrame.length;
                $("#alphabets").append('<input id="' + i + '" readonly="readonly" index="' + result[i].index + '" value="' + result[i].name + '"  type="text">');
                if (listFrame.length > 0) {
                    for (var j = 0; j < listFrame.length; j++) {
                        var test = testNbPhoneme(listFrame[j].name, result[i].name);
                        $("#listAlpabet").append('<input id="' + i + '' + j + 't" readonly="readonly" index="' + listFrame[j].index + '" value="' + listFrame[j].name + '"  type="text">');
                        $("#count").append('<input class="count" readonly="readonly" index="' + listFrame[j].index + '" value="' + listFrame[j].count + '"  type="text">');
                        if (isBeepPhonemes(result[i].name, listFrame[j].name)) {
                            $("#" + i + +j + "t").css({
                                'width': '50px',
                                'font-weight': 'bold',
                                'text-align': 'center',
                                'color': cPurple
                            });
                        } else {
                            if (test == 0) {
                                $("#" + i + +j + "t").css({
                                    'width': '50px',
                                    'font-weight': 'bold',
                                    'text-align': 'center',
                                    'color': cRed
                                });
                            } else if (test == 1) {
                                $("#" + i + +j + "t").css({
                                    'width': '50px',
                                    'font-weight': 'bold',
                                    'text-align': 'center',
                                    'color': cGreen
                                });
                            } else {
                                $("#" + i + +j + "t").css({
                                    'width': '50px',
                                    'font-weight': 'bold',
                                    'text-align': 'center',
                                    'color': cOrange
                                });
                            }
                        }
                        $(".count").css({'width': '50px', 'font-weight': 'bold', 'text-align': 'center'});
                    }
                } else {
                    number=number+1;
                    $("#listAlpabet").append('<input class="listAlpabets" readonly="readonly" type="text">');
                    $("#count").append('<input class="counts" readonly="readonly" type="text">');
                    $(".listAlpabets").css('width', '50px');
                    $(".counts").css('width', '50px');
                }
                $("#totalScore").append('<input id="' + i + 's" readonly="readonly" index="' + result[i].index + '" value="' + result[i].totalScore + '"  type="text">');
                if (listFrame.length > 0) {
                    $("#" + i + "").css({"width": (listFrame.length) * 50, 'text-align': 'center'});
                    if (score < 45) {
                        $("#" + i + "s").css({
                            "width": (listFrame.length) * 50,
                            'text-align': 'right',
                            'color': cRed
                        });
                    } else if (45 <= score && score <= 80) {
                        $("#" + i + "s").css({
                            "width": (listFrame.length) * 50,
                            'text-align': 'right',
                            'color': cOrange
                        });
                    } else {
                        $("#" + i + "s").css({
                            "width": (listFrame.length) * 50,
                            'text-align': 'right',
                            'color': cGreen
                        });
                    }

                } else {
                    $("#" + i + "").css({'width': '50px', 'font-weight': 'bold', 'text-align': 'center'});
                    $("#" + i + "s").css({'width': '50px', 'font-weight': 'bold', 'text-align': 'right'});
                }
            }
            $("#totalScore").append('<input class="counts" readonly="readonly" type="text" value="Score =' + data.data.score + '">');
            $("#listAlpabet").css('width',number*50+200);
            $("#count").css('width',number*50+200);
            $("#alphabets").css('width',number*50+200);
            $("#totalScore").css('width',number*50+200);
            $("#bestPhoneme").empty();
            for (var i = 0; i < bestPhoneme.length; i++) {
              //  $("#bestPhoneme").append('<input id="' + i + 'b" readonly="readonly" value="' + bestPhoneme[i] + '" type="text"/><br/>');
            }
        }
    }
</script>