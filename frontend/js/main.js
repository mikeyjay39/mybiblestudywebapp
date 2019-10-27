var url = "http://localhost:8080/biblestudy";
var viewCode = "6e9e6366-f386-11e9-b633-0242ac110002";

function make_base_auth(user, password) {
    var tok = user + ':' + password;
    var hash = btoa(tok);
    return "Basic " + hash;
}

function getChapter() {

    var book = $("#book").val();
    var chapterNo = $("#chapter").val();
    var apiEndPoint = url + "/" + viewCode + "/" + book + "/" + chapterNo;

    $.ajax({
        url: apiEndPoint,
        type: "GET",
        datatype: "application/json; charset=utf-8",
        beforeSend: function (xhr){
            xhr.setRequestHeader("Authorization", "Basic " + btoa("admin@admin.com:12345"));
        },
        success: function (data, status) {

            var returnedBook = data.book;
            var verseOutput = "";
            var noteOutput = "";
            var versesId = $("#verses");
            var verses = data.verses;
            var notes = data.notes;
            var size = verses.length;

            // iterate through verses
            for (var i = 0; i < size; i++) {
                verseOutput += "<sup>" + verses[i].verseNr + "</sup>" + verses[i].verse;
            }

            // iterate through notes
            for (var i = 0; i < notes.length; i++) {
                noteOutput += "<strong>v" + notes[i].verse + "</strong>: " + notes[i].note + "</br>";
            }

            if (notes.length <= 0) {
                noteOutput = "<em>No notes available for this chapter</em>"
            }

            versesId.html(verseOutput);
            $("#notes").html(noteOutput);
        },
        error: function (xhr, ajaxOptions, thrownError) { //Add these parameters to display the required response
            alert(xhr.status);
            alert(xhr.responseText);
        },
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true
    });
}

function testGet() {
    var book = $("#book").val();
    var chapterNo = $("#chapter").val();
    var apiEndPoint = url + "/" + viewCode + "/" + book + "/" + chapterNo;


    $.ajax({
        url: apiEndPoint,
        type: "GET",
        datatype: "application/json; charset=utf-8",
        /*beforeSend: function (xhr){
            xhr.setRequestHeader("Authorization", "Basic " + btoa("admin@admin.com:12345"));
        },*/
        success: function (data, status) {
            var returnedBook = data.book;
            var verseOutput = "";
            var noteOutput = "";
            var versesId = $("#verses");
            var verses = data.verses;
            var notes = data.notes;
            var size = verses.length;

            // iterate through verses
            for (var i = 0; i < size; i++) {
                verseOutput += "<sup>" + verses[i].verseNr + "</sup>" + verses[i].verse;
            }

            // iterate through notes
            for (var i = 0; i < notes.length; i++) {
                noteOutput += "<strong>v" + notes[i].verse + "</strong>: " + notes[i].note + "</br>";
            }

            if (notes.length <= 0) {
                noteOutput = "<em>No notes available for this chapter</em>"
            }

            versesId.html(verseOutput);
            $("#notes").html(noteOutput);


        },
        error: function (xhr, ajaxOptions, thrownError) { //Add these parameters to display the required response
            alert(xhr.status);
            alert(xhr.responseText);
        },
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true
    });
}

function test() {

    //var cookie = JSON.parse($.cookie('XSRF-TOKEN'));


    var book = $("#book").val();
    var chapterNo = $("#chapter").val();
    var apiEndPoint = url + "/" + viewCode + "/" + book + "/" + chapterNo;

    var request = {
        viewCode:"6e9e6366-f386-11e9-b633-0242ac110002",
        book:book,
        chapterNo:chapterNo
    };

    var data = JSON.stringify(request);
    var token = getCsrf();
    var header = "X-CSRF-TOKEN";
    //var token = $("meta[name='_csrf']").attr("content");
    //var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        url: url,
        type: "POST",
        datatype: "json",
        contentType: "application/json",
        //headers: {'X-CSRF-TOKEN': token},
        data: data,
       /* beforeSend: function (xhr){
            xhr.setRequestHeader("Authorization", "Basic " + btoa("admin@admin.com:12345"));
            //xhr.setRequestHeader("X-CSRF-TOKEN", token);
        },*/
        /*beforeSend: function (xhr){
            xhr.setRequestHeader("X-CSRF-TOKEN", token);
        },*/
        success: function (data, status) {


            var returnedBook = data.book;
            var verseOutput = "";
            var noteOutput = "";
            var versesId = $("#verses");
            var verses = data.verses;
            var notes = data.notes;
            var size = verses.length;

            // iterate through verses
            for (var i = 0; i < size; i++) {
                verseOutput += "<sup>" + verses[i].verseNr + "</sup>" + verses[i].verse;
            }

            // iterate through notes
            for (var i = 0; i < notes.length; i++) {
                noteOutput += "<strong>v" + notes[i].verse + "</strong>: " + notes[i].note + "</br>";
            }

            if (notes.length <= 0) {
                noteOutput = "<em>No notes available for this chapter</em>"
            }

            versesId.html(verseOutput);
            $("#notes").html(noteOutput);
        },
        error: function (xhr, ajaxOptions, thrownError) { //Add these parameters to display the required response
            alert(xhr.status);
            alert(xhr.responseText);
        },
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true
    });
}

function getCsrf() {
    return getCookie('XSRF-TOKEN');
}

/**
 * Return Csrf cookie
 */
function processCsrf() {
    // get csrf token
    var csrfToken = getCookie('XSRF-TOKEN');
    if (csrfToken) {
        document.cookie = "csrfToken=" + csrfToken;
    }

    return csrfToken;
}


// return value of cookie
function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}