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
						<th>Completed</th>
						<th>Token Balance</th>
					</tr>
					</thead>

					<tbody>
					<c:forEach var="row" items="${eventCompletionFormObject.rows}" varStatus="vs">
						<tr>
							<td class="form-control-static">${row.memberBalance.firstName}&nbsp;${row.memberBalance.lastName}</td>
							<td><form:checkbox cssClass="checkbox" path="rows[${vs.index}].present" name="present" value="${row.present}"/></td>
							<td class="form-control-static">${row.memberBalance.balance}</td>
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
						<label for="runDateTimeSelect" class="control-label">Run Date</label>
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

					<div class="form-group">
						<label for="distance" class="control-label">Distance</label>
						<form:select id="distanceSelect" cssClass="form-control" path="distance">
							<form:options items="${distances}"/>
						</form:select>
					</div>


						<%-- Distance Metric --%>

					<div id="metricRadioButtonGroup" class="form-group has-error">
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

	</form:form>

</body>

<script type="text/javascript">

    var runnerCount = 0;
    var runSelected = false;

    const $runDateTimeSelect = $('#runDateTimeSelect');
    const $cancelEventAnchor = $('#cancelEventAnchor');
    const $metricRadioButtonGroup = $('#metricRadioButtonGroup');
    const $completeButton = $('#completeButton');
    const $runnerCountBadge = $('#runnerCountBadge');

    $(function () {
        $completeButton.toggleClass('disabled', true);

        $('input[type=radio]').change(function () {
            runSelected = $runDateTimeSelect.find(':selected').length !== 0;

            $metricRadioButtonGroup.removeClass('has-error');
            $completeButton.toggleClass('disabled', !runSelected || runnerCount === 0);
        });

        $('form input:checkbox').on('click', function () {
            runnerCount += (this.checked ? 1 : -1);

            $completeButton.toggleClass('disabled', !runSelected || runnerCount === 0);
            $runnerCountBadge.text(runnerCount);
        });

        $runDateTimeSelect.change(function (e) {

            const eventId = $(e.target).val();

            // When no run schedules are present, there will be no numeric value for eventId
            if (!$.isNumeric(eventId))
            {
                return;
            }

            const newHref = $cancelEventAnchor.attr('href').replace(/=.*/, '=' + eventId);
            $cancelEventAnchor.attr('href', newHref);

        }).trigger('change');

    });

    $('#confirmModal').on('show.bs.modal', function (e) {
        const distance = $('#distanceSelect').find(':selected').text();
        const metric = $('input[type=radio]:checked').val();
				const dateTime = $runDateTimeSelect.find(':selected').text();

        $('#confirmRunDateTime').text(dateTime);
        $('#confirmRunnerCount').html('<strong>' + runnerCount + '</strong> ' + (runnerCount === 1 ? 'person' : 'people'));
        $('#confirmRunDistance').html('<strong>' + distance + ' ' + metric + '</strong> ');
    });
</script>

</html>