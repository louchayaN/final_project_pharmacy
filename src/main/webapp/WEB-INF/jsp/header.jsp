<%@ page contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">	
	<link rel="stylesheet"	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">	
	<link rel="stylesheet" href="<c:url value='/static/css/jquery.fancybox.min.css'/>">
	<link rel="stylesheet" href="<c:url value='/static/css/error-validation.css'/>">
	<link rel="stylesheet" href="<c:url value='/static/css/home-page.css'/>">
	<link rel="stylesheet" href="<c:url value='/static/css/main.css'/>">

	<title>Online-pharmacy</title>

	<fmt:setLocale value="${sessionScope.locale}" />
	<fmt:bundle basename="local" prefix="header.">
		<fmt:message key="home" var="home" />
		<fmt:message key="aboutUs" var="aboutUs" />
		<fmt:message key="products" var="products" />		
		<fmt:message key="contacts" var="contacts" />
		<fmt:message key="delivery" var="delivery" />
		<fmt:message key="pharmacist.page" var="pharmacistPage" />
		<fmt:message key="doctor.page" var="doctorPage" />
		<fmt:message key="basket" var="basket" />
		<fmt:message key="loginRegistration" var="loginRegistration" />
		<fmt:message key="logout" var="logout" />
		
		<fmt:message key="page.signin.title" var="signinTitle" />
		<fmt:message key="page.signin.emailOrlogin" var="emailOrlogin" />
		<fmt:message key="page.signin.password" var="password" />
		<fmt:message key="page.signin.action" var="siqninAction" />
		<fmt:message key="page.registration.message" var="registrationMessage" />
		<fmt:message key="page.registration.action" var="registration" />
		
		<fmt:message key="page.signup.title" var="signupTitle" />
		<fmt:message key="page.signup.email" var="signupEmail" />
		<fmt:message key="page.signup.login" var="signupLogin" />
		<fmt:message key="page.signup.password" var="signupPassword" />
		<fmt:message key="page.signup.repeatePassword" var="repeatePassword" />
		<fmt:message key="page.signup.action" var="siqnupAction" />	
	</fmt:bundle>
</head>

<body>
<nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
	<button class="navbar-toggler" type="button" data-toggle="collapse"	data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>
	<div class="collapse navbar-collapse" id="navbarCollapse">
		<ul class="navbar-nav mr-auto">
			<c:if test="${'PHARMACIST' != sessionScope.role && 'DOCTOR' != sessionScope.role}">
				<li class="nav-item active">
					<a class="nav-link" href="<c:url value='/home-page'/>">${home}</a>			
				</li>
				<li class="nav-item">
					<a class="nav-link" href="#">${aboutUs}</a>
				</li>
				<li class="nav-item">
					<a class="nav-link active" href="<c:url value='/products?currentPage=1&itemsPerPage=3'/>">${products}</a>	
				</li>
				<li class="nav-item">
					<a class="nav-link" href="#">${delivery}</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="#">${contacts}</a>
				</li>
			</c:if>
			<c:if test="${'PHARMACIST' == sessionScope.role}">
				<li class="nav-item active">
					<a class="nav-link" href="<c:url value='/pharmacist/add-product-form'/>">${pharmacistPage}</a>
				</li>			
			</c:if>
			<c:if test="${'DOCTOR' == sessionScope.role}">
				<li class="nav-item active">
					<a class="nav-link" href="<c:url value='/doctor/getting-requests?currentPage=1&itemsPerPage=3'/>">${doctorPage}</a>
				</li>			
			</c:if>
		</ul>

		<c:if test="${'DOCTOR' != sessionScope.role && 'PHARMACIST' != sessionScope.role}">
			<img src="<c:url value="/static/images/basket.png"/>" width="40" height="40" /> 
			<a class="nav-link btn-sm text-success" href="<c:url value='/basket'/>">${basket} </a> 
		</c:if>	
		
		<img src="<c:url value="/static/images/login.png"/>" width="20" height="20" /> 
		<c:choose>
			<c:when test="${not empty sessionScope.login}" >
     			<div class="nav-link btn-sm text-white">
     				<c:out value="${sessionScope.login}" />
     			</div>    			
     			<a class="nav-link btn-sm text-success" href="<c:url value='/logout'/>">${logout} </a>
  			</c:when>
			<c:otherwise>
      			<a class="nav-link btn-sm text-success" data-toggle="modal"	data-target="#loginModal">${loginRegistration} </a>
  			</c:otherwise>
		</c:choose>
		<a class="btn-sm btn-success" href="<c:url value='/change-locale?locale=ru_BY'/>">Рус</a>
		<a class="btn-sm btn-success" href="<c:url value='/change-locale?locale=en_US'/>">En</a>			
	</div>
</nav>

<div class="modal fade" id="loginModal">
	<div class="modal-dialog">
		<div class="modal-content">
		
			<div class="modal-header">
				<h4 class="modal-title">${signinTitle}</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			
			<div class="modal-body">
				<form id="signInForm" action="<c:url value="login" />" method="post">
					<div class="form-group">
						<input type="text" class="form-control"	placeholder="${emailOrlogin}" name="loginOrEmail"  id="loginOrEmail">
					</div>
					<div class="form-group">
						<input type="password" class="form-control" placeholder="${password}" name="password"  id="password">
					</div>
					<div id="signInError" class="error form-group"></div>
					<div class="row">
						<div class="col-md-6 mb-3">
							<div>
								<button type="submit" class="btn btn-primary">${siqninAction}</button>
							</div>
						</div>
						<div class="col-md-6 mb-3">							
						</div>
					</div>
				</form>
			</div>
			
			<div class="modal-footer">
				<p class="font-italic">${registrationMessage}</p>
				<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#registrModal">${registration}</button>
			</div>
			
		</div>
	</div>
</div>

<div class="modal fade" id="registrModal">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<h4 class="modal-title">${signupTitle}</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>

			<div class="modal-body">
				<form id="registrationForm" class="form-horizontal" action="<c:url value="registration" />" method="post">
					<div class="form-group">					
						<input type="text" name="reg_email" class="form-control" placeholder="${signupEmail}" id="reg_email">
					</div>
					<div class="form-group">
						<input type="text" name="reg_login" class="form-control" placeholder="${signupLogin}" id="reg_login">
					</div>
					<div class="form-group">
						<input type="password" name="reg_password" class="form-control"	placeholder="${signupPassword}" id="reg_password">
					</div>
					<div class="form-group">
						<input type="password" name="reg_confirm_password" class="form-control" placeholder="${repeatePassword}" id="reg_confirm_password">
					</div>
					<div id="registrError" class="error form-group"></div>
					<button type="submit" class="btn btn-primary" name="signup" value="Sign up">${siqnupAction}</button>
				</form>
			</div>			
		</div>
	</div>
</div>