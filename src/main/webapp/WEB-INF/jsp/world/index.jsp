<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">

<head>

	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="">
	<meta name="author" content="">

	<title>Reigate Mens Joggers</title>

	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
	<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
	<!--[if lt IE 9]>
	<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
	<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
	<![endif]-->

	<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet"
	      integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	<link href="css/main.css" rel="stylesheet">
	<link href="font-awesome-4.6.3/css/font-awesome.min.css" rel="stylesheet">

</head>

<body>

<nav class="navbar navbar-default navbar-fixed-top">
	<div class="container-fluid">
		<div class="navbar-header">
			<a href="#" class="navbar-brand">RMJ</a>
		</div>

		<%-- Display the users name and a way to log out if a user is logged in --%>
		<security:authorize access="isAuthenticated()">
			<form:form name="logoutForm" modelAttribute="userdetails" action="/rmj/logout" role="logout">
				<ul class="nav navbar-nav navbar-right">
					<security:authentication property="principal.username" var="username"/>
					<li class="dropdown">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
								${username} <span class="caret"></span>
						</a>
						<ul class="dropdown-menu">
							<li>
								<security:authorize access="hasAuthority('ADMIN')">
									<c:url value="admin" var="adminURL"/>
									<a href="${adminURL}">Admin</a>
								</security:authorize>
								<security:authorize access="hasAuthority('MEMBER')">
									<c:url value="member" var="memberURL"/>
									<a href="${memberURL}">Member</a>
								</security:authorize>
							</li>
							<li>
								<c:url value="account" var="accountURL"/>
								<a href="account">My Account</a>
							</li>
							<li>
								<a href="#" onclick="document.logoutForm.submit()">Logout</a>
							</li>
						</ul>
					<li>
					</li>
				</ul>
			</form:form>
		</security:authorize>

		<%-- If no user is logged in, then provide a mini login form --%>
		<security:authorize access="!isAuthenticated()">
			<form:form modelAttribute="userdetails" cssClass="navbar-form navbar-right" role="login">
				<c:if test="${not empty paramValues['error']}">
					<span class="text-muted">(Login Failed)</span>
				</c:if>
				<div class="form-group">
					<form:input path="username" type="text" class="form-control" placeholder="e-mail address"/>
				</div>
				<div class="form-group">
					<form:input path="password" type="password" class="form-control" placeholder="password"/>
				</div>
				<form:button type="submit" class="btn btn-default">Login</form:button>
			</form:form>
		</security:authorize>

	</div>
</nav>

<div class="jumbotron text-center">
	<div class="container">
		<div class="row">
			<div class="col-lg-12">
				<h1>Reigate Mens Joggers</h1>
			</div>
		</div>
		<%--
				<div class="row">
					<div class="col-lg-12">
						<p>
							<button type="button" class="btn btn-success btn-lg">SIGN UP!</button>
						</p>
					</div>
				</div>
		--%>
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
			<small class="text-muted">Under construction. © Paul Verity (2016) <a href="https://github.com/pjverity/rmj" class="social-buttons small"><i
							class="fa fa-github fa-fw"></i></a></small>
		</div>
	</div>
</div>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
        crossorigin="anonymous"></script>

</body>


</html>
