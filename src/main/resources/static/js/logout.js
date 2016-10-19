$(document).ready(function() {
	$('#logout').on('submit', function(e) {
		e.preventDefault();
		$.ajax({
			type : "GET",
			url : "/user/api/logout",
			success : function(data) {
				window.location = 'login.html'
			},
			error : function(e) {
				console.log("ERROR: ", e);
				json = JSON.parse(e.responseText);
				$('#info').text('*' + json.error);
			},
			done : function(e) {
				console.log("DONE");
			}
		});
	});
});