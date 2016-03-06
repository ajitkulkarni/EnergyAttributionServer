<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Consumption</title>
</head>
<body>
	<i>Details for user:</i> <b>${requestScope.userName} </b>
	<br />
	<br />
	<i>Total Consumption:</i> <b>${requestScope.totalConsumption} </b>
	<br />
	<br />
	
	<i>Consumption Details:</i>
	<br />
	<br />
	<c:if test="${empty consumptions}">
	    
	    <br/>
	    No consumption record for user.
	</c:if>
	<c:if test="${not empty consumptions}">
		<table border=1>
			<tr>
				<th>Device</th>
				<th>Start Time</th>
				<th>End Time</th>
				<th>Consumption</th>
			</tr>
			<c:forEach items="${consumptions}" var="consumption">
				<tr>
					<td>${consumption.deviceName}</td>
					<td>${consumption.startTime}</td>
					<td>${consumption.endTime}</td>
					<td>${consumption.energyConsumption}</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	<br />
	<br />
	<i>ON Event Details:</i>
	<br />
	<br />
	<c:if test="${empty onEvents}">
	    <br/>
	    No ON event record for user.
	</c:if>
	<c:if test="${not empty onEvents}">
	
		<table border=1>
			<tr>
				<th>Device</th>
				<th>Start Time</th>
			</tr>
			<c:forEach items="${onEvents}" var="event">
				<tr>
					<td>${event.deviceName}</td>
					<td>${event.eventTime}</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	
</body>
</html>