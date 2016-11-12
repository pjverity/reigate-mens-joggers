<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">

<c:set var="pageTitleSuffix" value="User Management" />

<%@include file="../head.jsp"%>

<body>

<%@include file="../navigation.jsp" %>

<div class="container">
	<div class="row">
		<div class="col-md-12">
			<h1>User Management</h1>
			<form:form class="well" modelAttribute="userManagementModel">
				<table class="table table-condensed">
					<thead>
					<tr>
						<th>User</th>
						<th>Enabled</th>
					</tr>
					</thead>
					<tbody>
					<c:forEach var="user" items="${userManagementModel.users}" varStatus="vs">
						<tr>
							<td>${user.username}</td>
							<td>
								<form:checkbox path="users[${vs.index}].enabled" name="enabled" value="users[${vs.index}].enabled"/>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
				<form:button type="submit" class="btn btn-primary">Update</form:button>
			</form:form>
		</div>
	</div>
</div>

</body>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
        crossorigin="anonymous"></script>

</html>
