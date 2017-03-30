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

	<div class="row">
		<div class="col-md-12">

			<%-- Password management --%>
			<form:form cssClass="form-inline" modelAttribute="passwordChangeFormObject" method="post">

				<spring:hasBindErrors name="passwordChangeFormObject">
					<c:set var="formError" value="true"/>
				</spring:hasBindErrors>

				<h5>Change Password</h5>

				<spring:bind path="oldPassword">
					<div class="form-group ${status.error ? 'has-error' : ''}">
						<form:label cssClass="sr-only" path="oldPassword">Old Password</form:label>
						<form:password cssClass="form-control" path="oldPassword" placeholder="Old Password"/>
					</div>
				</spring:bind>

				<spring:bind path="newPassword">
					<div class="form-group ${status.error ? 'has-error' : ''}">
						<form:label cssClass="sr-only" path="newPassword">New Password</form:label>
						<form:password cssClass="form-control" path="newPassword" placeholder="New Password"/>
					</div>
				</spring:bind>

				<spring:bind path="confirmedNewPassword">
					<div class="form-group ${status.error ? 'has-error' : ''}">
						<form:label cssClass="sr-only" path="confirmedNewPassword">Confirm Password</form:label>
						<form:password cssClass="form-control" path="confirmedNewPassword" placeholder="Confirm Password"/>
					</div>
				</spring:bind>

				<form:button class="btn btn-primary" type="submit">Change Password</form:button>

				<p></p>

				<c:if test="${passwordChangeFormObject.passwordChanged}">
					<div class="alert alert-success alert-dismissible" role="alert">
						<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<strong>Done!</strong> Your password has been changed
					</div>
				</c:if>

				<spring:hasBindErrors name="passwordChangeFormObject">
					<div class="alert alert-danger alert-dismissible" role="alert">
						<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<strong>Ooos!</strong> We couldn't change your password because of the following problems:
						<ul>
							<form:errors path="oldPassword" element="li"/>
							<form:errors path="newPassword" element="li"/>
							<form:errors path="confirmedNewPassword" element="li"/>
						</ul>
					</div>
				</spring:hasBindErrors>

			</form:form>

		</div>
	</div>

</div>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
        crossorigin="anonymous"></script>


</body>
</html>
