$(function() {

    var isNilaiNull = false;
    $('.nilaiSubfaktoruji').each(function(i, obj) {
        var value = $(this).val();
    // nilai pada tag option tidak bisa null, namun pada controller 0.01 = null
    if (value == 0.01) {
        $(this).css("background-color","white");
        $(this).css("border-color","#0072BB");
        $(this).css("border-width","2px");
        $(this).css("color","#0072BB")
        $(this).css("font-weight","bold");
        isNilaiNull = true;
    }

    else if (value == 1.00) {
        $(this).css("background-color","#0072BB");
        $(this).css("border-color","#0072BB");
        $(this).css("color","white");
    }

    else{
        $(this).css("background-color","#F16522");
        $(this).css("border-color","#F16522");
        $(this).css("color","white");
    }
    });

    var isPerfect = isTotalScorePerfect();
    if(isPerfect){
        $('#btnButton').show();
        $('#btnSubmitPenilaian').hide();
        $('#btnNull').hide();
    }
    else{
        if (isNilaiNull){
            $('#btnNull').show();
            $('#btnSubmitPenilaian').hide();
        }
        else {
            $('#btnSubmitPenilaian').show();
            $('#btnNull').hide();
        }
        $('#btnButton').hide();
    }


    $("#btnButton").click(function (event) {
         //stop submit the form, we will post it manually.
        event.preventDefault();

        // Get form
        var form = $('form')[0];

        // Create an FormData object
        var data = new FormData(form);

        // Post URL
        var urlPost = $("form").attr("action");
        // disabled the submit button
        $("#btnButton").prop("disabled", true);

        $.ajax({
            type: "POST",
            // enctype: 'multipart/form-data',
            url: urlPost,
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 2000,
            success: function (data) {
                $("#output-error").text("");
                $("#btnButton").prop("disabled", false);
                $("#successModal").modal("show")
            },
            error: function (e) {
                console.log("ERROR: ", e);
                $("#btnButton").prop("disabled", false);
            }
        });
    });

});


function validateForm() {
    var isValid = true;
    $('.form-control').each(function() {
        if ( $(this).val() === '0.01' ) {
            isValid = false;
        }
    });
    return isValid;
}

function isTotalScorePerfect() {
    var isPerfect = true;
    $('.nilaiSubfaktoruji').each(function(i, obj) {
        var value = $(this).val();

        if (value != "1.00"){
            isPerfect = false;
            return isPerfect;
        }
    });
    return isPerfect;
}

function isThereNull() {
    var isThereNull = false;
    $('.nilaiSubfaktoruji').each(function(i, obj) {
        var value = $(this).val();

        if (value == "0.01"){
            isThereNull = true;
            return isThereNull;
        }
    });
    return isThereNull;
}

$(".nilaiSubfaktoruji").change(function(){
    if ($(this).val() == "0.01") {
        $(this).css("background-color","white");
        $(this).css("border-color","#0072BB");
        $(this).css("border-width","2px");
        $(this).css("color","#0072BB")
        $(this).css("font-weight","bold");
        $('#btnNull').show();
        $('#btnButton').hide();
        $('#btnSubmitPenilaian').hide();
    }

    else if ($(this).val() == 1.00) {
        $(this).css("background-color","#0072BB");
        $(this).css("border-color","#0072BB");
        $(this).css("color","white");
        var isPerfect = isTotalScorePerfect();
        if(isPerfect){
            $('#btnButton').show();
            $('#btnSubmitPenilaian').hide();
            $('#btnNull').hide();
        }
        else{
            if (isThereNull()){
                $('#btnNull').show();
                $('#btnSubmitPenilaian').hide();
            }
            else {
                $('#btnSubmitPenilaian').show();
                $('#btnNull').hide();
            }
            $('#btnButton').hide();
        }

    }

    else{
        $(this).css("background-color","#F16522");
        $(this).css("border-color","#F16522");
        $(this).css("color","white");
        if (isThereNull()){
            $('#btnNull').show();
            $('#btnSubmitPenilaian').hide();
        }
        else {
            $('#btnSubmitPenilaian').show();
            $('#btnNull').hide();
        }
        $('#btnButton').hide();
    }

});


