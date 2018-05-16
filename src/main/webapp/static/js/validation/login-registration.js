$(document).ready( function () {
	$( "#registrationForm" ).validate( {
		rules: {
			reg_login: "required",
			reg_password: {
				required: true,
				minlength: 6
			},
			reg_confirm_password: {
				required: true,
				minlength: 6,
				equalTo: "#reg_password"
			},
			reg_email: {
				required: true,
				email: true
			},
		},		
	} );

	$( "#signInForm" ).validate( {
		rules: {
			loginOrEmail: "required",
			password: "required"
		},
	} );
	
	
} );
