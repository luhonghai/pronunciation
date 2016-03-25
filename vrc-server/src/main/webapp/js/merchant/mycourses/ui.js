/**
 * Created by lantb on 2016-02-02.
 */


function buildCourse(dto){
    var $lblCourse = $("<label>");
    $lblCourse.addClass("lbl_cname");
    $lblCourse.html(dto.nameCourse);

    var $lblInfor = $("<label>");
    $lblInfor.addClass("lbl_cinfor");
    if(dto.state == "duplicated" || dto.state == "edited"){
        $lblInfor.html(dto.state + " by " + dto.companyName + " " + dto.dateCreated.split(" ")[0]);
    }else{
        $lblInfor.html(dto.sr + " "  + dto.dateCreated.split(" ")[0]);
    }

    var $link = $("<a>");
    $link.addClass("btn btn-info");
    $link.css("background-color", dto.backgroundColor);
    $link.css("color", dto.textColor);
    $link.css("padding-bottom", "0px");
    $link.css("border-radius", "5px");
    $link.css("border-color","transparent");
    $link.attr("title",dto.descriptionCourse);
    $link.attr("href",dto.pageLink);

    $link.append($lblCourse);
    $link.append($lblInfor);
    if(dto.status!= "publish" ){
        var imgUnPb = $('<img>');
        imgUnPb.attr('src', '/images/treeview/unpublished_button.gif');
        imgUnPb.attr('width', '24px');
        imgUnPb.attr('height', '24px');
        imgUnPb.css('margin-left', '10px');
        $link.append(imgUnPb);
    }

    var $div = $("<div>");
    $div.addClass("col-lg-12 pull-left");
    $div.css("padding-bottom", "10px");
    $div.css("padding-left", "0px");
    $div.append($link);

    getDivContainCourse().append($div);

}