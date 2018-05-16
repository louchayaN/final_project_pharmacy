<%@ page contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:bundle basename="local" prefix="getting-request.">
	<fmt:message key="title" var="title" />	
</fmt:bundle>		
	
	
<%@ include file = "../header.jsp" %>

<div class="row main bg-light">
	
	<div class="col-3">
		<%@ include file = "doctor-menu.jsp" %>	
	</div>	
	
	<div class="col-8">		
		<div class="bg-success text-white text-center" >
			<h4>${title}</h4>
		</div>		

		<c:set var="action" value="/doctor/give-prescription"></c:set>		
		<%@ include file = "prescription-table.jsp" %>						
	</div>	
</div>

<%@ include file = "../footer.jsp" %>