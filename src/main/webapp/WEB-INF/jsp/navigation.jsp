<script id="loginScript" data-url="<c:url value='/rest/csrf'/>">
    function login() {

        // Obtain the URL from an HTML attribute which is modified by the proxy to remove the context path
        const url = $('#loginScript').attr('data-url');

        $.getJSON(url, function (response) {
            const $csrf = $('#csrf');
            $csrf.val(response.token);
            $csrf.attr('name', response.parameterName);
            $('#loginForm').submit();
        });
    }
</script>

<nav class="navbar navbar-default navbar-fixed-top">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="<c:url value='/'/>" style="padding: 0">
				<img src="<c:url value="/images/logo-only.svg"/>" height="50px">
			</a>
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
		</div>

		<%-- Display the users name and a way to log out if a user is logged in --%>
		<security:authorize access="isAuthenticated()">
			<form name="logoutForm" action="<c:url value='/logout'/>" method="post" role="logout">
				<security:csrfInput/>
				<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
					<ul class="nav navbar-nav navbar-right">
						<li class="dropdown">
							<a href="<c:url value="/"/>" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
									${userFirstName}&nbsp;${userLastName}&nbsp;<span class="caret"></span>
							</a>
							<ul class="dropdown-menu">
								<security:authorize access="hasRole('ADMIN')">
									<li>
										<a href="<c:url value="/admin/user-management"/>">User Management</a>
									</li>
									<li>
										<a href="<c:url value="/admin/token-management"/>">Token Management</a>
									</li>
								</security:authorize>
								<security:authorize access="hasRole('ORGANISER')">
									<li>
										<a href="<c:url value="/organiser/event-registration"/>">Event Registration</a>
									</li>
								</security:authorize>
								<security:authorize access="hasRole('MEMBER')">
									<li>
										<a href="<c:url value="/member/home"/>">Member</a>
									</li>
								</security:authorize>
								<li>
									<a href="<c:url value="/member/account"/>">My Account</a>
								</li>
								<li>
									<a href="#" onclick="document.logoutForm.submit()">Logout</a>
								</li>
							</ul>
						</li>
					</ul>
				</div>
			</form>
		</security:authorize>

		<%-- If no user is logged in, then provide a mini login form --%>
		<security:authorize access="!isAuthenticated()">
			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">

				<form id="loginForm" class="navbar-form navbar-right" role="login" action="<c:url value="/login"/>" method="post">
					<c:if test="${not empty paramValues['error']}">
						<span class="text-muted">(Login Failed)</span>
					</c:if>
					<div class="form-group">
						<input id="username" name="username" autocomplete="username" type="text" class="form-control" placeholder="e-mail address"/>
					</div>
					<div class="form-group">
						<input id="password" name="password" autocomplete="current-password" type="password" class="form-control" placeholder="password"/>
					</div>
					<button type="button" class="btn btn-default btn-primary" onclick="login()">Login</button>
					<input id="csrf" type="hidden">
				</form>
			</div>
		</security:authorize>

	</div>
</nav>
