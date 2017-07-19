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

	<div class="page-header">
		<h1>Run Completion</h1>
	</div>

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

	<div class="row" style="margin-top: 1em">
		<div class="col-md-12">

			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">Select Runners</h3>
				</div>

				<table class="table">
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
							<td class="form-control-static">
								<a href="mailto:${row.memberBalance.username}">${row.memberBalance.firstName}&nbsp;${row.memberBalance.lastName}</a>
							</td>
							<td><form:checkbox cssClass="checkbox" path="rows[${vs.index}].present" name="present" value="${row.present}"/></td>
							<td class="form-control-static">${row.memberBalance.balance == null ? 0 : row.memberBalance.balance}</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>

			</div>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">
			<div class="well well-sm">
				<div class="form-inline">


						<%-- Run Date Selection --%>

					<div class="form-group ${empty events ? 'has-error' : ''}">
						<label for="runDateTimeSelect" class="control-label">Completion Date</label>
						<c:choose>
							<c:when test="${empty events}">
								<c:url value='/organiser/event-scheduling' var="url"/>
								<div id="runDateTimeSelect" class="form-control-static">
									<spring:message code="ui.event-completion.NoEvents" arguments="${url}"/>
								</div>
							</c:when>
							<c:otherwise>
								<form:select id="runDateTimeSelect" cssClass="form-control" path="event">
									<form:options items="${events}" itemLabel="eventDateTimeFullText"/>
								</form:select>
							</c:otherwise>
						</c:choose>
					</div>


						<%-- Run Distance Selection --%>

					<div id="distanceGroup" class="form-group">
						<label for="distance" class="control-label">Distance</label>
						<form:input cssClass="form-control" id="distanceInput" path="distance"/>
					</div>


						<%-- Distance Metric --%>

					<div id="metricRadioButtonGroup" class="form-group">
						<label class="radio-inline">
							<form:radiobutton path="metric" value="MILES"/> Miles
						</label>
						<label class="radio-inline">
							<form:radiobutton path="metric" value="KILOMETERS"/> Km
						</label>
					</div>


						<%-- Complete Run Button --%>

					<a id="completeButton" class="btn btn-success" data-target="#confirmModal" data-toggle="modal"><span id="runnerCountBadge" class="badge">0</span> Complete Run</a>


						<%-- Cancel Run Link--%>

					<c:if test="${not empty events}">
						<c:url value="/organiser/event-scheduling" var="cancelEventUrl">
							<c:param name="cancelEventId" value=""/>
						</c:url>
						or <a id="cancelEventAnchor" href="${cancelEventUrl}">Cancel Run</a>
					</c:if>

				</div>
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
		$distanceGroup.toggleClass('has-error', !distanceEntered);
		$metricRadioButtonGroup.toggleClass('has-error', !metricSelected);

		$completeButton.toggleClass('disabled', !runSelected || runnerCount === 0 || !metricSelected || !distanceEntered);
	});

	$('input[type=radio]').change(function () {
		metricSelected = true;
		$metricRadioButtonGroup.removeClass('has-error');

		$completeButton.toggleClass('disabled', !runSelected || runnerCount === 0 || !metricSelected || !distanceEntered);
	});

	$('form input:checkbox').on('click', function () {
		runnerCount += this.checked ? 1 : -1;

		$runnerCountBadge.text(runnerCount);
		$completeButton.toggleClass('disabled', !runSelected || runnerCount === 0 || !metricSelected || !distanceEntered);
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
		  $distanceGroup.toggleClass('has-error', !distanceEntered);

		  $completeButton.toggleClass('disabled', !runSelected || runnerCount === 0 || !metricSelected || !distanceEntered);
	  });

	$('#confirmModal').on('show.bs.modal', function (e) {
		const distance = $distanceInput.val();
		const metric = $('input[type=radio]:checked').val();
		const dateTime = $runDateTimeSelect.find(':selected').text();

		$('#confirmRunDateTime').text(dateTime);
		$('#confirmRunnerCount').html('<strong>' + runnerCount + '</strong> ' + (runnerCount === 1 ? 'person' : 'people'));
		$('#confirmRunDistance').html('<strong>' + distance + ' ' + metric + '</strong> ');
	});

</script>

</html>