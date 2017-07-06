<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--@elvariable id="tokenManagementFormObject" type="uk.co.vhome.rmj.site.admin.TokenManagementFormObject"--%>

<!DOCTYPE html>
<html lang="en">

<c:set var="pageTitleSuffix" value="Token Management"/>

<head>
	<%@include file="../head-common.jsp" %>
</head>

<body>

<%@include file="../navigation.jsp" %>

<div class="container">

	<form:form modelAttribute="tokenManagementFormObject">

		<div class="row mt-5">

			<table class="table table-sm">
				<thead>
				<tr>
					<th>Member</th>
					<th>Current Balance</th>
					<th>Credit Amount</th>
				</tr>
				</thead>

				<tbody>
				<c:forEach var="row" items="${tokenManagementFormObject.rows}" varStatus="vs">
					<tr>
						<td>
							<a href="mailto:${row.memberBalance.username}">${row.memberBalance.firstName}&nbsp;${row.memberBalance.lastName}</a>
						</td>
						<td>
							<span class="form-control-static">${row.memberBalance.balance == null ? 0 : row.memberBalance.balance}</span>
						</td>
						<td>
							<form:input path="rows[${vs.index}].quantity" cssClass="form-control input-sm"/>
							<form:errors path="rows[${vs.index}].quantity" cssClass="text-danger"/>
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>

			<form:button type="submit" class="btn btn-primary">Credit Accounts</form:button>

		</div>

	</form:form>

</div>

</body>

</html>
