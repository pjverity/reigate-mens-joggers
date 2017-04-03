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
</head>

<body>

<%@include file="../navigation.jsp" %>

<div class="container text-center">

	<div class="row">
		<img class="img-responsive center-block" src="<c:url value="/images/logo-and-name.svg"/>" width="320px">
	</div>

	<div class="row lead">
		It's got to be better than sitting on the sofa right?<br/>
		Escape that stuffy office and come for a run - you won't regret it!
	</div>

	<div class="row">
		<a href="https://www.facebook.com/ReigateMensJoggers" class="glyphicon fa fa-2x fa-facebook-official social-buttons" style="background-color: #3B5998;"></a>
		<a href="mailto:anna@reigateladiesjoggers.co.uk (Anna),emma@reigateladiesjoggers.co.uk (Emma)?cc=administrator@reigatemensjoggers.co.uk (RMJ Admin)&subject=RMJ%20Enquiry"
		   class="glyphicon fa fa-2x fa-envelope social-buttons larger" style="background-color: #5cb85c"></a>
		<a href="https://twitter.com/MensJoggers" class="glyphicon fa fa-2x fa-twitter social-buttons" style="background-color: #55ACEE"></a>
	</div>

</div>

<hr/>

<div class="container">

	<div class="row">
		<div class="col-sm-12">
			<p>This is a mixed ability session so if you are returning to running, trying running for the first time or wanting to increase your running there is something for
				everyone.</p>
			<p>It's a fun, friendly group where no one gets left behind. We will have a real variety of sessions from hills work, speed work, interval training and steady runs. We also
				will have social events, races and plan special events.</p>
		</div>
	</div>

	<div class="row">
		<div class="col-xs-6 col-sm-3">
			<h3><i class="fa fa-calendar-o" style="color: cornflowerblue"></i> When?</h3>
			<p>Every Tuesday 7.30pm - 8.30pm</p>
		</div>
		<div class="col-xs-6 col-sm-3">
			<h3><i class="fa fa-map-marker" style="color: cornflowerblue"></i> Where?</h3>
			<address>
				<strong><a href="https://goo.gl/maps/fTJHKQQqRVK2" target="_blank">St Bedes School</a></strong><br/>
				Carlton Road<br/>
				Redhill. RH1 2LQ
			</address>
			<p>Our coach Richard will be waiting you. Please do come along!</p>
		</div>
		<div class="col-xs-6 col-sm-3">
			<h3><i class="fa fa-gbp" style="color: cornflowerblue"></i> How Much?</h3>
			<p>First session <strong>FREE!</strong></p>
			<p>Sessions following are £5 and can be bought in blocks of 10</p>
		</div>
		<div class="col-xs-6 col-sm-3">
			<h3><i class="fa fa-group" style="color: cornflowerblue"></i> Join!</h3>
			<p><strong><a data-toggle="modal" data-target="#signupModal" style="cursor: pointer">Sign up</a></strong>
				to keep updated as our site grows. We'll deliver information direct to your inbox as we
				add more content.
			</p>
		</div>
	</div>

	<hr/>

	<div class="row">
		<div class="media">
			<div class="media-left text-center">
				<c:url value="/organiser/profile" var="organiserProfileUrl"/>
				<a href="${organiserProfileUrl}">
					<img src="<c:url value="/images/richard_feist.png"/>" alt="Coach Richard">Richard Feist
				</a>
			</div>
			<div class="media-body">
				<h3 class="media-heading">RMJ Coach</h3>
				<p>With many years of sports coaching, you'll be in good hands with RMJ's experienced coach, Richard.</p>

				<p>Whether you're an absolute newbie needing that kick start to get you on your way to a healthier lifestyle, or
					a hardened all weather runner, Richard will be able to guide you towards achieving your goals.</p>

				<p>Still not convinced? Check out <a href="${organiserProfileUrl}">Richard's profile</a> to find out more.</p>
			</div>
		</div>
	</div>
</div>

<hr/>

<div class="container">

<div id="gallery-carousel" class="carousel slide" data-ride="carousel">
	<!-- Indicators -->
	<ol class="carousel-indicators">
		<li data-target="gallery-carousel" data-slide-to="0" class="active"></li>
		<li data-target="gallery-carousel" data-slide-to="1"></li>
		<li data-target="gallery-carousel" data-slide-to="2"></li>
	</ol>

	<!-- Wrapper for slides -->
	<div class="carousel-inner" role="listbox">
		<div class="item active">
			<img src="<c:url value="/images/gallery/1.jpeg"/>" alt="RMJ Gallery Run">
			<div class="carousel-caption">
				RMJ's First Run
			</div>
		</div>
		<div class="item">
			<img src="<c:url value="/images/gallery/2.jpeg"/>" alt="RMJ Gallery Run">
		</div>
		<div class="item">
			<img src="<c:url value="/images/gallery/3.jpeg"/>" alt="RMJ Gallery Run">
		</div>
	</div>

	<!-- Controls -->
	<a class="left carousel-control" href="#gallery-carousel" role="button" data-slide="prev">
		<span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
		<span class="sr-only">Previous</span>
	</a>
	<a class="right carousel-control" href="#gallery-carousel" role="button" data-slide="next">
		<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
		<span class="sr-only">Next</span>
	</a>
</div>
</div>

<hr/>

<div class="container-fluid small">
	<div class="row">
		<div class="col-xs-6">
			<small class="text-muted">Not a man? <a href="http://www.reigateladiesjoggers.co.uk">www.reigateladiesjoggers.co.uk</a></small>
		</div>
		<div class="col-xs-6 text-right">
			<small class="text-muted">© Paul Verity (2017) <a href="https://github.com/pjverity/rmj"><i class="fa fa-github"></i></a></small>
		</div>
	</div>
</div>

<security:authorize access="!isAuthenticated()">
	<%@include file="../signup-dialog.jsp" %>
</security:authorize>

</body>

</html>
