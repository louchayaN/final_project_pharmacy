<%@ page contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="paginator" uri="http://corporation.com/custom-tag/paginator"%>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:bundle basename="local" prefix="add-product-form.">
	<fmt:message key="product.name" var="name" />		
	<fmt:message key="product.nonPatentName" var="nonPatentName" />		
	<fmt:message key="product.producer" var="producer" />
	<fmt:message key="product.form" var="form" />
	<fmt:message key="product.load.instruction" var="loadInstruction" />
	<fmt:message key="product.prescription" var="prescription" />	
	<fmt:message key="product.prescription.no" var="no" />	
	<fmt:message key="product.prescription.yes" var="yes" />	
	<fmt:message key="product.quantity" var="quantity" />
	<fmt:message key="product.price" var="price" />
	<fmt:message key="product.price.format" var="priceFormat" />
	<fmt:message key="action.addProduct" var="addProductAction" />
</fmt:bundle>
<fmt:bundle basename="local" prefix="change-product-form.">
	<fmt:message key="title" var="title" />		
</fmt:bundle>


<%@ include file = "../header.jsp" %>

<div class="row main bg-light">
	
	<div class="col-3">
		<%@ include file = "pharm-menu.jsp" %>	
	</div>	
	
	<div class="col-8">	
		<div class="bg-success text-white text-center" >
			<h4>${title}</h4>
		</div>
		<br>
		<form action="<c:url value="/pharmacist/change-product" />" method="post" enctype="multipart/form-data" id = "changeProductForm">		
			<input type="hidden" name="idProduct" value="${requestScope.product.idProduct}">
			<input type="hidden" name="instructionFileName" value="${requestScope.product.instructionFileName}">
			<div class="form-group">
				<label for="name">${name}</label>
				<input type="text" class="form-control" value="${requestScope.product.name}" name="name" id="name">
			</div>
			<div class="form-group">
				<label for="nonPatentName">${nonPatentName}</label>
				<input type="text" class="form-control" value="${requestScope.product.nonPatentName}" name="nonPatentName" id="nonPatentName">
			</div>
			<div class="form-group">
				<label for="producer">${producer}</label>
				<input type="text" class="form-control" value="${requestScope.product.producer}" name="producer" id="producer">
			</div>
			<div class="form-group">
				<label for="form">${form}</label>
				<input type="text" class="form-control" value="${requestScope.product.form}" name="form" id="form">
			</div>
			<div class="form-group">
				${prescription}
				<c:choose>
					<c:when test="${requestScope.product.needPrescription}">
						<div class="form-check form-check-inline">					
							<input class="form-check-input" type="radio" name="prescriptionRadioOption" id="inlineRadio1" value="yes" checked>
							<label class="form-check-label" for="inlineRadio1">${yes}</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="radio" name="prescriptionRadioOption" id="inlineRadio2" value="no">
							<label class="form-check-label" for="inlineRadio2">${no}</label>
						</div>
					</c:when>
					<c:otherwise>
						<div class="form-check form-check-inline">					
							<input class="form-check-input" type="radio" name="prescriptionRadioOption" id="inlineRadio1" value="yes" >
							<label class="form-check-label" for="inlineRadio1">${yes}</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="radio" name="prescriptionRadioOption" id="inlineRadio2" value="no" checked>
							<label class="form-check-label" for="inlineRadio2">${no}</label>
						</div>
					</c:otherwise>
				</c:choose>	
			</div>			
			<div class="form-group">			
				<a href="<c:url value='/show-instruction?file=${requestScope.product.instructionFileName}'/>" data-fancybox data-caption="${instructionComment}">
					<img src="../static/images/pdf_icon.png" width="30" height="30">
				</a>
				
				<div class="form-group">
					<label for="file">${loadInstruction}</label> 
					<input type="file" class="form-control-file" name="file" id="file">																						
				</div>		
			</div>
			<div class="form-group">
				<label for="form">${price}</label>
				<input type="text" class="form-control" value="${requestScope.product.price}" name="price" id="price">
			</div>							
			<div class="form-group">	
				<label for="form">${quantity}</label>
				<input type="number" class="form-control" name="quantity" value="${requestScope.product.quantity}" min=0 >			
			</div>																	
			<div class="form-group">											
				<input type="submit" class="btn btn-outline-success" value="Изменить">
			</div>
		</form>	
	</div>	
</div>

<%@ include file = "../footer.jsp" %>


<script src="<c:url value='/static/js/validation/change-product.js'/>"></script>
<c:if test="${'ru_BY' eq sessionScope.locale}">
    <script src="<c:url value='/static/js/validation/messages_ru.js'/>"></script>
</c:if>
<script src="<c:url value='/static/js/action-reaction/change-product.js'/>"></script>
