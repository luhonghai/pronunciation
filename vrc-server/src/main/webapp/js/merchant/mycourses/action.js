/**
 * Created by lantb on 2016-02-02.
 */

function makeOnceCheckboxSelected(){
    $('input[type="checkbox"]').on('change', function() {
        $('input[type="checkbox"]').not(this).prop('checked', false);
    });
}

function saveCourse(){
    $(document).on("click","#btnSaveCourse", function(){
        if(validateFormAddCourse()){
            addCourse();
        }
    });

}

function openPopup(){
    $(document).on("click","#open_popup",function(){
        clearFormAdd();
        getPoupAdd().modal('show');
    });
}

function closeDropdown(){
    $('#adv-search').find('.open').removeClass('open');
}

function openSearchDetail(){
    $('.dropdown').on('shown.bs.dropdown', function(){
       clearFormSearch();
    });
}

function searchHeader(){
    $(document).on("click","#btnSearchHeader", function(){
        searchCourseHeader();
    });
}


function searchDetail(){
    $(document).on("click","#btnSearchDetail", function(){
        searchCourseDetail();
    });
}


function showTopHelp(){
    $("#help-icons").show();
}

function clickTopHelp(){
    $(document).on("click","#help-icons", function(){
        getPopUpHelp().find(".modal-body").empty();
        getPopUpHelp().find(".modal-title").html("my courses");
        getPopUpHelp().find(".modal-body").html(initHelpTopDataMyCourse());
        getPopUpHelp().modal('show');
    });
}

function clickHelpAddCourse(){
    $(document).on("click","#helpAddCourse", function(){
        getPopUpHelp().find(".modal-title").html("course management");
        getPopUpHelp().find(".modal-body").empty();
        getPopUpHelp().find(".modal-body").html(initHelpAddCourse());
        getPopUpHelp().modal('show');
    });
}

function reloadSearch(){
    $(document).on("click","#reload", function(){
        loadAllCourse();
    });
}

function collapseMenu(){
    $("#li-courses").find('ul').addClass('in');
}
function focusInput(){
    $('.modal').on('shown.bs.modal', function() {
        $(this).find('input:first').focus();
    });
}
$(document).ready(function(){
    initDate();
    makeOnceCheckboxSelected();
    saveCourse();
    clickHelpAddCourse();
    openPopup();
    loadAllCourse();
    openSearchDetail();
    searchHeader();
    searchDetail();
    showTopHelp();
    clickTopHelp();
    reloadSearch();
    collapseMenu();
    focusInput();
});
