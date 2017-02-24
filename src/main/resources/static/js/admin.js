/**
 * GOOGLE map API
 */
//autocomplete address
var clientAddress, clientAddressEdit, objectAddress, objectAddressEdit, objectClientAddress, taskClientAddress,
    taskObjectAddress, taskObjectClientAddress, layoutAddress, calendarClient, calendarObjectClient, calendarObject;
function initAutocomplete() {
    clientAddress = new google.maps.places.Autocomplete(
        (document.getElementById('client-address')), {types: ['geocode']});
    clientAddressEdit = new google.maps.places.Autocomplete(
        (document.getElementById('client-address-edit')), {types: ['geocode']});
    objectAddress = new google.maps.places.Autocomplete(
        (document.getElementById('object-address')), {types: ['geocode']});
    objectAddressEdit = new google.maps.places.Autocomplete(
        (document.getElementById('object-address-edit')), {types: ['geocode']});
    objectClientAddress = new google.maps.places.Autocomplete(
        (document.getElementById('object-client-address')), {types: ['geocode']});
    taskClientAddress = new google.maps.places.Autocomplete(
        (document.getElementById('task-client-address')), {types: ['geocode']});
    taskObjectAddress = new google.maps.places.Autocomplete(
        (document.getElementById('task-object-address')), {types: ['geocode']});
    taskObjectClientAddress = new google.maps.places.Autocomplete(
        (document.getElementById('task-object-client-address')), {types: ['geocode']});
    layoutAddress = new google.maps.places.Autocomplete(
        (document.getElementById('layout-companyaddress')), {types: ['geocode']});
    calendarClient = new google.maps.places.Autocomplete(
        (document.getElementById('calendar-task-client-address')), {types: ['geocode']});
    calendarObjectClient = new google.maps.places.Autocomplete(
        (document.getElementById('calendar-task-object-client-address')), {types: ['geocode']});
    calendarObject = new google.maps.places.Autocomplete(
        (document.getElementById('calendar-task-object-address')), {types: ['geocode']});
    google.maps.event.addListener(objectAddress, 'place_changed', function () {
        var place = objectAddress.getPlace();
        document.getElementById('object-lat').value = place.geometry.location.lat();
        document.getElementById('object-lng').value = place.geometry.location.lng();
    });
    google.maps.event.addListener(objectAddressEdit, 'place_changed', function () {
        var place = objectAddressEdit.getPlace();
        document.getElementById('object-lat-edit').value = place.geometry.location.lat();
        document.getElementById('object-lng-edit').value = place.geometry.location.lng();
    });
    google.maps.event.addListener(taskObjectAddress, 'place_changed', function () {
        var place = taskObjectAddress.getPlace();
        document.getElementById('task-object-lat').value = place.geometry.location.lat();
        document.getElementById('task-object-lng').value = place.geometry.location.lng();
    });
    google.maps.event.addListener(calendarObject, 'place_changed', function () {
        var place = calendarObject.getPlace();
        document.getElementById('calendar-task-object-lat').value = place.geometry.location.lat();
        document.getElementById('calendar-task-object-lng').value = place.geometry.location.lng();
    });

}
function geolocate() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var geolocation = {
                lat: position.coords.latitude,
                lng: position.coords.longitude
            };
            var circle = new google.maps.Circle({
                center: geolocation,
                radius: position.coords.accuracy
            });
            clientAddress.setBounds(circle.getBounds());
            clientAddressEdit.setBounds(circle.getBounds());
            objectAddress.setBounds(circle.getBounds());
            objectAddressEdit.setBounds(circle.getBounds());
            objectClientAddress.setBounds(circle.getBounds());
            taskClientAddress.setBounds(circle.getBounds());
            taskObjectAddress.setBounds(circle.getBounds());
            taskObjectClientAddress.setBounds(circle.getBounds());
            layoutAddress.setBounds(circle.getBounds());
            calendarClient.setBounds(circle.getBounds());
            calendarObjectClient.setBounds(circle.getBounds());
            calendarObject.setBounds(circle.getBounds());
        });
    }
}

var mapDashboard;
function initDashboardMap() {

    mapDashboard = new google.maps.Map(document.getElementById('map-container-dashboard'), {
        center: {lat: -34.397, lng: 150.644},
        zoom: 15
    });
    var infoWindow = new google.maps.InfoWindow({map: mapDashboard});

    // Try HTML5 geolocation.
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var pos = {
                lat: position.coords.latitude,
                lng: position.coords.longitude
            };

            infoWindow.setPosition(pos);
            infoWindow.setContent('I am here !');
            mapDashboard.setCenter(pos);
        }, function() {
            handleLocationError(true, infoWindow, mapDashboard.getCenter());
        });
    } else {
        // Browser doesn't support Geolocation
        handleLocationError(false, infoWindow, mapDashboard.getCenter());
    }


    function handleLocationError(browserHasGeolocation, infoWindow, pos) {
        infoWindow.setPosition(pos);
        infoWindow.setContent(browserHasGeolocation ?
            'Error: The Geolocation service failed.' :
            'Error: Your browser doesn\'t support geolocation.');
    }
}

var mapPlanning;
function initPlanningMap() {

    mapPlanning = new google.maps.Map(document.getElementById('map-container-planning'), {
        center: {lat: -34.397, lng: 150.644},
        zoom: 15
    });
    var infoWindow = new google.maps.InfoWindow({map: mapPlanning});

    // Try HTML5 geolocation.
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var pos = {
                lat: position.coords.latitude,
                lng: position.coords.longitude
            };

            infoWindow.setPosition(pos);
            infoWindow.setContent('I am here !');
            mapPlanning.setCenter(pos);
        }, function() {
            handleLocationError(true, infoWindow, mapPlanning.getCenter());
        });
    } else {
        // Browser doesn't support Geolocation
        handleLocationError(false, infoWindow, mapPlanning.getCenter());
    }


    function handleLocationError(browserHasGeolocation, infoWindow, pos) {
        infoWindow.setPosition(pos);
        infoWindow.setContent(browserHasGeolocation ?
            'Error: The Geolocation service failed.' :
            'Error: Your browser doesn\'t support geolocation.');
    }
}

