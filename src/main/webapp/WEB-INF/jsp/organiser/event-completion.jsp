<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--@elvariable id="row" type="uk.co.vhome.rmj.site.organiser.EventCompletionFormRow"--%>
<%--@elvariable id="eventCompletionFormObject" type="uk.co.vhome.rmj.site.organiser.EventCompletionFormObject"--%>

<!DOCTYPE html>
<html lang="en">

<c:set var="pageTitleSuffix" value="Run Completion"/>

<head>
	<%@ include file='../head-common.jsp' %>
</head>

<body>

<%@include file="../navigation.jsp" %>

<div class="container">

	<form:form id="registrationForm" modelAttribute="eventCompletionFormObject">

	<div id="confirmModal" class="modal" tabindex="-1" role="dialog" data-backdrop="static">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title">Confirm Run Completed</h4>
				</div>
				<div class="modal-body">
					<spring:message code="ui.event-completion.Confirm"/>
				</div>
				<div class="modal-footer">
					<form:button class="btn btn-default" data-dismiss="modal">Cancel</form:button>
					<input type="submit" class="btn btn-primary" value="Confirm"/>
				</div>
			</div>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">

			<div class="card mt-3 mb-3">
				<div class="card-header">Select Runners</div>
				<div class="card-block">

				<table class="table table-sm m-0 p-0">
					<thead>
					<tr>
						<th>Member</th>
						<th>Finished?</th>
						<th>Token Balance</th>
					</tr>
					</thead>

					<tbody>
					<c:forEach var="row" items="${eventCompletionFormObject.rows}" varStatus="vs">
						<tr>
							<td>
								<a href="mailto:${row.memberBalance.username}">${row.memberBalance.firstName}&nbsp;${row.memberBalance.lastName}</a>
							</td>
							<td><form:checkbox cssClass="checkbox" path="rows[${vs.index}].present" name="present" value="${row.present}"/></td>
							<td >${row.memberBalance.balance == null ? 0 : row.memberBalance.balance}</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>

			</div>
			</div>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">
				<div class="form-inline">


						<%-- Run Date Selection --%>

					<div class="form-group ${empty events ? 'has-danger' : ''}">
						<label for="runDateTimeSelect" class="control-label">Completion Date</label>
						<c:choose>
							<c:when test="${empty events}">
								<c:url value='/organiser/event-scheduling' var="url"/>
								<div id="runDateTimeSelect" class="form-control-static">
									<spring:message code="ui.event-completion.NoEvents" arguments="${url}"/>
								</div>
							</c:when>
							<c:otherwise>
								<form:select id="runDateTimeSelect" cssClass="custom-select mx-2" path="event">
									<form:options items="${events}" itemLabel="eventDateTimeFullText"/>
								</form:select>
							</c:otherwise>
						</c:choose>
					</div>


						<%-- Run Distance Selection --%>

					<div id="distanceGroup" class="form-group">
						<label for="distance" class="control-label">Distance</label>
						<form:input cssClass="form-control mx-2" id="distanceInput" path="distance"/>
					</div>


						<%-- Distance Metric --%>

							<div id="metricRadioButtonGroup" class="form-group">
					<div class="form-check form-check-inline">
						<label class="form-check-label">
							<form:radiobutton cssClass="form-check-input" path="metric" value="MILES" /> Miles
						</label>
					</div>

					<div class="form-check form-check-inline">
						<label class="form-check-label">
							<form:radiobutton cssClass="form-check-input" path="metric" value="KILOMETERS"/> Km
						</label>
					</div>
							</div>


						<%-- Complete Run Button --%>

					<button type="button" id="completeButton" class="btn btn-outline-success mx-2" data-target="#confirmModal" data-toggle="modal"><span id="runnerCountBadge" class="badge badge-pill badge-success">0</span> Complete Run</button>


						<%-- Cancel Run Link--%>

					<c:if test="${not empty events}">
						<c:url value="/organiser/event-scheduling" var="cancelEventUrl">
							<c:param name="cancelEventId" value=""/>
						</c:url>
						or <a id="cancelEventAnchor" class="ml-2" href="${cancelEventUrl}">Cancel Run</a>
					</c:if>

				</div>
		</div>
	</div>

	<spring:hasBindErrors name="eventCompletionFormObject">
	<div class="row">
		<div class="col-md-12">

			<div class="alert alert-danger" role="alert">
				<span><strong>Invalid input</strong></span>
				<ul>
					<c:forEach var="error" items="${errors.fieldErrors}">
						<li>${error.field}&nbsp;${error.defaultMessage}</li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</div>
	</spring:hasBindErrors>
	</form:form>

</body>

<script type="text/javascript">

    const $runDateTimeSelect = $('#runDateTimeSelect');
    const $cancelEventAnchor = $('#cancelEventAnchor');
    const $metricRadioButtonGroup = $('#metricRadioButtonGroup');
    const $completeButton = $('#completeButton');
    const $runnerCountBadge = $('#runnerCountBadge');
    const $distanceGroup = $('#distanceGroup');
    const $distanceInput = $('#distanceInput');

    var runnerCount = $('form input:checkbox:checked').length;
    var runSelected = $runDateTimeSelect.find(':selected').length > 0;
    var metricSelected = $('input[type=radio]:checked').length > 0;
    var distanceEntered = $.isNumeric($distanceInput.val());

    $(function () {
        $runnerCountBadge.text(runnerCount);
        $distanceGroup.toggleClass('has-danger', !distanceEntered);
        $metricRadioButtonGroup.toggleClass('has-danger', !metricSelected);

        updateCompleteButtonState();
    });

    $('input[type=radio]').change(function () {
        metricSelected = true;
        $metricRadioButtonGroup.removeClass('has-danger');

        updateCompleteButtonState();
    });

    $('form input:checkbox').on('click', function () {
        runnerCount += this.checked ? 1 : -1;

        $runnerCountBadge.text(runnerCount);
        updateCompleteButtonState();
    });

    $runDateTimeSelect.change(function (e) {

        const eventId = $(e.target).val();

        // When no run schedules are present, there will be no numeric value for eventId
        if (!$.isNumeric(eventId)) {
            return;
        }

        const newHref = $cancelEventAnchor.attr('href').replace(/=.*/, '=' + eventId);
        $cancelEventAnchor.attr('href', newHref);

    }).trigger('change');

    $distanceInput
        .on('keyup', function (e) {
            distanceEntered = $.isNumeric($(this).val());
            $distanceGroup.toggleClass('has-danger', !distanceEntered);

            updateCompleteButtonState();
        })
        .on('keydown', function (e) {
            if (!$.isNumeric(e.key) && e.keyCode !== 8 && e.keyCode !== 37 && e.keyCode !== 39 && e.keyCode !== 190) {
                e.preventDefault();
            }
        });

    $('#confirmModal').on('show.bs.modal', function (e) {
        const distance = $distanceInput.val();
        const metric = $('input[type=radio]:checked').val();
        const dateTime = $runDateTimeSelect.find(':selected').text();

        $('#confirmRunDateTime').text(dateTime);
        $('#confirmRunnerCount').html('<strong>' + runnerCount + '</strong> ' + (runnerCount === 1 ? 'person' : 'people'));
        $('#confirmRunDistance').html('<strong>' + distance + ' ' + metric + '</strong> ');
    });

    function updateCompleteButtonState() {
        const formIncomplete = !runSelected || runnerCount === 0 || !metricSelected || !distanceEntered;
        if (formIncomplete)
        {
            $completeButton.attr('disabled', true);
        }
        else
        {
            $completeButton.removeAttr('disabled');
        }

    }
</script>

</html>