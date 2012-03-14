<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<openmrs:htmlInclude file="/moduleResources/odkconnector/jquery.jeditable.mini.js"/>

<openmrs:require privilege="Manage Connector" otherwise="/login.htm" redirect="/module/odkconnector/reporting/manageProperty.form"/>

<%@ include file="../template/localHeader.jsp" %>

<script type="text/javascript">

	var $j = jQuery.noConflict();

	$j(document).ready(function () {

		$j("#definitionList").change(
				function () {
					var selectedUuid = $j(this).val();
					jQuery.ajax({
						url:"searchProperty.form",
						type:"POST",
						dataType:"json",
						data:{
							uuid:selectedUuid
						},
						success:function (data, status, jqXHR) {
							jQuery.each(data, function (index, element) {
								console.log(element.property);
								console.log(element.propertyValue);
								console.log(element.propertyDescription);
							});
						},
						error:function (jqXHR, status, error) {
							console.log(status);
						}
					});
				}
		);

		$j('.edit').editable(
				function (value, settings) {
					jQuery.post("saveProperty.form", { id:"some id", value:value });
					console.log(this);
					console.log(value);
					console.log(settings);
					return value;
				},
				{
					indicator:'Saving...',
					tooltip:'Click to edit...',
					callback:function (value, settings) {
						console.log(this);
						console.log(value);
						console.log(settings);
					}
				});
	});

</script>

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

	.header {
		text-align: left;
	}
</style>

<c:if test="${message != null}">
	<div id="message"><spring:message code="${message}" text="${message}"/></div>
</c:if>

<div>
	<form action="" method="post">
		<fieldset>
			<legend>Select Cohort Definition</legend>
			<ol>
				<li>
					<label for="definitionList"><spring:message code="odkconnector.definition.definitionList"/></label>
					<select id="definitionList" class="largeWidth">
						<option value="-1"></option>
						<c:forEach items="${definitions}" var="definition">
							<option value="${definition.uuid}">${definition.name}</option>
						</c:forEach>
					</select>
				</li>
			</ol>
		</fieldset>
	</form>

	<fieldset>
		<legend>Cohort Definition Extended Property</legend>
		<table width="80%" cellpadding="5px">
			<tr>
				<th class="header"><span><spring:message code="odkconnector.definition.property"/></span></th>
				<th class="header"><span><spring:message code="odkconnector.definition.propertyValue"/></span></th>
				<th class="header"><span><spring:message code="odkconnector.definition.propertyDescription"/></span></th>
			</tr>
			<tr>
				<td>
					<div class="edit" name="property"></div>
				</td>
				<td>
					<div class="edit" name="value"></div>
				</td>
				<td>
					<div class="edit" name="description"></div>
				</td>
			</tr>
		</table>
	</fieldset>
</div>


<%@ include file="/WEB-INF/template/footer.jsp" %>
