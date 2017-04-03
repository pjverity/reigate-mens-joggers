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

	<div class="page-header">
		<h1>Token Management</h1>
	</div>

	<form:form modelAttribute="tokenManagementFormObject">

		<div class="table-responsive">
			<table class="table table-condensed">
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
						<td class="form-control-static">
								${row.memberBalance.firstName}&nbsp;${row.memberBalance.lastName}
						</td>
						<td class="form-control-static">
								${row.memberBalance.balance}
						</td>
						<td>
							<form:select path="rows[${vs.index}].quantity" cssClass="form-control input-sm">
								<form:options items="${creditQuantities}"/>
							</form:select>
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
		<form:button type="submit" class="btn btn-primary">Update</form:button>
	</form:form>

</div>

</body>

</html>
