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
            xhr.setRequestHeader("Authorization", "Basic " + btoa("admin:12345"));
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