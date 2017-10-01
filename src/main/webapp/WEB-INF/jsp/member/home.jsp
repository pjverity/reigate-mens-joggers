<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">

<c:set var="pageTitleSuffix" value="Member Home"/>

<head>
	<%@include file="../head-common.jsp" %>
</head>

<body>

<%@include file="../navigation.jsp" %>

<div class="container pt-3">
	<div class="row">
		<div class="col-12">
			<div class="card">
				<h5 class="card-header">Messages</h5>
				<div class="card-block">
					<p>Hi ${userFirstName}!</p>
					<p>
						It's been a while coming, but we have our first significant site update! In this update you can:
					</p>
					<ul>
						<li>Check how many tokens you have from your <a href="<c:url value="/member/account"/> ">Account</a> page</li>
						<li>See when all our upcoming runs are planned to take place</li>
						<li>See how many runs you've completed. (Since we started keeping track that is!)</li>
					</ul>
					<p>
						We're always happy to get your feedback on the site and what you'd like to see, so drop us a message with your ideas!
					</p>
					<p>
						If you spot any errors or run in to any difficulties with the site, then please contact us at <a href="mailto:admin@reigatemensjoggers.co.uk (Reigate Mens Joggers)">admin@reigatemensjoggers.co.uk</a>
						and we will endeavour to fix it.
					</p>
					<p>
						We hope to see you on our the next run!
					</p>
					<p>
						The RMJ Team
					</p>
				</div>
			</div>
		</div>
	</div>
	<div class="row mt-md-2">
		<div class="col-md-6 mt-2 mt-md-0">
			<div class="card card-outline-success">
				<h5 class="card-header bg-success text-white">Completed Runs</h5>
				<table class="table m-0">
					<thead>
					<tr>
						<th>Date</th>
						<th>Distance (km)</th>
					</tr>
					</thead>
					<tbody>
					<c:forEach var="event" items="${completedEvents}">
						<tr>
							<td>${event.eventDateTimeText}</td>
							<td><fmt:formatNumber type="number" maxFractionDigits="2" value="${event.eventDetails.distance}"/></td>
						</tr>
					</c:forEach>
					</tbody>
					<tfoot>
					</tfoot>
				</table>
				<div class="card-footer bg-success text-white">
					Total distance ran: <strong><fmt:formatNumber type="number" maxFractionDigits="2" value="${totalDistance}"/> km</strong>
				</div>
			</div>
		</div>

		<div class="col-md-6 mt-2 mt-md-0">
			<div class="card card-outline-primary">
				<h5 class="card-header bg-primary text-white">Upcoming Runs</h5>
				<ul class="list-group list-group-flush">
					<c:forEach var="event" items="${upcomingEvents}">
						<li class="list-group-item">${event.eventDateTimeFullText}</li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</div>

</div>

</body>

</html>
