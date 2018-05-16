<%@ page contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:bundle basename="local" prefix="order-history.">
	<fmt:message key="order.notification" var="orderNotification" />
	<fmt:message key="user.action.login" var="loginAction" />
	<fmt:message key="or" var="orDo" />
	<fmt:message key="user.action.registration" var="registrationAction" />
	<fmt:message key="user.message.registration" var="registrationIfNewUserMessage" />
	
	<fmt:message key="title" var="title" />
	<fmt:message key="thead.productName" var="productName" />
	<fmt:message key="thead.price" var="price" />
	<fmt:message key="thead.quantity" var="quantity" />
	<fmt:message key="thead.orderDate" var="orderDate" />	
</fmt:bundle>


<%@ include file = "../header.jsp" %>

<div class="row main bg-light" >
	
	<div class="col-3">
		<%@ include file = "user-menu.jsp" %>	
	</div>	
	
	<div class="col-8">
 		<c:choose>
			<c:when test="${requestScope.orders == null}">
				<div class="font-weight-bold">
					${orderNotification}
					<a class="btn-sm text-success" data-toggle="modal" data-target="#loginModal">${loginAction} </a>
					${orDo}
					<a class="btn-sm text-success" data-toggle="modal"	data-target="#registrModal">${registrationAction}</a>, ${registrationIfNewUserMessage}			
				</div>
			</c:when>
			<c:otherwise>		
				<div class="bg-success text-white text-center" >
					<h5>${title}</h5>
				</div>		
				<br>	
				<table class="table table-bordered table-hover text-center">
					<thead class="bg-success text-light">
						<tr>
							<th scope="col">${productName}</th>
							<th scope="col">${price}</th>
							<th scope="col">${quantity}</th>
							<th scope="col">${orderDate}</th>
						</tr>
					</thead>
					<tbody class="table-striped">
						<c:forEach var="order" items="${requestScope.orders}">
							<tr>
								<td>${order.orderedProduct.name}</td>
								<td>${order.price}</td>				
								<td>${order.orderedQuantity}</td>				
								<td><fmt:formatDate value="${order.date}" pattern="dd-MM-yyyy HH:mm " /></td>						
							</tr>
						</c:forEach>
					</tbody>
				</table>								
			</c:otherwise> 		
		</c:choose>		
	</div>	
</div>

<%@ include file = "../footer.jsp" %>