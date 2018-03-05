$(document).ready(e => {
    $('#mainSearchForm').hide();
    $.get('GetLoggedInUserServlet', function (loggedInUser) {
        if (loggedInUser == null || loggedInUser.role != 'ADMIN') {
            $('body').empty();
            $('body').html("<p>You do not have access to this page</p>");
        }
    });
    //Nakon sto je stranica ucitana, poziva se funkcija za dobavljanje SVIH korisnika (ima default parametar)
    getAllUsers();

    // tbdoy.css({'height':'100px', 'overflow':'scroll', 'display':'block'});

    $("input[name='usernameInput'").on('keyup', function (e) {
        $("#profileLink").empty();
        $('tbody').empty();

        let usernameInput = $("input[name='usernameInput'").val();
        let firstNameInput = $("input[name='firstNameInput'").val();
        let lastNameInput = $("input[name='lastNameInput']").val();
        let emailInput = $("input[name='emailInput']").val();
        let roleSelect = $("select[name='roleSelect']").val();
        let sortSelect = $("select[name='sortSelect']").val();

        let params = {
            'usernameInput': usernameInput,
            'firstNameInput': firstNameInput,
            'lastNameInput': lastNameInput,
            'emailInput': emailInput,
            'roleSelect': roleSelect,
            'sortSelect': sortSelect
        };
        console.log(params);

        getAllUsers(params);

        e.preventDefault();
        return false;
    });

    $("input[name='firstNameInput'").on('keyup', function (e) {
        $("#profileLink").empty();
        $('tbody').empty();

        let usernameInput = $("input[name='usernameInput'").val();
        let firstNameInput = $("input[name='firstNameInput'").val();
        let lastNameInput = $("input[name='lastNameInput']").val();
        let emailInput = $("input[name='emailInput']").val();
        let roleSelect = $("select[name='roleSelect']").val();
        let sortSelect = $("select[name='sortSelect']").val();

        let params = {
            'usernameInput': usernameInput,
            'firstNameInput': firstNameInput,
            'lastNameInput': lastNameInput,
            'emailInput': emailInput,
            'roleSelect': roleSelect,
            'sortSelect': sortSelect
        };
        console.log(params);

        getAllUsers(params);

        e.preventDefault();
        return false;
    });

    $("input[name='lastNameInput'").on('keyup', function (e) {
        $("#profileLink").empty();
        $('tbody').empty();

        let usernameInput = $("input[name='usernameInput'").val();
        let firstNameInput = $("input[name='firstNameInput'").val();
        let lastNameInput = $("input[name='lastNameInput']").val();
        let emailInput = $("input[name='emailInput']").val();
        let roleSelect = $("select[name='roleSelect']").val();
        let sortSelect = $("select[name='sortSelect']").val();

        let params = {
            'usernameInput': usernameInput,
            'firstNameInput': firstNameInput,
            'lastNameInput': lastNameInput,
            'emailInput': emailInput,
            'roleSelect': roleSelect,
            'sortSelect': sortSelect
        };
        console.log(params);

        getAllUsers(params);

        e.preventDefault();
        return false;
    });

    $("input[name='emailInput'").on('keyup', function (e) {
        $("#profileLink").empty();
        $('tbody').empty();

        let usernameInput = $("input[name='usernameInput'").val();
        let firstNameInput = $("input[name='firstNameInput'").val();
        let lastNameInput = $("input[name='lastNameInput']").val();
        let emailInput = $("input[name='emailInput']").val();
        let roleSelect = $("select[name='roleSelect']").val();
        let sortSelect = $("select[name='sortSelect']").val();

        let params = {
            'usernameInput': usernameInput,
            'firstNameInput': firstNameInput,
            'lastNameInput': lastNameInput,
            'emailInput': emailInput,
            'roleSelect': roleSelect,
            'sortSelect': sortSelect
        };
        console.log(params);

        getAllUsers(params);

        e.preventDefault();
        return false;
    });

    $("select[name='roleSelect'").on('change', function (e) {
        $("#profileLink").empty();

        $('tbody').empty();

        let usernameInput = $("input[name='usernameInput'").val();
        let firstNameInput = $("input[name='firstNameInput'").val();
        let lastNameInput = $("input[name='lastNameInput']").val();
        let emailInput = $("input[name='emailInput']").val();
        let roleSelect = $("select[name='roleSelect']").val();
        let sortSelect = $("select[name='sortSelect']").val();

        let params = {
            'usernameInput': usernameInput,
            'firstNameInput': firstNameInput,
            'lastNameInput': lastNameInput,
            'emailInput': emailInput,
            'roleSelect': roleSelect,
            'sortSelect': sortSelect
        };
        console.log(params);

        getAllUsers(params);

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

    $("select[name='sortSelect']").on('change', function (e) {
        $("#profileLink").empty();
        $('tbody').empty();

        let usernameInput = $("input[name='usernameInput'").val();
        let firstNameInput = $("input[name='firstNameInput'").val();
        let lastNameInput = $("input[name='lastNameInput']").val();
        let emailInput = $("input[name='emailInput']").val();
        let roleSelect = $("select[name='roleSelect']").val();
        let sortSelect = $("select[name='sortSelect']").val();

        let params = {
            'usernameInput': usernameInput,
            'firstNameInput': firstNameInput,
            'lastNameInput': lastNameInput,
            'emailInput': emailInput,
            'roleSelect': roleSelect,
            'sortSelect': sortSelect
        };
        console.log(params);

        getAllUsers(params);

        e.preventDefault();
        return false;
    });

    $("tbody").on('click', '.deleteUserBtn', function (e) {
        var username = $(this).data('username');
        param = { 'username': username };
        console.log(param);
        $.post('DeleteUserServlet', param, function (data) {
            if (data.status == 'success') {
                getAllUsers();
            }
            else
                $('#deleteUserAlert').modal();
        });
    });

});


function getAllUsers(params = { 'usernameInput': '', 'firstNameInput': '', 'lastNameInput': '', 'emailInput': '', 'roleSelect': '', 'sortSelect': '' }) {

    $.post('UserServlet', params, function (data) {
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

            $.each(data.users, function (index, user) {
                $('tbody').append("<tr>"
                    + "<th scope='row'>" + (index + 1) + "</th>"
                    + "<td><a href='#'>" + user.username + "</a></td>"
                    + "<td>" + user.firstName + "</td>"
                    + "<td>" + user.lastName + "</td>"
                    + "<td>" + user.email + "</td>"
                    + "<td>" + user.role + "</td>"
                    + "<td>" + "<a class='btn btn-info' href='edit_user.html?username=" + user.username + "' role='button'><span class='glyphicon glyphicon-edit'></span> Edit</a>" + "</td>"
                    + "<td>" + "<button type='button' class='btn btn-warning deleteUserBtn' data-username='" + user.username + "'><span class='glyphicon glyphicon-trash'></span> Delete</button>" + "</td>"
                    + "</tr>"
                );
            });
        }
    });
}
