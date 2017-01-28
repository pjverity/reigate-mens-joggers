<nav class="navbar navbar-default navbar-fixed-top">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a href="<c:url value="/"/>" class="navbar-brand">RMJ</a>
		</div>

		<%-- Display the users name and a way to log out if a user is logged in --%>
		<security:authorize access="isAuthenticated()">
			<form name="logoutForm" action="<c:url value="/logout"/>" method="post" role="logout">
				<security:csrfInput/>
				<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown">
						<a href="<c:url value="/"/>" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
								${userFirstName}&nbsp;${userLastName}&nbsp;<span class="caret"></span>
						</a>
						<ul class="dropdown-menu">
							<li>
								<security:authorize access="hasAuthority('ADMIN')">
									<a href="<c:url value="/admin/usermanagement"/>" >Admin</a>
								</security:authorize>
								<security:authorize access="hasAuthority('MEMBER')">
									<a href="<c:url value="/member/home"/>">Member</a>
								</security:authorize>
							</li>
							<li>
								<a href="<c:url value="/member/account"/>">My Account</a>
							</li>
							<li>
								<a href="#" onclick="document.logoutForm.submit()">Logout</a>
							</li>
						</ul>
					</li>
				</ul>
			</form>
		</security:authorize>

		<%-- If no user is logged in, then provide a mini login form --%>
		<security:authorize access="!isAuthenticated()">
			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">

				<form class="navbar-form navbar-right" role="login" action="<c:url value="/login"/>" method="post">
					<c:if test="${not empty paramValues['error']}">
						<span class="text-muted">(Login Failed)</span>
					</c:if>
					<div class="form-group">
						<input id="username" name="username" autocomplete="username" type="text" class="form-control" placeholder="e-mail address"/>
					</div>
					<div class="form-group">
						<input id="password" name="password" autocomplete="current-password" type="password" class="form-control" placeholder="password"/>
					</div>
					<button type="submit" class="btn btn-default">Login</button>
					<security:csrfInput/>
				</form>
			</div>
		</security:authorize>

	</div>
</nav>
