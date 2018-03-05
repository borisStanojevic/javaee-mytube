$(document).ready(e => {
    $('#videosSearchSection').hide();
    $('.ghostBtn').on('click', function () {
        $('.jumbotron').hide();
        $('#videosSection').focus();
    });

    getAllVideos();

    $('#signUpLink').on('click', function (e) {
        $('#signUpDialog').modal();
    });

    $('#signUpBtn').on('click', function (e) {
        e.preventDefault();
        var signUpForm = $('#signUpForm');

        if (signUpForm.find("input[name='username']").val().length < 6 || signUpForm.find("input[name='username']").val().length > 16) {
            signUpForm.find("input[name='username']").after("<label class='errorMsgLabel'> Username should be between 6 and 16 characters </label>");
            return
        }
        if (signUpForm.find("input[name='password']").val().length < 6 || signUpForm.find("input[name='password']").val().length > 16) {
            signUpForm.find("input[name='password']").after("<label class='errorMsgLabel'> Password should be between 6 and 16 characters </label>");
            return
        }
        if (signUpForm.find("input[name='password']").val() != signUpForm.find("input[name='repeatedPassword']").val()) {
            signUpForm.find("input[name='repeatedPassword']").after("<label class='errorMsgLabel'> Passwords do not match</label>");
            return;
        }
        if (!(/^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/.test(signUpForm.find("input[name='email']").val()))) {
            signUpForm.find("input[name='email']").after("<label class='errorMsgLabel'> Email not valid</label>");
            return;
        }

        var form = $('#signUpForm')[0];
        var data = new FormData(form);

        $.ajax({
            type: "POST",
            enctype: "multipart/form-data",
            url: 'SignupServlet',
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            success: function (data) {
                if (data.status == 'success')
                    window.location.replace('user.html?username=' + data.user.username);
                else
                    $('.errorMsgLabel').text(data.message);
            }
        });
    });

    $('#loginLink').on('click', function (e) {
        $('#loginDialog').modal();
        e.preventDefault();
    });

    $('#loginBtn').on('click', function (e) {
        var username = $("#loginDialog input[name='username']").val();
        var password = $("#loginDialog input[name='password']").val();

        var params = { 'username': username, 'password': password };
        console.log(params);

        $.post('LoginServlet', params, function (data) {
            if (data.status == 'failure')
                $('#loginDialog .errorMsgLabel').text(data.message);
            else {
                window.location.replace('user.html?username=' + username);
            }
        });
        e.preventDefault();
    });

    $('#loginDialog').on('hidden.bs.modal', function (e) {
        $(this).find("input[name='username']").empty();
        $(this).find("input[name='password']").empty();
        $(this).find('.errorMsgLabel').empty();
    });

    $('#mainSearchBtn').on('click', function (e) {
        e.preventDefault();
        var mainSearchInput = $("input[name='mainSearchInput']").val();
        var nameCb = $("input[name='nameCb']").prop('checked');
        var ownerCb = $("input[name='ownerCb']").prop('checked');
        var commentContentCb = $("input[name='commentContentCb']").prop('checked');

        params = { 'mainSearchInput': mainSearchInput, 'nameCb': nameCb, 'ownerCb': ownerCb, 'commentContentCb': commentContentCb }
        console.log(params);

        $.post('HomeServlet', params, function (data) {
            $('#videosSection > .row').empty();
            var loggedInUser = data.loggedInUser;
            $.each(data.videos, function (index, video) {
                if (loggedInUser == null || (loggedInUser != null && loggedInUser.role == 'USER')) {
                    //... ako je video unlisted ili private ili ako je izbrisan ne prikazuj ga
                    if (!(video.visibility == 'UNLISTED' || video.visibility == 'PRIVATE' || video.deleted)) {
                        $('#videosSection > .row').append("<div class='videoDiv col-xs-12 col-sm-4 col-md-15 col-lg-15'>"
                            + "<div class='embed-responsive embed-responsive-4by3'>"
                            + "<iframe class='embed-responsive-item' src='" + video.videoUrl + "'></iframe>"
                            + "</div>"
                            + "<h4><a href='video.html?id=" + video.id + "'>" + video.name + "</a></h4>"
                            + "<span><img src='images/" + video.owner.thumbnailUrl + "' class='smallThumbnail'><a href='user.html?username=" + video.owner.username + "'>" + ' ' + video.owner.username + "</a></span>"
                            + "<br>"
                            + "<span>" + video.views + " views</span>"
                            + "<span>" + '&nbsp;' + video.dateTimeUploaded.dayOfMonth + '/' + video.dateTimeUploaded.monthValue + '/' + video.dateTimeUploaded.year + "</span>"
                            + "<div class = 'ratingDiv'>" + video.likes + ' likes ' + video.dislikes + ' dislikes' + "</div>"
                            + "</div>"
                        );
                    }
                    if (video.ratingVisible == false) {
                        $('.ratingDiv').hide();
                    }
                }
                //U suprotnome je admin pa prikazi sve
                else {
                    $('#videosSection > .row').append("<div class='videoDiv col-xs-12 col-sm-4 col-md-15 col-lg-15'>"
                        + "<div class='embed-responsive embed-responsive-4by3'>"
                        + "<iframe class='embed-responsive-item' src='" + video.videoUrl + "'></iframe>"
                        + "</div>"
                        + "<h4><a href='video.html?id=" + video.id + "'>" + video.name + "</a></h4>"
                        + "<span><img src='images/" + video.owner.thumbnailUrl + "' class='smallThumbnail'><a href='user.html?username=" + video.owner.username + "'>" + ' ' + video.owner.username + "</a></span>"
                        + "<br>"
                        + "<span>" + video.views + " views</span>"
                        + "<span>" + '&nbsp;' + video.dateTimeUploaded.dayOfMonth + '/' + video.dateTimeUploaded.monthValue + '/' + video.dateTimeUploaded.year + "</span>"
                        + "<div>" + video.likes + ' ' + video.dislikes + "</div>"
                        + "</div>"
                    );
                }
            });
        });
    });

    $('#showSearchBtn').on('click', function (e) {
        e.preventDefault();
        $('#videosSearchSection').slideToggle('fast');
    });

    $('#advancedSearchBtn').on('click', function (e) {
        var videoNameInput = $("input[name='videoNameInput'").val();
        var videoOwnerInput = $("input[name='videoOwnerInput'").val();
        var minViewsInput = $("input[name='minViewsInput'").val();
        var maxViewsInput = $("input[name='maxViewsInput'").val();
        //Preuzimanje minDateTimeInput
        //Preuzimanje maxDateTimeInput
        var sortSelect = $("#sortSelect").val();

        //Dodati datume u params
        var params = { 'sortSelect': sortSelect, 'videoNameInput': videoNameInput, 'videoOwnerInput': videoOwnerInput, 'minViewsInput': minViewsInput, 'maxViewsInput': maxViewsInput, 'minDateTimeInput': '', 'maxDateTimeInput': '' };
        console.log(params);
        $('#top5VideosSection > .row').empty();
        $('#videosSection > .row').empty();
        $('#profileLink').empty();
        getAllVideos(params);

        e.preventDefault();
    });

    $("#sortSelect").on('change', function (e) {
        $('#top5VideosSection > .row').empty();
        $('#videosSection > .row').empty();
        $('#profileLink').empty();
        getAllVideos({ 'sortSelect': $(this).val(), 'videoNameInput': '', 'videoOwnerInput': '', 'minViewsInput': '', 'maxViewsInput': '', 'minDateTimeInput': '', 'maxDateTimeInput': '' })
        e.preventDefault();
    });
});

