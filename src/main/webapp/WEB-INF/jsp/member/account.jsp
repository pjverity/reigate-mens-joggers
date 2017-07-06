<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--@elvariable id="passwordChangeFormObject" type="uk.co.vhome.rmj.site.member.PasswordChangeFormObject"--%>

<!DOCTYPE html>
<html lang="en">

<c:set var="pageTitleSuffix" value="Account Management"/>

<head>
	<%@include file="../head-common.jsp" %>
</head>

<body>

<%@include file="../navigation.jsp" %>

<div class="container">

	<%-- Token balance --%>
	<div class="row mt-5">
		<div class="col-12">
			<h6>Token Balance</h6>
			You currently have <strong>${tokenBalance}</strong> token(s) to use
		</div>
	</div>

	<%-- Password management --%>
	<form:form cssClass="form" modelAttribute="passwordChangeFormObject" method="post">

		<div class="form-group row mt-2">
			<div class="col-12">
				<h6>Change Password</h6>
		</div>
				<spring:hasBindErrors name="passwordChangeFormObject">
					<c:set var="formError" value="true"/>
				</spring:hasBindErrors>

				<spring:bind path="oldPassword">
					<div class="col-12 col-sm-3">
					<div class="form-group ${status.error ? 'has-danger' : ''}">
						<form:label cssClass="sr-only" path="oldPassword">Old Password</form:label>
							<form:password cssClass="form-control mr-2" path="oldPassword" placeholder="Old Password"/>
						</div>
					</div>
				</spring:bind>

				<spring:bind path="newPassword">
					<div class="col-12 col-sm-3">
					<div class="form-group ${status.error ? 'has-danger' : ''}">
						<form:label cssClass="sr-only" path="newPassword">New Password</form:label>
							<form:password cssClass="form-control mr-2" path="newPassword" placeholder="New Password"/>
						</div>
					</div>
				</spring:bind>

				<spring:bind path="confirmedNewPassword">
					<div class="col-12 col-sm-3">
					<div class="form-group ${status.error ? 'has-danger' : ''}">
						<form:label cssClass="sr-only" path="confirmedNewPassword">Confirm Password</form:label>

							<form:password cssClass="form-control mr-2" path="confirmedNewPassword" placeholder="Confirm Password"/>
						</div>
					</div>
				</spring:bind>

				<div class="col-12 col-sm-3">
					<form:button class="btn btn-primary" type="submit">Change Password</form:button>
				</div>

			</div>

		<div class=" row">
			<div class="col-12">
				<c:if test="${passwordChangeFormObject.passwordChanged}">
					<div class="alert alert-success alert-dismissible" role="alert">
						<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<strong>Done!</strong> Your password has been changed
					</div>
				</c:if>

				<spring:hasBindErrors name="passwordChangeFormObject">
					<div class="alert alert-danger alert-dismissible" role="alert">
						<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<strong>Oops!</strong> We couldn't change your password because of the following problems:
						<ul>
							<form:errors path="oldPassword" element="li"/>
							<form:errors path="newPassword" element="li"/>
							<form:errors path="confirmedNewPassword" element="li"/>
						</ul>
					</div>
				</spring:hasBindErrors>
			</div>
		</div>

	</form:form>

</div>

</body>
</html>
