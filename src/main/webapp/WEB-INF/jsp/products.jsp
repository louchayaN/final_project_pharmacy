<%@ page contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="paginator" uri="http://corporation.com/custom-tag/paginator"%>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:bundle basename="local" prefix="products.">
	<fmt:message key="allProducts" var="allProducts" />
	<fmt:message key="pages" var="pages" />
</fmt:bundle>


<%@ include file = "header.jsp" %>

<div class="row main bg-light" >	
	<div class="col-3">
		<c:choose>						
			<c:when test="${'PHARMACIST' == sessionScope.role}">
				<%@ include file = "pharmacist/pharm-menu.jsp" %>				
			</c:when>			
			<c:otherwise>
				<%@ include file = "user/user-menu.jsp" %>							
			</c:otherwise>				
		</c:choose>							
	</div>	
	
	<div class="col-8">
 		<h5>${allProducts}</h5>
		<br>	
		<c:set var="products" value="${requestScope.productsView.products}" />	
		<%@ include file = "product-table.jsp" %>
		<div class="row">
			<div class="col">
				${pages} 
				<a class="text-success" href="<c:url value='/products?currentPage=1&itemsPerPage=3'/>">3</a>
				<a class="text-success" href="<c:url value='/products?currentPage=1&itemsPerPage=5'/>">5</a>
				<a class="text-success" href="<c:url value='/products?currentPage=1&itemsPerPage=30'/>">30</a>
			</div>		
			<div class="col">
				<paginator:display currentPage="${param.currentPage}" 
					totalPageCount="${requestScope.productsView.totalPageCount}" viewPageCount="3" 
					urlPattern="products?itemsPerPage=${param.itemsPerPage}&" />
			</div>	
		</div>
	</div>	
</div>

<%@ include file = "footer.jsp" %>