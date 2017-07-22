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

<div class="container pt-3">

	<c:url var="createEventUrl" value="/organiser/create-event"/>

	<h3><i class="fa fa-calendar-plus-o"></i> Run Creation</h3>
	<hr/>

	<form:form cssClass="form-inline" modelAttribute="eventCreationFormObject" action="${createEventUrl}">

		<div class="form-group">
			<label class="col-form-label mr-2" for="timePicker">Date & Time</label>
			<div class="row">
				<div class="col">
					<div class="input-group p-0">
						<form:input cssClass="form-control mr-0 pr-0" path="eventDate" type="text" id="datepicker"/>
						<div class="input-group-addon p-0">
							<form:select cssClass=" custom-select border-0" cssStyle="width: 100%; background-color: transparent" path="eventHour" id="timePicker">
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
						</div>
						<div class="input-group-addon p-0">
							<form:select cssClass=" custom-select border-0" path="eventMinutes" cssStyle="width: 100%; background-color: transparent">
								<form:option value="0" label="00"/>
								<form:option value="15"/>
								<form:option value="30"/>
								<form:option value="45"/>
							</form:select>
						</div>
					</div>
				</div>
			</div>

			<button class="btn btn-primary mt-2 mt-sm-0 ml-sm-1">Create Run</button>

		</div>

		<spring:hasBindErrors name="eventCreationFormObject">
			<div class="form-group ml-0 ml-sm-2 mt-sm-2">
				<div class="alert alert-danger">
					<form:errors/>
					<form:errors path="eventDate"/>
				</div>
			</div>
		</spring:hasBindErrors>

	</form:form>

	<div class="row mt-5">
		<div class="col">
			<h3><i class="fa fa-calendar"></i> Runs</h3>
			<hr/>
		</div>
	</div>

	<div class="row mt-2">


		<%-- Upcoming Runs --%>

		<div class="col-12 col-md-6">
			<div class="card">
				<div class="card-header">Upcoming Runs</div>
				<c:url var="cancelEventUrl" value="/organiser/cancel-event"/>
				<form:form modelAttribute="eventCancellationFormObject" action="${cancelEventUrl}">


					<%-- Upcoming Runs Table--%>

					<table class="table table-sm">
						<thead>
						<tr>
							<th>Date</th>
							<th>Cancel</th>
						</tr>
						</thead>
						<tbody>
						<c:forEach var="event" items="${eventCancellationFormObject.events}" varStatus="vs">
							<tr>
								<td nowrap>
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
								<td nowrap>
									<form:checkbox path="events[${vs.index}].cancelled"/>
								</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>


					<%-- Cancel Event Button --%>

					<button type="button" id="cancelEventButton" class="btn btn-danger m-2" disabled data-target="#confirmModal" data-toggle="modal">Cancel Selected Runs</button>


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


		<%-- Completed Runs --%>

		<div class="col-12 col-md-6 mt-2 mt-md-0">
			<div class="card">
				<div class="card-header">Completed Runs (10 Most Recent)</div>

				<table class="table table-hover table-sm mb-0">
					<thead>
					<tr>
						<th>Date</th>
						<th class="text-center">Runners</th>
						<th>Distance (km)</th>
					</tr>
					</thead>
					<tbody>
					<c:forEach var="event" items="${completedEvents}">
						<tr onclick="gotoUrl('organiser/event-scheduling?eventId=${event.id}')" style="cursor: pointer">
							<td nowrap>${event.eventDateTimeFullText}</td>
							<td nowrap class="text-center">${fn:length(event.userDetailsEntities)}</td>
							<td nowrap><fmt:formatNumber type="number" maxFractionDigits="2" value="${event.eventInfo.distance}"/></td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>

			<%-- Selected Complete Run Details --%>

			<c:if test="${not empty selectedEvent}">
			<div class="card card-outline-success mt-2">
				<div class="card-header bg-success text-white">${selectedEvent.eventDateTimeFullText}</div>
				<ul class="list-group list-group-flush">
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
		updateCancelButton();
	});

	$("form input:checkbox").on('click', function () {
		selectedEventCount += this.checked ? 1 : -1;
		updateCancelButton()
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

	function updateCancelButton() {
		if (selectedEventCount === 0) {
			$('#cancelEventButton').attr('disabled', true);
		}
		else {
			$('#cancelEventButton').removeAttr('disabled');
		}

	}
</script>

</body>

</html>