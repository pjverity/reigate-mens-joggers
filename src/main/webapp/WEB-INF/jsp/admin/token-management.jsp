<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--@elvariable id="tokenManagementFormObject" type="uk.co.vhome.clubbed.web.site.admin.TokenManagementFormObject"--%>

<!DOCTYPE html>
<html lang="en">

<c:set var="pageTitleSuffix" value="Token Management"/>

<head>
		<%@ include file="../head-common.jsp" %>
</head>

<body>

<%@include file="../navigation.jsp" %>

<div class="container pt-3">

	<form:form modelAttribute="tokenManagementFormObject">

		<div class="row">
			<div class="col-12">
			<table class="table table-sm table-responsive">
				<thead>
				<tr>
					<th>Member</th>
					<th class="text-center">Current Balance</th>
					<th class="text-center">Credit Amount</th>
				</tr>
				</thead>

				<tbody>
				<c:forEach var="row" items="${tokenManagementFormObject.rows}" varStatus="vs">
					<tr>
						<td nowrap>
							<div class="form-control-static form-control-sm">
							<a href="mailto:${row.userDetailsEntity.userEntity.username}">${row.userDetailsEntity.firstName}&nbsp;${row.userDetailsEntity.lastName}</a>
							</div>
						</td>
						<td nowrap>
							<div class="form-control-static form-control-sm text-center">${row.userDetailsEntity.balance == null ? 0 : row.userDetailsEntity.balance}</div>
						</td>
						<td nowrap>
							<form:input path="rows[${vs.index}].quantity" cssClass="form-control form-control-sm"/>
							<form:errors path="rows[${vs.index}].quantity" cssClass="text-danger"/>
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			</div>
		</div>

		<div class="row">
			<div class="col-12">
				<form:button type="submit" class="btn btn-primary">Credit Accounts</form:button>
			</div>
		</div>

	</form:form>

</div>

</body>

</html>
