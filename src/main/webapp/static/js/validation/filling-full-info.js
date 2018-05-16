$(document).ready( function () {
	$( "#userInfoForm" ).validate( {
		rules: {
			name: "required",
			middleName: "required",
			surname: "required",
			address: "required",
			passport: "required",
			telephone: "required",
		},		
	} );
	
} );
