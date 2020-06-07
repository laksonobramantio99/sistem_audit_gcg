$(function() {

    $('[data-toggle="tooltip"]').tooltip();
    var url = window.location.href; // http://localhost:8080/lembarkerja/2019/1
    var mainPath = url.split("/")[3]; // lembarkerja
    var tahun = url.split("/")[4]; // 2019

    $("#sidebar ul li").removeClass("active");

    if(mainPath == "periode"){
        $(".periode").addClass("active");
    }
    else if(mainPath == "lembarkerja"){
        $(".lembarkerja").addClass("active");
    }
    else if(mainPath == "user" || mainPath == "divisi" || mainPath == "role"){
        $(".akun").addClass("active");
    }
    else if(mainPath == "submisi"){
        $(".lembarkerja").addClass("active");
    }
    else if(mainPath == "delegasi"){
        $(".delegasi").addClass("active");
    }
    else if(mainPath == "dashboard"){
        $(".dashboard").addClass("active");
    }

    var apiAllAspek = "http://" + url.split("/")[2] + "/api/lembarkerja/"  + tahun + "/getAllAspek";
    $.getJSON( apiAllAspek, function( data ) {
        var indexAspek = parseInt(url.split("/")[5]) - 1;
        var index = 0;
        $('.daftarAspek').append(
          "<a class='periodePenilaian'><p id='periodePenilaian' class='Subtitle-orange'><span></span></p></a>"
        );
        $('#periodePenilaian').text("Periode Penilaian : " + tahun);
        console.log("aspek " + data)
        $.each( data, function( key, val ) {
            var aspek = key.slice(6);
            var nilai = val.split(" ")[0];
            var no_aspek = val.split(" ")[1];
            
            $('.daftarAspek').append(
                "<a href='/lembarkerja/2019/1' class='submenuAspek " + aspek + "'></a>"
            );
            $("<span>" + key + "</span>").appendTo("." + aspek );
            $("<span><button style='font-size: x-small; font-weight: bold' type='button' class='btn font-weight-bold btn-orange p-1 ml-3' >" + nilai + "</span>").appendTo("." + aspek);
            index++;
            $("." + aspek ).attr("href","/lembarkerja/" + tahun + '/' + no_aspek);
        });
        

        if (mainPath == "lembarkerja"){
            $(".submenuAspek:eq("+indexAspek+")").addClass("active");
        }

        else {
            $(".submenuAspek").hide();
        }

    });



});
