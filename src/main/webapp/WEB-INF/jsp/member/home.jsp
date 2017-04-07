<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">

<c:set var="pageTitleSuffix" value="Member Home"/>

<head>
	<%@include file="../head-common.jsp" %>
</head>

<body>

<%@include file="../navigation.jsp" %>

<div class="container">
	<div class="page-header">
		<h1>Messages</h1>
	</div>
	<div class="well">
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
		If you spot any errors or run in to any difficulties with the site, then please contact us at <a href="mailto:administrator@reigatemensjoggers.co.uk">administrator@reigatemensjoggers.co.uk</a>
		and we will endeavour to fix it.
	</p>
	<p>
		We hope to see you on our the next run!
	</p>
	<p>
		The RMJ Team
	</p>
	</div>

	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-info">
				<div class="panel-heading">
					<h3 class="panel-title">Upcoming Runs. You have completed <span class="badge ">${completedEvents}</span> runs!</h3>
				</div>
				<div class="panel-body">
					<c:choose>
						<c:when test="${empty upcomingEvents}">
							No runs planned. Stay tuned!
						</c:when>
						<c:otherwise>
							<ul>
								<c:forEach var="event" items="${upcomingEvents}">
									<li>${event.eventDateTimeText}</li>
								</c:forEach>
							</ul>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>

</div>

</body>

</html>
