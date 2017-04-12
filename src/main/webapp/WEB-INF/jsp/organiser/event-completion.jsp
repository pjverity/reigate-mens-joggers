<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--@elvariable id="row" type="uk.co.vhome.rmj.site.organiser.EventCompletionFormRow"--%>
<%--@elvariable id="eventCompletionFormObject" type="uk.co.vhome.rmj.site.organiser.EventCompletionFormObject"--%>

<!DOCTYPE html>
<html lang="en">

<c:set var="pageTitleSuffix" value="Event Registration"/>

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

					<c:if test="${empty events}">
						<c:set var="disableSubmit" value="true"/>
					</c:if>

					<div id="confirmModal" class="modal" tabindex="-1" role="dialog" data-backdrop="static">
						<div class="modal-dialog" role="document">
							<div class="modal-content">
								<div class="modal-header">
									<h4 class="modal-title">Confirm Completion</h4>
								</div>
								<div class="modal-body">
									<spring:message code="ui.event-registration.Confirm"/>
								</div>
								<div class="modal-footer">
									<form:button type="button" class="btn btn-default" data-dismiss="modal">Cancel</form:button>
									<form:button type="submit" class="btn btn-primary" disabled="${disableSubmit}">Confirm</form:button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

		</div>

		<div class="row">
			<div class="col-md-12">
				<div class="well well-sm">
					<div class="form-inline">

						<div class="form-group">
							<label for="run" class="control-label">Run Date</label>
							<c:if test="${empty events}">
								<c:url value='/organiser/event-scheduling' var="url"/>
								<p id="run" class="form-control-static"><spring:message code="ui.event-registration.NoEvents" arguments="${url}"/></p>
							</c:if>
							<c:if test="${not empty events}">
								<form:select id="run" cssClass="form-control" path="event">
									<form:options items="${events}" itemLabel="eventDateTimeFullText"/>
								</form:select>
							</c:if>
						</div>

						<div class="form-group">
							<label for="distance" class="control-label">Distance</label>
							<form:select id="distance" cssClass="form-control" path="distance">
								<form:options items="${distances}"/>
							</form:select>
						</div>

						<spring:bind path="metric">
							<c:if test="${status.error}">
								<c:set var="metricError" value="has-error"/>
							</c:if>
						</spring:bind>

						<div class="form-group ${metricError}">
							<label class="radio-inline">
								<form:radiobutton path="metric" value="MILES"/> Miles
							</label>
							<label class="radio-inline">
								<form:radiobutton path="metric" value="KILOMETERS"/> Km
							</label>
						</div>

						<a class="btn btn-success ${disableSubmit ? 'disabled' : ''}" data-target="#confirmModal" data-toggle="modal">Complete Run</a>

						<spring:hasBindErrors name="eventCompletionFormObject">
							<div class="alert alert-danger" style="margin-top: 0.5em">
								<form:errors path="metric"/>
							</div>
						</spring:hasBindErrors>

					</div>
				</div>
			</div>
		</div>

	</form:form>

</div>
</body>

<script type="text/javascript">
    $('#confirmModal').on('show.bs.modal', function (e) {
        $('#selectedEventDateTime').text($('#run').find(':selected').text());

        var count = 0;
        var input = $("form input:checkbox").each(function () {
            if (this.checked) {
                ++count;
            }
        });

        if (count === 0) {
            $('#participantCount').html("<strong>no one</strong>");
        }
        else {
            $('#participantCount').html("<strong>" + count + "</strong> " + (count === 1 ? 'person' : 'people'));
        }
    });
</script>

</html>