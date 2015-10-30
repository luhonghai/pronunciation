/**
 * Created by CMGT400 on 10/8/2015.
 */

function buildSelectBox(data,idSelected){
    $('#select-course').empty();
    $.each(data, function(i, item) {
        var $option = $("<option>");
        $option.attr("value",item.id);
        $option.text(item.name);
        if(item.id === idSelected){
            $option.attr("selected","selected");
        }
        $('#select-course').append($option);
    });
    $('.loading').hide();
    $('#select-course').show();
    $('#select-course').multiselect();

}
