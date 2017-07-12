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

<div class="container pt-3">

	<form:form modelAttribute="userManagementFormObject">

	<div class="row">
		<div class="col-12">
			<table class="table table-sm table-responsive">
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
						<td nowrap>
							<i class="fa fa-circle" style="color: ${activeSessions[user.username] ? 'lightseagreen' : 'indianred'}; "
							   aria-hidden="true"></i> ${user.firstName}&nbsp;${user.lastName}
						</td>
						<td nowrap><a href="mailto:${user.username}">${user.username}</a></td>
						<td nowrap><form:checkbox cssClass="checkbox" path="userManagementFormRows[${vs.index}].enabled" name="enabled"/></td>
						<td nowrap><fmt:formatDate value="${user.lastLoginAsDate}" type="both"/></td>
					</tr>
					<form:hidden path="userManagementFormRows[${vs.index}].id" name="id"/>
				</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

	<div class="row">
		<div class="col-12">
			<form:button type="submit" class="btn btn-primary">Update</form:button>
		</div>
	</div>

	</form:form>

</body>

</html>
