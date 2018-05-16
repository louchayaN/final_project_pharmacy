<%@ page contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>	
	
<fmt:setLocale value="${sessionScope.locale}"/>	
<fmt:bundle basename="local" prefix = "home-page.">
	<fmt:message key="title" var="title"/>	
	<fmt:message key="title.description" var="description"/>
	<fmt:message key="title1" var="title1"/>
	<fmt:message key="title1.description1" var="description1"/>
	<fmt:message key="title2" var="title2"/>
	<fmt:message key="title2.description2" var="description2"/>
	<fmt:message key="title3" var="title3"/>
	<fmt:message key="title3.description3" var="description3"/>
	<fmt:message key="title4" var="title4"/>
	<fmt:message key="title4.description4" var="description4"/>
</fmt:bundle>	
	
	
<%@ include file = "header.jsp" %>

<section class="jumbotron text-center">
	<div class="container">
		<img src="static/images/icon.jpg" />
		<h1 class="jumbotron-heading">${title}</h1>
		<p class="lead text-muted">${description}</p>
	</div>
</section>

<div class="d-md-flex flex-md-equal w-100 my-md-3 pl-md-3">
	<div class="bg-dark mr-md-3 pt-3 px-3 pt-md-5 px-md-5 text-center text-white overflow-hidden">
		<div class="my-3 py-3">
			<h2 class="display-5"><a class="text-success" href="<c:url value='/products?currentPage=1&itemsPerPage=3'/>">${title1}</a></h2>
			<p class="lead">${description1}</p>			
		</div>
		<img src="static/images/drugs.jpg" style="width: 80%; height: 300px; border-radius: 21px 21px 0 0;" />
	</div>
	<div class="bg-dark mr-md-3 pt-3 px-3 pt-md-5 px-md-5 text-center text-white overflow-hidden">
		<div class="my-3 py-3">
			<h2 class="display-5"><a class="text-success" href="<c:url value='/products?currentPage=1&itemsPerPage=3'/>">${title2}</a></h2>
			<p class="lead">${description2}</p>
		</div>
		<img src="static/images/prescription_check.jpg" style="width: 80%; height: 300px; border-radius: 21px 21px 0 0;" />
	</div>
</div>

<div class="d-md-flex flex-md-equal w-100 my-md-3 pl-md-3">
	<div
		class="bg-dark mr-md-3 pt-3 px-3 pt-md-5 px-md-5 text-center text-white overflow-hidden">
		<div class="my-3 py-3">
			<h2 class="display-5"><a class="text-success" href="<c:url value='/basket'/>">${title3}</a></h2>
			<p class="lead">${description3}</p>
		</div>
		<img src="static/images/pharmacist.png" style="width: 80%; height: 300px; border-radius: 21px 21px 0 0;" />
	</div>
	<div
		class="bg-dark mr-md-3 pt-3 px-3 pt-md-5 px-md-5 text-center text-white overflow-hidden">
		<div class="my-3 py-3">
			<h2 class="display-5"><a class="text-success" href="<c:url value='/basket'/>">${title4}</a></h2>
			<p class="lead">${description4}</p>
		</div>
		<img src="static/images/prescription.jpg" style="width: 80%; height: 300px; border-radius: 21px 21px 0 0;" />
	</div>
</div>

<%@ include file = "footer.jsp" %>
