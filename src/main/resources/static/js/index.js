$(document).ready(function() {
	$('#register').on('submit', function(e) {
		e.preventDefault();
		$.ajax({
			type : "POST",
			url : "/user/api/register",
			data : $('#register').serialize(),
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
