$(document).ready(function() {

    /*
    Register btn listener
     */
    $('.btn-register').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            type : "POST",
            url : "/user/api/customer",
            data : $(".register-login-form").serialize(),
            success : function(data) {
                console.log(JSON.stringify(data));
            	$('#modal-register-label').html('Registration Successful!');
            	$('#modal-register-body').html("Please verify email: " + $('.input-email').val());
            	$('.modal-register').modal()
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

    /*
    Login btn listener
     */
    $('.btn-login').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            type : "POST",
            url : "/user/api/login",
            data : $(".register-login-form").serialize(),
            success : function(json) {
                console.log(JSON.stringify(json));
                sessionStorage.setItem('id', json.id);
                sessionStorage.setItem("email", json.email);
                console.log("sessionStorage id:" + json.id);
            	window.location = 'admin.html'
            },
            error : function(e) {
                console.log("ERROR: ", e);
                json = JSON.parse(e.responseText);
                $('#modal-register-label').html("Error:");
            	$('#modal-register-body').html(json.error);
            	$('.modal-register').modal()
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    });
});