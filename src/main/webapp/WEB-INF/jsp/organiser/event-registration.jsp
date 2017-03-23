<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%--@elvariable id="row" type="uk.co.vhome.rmj.site.organiser.EventRegistrationFormRow"--%>
<%--@elvariable id="eventRegistrationFormObject" type="uk.co.vhome.rmj.site.organiser.EventRegistrationFormObject"--%>

<!DOCTYPE html>
<html lang="en">

<c:set var="pageTitleSuffix" value="Member Registration"/>

<%@include file="../head.jsp" %>

<body>

<%@include file="../navigation.jsp" %>

<div class="container">

	<div class="page-header">
		<h1>Member Registration</h1>
	</div>

	<form:form modelAttribute="eventRegistrationFormObject" >

		<table class="table table-condensed">
			<thead>
			<tr>
				<th>Name</th>
				<th>Tokens</th>
				<th>Present</th>
			</tr>
			</thead>

			<tbody>

			<c:forEach var="row" items="${eventRegistrationFormObject.rows}" varStatus="vs">
				<tr>
					<td>${row.memberBalance.firstName}&nbsp;${row.memberBalance.lastName}</td>
					<td>${row.memberBalance.quantity}</td>
					<td><form:checkbox cssClass="checkbox" path="rows[${vs.index}].present" name="present" value="${row.present}"/></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>

		<form:button type="submit" class="btn btn-primary">Update</form:button>
	</form:form>

</div>
</body>

</html>