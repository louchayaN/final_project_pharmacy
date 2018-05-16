$(document).ready(function(){
	var currentURL = new URI();
	
	if(currentURL.hasQuery("message", "fillInfo")){
		$("#userInfoModel").modal("show");
		$("#userInfoError").append("Заполните,пожалуйста, все поля!");			
	}		
});

