<%@ page contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:bundle basename="local" prefix="pharm-menu.">
	<fmt:message key="goods" var="goods" />
	<fmt:message key="goods.allProducts" var="allProducts" />
	<fmt:message key="goods.addProduct" var="addProduct" />		
</fmt:bundle>

<div class="container">
	<h6>${goods}</h6>
	<ul class="nav flex-column">
		<li class="nav-item">
			<a class="nav-link text-success" href="<c:url value='/products?currentPage=1&itemsPerPage=3'/>"><strong>${allProducts}</strong></a>
		</li>
		<li class="nav-item">
			<a class="nav-link text-success" href="<c:url value='/pharmacist/add-product-form' />"><strong>${addProduct}</strong></a>
		</li>
	</ul>	
</div>

