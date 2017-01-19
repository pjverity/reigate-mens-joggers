<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html lang="en">

<c:set var="pageTitleSuffix" value="Reigate Mens Joggers"/>

<%@include file="../head.jsp" %>

<body>

<%@include file="../navigation.jsp" %>

<div class="jumbotron text-center">
	<div class="container">
		<div class="row">
			<div class="col-lg-12">
				<h1>Reigate Mens Joggers</h1>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-12">
				<security:authorize access="!isAuthenticated()">
					<c:choose>
						<c:when test="${registrationEmail != null}">
							<div class="alert alert-success">
								<span class="fa fa-envelope" aria-hidden="true"></span> Registration confirmation e-mail sent to ${registrationEmail} (Don't forget to check your spam filters and junk mail!)
							</div>
						</c:when>
						<c:when test="${registrationResponseProcessed != null && registrationResponseProcessed}">
							<div class="alert alert-success">
								${registrationResponseMessage}&nbsp;<a href="<c:url value='/'/>">Ok</a>
							</div>
						</c:when>
						<c:when test="${registrationResponseProcessed != null && !registrationResponseProcessed}">
							<div class="alert alert-danger">
									${registrationResponseMessage}&nbsp;<a href="<c:url value='/'/>">Ok</a>
							</div>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${registrationServiceAvailable}">
									<p>
										<button type="button" class="btn btn-success btn-lg" data-toggle="modal" data-target="#signupModal">SIGN UP!</button>
									</p>
								</c:when>
								<c:otherwise>
									<p>
										<span class="label label-warn">New registrations currently unavailable</span>
									</p>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</security:authorize>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-12">
				<p>Its got to be better than sitting on the sofa right? Escape that stuffy office and come for a run - you won't regret it!</p>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-12">
				<a href="https://www.facebook.com/ReigateMensJoggers" class="social-buttons"><i class="fa fa-facebook fa-fw"></i></a>
				<a href="https://twitter.com/MensJoggers" class="social-buttons"><i class="fa fa-twitter fa-fw"></i></a>
			</div>
		</div>
	</div>
</div>

<div class="container">
	<p>This is a mixed ability session so if you are returning to running, trying running for the first time or wanting to increase your running there is something for
		everyone.</p>
	<p>Its a fun, friendly group where no one gets left behind. We will have a real variety of sessions from hills work, speed work, interval training and steady runs. We also will
		have social events, races and plan special events.</p>
</div>

<hr/>

<div class="container">
	<div class="row">
		<div class="col-sm-4">
			<h3>When?</h3>
			<p>Every Tuesday 7.30pm - 8.30pm</p>
		</div>
		<div class="col-sm-4">
			<h3>Where?</h3>
			<p>Our coach Richard will be waiting for you at St Bedes School. Carlton Road, Redhill. Please do come along!</p>
		</div>
		<div class="col-sm-4">
			<h3>How Much?</h3>
			<p>First session FREE! Sessions following are £5 and can be bought in blocks of 10</p>
		</div>
	</div>
</div>

<hr/>

<div class="container-fluid small">
	<div class="row">
		<div class="col-sm-6">
			<small class="text-muted">Not a man? <a href="http://www.reigateladiesjoggers.co.uk">www.reigateladiesjoggers.co.uk</a></small>
		</div>
		<div class="col-sm-6 text-right">
			<small class="text-muted">Under construction. © Paul Verity (2017) <a href="https://github.com/pjverity/rmj" class="social-buttons small"><i
							class="fa fa-github fa-fw"></i></a></small>
		</div>
	</div>
</div>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
        crossorigin="anonymous"></script>

<security:authorize access="!isAuthenticated()">
	<c:if test="${registrationEmail == null}">
		<%@include file="../signup-dialog.jsp" %>
	</c:if>
	<c:if test="${registrationConfirmationUuid != null}">
		<%@include file="../registration-confirmation-dialog.jsp" %>
	</c:if>
</security:authorize>

</body>

</html>
