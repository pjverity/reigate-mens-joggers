<div id="registrationConfirmationModal" class="modal" tabindex="-1" role="dialog" data-backdrop="static">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">Confirm or Decline Registration</h4>
			</div>
			<div class="modal-body">

				<c:url value="/processRegistration" var="confirmUrl"/>
				<form id="processRegistrationForm" action="${confirmUrl}" method="post">

					<p>
						<strong>Name:</strong> ${firstName}&nbsp;${lastName}
						<br/>
						<strong>E-mail address:</strong> ${emailAddress}
					</p>

					<input type="hidden" name="uuid" value="${uuid}">
					<input type="hidden" id="isConfirm" name="isConfirm">

					<div class="modal-footer">
						<div class="btn-group btn-group-justified" role="group" aria-label="...">
							<div class="btn-group" role="group">
								<button id="confirm-registration" type="button" class="btn btn-danger" onclick="decline()">That's not me!</button>
							</div>
							<div class="btn-group" role="group">
								<button id="decline-registration" type="button" class="btn btn-success" onclick="confirm()">Sign me up!</button>
							</div>
						</div>
					</div>
					<security:csrfInput/>
				</form>

			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div>
<!-- /.modal -->

<script>
    $('#registrationConfirmationModal').modal('show');

    function confirm() {
        $('#isConfirm').attr('value', 'true');
        $('#processRegistrationForm').submit();
    }

    function decline() {
        $('#isConfirm').attr('value', 'false');
        $('#processRegistrationForm').submit();
    }
</script>