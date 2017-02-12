<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%--@elvariable id="userAccountDetails" type="uk.co.vhome.rmj.services.UserAccountDetails"--%>

<!DOCTYPE html>
<html lang="en">

<c:set var="pageTitleSuffix" value="User Management"/>

<%@include file="../head.jsp" %>

<body>

<%@include file="../navigation.jsp" %>

<div class="container">

	<div class="page-header">
		<h1>User Management</h1>
	</div>

	<form:form modelAttribute="userManagementForm">

		<table class="table table-condensed">
			<thead>
			<tr>
				<th>Name</th>
				<th>Email Address</th>
				<th>Group</th>
				<th>Enabled</th>
				<th>Last Login</th>
			</tr>
			</thead>

			<tbody>

			<c:forEach var="userAccountDetails" items="${userManagementForm.userAccountDetails}" varStatus="vs">
				<tr>
					<td>${userAccountDetails.fullName}</td>
					<td><a href="mailto:${userAccountDetails.emailAddress}">${userAccountDetails.emailAddress}</a></td>
					<td>${userAccountDetails.group}</td>
					<td><form:checkbox path="userAccountDetails[${vs.index}].enabled" name="enabled" value="${userAccountDetails.enabled}"/></td>
					<td>${userAccountDetails.lastLogin}</td>
				</tr>
				<form:hidden path="userAccountDetails[${vs.index}].emailAddress" name="emailAddress" value="${userAccountDetails.emailAddress}"/>
			</c:forEach>
			</tbody>
		</table>

		<form:button type="submit" class="btn btn-primary">Update</form:button>
	</form:form>

</div>

</body>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
        crossorigin="anonymous"></script>

</html>
