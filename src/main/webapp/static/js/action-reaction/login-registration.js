$(document).ready(function(){
	var currentURL = new URI();
	
	if(currentURL.hasQuery("message", "signInSuccessful")){
		alert("Вы вошли успешно!");			
	}
	if(currentURL.hasQuery("message", "signInfailed")){
		$("#loginModal").modal("show");
		$("#signInError").append("Логин (электронная почта) или пароль введены неправильно!");	
	}
	
	if(currentURL.hasQuery("message", "registrSuccessful")){
		alert("Регистрация прошла успешно!");		
	}
	if(currentURL.hasQuery("message", "registrFailed")){
		$('#registrModal').modal("show");
		$("#registrError").append("Введены некорректные данные для регистрации!"); 			
	}	
});

