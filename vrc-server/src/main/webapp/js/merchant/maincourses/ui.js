/**
 * Created by lantb on 2016-02-02.
 */


function buildCourse(dto){
    var $lblCourse = $("<label>");
    $lblCourse.addClass("lbl_cname");
    $lblCourse.html(dto.nameCourse);

    var $lblInfor = $("<label>");
    $lblInfor.addClass("lbl_cinfor");
    $lblInfor.html(dto.state + " by " + dto.companyName + " " + dto.dateCreated);

    var $link = $("<a>");
    $link.addClass("btn btn-info");
    $link.css("background-color", dto.backgroundColor);
    $link.css("text-color", dto.textColor);
    $link.attr("title",dto.descriptionCourse);
    $link.append($lblCourse);
    $link.append($lblInfor);

    var $div = $("<div>");
    $div.addClass("col-lg-12 pull-left");
    $div.css("padding-bottom", "10px");
    $div.append($link);

    getDivContainCourse().append($div);

}