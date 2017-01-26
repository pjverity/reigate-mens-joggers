<div id="signupModal" class="modal" tabindex="-1" role="dialog" data-backdrop="static">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">Sign Up</h4>
			</div>
			<div class="modal-body">

				<c:url value="/register" var="signupUrl"/>
				<form:form id="signupForm" modelAttribute="form" action="${signupUrl}" method="post">

					<fieldset name="userdetails" form="signupForm">
						<div class="row">
							<div class="form-group col-md-5">
								<form:label cssClass="control-label" path="firstName">First Name</form:label>
								<form:input cssClass="form-control" autocomplete="section-userdetails given-name" pattern="[a-zA-Z- ]+" path="firstName" placeholder="Ron"/>
								<div id="firstName.errors"></div>
							</div>

							<div class="form-group col-md-7">
								<form:label cssClass="control-label" path="lastName">Last Name</form:label>
								<form:input cssClass="form-control" autocomplete="section-userdetails family-name" pattern="[a-zA-Z- ]+" path="lastName" placeholder="Further"/>
								<div id="lastName.errors"></div>
							</div>
						</div>

						<div class="form-group">
							<form:label cssClass="control-label" path="emailAddress">Email Address</form:label>
							<form:input cssClass="form-control" autocomplete="section-userdetails home email" path="emailAddress" placeholder="ron.further@home.co.uk"/>
							<div id="emailAddress.errors"></div>
						</div>

						<div class="form-group">
							<form:label cssClass="control-label" path="confirmEmailAddress">Confirm Email Address</form:label>
							<form:input cssClass="form-control" autocomplete="section-userdetails home email" path="confirmEmailAddress" placeholder="ron.further@home.co.uk"/>
							<div id="confirmEmailAddress.errors"></div>
						</div>

						<div class="row">
							<div class="form-group col-md-6">
								<form:label cssClass="control-label" path="password">Password</form:label>
								<form:password cssClass="form-control" path="password" placeholder="password"/>
								<div id="password.errors"></div>
							</div>

							<div class="form-group col-md-6">
								<form:label cssClass="control-label" path="reenteredPassword">Re-enter Password</form:label>
								<form:password cssClass="form-control" path="reenteredPassword" placeholder="password"/>
								<div id="reenteredPassword.errors"></div>
							</div>
						</div>

						<div class="form-group">
							<div id="g-recaptcha" class="g-recaptcha"></div>
							<form:hidden id="reCaptchaResponse" path="reCaptchaResponse"/>
							<div id="reCaptchaResponse.errors"></div>
						</div>

					</fieldset>
					<div id="general.errors"></div>

					<div class="modal-footer">
						<form:button id="signup-cancel" type="button" class="btn btn-default" data-dismiss="modal">Cancel</form:button>
						<form:button type="submit" class="btn btn-primary">Sign Up! <i id="signup-spinner" class="fa fa-circle-o-notch fa-spin" ></i></form:button>
					</div>
				</form:form>

			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div>
<!-- /.modal -->

<script id="signup-script" type="text/javascript" src="<c:url value='/js/signup-dialog.js' />" data-redirectUrl="<c:url value='/' />">
</script>

<script src="https://www.google.com/recaptcha/api.js?onload=onloadCallback&render=explicit" async defer></script>