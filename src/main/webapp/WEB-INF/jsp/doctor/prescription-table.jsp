<%@ page contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="paginator" uri="http://corporation.com/custom-tag/paginator"%>
	
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:bundle basename="local" prefix="prescription-table.">
	<fmt:message key="thead.clientInfo" var="clientInfo" />	
	<fmt:message key="thead.productInfo" var="productInfo" />	
	<fmt:message key="thead.requestDate" var="requestDate" />	
	<fmt:message key="thead.controlPanel" var="controlPanel" />	
	<fmt:message key="client.fullName" var="fullName" />	
	<fmt:message key="client.passport" var="passport" />	
	<fmt:message key="client.telephone" var="telephone" />	
	<fmt:message key="product.name" var="productName" />	
	<fmt:message key="product.form" var="productForm" />	
	<fmt:message key="action.give" var="give" />	
	<fmt:message key="requestsPerPage" var="requestsPerPage" />
</fmt:bundle>			

	
<table class="table table-bordered table-hover">
	<thead class="bg-success text-white text-center">
		<tr>
			<th scope="col">${clientInfo}</th>
			<th scope="col">${productInfo}</th>
			<th scope="col">${requestDate}</th>
			<th scope="col">${controlPanel}</th>
		</tr>
	</thead>
	<tbody class="table-striped">
		<c:forEach var="request" items="${requestScope.prescriptionRequestsView.prescriptionRequests}">
			<tr>
				<td>
					<p>${fullName}: ${request.user.fullName}</p>
					<p>${passport}: ${request.user.passport}</p>
					<p>${telephone}: ${request.user.telephone}</p>
				</td>
				<td>
					<p>${productName}: ${request.product.name}</p>
					<p>${productForm}: ${request.product.form}</p>
				</td>
				<td>
					<fmt:formatDate value="${request.prescription.requestDate}" pattern="dd-MM-yyyy hh:mm" />
				</td>						
				<td>	
					<a class="btn btn-outline-success" href="<c:url value='${action}?idProduct=${request.product.idProduct}&idUser=${request.user.idUser}'/>" >
						${give}
					</a>				
				</td>					
			</tr>
		</c:forEach>
	</tbody>
</table>

<div>
	${requestsPerPage}
	<a class="text-success" href="<c:url value='/doctor/getting-requests?currentPage=1&itemsPerPage=3'/>">3</a>
	<a class="text-success" href="<c:url value='/doctor/getting-requests?currentPage=1&itemsPerPage=5'/>">5</a>
	<a class="text-success" href="<c:url value='/doctor/getting-requests?currentPage=1&itemsPerPage=30'/>">30</a>
</div>	
		


<div align="center">
	<paginator:display currentPage="${param.currentPage}" totalPageCount="${requestScope.prescriptionRequestsView.totalPageCount}" viewPageCount="3" urlPattern="getting-requests?" />
</div>	
