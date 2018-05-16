<%@ page contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:bundle basename="local" prefix="doctor-menu.">
	<fmt:message key="title1" var="title1" />
	<fmt:message key="title1.gettingRequests" var="gettingRequests" />
	<fmt:message key="title1.extendingRequests" var="extendingRequests" />		
</fmt:bundle>


<div class="title1">
	<h6>${goods}</h6>
	<ul class="nav flex-column">
		<li class="nav-item">
			<a class="nav-link" href="<c:url value='/doctor/getting-requests?currentPage=1&itemsPerPage=3' />">${gettingRequests}</a>
		</li>
		<li class="nav-item">
			<a class="nav-link" href="<c:url value='/doctor/extending-requests?currentPage=1&itemsPerPage=3' />">${extendingRequests}</a>
		</li>
	</ul>	
</div>

