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

	<div class="row mt-sm-2">
		<div class="col-lg-12">

			<div class="card">
				<h6 class="card-header">Create Run</h6>
				<div class="card-block">

					<c:url var="createEventUrl" value="/organiser/create-event"/>

					<form:form cssClass="form-inline" modelAttribute="eventCreationFormObject" action="${createEventUrl}">

						<div class="form-group">
							<label class="mr-sm-2" for="datepicker">Date</label>
							<form:input cssClass="form-control mr-sm-2" path="eventDate" type="text" id="datepicker" cssStyle="width: 9em; display: inline-block"/>
						</div>

						<div class="form-group">
							<label class="mr-sm-2" for="timePicker">Time</label>
							<form:select cssClass="custom-select" path="eventHour" id="timePicker" cssStyle="width: 4em; display: inline-block">
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
							<form:select cssClass="custom-select mr-sm-2" path="eventMinutes" cssStyle="width: 4em; display: inline-block">
								<form:option value="0" label="00"/>
								<form:option value="15"/>
								<form:option value="30"/>
								<form:option value="45"/>
							</form:select>
						</div>

						<div class="form-group">
							<button class="btn btn-primary ml-sm-2" type="submit">Create Run</button>
						</div>
						<spring:hasBindErrors name="eventCreationFormObject">
							<form:errors cssClass="text-danger"/>
							<form:errors path="eventDate" cssClass="text-danger"/>
						</spring:hasBindErrors>

					</form:form>
				</div>
			</div>
		</div>
	</div>

	<div class="row mt-sm-2">
		<div class="col-md-6">
			<div class="card">
				<h6 class="card-header">Upcoming Runs</h6>
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
		</div>

		<div class="col-md-6">
			<div class="card">
				<h6 class="card-header">Completed Runs (10 Most Recent)</h6>


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
						<c:url value="/organiser/event-scheduling" var="url">
							<c:param name="eventId" value="${event.id}"/>
						</c:url>
						<tr onclick="window.location.href='${url}'" style="cursor: pointer">
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
			<div class="card mt-sm-2">
				<div class="card-header">${selectedEvent.eventDateTimeFullText}</div>
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

<script type="text/javascript">
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

    function updateCancelButton() {
        if ( selectedEventCount === 0 )
        {
            $('#cancelEventButton').attr('disabled', true);
        }
        else
        {
            $('#cancelEventButton').removeAttr('disabled');
        }

    }
</script>

</body>

</html>