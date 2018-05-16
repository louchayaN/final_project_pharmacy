$(document).ready( function () {
	$( "#addProductForm" ).validate( {
		rules: {
			name_ru: "required",
			nonPatentName_ru: "required",
			producer_ru: "required",
			form_ru: "required",
			file_ru: "required",
			name_ru: "required",
			name_en: "required",
			nonPatentName_en: "required",
			producer_en: "required",
			form_en: "required",
			file_en: "required",
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
