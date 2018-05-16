$(document).ready( function () {
	$( "#changeProductForm" ).validate( {
		rules: {
			name: "required",
			nonPatentName: "required",
			producer: "required",
			form: "required",
			prescriptionRadioOption: "required",
			quantity: {
				required: true,
				digits: true,
				min: 0
			},
			price: {
				required: true,
				number: true
			},
		},

		errorPlacement: function(error, element) {
			if (element.is(":radio"))
				error.appendTo(element.parent().next());
			else if (element.is(":checkbox"))
				error.appendTo(element.next());
			else
				error.appendTo(element.parent());
		},

	} );

} );