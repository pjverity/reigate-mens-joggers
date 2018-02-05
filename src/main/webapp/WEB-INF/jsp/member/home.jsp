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
				<div class="card-body">
					<div class="card-text">
						<p>Hi ${userFirstName},</p>
						<p>
							We are excited to announce that starting from <strong>Thursday 1st March</strong> we're adding an additional run to our schedule and joining forces with the <a href="http://www.reigateladiesjoggers.co.uk">Reigate Ladies Joggers</a>! Come and join
							us every Thursday (and Tuesday too!) at the usual time and place.
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

<%@include file="../footer-common.jsp" %>

</body>

</html>
