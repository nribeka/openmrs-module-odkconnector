<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>

<openmrs:require privilege="Manage Connector" otherwise="/login.htm" redirect="/module/odkconnector/manageConcept.form"/>

<%@ include file="../template/localHeader.jsp" %>

<style type="text/css">

	#message {
		background-color: lightyellow;
	}

	fieldset {
		padding: 0;
		margin-bottom: 1em;
	}

	legend {
		margin-left: 1em;
		margin-left: 1em;
		color: #000000;
		font-weight: bold;
	}

	fieldset ol {
		padding: 0 0 1em 1em;
		list-style: none;
	}

	fieldset li {
		padding-bottom: 0.5em;
	}

	ol li label {
		display: block;
	}

	fieldset ul {
		padding: 0 0 0 0;
		list-style: none;
	}
</style>

<c:if test="${message != null}">
	<div id="message"><spring:message code="${message}" text="${message}"/></div>
</c:if>

<div>
	<form method="post">
		<fieldset>
			<ol>
				<li>
					<label for="configurationName"><spring:message code="odkconnector.manage.configurationName"/></label>
					<input name="name" type="text" id="configurationName" />
				</li>
				<li>
					<label for="configurationDescription"><spring:message code="odkconnector.manage.configurationDescription"/></label>
					<textarea name="description" id="configurationDescription" rows="20" cols="30"></textarea>
				</li>
				<li><input type="submit" value="<spring:message code='odkconnector.manage.configurationSave' />"/></li>
			</ol>
		</fieldset>
	</form>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>
