<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>

<openmrs:require privilege="Manage Connector" otherwise="/login.htm" redirect="/module/odkconnector/manageConcept.form"/>

<%@ include file="../template/localHeader.jsp" %>

<c:if test="${message != null}">
	<div id="message"><spring:message code="${message}" text="${message}"/></div>
</c:if>

<div>
	<a href="conceptConfiguration.form">Create Configuration</a>
	<table>
		<c:forEach items="${configurations}" var="configuration">
			<tr>
				<td><a href="manageConcept.form?uuid=${configuration.uuid}">Edit Configuration</a></td>
				<td>${configuration.id}</td>
				<td>${configuration.name}</td>
				<td>${configuration.description}</td>
			</tr>
		</c:forEach>
	</table>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>
