<nav class="navbar navbar-default navbar-fixed-top">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a href="#" class="navbar-brand">RMJ</a>
		</div>

		<%-- Display the users name and a way to log out if a user is logged in --%>
		<security:authorize access="isAuthenticated()">
			<form:form name="logoutForm" modelAttribute="userdetails" action="/rmj/logout" role="logout">
				<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav navbar-right">
					<security:authentication property="principal.username" var="username"/>
					<li class="dropdown">
						<a href="/" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
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
			</div>
		</security:authorize>

		<%-- If no user is logged in, then provide a mini login form --%>
		<security:authorize access="!isAuthenticated()">
			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">

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
			</div>
		</security:authorize>

	</div>
</nav>
