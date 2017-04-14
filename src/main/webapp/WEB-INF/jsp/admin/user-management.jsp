<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%--@elvariable id="user" type="uk.co.vhome.rmj.entities.UserDetailsEntity"--%>

<!DOCTYPE html>
<html lang="en">

<c:set var="pageTitleSuffix" value="User Management"/>

<head>
	<%@include file="../head-common.jsp" %>
</head>

<body>

<%@include file="../navigation.jsp" %>

<div class="container">

	<div class="page-header">
		<h1>User Management</h1>
	</div>

	<form:form modelAttribute="userManagementFormObject">

	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">RMJ Users</h3>
		</div>

		<table class="table">
			<thead>
			<tr>
				<th>Name</th>
				<th>Email Address</th>
				<th>Enabled</th>
				<th>Last Login</th>
			</tr>
			</thead>

			<tbody>

			<c:forEach var="user" items="${userDetails}" varStatus="vs">
				<tr>
					<td><i class="fa fa-circle" style="color: ${activeSessions[user.username] ? 'lightseagreen' : 'indianred'}" aria-hidden="true"></i> ${user.firstName}&nbsp;${user.lastName}</td>
					<td><a href="mailto:${user.username}">${user.username}</a></td>
					<td><form:checkbox cssClass="checkbox" path="userManagementFormRows[${vs.index}].enabled" name="enabled" /></td>
					<td><fmt:formatDate value="${user.lastLoginAsDate}" type="both"/></td>
				</tr>
				<form:hidden path="userManagementFormRows[${vs.index}].id" name="id" />
			</c:forEach>
			</tbody>
		</table>
	</div>

		<form:button type="submit" class="btn btn-primary">Update</form:button>
	</form:form>
</div>

</body>

</html>
