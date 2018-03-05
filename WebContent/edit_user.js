$(document).ready(e => {
    $('#mainSearchForm').hide();
    $.get('GetLoggedInUserServlet', function (loggedInUser) {
        if (loggedInUser == null || loggedInUser.role != "ADMIN") {
            $('body').empty();
            $('body').html("<p>You do not have access to this page</p>");
        }
    });


    getUser();

    $('#editUserBtn').on('click', function (e) {
        $("#profileLink").empty();
        $('#blockUserBtn').val('unblock');

        updateUser();

        e.preventDefault();
        return false;
    });

    $('#blockUserBtn').on('click', function (e) {
        $("#profileLink").empty();

        var username = $("input[name='usernameInput']").val();
        var blockOrUnblock = $(this).val();

        $.post('BlockUserServlet', { 'username': username, 'blockOrUnblock': blockOrUnblock }, function (data) {
            getUser({ 'username': username });
        });

        e.preventDefault();
        return false;
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

    $('#uploadImageBtn').on('click', function (e) {
        e.preventDefault();
        if ($('#uploadImageForm').find("input[name='thumbnail']").val == '') {
            $('#uploadImageForm').find("input[name='thumbnail']").after("<label class='errorMsgLabel'> No image selected </label>");
            return;
        }
        console.log('Kliknuto');
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
                    getUser();
                }
                else
                    $('#uploadImageForm > .errorMsgLabel').text(data.message);
            }
        });
        e.preventDefault();
    });

});

function getUser(param = { 'username': location.search.split('username=')[1].trim() }) {

    $("input[name='usernameInput']").prop('disabled', true);
    $('#blockUserBtn').val('block');
    $('#blockUserBtn').text('Block');


    $.get('UserServlet', param, function (data) {
        var user = data.user;
        if (data.loggedInUser == null || data.loggedInUser.role != 'ADMIN') {
            $('body').empty();
            $('body').html("<p>You do not have access to this page</p>");
        }
        else {
            $('#signUpLink').hide();
            $('#loginLink').hide();
            $('#logoutLink').show();
            $('#profileLink').attr('href', 'user.html?username=' + data.loggedInUser.username);
            $('#profileLink').append("<span class='glyphicon glyphicon-user'></span> " + data.loggedInUser.username);
            $('#profileLink').show();

            $('.bigThumbnail').attr('src', 'images/' + user.thumbnailUrl);
            $("#uploadImageForm > input[name='username']").val(user.username);
            $("input[name='usernameInput']").val(user.username);
            $("input[name='passwordInput']").val(user.password);
            $("input[name='firstNameInput']").val(user.firstName);
            $("input[name='lastNameInput']").val(user.lastName);
            $("input[name='emailInput']").val(user.email);
            $("select[name='roleSelect']").val(user.role);

            if (user.blocked) {
                $('#blockUserBtn').val('unblock');
                $('#blockUserBtn').text('Unblock');
            }
            else {
                $('#blockUserBtn').val('block');
                $('#blockUserBtn').text('Block');
            }
        }
    });

}



function updateUser() {
    var userUpdated = false;
    // let userDataObj = new Object();
    let usernameInput = $("input[name='usernameInput']").val();
    let passwordInput = $("input[name='passwordInput']").val();
    let firstNameInput = $("input[name='firstNameInput']").val();
    let lastNameInput = $("input[name='lastNameInput']").val();
    let emailInput = $("input[name='emailInput']").val();
    let roleSelect = $("select[name='roleSelect'] option:selected").val();
    // let params = JSON.stringify(userDataObj);

    let params = { 'usernameInput': usernameInput, 'passwordInput': passwordInput, 'firstNameInput': firstNameInput, 'lastNameInput': lastNameInput, 'emailInput': emailInput, 'roleSelect': roleSelect };
    console.log(params);

    $.post('EditUserServlet', params, function (data) {
        if (data.status == 'success') {
            getUser();
            window.location.replace('administration.html');
        }
        else
            alert('Something went wrong');
    });
}
