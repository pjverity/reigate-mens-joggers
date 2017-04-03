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

	<form:form modelAttribute="eventRegistrationFormObject">

		<div class="form-inline">
			<div class="form-group">
				<label for="run" class=" control-label">Run</label>
				<c:if test="${empty events}">
					<p id="run" class="form-control-static">No Events scheduled. Click <a href="<c:url value="/organiser/event-management"/>">here</a> to create one</p>
				</c:if>
				<c:if test="${not empty events}">
					<form:select id="run" cssClass="form-control" path="event">
						<form:options items="${events}" itemLabel="eventDateTimeText"/>
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

		<form:button type="submit" class="btn btn-primary" disabled="${disableSubmit}">Complete Run</form:button>
	</form:form>

</div>
</body>

</html>