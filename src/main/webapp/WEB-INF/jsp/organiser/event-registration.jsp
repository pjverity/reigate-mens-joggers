<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%--@elvariable id="row" type="uk.co.vhome.rmj.site.organiser.EventRegistrationFormRow"--%>
<%--@elvariable id="eventRegistrationFormObject" type="uk.co.vhome.rmj.site.organiser.EventRegistrationFormObject"--%>

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
		<h1>Event Registration</h1>
	</div>

	<form:form id="registrationForm" modelAttribute="eventRegistrationFormObject">

		<div class="form-inline">
			<div class="form-group">
				<label for="run" class=" control-label">Run</label>
				<c:if test="${empty events}">
					<p id="run" class="form-control-static">No Events scheduled. Click <a href="<c:url value="/organiser/event-management"/>">here</a> to create one</p>
				</c:if>
				<c:if test="${not empty events}">
					<form:select id="run" cssClass="form-control" path="event">
						<form:options items="${events}" itemLabel="eventDateTimeFullText"/>
					</form:select>
				</c:if>
			</div>
		</div>

		<p></p>
		<table class="table table-condensed">
			<thead>
			<tr>
				<th>Member</th>
				<th>Token Balance</th>
				<th>Completed Run</th>
			</tr>
			</thead>

			<tbody>

			<c:forEach var="row" items="${eventRegistrationFormObject.rows}" varStatus="vs">
				<tr>
					<td class="form-control-static">${row.memberBalance.firstName}&nbsp;${row.memberBalance.lastName}</td>
					<td class="form-control-static">${row.memberBalance.balance}</td>
					<td><form:checkbox cssClass="checkbox" path="rows[${vs.index}].present" name="present" value="${row.present}"/></td>
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
						<h4 class="modal-title">Complete Run?</h4>
					</div>
					<div class="modal-body">
						Confirm <strong><span id="partipantCount"></span></strong> people completed the run on <strong><span id="selectedEventDateTime"></span></strong>
					</div>
					<div class="modal-footer">
						<form:button id="signup-cancel" type="button" class="btn btn-default" data-dismiss="modal">Cancel</form:button>
						<form:button type="submit" class="btn btn-primary" disabled="${disableSubmit}">Confirm</form:button>
					</div>
				</div>
			</div>
		</div>

		<a class="btn btn-primary" ${disableSubmit ? '' : 'data-target="#confirmModal"'} ${disableSubmit ? 'disabled' : ''} data-toggle="modal"  style="cursor: pointer">Complete Run</a>

	</form:form>

</div>
</body>

<script type="text/javascript">
    const form = $("#registrationForm");

    $('#confirmModal').on('show.bs.modal', function (e) {
        $('#selectedEventDateTime').text($('#run').find(':selected').text());

        var count = 0;
        var input = $( "form input:checkbox" ).each( function () {
            if ( this.checked ) { ++count; }
        });

        $('#partipantCount').text(count);

    });

</script>

</html>