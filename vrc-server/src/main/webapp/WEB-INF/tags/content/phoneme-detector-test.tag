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
    function detectPhoneme(url, word) {
        $.ajax({
            url:"PhonemeDetector",
            type:"POST",
            data:{
                url:url,
                word:word
            },
            success:function(data){
                drawPhonemeBreakDown(JSON.parse(data));
            },
            error:function(){
                swal("Error!", "Could not connect to server", "error");
            }

        });
    }

    function drawPhonemeBreakDown(result) {
        var $container = $("#phonemeDetectorContainer");
        var $canvas = $('<canvas id="phonemeDetectorChart" width="' + $container.width() + '" height="400"></canvas>');
        $container.empty();
        $container.append($canvas);
        var data = {
            labels: [],
            datasets:[]
        };
        var ampSet = {
            label: "Max amplitude",
            fillColor: "rgba(220,220,220,0.2)",
            strokeColor: "rgba(220,220,220,1)",
            pointColor: "rgba(220,220,220,1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(220,220,220,1)",
            data: []
        };
        var scoreSet = {
            label: "Phoneme score",
            fillColor: "rgba(151,187,205,0.2)",
            strokeColor: "rgba(151,187,205,1)",
            pointColor: "rgba(151,187,205,1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(151,187,205,1)",
            data: []
        };


        var phonemeScores = result.data.phonemeScores;
        for (var i = 0; i < phonemeScores.length; i++) {
            var phonemeScore = phonemeScores[i];
            var name = phonemeScore.name;
            var totalScore = phonemeScore.totalScore;
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
                            data.labels.push(name + "[" + pName + "] - " + extra.collectTime + "ms");
                            // Append score
                            scoreSet.data.push(totalScore);
                            // Append amp
                            ampSet.data.push(extra.maxAmp);
                        }
                    }


                }
            }
        }
        data.datasets.push(ampSet);
        data.datasets.push(scoreSet);
        var options = {

        };
        var ctx = $("#phonemeDetectorChart").get(0).getContext("2d");
        var lineChart = new Chart(ctx).Line(data, options);
    }
</script>