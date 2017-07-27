<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">

<c:set var="pageTitleSuffix" value="Site Management"/>

<head>
	<%@include file="../head-common.jsp" %>
</head>

<body>

<%@include file="../navigation.jsp" %>

<div class="container mt-3">

	<form class="form mb-2">


		<%-- Search box --%>

		<div id="search-form-group" class="form-group">
			<div class="input-group">
				<div class="input-group-addon"><i class="fa fa-fw fa-search"></i> </div>
				<input id="groupName" class="form-control" placeholder="Search Flickr Groups...">
				<span class="input-group-btn">
					<button class="btn btn-secondary" onclick="searchFlickrGroups()" type="button">Search</button>
				</span>
			</div>
			<small class="form-control-feedback">&nbsp;</small>
		</div>

	</form>


	<%-- Search results --%>

	<form id="groupSelectionForm" class="form" method="post">

		<div id="groupSelectionGroup" class="form-group">
			<label class="control-label" for="groupSelection">Groups <br/><small id="selectedGroupStatic" class="text-muted">${groupName}&nbsp;(${nsid})</small></label>
			<select id="groupSelection" class="form-control" size="10">
			</select>
			<small class="form-control-feedback">&nbsp;</small>
		</div>

		<input id="formGroupName" type="hidden" name="groupName" value="">
		<input id="formGroupId" type="hidden" name="groupId" value="">

		<security:csrfInput/>

		<button type="button" class="btn btn-primary" onclick="saveGroupNsid()">Set</button>
	</form>

</div>

</body>

<script id="site-management-script" type="text/javascript" data-url="<c:url value='/'/>">

	const contextPath = $('#site-management-script').attr('data-url');

	const saveGroupUrl = contextPath + "admin/flickr/saveGroupNsid";
	const searchUrl = contextPath + "admin/flickr/searchGroups";

	const $searchFormGroup = $('#search-form-group');
	const $searchFeedback = $searchFormGroup.find('.form-control-feedback');

	const $groupSelectionGroup = $('#groupSelectionGroup');
	const $groupSelectionFeedback = $groupSelectionGroup.find('.form-control-feedback');

	const $groupSelection = $('#groupSelection');

	function searchFlickrGroups() {

		clearFormFeedback();

		const groupName = $('#groupName').val();

		startSearchSpinner();

		$.getJSON(searchUrl, {searchText: groupName})
			.done(function (groups) {

				// Remove all previous options
				$groupSelection.find('> option').remove();

				// Add all new options
				_.forOwn(groups, function (value, key) {
					$groupSelection.append('<option value="' + value + '">' + key + '</option>')
				});

				setSearchSuccessMessage('Found ' + _.keys(groups).length + ' groups');

			})
			.fail(function (jqxhr, textStatus, error) {
				setSearchErrorMessage(error);
			})
		.always(function () {
			stopSearchSpinner();
		});
	}

	function saveGroupNsid() {

		clearFormFeedback();

		const selectedGroupNsid = $groupSelection.find(':selected').val();
		const selectedGroupName = $groupSelection.find(':selected').text();

		$('#formGroupName').val(selectedGroupName);
		$('#formGroupId').val(selectedGroupNsid);

		$.post(saveGroupUrl, $('#groupSelectionForm').serialize())
			.done(function (response) {
				if (response === true) {
					$('#selectedGroupStatic').text(selectedGroupName + ' (' + selectedGroupNsid + ')');
				}
				else {
					setUpdatedErrorMessage('Failed to update group information');
				}
			})
			.fail(function (jqxhr, textStatus, error) {
				setUpdatedErrorMessage(error);
			});

	}

	function startSearchSpinner() {
	  $searchFormGroup.find('.input-group-addon i').removeClass('fa-search');
	  $searchFormGroup.find('.input-group-addon i').addClass('fa-spin fa-circle-o-notch');
	  $searchFormGroup.find('.btn').addClass('disabled');
  }

  function stopSearchSpinner() {
	  $searchFormGroup.find('.btn').removeClass('disabled');
	  $searchFormGroup.find('.input-group-addon i').removeClass('fa-spin fa-circle-o-notch');
	  $searchFormGroup.find('.input-group-addon i').addClass('fa-search');
  }

	function clearFormFeedback() {
		$searchFormGroup.toggleClass('has-danger has-success', false);
		$groupSelectionGroup.toggleClass('has-danger has-success', false);

		$searchFeedback.text('\xa0');
		$groupSelectionFeedback.text('\xa0');
	}

	function setSearchSuccessMessage(message) {
		$searchFormGroup.toggleClass('has-success', true);
		$searchFeedback.text(message);
	}

	function setSearchErrorMessage(message) {
		$searchFormGroup.toggleClass('has-danger', true);
		$searchFeedback.text(message);
	}

	function setUpdatedErrorMessage(message) {
		$groupSelectionGroup.toggleClass('has-danger', true);
		$groupSelectionFeedback.text(message);
	}


</script>

</html>
