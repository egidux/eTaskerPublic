$(document).ready(function() {
	$('#login').on('submit', function(e) {
		e.preventDefault();
		$.ajax({
			type : "POST",
			url : "/user/api/login",
			data : $('#login').serialize(),
			success : function(data) {
				window.location = 'logout.html'
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