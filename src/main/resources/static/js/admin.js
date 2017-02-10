$(document).ready(function() {

    //NAV BAR TOP DATE INFO
    var d = new Date().toString();
    $('#time-label').text(d.slice(0, -29));

    //Hide alerts
    $("[data-hide]").on("click", function(){
        $(this).closest("." + $(this).attr("data-hide")).hide();
    });
    function hideAlert() {
        $(".alert").delay(2500).slideUp(500, function() {
            $(this).slideUp(500);
        });
    }

    //RESET MODALS
    $('.modal').on('hidden.bs.modal', function () {
        $(this).find("input,textarea,select").val('');
     });


    /**
     * START MODAL
     */

    //when admin page loaded check if to show startup modal
    $.ajax({
        type : "GET",
        url : "/user/api/users/" + sessionStorage.getItem("id"),
        success : function(json) {
            console.log(JSON.stringify(json));
            if (json.name == null && json.companyname == null) {
                $('#modal-startup').modal();

                //start up modal save btn listener
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


    /**
     * LOGOUT BTN
     */

    //logout btn listener
    $('#logout').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            type : "POST",
            url : "/user/api/logout",
            success : function() {
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

    /**
     * NAV WORKERS
     */

    //Draw Worker table
    function drawWorkerTable() {
        $.ajax({
            type : "GET",
            url : "/user/api/workers",
            success : function(json) {
                var dataSet = [];
                $.each(json, function(i, obj) {
                    var temp = [];
                    temp.push(obj.id);
                    temp.push(obj.name);
                    temp.push(obj.email);
                    temp.push(obj.created);
                    temp.push(obj.updated);
                    dataSet.push(temp);
                });
                var table = $('#table-worker').DataTable( {
                    destroy: true,
                    data: dataSet,
                    columns: [
                        { title: "ID" },
                        { title: "Name" },
                        { title: "Email" },
                        { title: "Created" },
                        { title: "Updated" }
                    ]
                } );
               setWorkerTableListener(table);
            },
            error : function(e) {
                console.log("ERROR: ", e);
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    }

    //NAV LEFT WORKERS LISTENER
    $('#nav-left-workers').on('click', function(e) {
        drawWorkerTable();
    });

    // BTN NEW WORKER SAVE LISTENER
    $('#modal-btn-save-worker').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            type : "POST",
            url : "/user/api/workers",
            data : $("#form-new-worker").serialize(),
            success : function(data) {
                drawWorkerTable();
                $('#modal-worker').modal('toggle');
                $('#alert-tab-worker').html('New Worker created')
                $('#alert-worker').show();
                hideAlert();
            },
            error : function(e) {
                console.log("ERROR: ", e);
                json = JSON.parse(e.responseText);
                $('#alert-worker-modal-text').html(json.error);
                $('#alert-worker-modal').show();
                hideAlert();
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    })

    function setWorkerTableListener(table) {
        //Worker table click listener
        $('#table-worker tbody').off('click');
        $('#table-worker tbody').on('click', 'tr', function () {
            var data = table.row( this ).data();
            $.ajax({
                type : "GET",
                url : "/user/api/workers/" + data[0],
                success : function(json) {
                    $('#modal-edit-worker').modal();
                    $('#worker-name-edit').val(json.name);
                    $('#worker-email-edit').val(json.email);
                    $('#worker-password-edit').val(json.password);

                    $('#modal-btn-edit-worker').off('click');
                    $('#modal-btn-edit-worker').on('click', function (e) {
                        e.preventDefault();
                        $.ajax({
                            type : "PUT",
                            url : "/user/api/workers/" + data[0],
                            data : $("#form-edit-worker").serialize(),
                            success : function(json) {
                                $('#modal-edit-worker').modal('toggle');
                                $('#alert-tab-worker').html('Worker updated');
                                hideAlert();
                                $('#alert-worker').show();
                                drawWorkerTable();
                            },
                            error : function(e) {
                                console.log("ERROR: ", e);
                                json = JSON.parse(e.responseText);
                                $('#alert-worker-edit-modal-text').html(json.error);
                                $('#alert-worker-edit-modal').show();
                                hideAlert();
                            },
                            done : function(e) {
                                console.log("DONE");
                            }
                        })
                    })
                    $('#modal-btn-delete-worker').off('click');
                    $('#modal-btn-delete-worker').on('click', function (e) {
                        e.preventDefault();
                        $.ajax({
                            type : "DELETE",
                            url : "/user/api/workers/" + data[0],
                            success : function(json) {
                                $('#modal-edit-worker').modal('toggle');
                                $('#alert-tab-worker').html('Worker deleted');
                                $('#alert-worker').show();
                                hideAlert();
                                drawWorkerTable();
                            },
                            error : function(e) {
                                console.log("ERROR: ", e);
                                json = JSON.parse(e.responseText);
                                $('#alert-worker-edit-modal-text').html(json.error);
                                $('#alert-worker-edit-modal').show();
                                hideAlert();
                            },
                            done : function(e) {
                                console.log("DONE");
                            }
                        })
                    })
                },
                error : function(e) {
                    console.log("ERROR: ", e);
                },
                done : function(e) {
                    console.log("DONE");
                }
            });
        } );
    }

    /**
     * NAV SETTINGS
     */

    //nav settings listener
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

    //TAB PROFILE SAVE BTN LISTENER
    $('#tab-profile-btn').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            type : "PUT",
            url : "/user/api/profile",
            data : $("#tab-profile-form").serialize(),
            success : function(data) {
                $('#tab-profile-alert').show();
                hideAlert()
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
    $('#tab-password-btn').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            type : "PUT",
            url : "/user/api/password",
            data : $("#tab-password-form").serialize(),
            success : function(data) {
                $('#tab-password-alert').show();
                hideAlert()
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
    $('#tab-layout-btn').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            type : "PUT",
            url : "/user/api/report",
            data : $("#tab-layout-form").serialize(),
            success : function(data) {
                $('#tab-layout-alert-error').hide();
                $('#tab-layout-alert').show();
                hideAlert();
            },
            error : function(e) {
                console.log("ERROR: ", e);
                json = JSON.parse(e.responseText);
                $('#tab-layout-alert-erro-text').html(json.error);
                $('#tab-layout-alert-error').show();
                hideAlert();
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    });
});