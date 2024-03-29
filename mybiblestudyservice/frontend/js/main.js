//var url = "http://mybiblestudywebapp.us-east-2.elasticbeanstalk.com";
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
var requestFromClientArea = false; // boolean to track if the request came from the client instead of the dashboard
var exploreNotesSection = false;

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

/**
 * This should be called from either the client or from the manage my views section in the dashboard
 */
function getChapter() {

    var book = $("#book").val();
    var chapterNo = $("#chapter").val();
    var apiEndPoint = url + "/biblestudy/" + currentViewCode + "/" + book + "/" + chapterNo;
    var clientArea = requestFromClientArea;


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
                    notes[i].noteText + '</br>';

                if (clientArea != true) {
                    // request came from dashboard area so show management buttons
                    noteOutput += '<button type="button" class="btn btn-sm btn-danger" ' +
                        'onclick="removeNote(' + i + ')">Remove</button>';
                }

                noteOutput += '<hr></br>';

                /*
                noteOutput += '<div id="note' + i + '"<strong>' + notes[i].verseStart + '-' + notes[i].verseEnd + '</strong>: ' +
                    notes[i].noteText + '</br><button type="button" class="btn btn-sm btn-danger" ' +
                    'onclick="removeNote(' + i + ')">Remove</button><hr></br>'; */
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
    var endpoint = url + "/notes/mynotes/" + book + "/" + chapterNo + "/";

    if (exploreNotesSection) {

        // return if there is no selectedUser
        if (selectedUser == "" || selectedUser == null) {
            return;
        } else {
            // get selected user_id
            endpoint += selectedUser;
        }

    } else {
        endpoint += currentUserId;
    }

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
                    verseOutput += '<sup>' + verses[i].verseNr + '</sup>' + verses[i].verse;
                }

                // iterate through notes
                for (var i = 0; i < notes.length; i++) {
                    noteOutput += '<div id="note' + i + '"><strong>' +
                        '<span data-toggle="tooltip" ' +
                        'title="this note applies from verse ' + notes[i].verseStart +
                        ' to verse ' + notes[i].verseEnd + '">' +
                        notes[i].verseStart + '-' + notes[i].verseEnd + '</span></strong>: ' +
                        '<sup><span class="badge badge-warning" data-toggle="tooltip" ' +
                        'title="ranking value for this note is ' + notes[i].ranking + '">' +
                        notes[i].ranking + '</span></sup>' +
                        notes[i].noteText + '<br>';

                    if (exploreNotesSection) {
                        noteOutput += 'Created by user: ' + notes[i].userId + '<br>' +
                            '<div class ="btn-group">' +
                            '<button type="button" class="btn btn-sm btn-info" ' +
                            'onclick="showAddComment(' + i + ')">Add Comment</button>' +
                            '<button type="button" class="btn btn-sm btn-info" ' +
                            'onclick="showAddToView(' + i + ')">Add to view</button>' +
                            '<button type="button" class="btn btn-sm btn-danger" ' +
                            'onclick="downVote(' + notes[i].noteId + ')"> - </button>' +
                            '<button type="button" class="btn btn-sm btn-success" ' +
                            'onclick="upVote(' + notes[i].noteId + ')"> + </button></div>' +
                            '<div id="selectViewListRow' + i + '">' +
                            '</div></div><hr>';
                    } else {
                        noteOutput += '<div class ="btn-group">' +
                            '<button type="button" class="btn btn-sm btn-info" ' +
                            'onclick="editNote(' + i + ')">Edit</button>' +
                            '<button type="button" class="btn btn-sm btn-info" ' +
                            'onclick="viewComments(' + notes[i].noteId + ')">View comments</button>' +
                            '<button type="button" class="btn btn-sm btn-danger" ' +
                            'onclick="deleteNote(' + notes[i].noteId + ')">Delete</button>' +
                            '</div></div><hr>';
                    }
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

    var apiEndPoint = url + "/users/signup";

    var request = {
        email: "testingemail@testingthis.com",
        firstname: "Abe",
        lastname: "Lincoln",
        password: "testpassword"
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

/**
 * Called for main navbar
 */
function login() {
    var email = $("#emailLoginField").val();
    var pass = $("#passwordField").val();
    doLogin(email, pass);
}

/**
 * Also called after sign up
 * @param email
 * @param pass
 */
function doLogin(email, pass) {
    var apiEndPoint = url + "/login";

    $.ajax({
        url: apiEndPoint,
        type: "GET",
        datatype: "application/json; charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(email + ":" + pass));
            //xhr.setRequestHeader("Authorization", "Basic " + btoa("admin@admin.com:12345"));
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

function autoLogin() {
    var apiEndPoint = url + "/login";
    var email = $("#emailLoginField").val();
    var pass = $("#passwordField").val();
    $.ajax({
        url: apiEndPoint,
        type: "GET",
        datatype: "application/json; charset=utf-8",
        beforeSend: function (xhr) {
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
    $("#signupDiv").hide();
    $("#getChapterForm").hide();
    $("#verses").hide();
    hideRightContentDiv();
}

/**
 * Hide the content column next to verses
 */
function hideRightContentDiv() {
    exploreNotesSection = false;
    $("#goButton").attr("onclick", "getService()");
    $("#notes").hide();
    $("#createNote").hide();
    $("#manageViews").hide();
    $("#exploreNotes").hide();
    $("#addCommentDiv").hide();
    $("#addNotes").hide();
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
    $("#login").attr("onclick", "logout()");
}

/**
 * Call this after logging out or received an unauthorized response from server
 */
function logoutHandler() {
    showLoginForm();
    currentUserId = "";
    $("#login").text("Login");
    $("#login").attr("onclick", "showLoginForm()");
}

function showCreateNote() {
    hideRightContentDiv();
    $("#createNoteButton").attr("onclick", "createNote()");
    $("#createNoteHeader").html("Create Note");

    // erase values from fields
    // populate fields
    $("#verseStart").val("");
    $("#verseEnd").val("");
    $("#privNote").val("");
    $("#noteText").val("");

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
    $("#notes").empty();
    getChapterUserNotes();
    $("#notes").show();
    $("#goButton").attr("onclick", "getChapterUserNotes()");
}

function showExploreNotes() {
    hideRightContentDiv();
    $("#notes").empty();
    exploreNotesGetUsers();
    exploreNotesSection = true;
    $("#goButton").attr("onclick", "getChapterUserNotes()");
    getChapterUserNotes();
    $("#exploreNotes").show();
    $("#notes").show();
}

/**
 * Called when user clicks on add comment button
 * @param id
 */
function showAddComment(i) {
    // hide all other notes
    $("#notes").hide();

    $("#addCommentNote").html(currentNotes[i].noteText);
    $("#addComment").attr("onclick", "addCommentClicked(" + currentNotes[i].noteId + ")");
    $("#addCommentDiv").show();
}

function viewComments(id) {
    var endpoint = url + "/notes/comments/" + id;
    var token = getCsrf();

    $.ajax({
        url: endpoint,
        type: "GET",
        datatype: "application/json; charset=utf-8",
        success: function (data, status) {

            var comments = data.comments;
            var commentsSize = comments.length;
            var commentOutput = "";

            if (commentsSize < 1) {
                alert("no comments available for this note");
            } else {
                // iterate through and append comments
                for (var i = 0; i < commentsSize; i++) {
                    commentOutput += '<div id="comment' + i + '">' +
                        '<strong>' + comments[i].createdAt + '</strong> ' +
                        comments[i].commentText +
                        '</div><hr>';
                }

                $("#notes").html(commentOutput);
            }

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
 * Called when user clicks on 'Add Comment' button
 * @param i
 */
function addCommentClicked(id) {


    var endpoint = url + "/notes/comments";
    var token = getCsrf();

    var commentRequest = {
        noteId: id,
        commentText: $("#commentText").val()
    };

    var textValue = $("#commentText").val();

    if (textValue == null || textValue == "") {
        alert('Please enter a comment before submitting');
        return;
    }

    var data = JSON.stringify(commentRequest);

    $.ajax({
        url: endpoint,
        type: "POST",
        datatype: "json",
        contentType: "application/json",
        headers: {'X-XSRF-TOKEN': token},
        data: data,
        success: function (data, status) {
            alert("comment added");
            $("#addCommentDiv").hide();
            $("#notes").show();
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
        priv: $("#privNote").is(":checked")
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
        async: false,
        success: function (data, status) {

            var size = data.viewCodes.length;
            //userViewCodes = data.viewCodes;
            setUserViewCodes(data.viewCodes);

            // iterate through view codes
            for (var i = 0; i < size; i++) {
                var vc = data.viewCodes[i];
                $("#viewsList").append(
                    '<label class="btn btn-sm btn-info"><input type="radio" ' +
                    'name="viewlistrow" value="' + i + '">' + vc + '</label>'
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
 * Use this to set the global userViewCodes
 * @param viewCodes return from ajax
 */
function setUserViewCodes(viewCodes) {
    userViewCodes = viewCodes;
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
    var viewCodeLink =
        url + '/index.html?viewcode=' + userViewCodes[i];
    $("#viewCodeLink").html('Your client link is: <br>' +
        '<a href="' + viewCodeLink + '">' + viewCodeLink + '</a>'
    );
    $("#notes").show();
}

function setSelectedUser(id) {
    selectedUser = id;
}


$(document).ready(function () {

    // get radio button value
    $("#viewsList").change(function () {
        var i = $('input[name=viewlistrow]:checked').val();
        setCurrentViewCode(i);

        // only load verses and notes if book and chapter have been set
        if (currentBook != "" && currentChapter != "" && currentBook != null && currentChapter != null) {
            getService();
        }
    });

    // get author on explore notes
    $("#selectAuthorsList").change(function () {
        var i = $('input[name=selectAuthorsListRow]:checked').val();
        setSelectedUser(i);
    });

    // enable tooltips
    $('[data-toggle="tooltip"]').tooltip()
});

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

function deleteNote(id) {
    var endpoint = url + "/notes/delete/" + id;
    var token = getCsrf();

    $.ajax({
        url: endpoint,
        type: "DELETE",
        datatype: "application/json; charset=utf-8",
        headers: {'X-XSRF-TOKEN': token},
        success: function (data, status) {

            alert('success');
            $("#notes").hide();
            getChapterUserNotes();
            $("#notes").show();

        },
        error: function (xhr, ajaxOptions, thrownError) { //Add these parameters to display the required response
            alert(xhr.status);
            alert(xhr.responseText);
            alert('did not delete note')
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

            $("#addNotes").show();
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

/**
 * Called when user clicks on "Add to view" button in the explore notes section
 * @param index - note index
 */
function showAddToView(index) {

    getViewsForLoggedInUser();
    var size = 0;

    size = userViewCodes.length;

    if (size < 1) {
        $("#selectViewListRow" + index).empty();
        $("#selectViewListRow" + index).append(
            '<em>You have no views</em>'
        );
        return;
    } else {

        if ($("#selectViewListRow" + index).text().indexOf('My Views') <= 0) {
            $("#selectViewListRow" + index).empty();
            $("#selectViewListRow" + index).append(
                '<div id="selectViewRadioList' + index + '" class="btn-group-vertical btn-group-toggle" data-toggle="buttons">' +
                '</div>'
            );
        }

        // iterate through view codes
        for (var i = 0; i < size; i++) {
            var vc = userViewCodes[i];

            $("#selectViewRadioList" + index).append(
                '<label class="btn btn-sm btn-secondary"><input type="radio" ' +
                'name="addnotetoviewlistrow" value="' + i + '">' + vc + '</label>'
            );
        }

        $("#selectViewListRow" + index).append(
            '<div>' +
            '<button type="button" class="btn btn-primary" onclick="addNoteToView(' + index + ')">Add Note to View</button>' +
            '</div>'
        );
    }
}

/**
 * Ajax call to add a single note
 * @param noteIndex
 */
function addNoteToView(noteIndex) {

    // get viewcode
    var i = $('input[name=addnotetoviewlistrow]:checked').val();

    var viewcode = userViewCodes[i];

    // get noteId
    var noteId = currentNotes[noteIndex].noteId;

    var endpoint = url + "/views/add/" + viewcode + "/" + noteId;
    var token = getCsrf();

    $.ajax({
        url: endpoint,
        type: "POST",
        headers: {'X-XSRF-TOKEN': token},
        success: function (data, status) {

            if (data.status == "success") {
                alert('note added to view');
            }
        },
        error: function (xhr, ajaxOptions, thrownError) { //Add these parameters to display the required response
            // TODO - add message that note is already added to this view
            /*alert(xhr.status);
            alert(xhr.responseText);*/
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

/**
 * Called when user clicks on edit note button
 * @param i
 */
function editNote(i) {
    hideRightContentDiv();
    var noteToEdit = currentNotes[i];
    $("#createNoteHeader").html("Edit Note");
    $("#createNoteButton").attr("onclick", "updateNote(" + currentNotes[i].noteId + ")");

    // populate fields
    $("#verseStart").val(noteToEdit.verseStart);
    $("#verseEnd").val(noteToEdit.verseEnd);
    $("#privNote").val(noteToEdit.priv);
    $("#noteText").val(noteToEdit.noteText);


    $("#createNote").show();

}

/**
 * Used to send the update note request to the server
 */
function updateNote(id) {
    var endpoint = url + "/notes/update";

    var note = {
        noteId: id,
        noteText: $("#noteText").val(),
        bookId: currentBookId,
        chapterId: currentChapterId,
        verseStart: $("#verseStart").val(),
        verseEnd: $("#verseEnd").val(),
        priv: $("#privNote").is(":checked")
    };

    var data = JSON.stringify(note);
    var token = getCsrf();

    $.ajax({
        url: endpoint,
        type: "PUT",
        datatype: "json",
        contentType: "application/json",
        headers: {'X-XSRF-TOKEN': token},
        data: data,
        success: function (data, status) {
            alert("note updated");
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
 * Used in the client. Check the url parameter value for viewcode and if set then retrieve text and notes, otherwise
 * only text.
 */
function clientGetBibleTextAndNotes() {

    var urlParams = new URLSearchParams(window.location.search);
    //var viewCodeString = urlParams.toString();
    var viewCode = urlParams.get('viewcode');

    if (viewCode != "" && viewCode != null) {
        currentViewCode = viewCode;
    } else {
        currentViewCode = "0"; // 0 denotes generic view code for all public notes
    }

    requestFromClientArea = true;
    getService();
    requestFromClientArea = false;
}

/**
 * Call this function when loading explore notes. It populates the authors list
 */
function exploreNotesGetUsers() {
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
            $("#selectAuthorsList").empty();

            // add all authors radio button
            $("#selectAuthorsList").append('<label class="btn btn-sm btn-secondary">\n' +
                '<input type="radio" name="selectAuthorsListRow" value=0>All authors</label>');


            // iterate through users and append to list
            for (var i = 0; i < usersSize; i++) {
                $("#selectAuthorsList").append(
                    '<label class="btn btn-sm btn-secondary"><input type="radio" ' +
                    'name="selectAuthorsListRow" value="' + users[i].userId + '">' + users[i].name + '</label>'
                );
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

/**
 * Called when user downvotes a note
 * @param id
 */
function downVote(id) {
    noteVote(id, false);
}

/**
 * Called when a user upvotes a note
 * @param id
 */
function upVote(id) {
    noteVote(id, true);
}

/**
 * Called by downVote and upVote to send the ajax request
 * @param id note_id
 * @param vote - boolean: true for up or false for negative
 */
function noteVote(id, vote) {

    var endpoint = url + "/notes/rank";

    var token = getCsrf();

    var rankObj = {
        noteId: id,
        increaseRanking: vote
    };

    var request = JSON.stringify(rankObj);

    $.ajax({
        url: endpoint,
        type: "POST",
        datatype: "json",
        contentType: "application/json",
        data: request,
        headers: {'X-XSRF-TOKEN': token},
        success: function (data, status) {

            getChapterUserNotes();
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

function signUp() {
    $("#signupDiv").show();
    getCsrfFromServer();
}

/**
 * Call to get token when not logged in
 */
function getCsrfFromServer() {
    var endpoint = url + "/users/csrf/get";
    var token = getCsrf();

    $.ajax({
        url: endpoint,
        type: "POST",
        headers: {'X-XSRF-TOKEN': token},
        success: function (data, status) {


        },
        error: function (xhr, ajaxOptions, thrownError) { //Add these parameters to display the required response
        },
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true
    });
}

/**
 * Call this to create a new account
 */
function createAccount() {

    var endpoint = url + "/users/signup";
    var token = getCsrf();
    var email = $("#setEmailId").val();
    var pass = $("#setPasswordId").val();

    var requestObj = {
        email: email,
        firstname: $("#setFirstnameId").val(),
        lastname: $("#setLastnameId").val(),
        password: pass
    };

    var requestJson = JSON.stringify(requestObj);

    $.ajax({
        url: endpoint,
        type: "POST",
        datatype: "json",
        contentType: "application/json",
        data: requestJson,
        headers: {'X-XSRF-TOKEN': token},
        success: function (data, status) {

            alert('account created');
            doLogin(email, pass);

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
        viewCode: "6e9e6366-f386-11e9-b633-0242ac110002",
        book: book,
        chapterNo: chapterNo
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