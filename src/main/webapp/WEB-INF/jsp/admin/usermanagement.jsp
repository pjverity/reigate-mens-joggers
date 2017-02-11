<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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

	<table class="table table-condensed">
		<thead>
		<tr>
			<th>Name</th>
			<th>Email Address</th>
			<th>Group</th>
			<th>Enabled</th>
		</tr>
		</thead>

		<tbody>
		<form:form modelAttribute="userManagementForm">

		<c:forEach var="entry" items="${userManagementForm.userSettings}" varStatus="vs">
			<tr>
				<td>${requestScope[entry.key].firstName}&nbsp;${requestScope[entry.key].lastName}</td>
				<td><a href="mailto:${entry.key}">${entry.key}</a></td>
				<td>${entry.value.group}</td>
				<td><form:checkbox path="userSettings[${entry.key}].enabled" name="enabled" value="userSettings[${entry.key}].enabled"/></td>
			</tr>
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
