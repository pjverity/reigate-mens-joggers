<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html lang="en">

<c:set var="pageTitleSuffix" value="Reigate Mens Joggers"/>

<head>
	<%@include file="../head-common.jsp" %>

	<meta name="description" content="Home of Mens Run Club in Reigate, Surrey">
	<meta name="robots" content="index,follow">

	<script src="<c:url value='/galleria/galleria-1.5.7.min.js'/>"></script>
	<script src="<c:url value='/galleria/plugins/flickr/galleria.flickr.min.js'/>"></script>

	<style>
		.galleria {
			max-width: 700px;
			height: 400px;
			background: #000;
			margin: auto;
		}
	</style>

</head>

<body>

<%@include file="../navigation.jsp" %>

<security:authorize access="!isAuthenticated()">
	<%@include file="../signup-dialog.jsp" %>
</security:authorize>

<header class="container">

	<c:if test="${!cookiesAccepted}">
		<div id="cookieAlert" class="alert alert-info alert-dismissible" role="alert" style="margin-top: 0.5em">
			<button id="acceptCookies" type="button" class="close" data-dismiss="alert" aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<strong>Cookies</strong>
			<p>
				Our site uses cookies to allow access to certain areas of the site. No personal information is stored in the cookies we use. By continuing to use our site you agree to our
				use of cookies.</p>
			<br/>
		</div>
	</c:if>

	<div class="row justify-content-center">
		<img class="img-responsive" src="<c:url value="/images/logo-and-name.svg"/>" width="320px">
	</div>

	<div class="row lead text-center justify-content-center">
		It's got to be better than sitting on the sofa right?<br/>
		Escape that stuffy office and come for a run - you won't regret it!
	</div>

	<div class="row justify-content-center align-items-center">
		<a href="https://www.facebook.com/ReigateMensJoggers" class="glyphicon fa fa-2x fa-facebook-official social-buttons" style="background-color: #3B5998;"></a>
		<a href="mailto:admin@reigatemensjoggers.co.uk (Reigate Mens Joggers)?subject=General%20Enquiry"
		   class="glyphicon fa fa-2x fa-envelope social-buttons larger" style="background-color: #5cb85c"></a>
		<a href="https://twitter.com/MensJoggers" class="glyphicon fa fa-2x fa-twitter social-buttons" style="background-color: #55ACEE"></a>
	</div>

	<br/>

	<div class="row justify-content-center">
		<div class="card w-75 border-info text-center">
			<h4 class="card-header bg-info text-white">Next Run</h4>
			<div class="card-body">
				<h4 class="card-text">${nextEvent}</h4>
			</div>
		</div>
	</div>
</header>

<main class="container">

	<hr/>

	<div class="row">
		<div class="col-12">
			<p>This is a mixed ability session so if you are returning to running, trying running for the first time or wanting to increase your running there is something for
				everyone.</p>
			<p>It's a fun, friendly group where no one gets left behind. We will have a real variety of sessions from hills work, speed work, interval training and steady runs. We also
				will have social events, races and plan special events.</p>
		</div>
	</div>

	<div class="row">
		<div class="col-6 col-sm-6 col-md-3">
			<h3><i class="fa fa-calendar-o" style="color: cornflowerblue"></i> When?</h3>
			<p>Every Tuesday 7.30pm - 8.30pm</p>
		</div>
		<div class="col-6 col-sm-6 col-md-3">
			<h3><i class="fa fa-map-marker" style="color: cornflowerblue"></i> Where?</h3>
			<address>
				<strong><a href="https://goo.gl/maps/fTJHKQQqRVK2" target="_blank">St Bedes School</a></strong><br/>
				Carlton Road<br/>
				Redhill. RH1 2LQ
			</address>
			<p>Our coach Richard will be waiting you. Please do come along!</p>
		</div>
		<div class="col-6 col-sm-6 col-md-3">
			<h3><i class="fa fa-gbp" style="color: cornflowerblue"></i> How Much?</h3>
			<p>First session <strong>FREE!</strong></p>
			<p>Sessions following are £5 and can be bought in blocks of 10</p>
		</div>
		<div class="col-6 col-sm-6 col-md-3">
			<h3><i class="fa fa-group" style="color: cornflowerblue"></i> Join!</h3>
			<p><strong><a href="" rel="nofollow" data-toggle="modal" data-target="#signupModal">Sign up</a></strong>
				to keep updated as our site grows. We'll deliver information direct to your inbox as we
				add more content.
			</p>
		</div>
	</div>

	<hr/>

	<div class="row">
		<div class="media">
			<c:url value="/world/coach-profile" var="organiserProfileUrl"/>
			<a class="text-center" href="${organiserProfileUrl}">
				<img class="d-flex align-self-start mr-3" src="<c:url value="/images/richard_feist.png"/>" alt="Coach Richard">Richard Feist
			</a>
			<div class="media-body">
				<h3 class="mt-0">RMJ Coach</h3>
				<p>With many years of sports coaching, you'll be in good hands with RMJ's experienced coach, Richard.</p>

				<p>Whether you're an absolute newbie needing that kick start to get you on your way to a healthier lifestyle, or
					a hardened all weather runner, Richard will be able to guide you towards achieving your goals.</p>

				<p>Still not convinced? Check out <a href="${organiserProfileUrl}">Richard's profile</a> to find out more.</p>
			</div>
		</div>
	</div>

	<hr/>

	<div class="galleria"></div>

</main>

<%@include file="../footer-common.jsp" %>

</body>

<script id="home-script" type="text/javascript" data-url="<c:url value='/'/>">

	const contextPath = $('#home-script').attr('data-url');

	$('#acceptCookies').on('click', function () {
		var d = new Date();
		d.setTime(d.getTime() + (365 * 24 * 60 * 60 * 1000));
		var expires = "expires=" + d.toUTCString();
		document.cookie = 'cookiesAccepted=true;' + expires;
		$('#cookieAlert').alert('close');
	});

	$(function () {
		Galleria.loadTheme(contextPath + 'galleria/themes/classic/galleria.classic.min.js');
		var flickr = new Galleria.Flickr();
		flickr.setOptions({
			sort: 'date-posted-desc',
			max: 20,
			thumbSize: 'medium'
		}).group('${flickGroupNsid}', function (data) {
			Galleria.run('.galleria', {
				dataSource: data
			});
		});

	});

</script>

</html>
