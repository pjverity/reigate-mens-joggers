<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">

<c:set var="pageTitleSuffix" value="Gallery"/>

<head>
	<%@include file="../head-common.jsp" %>

	<script src="https://unpkg.com/imagesloaded@4/imagesloaded.pkgd.min.js"></script>
	<script src="https://unpkg.com/masonry-layout@4/dist/masonry.pkgd.min.js"></script>

</head>

<body>

<%@include file="../navigation.jsp" %>

<div class="container">
	<div class="page-header">
		<h1>${galleryViewModelObject.galleryName}</h1>
	</div>

	<div id="pageNavigator">
		<c:forEach var="pageNo" begin="1" end="${galleryViewModelObject.totalPages}">
			<a href="<c:url value="/world/gallery/${currentGroupNsid}/${pageNo}" />">${pageNo}</a>
		</c:forEach>
	</div>

	<div class="grid">
		<c:forEach var="url" items="${galleryViewModelObject.imageUrls}">
			<img class="thumbnail grid-item" src="${url}"/>
		</c:forEach>
	</div>

</div>

</body>

<script type="text/javascript">

    $(function () {
        var $grid = $('.grid').imagesLoaded( function() {
            $grid.masonry({
                itemSelector: '.grid-item'
            });
        });
    });

</script>



</html>
