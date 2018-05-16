<%@ page contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:bundle basename="local" prefix="footer.">
	<fmt:message key="aboutUs" var="aboutUs" />
	<fmt:message key="aboutUs.contacts" var="contacts" />
	<fmt:message key="aboutUs.team" var="team" />		
	<fmt:message key="aboutUs.vacancies" var="vacancies" />
	<fmt:message key="companyName" var="companyName" />
	<fmt:message key="delivery" var="delivery" />
	<fmt:message key="payment" var="payment" />
	<fmt:message key="services" var="services" />
	<fmt:message key="services.service1" var="service1" />
	<fmt:message key="services.service2" var="service2" />
	<fmt:message key="services.service3" var="service3" />
</fmt:bundle>

<footer class="container py-5">
	<div class="row">
		<div class="col-12 col-md">
			<small class="d-block mb-3 text-muted"></small>${companyName}<small class="d-block mb-3 text-muted">&copy;	2018</small>
		</div>
		<div class="col-6 col-md">
			<h5>${services}</h5>
			<ul class="list-unstyled text-small">
				<li><a class="text-muted" href="#">${service1}</a></li>
				<li><a class="text-muted" href="#">${service2}</a></li>
				<li><a class="text-muted" href="#">${service3}</a></li>
			</ul>
		</div>
		<div class="col-6 col-md">
			<h5>${delivery}</h5>
			<ul class="list-unstyled text-small">
				<li><a class="text-muted" href="#">${delivery}</a></li>
			</ul>
		</div>
		<div class="col-6 col-md">
			<h5>${payment}</h5>
			<ul class="list-unstyled text-small">
				<li><a class="text-muted" href="#">${payment}</a></li>
			</ul>
		</div>
		<div class="col-6 col-md">
			<h5>${aboutUs}</h5>
			<ul class="list-unstyled text-small">
				<li><a class="text-muted" href="#">${team}</a></li>
				<li><a class="text-muted" href="#">${contacts}</a></li>
				<li><a class="text-muted" href="#">${vacancies}</a></li>
			</ul>
		</div>
	</div>
</footer>


<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>

<script src="<c:url value='/static/js/third-party/jquery.validate.js'/>"></script>	
<script src="<c:url value='/static/js/third-party/URI.js'/>"></script>
<script src="<c:url value='/static/js/third-party/jquery.URI.js'/>"></script>
<script src="<c:url value='/static/js/third-party/jquery.fancybox.min.js'/>"></script>

<script src="<c:url value='/static/js/validation/login-registration.js'/>"></script>	
<c:if test="${'ru_BY' eq sessionScope.locale}">
    <script src="<c:url value='/static/js/validation/messages_ru.js'/>"></script>
</c:if>
<script src="<c:url value='/static/js/action-reaction/login-registration.js'/>"></script>	

</body>
</html>