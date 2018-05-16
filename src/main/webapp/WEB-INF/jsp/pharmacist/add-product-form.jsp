<%@ page contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="paginator" uri="http://corporation.com/custom-tag/paginator"%>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:bundle basename="local" prefix="add-product-form.">
	<fmt:message key="title" var="title" />
	<fmt:message key="title.ru" var="titleRU" />
	<fmt:message key="title.en" var="titleEN" />
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


<%@ include file = "../header.jsp" %>

<div class="row main bg-light">
	
	<div class="col-3">
		<%@ include file = "pharm-menu.jsp" %>	
	</div>	
	
	<div class="col-8">	
	
		<div class="bg-success text-white text-center" >
			<h4>${title}</h4>
		</div>
		<form action="<c:url value="/pharmacist/add-product" />" method="post" enctype="multipart/form-data" id="addProductForm">		
			<div class="row">					
				<div class="col" style="border-bottom: thin solid black; border-right: thin solid black;">					
					<div class="form-group font-weight-bold">					
						${titleRU}
					</div>
					<div class="form-group">					
						<input type="text" class="form-control" placeholder="${name}" name="name_ru" id="name_ru">
					</div>
					<div class="form-group">
						<input type="text" class="form-control" placeholder="${nonPatentName}" name="nonPatentName_ru" id="nonPatentName_ru">
					</div>
					<div class="form-group">					
						<input type="text" class="form-control" placeholder="${producer}" name="producer_ru" id="producer_ru">
					</div>
					<div class="form-group">
						<input type="text" class="form-control" placeholder="${form}" name="form_ru"  id="form_ru">
					</div>		
					<div class="form-group">
						<label for="file">${loadInstruction}</label> 
						<input type="file" class="form-control-file" name="file_ru" id="file_ru">																						
					</div>				
				</div>
				
				<div class="col" style="border-bottom : thin solid black">					
					<div class="form-group font-weight-bold">					
						${titleEN}
					</div>					
					<div class="form-group">					
						<input type="text" class="form-control" placeholder="${name}" name="name_en" id="name_en">
					</div>
					<div class="form-group">
						<input type="text" class="form-control" placeholder="${nonPatentName}" name="nonPatentName_en" id="nonPatentName_en">
					</div>
					<div class="form-group">					
						<input type="text" class="form-control" placeholder="${producer}" name="producer_en" id="producer_en">
					</div>
					<div class="form-group">
						<input type="text" class="form-control" placeholder="${form}" name="form_en"  id="form_en">
					</div>						
					<div class="form-group">
						<label for="file">${loadInstruction}</label> 
						<input type="file" class="form-control-file" name="file_en" id="file_en">																						
					</div>				
				</div>
			</div>
			<div class="form-group">
				${prescription}
				<div class="form-check form-check-inline">					
					<input class="form-check-input" type="radio" name="prescriptionRadioOption" id="inlineRadio1" value="yes">
					<label class="form-check-label" for="inlineRadio1">${yes}</label>
				</div>
				<div class="form-check form-check-inline">
					<input class="form-check-input" type="radio" name="prescriptionRadioOption" id="inlineRadio2" value="no">
					<label class="form-check-label" for="inlineRadio2">${no}</label>
				</div>
			</div>	
			<div class="row" style="border-bottom : thin solid black">	
				<div class="col">
					<div class="form-group">
						${quantity}
						<input type="number" class="form-control" name="quantity" id="quantity" value="0" min=0 >
					</div>
				</div>	
				<div class="col">	
					<div class="form-group">
						${price}
						<input type="text" class="form-control" placeholder="${priceFormat}" name="price" id="price">
					</div>
				</div>
			</div> 	
					
			<div id="addProductError" class="error form-group"></div>
			<button type="submit" class="btn btn-success">${addProductAction}</button>		
		</form>	
	</div>	
</div>

<%@ include file = "../footer.jsp" %>


<script src="<c:url value='/static/js/validation/add-product.js'/>"></script>
<c:if test="${'ru_BY' eq sessionScope.locale}">
    <script src="<c:url value='/static/js/validation/messages_ru.js'/>"></script>
</c:if>

<script src="<c:url value='/static/js/action-reaction/adding-product.js'/>"></script>
