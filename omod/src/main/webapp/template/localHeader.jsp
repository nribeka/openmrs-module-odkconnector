<spring:htmlEscape defaultHtmlEscape="true" />
<ul id="menu">
	<li class="first">
		<a href="${pageContext.request.contextPath}/admin">
			<spring:message code="admin.title.short" />
		</a>
	</li>

	<li <c:if test='<%= request.getRequestURI().contains("/manage") %>'>class="active"</c:if>>
		<a href="${pageContext.request.contextPath}/module/odkconnector/manage.form">
			<spring:message code="odkconnector.manage" />
		</a>
	</li>
</ul>
<h2>
	<spring:message code="odkconnector.title" />
</h2>
