const $signupForm = $("#signupForm");
$('#signup-spinner').toggle(false);

$('#signupModal').on('hidden.bs.modal', function (e) {
    resetForm();
});

// Attach a submit handler to the form
$signupForm.submit(function (event) {

    // Stop form from submitting normally
    event.preventDefault();

    // Get some values from elements on the page:
    const url = $signupForm.attr("action");

    resetErrors();

    // Send the data using post
    var posting = $.post(url, $signupForm.serialize());

    $('#signup-cancel').attr('disabled', 'disabled');
    $(':submit').attr('disabled', 'disabled');
    $(':input').attr('disabled', 'disabled');
    $(':password').attr('disabled', 'disabled');
    $('#signup-spinner').toggle(true);

    posting.done(function (result) {

        if (result.success) {
            window.location.href = $('#signup-script').attr('data-redirectUrl');
            return;
        }

        grecaptcha.reset();
        $('#reCaptchaResponse').val("");

        $('#signup-cancel').removeAttr('disabled');
        $(':submit').removeAttr('disabled');
        $(':input').removeAttr('disabled');
        $(':password').removeAttr('disabled');
        $('#signup-spinner').toggle(false);

        if (result.fieldErrors !== null) {
            renderFieldErrors(result.error, result.fieldErrors);
        }
        else {
            renderGeneralError("Oops! Something went wrong. We'll punish our developers for this...", result.error);
        }
    });
});

function renderGeneralError(headerText, error) {
    var $general = $('#general\\.errors');
    $general.addClass('alert alert-danger');
    $general.append('<strong>' + headerText + '</strong>');
    $general.append('<p>' + error + '</p>')
}

function renderFieldErrors(error, fieldErrors) {
    renderGeneralError("Oops! Computer says:", error);

    _.forEach(fieldErrors, function (fieldError) {
        const fieldId = '#' + fieldError.field;
        const $invalidInput = $signupForm.find(fieldId);
        $invalidInput.parent('.form-group').addClass("has-error");

        const errorsNodeId = fieldId + '\\.errors';
        const $errorMessages = $(errorsNodeId);
        $errorMessages.append('<span>' + fieldError.defaultMessage + '</span><br/>');
    });
}

function resetErrors() {
    const $general = $('#general\\.errors');
    $general.removeClass('alert alert-danger');
    $general.children().remove();

    // Remove all error messages
    var $element = $('#firstName\\.errors');
    $element.children().remove();
    $element.parent().removeClass('has-error');

    $element = $('#lastName\\.errors');
    $element.children().remove();
    $element.parent().removeClass('has-error');

    $element = $('#emailAddress\\.errors');
    $element.children().remove();
    $element.parent().removeClass('has-error');

    $element = $('#confirmEmailAddress\\.errors');
    $element.children().remove();
    $element.parent().removeClass('has-error');

    $element = $('#password\\.errors');
    $element.children().remove();
    $element.parent().removeClass('has-error');

    $element = $('#reenteredPassword\\.errors');
    $element.children().remove();
    $element.parent().removeClass('has-error');

    $element = $('#reCaptchaResponse\\.errors');
    $element.children().remove();
    $element.parent().removeClass('has-error');
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
            const $reCaptchaResponse = $('#reCaptchaResponse\\.errors');
            $reCaptchaResponse.children().remove();
            $reCaptchaResponse.parent().removeClass('has-error');
        }
    });
};
