var username = location.search.split('username=')[1].trim();
$(document).ready(e => {
    $('#mainSearchForm').hide();
    username = location.search.split('username=')[1].trim();

    getUser({ 'username': username });
    getUploadedVideos({ 'username': username, 'sortUploadedSelect': 'dtUploadedDesc' });

    $('#showUploadVideoBtn').on('click', function (e) {
        $('#uploadVideoDialog').modal();
        e.preventDefault();
    });

    $('#uploadVideoBtn').on('click', function (e) {
        var uploadVideoDialog = $('#uploadVideoDialog');
        var name = uploadVideoDialog.find("input[name='name']").val();
        if (name.length < 6) {
            uploadVideoDialog.find("input[name='name']").after("<label class='errorMsgLabel'> Video name should be at least 6 characters long</label>");
            return;
        }
        var url = uploadVideoDialog.find("input[name='url']").val();
        if (url == '') {
            uploadVideoDialog.find("input[name='url']").after("<label class='errorMsgLabel'>Please enter valid url</label>");
            return;
        }
        var description = uploadVideoDialog.find("input[name='description']").val();
        var visibility = $('#visibilitySelect').val();
        var commentsAllowed = uploadVideoDialog.find("input[name='commentsAllowed']").prop('checked');
        var ratingVisible = uploadVideoDialog.find("input[name='ratingVisible']").prop('checked');

        let params = {
            'name': name, 'url': url, 'description': description, 'visibility': visibility,
            'commentsAllowed': commentsAllowed, 'ratingVisible': ratingVisible
        };
        console.log('params');

        $.post('UploadVideoServlet', params, function (data) {
            if (data.status == 'failure')
                uploadVideoDialog.find('.errorMsgLabel').text(' We failed to upload video. Try again');
            else {
                uploadVideoDialog.find('.errorMsgLabel').empty();
                getUploadedVideos({ 'username': username, 'sortUploadedSelect': 'dtUploadedDesc' });
                uploadVideoDialog.toggle();
            }
        });
        e.preventDefault();
    });

    $('#uploadedVideosList').on('click', '.deleteVideoBtn', function (e) {
        var id = $(this).data('id')
        console.log(id);
        $.post('DeleteVideoServlet', { 'id': id }, function (data) {
            if (data.status == 'failure')
                $('#deleteVideoAlert').modal();
            else {
                $('#uploadedVideosList').empty();
                getUploadedVideos({ 'username': username, 'sortUploadedSelect': $('#sortUploadedSelect').val() });
            }
        });
        e.preventDefault();
    });

    $('#sortUploadedSelect').on('change', function (e) {
        var sortUploadedSelect = $(this).val();
        $('#uploadedVideosList').empty();
        getUploadedVideos({ 'username': username, 'sortUploadedSelect': sortUploadedSelect });
        e.preventDefault();
    });

    $('#showEditUserBtn').on('click', function (e) {
        $('#editChannelSection').slideToggle('fast');
    });

    $('#deleteUserBtn').on('click', function (e) {
        $.post('DeleteUserServlet', { 'username': username }, function (data) {
            if (data.status == 'success') {
                window.location.replace('home.html');
            }
            else
                $('#deleteUserAlert').modal();
        });
        e.preventDefault();
    });

    $('#blockUserBtn').on('click', function (e) {
        var blockOrUnblock = $(this).val();
        $.post('BlockUserServlet', { 'username': username, 'blockOrUnblock': blockOrUnblock }, function (data) {
            if (data.status == 'success') {
                $("#profileLink").empty();
                $("#subscribedsList").empty();
                getUser({ 'username': username });
                $('#subscribeBtn').before().empty();
            }
            else
                $('.errorMsgLabel').text('We did not manage to block user. Try again');
        });
        e.preventDefault();
    });

    $('#editUserBtn').on('click', function (e) {
        var firstName = $("#editUserForm").find("input[name='firstName']").val();
        var lastName = $("#editUserForm input[name='lastName']").val();
        var password = $("#editUserForm input[name='password']").val();
        if (password.length < 6 || password.length > 16) {
            $("#editUserForm input[name='password']").after("<label class='errorMsgLabel'>Password should be between 6 and 16 characters long</label>");
            return
        }
        var channelDescription = $("#editUserForm input[name='channelDescription']").val();
        var role = $('#editUserForm #roleSelect').val();
        var email = $("#editUserForm input[name='email']").val();
        if (email.length == 0 || !(/^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/.test(email))) {
            $("#editUserForm input[name='email']").after("<label class='errorMsgLabel'>Please enter valid email</label>");
            return;
        }

        let params = {
            'usernameInput': username, 'firstNameInput': firstName, 'lastNameInput': lastName, 'passwordInput': password,
            'channelDescriptionInput': channelDescription, 'roleSelect': role, 'emailInput': email
        };
        console.log(params);

        $.post('EditUserServlet', params, function (data) {
            if (data.status == 'success') {
                $("#editUserForm .errorMsgLabel").empty();
                $('#profileLink').empty();
                $('#subscribersSpan').empty();
                $('#subscribedsList').empty();
                $('#editChannelSection').hide();
                getUser({ 'username': username });

            }
            else
                $('.errorMsgLabel').text('We did not manage to edit user. Try again');
        });
        e.preventDefault();
    });

    $('#uploadImageBtn').on('click', function (e) {
        e.preventDefault();
        if ($('#uploadImageForm').find("input[name='thumbnail']").val() == '') {
            $('#uploadImageForm').find("input[name='thumbnail']").after("<label class='errorMsgLabel'> No image selected </label>");
            return;
        }
        var form = $('#uploadImageForm')[0];
        var data = new FormData(form);

        $.ajax({
            type: "POST",
            enctype: "multipart/form-data",
            url: 'UploadImageServlet',
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            success: function (data) {
                if (data.status == 'success') {
                    $("#profileLink").empty();
                    $('#subscribedsList').empty();
                    getUser({ 'username': username });
                }
                else
                    $('#uploadImageForm > .errorMsgLabel').text(data.message);
            }
        });
    });

    $("#subscribeBtn").on('click', function (e) {
        $.post('SubscriptionServlet', { 'subscribedUsername': username }, function (status) {
            if (status == 'success') {
                $("#profileLink").empty();
                $('#subscribedsList').empty();
                getUser({ 'username': username });
            }
            else {
                $(this).before("<label class='errorMsgLabel'> Failed to subscribe/unsubscribe. Try again</label>");
            }
        });
    });

    $('#loginDialog').on('hidden.bs.modal', function (e) {
        $(this).find("input[name='username']").empty();
        $(this).find("input[name='password']").empty();
        $(this).find('.errorMsgLabel').empty();
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

    $('#mainSearchBtn').on('click', function (e) {
        e.preventDefault();
        var mainSearchInput = $("input[name='mainSearchInput']").val();
        var nameCb = $("input[name='nameCb']").prop('checked');
        var ownerCb = $("input[name='ownerCb']").prop('checked');
        var commentContentCb = $("input[name='commentContentCb']").prop('checked');

        var params = '?mainSearchFilter='
            + mainSearchInput + '&' + 'nameIncluded='
            + nameCb + '&ownerIncluded=' + ownerCb
            + '&commentContentIncluded=' + commentContentCb;

        console.log(params);

        window.location.replace("videos.html" + params);
    });

    // //Jedan ajax poziv koji dobavlja korisnika koji se gleda i ulogovanog korisnika !!!!!
    // // Potreban radi onemogucivanja odredjenih funkcionalnosti
    // $.get('GetViewingUserServlet', { 'username': username }, function (data) {
    //     var _loggedInUser = data.loggedInUser;
    //     var _viewingUser = data.viewingUser;
    //     console.log('Ulogovan je : ' + _loggedInUser);
    //     console.log('Posmatra se : ');
    //     console.log(_viewingUser);

    //     //Ako nije ulogovan onemoguci upload i subscribe
    //     if (_loggedInUser == null) {
    //         $('#uploadImageForm').hide();
    //         $('#showUploadVideoBtn').hide();
    //         $('#subscribeBtn').hide();
    //         $('#showEditUserBtn').hide();
    //         $('#deleteUserBtn').hide();
    //         $.each($('#uploadedVideosList > li'), function (index, video) {
    //             $(this).find('.deleteVideoBtn').hide();
    //         });
    //     }
    //     else {
    //         //Ako je ulogovan  i ako je to taj cija se strana posmatra onemoguci da sam sebe prati
    //         //Ako nije admin a pri tome je posmatrani korisnik blokiran onemoguci pracenje
    //         if ((_loggedInUser.username == username) || (_loggedInUser.role != 'ADMIN' && _viewingUser.blocked))
    //             $('#subscribeBtn').hide();
    //         //Ako nije admin i nije taj koji se posmatra onemoguci upload
    //         if (_loggedInUser.role != 'ADMIN' && _loggedInUser.username != username)
    //             $('#showUploadVideoBtn').hide();
    //     }
    // });

    //Kraj document ready funkcije
});

function getUser(param) {
    //AJAX-om idem na server po korisnika koji ce biti prikazan na stranici, a ciji je username proslijedjen kao parametar
    $.get('UserChannelServlet', param, function (data) {
        var user = data.user;
        var loggedInUser = data.loggedInUser;
        console.log('Posmatra se : ' + user.firstName + user.lastName);
        console.log('Ulogovan je : ' + loggedInUser);

        var subscribersCount = 0;
        $.each(user.subscribers, function () {
            subscribersCount += 1;
        });
        var subscribedsList = $('#subscribedsList');
        $.each(user.subscribeds, function (index, subscribed) {
            // var subscribedSubscribers = 0;
            // $.each(subscribed.subscribers, function () {
            //     subscribedSubscribers += 1;
            // });
            subscribedsList.append("<li><a href='user.html?username=" + subscribed.username + "'><img src='" + 'images/' + subscribed.thumbnailUrl + "' class='smallThumbnail'> " + subscribed.username + "</a></li><hr>");
        });
        $('.bigThumbnail').attr('src', 'images/' + user.thumbnailUrl);
        $("#uploadImageForm > input[name='username']").val(user.username);
        $('#username').text(user.username);
        $('#subscribersSpan').text(subscribersCount + ' subscribers ');
        $('#channelInfo').html("&nbsp;" +
            user.registrationDate.dayOfMonth + '/' + user.registrationDate.monthValue + '/' + user.registrationDate.year + "<br>"
            + (user.blocked ? 'blocked' : 'active') + "<br>"
            + user.role.toLowerCase() + "<br>"
            + user.channelDescription
        );

        var editUserForm = $('#editUserForm');
        editUserForm.find("input[name='firstName']").val(user.firstName);
        editUserForm.find("input[name='lastName']").val(user.lastName);
        editUserForm.find("input[name='channelDescription']").val(user.channelDescription);
        editUserForm.find("input[name='password']").val(user.password);
        editUserForm.find("input[name='email']").val(user.email);
        editUserForm.find("#roleSelect").val(user.role);
        editUserForm.find('#blockUserBtn').val(user.blocked ? 'unblock' : 'block');
        editUserForm.find('#blockUserBtn').text(user.blocked ? 'Unblock' : 'Block');

        //Ako posmatra korisnik koji nije ulogovan sakrij sve
        if (loggedInUser == null) {
            if (user.blocked) {
                $('body').empty();
                $('body').html("<p>We are sorry but this user is blocked</p>");
            }
            $('#showUploadVideoBtn').hide();
            $('#uploadVideoBtn').hide();
            $('#showEditUserBtn').hide();
            $('#editUserForm').hide();
            $('#editUserBtn').hide();
            $('#deleteUserBtn').hide();
            $('#subscribeBtn').hide();
            $('#uploadImageForm').hide();
            $('#uploadImageForm').hide();
            $('#uploadVideoBtn').hide();
        }
        //U suprotnom posmatra neko ko je ulogovan, pa ->
        else {
            if (loggedInUser.subscribeds[user.username] != null) {
                $('#subscribeBtn').text('Unsubscribe');
            }
            //... u suprotnome subscribe dugme dobija vrijednost subscribe
            else {
                $('#subscribeBtn').text('Subscribe');
            }
            //Napravi logout link i link ka profilu ulogovanog korisnika
            $('#signUpLink').hide();
            $('#loginLink').hide();
            $('#logoutLink').show();
            $('#profileLink').attr('href', 'user.html?username=' + loggedInUser.username);
            $('#profileLink').append("<span class='glyphicon glyphicon-user'></span> " + loggedInUser.username);
            $('#profileLink').show();
            //Ako je to obican korisnik a nije taj cija se stranica posmatra, ne moze upload slike, edit i brisanje, moze samo da se subscribe-uje
            if (loggedInUser.role != 'ADMIN' && loggedInUser.username != user.username) {
                if (user.blocked) {
                    $('body').empty();
                    $('body').html("<p>We are sorry but this user is blocked </p>");
                }
                $('#uploadImageForm').hide();
                $('#showEditUserBtn').hide();
                $('#deleteUserBtn').hide();
                $('#editUserForm').hide();
                $('#editUserBtn').hide();
                $('#blockUserBtn').hide();

                //Ako je uz to korisnik koji se posmatra i blokiran, onda onemoguci subscribe
                if (user.blocked || loggedInUser.blocked)
                    $('#subscribeBtn').hide();
            }
            //Ako je to obican korisnik i bas taj cija se stranica posmatra, ne moze da sebe brise, ne moze da se blokira, da se subscribe-uje sam sebi i da mijenja sebi ulogu
            else if (loggedInUser.role != 'ADMIN' && loggedInUser.username == user.username) {
                $('#deleteUserBtn').hide();
                $('#blockUserBtn').hide();
                $('#subscribeBtn').hide();
                $('#editUserForm #roleSelect').hide();

                //Ako je korisnik blokiran onemoguci editovanje
                if (loggedInUser.blocked) {
                    $('#showEditUserBtn').hide();
                    $('#editUserForm').hide();
                }
            }
            //Ako je to admin i ako sam sebe posmatra onemoguci mu da se izbrise, da se subscribe-uje sa sebi i da sebe sam blokira
            if (loggedInUser.role == 'ADMIN' && loggedInUser.username == user.username) {
                $('#deleteUserBtn').hide();
                $('#subscribeBtn').hide();
                $('#blockUserBtn').hide();
            }
        }
    });
}

function getUploadedVideos(param) {
    $.get('UploadedVideosServlet', param, function (data) {

        var uploadedVideos = data.uploadedVideos;
        var loggedInUser = data.loggedInUser;

        var uploadedVideosList = $('#uploadedVideosList');

        $.each(uploadedVideos, function (index, uploadedVideo) {
            uploadedVideosList.append(
                "<li>"
                + "<div>"
                + "<iframe  src='" + uploadedVideo.videoUrl + "'></iframe>"
                + "<button type='button' class='btn btn-danger deleteVideoBtn' data-id='" + uploadedVideo.id + "'><span class='glyphicon glyphicon-trash'></span> Delete</button>"
                + "<h4><a href='video.html?id=" + uploadedVideo.id + "'>" + uploadedVideo.name + "</a></h4>"
                + "<span>" + uploadedVideo.views + " views</span>"
                + "<span style='display:block'>" + 'uploaded on ' + uploadedVideo.dateTimeUploaded.dayOfMonth + '/' + uploadedVideo.dateTimeUploaded.monthValue + '/' + uploadedVideo.dateTimeUploaded.year + "</span>"
                + "</div>"
                + "</li>"
            );
            var currentLiSelector = "#uploadedVideosList > li:eq(" + index + ")";
            var currentLi = $(currentLiSelector);
            var currentDeleteBtnSelector = currentLiSelector + " .deleteVideoBtn";
            var currentDeleteBtn = $(currentDeleteBtnSelector);
            //Ako je korisnik cija se stranica posmatra a ako je video blokiran, onemoguci mu brisanje tog videa
            if (loggedInUser != null && loggedInUser.role != 'ADMIN' && loggedInUser.username == username && uploadedVideo.blocked) {
                currentDeleteBtn.hide();
            }
            if (loggedInUser == null) {
                if (uploadedVideo.deleted) {
                    currentLi.hide();
                }
            }
            if (loggedInUser != null && loggedInUser.role != 'ADMIN') {
                if (uploadedVideo.deleted) {
                    currentLi.hide();
                }
            }
        });

        //Ako nije ulogovan, ili ako ulogovani korisnik nije taj cija se stranica posmatra a pri tom nije admin, sakrij brisanje
        if (loggedInUser == null) {
            $('#showUploadVideoBtn').hide();
            $('.deleteVideoBtn').hide();
        }
        //U suprotnome, neki korisnik je ulogovan pa ->
        else {
            //Ako je to obican korisnik a nije taj cija se stranica posmatra, ne moze da uploaduje, i ne moze da brise videe
            if (loggedInUser.role != 'ADMIN' && loggedInUser.username != username) {
                $('#showUploadVideoBtn').hide();
                $('.deleteVideoBtn').hide();
            }
            //Ako je obican korisnik i bas taj koji se posmatra a uz to je blokiran, ne moze da uploaduje i ne moze da brise
            else if (loggedInUser.role != 'ADMIN' && loggedInUser.username == username && loggedInUser.blocked) {
                $('#showUploadVideoBtn').hide();
                $('.deleteVideoBtn').hide();
            }
        }
    });
}
