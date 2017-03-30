<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--@elvariable id="passwordChangeFormObject" type="uk.co.vhome.rmj.site.member.PasswordChangeFormObject"--%>

<!DOCTYPE html>
<html lang="en">

<c:set var="pageTitleSuffix" value="Account Management"/>

<%@include file="../head.jsp" %>

<body>

<%@include file="../navigation.jsp" %>

<div class="container">
	<div class="page-header">
		<h1>Account Management</h1>
	</div>

	<%-- Token balance --%>
	<div class="row">
		<div class="col-md-12">
			<h5>Token Balance</h5>
			You currently have <strong>${tokenBalance}</strong> token(s) to use
		</div>
	</div>

	<p></p>

	<%-- Password management --%>
	<h5>Change Password</h5>

	<div class="row">
		<div class="col-md-12">

			<form:form cssClass="form-inline" modelAttribute="passwordChangeFormObject" method="post">

		<spring:hasBindErrors name="passwordChangeFormObject">
			<c:set var="formError" value="true"/>
		</spring:hasBindErrors>

		<div class="row">
			<spring:bind path="oldPassword">
				<div class="form-group ${status.error ? 'has-error' : ''} col-xs-12 col-sm-3">
					<form:label cssClass="sr-only" path="oldPassword">Old Password</form:label>
					<form:password cssClass="form-control" cssStyle="width: 100%" path="oldPassword" placeholder="Old Password"/>
				</div>
			</spring:bind>

			<spring:bind path="newPassword">
				<div class="form-group ${status.error ? 'has-error' : ''} col-xs-12 col-sm-3">
					<form:label cssClass="sr-only" path="newPassword">New Password</form:label>
					<form:password cssClass="form-control" cssStyle="width: 100%" path="newPassword" placeholder="New Password"/>
				</div>
			</spring:bind>

			<spring:bind path="confirmedNewPassword">
				<div class="form-group ${status.error ? 'has-error' : ''} col-xs-12 col-sm-3">
					<form:label cssClass="sr-only" path="confirmedNewPassword">Confirm Password</form:label>
					<form:password cssClass="form-control" cssStyle="width: 100%" path="confirmedNewPassword" placeholder="Confirm Password"/>
				</div>
			</spring:bind>

			<div class="col-xs-12 col-sm-3">
				<form:button class="btn btn-primary" style="width:100%" type="submit">Change Password</form:button>
			</div>
		</div>

		<p></p>

		<div class="row">
			<c:if test="${passwordChangeFormObject.passwordChanged}">
				<div class="col-xs-12">
					<div class="alert alert-success alert-dismissible col-xs-12" role="alert">
						<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<strong>Done!</strong> Your password has been changed
					</div>
				</div>
			</c:if>

			<spring:hasBindErrors name="passwordChangeFormObject">
				<div class="col-xs-12">
					<div class="alert alert-danger alert-dismissible" role="alert">
						<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<strong>Ooos!</strong> We couldn't change your password because of the following problems:
						<ul>
							<form:errors path="oldPassword" element="li"/>
							<form:errors path="newPassword" element="li"/>
							<form:errors path="confirmedNewPassword" element="li"/>
						</ul>
					</div>
				</div>
			</spring:hasBindErrors>
		</div>

	</form:form>

</div>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
        crossorigin="anonymous"></script>


</body>
</html>
