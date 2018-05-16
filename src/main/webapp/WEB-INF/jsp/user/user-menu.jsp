<%@ page contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:bundle basename="local" prefix="user-menu.">
	<fmt:message key="goods" var="goods" />
	<fmt:message key="goods.allProducts" var="allProducts" />
	<fmt:message key="goods.search" var="search" />		
	<fmt:message key="goods.analogues" var="analogues" />
	<fmt:message key="orders" var="orders" />
	<fmt:message key="orders.basket" var="basket" />
	<fmt:message key="orders.history" var="history" />
</fmt:bundle>

<div class="container">
	<h6>${goods}</h6>
	<ul class="nav flex-column">
		<li class="nav-item">
			<a class="nav-link text-success" href="<c:url value='/products?currentPage=1&itemsPerPage=3'/>"><strong>${allProducts}</strong></a>		
		</li>
		<li class="nav-item">
			<form class="form-inline mt-2 mt-md-0" action="<c:url value='find-products' />" method="get">
				<input	class="form-control mr-sm-2" type="text" placeholder="${search}" aria-label="Search" name="productName">
				<button class="btn btn-outline-success my-2 my-sm-0" type="submit">
					<img src="<c:url value='/static/images/search.png'/>" width="15" height="15" />
				</button>
				<div class="form-check font-italic">
					<input class="form-check-input" type="checkbox" name="includeAnalogs" checked> 
					<small>${analogues}</small>
				</div>
			</form>
		</li>
	</ul>
	<h6>${orders}</h6>
	<ul class="nav flex-column">
		<li class="nav-item">
			<a class="nav-link text-success" href="<c:url value='/basket'/>"><strong>${basket}</strong></a>			
		</li>
		<li class="nav-item">
			<a class="nav-link text-success" href="<c:url value='/order-history'/>"><strong>${history}</strong></a>
		</li>
	</ul>
</div>