<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>

<openmrs:require privilege="Manage Connector" otherwise="/login.htm" redirect="/module/odkconnector/concept/configurationList.list"/>

<%@ include file="../template/localHeader.jsp" %>

<c:if test="${message != null}">
	<div id="message"><spring:message code="${message}" text="${message}"/></div>
</c:if>

<script type="text/javascript">

	var $j = jQuery.noConflict();

	$j(document).ready(function () {
		$j(".delete").click (function () {
			if  (confirm("<spring:message code='odkconnector.conceptConfiguration.confirmDelete' />")) {
				var row = $j(this).parent().parent();
				var uuid = row.attr("uuid");
				jQuery.ajax({
					url:"deleteConfiguration.form",
					type: "GET",
					dataType:"json",
					data:{
						"uuid": uuid
					},
					success:function (data) {
						if (data)
							row.hide();
					}
				})
			}
		});
	});
</script>

<style>
	#configurationList {
		border: 1px solid black;
		clear: both;
		margin: 5px auto;
		width: 100%;
		padding: 3px 3px;
	}

	#configurationList thead th {
		background-color: #FFFFFF;
		border-bottom: 1px solid black;
		font-weight: bold;
	}

	#configurationList tr.even {
		background-color: #E2E4FF;
	}

	#configurationList tr.odd {
		background-color: #D3D6FF;
	}
</style>

<div>
	<a href="conceptConfiguration.form"><spring:message code="odkconnector.conceptConfiguration.create" /></a>
	<table id="configurationList">
		<thead>
		<tr>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<th><spring:message code="odkconnector.conceptConfiguration.id" /></th>
			<th><spring:message code="odkconnector.conceptConfiguration.name" /></th>
			<th><spring:message code="odkconnector.conceptConfiguration.description" /></th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${configurations}" var="configuration" varStatus="counterRow">
			<c:choose>
				<c:when test="${counterRow.count % 2 == 0}">
					<tr class="even" uuid="${configuration.uuid}">
						<td><a href="manageConcept.form?uuid=${configuration.uuid}">Edit Configuration</a></td>
						<td><a href="#" class="delete">Delete Configuration</a></td>
						<td>${configuration.id}</td>
						<td>${configuration.name}</td>
						<td>${configuration.description}</td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr class="odd" uuid="${configuration.uuid}">
						<td><a href="manageConcept.form?uuid=${configuration.uuid}">Edit Configuration</a></td>
						<td><a href="#" class="delete">Delete Configuration</a></td>
						<td>${configuration.id}</td>
						<td>${configuration.name}</td>
						<td>${configuration.description}</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		</tbody>
	</table>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>
