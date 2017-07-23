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

<nav class="navbar navbar-toggleable-md navbar-light bg-faded">
	<button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
	        aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>
	<a class="navbar-brand" href="<c:url value='/'/>" style="padding: 0">
		<img src="<c:url value="/images/logo-only.svg"/>" height="50px">
	</a>

	<div class="collapse navbar-collapse" id="navbarSupportedContent">

		<%-- mr-auto causes anything after this to be right aligned--%>
		<div class="navbar-text mr-auto">
			${pageTitleSuffix}
		</div>

		<%-- Display the users name and a way to log out if a user is logged in --%>
		<security:authorize access="isAuthenticated()">
			<ul class="navbar-nav">
				<li class="nav-item dropdown">
					<a class="nav-link dropdown-toggle" href="<c:url value="/"/>" class="dropdown-toggle" data-toggle="dropdown" role="button">
							${userFirstName}&nbsp;${userLastName}&nbsp;<span class="caret"></span>
					</a>

					<div class="dropdown-menu dropdown-menu-right">
						<security:authorize access="hasRole('ADMIN')">
							<h6 class="dropdown-header">Admin</h6>
							<a class="dropdown-item" href="<c:url value="/admin/site-management"/>">Site Management</a>
							<a class="dropdown-item" href="<c:url value="/admin/user-management"/>">User Management</a>
							<a class="dropdown-item" href="<c:url value="/admin/token-management"/>">Token Management</a>
							<div class="dropdown-divider"></div>
						</security:authorize>
						<security:authorize access="hasRole('ORGANISER')">
							<h6 class="dropdown-header">Coach</h6>
							<a class="dropdown-item" href="<c:url value="/organiser/event-scheduling"/>">Run Scheduling</a>
							<a class="dropdown-item" href="<c:url value="/organiser/event-completion"/>">Run Completion</a>
							<div class="dropdown-divider"></div>
						</security:authorize>
						<security:authorize access="hasRole('MEMBER')">
							<h6 class="dropdown-header">Member</h6>
							<a class="dropdown-item" href="<c:url value="/member/home"/>">Messages</a>
							<a class="dropdown-item" href="<c:url value="/member/account"/>">Account</a>
							<div class="dropdown-divider"></div>
							<form class="form-inline" name="logoutForm" action="<c:url value='/logout'/>" method="post" role="logout">
								<security:csrfInput/>
								<a class="dropdown-item" href="#" onclick="document.logoutForm.submit()">Logout</a>
							</form>
						</security:authorize>
					</div>
				</li>
			</ul>
		</security:authorize>

		<%-- If no user is logged in, then provide a mini login form --%>
		<security:authorize access="!isAuthenticated()">
			<form id="loginForm" class="form-inline" action="<c:url value="/login"/>" method="post">
				<c:if test="${not empty paramValues['error']}">
					<span class="text-muted">(Login Failed)</span>
				</c:if>
				<input id="username" name="username" autocomplete="username" class="form-control mr-sm-2" placeholder="e-mail address"/>
				<input id="password" name="password" autocomplete="current-password" type="password" class="form-control mt-1 mt-sm-0 mr-sm-2" placeholder="password"/>
				<button type="button" class="btn btn-outline-success my-2 my-sm-0" onclick="login()">Login</button>
				<security:csrfInput/>
			</form>
		</security:authorize>

	</div>

</nav>