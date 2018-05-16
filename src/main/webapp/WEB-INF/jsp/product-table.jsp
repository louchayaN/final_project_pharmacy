<%@ page contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:bundle basename="local" prefix="product-table.">
	<fmt:message key="thead.name" var="name" />
	<fmt:message key="thead.nonPatentName" var="nonPatentName" />		
	<fmt:message key="thead.producer" var="producer" />
	<fmt:message key="thead.form" var="form" />
	<fmt:message key="thead.prescription" var="prescription" />
	<fmt:message key="thead.instruction" var="instruction" />
	<fmt:message key="thead.price" var="price" />
	<fmt:message key="thead.quantity" var="quantity" />
	<fmt:message key="instruction.comment" var="instructionComment" />
	<fmt:message key="action.change" var="changeAction" />
</fmt:bundle>

<table class="table table-bordered table-hover text-center">
	<thead class="bg-success text-light">
		<tr>
			<th scope="col">${name}</th>
			<th scope="col">${nonPatentName}</th>
			<th scope="col">${producer}</th>
			<th scope="col">${form}</th>
			<th scope="col">${prescription}</th>
			<th scope="col">${instruction}</th>
			<th scope="col">${price}</th>
			<th scope="col">${quantity}</th>
		</tr>
	</thead>
	<tbody class="table-striped">
		<c:forEach var="product" items="${products}">
			<tr>
				<td>${product.name}</td>
				<td>${product.nonPatentName}</td>
				<td>${product.producer}</td>
				<td>${product.form}</td>
				<td>
					<c:choose>
						<c:when test="${product.needPrescription}">
							<img src="static/images/yes.png" width="15" height="15" />
						</c:when>
						<c:otherwise>
							<img src="static/images/no.png" width="15" height="15" />
						</c:otherwise>
					</c:choose>
				</td>
				<td>
					<a href="<c:url value='/show-instruction?file=${product.instructionFileName}'/>" data-fancybox data-caption="${instructionComment}">
						<img src="static/images/pdf_icon.png" width="30" height="30">
					</a>
				</td>
				<td>${product.price}</td>				
				
				<td>					
					<c:choose>						
						<c:when test="${'PHARMACIST' == sessionScope.role}">
							<div class="form-group">
								<input type="number" class="form-control" name="quantity" value="${product.quantity}" min=0>
							</div>		
							<form action="<c:url value="/pharmacist/show-product" />" method="get">
								<input type="hidden" name="idProduct" value="${product.idProduct}">	
								<div class="form-group">	
									<input type="submit" class="btn btn-outline-success" value="${changeAction}">
								</div>	
							</form>					
						</c:when>
						
						<c:otherwise>
							<form action="<c:url value="add-to-basket" />" method="post">
								<input type="hidden" name="idProduct" value="${product.idProduct}">	
							    <input type="number" class="form-control" name="quantity" value="1" min=1 max="${product.quantity}">			   
						    	<button type="submit" class="btn btn-outline-success form-control">
									<img src="static/images/fill_basket.png" width="15" height="15"/>
								</button>			    
							</form>							
						</c:otherwise>				
					</c:choose>	
				</td>				
			</tr>
		</c:forEach>
	</tbody>
</table>