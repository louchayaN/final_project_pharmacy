<%@ page contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>	
	
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:bundle basename="local" prefix="found-products.">
	<fmt:message key="foundProducts" var="foundProducts" />		
	<fmt:message key="notExactMatch" var="notExactMatch" />		
	<fmt:message key="notFound" var="notFound" />
	<fmt:message key="allProducts" var="allProducts" />
</fmt:bundle>	


<%@ include file = "../header.jsp" %>

<div class="row main bg-light" >
	<div class="col-3">
		<%@ include file = "user-menu.jsp" %>	
	</div>
	
	<div class="col-8">
		<c:choose>
			<c:when test="${not empty requestScope.foundProducts}">
				<h5>${foundProducts}</h5>
				<br>	
				<c:set var="products" value="${requestScope.foundProducts}" />	
				<%@ include file = "../product-table.jsp" %>				
			</c:when>
			
			<c:when test="${not empty requestScope.foundResemblingProducts}">
				<h5>${notExactMatch}</h5>
				<br>		
				<c:set var="products" value="${requestScope.foundResemblingProducts}" />	
				<%@ include file = "../product-table.jsp" %>			
			</c:when>
						
			<c:otherwise>
				<h5>
					${notFound}
					<a href="<c:url value='/products?currentPage=1&itemsPerPage=3'/>">${allProducts}</a>
				</h5>
			</c:otherwise>
		</c:choose>	
	</div>	
</div>

<%@ include file = "../footer.jsp" %>