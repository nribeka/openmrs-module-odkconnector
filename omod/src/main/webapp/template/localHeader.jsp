<spring:htmlEscape defaultHtmlEscape="true"/>
<ul id="menu">
	<li class="first">
		<a href="${pageContext.request.contextPath}/admin">
			<spring:message code="admin.title.short"/>
		</a>
	</li>

	<li
	<c:if test='<%= request.getRequestURI().contains("/manageConcept") %>'>class="active"</c:if>>
	<a href="${pageContext.request.contextPath}/module/odkconnector/concept/configurationList.form">
		<spring:message code="odkconnector.manage"/>
	</a>
	</li>

	<li
	<c:if test='<%= request.getRequestURI().contains("/manageProperty") %>'>class="active"</c:if>>
	<a href="${pageContext.request.contextPath}/module/odkconnector/reporting/manageProperty.form">
		<spring:message code="odkconnector.cohortDefinition.manage"/>
	</a>
	</li>
</ul>
<h2>
	<spring:message code="odkconnector.title"/>
</h2>
