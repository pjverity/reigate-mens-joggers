<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">

<c:set var="pageTitleSuffix" value="Member Home"/>

<head>
	<%@include file="../head-common.jsp" %>
</head>

<body>

<%@include file="../navigation.jsp" %>

<div class="container">
	<div class="page-header">
		<h1>Messages
			<small>(More Coming Soon)</small>
		</h1>
	</div>
	<p>Hi ${userFirstName}!</p>
	<p>
		This is your very own page at Reigate Mens Joggers. It's a little barren at the moment but we hope to fix that soon enough as we start to build
		up our site.
	</p>
	<p>
		Use the navigation menu on the top bar of any page to access other member areas.
	</p>
	<p>
		If you spot any errors or run in to any difficulties with the site, then please contact us at <a href="mailto:administrator@reigatemensjoggers.co.uk">administrator@reigatemensjoggers.co.uk</a>
		and we will endeavour to fix it.
	</p>
	<p>
		We hope to see you on our the next run!
	</p>
	<p>
		The RMJ Team
	</p>

	<div class="text-center text-success bg-success">
		<div style="font-size: 4em"><strong>${tokenCount}</strong></div>
		Tokens
	</div>

</div>

</body>

</html>
