<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--@elvariable id="eventCreationFormObject" type="uk.co.vhome.rmj.site.organiser.EventCreationFormObject"--%>
<%--@elvariable id="eventCancellationFormObject" type="uk.co.vhome.rmj.site.organiser.EventCancellationFormObject"--%>

<!DOCTYPE html>
<html lang="en">

<c:set var="pageTitleSuffix" value="Event Management"/>

<head>
	<%@include file="../head-common.jsp" %>

	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

	<script>
      $(function () {
          $("#datepicker").datepicker({dateFormat: "yy-mm-dd"});
      });
	</script>
</head>

<body>

<%@include file="../navigation.jsp" %>

<div class="container">

	<div class="page-header">
		<h1>Event Management</h1>
	</div>

	<div class="row">
		<div class="col-lg-12">

			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">Event Creation</h3>
				</div>
				<div class="panel-body">
					<c:url var="createEventUrl" value="/organiser/create-event" />

					<form:form cssClass="form-inline" modelAttribute="eventCreationFormObject" action="${createEventUrl}">

						<div class="form-group">
							<label for="datepicker">Date</label>
							<form:input cssClass="form-control" path="eventDate" type="text" id="datepicker"/>
						</div>

						<div class="form-group">
							<label for="timePicker">Time</label>
							<form:select cssClass="form-control" path="eventTime" id="timePicker">
								<form:options items="${eventTimes}"/>
							</form:select>
						</div>

						<button class="btn btn-primary" type="submit">Create Event</button>

						<spring:hasBindErrors name="eventCreationFormObject">
							<form:errors cssClass="text-danger"/>
							<form:errors path="eventDate" cssClass="text-danger"/>
						</spring:hasBindErrors>

					</form:form>

				</div>
			</div>
		</div>
	</div>

	<div class="row">
		<div class="col-md-4">
			<div class=" panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">Upcoming Events</h3>
				</div>
				<div class="panel-body">
					<c:url var="cancelEventUrl" value="/organiser/cancel-event" />
					<form:form modelAttribute="eventCancellationFormObject" action="${cancelEventUrl}">
						<table class="table table-condensed">
							<thead>
							<tr>
								<th>Event Date</th>
								<th>Cancel Event</th>
							</tr>
							</thead>
							<tbody>
							<c:forEach var="event" items="${eventCancellationFormObject.events}" varStatus="vs">
								<tr>
									<td>${event.eventDateTimeText}</td>
									<td>
										<form:checkbox path="events[${vs.index}].cancelled"/>
									</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
						<button class="btn btn-primary" type="submit">Cancel Selected Events</button>

					</form:form>
				</div>
			</div>
		</div>
	</div>

</div>


</body>

</html>