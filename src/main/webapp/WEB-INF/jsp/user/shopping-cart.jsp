<%@ page contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
	
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:bundle basename="local" prefix="shopping-cart.">
	<fmt:message key="title" var="title" />
	
	<fmt:message key="thead.name" var="name" />
	<fmt:message key="thead.form" var="form" />
	<fmt:message key="thead.prescriptionCheck" var="prescriptionCheck" />
	<fmt:message key="thead.price" var="price" />
	<fmt:message key="thead.quantity" var="quantity" />
	<fmt:message key="thead.totalPrice" var="totalPrice" />
	
	<fmt:message key="prescription.message.needGetting" var="needGetting" />
	<fmt:message key="prescription.action.toGet" var="toGet" />
	<fmt:message key="prescription.message.underСonsideration" var="underСonsideration" />
	<fmt:message key="prescription.message.needExtending" var="needExtending" />
	<fmt:message key="prescription.action.toExtend" var="toExtend" />
	
	<fmt:message key="quantity.message.quantityWarning.beginnig" var="quantityWarningBeginnig" />
	<fmt:message key="quantity.message.quantityWarning.ending" var="quantityWarningBeginnigEnding" />
	
	<fmt:message key="product.action.changeQuantity" var="changeProductQuantity" />
	<fmt:message key="product.action.delete" var="deleteProduct" />

	<fmt:message key="price.total" var="totalPriceSum" />
	<fmt:message key="price.total.format" var="totalPriceFormat" />

	<fmt:message key="prescription.notification.needPrescription" var="needPrescriptionNotification" />
	<fmt:message key="product.action.pay" var="actionPay" />
	
	<fmt:message key="prescription.notification.authenticate" var="authenticateNotification" />
	<fmt:message key="user.action.login" var="login" />
	<fmt:message key="or" var="orDo" />
	<fmt:message key="user.action.registration" var="registrationAction" />
	<fmt:message key="user.message.registration" var="registrationIfNewUserMessage" />
	
	<fmt:message key="basket.message.empty" var="emptyBasketMessage" />
	<fmt:message key="product.all" var="allProducts" />
	
	<fmt:message key="user.fillFullInfo.message" var="fillFullInfoMessage" />
	<fmt:message key="user.fillFullInfo.name" var="userName" />
	<fmt:message key="user.fillFullInfo.middleName" var="userMiddleName" />
	<fmt:message key="user.fillFullInfo.surname" var="userSurname" />
	<fmt:message key="user.fillFullInfo.adress" var="userAdress" />
	<fmt:message key="user.fillFullInfo.passport" var="userPassport" />
	<fmt:message key="user.fillFullInfo.telephone" var="userTelephone" />
	<fmt:message key="user.fillFullInfo.action.sendInfo" var="sendInfoAction" />
</fmt:bundle>	
	
<%@ include file = "../header.jsp" %>

