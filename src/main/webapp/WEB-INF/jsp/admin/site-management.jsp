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

	<form>

		<div class="form-group row">
			<div class="col">
				<label class="col-form-label" for="selectedGroup">Selected Group</label>
				<div class="form-control-static" id="selectedGroup">${groupName}&nbsp;(${nsid})</div>
			</div>
		</div>

		<div class="form-group row">
			<div class="col">

				<div class="input-group">
					<input id="groupName" type="text" class="form-control" placeholder="Search Flickr Groups...">
					<span class="input-group-btn">
            <button class="btn btn-secondary" onclick="searchFlickrGroups()" type="button">Search</button>
          </span>
				</div>

			</div>
		</div>
	</form>

	<form id="testForm" class="form" method="post">
		<div class=" row">
			<div class="col">

				<div class="form-group">
					<label class="control-label" for="groupSelection">Groups</label>
					<select id="groupSelection" class="form-control" size="10">
					</select>
				</div>

				<input id="formGroupName" type="hidden" name="groupName" value="">
				<input id="formGroupId" type="hidden" name="groupId" value="">

				<security:csrfInput/>
			</div>
		</div>
		<div class=" row justify-content-end">
			<div class="col-11"></div>
			<div class="col-1">
			<button type="button" class="btn btn-primary btn-block" onclick="saveGroupNsid()">Set</button>
			</div>
		</div>
	</form>

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
