<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">

<c:set var="pageTitleSuffix" value="Richard Fiest"/>

<head>
	<%@include file="../head-common.jsp" %>
</head>

<body>

<%@include file="../navigation.jsp" %>

<div class="container pt-3">
	<div class="page-header">
		<h1 style="text-align: center">
			<img class="media-object" src="<c:url value="/images/richard_feist.png"/>" alt="Coach Richard" style="display: inline-block"/>
			<br/>Richard Feist<br/>
			<small>(Coach)</small>
		</h1>
		<hr/>
	</div>

	<main class="clearfix">
			<p>When at school I was a long distance runner competing for Surrey and Reigate Priory in the 1500M. When at college the running didn't carry on so much, I was more of a casual
				runner from time to time as it's always been something I have enjoyed.</p>

			<p>Whilst studying, I started my coaching career; first in football and cricket, then swimming.</p>

			<p>I went from college into leisure where I used to coach as well as being a Duty Manager in a busy Centre.</p>

			<p>In 2006 I took up a management role in the Sport Centre at The Hawthorns School, this is when I rediscovered running.</p>

			<p>I was heavily involved in coaching the Athletics team and also took the cross country training weekly plus taking them to fixtures. I completed 4 London marathons - running
				sub 4 hour marathons was a highlight as well as raising awareness for CRY (Cardiac Risk in the Young).</p>

			<p>I am now really enjoying a new career coaching many sports and working with the ladies at RLJ to develop RMJ and working with our men - enabling them to get fit and improve
				their running abilities.</p>

			<p>I look forward to meeting you and helping you achieve your goals.</p>
	</main>

	<h3 class="text-center">
		<a href="<c:url value="/" />">Home</a>
	</h3>

</div>

<%@include file="../footer-common.jsp" %>

</body>

</html>
