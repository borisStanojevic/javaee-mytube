$(document).ready(e => {

    $.get('HomeServlet', function(data){

        $('h1').text(data.status);
    });


});