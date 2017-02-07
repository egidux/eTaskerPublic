$(document).ready(function() {
    //NAV BAR TOP DATE INFO
    var d = new Date().toString();
    $('#time-label').text(d.slice(0, -29));

    //ON START PAGE CHECK IF TO SHOW STARTUP MODAL
    $.ajax({
        type : "GET",
        url : "/user/api/users/" + sessionStorage.getItem("id"),
        success : function(json) {
            console.log(JSON.stringify(json));
            if (json.name == null && json.companyname == null) {
                $('#modal-startup').modal();
            } else {
                sessionStorage.setItem("name", json.name);
                sessionStorage.setItem("companyname", json.companyname);
                $('#name-label').text("Hi, " + json.name);
            }
        },
        error : function(e) {
            console.log("ERROR: ", e);
        },
        done : function(e) {
            console.log("DONE");
        }
    });

    //STARTUP MODAL DETAILS SAVE BTN LISTENER
    $('#modal-startup-save').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            type : "PUT",
            url : "/user/api/profile",
            data : $("#modal-startup-form").serialize(),
            success : function(data) {
                $('#modal-startup').modal('toggle')
                $('#name-label').text("Hi, " + ($("#modal-startup-name").val()));
                sessionStorage.setItem("name", $('#modal-startup-name').val());
                sessionStorage.setItem("companyname", $('#modal-startup-companyname').val());
            },
            error : function(e) {
                console.log("ERROR: ", e);
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    });

    //LOGOUT BTN LISTENER
    $('#logout').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            type : "POST",
            url : "/user/api/logout",
            success : function(data) {
                window.location = 'index.html'
                sessionStorage.clear();
            },
            error : function(e) {
                console.log("ERROR: ", e);
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    });

    //TAB PROFILE SAVE BTN LISTENER
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
                sessionStorage.setItem('email', $('#profile-email').val());
                sessionStorage.setItem('name', $('#profile-name').val());
                sessionStorage.setItem('companyname', $('#profile-companyname').val());
            },
            error : function(e) {
                console.log("ERROR: ", e);
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    })

    //TAB PASSWORD BTN LISTENER
    $('#tab-password-alert').hide();
    $('#tab-password-btn').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            type : "PUT",
            url : "/user/api/password",
            data : $("#tab-password-form").serialize(),
            success : function(data) {
                $('#tab-password-alert').show();
            },
            error : function(e) {
                console.log("ERROR: ", e);
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    })

    //TAB LAYOUT BTN LISTENER
    $('#tab-layout-alert').hide();
    $('#tab-layout-alert-error').hide();
    $('#tab-layout-btn').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            type : "PUT",
            url : "/user/api/report",
            data : $("#tab-layout-form").serialize(),
            success : function(data) {
                $('#tab-layout-alert-error').hide();
                $('#tab-layout-alert').show();
            },
            error : function(e) {
                console.log("ERROR: ", e);
                json = JSON.parse(e.responseText);
                $('#tab-layout-alert-erro-text').html(json.error);
                $('#tab-layout-alert-error').show();
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    });

    //NAV LEFT SETTINGS LISTENER
    $('#nav-left-settings').on('click', function(e) {
        e.preventDefault();
        $('#profile-email').val(sessionStorage.getItem('email'));
        $('#profile-name').val(sessionStorage.getItem('name'));
        $('#layout-companyname').val(sessionStorage.getItem('companyname'));
        $.ajax({
            type : "GET",
            url : "/user/api/report",
            success : function(data) {
                $('#layout-companyaddress').val(data.company_address);
                $('#layout-companyphone').val(data.company_phone);
                $('#layout-companycode').val(data.company_code);
                $('#layout-text').val(data.report_text);
                $('#layout-companyaddress').val(data.company_address);
                $('#cb-price').prop("checked", data.show_price);
                $('#cb-description').prop("checked", data.show_description);
                $('#cb-duration').prop("checked", data.show_duration);
                $('#cb-finish').prop("checked", data.show_finish);
                $('#cb-start').prop("checked", data.show_start);
            },
            error : function(e) {
                console.log("ERROR: ", e);
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    })

    $('#btn-new-worker').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            type : "POST",
            url : "/user/api/workers",
            data : $("#form-new-worker").serialize(),
            success : function(data) {
                
            },
            error : function(e) {
                console.log("ERROR: ", e);
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    })
});