function getAllVideos(params = { 'sortSelect': '', 'viewsDesc': '', 'videoNameInput': '', 'videoOwnerInput': '', 'minViewsInput': '', 'maxViewsInput': '', 'minDateTimeInput': '', 'maxDateTimeInput': '' }) {
    //Sa parametrima za pretragu koje proslijedjujemo ovoj funkciji kao parametar dobavljamo sa servera sve videe
    $.get('HomeServlet', params, function (data) {
        var top5 = data.top5;
        var videos = data.videos;
        var loggedInUser = data.loggedInUser;
        if (loggedInUser != null) {
            $('#signUpLink').hide();
            $('#loginLink').hide();
            $('#logoutLink').show();
            $('#profileLink').attr('href', 'user.html?username=' + loggedInUser.username);
            $('#profileLink').append("<span class='glyphicon glyphicon-user'></span> " + loggedInUser.username);
            $('#profileLink').show();
        }
        else {
            $('#mainSearchForm').hide();
        }

        $.each(top5, function (index, video) {
            //Ako je korisnik koji nije ulogovan ili ako je ulogovan a nije admin, onda ->
            if (loggedInUser == null || (loggedInUser != null && loggedInUser.role == 'USER')) {
                //... ako je video unlisted ili private ili ako je izbrisan ne prikazuj ga
                if (!(video.visibility == 'UNLISTED' || video.visibility == 'PRIVATE' || video.deleted)) {
                    $('#top5VideosSection > .row').append("<div class='videoDiv col-xs-12 col-sm-4 col-md-15 col-lg-15'>"
                        + "<div class='embed-responsive embed-responsive-4by3'>"
                        + "<iframe class='embed-responsive-item' src='" + video.videoUrl + "'></iframe>"
                        + "</div>"
                        + "<h4><a href='video.html?id=" + video.id + "'>" + video.name + "</a></h4>"
                        + "<span><img src='images/" + video.owner.thumbnailUrl + "' class='smallThumbnail'><a href='user.html?username=" + video.owner.username + "'>" + ' ' + video.owner.username + "</a></span>"
                        + "<br>"
                        + "<span>" + video.views + " views</span>"
                        + "<span>" + '&nbsp;' + video.dateTimeUploaded.dayOfMonth + '/' + video.dateTimeUploaded.monthValue + '/' + video.dateTimeUploaded.year + "</span>"
                        + "</div>"
                    );
                }
            }
            //U suprotnome je admin pa prikazi sve
            else {
                $('#top5VideosSection > .row').append("<div class='videoDiv col-xs-12 col-sm-4 col-md-15 col-lg-15'>"
                    + "<div class='embed-responsive embed-responsive-4by3'>"
                    + "<iframe class='embed-responsive-item' src='" + video.videoUrl + "'></iframe>"
                    + "</div>"
                    + "<h4><a href='video.html?id=" + video.id + "'>" + video.name + "</a></h4>"
                    + "<span><img src='images/" + video.owner.thumbnailUrl + "' class='smallThumbnail'><a href='user.html?username=" + video.owner.username + "'>" + ' ' + video.owner.username + "</a></span>"
                    + "<br>"
                    + "<span>" + video.views + " views</span>"
                    + "<span>" + '&nbsp;' + video.dateTimeUploaded.dayOfMonth + '/' + video.dateTimeUploaded.monthValue + '/' + video.dateTimeUploaded.year + "</span>"
                    + "</div>"
                );
            }
        });

        //Za svaki video ->
        $.each(videos, function (index, video) {
            //Ako je korisnik koji nije ulogovan ili ako je ulogovan a nije admin, onda ->
            if (loggedInUser == null || (loggedInUser != null && loggedInUser.role == 'USER')) {
                //... ako je video unlisted ili private ili ako je izbrisan ne prikazuj ga
                if (!(video.visibility == 'UNLISTED' || video.visibility == 'PRIVATE' || video.deleted)) {
                    $('#videosSection > .row').append("<div class='videoDiv col-xs-12 col-sm-4 col-md-15 col-lg-15'>"
                        + "<div class='embed-responsive embed-responsive-4by3'>"
                        + "<iframe class='embed-responsive-item' src='" + video.videoUrl + "'></iframe>"
                        + "</div>"
                        + "<h4><a href='video.html?id=" + video.id + "'>" + video.name + "</a></h4>"
                        + "<span><img src='images/" + video.owner.thumbnailUrl + "' class='smallThumbnail'><a href='user.html?username=" + video.owner.username + "'>" + ' ' + video.owner.username + "</a></span>"
                        + "<br>"
                        + "<span>" + video.views + " views</span>"
                        + "<span>" + '&nbsp;' + video.dateTimeUploaded.dayOfMonth + '/' + video.dateTimeUploaded.monthValue + '/' + video.dateTimeUploaded.year + "</span>"
                        + "</div>"
                    );
                }
            }
            //U suprotnome je admin pa prikazi sve
            else {
                $('#videosSection > .row').append("<div class='videoDiv col-xs-12 col-sm-4 col-md-15 col-lg-15'>"
                    + "<div class='embed-responsive embed-responsive-4by3'>"
                    + "<iframe class='embed-responsive-item' src='" + video.videoUrl + "'></iframe>"
                    + "</div>"
                    + "<h4><a href='video.html?id=" + video.id + "'>" + video.name + "</a></h4>"
                    + "<span><img src='images/" + video.owner.thumbnailUrl + "' class='smallThumbnail'><a href='user.html?username=" + video.owner.username + "'>" + ' ' + video.owner.username + "</a></span>"
                    + "<br>"
                    + "<span>" + video.views + " views</span>"
                    + "<span>" + '&nbsp;' + video.dateTimeUploaded.dayOfMonth + '/' + video.dateTimeUploaded.monthValue + '/' + video.dateTimeUploaded.year + "</span>"
                    + "</div>"
                );
            }
        });
    });
}

function getDateTimeFilters() {
    $('#minDateTimeInput').datetimepicker();
    $('#maxDateTimeInput').datetimepicker({
        useCurrent: false
    });
    $("#minDateTimeInput").on("dp.change", function (e) {
        $('#maxDateTimeInput').data("DateTimePicker").minDate(e.date);
    });
    $("#maxDateTimeInput").on("dp.change", function (e) {
        $('#minDateTimeInput').data("DateTimePicker").maxDate(e.date);
    });
}