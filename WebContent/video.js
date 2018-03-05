var videoId = location.search.split('id=')[1].trim();
$(document).ready(e => {
    $('#mainSearchForm').hide();
    videoId = location.search.split('id=')[1].trim();
    getVideo({ 'id': videoId, 'load': 'load' });
    getVideoComments({ 'id': videoId, 'sortSelect': '', 'limitSelect': '' });

    $('#sortSelect').on('change', function (e) {
        var sortSelect = $(this).val();
        var limitSelect = $('#limitSelect').val();
        let params = { 'id': videoId, 'sortSelect': sortSelect, 'limitSelect': limitSelect };
        console.log(params);
        $('#commentsList').empty();
        getVideoComments(params);
        e.preventDefault();
    });

    $('#limitSelect').on('change', function (e) {
        var limitSelect = $(this).val();
        var sortSelect = $('#sortSelect').val();
        let params = { 'id': videoId, 'sortSelect': sortSelect, 'limitSelect': limitSelect };
        console.log(params);
        $('#commentsList').empty();
        getVideoComments(params);
        e.preventDefault();
    });

    $('#toggleEditBtn').on('click', function (e) {
        $('#editVideoSection').slideToggle('fast');
        e.preventDefault();
    });

    $('#editVideoBtn').on('click', function (e) {
        var name = $('#name').val();
        if (name.length < 6) {
            $('#name').after("<label class='errorMsgLabel'> Video name should be at least 6 characters long</label>");
            return;
        }
        var description = $('#description').val();
        var visibility = $('#visibility').val();
        var commentsAllowed = $('#commentsAllowed').prop('checked');
        var ratingVisible = $('#ratingVisible').prop('checked');

        $('#sortSelect').val('ratingDesc');
        $('#limitSelect').val('all');
        $('#commentsList').empty();

        let params = { 'id': videoId, 'name': name, 'description': description, 'visibility': visibility, 'commentsAllowed': commentsAllowed, 'ratingVisible': ratingVisible };
        console.log(params);

        $.post('VideoServlet', params, function (status) {
            console.log(status);
            if (status == 'success') {
                $('#editVideoSection .errorMsgLabel').empty();
                $("#profileLink").empty();
                $('#blockVideoBtn').empty();
                getVideo({ 'id': videoId });
                getVideoComments({ 'id': videoId, 'sortSelect': '', 'limitSelect': '' });
            }
            else
                $('#editVideoSection').find('.errorMsgLabel').text('We did not manage to edit video info');
        });
        e.preventDefault();
    });

    $('#deleteVideoBtn').on('click', function (e) {
        $.post('DeleteVideoServlet', { 'id': videoId }, function (data) {
            if (data.status == 'success') {
                window.location.replace("user.html?username=" + data.redirectionPage);
            }
            else {
                $("#deleteVideoAlert").modal();
            }
        });
        e.preventDefault();
    });

    $('#blockVideoBtn').on('click', function (e) {
        var blockOrUnblock = $(this).val();
        let params = { 'id': videoId, 'blockOrUnblock': blockOrUnblock };
        console.log(params);

        $.post('BlockVideoServlet', params, function (status) {
            console.log(status);
            if (status == 'success') {
                $("#profileLink").empty();
                $("#blockVideoBtn").empty();
                getVideo({ 'id': videoId });
            }
            else
                $('#editVideoSection').find('.errorMsgLabel').text('We did not manage to block this video');
        });
        e.preventDefault();
    });

    $('#likeVideoBtn').on('click', function (e) {
        var likeOrDislike = $(this).val();
        let params = { 'id': videoId, 'likeOrDislike': likeOrDislike };
        console.log(params);
        $.post('LikeVideoServlet', params, function (status) {
            if (status == 'success') {
                $("#profileLink").empty();
                $('#blockVideoBtn').empty();
                getVideo({ 'id': videoId });
            }
            else
                $(this).after("<label class='errorMsgLabel'> Failed to like/dislike. Try again</label>")
        });
        e.preventDefault();
    });

    $('#commentsList').on('click', '.editCommentBtn', function (e) {
        var index = $(this).data('index');
        var commentId = $(this).data('commentid');
        var editCommentDialog = $('#editCommentDialog');

        $.get("CommentServlet", { "id": commentId }, function (comment) {
            editCommentDialog.find("textarea[name='content']").val(comment.content);
            editCommentDialog.find("input[name='index']").val(index);
            editCommentDialog.find("input[name='id']").val(comment.id);
            editCommentDialog.modal();
        });
        e.preventDefault();
    });

    $('#editCommentSubmit').on('click', function (e) {
        var index = $("#editCommentDialog").find("input[name='index']").val();
        var commentId = $('#editCommentDialog').find("input[name='id']").val();
        var content = $('#editCommentDialog').find("textarea[name='content']").val();

        $.post('EditCommentServlet', { "id": commentId, "content": content }, function (data) {
            if (data.status == 'success') {
                $("#commentsList").empty();
                getVideoComments({ 'id': videoId, 'sortSelect': $("#sortSelect").val(), 'limitSelect': $("#limitSelect").val() });
            }
            else {
                editCommentDialog.find('.errorMsgLabel').text("Failed to edit comment. Try again");
            }

        });
        e.preventDefault();
    });

    $('#commentsList').on('click', '.deleteCommentBtn', function (e) {
        var commentId = $(this).data('commentid');
        console.log('ID komentara : ' + commentId);
        var index = $(this).data('index');
        $('#deleteCommentDialog').find("input[name='id']").val(commentId);
        $('#deleteCommentDialog').find("input[name='index']").val(index);
        $('#deleteCommentDialog').modal();
    });

    $('#deleteCommentSubmit').on('click', function (e) {
        var deleteCommentDialog = $('#deleteCommentDialog');
        var commentId = deleteCommentDialog.find("input[name='id']").val();

        console.log('ID komentara : ' + commentId);

        $.post('DeleteCommentServlet', { "id": commentId }, function (status) {
            if (status == 'success') {
                $("#commentsList").empty();
                getVideoComments({ 'id': videoId, 'sortSelect': $("#sortSelect").val(), 'limitSelect': $("#limitSelect").val() });
            }
            else {
                deleteCommentDialog.find('.errorMsgLabel').text("Failed to delete comment. Try again");
            }
        });
        e.preventDefault();
    });

    $('#commentsList').on('click', '.likeCommentBtn', function (e) {
        var likeOrDislike = $(this).val();
        var commentId = $(this).data('commentid');

        $.post('LikeCommentServlet', { 'id': commentId, 'likeOrDislike': likeOrDislike }, function (status) {
            if (status == 'success') {
                $('#commentsList').empty();
                getVideoComments({ 'id': videoId, 'sortSelect': $("#sortSelect").val(), 'limitSelect': $("#limitSelect").val() });
            }
            else {
                $(this).after("<label class='errorMsgLabel'> Failed to like/dislike. Try again</label>");
            }
        });
        e.preventDefault();
    });

    $('#commentBtn').on('click', function (e) {
        var content = $('#myCommentInput').val();

        if (content.length < 1 || content.length > 499) {
            $('#commentBtn').before("<label class='errorMsgLabel'>Your comment should be between 1 and 500 characters long</label>");
            return;
        }

        $.post('CommentVideoServlet', { 'content': content, 'videoId': videoId }, function (data) {
            if (data.status == 'success') {
                $("#commentsList").empty();
                getVideoComments({ 'id': videoId, 'sortSelect': '', 'limitSelect': '' });
            }
            else {
                $(this).before("<label class='errorMsgLabel'> Failed to post. Try again </label>");
            }
        });
        e.preventDefault();
    });

    $('#subscribeBtn').on('click', function (e) {
        $.post('SubscriptionServlet', { 'subscribedUsername': $("#ownerSpan > a").text() }, function (status) {
            if (status == 'success') {
                $("#profileLink").empty();
                $('#blockVideoBtn').empty();
                getVideo({ 'id': videoId });
            }
            else {
                $(this).before("<label class='errorMsgLabel'> Failed to subscribe/unsubscribe. Try again</label>");
            }
        });
        e.preventDefault();
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

    // //Privilegije
    // $.get('GetLoggedInUserServlet', function (data) {
    // let loggedInUser = data;

    // if (loggedInUser == null) {
    // $.each($('#commentsList > li'), function (index, comment) {
    // $(this).find('.likeCommentBtn').hide();
    // });
    // $('#myCommentInput').hide();
    // $('#commentBtn').hide();
    // $('#likeVideoBtn').hide();
    // $('#subscribeBtn').hide();
    // $('#toggleEditBtn').hide();
    // $('#deleteVideoBtn').hide();
    // if (('#commentsAllowed').prop('checked') == false)
    // $('#commentsSection').hide();
    // }
    // else {
    // if (loggedInUser.role == 'ADMIN') {

    // }

    // else if (loggedInUser.role == 'USER' && !loggedInUser.blocked &&
    // $('#blockVideoBtn').val() == 'unblock' &&
    // $('#commentsAllowed').prop('checked') == true) {
    // $.each($('#commentsList > li'), function (index, comment) {
    // var currentLiSelector = "#commentsList > li:eq(" + index + ")";
    // var currentLi = $(currentLiSelector);
    // var currentLikeBtnSelector = currentLiSelector + " .likeCommentBtn";
    // var currentLikeBtn = $(currentLikeBtnSelector);
    // var currentCommentId = $(currentLikeBtn).data('commentid');

    // if (loggedInUser.commentLikes[currentCommentId] != null) {
    // if (loggedInUser.commentLikes[currentCommentId].like) {
    // $(currentLikeBtn).val('dislike');
    // $(currentLikeBtn).text('Dislike');
    // }
    // else {
    // $(currentLikeBtn).val('like');
    // $(currentLikeBtn).text('Like');
    // }
    // }
    // else {
    // $(currentLikeBtn).val('like');
    // $(currentLikeBtn).text('Like');
    // }
    // });
    // }
    // }

    // });

});


function getVideo(params) {
    $.get('VideoServlet', params, function (data) {
        var loggedInUser = data.loggedInUser;
        if (data.video == null) {
            $('body .container').empty();
            $('body .container').html('<p> We are sorry but the video you are looking for does not exist </p>');
        }
        else {
            // Na prazan html kalup za video i informacije o videu lijepim
            // vrijednosti polja dobavljenog videa sa servera
            $('.embed-responsive-item').attr('src', data.video.videoUrl);
            $('h2').text(data.video.name);
            $('#viewsSpan').text(data.video.views + ' views');
            $('#ownerSpan > img').attr('src', 'images/' + data.video.owner.thumbnailUrl);
            $('#ownerSpan > a').attr('href', ('user.html?username=' + data.video.owner.username));
            $('#ownerSpan > a').text(data.video.owner.username);
            $('#dtUploadedSpan').text(data.video.dateTimeUploaded.dayOfMonth + '/' + data.video.dateTimeUploaded.monthValue + '/' + data.video.dateTimeUploaded.year);
            $('#descriptionParagraph').text(data.video.description);
            $('#likesSpan').text(data.video.likes);
            $('#dislikesSpan').text(data.video.dislikes);

            var likeVideoBtn = $('#likeVideoBtn');
            var subscribeBtn = $('#subscribeBtn');
            var subscribed = data.video.owner;

            // Ako pristpa korisnik koji nije ulogovan, a video ili korisnik je
            // blokiran, prikazi poruku da je blokiran
            if (loggedInUser == null) {
                if (!data.video.ratingVisible || data.video.visibility != 'PUBLIC') {
                    $('#likesSpan').hide();
                    $('#dislikesSpan').hide();
                }
                if (data.video.visibility == 'PRIVATE') {
                    $('body').empty();
                    $('body').html("<p>We are sorry but this video is private</p>");
                }
                if (data.video.blocked || data.video.owner.blocked) {
                    $('body').empty();
                    $('body').html("<p>We are sorry but this content is blocked</p>");
                }
                // ... u suprotnome onemoguci funkcionalnosti
                else {
                    $('#toggleEditBtn').hide();
                    $('#blockVideoBtn').hide();
                    $('#editVideoSection').hide();
                    $('#deleteVideoBtn').hide();
                    $('#likeVideoBtn').hide();
                    $('#subscribeBtn').hide();
                }
            }
            // U suprotnome pristupa korisnik koji je ulogovan, pa - >
            else {
                if (loggedInUser.username == data.video.owner.username)
                    $('#subscribeBtn').hide();

                $('#signUpLink').hide();
                $('#loginLink').hide();
                $('#logoutLink').show();
                $('#profileLink').attr('href', 'user.html?username=' + loggedInUser.username);
                $('#profileLink').append("<span class='glyphicon glyphicon-user'></span> " + loggedInUser.username);
                $('#profileLink').show();
                // Ako je korisnik lajkovao ovaj video
                if (loggedInUser.videoLikes[videoId] != null) {
                    // ... ako je to lajk, lajk dugme dobija vrijednost i text
                    // dislike, jer ce sledeca akcija na klik biti dislajkovanje
                    if (loggedInUser.videoLikes[videoId].like) {
                        likeVideoBtn.val('dislike');
                        likeVideoBtn.text('Dislike');
                    }
                    // ... ako je to dislike, lajk dugme dobija vrijednost i
                    // text like, jer ce sledeca akcija na klik biti lajkovanje
                    else {
                        likeVideoBtn.val('like');
                        likeVideoBtn.text('Like');
                    }
                }
                else {
                    likeVideoBtn.val('like');
                    likeVideoBtn.text('Like');
                }
                // Ako je onaj ko pristupa videu pretplacen na kanal korisnika
                // ciji je video, subscribe dugme dobija tekst unsubscribe
                if (loggedInUser.subscribeds[subscribed.username] != null) {
                    subscribeBtn.text('Unsubscribe');
                }
                // ... u suprotnome subscribe dugme dobija vrijednost subscribe
                else {
                    subscribeBtn.text('Subscribe');
                }

                // Ako pristupa obican korisnik i bas taj ciji je video
                if (loggedInUser.role != 'ADMIN' && loggedInUser.username == data.video.owner.username) {
                    $('#blockVideoBtn').hide();
                    $('#subscribeBtn').hide();
                    // Ako je uz to jos i video blokiran ili je korisnik
                    // blokiran onemoguci brisanje i editovanje i lajkovanje
                    if (data.video.blocked || loggedInUser.blocked == true) {
                        $('#deleteVideoBtn').hide();
                        $('#toggleEditBtn').hide();
                        $('#editVideoSection').hide();
                        $('#likeVideoBtn').hide();
                    }
                }
                // Ako je to obican korisnik a nije taj ciji je video
                else if (loggedInUser.role != 'ADMIN' && loggedInUser.username != data.video.owner.username) {
                    if (data.video.blocked || data.video.owner.blocked) {
                        $('body').empty();
                        $('body').html("<p>We are sorry but this content is blocked</p>");
                    }
                    $('#toggleEditBtn').hide();
                    $('#deleteVideoBtn').hide();
                    $('#blockVideoBtn').hide();
                    $('#editVideoSection').hide();
                    // ... ako je korisnik ciji je video, blokiran, ili je onaj
                    // koji pristupa blokiran onemoguci subscribe
                    if (data.video.owner.blocked || loggedInUser.blocked) {
                        $('#subscribeBtn').hide();
                    }
                    // ... ako je sam video blokiran, ili korisik koji pristupa
                    // je blokiran, onemoguci lajkovanje
                    if (data.video.blocked || loggedInUser.blocked) {
                        $('#likeVideoBtn').hide();
                    }
                    // ... ako je vlasnik videa onemogucio vidljivost rating-a,
                    // ili video nije public, sakrij rating
                    if (!data.video.ratingVisible || data.video.visibility != 'PUBLIC') {
                        $('#likesSpan').hide();
                        $('#dislikesSpan').hide();
                    }
                }

                $("subscribeBtn").data('subscribed', data.video.owner.username);
                $('#name').val(data.video.name);
                $('#description').val(data.video.description);
                $('#visibility').val(data.video.visibility);
                $('#commentsAllowed').prop('checked', data.video.commentsAllowed ? true : false);
                $('#ratingVisible').prop('checked', data.video.ratingVisible ? true : false);
                $('#blockVideoBtn').val(data.video.blocked ? 'unblock' : 'block');
                $('#blockVideoBtn').append("<span class='glyphicon glyphicon-alert'></span>" + (data.video.blocked ? ' Unblock' : ' Block'));
            }
        }
    });
}

function getVideoComments(params) {
    // Naknadno za taj isti video idem na server po sve komentare koji njemu
    // pripadaju
    $.get('VideoCommentsServlet', params, function (data) {
        var loggedInUser = data.loggedInUser;
        var video = data.video;
        var videoComments = data.videoComments;

        var commentsSection = $('#commentsSection');
        var commentsList = $("#commentsList");

        // $.each(videoComments, function (index, comment) {

        // commentsList.append(
        // "<li>"
        // + "<div class='panel panel-default'>"

        // + "<div class='panel-heading'>"
        // + "<img src='images/" + comment.user.thumbnailUrl + "'
        // class='smallThumbnail'>"
        // + "<a href='user.html?username=" + comment.user.username + "'>" +
        // comment.user.username + " </a>"
        // + "<span>" + ' posted on ' + comment.dateTimePosted.dayOfMonth + '/'
        // + comment.dateTimePosted.monthValue + '/' +
        // comment.dateTimePosted.year + ' ' + "</span>"
        // + "<button type='button' class='btn btn-default btn-sm
        // editCommentBtn' data-index='" + index + "' data-commentid='" +
        // comment.id + "'><span class='glyphicon
        // glyphicon-edit'></span></button>"
        // + "<button type='button' class='btn btn-danger btn-sm
        // deleteCommentBtn' data-index='" + index + "' data-commentid='" +
        // comment.id + "'><span class='glyphicon
        // glyphicon-trash'></span></button>"
        // + "</div>"
        // + "<div class='panel-body'>"
        // + comment.content
        // + "<br>"
        // + "<hr>"
        // + "<button type='button' class='btn btn-default btn-sm
        // likeCommentBtn' data-index='" + index + "' data-commentid='" +
        // comment.id + "'>"
        // + "</button>"
        // + "<span>" + ' ' + comment.likes + ' ' + comment.dislikes + "</span>"
        // + " </div>"
        // + "</div>"
        // + "</li>"
        // );

        // });

        // Ako pristupa korisnik koji nije ulogovan - >
        if (loggedInUser == null) {
            // Onemoguci komentarisanje
            $('#commentBtn').hide();
            $('#myCommentInput').hide();

            $.each(videoComments, function (index, comment) {
                if (comment.deleted == false) {
                    commentsList.append(
                        "<li>"
                        + "<div class='panel panel-default'>"

                        + "<div class='panel-heading'>"
                        + "<img src='images/" + comment.user.thumbnailUrl + "' class='smallThumbnail'>"
                        + "<a href='user.html?username=" + comment.user.username + "'>" + comment.user.username + " </a>"
                        + "<span>" + ' posted on ' + comment.dateTimePosted.dayOfMonth + '/' + comment.dateTimePosted.monthValue + '/' + comment.dateTimePosted.year + ' ' + "</span>"
                        + "<button type='button' class='btn btn-default btn-sm editCommentBtn' data-index='" + index + "' data-commentid='" + comment.id + "'><span class='glyphicon glyphicon-edit'></span></button>"
                        + "<button type='button' class='btn btn-danger btn-sm deleteCommentBtn' data-index='" + index + "' data-commentid='" + comment.id + "'><span class='glyphicon glyphicon-trash'></span></button>"
                        + "</div>"
                        + "<div class='panel-body'>"
                        + comment.content
                        + "<br>"
                        + "<hr>"
                        + "<button type='button' class='btn btn-default btn-sm likeCommentBtn' data-index='" + index + "' data-commentid='" + comment.id + "'>"
                        + "</button>"
                        + "<span>" + ' ' + comment.likes + ' ' + comment.dislikes + "</span>"
                        + " </div>"
                        + "</div>"
                        + "</li>"
                    );
                }
            });

            // Onemoguci editovanje, brisanje i lajkovanje komentara
            commentsList.find('.editCommentBtn').hide();
            commentsList.find('.deleteCommentBtn').hide();
            commentsList.find('.likeCommentBtn').hide();

            // Naknadno ako video nije public ili vlasnik videa nije dozvolio
            // komentare, sakrij komentare
            if (video.visibility != 'PUBLIC' || video.commentsAllowed == false)
                commentsSection.hide();
        }
        // U suprotnome, pristupa korisik koji je ulogovan, pa ->
        else {
            // ... ako je taj korisnik admin
            if (loggedInUser.role == 'ADMIN') {
                $.each(videoComments, function (index, comment) {

                    commentsList.append(
                        "<li>"
                        + "<div class='panel panel-default'>"

                        + "<div class='panel-heading'>"
                        + "<img src='images/" + comment.user.thumbnailUrl + "' class='smallThumbnail'>"
                        + "<a href='user.html?username=" + comment.user.username + "'>" + comment.user.username + " </a>"
                        + "<span>" + ' posted on ' + comment.dateTimePosted.dayOfMonth + '/' + comment.dateTimePosted.monthValue + '/' + comment.dateTimePosted.year + ' ' + "</span>"
                        + "<button type='button' class='btn btn-default btn-sm editCommentBtn' data-index='" + index + "' data-commentid='" + comment.id + "'><span class='glyphicon glyphicon-edit'></span></button>"
                        + "<button type='button' class='btn btn-danger btn-sm deleteCommentBtn' data-index='" + index + "' data-commentid='" + comment.id + "'><span class='glyphicon glyphicon-trash'></span></button>"
                        + "</div>"
                        + "<div class='panel-body'>"
                        + comment.content
                        + "<br>"
                        + "<hr>"
                        + "<button type='button' class='btn btn-default btn-sm likeCommentBtn' data-index='" + index + "' data-commentid='" + comment.id + "'>"
                        + "</button>"
                        + "<span>" + ' ' + comment.likes + ' ' + comment.dislikes + "</span>"
                        + " </div>"
                        + "</div>"
                        + "</li>"
                    );
                });

                // Prodji kroz listu komentara, za svaki komentar vidi da li ga
                // je ulogovani korisnik lajkovao/dislajkovao i postavi
                // odgovarajucu vrijednost lajk dugmetu
                $.each($('#commentsList > li'), function (index, comment) {
                    console.log('Trenutni index : ' + index);
                    var currentLiSelector = "#commentsList > li:eq(" + index + ")";
                    var currentLi = $(currentLiSelector);
                    var currentLikeBtnSelector = currentLiSelector + " .likeCommentBtn";
                    var currentLikeBtn = $(currentLikeBtnSelector);
                    var currentCommentId = $(currentLikeBtn).data('commentid');
                    // Ako je korisnik lajkovao/dislajkovao komentar na kojem se
                    // trenutno nalazimo u iteraciji ...
                    if (loggedInUser.commentLikes[currentCommentId] != null) {
                        // ... vidi da li je to lajk pa postavi vrijednost
                        // dislike jer ce sledeca akcija da bude dislike
                        if (loggedInUser.commentLikes[currentCommentId].like) {
                            currentLikeBtn.val('dislike');
                            currentLikeBtn.text('Dislike');
                        }
                        // ... ili je to dislike pa postavi vrijednost like jer
                        // ce sledeca akcija da bude like
                        else {
                            currentLikeBtn.val('like');
                            currentLikeBtn.text('Like');
                        }
                    }
                    else {
                        currentLikeBtn.val('like');
                        currentLikeBtn.text('Like');
                    }
                });

            }
            // Ako ulogovani korisnik nije admin i nije taj ciji je video - >
            else if (loggedInUser.role != 'ADMIN' && video.owner.username != loggedInUser.username) {
                // Ako je vlasnik onemogucio komentare ili je korisnik koji
                // pristupa blokiran ili je video blokiran, onemoguci
                // kometarisanje
                if (loggedInUser.blocked || video.blocked) {
                    $('#myCommentInput').hide();
                    $('#commentBtn').hide();
                }

                $.each(videoComments, function (index, comment) {
                    if (comment.deleted == false) {
                        commentsList.append(
                            "<li>"
                            + "<div class='panel panel-default'>"

                            + "<div class='panel-heading'>"
                            + "<img src='images/" + comment.user.thumbnailUrl + "' class='smallThumbnail'>"
                            + "<a href='user.html?username=" + comment.user.username + "'>" + comment.user.username + " </a>"
                            + "<span>" + ' posted on ' + comment.dateTimePosted.dayOfMonth + '/' + comment.dateTimePosted.monthValue + '/' + comment.dateTimePosted.year + ' ' + "</span>"
                            + "<button type='button' class='btn btn-default btn-sm editCommentBtn' data-index='" + index + "' data-commentid='" + comment.id + "'><span class='glyphicon glyphicon-edit'></span></button>"
                            + "<button type='button' class='btn btn-danger btn-sm deleteCommentBtn' data-index='" + index + "' data-commentid='" + comment.id + "'><span class='glyphicon glyphicon-trash'></span></button>"
                            + "</div>"
                            + "<div class='panel-body'>"
                            + comment.content
                            + "<br>"
                            + "<hr>"
                            + "<button type='button' class='btn btn-default btn-sm likeCommentBtn' data-index='" + index + "' data-commentid='" + comment.id + "'>"
                            + "</button>"
                            + "<span>" + ' ' + comment.likes + ' ' + comment.dislikes + "</span>"
                            + " </div>"
                            + "</div>"
                            + "</li>"
                        );
                    }
                });

                // onemoguci izmjenu i brisanje
                commentsList.find('.editCommentBtn').hide();
                commentsList.find('.deleteCommentBtn').hide();

                $.each($('#commentsList > li'), function (index, comment) {
                    var currentLiSelector = "#commentsList > li:eq(" + index + ")";
                    var currentLi = $(currentLiSelector);
                    var currentLikeBtnSelector = currentLiSelector + " .likeCommentBtn";
                    var currentLikeBtn = $(currentLikeBtnSelector);
                    var currentCommentId = $(currentLikeBtn).data('commentid');
                    console.log('RIle TITO : ' + currentLi.find('a').text())
                    // Ako je korisnik lajkovao/dislajkovao komentar na kojem se
                    // trenutno nalazimo u iteraciji ...
                    if (loggedInUser.commentLikes[currentCommentId] != null) {
                        // ... vidi da li je to lajk pa postavi vrijednost
                        // dislike jer ce sledeca akcija da bude dislike
                        if (loggedInUser.commentLikes[currentCommentId].like) {
                            currentLikeBtn.val('dislike');
                            currentLikeBtn.text('Dislike');
                        }
                        // ... ili je to dislike pa postavi vrijednost like jer
                        // ce sledeca akcija da bude like
                        else {
                            currentLikeBtn.val('like');
                            currentLikeBtn.text('Like');
                        }
                    }
                    else {
                        currentLikeBtn.val('like');
                        currentLikeBtn.text('Like');
                    }
                    var currentEditBtnSelector = currentLiSelector + " .editCommentBtn";
                    var currentEditBtn = $(currentEditBtnSelector);
                    var currentDeleteBtnSelector = currentLiSelector + " .deleteCommentBtn";
                    var currentDeleteBtn = $(currentDeleteBtnSelector);
                    // Ako je ulogovani korisnik vlasnik komentara na kojem se
                    // trenutno nalazimo u iteraciji, a nije blokiran , omoguci
                    // mu izmjenu i brisanje komentara
                    if (loggedInUser.username == currentLi.find('a').text().trim() && loggedInUser.blocked == false) {
                        currentEditBtn.show();
                        currentDeleteBtn.show();
                    }
                });

                // Ako je video blokiran ili je onaj koji pristupa blokiran
                // onemoguci dodavanje komentara
                if (video.blocked == true || loggedInUser.blocked == true) {
                    $('#myCommentInput').hide();
                    $('#commentBtn').hide();
                    $('.editCommentBtn').hide();
                    $('.deleteCommentBtn').hide();
                    $('.likeCommentBtn').hide();
                }
                // Ako video nije public ili vlasnik nije dozvolio komentare
                // onemoguci komentare
                if (video.visibility != 'PUBLIC' || video.commentsAllowed == false) {
                    commentsSection.hide();
                }

            }

            // Ako korisnik nije admin i bas je taj ciji je video onda ->
            else if (loggedInUser.role != 'ADMIN' && loggedInUser.username == video.owner.username) {
                // Ako je korisnik blokiran ili je video blokiran ne moze
                // komentarisat
                if (loggedInUser.blocked || video.blocked) {
                    $('#myCommentInput').hide();
                    $('#commentBtn').hide();
                    $('.likeCommentBtn').hide();
                }

                $.each(videoComments, function (index, comment) {
                    if (comment.deleted == false) {
                        commentsList.append(
                            "<li>"
                            + "<div class='panel panel-default'>"

                            + "<div class='panel-heading'>"
                            + "<img src='images/" + comment.user.thumbnailUrl + "' class='smallThumbnail'>"
                            + "<a href='user.html?username=" + comment.user.username + "'>" + comment.user.username + " </a>"
                            + "<span>" + ' posted on ' + comment.dateTimePosted.dayOfMonth + '/' + comment.dateTimePosted.monthValue + '/' + comment.dateTimePosted.year + ' ' + "</span>"
                            + "<button type='button' class='btn btn-default btn-sm editCommentBtn' data-index='" + index + "' data-commentid='" + comment.id + "'><span class='glyphicon glyphicon-edit'></span></button>"
                            + "<button type='button' class='btn btn-danger btn-sm deleteCommentBtn' data-index='" + index + "' data-commentid='" + comment.id + "'><span class='glyphicon glyphicon-trash'></span></button>"
                            + "</div>"
                            + "<div class='panel-body'>"
                            + comment.content
                            + "<br>"
                            + "<hr>"
                            + "<button type='button' class='btn btn-default btn-sm likeCommentBtn' data-index='" + index + "' data-commentid='" + comment.id + "'>"
                            + "</button>"
                            + "<span>" + ' ' + comment.likes + ' ' + comment.dislikes + "</span>"
                            + " </div>"
                            + "</div>"
                            + "</li>"
                        );
                    }
                });

                commentsList.find('.editCommentBtn').hide();
                commentsList.find('.deleteCommentBtn').hide();

                $('#commentsList > li').each(function (index) {
                    var comment = $(this);
                    var currentLiSelector = "#commentsList > li:eq(" + index + ")";
                    var currentLi = $(currentLiSelector);
                    var currentCommentOwnerUsername = currentLi.find('a').text().trim();
                    var currentLikeBtnSelector = currentLiSelector + " .likeCommentBtn";
                    var currentLikeBtn = $(currentLikeBtnSelector);
                    var currentCommentId = $(currentLikeBtn).data('commentid');
                    console.log('Trenutno : ' + index + ', vlasnik komentara : ' + currentCommentOwnerUsername);

                    var currentEditBtnSelector = currentLiSelector + " .editCommentBtn";
                    var currentEditBtn = $(currentEditBtnSelector);
                    var currentDeleteBtnSelector = currentLiSelector + " .deleteCommentBtn";
                    var currentDeleteBtn = $(currentDeleteBtnSelector);
                    // Ako je ulogovani korisnik vlasnik komentara na kojem se
                    // trenutno nalazimo u iteraciji, a nije blokiran , omoguci
                    // mu izmjenu i brisanje komentara
                    if (loggedInUser.username == currentCommentOwnerUsername && loggedInUser.blocked == false && video.blocked == false) {
                        currentEditBtn.show();
                        currentDeleteBtn.show();
                    }

                    // Ako je korisnik lajkovao/dislajkovao komentar na kojem se
                    // trenutno nalazimo u iteraciji ...
                    if (loggedInUser.commentLikes[currentCommentId] != null) {
                        // ... vidi da li je to lajk pa postavi vrijednost
                        // dislike jer ce sledeca akcija da bude dislike
                        if (loggedInUser.commentLikes[currentCommentId].like) {
                            currentLikeBtn.val('dislike');
                            currentLikeBtn.text('Dislike');
                        }
                        // ... ili je to dislike pa postavi vrijednost like jer
                        // ce sledeca akcija da bude like
                        else {
                            currentLikeBtn.val('like');
                            currentLikeBtn.text('Like');
                        }
                    }
                    else {
                        currentLikeBtn.val('like');
                        currentLikeBtn.text('Like')
                    }
                });
            }
        }
    });
}