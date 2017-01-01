<div id="signupModal" class="modal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title">Sign Up</h4>
			</div>
			<div class="modal-body">

				<c:url value="/" var="signupUrl"/>
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
							<div id="g-recaptcha" class="g-recaptcha"></div>
							<form:hidden id="reCaptchaResponse" path="reCaptchaResponse"/>
							<div id="reCaptchaResponse.errors"></div>
						</div>

					</fieldset>

					<div class="modal-footer">
						<form:button type="button" class="btn btn-default" data-dismiss="modal" onclick="resetForm()">Cancel</form:button>
						<form:button type="submit" class="btn btn-primary">Sign Up!</form:button>
					</div>
				</form:form>

			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div>
<!-- /.modal -->

<script type="text/javascript">

    const $signupForm = $("#signupForm");

    // Attach a submit handler to the form
    $signupForm.submit(function (event) {

        // Stop form from submitting normally
        event.preventDefault();

        // Get some values from elements on the page:
        const $form = $(this);
        const url = $form.attr("action");

        // Send the data using post
        var posting = $.post(url, $form.serialize());

        posting.done(function (errors) {

            if ( errors.length == 0 )
            {
                window.location.href = '<c:url value="/" />';
            }

            resetErrors();
            renderErrors(errors);
        });
    });

    function renderErrors(errors)
    {
        _.forEach(errors, function (fieldError) {
            const fieldId = '#' + fieldError.field;
            const $invalidInput = $signupForm.find(fieldId);
            $invalidInput.parent('.form-group').addClass("has-error");

            const errorsNodeId = fieldId + '\\.errors';
            const $errorMessages = $(errorsNodeId);
            $errorMessages.append('<span>' + fieldError.defaultMessage + '</span><br/>');
        });
    }

    function resetErrors()
    {
        grecaptcha.reset();

        // Remove all error messages
		    const $firstNameErrors = $('#firstName\\.errors');
		    $firstNameErrors.children().remove();
        $firstNameErrors.parent().removeClass('has-error');

        const $lastNameErrors = $('#lastName\\.errors');
        $lastNameErrors.children().remove();
		    $lastNameErrors.parent().removeClass('has-error');

        const $emailAddressErrors = $('#emailAddress\\.errors');
        $emailAddressErrors.children().remove();
        $emailAddressErrors.parent().removeClass('has-error');

        const $reCaptchaResponse = $('#reCaptchaResponse\\.errors');
        $reCaptchaResponse.children().remove();
        $reCaptchaResponse.parent().removeClass('has-error');
    }

    function resetForm() {
        // Clear autocomplete styling
        $signupForm[0].reset();

        // Clear all inputs
        $.each($signupForm.find('input:text'), function (index, element) {
		        $(element).val('');
        });

        resetErrors();
    }

    // We have to set a hidden field with the value of the reCaptcha response so that it can be used in the form object
    // and be validated. The reason it's done this way is because names with hyphens are syntactically incorrect in Java.
    // Spring binds the input 'path="name"' to fields in the form object with the given name, so we can't use
    // 'g-recaptcha-response' that the API generates when the form is POST'ed.
    var onloadCallback = function () {
        grecaptcha.render('g-recaptcha', {
            sitekey: '6LdL0QsUAAAAAHcRq4_t0t-jTYJJ0bLbab5VvC3f',
            callback: function (gRecaptchaResponse) {
                $('#reCaptchaResponse').val(gRecaptchaResponse);
            }
        });
    };

</script>

<script src="https://www.google.com/recaptcha/api.js?onload=onloadCallback&render=explicit" async defer></script>
