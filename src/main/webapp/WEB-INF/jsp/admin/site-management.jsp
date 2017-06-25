<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">

<c:set var="pageTitleSuffix" value="Site Management"/>

<head>
	<%@include file="../head-common.jsp" %>

	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
</head>

<body>

<%@include file="../navigation.jsp" %>

<div class="container">
	<div class="page-header">
		<h1>Site Management</h1>
	</div>

	<div class="row">
		<div class="col-md-12">
			<form class="form-inline">

				<div class="row">
					<div class="col-md-12">
						<label class="control-label" for="selectedGroup">Selected Group</label>
						<div class="form-control-static" id="selectedGroup">${groupName}&nbsp;(${nsid})</div>
					</div>
				</div>

				<div class="row">
					<div class="col-md-12">

						<div class="form-group">
							<label class="control-label">Search Text</label>
							<input id="groupName" class="form-control" placeholder="Search Flickr Groups"/>
							<button type="button" class="btn btn-default" onclick="searchFlickrGroups()">Search</button>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>

	<div class="row" style="margin-top: 1em">
		<div class="col-md-6">

			<form id="testForm" class="form" method="post">

				<div class="form-group">

					<label class="control-label" for="groupSelection">Groups</label>
					<select id="groupSelection" class="form-control" size="10">
					</select>
					<button type="button" class="btn btn-default" onclick="saveGroupNsid()">Set</button>
				</div>
				<input id="formGroupName" type="hidden" name="groupName" value="">
				<input id="formGroupId" type="hidden" name="groupId" value="">
				<security:csrfInput/>
			</form>
		</div>
	</div>

</div>

</body>

<script type="text/javascript">
    function searchFlickrGroups() {
        const groupName = $('#groupName').val();
        $.getJSON("/rmj/admin/flickr/searchGroups", {searchText: groupName})
            .done(function (groups) {
                console.log(groups);
                $('#groupSelection > option').remove();
                _.forOwn(groups, function (value, key) {
                    console.log(key, value);
                    $('#groupSelection').append('<option value="' + value + '">' + key + '</option>')

                });
            })
            .fail(function () {
                console.log("error");
            });
    }

    function saveGroupNsid() {
        const selectedGroupNsid = $('#groupSelection :selected').val();
        const selectedGroupName = $('#groupSelection :selected').text();
        $('#formGroupName').val(selectedGroupName);
        $('#formGroupId').val(selectedGroupNsid);
        var jqxhr = $.post("/rmj/admin/flickr/saveGroupNsid", $("#testForm").serialize(), function () {
            $('#selectedGroup').text(selectedGroupName + ' (' + selectedGroupNsid + ')');
        });
    }

</script>

</html>
