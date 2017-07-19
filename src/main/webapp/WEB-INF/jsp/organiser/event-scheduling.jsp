<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--@elvariable id="eventCreationFormObject" type="uk.co.vhome.rmj.site.organiser.EventCreationFormObject"--%>
<%--@elvariable id="eventCancellationFormObject" type="uk.co.vhome.rmj.site.organiser.EventCancellationFormObject"--%>

<!DOCTYPE html>
<html lang="en">

<c:set var="pageTitleSuffix" value="Run Scheduling"/>

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
		<h1>Run Scheduling</h1>
	</div>

	<div class="row">
		<div class="col-lg-12">

			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">Create New Run</h3>
				</div>
				<div class="panel-body">
					<c:url var="createEventUrl" value="/organiser/create-event"/>

					<form:form cssClass="form-inline" modelAttribute="eventCreationFormObject" action="${createEventUrl}">

						<%-- The hours:mins controls are inline-blocks to prevent them from spanning the entire column on small viewports.
									In doing so, their labels are aligned with the control. The form-inline class will only style controls as
									inline-block's when the viewport >=768px. So this means in some cases the 'Date' label will be above the
									control and looks out of place with the Time label which is always in line. Force  the date control an
									inline-block all the time
						--%>
						<div class="form-group">
							<label for="datepicker">Date</label>
							<form:input cssClass="form-control" path="eventDate" type="text" id="datepicker" cssStyle="width: 9em; display: inline-block"/>
						</div>

						<div class="form-group">
							<label for="timePicker">Time</label>
							<form:select cssClass="form-control" path="eventHour" id="timePicker" cssStyle="width: 4em; display: inline-block">
								<form:option value="6" label="06"/>
								<form:option value="7" label="07"/>
								<form:option value="8" label="08"/>
								<form:option value="9" label="09"/>
								<form:option value="10"/>
								<form:option value="11"/>
								<form:option value="12"/>
								<form:option value="13"/>
								<form:option value="14"/>
								<form:option value="15"/>
								<form:option value="16"/>
								<form:option value="17"/>
								<form:option value="18"/>
								<form:option value="19"/>
								<form:option value="20"/>
							</form:select>
							:
							<form:select cssClass="form-control" path="eventMinutes" cssStyle="width: 4em; display: inline-block">
								<form:option value="0" label="00"/>
								<form:option value="15"/>
								<form:option value="30"/>
								<form:option value="45"/>
							</form:select>
						</div>

						<button class="btn btn-primary" type="submit">Create Run</button>

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
		<div class="col-md-6">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">Upcoming Runs</h3>
				</div>
				<div class="panel-body">
					<c:url var="cancelEventUrl" value="/organiser/cancel-event"/>
					<form:form modelAttribute="eventCancellationFormObject" action="${cancelEventUrl}">


						<%-- Upcoming Runs Table--%>

						<table class="table table-condensed">
							<thead>
							<tr>
								<th>Date</th>
								<th>Cancel</th>
							</tr>
							</thead>
							<tbody>
							<c:forEach var="event" items="${eventCancellationFormObject.events}" varStatus="vs">
								<tr>
									<td>
										<c:choose>
											<c:when test="${event.hasStarted()}">
												<c:url var="eventCompletionUrl" value="/organiser/event-completion">
													<c:param name="eventId" value="${event.id}"/>
												</c:url>
												<a href="${eventCompletionUrl}">${event.eventDateTimeFullText}</a>
											</c:when>
											<c:otherwise>
												${event.eventDateTimeFullText}
											</c:otherwise>
										</c:choose>
									</td>
									<td>
										<form:checkbox path="events[${vs.index}].cancelled"/>
									</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>


						<%-- Cancel Event Button --%>

						<a id="cancelEventButton" class="btn btn-danger disabled" data-target="#confirmModal" data-toggle="modal">Cancel Selected Runs</a>


						<%-- Cancellation Confirmation Dialog --%>

						<div id="confirmModal" class="modal" tabindex="-1" role="dialog" data-backdrop="static">
							<div class="modal-dialog" role="document">
								<div class="modal-content">
									<div class="modal-header">
										<h4 class="modal-title">Confirm Cancellation</h4>
									</div>
									<div class="modal-body">
										<spring:message code="ui.event-scheduling.CreateEvent"/>
									</div>
									<div class="modal-footer">
										<form:button type="button" class="btn btn-default" data-dismiss="modal">Cancel</form:button>
										<form:button type="submit" class="btn btn-primary">Confirm</form:button>
									</div>
								</div>
							</div>
						</div>

					</form:form>
				</div>
			</div>
		</div>

		<div class="col-md-6">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">Completed Runs (10 Most Recent)</h3>
				</div>


				<%-- Completed Runs Table--%>

				<table class="table table-hover">
					<thead>
					<tr>
						<th>Date</th>
						<th>Runners</th>
						<th>Distance (km)</th>
					</tr>
					</thead>
					<tbody>
					<c:forEach var="event" items="${completedEvents}">
						<tr onclick="gotoUrl('organiser/event-scheduling?eventId=${event.id}')" style="cursor: pointer">
							<td>${event.eventDateTimeFullText}</td>
							<td>${fn:length(event.userDetailsEntities)}</td>
							<td><fmt:formatNumber type="number" maxFractionDigits="2" value="${event.eventInfo.distance}"/></td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>


			<%-- Selected Complete Run Details --%>

			<c:if test="${not empty selectedEvent}">
			<div class=" panel panel-success">
				<div class="panel-heading">
					<h3 class="panel-title">${selectedEvent.eventDateTimeFullText}</h3>
				</div>
				<ul class="list-group">
					<c:forEach var="user" items="${selectedEvent.userDetailsEntities}">
						<li class="list-group-item"><a href="mailto:${user.username}">${user.firstName}&nbsp;${user.lastName}</a></li>
					</c:forEach>
				</ul>
				</c:if>
			</div>
		</div>

	</div>
</div>

<script id="event-scheduling-script" type="text/javascript" data-url="<c:url value='/'/>">

	const contextPath = $('#event-scheduling-script').attr('data-url');

	var selectedEventCount = $("form input:checkbox:checked").length;

	$(function () {
		$('#cancelEventButton').toggleClass('disabled', selectedEventCount === 0);
	});

	$("form input:checkbox").on('click', function () {
		selectedEventCount += this.checked ? 1 : -1;
		$('#cancelEventButton').toggleClass('disabled', selectedEventCount === 0);
	});

	$('#confirmModal').on('show.bs.modal', function (e) {
		var count = 0;
		var input = $("form input:checkbox").each(function () {
			if (this.checked) {
				++count;
			}
		});

		$('#cancelledEventCount').html('<strong>' + count + '</strong> run' + (count === 1 ? '' : 's'));
	});

	function gotoUrl(url) {
		window.location.href = contextPath + url;
	}

</script>

</body>

</html>