$(document).ready(function() {
    drawTaskTable()

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
        $(this).find("input").val('');
     });

    initAutocomplete()


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
     * NAV DASHBOARD
     */

    $('#nav-left-dashboard').on('shown.bs.tab', function(e) {
        initDashboardMap();
    });

    /**
     *
     * NAV CALENDAR
     *
     */

    function initCalendar(){
        $.ajax({
            url : "/user/api/tasks",
            type: 'GET',
            success: function(tasks) {
                var events = [];
                $.each(tasks, function(i, task) {
                    var time = task.planned_time.split(" ").pop();
                    var date = Date.parseExact(task.planned_time, "d/M/yyyy HH:mm")
                    events.push({
                        title: time + ' ' + task.worker,
                        start: date.toISOString(),
                        id: task.id
                    });
                });
                $('#calendar').fullCalendar('removeEvents');
                $('#calendar').fullCalendar('addEventSource', events);
                $('#calendar').fullCalendar({
                    height: "auto",
                    //events: events,
                    dayClick: function(date, jsEvent, view) {
                        $('#modal-calendar-create-task').modal()
                        $('#date-calendar-create-task-planned-start').datetimepicker({format: 'DD/MM/YYYY HH:mm'});
                        var tmpDate = new Date();
                        $('#date-calendar-create-task-planned-start').data("DateTimePicker").date(date.get('date') + '/' + (date.get('month')+1) + '/' + date.get('year') + ' ' + tmpDate.getHours() +
                            ':' + tmpDate.getMinutes());
                        $('#date-calendar-create-task-planned-end').datetimepicker({format: 'DD/MM/YYYY HH:mm'});
                        //FILL CLIENTS OBJECTS WORKERS
                        $.ajax({
                            type : "GET",
                            url : "/user/api/clients",
                            success : function(json) {
                                $('#calendar-create-task-client').empty();
                                $('#calendar-create-task-client').append('<option selected></option>');
                                $.each(json, function(i, obj) {
                                    $('#calendar-create-task-client').append('<option value="' + obj.name + '">' + obj.name + '</option>');
                                });
                            },
                            error : function(e) {
                                console.log("ERROR: ", e);
                            },
                            done : function(e) {
                                console.log("DONE");
                            }
                        });
                        $.ajax({
                            type : "GET",
                            url : "/user/api/objects",
                            success : function(json) {
                                $('#calendar-create-task-object').empty();
                                $('#calendar-create-task-object').append('<option selected></option>');
                                $.each(json, function(i, obj) {
                                    $('#calendar-create-task-object').append('<option value="' + obj.name + '">' + obj.name + '</option>');
                                });
                            },
                            error : function(e) {
                                console.log("ERROR: ", e);
                            },
                            done : function(e) {
                                console.log("DONE");
                            }
                        });
                        $.ajax({
                            type : "GET",
                            url : "/user/api/workers",
                            success : function(json) {
                                $('#calendar-create-task-worker').empty();
                                $('#calendar-create-task-worker').append('<option selected></option>');
                                $.each(json, function(i, obj) {
                                    $('#calendar-create-task-worker').append('<option value="' + obj.name + '">' + obj.name + '</option>');
                                });
                            },
                            error : function(e) {
                                console.log("ERROR: ", e);
                            },
                            done : function(e) {
                                console.log("DONE");
                            }
                        });
                        // BTN CALENDAR TASK MODAL NEW CLIENT SAVE LISTENER
                        $('#modal-btn-save-calendar-task-client').on('click', function(e) {
                            var clientName = $('#calendar-task-client-name').val();
                            e.preventDefault();
                            $.ajax({
                                type : "POST",
                                url : "/user/api/clients",
                                data : $("#form-calendar-task-new-client").serialize(),
                                success : function(data) {
                                    $('#modal-calendar-task-new-client').modal('toggle');
                                    $.ajax({
                                        type : "GET",
                                        url : "/user/api/clients",
                                        success : function(json) {
                                            $('#calendar-create-task-client').empty();
                                            $.each(json, function(i, obj) {
                                                if (obj.name === clientName) {
                                                    $('#calendar-create-task-client').append('<option selected value="' + obj.name + '">' + obj.name + '</option>');
                                                } else {
                                                    $('#calendar-create-task-client').append('<option value="' + obj.name + '">' + obj.name + '</option>');
                                                }
                                            });
                                        },
                                        error : function(e) {
                                            console.log("ERROR: ", e);
                                        },
                                        done : function(e) {
                                            console.log("DONE");
                                        }
                                    });
                                },
                                error : function(e) {
                                    console.log("ERROR: ", e);
                                    json = JSON.parse(e.responseText);
                                    $('#alert-calendar-task-client-modal-text').html(json.error);
                                    $('#alert-calendar-task-client-modal').show();
                                    hideAlert();
                                },
                                done : function(e) {
                                    console.log("DONE");
                                }
                            });
                        })

                        // BTN CALENDAR TASK MODAL NEW OBJECT SAVE LISTENER
                        $('#modal-btn-calendar-task-save-object').on('click', function(e) {
                            var objectName = $('#calendar-task-object-name').val();
                            e.preventDefault();
                            $.ajax({
                                type : "POST",
                                url : "/user/api/objects",
                                data : $("#form-calendar-task-new-object").serialize(),
                                success : function(data) {
                                    $('#modal-calendar-task-new-object').modal('toggle');
                                    $.ajax({
                                        type : "GET",
                                        url : "/user/api/objects",
                                        success : function(json) {
                                            $('#calendar-create-task-object').empty();
                                            $.each(json, function(i, obj) {
                                                if (obj.name === objectName) {
                                                    $('#calendar-create-task-object').append('<option selected value="' + obj.name + '">' + obj.name + '</option>');
                                                } else {
                                                    $('#calendar-create-task-object').append('<option value="' + obj.name + '">' + obj.name + '</option>');
                                                }
                                            });
                                        },
                                        error : function(e) {
                                            console.log("ERROR: ", e);
                                        },
                                        done : function(e) {
                                            console.log("DONE");
                                        }
                                    });
                                },
                                error : function(e) {
                                    console.log("ERROR: ", e);
                                    json = JSON.parse(e.responseText);
                                    $('#alert-calendar-task-object-modal-text').html(json.error);
                                    $('#alert-calendar-task-object-modal').show();
                                    hideAlert();
                                },
                                done : function(e) {
                                    console.log("DONE");
                                }
                            });
                        })

                        // BTN CALENDAR TASK MODAL OBJECT CLIENT SAVE LISTENER
                        $('#modal-btn-save-calendar-task-object-client').on('click', function(e) {
                            var clientName = $('#calendar-task-object-client-name').val();
                            e.preventDefault();
                            $.ajax({
                                type : "POST",
                                url : "/user/api/clients",
                                data : $("#form-calendar-task-object-new-client").serialize(),
                                success : function(data) {
                                    $('#modal-calendar-task-object-new-client').modal('toggle');
                                    $.ajax({
                                        type : "GET",
                                        url : "/user/api/clients",
                                        success : function(json) {
                                            $('#calendar-task-object-client').empty();
                                            $.each(json, function(i, obj) {
                                                if (obj.name === clientName) {
                                                    $('#calendar-task-object-client').append('<option selected value="' + obj.name + '">' + obj.name + '</option>');
                                                } else {
                                                    $('#calendar-task-object-client').append('<option value="' + obj.name + '">' + obj.name + '</option>');
                                                }
                                            });
                                        },
                                        error : function(e) {
                                            console.log("ERROR: ", e);
                                        },
                                        done : function(e) {
                                            console.log("DONE");
                                        }
                                    });
                                },
                                error : function(e) {
                                    console.log("ERROR: ", e);
                                    json = JSON.parse(e.responseText);
                                    $('#alert-calendar-task-object-modal-text').html(json.error);
                                    $('#alert-calendar-task-object-modal').show();
                                    hideAlert();
                                },
                                done : function(e) {
                                    console.log("DONE");
                                }
                            });
                        })

                        //BTN CALENDAR NEW OBJECT LISTENER
                        $('#btn-modal-calendar-create-task-new-object').on('click', function(e) {
                            e.preventDefault();
                            $.ajax({
                                type : "GET",
                                url : "/user/api/clients",
                                success : function(json) {
                                    $('#calendar-task-object-client').empty();
                                    $('#calendar-task-object-client').append('<option selected></option>');
                                    $.each(json, function(i, obj) {
                                        $('#calendar-task-object-client').append('<option value="' + obj.name + '">' + obj.name + '</option>');
                                    });
                                },
                                error : function(e) {
                                    console.log("ERROR: ", e);
                                },
                                done : function(e) {
                                    console.log("DONE");
                                }
                            });
                        })

                        // BTN CALENDAR NEW TASK SAVE LISTENER
                        $('#modal-btn-save-calendar-create-task').off('click');
                        $('#modal-btn-save-calendar-create-task').on('click', function(e) {
                            e.preventDefault();
                            $.ajax({
                                type : "POST",
                                url : "/user/api/tasks",
                                data : $("#form-calendar-create-task").serialize(),
                                success : function(data) {
                                    $('#modal-calendar-create-task').modal('toggle');
                                    initCalendar()
                                },
                                error : function(e) {
                                    console.log("ERROR: ", e);
                                    json = JSON.parse(e.responseText);
                                    $('#alert-calendar-create-task-modal-text').html(json.error);
                                    $('#alert-calendar-create-task-modal').show();
                                    hideAlert();
                                },
                                done : function(e) {
                                    console.log("DONE");
                                }
                            });
                        })

                    },
                    eventClick: function(event, jsEvent, view) {
                        $.ajax({
                            type : "GET",
                            url : "/user/api/tasks/" + event.id,
                            success : function(jsonTask) {
                                $('#modal-calendar-task').modal();
                                $('#calendar-task-title').val(jsonTask.title);
                                $('#calendar-task-description').val(jsonTask.description);
                                $('#date-calendar-task-planned-start').datetimepicker({format: 'DD/MM/YYYY HH:mm'});
                                $('#date-calendar-task-planned-start').data("DateTimePicker").date(jsonTask.planned_time);
                                $('#date-calendar-task-planned-end').datetimepicker({format: 'DD/MM/YYYY HH:mm'});
                                $('#date-calendar-task-planned-end').data("DateTimePicker").date(jsonTask.planned_end_time);
                                $.ajax({
                                    type : "GET",
                                    url : "/user/api/clients",
                                    success : function(jsonClients) {
                                        $('#calendar-task-client').empty();
                                        $('#calendar-task-client').append('<option></option>');
                                        $.each(jsonClients, function(i, client) {
                                            if (client.name == jsonTask.client) {
                                                $('#calendar-task-client').append('<option selected value="' + client.name + '">' + client.name + '</option>');
                                            } else {
                                                $('#calendar-task-client').append('<option value="' + client.name + '">' + client.name + '</option>');
                                            }
                                        });
                                    },
                                    error : function(e) {
                                        console.log("ERROR: ", e);
                                    },
                                    done : function(e) {
                                        console.log("DONE");
                                    }
                                });
                                $.ajax({
                                    type : "GET",
                                    url : "/user/api/objects",
                                    success : function(jsonObjects) {
                                        $('#calendar-task-object').empty();
                                        $('#calendar-task-object').append('<option></option>');
                                        $.each(jsonObjects, function(i, object) {
                                            if (object.name == jsonTask.object) {
                                                $('#calendar-task-object').append('<option selected value="' + object.name + '">' + object.name + '</option>');
                                            } else {
                                                $('#calendar-task-object').append('<option value="' + object.name + '">' + object.name + '</option>');
                                            }
                                        });
                                    },
                                    error : function(e) {
                                        console.log("ERROR: ", e);
                                    },
                                    done : function(e) {
                                        console.log("DONE");
                                    }
                                });
                                $.ajax({
                                    type : "GET",
                                    url : "/user/api/workers",
                                    success : function(jsonWorkers) {
                                        $('#calendar-task-worker').empty();
                                        $('#calendar-task-worker').append('<option></option>');
                                        $.each(jsonWorkers, function(i, worker) {
                                            if (worker.name == jsonTask.worker) {
                                                $('#calendar-task-worker').append('<option selected value="' + worker.name + '">' + worker.name + '</option>');
                                            } else {
                                                $('#calendar-task-worker').append('<option value="' + worker.name + '">' + worker.name + '</option>');
                                            }
                                        });
                                    },
                                    error : function(e) {
                                        console.log("ERROR: ", e);
                                    },
                                    done : function(e) {
                                        console.log("DONE");
                                    }
                                });
                                $('#modal-btn-edit-calendar-task').off('click');
                                $('#modal-btn-edit-calendar-task').on('click', function (e) {
                                    e.preventDefault();
                                    $.ajax({
                                        type : "PUT",
                                        url : "/user/api/tasks/" + event.id,
                                        data : $("#form-calendar-task").serialize(),
                                        success : function(json) {
                                            $('#modal-calendar-task').modal('toggle');
                                            //$('#alert-tab-task').html('Task updated');
                                            //hideAlert();
                                            //$('#alert-task').show();
                                            initCalendar()
                                        },
                                        error : function(e) {
                                            console.log("ERROR: ", e);
                                            //json = JSON.parse(e.responseText);
                                            //$('#alert-task-edit-modal-text').html(json.error);
                                            //$('#alert-task-edit-modal').show();
                                           // hideAlert();
                                        },
                                        done : function(e) {
                                            console.log("DONE");
                                        }
                                    })
                                })
                                $('#modal-btn-delete-calendar-task').off('click');
                                $('#modal-btn-delete-calendar-task').on('click', function (e) {
                                    e.preventDefault();
                                    $.ajax({
                                        type : "DELETE",
                                        url : "/user/api/tasks/" + event.id,
                                        success : function(json) {
                                            $('#modal-calendar-task').modal('toggle');
                                            //$('#alert-tab-task').html('Task deleted');
                                            //$('#alert-task').show();
                                            //hideAlert();
                                            initCalendar()
                                        },
                                        error : function(e) {
                                            console.log("ERROR: ", e);
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
                    }
                });
            }
        });
    }
    $('#nav-left-calendar').on('click', function(e) {
        setTimeout(initCalendar, 10);
    });

    /**
     * NAV TASKS
     */

    $('#date-task-planned-start').datetimepicker({format: 'DD/MM/YYYY HH:mm'});
    $('#date-task-planned-end').datetimepicker({format: 'DD/MM/YYYY HH:mm'});

    $('.nav-tabs a[href="#tab-planning"]').on('shown.bs.tab', function(e) {
        initPlanningMap()
        addMarker()
        google.maps.event.trigger(mapPlanning, 'resize');
        function drawMarker(objTask) {
            $.ajax({
                type: "GET",
                url: "/user/api/objects/name/" + objTask.object,
                success: function (jsonObject) {
                    var myLatlng = new google.maps.LatLng(jsonObject.lat, jsonObject.lng);
                    var marker = new google.maps.Marker({
                        position: myLatlng,
                        title: "Hello",
                        map: mapPlanning
                    });
                    var contentString = '<style>#left {padding-right: 5px;}</style><table> <tr> <td id="left">Task ID</td> <td>'+objTask.id+'</td> </tr> <tr> <td id="left">Client</td>' +
                        '<td>'+objTask.client+'</td> </tr> <tr> <td id="left">Location</td> <td>'+objTask.object+'</td> </tr> <tr> <td id="left">Task title</td>' +
                        '<td>'+objTask.title+'</td> </tr> <tr> <td id="left">Status</td> <td>'+objTask.status+'</td> </tr> '+
                        '<tr> <td id="left">Description</td> <td>'+objTask.description+'</td> </tr></table>';
                    var infowindow = new google.maps.InfoWindow({
                        content: contentString,
                    });
                    marker.addListener('click', function() {
                        infowindow.open(mapPlanning, marker);
                    });
                },
                error: function (e) {
                    console.log("ERROR: ", e);
                },
                done: function (e) {
                    console.log("DONE");
                }
            });
        }
        function addMarker() {
            $.ajax({
                type: "GET",
                url: "/user/api/tasks",
                success: function (jsonTask) {
                    $.each(jsonTask, function (i, objTask) {
                        drawMarker(objTask)
                    });
                },
                error: function (e) {
                    console.log("ERROR: ", e);
                },
                done: function (e) {
                    console.log("DONE");
                }
            });
        }
    });

    //Draw task table
    function drawTaskTable() {
        $.ajax({
            type : "GET",
            url : "/user/api/tasks",
            success : function(json) {
                var dataSet = [];
                $.each(json, function(i, obj) {
                    var temp = [];
                    temp.push(obj.id);
                    temp.push(obj.title);
                    temp.push(obj.worker);
                    temp.push(obj.client);
                    temp.push(obj.object);
                    temp.push(obj.status);
                    dataSet.push(temp);
                });
                var table = $('#table-task').DataTable( {
                    destroy: true,
                    data: dataSet,
                    columns: [
                        { title: "Task ID" },
                        { title: "Task title" },
                        { title: "Worker" },
                        { title: "Client" },
                        { title: "Object" },
                        { title: "Status" }
                    ]
                } );
                setTaskTableListener(table);
            },
            error : function(e) {
                console.log("ERROR: ", e);
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    }

    // BTN TASK MODAL NEW CLIENT SAVE LISTENER
    $('#modal-btn-save-task-client').on('click', function(e) {
        var clientName = $('#task-client-name').val();
        e.preventDefault();
        $.ajax({
            type : "POST",
            url : "/user/api/clients",
            data : $("#form-task-new-client").serialize(),
            success : function(data) {
                $('#modal-task-new-client').modal('toggle');
                $.ajax({
                    type : "GET",
                    url : "/user/api/clients",
                    success : function(json) {
                        $('#task-client').empty();
                        $.each(json, function(i, obj) {
                            if (obj.name === clientName) {
                                $('#task-client').append('<option selected value="' + obj.name + '">' + obj.name + '</option>');
                            } else {
                                $('#task-client').append('<option value="' + obj.name + '">' + obj.name + '</option>');
                            }
                        });
                    },
                    error : function(e) {
                        console.log("ERROR: ", e);
                    },
                    done : function(e) {
                        console.log("DONE");
                    }
                });
            },
            error : function(e) {
                console.log("ERROR: ", e);
                json = JSON.parse(e.responseText);
                $('#alert-task-client-modal-text').html(json.error);
                $('#alert-task-client-modal').show();
                hideAlert();
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    })

    // BTN TASK MODAL NEW OBJECT SAVE LISTENER
    $('#modal-btn-task-save-object').on('click', function(e) {
        var objectName = $('#task-object-name').val();
        e.preventDefault();
        $.ajax({
            type : "POST",
            url : "/user/api/objects",
            data : $("#form-task-new-object").serialize(),
            success : function(data) {
                $('#modal-task-new-object').modal('toggle');
                $.ajax({
                    type : "GET",
                    url : "/user/api/objects",
                    success : function(json) {
                        $('#task-object').empty();
                        $.each(json, function(i, obj) {
                            if (obj.name === objectName) {
                                $('#task-object').append('<option selected value="' + obj.name + '">' + obj.name + '</option>');
                            } else {
                                $('#task-object').append('<option value="' + obj.name + '">' + obj.name + '</option>');
                            }
                        });
                    },
                    error : function(e) {
                        console.log("ERROR: ", e);
                    },
                    done : function(e) {
                        console.log("DONE");
                    }
                });
            },
            error : function(e) {
                console.log("ERROR: ", e);
                json = JSON.parse(e.responseText);
                $('#alert-task-object-modal-text').html(json.error);
                $('#alert-task-object-modal').show();
                hideAlert();
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    })

    // BTN TASK MODAL OBJECT CLIENT SAVE LISTENER
    $('#modal-btn-save-task-object-client').on('click', function(e) {
        var clientName = $('#task-object-client-name').val();
        e.preventDefault();
        $.ajax({
            type : "POST",
            url : "/user/api/clients",
            data : $("#form-task-object-new-client").serialize(),
            success : function(data) {
                $('#modal-task-object-new-client').modal('toggle');
                $.ajax({
                    type : "GET",
                    url : "/user/api/clients",
                    success : function(json) {
                        $('#task-object-client').empty();
                        $.each(json, function(i, obj) {
                            if (obj.name === clientName) {
                                $('#task-object-client').append('<option selected value="' + obj.name + '">' + obj.name + '</option>');
                            } else {
                                $('#task-object-client').append('<option value="' + obj.name + '">' + obj.name + '</option>');
                            }
                        });
                    },
                    error : function(e) {
                        console.log("ERROR: ", e);
                    },
                    done : function(e) {
                        console.log("DONE");
                    }
                });
            },
            error : function(e) {
                console.log("ERROR: ", e);
                json = JSON.parse(e.responseText);
                $('#alert-task-object-modal-text').html(json.error);
                $('#alert-task-object-modal').show();
                hideAlert();
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    })

    //BTN NEW OBJECT LISTENER
    $('#btn-modal-task-new-object').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            type : "GET",
            url : "/user/api/clients",
            success : function(json) {
                $('#task-object-client').empty();
                $('#task-object-client').append('<option selected></option>');
                $.each(json, function(i, obj) {
                    $('#task-object-client').append('<option value="' + obj.name + '">' + obj.name + '</option>');
                });
            },
            error : function(e) {
                console.log("ERROR: ", e);
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    })


    //NAV LEFT TASK LISTENER
    $('#nav-left-tasks').on('click', function(e) {
        $('#ul-task li:first').find('a[data-toggle="tab"]').tab('show');
        drawTaskTable();
    });

    //BTN NEW TASK LISTENER
    $('#btn-new-task').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            type : "GET",
            url : "/user/api/clients",
            success : function(json) {
                $('#task-client').empty();
                $('#task-client').append('<option selected></option>');
                $.each(json, function(i, obj) {
                    $('#task-client').append('<option value="' + obj.name + '">' + obj.name + '</option>');
                });
            },
            error : function(e) {
                console.log("ERROR: ", e);
            },
            done : function(e) {
                console.log("DONE");
            }
        });
        $.ajax({
            type : "GET",
            url : "/user/api/objects",
            success : function(json) {
                $('#task-object').empty();
                $('#task-object').append('<option selected></option>');
                $.each(json, function(i, obj) {
                    $('#task-object').append('<option value="' + obj.name + '">' + obj.name + '</option>');
                });
            },
            error : function(e) {
                console.log("ERROR: ", e);
            },
            done : function(e) {
                console.log("DONE");
            }
        });
        $.ajax({
            type : "GET",
            url : "/user/api/workers",
            success : function(json) {
                $('#task-worker').empty();
                $('#task-worker').append('<option selected></option>');
                $.each(json, function(i, obj) {
                    $('#task-worker').append('<option value="' + obj.name + '">' + obj.name + '</option>');
                });
            },
            error : function(e) {
                console.log("ERROR: ", e);
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    })
    // BTN NEW TASK SAVE LISTENER
    $('#modal-btn-save-task').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            type : "POST",
            url : "/user/api/tasks",
            data : $("#form-new-task").serialize(),
            success : function(data) {
                drawTaskTable();
                $('#modal-task').modal('toggle');
                $('#alert-tab-task').html('New task created')
                $('#alert-task').show();
                hideAlert();
            },
            error : function(e) {
                console.log("ERROR: ", e);
                json = JSON.parse(e.responseText);
                $('#alert-task-modal-text').html(json.error);
                $('#alert-task-modal').show();
                hideAlert();
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    })

    function setTaskTableListener(table) {
        //Worker table click listener
        $('#table-task tbody').off('click');
        $('#table-task tbody').on('click', 'tr', function () {
            var data = table.row( this ).data();
            $.ajax({
                type : "GET",
                url : "/user/api/tasks/" + data[0],
                success : function(jsonTask) {
                    $('#modal-edit-task').modal();
                    $('#task-title-edit').val(jsonTask.title);
                    $('#task-description-edit').val(jsonTask.description);
                    $('#date-task-planned-start-edit').datetimepicker({format: 'DD/MM/YYYY HH:mm'});
                    $('#date-task-planned-start-edit').data("DateTimePicker").date(jsonTask.planned_time);
                    $('#date-task-planned-end-edit').datetimepicker({format: 'DD/MM/YYYY HH:mm'});
                    $('#date-task-planned-end-edit').data("DateTimePicker").date(jsonTask.planned_end_time);
                    $.ajax({
                        type : "GET",
                        url : "/user/api/clients",
                        success : function(jsonClients) {
                            $('#task-client-edit').empty();
                            $('#task-client-edit').append('<option></option>');
                            $.each(jsonClients, function(i, client) {
                                if (client.name == jsonTask.client) {
                                    $('#task-client-edit').append('<option selected value="' + client.name + '">' + client.name + '</option>');
                                } else {
                                    $('#task-client-edit').append('<option value="' + client.name + '">' + client.name + '</option>');
                                }
                            });
                        },
                        error : function(e) {
                            console.log("ERROR: ", e);
                        },
                        done : function(e) {
                            console.log("DONE");
                        }
                    });
                    $.ajax({
                        type : "GET",
                        url : "/user/api/objects",
                        success : function(jsonObjects) {
                            $('#task-object-edit').empty();
                            $('#task-object-edit').append('<option></option>');
                            $.each(jsonObjects, function(i, object) {
                                if (object.name == jsonTask.object) {
                                    $('#task-object-edit').append('<option selected value="' + object.name + '">' + object.name + '</option>');
                                } else {
                                    $('#task-object-edit').append('<option value="' + object.name + '">' + object.name + '</option>');
                                }
                            });
                        },
                        error : function(e) {
                            console.log("ERROR: ", e);
                        },
                        done : function(e) {
                            console.log("DONE");
                        }
                    });
                    $.ajax({
                        type : "GET",
                        url : "/user/api/workers",
                        success : function(jsonWorkers) {
                            $('#task-worker-edit').empty();
                            $('#task-worker-edit').append('<option></option>');
                            $.each(jsonWorkers, function(i, worker) {
                                if (worker.name == jsonTask.worker) {
                                    $('#task-worker-edit').append('<option selected value="' + worker.name + '">' + worker.name + '</option>');
                                } else {
                                    $('#task-worker-edit').append('<option value="' + worker.name + '">' + worker.name + '</option>');
                                }
                            });
                        },
                        error : function(e) {
                            console.log("ERROR: ", e);
                        },
                        done : function(e) {
                            console.log("DONE");
                        }
                    });
                    $('#modal-btn-edit-task').off('click');
                    $('#modal-btn-edit-task').on('click', function (e) {
                        e.preventDefault();
                        $.ajax({
                            type : "PUT",
                            url : "/user/api/tasks/" + data[0],
                            data : $("#form-edit-task").serialize(),
                            success : function(json) {
                                $('#modal-edit-task').modal('toggle');
                                $('#alert-tab-task').html('Task updated');
                                hideAlert();
                                $('#alert-task').show();
                                drawTaskTable();
                            },
                            error : function(e) {
                                console.log("ERROR: ", e);
                                json = JSON.parse(e.responseText);
                                $('#alert-task-edit-modal-text').html(json.error);
                                $('#alert-task-edit-modal').show();
                                hideAlert();
                            },
                            done : function(e) {
                                console.log("DONE");
                            }
                        })
                    })
                    $('#modal-btn-delete-task').off('click');
                    $('#modal-btn-delete-task').on('click', function (e) {
                        e.preventDefault();
                        $.ajax({
                            type : "DELETE",
                            url : "/user/api/tasks/" + data[0],
                            success : function(json) {
                                $('#modal-edit-task').modal('toggle');
                                $('#alert-tab-task').html('Task deleted');
                                $('#alert-task').show();
                                hideAlert();
                                drawTaskTable();
                            },
                            error : function(e) {
                                console.log("ERROR: ", e);
                                json = JSON.parse(e.responseText);
                                $('#alert-task-edit-modal-text').html(json.error);
                                $('#alert-task-edit-modal').show();
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
     * NAV CLIENTS
     */

    //Draw Client table
    function drawClientTable() {
        $.ajax({
            type : "GET",
            url : "/user/api/clients",
            success : function(json) {
                var dataSet = [];
                $.each(json, function(i, obj) {
                    var temp = [];
                    temp.push(obj.id);
                    temp.push(obj.name);
                    temp.push(obj.code);
                    temp.push(obj.email);
                    temp.push(obj.address);
                    temp.push(obj.phone);
                    dataSet.push(temp);
                });
                var table = $('#table-client').DataTable( {
                    destroy: true,
                    data: dataSet,
                    columns: [
                        { title: "ID" },
                        { title: "Company Name" },
                        { title: "Company code" },
                        { title: "Email" },
                        { title: "Address" },
                        { title: "Phone" }
                    ]
                } );
                setClientTableListener(table);
            },
            error : function(e) {
                console.log("ERROR: ", e);
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    }
    //NAV LEFT Client LISTENER
    $('#nav-left-clients').on('click', function(e) {
        drawClientTable();
    });

    // BTN NEW CLIENT SAVE LISTENER
    $('#modal-btn-save-client').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            type : "POST",
            url : "/user/api/clients",
            data : $("#form-new-client").serialize(),
            success : function(data) {
                drawClientTable();
                $('#modal-client').modal('toggle');
                $('#alert-tab-client').html('New Client created')
                $('#alert-client').show();
                hideAlert();
            },
            error : function(e) {
                console.log("ERROR: ", e);
                json = JSON.parse(e.responseText);
                $('#alert-client-modal-text').html(json.error);
                $('#alert-client-modal').show();
                hideAlert();
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    })

    function setClientTableListener(table) {
        //Client table click listener
        $('#table-client tbody').off('click');
        $('#table-client tbody').on('click', 'tr', function () {
            var data = table.row( this ).data();
            $.ajax({
                type : "GET",
                url : "/user/api/clients/" + data[0],
                success : function(json) {
                    $('#modal-edit-client').modal();
                    $('#client-name-edit').val(json.name);
                    $('#client-code-edit').val(json.code);
                    $('#client-email-edit').val(json.email);
                    $('#client-address-edit').val(json.address);
                    $('#client-phone-edit').val(json.phone);

                    $('#modal-btn-edit-client').off('click');
                    $('#modal-btn-edit-client').on('click', function (e) {
                        e.preventDefault();
                        $.ajax({
                            type : "PUT",
                            url : "/user/api/clients/" + data[0],
                            data : $("#form-edit-client").serialize(),
                            success : function(json) {
                                $('#modal-edit-client').modal('toggle');
                                $('#alert-tab-client').html('Client updated');
                                hideAlert();
                                $('#alert-client').show();
                                drawClientTable();
                            },
                            error : function(e) {
                                console.log("ERROR: ", e);
                                json = JSON.parse(e.responseText);
                                $('#alert-client-edit-modal-text').html(json.error);
                                $('#alert-client-edit-modal').show();
                                hideAlert();
                            },
                            done : function(e) {
                                console.log("DONE");
                            }
                        })
                    })
                    $('#modal-btn-delete-client').off('click');
                    $('#modal-btn-delete-client').on('click', function (e) {
                        e.preventDefault();
                        $.ajax({
                            type : "DELETE",
                            url : "/user/api/clients/" + data[0],
                            success : function(json) {
                                $('#modal-edit-client').modal('toggle');
                                $('#alert-tab-client').html('Client deleted');
                                $('#alert-client').show();
                                hideAlert();
                                drawClientTable();
                            },
                            error : function(e) {
                                console.log("ERROR: ", e);
                                json = JSON.parse(e.responseText);
                                $('#alert-client-edit-modal-text').html(json.error);
                                $('#alert-client-edit-modal').show();
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
     * NAV LOCATIONS
     */

    //Draw Object table
    function drawObjectTable() {
        $.ajax({
            type : "GET",
            url : "/user/api/objects",
            success : function(json) {
                var dataSet = [];
                $.each(json, function(i, obj) {
                    var temp = [];
                    temp.push(obj.id);
                    temp.push(obj.name);
                    temp.push(obj.address);
                    temp.push(obj.client);
                    dataSet.push(temp);
                });
                var table = $('#table-object').DataTable( {
                    destroy: true,
                    data: dataSet,
                    columns: [
                        { title: "ID" },
                        { title: "Name" },
                        { title: "Address" },
                        { title: "Client" }
                    ]
                } );
                setObjectTableListener(table);
            },
            error : function(e) {
                console.log("ERROR: ", e);
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    }

    //NAV LEFT OBJECTS LISTENER
    $('#nav-left-objects').on('click', function(e) {
        drawObjectTable();
    });

    // BTN OBJECT MODAL NEW CLIENT SAVE LISTENER
    $('#modal-btn-save-object-client').on('click', function(e) {
        var clientName = $('#object-client-name').val();
        e.preventDefault();
        $.ajax({
            type : "POST",
            url : "/user/api/clients",
            data : $("#form-object-new-client").serialize(),
            success : function(data) {
                $('#modal-object-new-client').modal('toggle');
                $.ajax({
                    type : "GET",
                    url : "/user/api/clients",
                    success : function(json) {
                        $('#object-client').empty();
                        $.each(json, function(i, obj) {
                            if (obj.name === clientName) {
                                $('#object-client').append('<option selected value="' + obj.name + '">' + obj.name + '</option>');
                            } else {
                                $('#object-client').append('<option value="' + obj.name + '">' + obj.name + '</option>');
                            }
                        });
                    },
                    error : function(e) {
                        console.log("ERROR: ", e);
                    },
                    done : function(e) {
                        console.log("DONE");
                    }
                });
            },
            error : function(e) {
                console.log("ERROR: ", e);
                json = JSON.parse(e.responseText);
                $('#alert-object-new-client-modal-text').html(json.error);
                $('#alert-object-client-modal').show();
                hideAlert();
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    })

    //BTN NEW OBJECT LISTENER
    $('#btn-new-object').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            type : "GET",
            url : "/user/api/clients",
            success : function(json) {
                $('#object-client').empty();
                $('#object-client').append('<option selected></option>');
                $.each(json, function(i, obj) {
                    $('#object-client').append('<option value="' + obj.name + '">' + obj.name + '</option>');
                });
            },
            error : function(e) {
                console.log("ERROR: ", e);
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    })
    // BTN NEW OBJECT SAVE LISTENER
    $('#modal-btn-save-object').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            type : "POST",
            url : "/user/api/objects",
            data : $("#form-new-object").serialize(),
            success : function(data) {
                drawObjectTable();
                $('#modal-object').modal('toggle');
                $('#alert-tab-object').html('New location created')
                $('#alert-object').show();
                hideAlert();
            },
            error : function(e) {
                console.log("ERROR: ", e);
                json = JSON.parse(e.responseText);
                $('#alert-object-modal-text').html(json.error);
                $('#alert-object-modal').show();
                hideAlert();
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    })

    function setObjectTableListener(table) {
        //Worker table click listener
        $('#table-object tbody').off('click');
        $('#table-object tbody').on('click', 'tr', function () {
            var data = table.row( this ).data();
            $.ajax({
                type : "GET",
                url : "/user/api/objects/" + data[0],
                success : function(json) {
                    $('#modal-edit-object').modal();
                    $('#object-name-edit').val(json.name);
                    $('#object-address-edit').val(json.address);
                    $.ajax({
                        type : "GET",
                        url : "/user/api/clients",
                        success : function(json1) {
                            $('#object-client-edit').empty();
                            $.each(json1, function(i, obj) {
                                if (obj.name === json.client) {
                                    $('#object-client-edit').append('<option selected value="' + obj.name + '">' + obj.name + '</option>');
                                } else {
                                    $('#object-client-edit').append('<option value="' + obj.name + '">' + obj.name + '</option>');
                                }
                            });
                        },
                        error : function(e) {
                            console.log("ERROR: ", e);
                        },
                        done : function(e) {
                            console.log("DONE");
                        }
                    });
                    $('#modal-btn-edit-object').off('click');
                    $('#modal-btn-edit-object').on('click', function (e) {
                        e.preventDefault();
                        $.ajax({
                            type : "PUT",
                            url : "/user/api/objects/" + data[0],
                            data : $("#form-edit-object").serialize(),
                            success : function(json) {
                                $('#modal-edit-object').modal('toggle');
                                $('#alert-tab-object').html('Location updated');
                                hideAlert();
                                $('#alert-object').show();
                                drawObjectTable();
                            },
                            error : function(e) {
                                console.log("ERROR: ", e);
                                json = JSON.parse(e.responseText);
                                $('#alert-object-edit-modal-text').html(json.error);
                                $('#alert-object-edit-modal').show();
                                hideAlert();
                            },
                            done : function(e) {
                                console.log("DONE");
                            }
                        })
                    })
                    $('#modal-btn-delete-object').off('click');
                    $('#modal-btn-delete-object').on('click', function (e) {
                        e.preventDefault();
                        $.ajax({
                            type : "DELETE",
                            url : "/user/api/objects/" + data[0],
                            success : function(json) {
                                $('#modal-edit-object').modal('toggle');
                                $('#alert-tab-object').html('Location deleted');
                                $('#alert-object').show();
                                hideAlert();
                                drawObjectTable();
                            },
                            error : function(e) {
                                console.log("ERROR: ", e);
                                json = JSON.parse(e.responseText);
                                $('#alert-object-edit-modal-text').html(json.error);
                                $('#alert-object-edit-modal').show();
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
     * NAV MATERIALS
     */

    //Draw Material table
    function drawMaterialTable() {
        $.ajax({
            type : "GET",
            url : "/user/api/materials",
            success : function(json) {
                var dataSet = [];
                $.each(json, function(i, obj) {
                    var temp = [];
                    temp.push(obj.id);
                    temp.push(obj.name);
                    temp.push(obj.unit);
                    temp.push(obj.price + ' EUR');
                    dataSet.push(temp);
                });
                var table = $('#table-material').DataTable( {
                    destroy: true,
                    data: dataSet,
                    columns: [
                        { title: "ID" },
                        { title: "Name" },
                        { title: "Unit" },
                        { title: "Price" }
                    ]
                } );
                setMaterialTableListener(table);
            },
            error : function(e) {
                console.log("ERROR: ", e);
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    }

    //NAV LEFT MATERIALS LISTENER
    $('#nav-left-materials').on('click', function(e) {
        drawMaterialTable();
    });

    // BTN NEW MATERIAL SAVE LISTENER
    $('#modal-btn-save-material').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            type : "POST",
            url : "/user/api/materials",
            data : $("#form-new-material").serialize(),
            success : function(data) {
                drawMaterialTable();
                $('#modal-material').modal('toggle');
                $('#alert-tab-material').html('New material created')
                $('#alert-material').show();
                hideAlert();
            },
            error : function(e) {
                console.log("ERROR: ", e);
                json = JSON.parse(e.responseText);
                $('#alert-material-modal-text').html(json.error);
                $('#alert-material-modal').show();
                hideAlert();
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    })

    function setMaterialTableListener(table) {
        $('#table-material tbody').off('click');
        $('#table-material tbody').on('click', 'tr', function () {
            var data = table.row( this ).data();
            $.ajax({
                type : "GET",
                url : "/user/api/materials/" + data[0],
                success : function(json) {
                    $('#modal-edit-material').modal();
                    $('#material-name-edit').val(json.name);
                    $('#material-unit-edit').val(json.unit);
                    $('#material-price-edit').val(json.price);

                    $('#modal-btn-edit-material').off('click');
                    $('#modal-btn-edit-material').on('click', function (e) {
                        e.preventDefault();
                        $.ajax({
                            type : "PUT",
                            url : "/user/api/materials/" + data[0],
                            data : $("#form-edit-material").serialize(),
                            success : function(json) {
                                $('#modal-edit-material').modal('toggle');
                                $('#alert-tab-material').html('Material updated');
                                hideAlert();
                                $('#alert-material').show();
                                drawMaterialTable();
                            },
                            error : function(e) {
                                console.log("ERROR: ", e);
                                json = JSON.parse(e.responseText);
                                $('#alert-material-edit-modal-text').html(json.error);
                                $('#alert-material-edit-modal').show();
                                hideAlert();
                            },
                            done : function(e) {
                                console.log("DONE");
                            }
                        })
                    })
                    $('#modal-btn-delete-material').off('click');
                    $('#modal-btn-delete-material').on('click', function (e) {
                        e.preventDefault();
                        $.ajax({
                            type : "DELETE",
                            url : "/user/api/materials/" + data[0],
                            success : function(json) {
                                $('#modal-edit-material').modal('toggle');
                                $('#alert-tab-material').html('Material deleted');
                                $('#alert-material').show();
                                hideAlert();
                                drawMaterialTable();
                            },
                            error : function(e) {
                                console.log("ERROR: ", e);
                                json = JSON.parse(e.responseText);
                                $('#alert-material-edit-modal-text').html(json.error);
                                $('#alert-material-edit-modal').show();
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