<div class="row main bg-light" >
	<div class="col-3">
		<%@ include  file = "user-menu.jsp" %>	
	</div>
	
	<div class="col-8">		
		<c:choose>
			<c:when test="${not empty requestScope.detailedBasket}">		
				<h5>${title}</h5>
				<br>
				<table class="table table-sm table-bordered table-hover text-center">
					<thead class="bg-success text-light">
						<tr>
							<th class="w-10" scope="col">${name}</th>
							<th class="w-10" scope="col">${form}</th>
							<th class="w-20" scope="col">${prescriptionCheck}</th>
							<th class="w-10" scope="col">${price}</th>
							<th class="w-40" scope="col">${quantity}</th>
							<th class="w-40" scope="col">${totalPrice}</th>
						</tr>
					</thead>
					<tbody class="table-striped">
						<c:forEach var="basketItem" items="${requestScope.detailedBasket}">
							<tr>
								
								<td>${basketItem.product.name}</td>
								
								<td>${basketItem.product.form}</td>								
								
								<td>
									<jsp:useBean id="currentDate" class="java.util.Date" />			
									<c:choose>
										<c:when test="${sessionScope.idUser != null && ! basketItem.product.needPrescription}">
											<img src="<c:url value='/static/images/yes.png'/>" width="15" height="15" />
										</c:when>
										<c:when test="${sessionScope.idUser != null && basketItem.product.needPrescription}">
											<c:choose>
												<c:when test="${basketItem.prescription.gettingStatus == null}">
													<c:set var="prescriptionCheck" value="failed" />
													<div class="alert alert-danger" role="alert">
														${needGetting}
													</div>
													<a class="btn btn-outline-success" href="<c:url value='/get-prescription?idProduct=${basketItem.product.idProduct}'/>" >
														${toGet}
													</a>
												</c:when>
												<c:when test="${basketItem.prescription.gettingStatus == 'WAITING' || basketItem.prescription.extendingStatus == 'WAITING'}">
													<div class="alert alert-info" role="alert">
														<c:set var="prescriptionCheck" value="failed" />
														${underСonsideration}
													</div>
												</c:when>											
												<c:when test="${currentDate > basketItem.prescription.dateEnd}">
													<c:set var="prescriptionCheck" value="failed" />
													<div class="alert alert-warning" role="alert">
														${needExtending}
													</div>
													<a class="btn btn-outline-success" href="<c:url value='/extend-prescription?idProduct=${basketItem.product.idProduct}'/>">
														${toExtend}
													</a>
												</c:when>
												<c:otherwise>
													<img src="<c:url value='/static/images/yes.png'/>" width="15" height="15" />
												</c:otherwise>
											</c:choose>									
										</c:when>
									</c:choose>
								</td>	
									
								<td>${basketItem.product.price}</td>
								
								<td>
									<form method="post">
										<input type="hidden" name="idProduct" value="${basketItem.product.idProduct}">
										<div class="form-group">
												<c:set var="orderedQuantity" value="${basketItem.orderedQuantity}" />
												<c:set var="maxQuantity" value="${basketItem.product.quantity}" />
												<c:if test="${orderedQuantity > maxQuantity}">
													<div class="alert alert-warning" role="alert">
														${quantityWarningBeginnig}
														(<c:out value="${orderedQuantity}" />)
														${quantityWarningBeginnigEnding}
													</div>
													<c:set var="orderedQuantity" value="${maxQuantity}" />
												</c:if>
										</div>										
										<div class="form-group">											
											<input class="form-control" type="number" name="quantity"
											value="${orderedQuantity}" min=1 max="${maxQuantity}">
										</div>		
										<div class="form-group">											
											<input type="submit" class="btn btn-outline-success" formaction="<c:url value="change-quantity" />" value="${changeProductQuantity}">
										</div>	
										<div class="form-group">
											<input type="submit" class="btn btn-outline-success" formaction="<c:url value="delete-basket-item" />" value="${deleteProduct}">																						
										</div>																				
									</form>									
								</td>
																
								<td>
									<c:out value="${basketItem.product.price*orderedQuantity}"/>
								</td>
								
							</tr>
						</c:forEach>
					</tbody>
				</table>
								
				<div>
					${totalPriceSum} <c:out value="${requestScope.totalProductSum}"/> ${totalPriceFormat}	
				</div>
				<br>
						
				<div>
					<c:choose>
						<c:when test="${prescriptionCheck eq 'failed'}">
							<div class="font-weight-bold">
								${needPrescriptionNotification}
							</div>
							<br>
							<button class="btn btn-success my-2 my-sm-0" type="submit" disabled>${actionPay}</button>
						</c:when>
						<c:when test="${not empty sessionScope.idUser}">
							<form class="form-inline" action="<c:url value="order" />" method="post">						
								<button class="btn btn-success my-2 my-sm-0" type="submit">${actionPay}</button>
							</form>
						</c:when>
						<c:otherwise>						
							<div class="font-weight-bold">
								${authenticateNotification}
								<a class="btn-sm text-success" data-toggle="modal" data-target="#loginModal">${login} </a>
								${orDo} 
								<a class="btn-sm text-success" data-toggle="modal"	data-target="#registrModal">${registrationAction}</a>, ${registrationIfNewUserMessage} 
							</div>
							<br>
							<button class="btn btn-success my-2 my-sm-0" type="submit" disabled>${actionPay}</button>
						</c:otherwise>
					</c:choose>					
				</div>
			</c:when>			
			<c:otherwise>
				<h6>
					${emptyBasketMessage} 
					<a class="text-success" href="<c:url value='/products?currentPage=1&itemsPerPage=3'/>">${allProducts} </a>			
				</h6>
			</c:otherwise>
		</c:choose>						
	</div>
</div>



<div class="modal fade" id="userInfoModel">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">${fillFullInfoMessage}</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<div class="modal-body">
				<form class="form-horizontal" action="<c:url value="send-full-info" />" method="post" id="userInfoForm">
					<div class="form-group">					
						<input type="text" class="form-control" placeholder="${userName}"  name="name" id="name">
					</div>
					<div class="form-group">
						<input type="text" class="form-control" placeholder="${userMiddleName}" name="middleName" id="middleName">
					</div>
					<div class="form-group">
						<input type="text" class="form-control" placeholder="${userSurname}" name="surname" id="surname">
					</div>
					<div class="form-group">
						<input type="text" class="form-control" placeholder="${userAdress}" name="address" id="address">
					</div>
					<div class="form-group">
						<input type="text" class="form-control" placeholder="${userPassport}" name="passport"  id="passport">
					</div>
					<div class="form-group">
						<input type="text" class="form-control" placeholder="${userTelephone}" name="telephone"  id="telephone">
					</div>
					<div id="userInfoError" class="error form-group"></div>
					<button type="submit" class="btn btn-primary">${sendInfoAction}</button>
				</form>
			</div>			
		</div>
	</div>
</div>

<%@ include file = "../footer.jsp" %>

<script src="<c:url value='/static/js/validation/filling-full-info.js'/>"></script>
<c:if test="${'ru_BY' eq sessionScope.locale}">
    <script src="<c:url value='/static/js/validation/messages_ru.js'/>"></script>
</c:if>
<script src="<c:url value='/static/js/action-reaction/filling-full-info.js'/>"></script>