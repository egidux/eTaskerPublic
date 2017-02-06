$(document).ready(function() {
    $('#modal-startup').modal();
    var d = new Date().toString();
    $('#time-label').text(d.slice(0, -29));
    $('#logout').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            type : "POST",
            url : "/user/api/logout",
            success : function(data) {
                window.location = 'index.html'
            },
            error : function(e) {
                console.log("ERROR: ", e);
                json = JSON.parse(e.responseText);
                $('#modal-register-label').html("Error:");
                $('#modal-register-body').html(json.error);
                $('.modal-register').modal();
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    });
    $('#modal-startup-save').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            type : "PUT",
            url : "/user/api/profile",
            data : $("#modal-startup-form").serialize(),
            success : function(data) {
                $('#modal-startup').modal('toggle')
                $('#name-label').text("Hi, " + ($("#modal-startup-name").val()));
            },
            error : function(e) {
                console.log("ERROR: ", e);
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    });
    $('#tab-profile-alert').hide();
    $('#tab-profile-btn').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            type : "PUT",
            url : "/user/api/profile",
            data : $("#tab-profile-form").serialize(),
            success : function(data) {
                $('#tab-profile-alert').show();
                $('#name-label').text("Hi, " + ($("#profile-name").val()));
            },
            error : function(e) {
                console.log("ERROR: ", e);
                json = JSON.parse(e.responseText);
                $('#modal-register-label').html("Error:");
                $('#modal-register-body').html(json.error);
                $('.modal-register').modal();
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    })
});