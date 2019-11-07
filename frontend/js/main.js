var url = "http://localhost:8080";
//var viewCode = "6e9e6366-f386-11e9-b633-0242ac110002";
var currentBook;
var currentChapter;
var currentBookId;
var currentChapterId;
var currentViewCode = "";
var currentNotes;
var currentUserId; // id of the currently logged in user
var selectedUser; // id of the selected user
var users; // list of user objects
var userViewCodes;
var chapterSize; // the number of verses in chapter

function make_base_auth(user, password) {
    var tok = user + ':' + password;
    var hash = btoa(tok);
    return "Basic " + hash;
}

function getService() {
    if (currentViewCode == "") {
        getChapterId();
    } else {
        getChapter();
    }
}

function getChapter() {

    var book = $("#book").val();
    var chapterNo = $("#chapter").val();
    var apiEndPoint = url + "/biblestudy/" + currentViewCode + "/" + book + "/" + chapterNo;

    $.ajax({
        url: apiEndPoint,
        type: "GET",
        datatype: "application/json; charset=utf-8",
        success: function (data, status) {

            var returnedBook = data.book;
            var verseOutput = "";
            var noteOutput = "";
            var versesId = $("#verses");
            var verses = data.verses;
            var notes = data.notes;
            var size = verses.length;
            currentBook = data.book;
            currentChapter = data.chapter;
            currentBookId = data.bookId;
            currentChapterId = data.chapterId;
            currentNotes = data.notes;

            // iterate through verses
            for (var i = 0; i < size; i++) {
                verseOutput += "<sup>" + verses[i].verseNr + "</sup>" + verses[i].verse;
            }

            // iterate through notes
            for (var i = 0; i < notes.length; i++) {
                noteOutput += '<div id="note' + i + '"<strong>' + notes[i].verseStart + '-' + notes[i].verseEnd + '</strong>: ' +
                    notes[i].noteText + '</br><button type="button" class="btn btn-sm btn-danger" ' +
                    'onclick="removeNote(' + i + ')">Remove</button><hr></br>';
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

function getChapterId() {

    var book = $("#book").val();
    var chapterNo = $("#chapter").val();
    var apiEndPoint = url + "/biblestudy/" + book + "/" + chapterNo;

    $.ajax({
        url: apiEndPoint,
        type: "GET",
        datatype: "application/json; charset=utf-8",
        success: function (data, status) {

            var returnedBook = data.book;
            var verseOutput = "";
            var verses = data.verses;
            chapterSize = verses.length;
            currentBook = data.book;
            currentChapter = data.chapter;
            currentBookId = data.bookId;
            currentChapterId = data.chapterId;

            // iterate through verses
            for (var i = 0; i < chapterSize; i++) {
                verseOutput += "<sup>" + verses[i].verseNr + "</sup>" + verses[i].verse;
            }


            $("#verses").html(verseOutput);
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

/**
 * Used to get Bible text and notes for the currently logged in user
 */
function getChapterUserNotes() {

    var book = $("#book").val();
    var chapterNo = $("#chapter").val();
    var endpoint = url + "/notes/mynotes/" + book + "/" + chapterNo + "/" + currentUserId;

    if (book != "" && chapterNo != "") {
        $.ajax({
            url: endpoint,
            type: "GET",
            datatype: "application/json; charset=utf-8",
            success: function (data, status) {

                var returnedBook = data.book;
                var verseOutput = "";
                var noteOutput = "";
                var versesId = $("#verses");
                var verses = data.verses;
                var notes = data.notes;
                var size = verses.length;
                currentBook = data.book;
                currentChapter = data.chapter;
                currentBookId = data.bookId;
                currentChapterId = data.chapterId;
                currentNotes = data.notes;

                // iterate through verses
                for (var i = 0; i < size; i++) {
                    verseOutput += "<sup>" + verses[i].verseNr + "</sup>" + verses[i].verse;
                }

                // iterate through notes
                for (var i = 0; i < notes.length; i++) {
                    noteOutput += '<div id="note' + i + '"<strong>' + notes[i].verseStart + '-' + notes[i].verseEnd + '</strong>: ' +
                        notes[i].noteText + '</br><button type="button" class="btn btn-sm btn-danger" ' +
                        'onclick="removeNote(' + i + ')">Remove</button><hr></br>';
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
}

function createUser() {

    var apiEndPoint = "http://localhost:8080/users/signup";

    var request = {
        email:"testingemail@testingthis.com",
        firstname:"Abe",
        lastname:"Lincoln",
        password:"testpassword"
    };

    var data = JSON.stringify(request);
    var token = getCsrf();
    var header = "X-CSRF-TOKEN";

    $.ajax({
        url: apiEndPoint,
        type: "POST",
        datatype: "json",
        contentType: "application/json",
        headers: {'X-XSRF-TOKEN': token},
        data: data,
        success: function (data, status) {

            alert('success');
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

function login() {
    var apiEndPoint = "http://localhost:8080/login";
    var email = $("#emailLoginField").val();
    var pass = $("#passwordField").val();
    $.ajax({
        url: apiEndPoint,
        type: "GET",
        datatype: "application/json; charset=utf-8",
        beforeSend: function (xhr){
            xhr.setRequestHeader("Authorization", "Basic " + btoa(email + ":" + pass));
            //xhr.setRequestHeader("Authorization", "Basic " + btoa("admin@admin.com:12345"));
        },
        success: function (data, status) {
            var userId = data.userId;
            successfulLogin();
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

function autoLogin() {
    var apiEndPoint = "http://localhost:8080/login";
    var email = $("#emailLoginField").val();
    var pass = $("#passwordField").val();
    $.ajax({
        url: apiEndPoint,
        type: "GET",
        datatype: "application/json; charset=utf-8",
        beforeSend: function (xhr){
            //xhr.setRequestHeader("Authorization", "Basic " + btoa(email + ":" + pass));
            xhr.setRequestHeader("Authorization", "Basic " + btoa("admin@admin.com:12345"));
        },
        success: function (data, status) {
            currentUserId = data.userId;
            successfulLogin();
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

function logout() {

    var logoutUrl = url + "/perform_logout";

    $.ajax({
        url: logoutUrl,
        type: "POST",
        datatype: "application/json; charset=utf-8",
        headers: {'X-XSRF-TOKEN': getCsrf()},
        success: function (data, status) {
            logoutHandler();
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

/**
 * Use to hide all body elements. Will not hide the sub navbar
 */
function hideAllBody() {
    $("#loginDiv").hide();
    $("#getChapterForm").hide();
    $("#verses").hide();
    hideRightContentDiv()
}

/**
 * Hide the content column next to verses
 */
function hideRightContentDiv() {
    $("#goButton").attr("onclick","getService()");
    $("#notes").hide();
    $("#createNote").hide();
    $("#manageViews").hide();
}

/**
 * Hides sub navbar and all body elements
 */
function hideAll() {
    $("#secondaryNavbar").hide();
    hideAllBody();
}

/**
 * Used to show the login form
 */
function showLoginForm() {
    hideAll();
    $("#loginDiv").show();
}

/**
 * Call this after successfully logging in
 */
function successfulLogin() {
    hideAll();
    $("#secondaryNavbar").show();
    $("#getChapterForm").show();
    $("#verses").show();
    $("#login").text("Logout");
    $("#login").attr("onclick","logout()");
}

/**
 * Call this after logging out or received an unauthorized response from server
 */
function logoutHandler() {
    showLoginForm();
    currentUserId = "";
    $("#login").text("Login");
    $("#login").attr("onclick","showLoginForm()");
}

function showCreateNote() {
    hideRightContentDiv();
    $("#createNote").show();
}

function showManageViews() {
    $("#viewsList").empty();
    hideRightContentDiv();
    getViewsForLoggedInUser();
    $("#manageViews").show();
}

function showManageNotes() {
    hideRightContentDiv();
    getChapterUserNotes();
    $("#notes").show();
    $("#goButton").attr("onclick","getChapterUserNotes()");
}

/**
 * Call this when adding a new note
 */
function createNote() {

    var endpoint = url + "/notes/add";

    var note = {
        noteText: $("#noteText").val(),
        bookId: currentBookId,
        chapterId: currentChapterId,
        verseStart: $("#verseStart").val(),
        verseEnd: $("#verseEnd").val(),
        priv: $("#privNote").val()
    };

    var data = JSON.stringify(note);
    var token = getCsrf();

    $.ajax({
        url: endpoint,
        type: "POST",
        datatype: "json",
        contentType: "application/json",
        headers: {'X-XSRF-TOKEN': token},
        data: data,
        success: function (data, status) {
            alert("note added");
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

/**
 * Call this to get the logged in user's views
 */
function getViewsForLoggedInUser() {
    var endpoint = url + "/views/get";

    $.ajax({
        url: endpoint,
        type: "GET",
        datatype: "application/json; charset=utf-8",
        success: function (data, status) {

            var size = data.viewCodes.length;
            userViewCodes = data.viewCodes;

            // iterate through view codes
            for (var i = 0; i < size; i++) {
                var vc = data.viewCodes[i];
                $("#viewsList").append(
                    '<li class="list-group-item" onclick="setCurrentViewCode(' + i + ')" onmouseover="" style="cursor: pointer;">' +
                    vc + '</li>'
                );
            }


            //$("#verses").html(verseOutput);
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

/**
 * Call this to add a new view
 */
function createView() {

    var endpoint = url + "/views/add";
    var token = getCsrf();

    $.ajax({
        url: endpoint,
        type: "POST",
        headers: {'X-XSRF-TOKEN': token},
        success: function (data, status) {

            alert('view added');
            $("#viewsList").empty();
            getViewsForLoggedInUser();

        },
        error: function (xhr, ajaxOptions, thrownError) { //Add these parameters to display the required response
            alert(xhr.status);
            alert(xhr.responseText);
            alert('did not create view')
        },
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true
    });
}

function setCurrentViewCode(i) {
    currentViewCode = userViewCodes[i];
    $("#currentViewCode").text(userViewCodes[i]);
    $("#notes").show();
}

function removeNote(i) {

    var endpoint = url + "/views/" + currentViewCode + "/" + currentNotes[i].noteId;
    var token = getCsrf();

    $.ajax({
        url: endpoint,
        type: "DELETE",
        headers: {'X-XSRF-TOKEN': token},
        success: function (data, status) {

            alert('note removed from view');
            $("#note" + i).hide();

        },
        error: function (xhr, ajaxOptions, thrownError) { //Add these parameters to display the required response
            alert(xhr.status);
            alert(xhr.responseText);
            alert('did not remove note from view')
        },
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true
    });
}

/**
 * Called when user clicks on delete current view
 */
function deleteView() {
    var endpoint = url + "/views/" + currentViewCode + "/delete";
    var token = getCsrf();

    $.ajax({
        url: endpoint,
        type: "DELETE",
        datatype: "application/json; charset=utf-8",
        headers: {'X-XSRF-TOKEN': token},
        success: function (data, status) {

            alert('success');
            $("#viewsList").empty();
            currentViewCode = "";
            $("#currentViewCode").text("");
            getViewsForLoggedInUser();

        },
        error: function (xhr, ajaxOptions, thrownError) { //Add these parameters to display the required response
            alert(xhr.status);
            alert(xhr.responseText);
            alert('did not remove view')
        },
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true
    });
}

function showAddNotes() {
    var endpoint = url + "/users";
    var token = getCsrf();

    $.ajax({
        url: endpoint,
        type: "GET",
        datatype: "application/json; charset=utf-8",
        headers: {'X-XSRF-TOKEN': token},
        success: function (data, status) {

            users = data.users;
            usersSize = users.length;
            $("#authorsList").empty();

            // add users to list
            for (var i = 0; i < usersSize; i++) {
                $("#authorsList").append(
                    '<button type="button" class="list-group-item list-group-item-action list-group-item-primary" onclick="setCurrentAuthor(' + users[i].userId + ')" onmouseover="" style="cursor: pointer;">' +
                    users[i].name + '</button>'
                )
            }
        },
        error: function (xhr, ajaxOptions, thrownError) { //Add these parameters to display the required response
            alert(xhr.status);
            alert(xhr.responseText);
            alert('cannot get users')
        },
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true
    });
}

function setCurrentAuthor(id) {
    selectedUser = id;
}

/**
 * Called from manage view screen to add notes from an author with a minimum rank to the currently selected view.
 */
function addNotesToView() {
    var minimumValue = $("#minimumRanking").val();

    if (minimumValue == "") {
        minimumValue = 0;
    }

    var endpoint = url + "/views/add/" + currentViewCode + "/" + selectedUser + "/" + minimumValue;
    var token = getCsrf();

    $.ajax({
        url: endpoint,
        type: "POST",
        datatype: "application/json; charset=utf-8",
        headers: {'X-XSRF-TOKEN': token},
        success: function (data, status) {

            alert('notes added to view');
            getChapter();

        },
        error: function (xhr, ajaxOptions, thrownError) { //Add these parameters to display the required response
            alert(xhr.status);
            alert(xhr.responseText);
            alert('cannot add notes to view')
        },
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true
    });
}




// for testing

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
                noteOutput += "<strong>v" + notes[i].verse + "</strong>: " + notes[i].noteText + "</br>";
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
        headers: {'X-XSRF-TOKEN': token},
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
                noteOutput += "<strong>v" + notes[i].verse + "</strong>: " + notes[i].noteText + "</br>";
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