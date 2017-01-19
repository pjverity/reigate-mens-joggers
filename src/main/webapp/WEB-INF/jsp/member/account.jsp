<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html lang="en">

<c:set var="pageTitleSuffix" value="Account Management"/>

<%@include file="../head.jsp" %>

<body>

<%@include file="../navigation.jsp" %>

<div class="container">
	<div class="row">
		<div class="col-md-12">
			<h1>
				Account Management
				<br/>
				<small>${userDetail.firstName}&nbsp;${userDetail.lastName}</small>
			</h1>
			<hr/>

			<c:url value="/changePassword" var="accountUrl"/>
			<form:form modelAttribute="formObject" action="${accountUrl}" method="post">
				<h5>Change Password <small>${pwChanged == 'true' ? 'Password Changed' : ''}<form:errors/></small></h5>

				<div class="row">
					<spring:bind path="oldPassword">
						<div class="form-group ${status.error ? 'has-error' : ''} col-md-4">
							<form:label cssClass="sr-only" path="oldPassword">Old Password</form:label>
							<form:password cssClass="form-control" path="oldPassword" placeholder="Old Password"/>
							<form:errors path="oldPassword" element="div"/>
						</div>
					</spring:bind>

					<spring:bind path="newPassword">
						<div class="form-group ${status.error ? 'has-error' : ''} col-md-4">
							<form:label cssClass="sr-only" path="newPassword">New Password</form:label>
							<form:password cssClass="form-control" path="newPassword" placeholder="New Password"/>
							<form:errors path="newPassword" element="div"/>
						</div>
					</spring:bind>

					<spring:bind path="confirmedNewPassword">
						<div class="form-group ${status.error ? 'has-error' : ''} col-md-4">
							<form:label cssClass="sr-only" path="confirmedNewPassword">Confirm Password</form:label>
							<form:password cssClass="form-control" path="confirmedNewPassword" placeholder="Confirm Password"/>
							<form:errors path="confirmedNewPassword" element="div"/>
						</div>
					</spring:bind>
				</div>

				<form:button class="btn btn-primary" type="submit">Submit</form:button>

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
