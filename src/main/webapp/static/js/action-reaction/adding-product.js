$(document).ready(function(){
	var currentURL = new URI();
	
	if(currentURL.hasQuery("message", "addSuccessful")){
		alert("товар добавлен успешно!");
		
	}
	if(currentURL.hasQuery("message", "addFailed")){
		$("#addProductError").append("Заполните все поля корректными данными!");
	}	
});

