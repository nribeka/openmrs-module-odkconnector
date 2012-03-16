<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<openmrs:htmlInclude file="/moduleResources/odkconnector/jquery.jeditable.mini.js"/>

<openmrs:require privilege="Manage Connector" otherwise="/login.htm" redirect="/module/odkconnector/reporting/manageProperty.form"/>

<%@ include file="../template/localHeader.jsp" %>

<script type="text/javascript">

	var $j = jQuery.noConflict();

	function createEditableDiv(propertyId, property, text) {
		var div = $j(document.createElement("div"));
		div.attr({
			class:"edit",
			property:property,
			propertyId:propertyId
		});
		div.html(text);
		return div;
	}

	function createElement(name) {
		return $j(document.createElement(name));
	}

	function search(uuid) {
		jQuery.ajax({
			url:"searchProperty.form",
			type:"POST",
			dataType:"json",
			data:{
				uuid:uuid
			},
			success:function (data, status, jqXHR) {
				$j("#properties").children().remove();
				jQuery.each(data, function (index, element) {
					var property = createElement("td");
					var propertyDiv = createEditableDiv(element.propertyId, "property", element.property);
					property.append(propertyDiv);

					var value = createElement("td");
					var valueDiv = createEditableDiv(element.propertyId, "propertyValue", element.propertyValue);
					value.append(valueDiv);

					var description = createElement("td");
					var descriptionDiv = createEditableDiv(element.propertyId, "propertyDescription", element.propertyDescription);
					description.append(descriptionDiv);

					var row = createElement("tr");
					row.append(property);
					row.append(value);
					row.append(description);

					$j("#properties").append(row);
				});
			},
			error:function (jqXHR, status, error) {
				$j("#properties").children().remove();
			}
		});
	}

	$j(document).ready(function () {

		$j("#definitionList").change(function () {
			search($j(this).val());
		});

		$j("#definitionList").ajaxComplete(function () {
			$j('.edit').editable(
					function (value, settings) {
						var property = $j(this).attr("property");
						var propertyId = $j(this).attr("propertyId");
						jQuery.ajax({
							url:"saveProperty.form",
							type:"POST",
							dataType:"json",
							data:{
								id:propertyId,
								property:property,
								value:value
							}
						});
						return value;
					},
					{
						indicator:'Saving...',
						tooltip:'Click to edit...'
					});

		});

		$j("#saveProperty").click(function () {
			var uuid = $j("#definitionList").val();
			var request = $j("#newProperty").serialize();
			jQuery.ajax({
				url:"newProperty.form",
				type:"POST",
				data:request + "&uuid=" + uuid,
				complete:function () {
					$j("#definitionList").trigger("change");
					$j("#newPropertyRow").hide();
					$j("#addPropertyRow").show();
				}
			});
		});

		$j("#addPropertyRow").click(function () {
			$j("#newPropertyRow").show();
			$j(this).hide();
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
			<legend><span><spring:message code="odkconnector.cohortDefinition.header"/></span></legend>
			<ol>
				<li>
					<label for="definitionList"><spring:message code="odkconnector.cohortDefinition.definitionList"/></label>
					<select id="definitionList" class="largeWidth">
						<option value="-1"></option>
						<c:forEach items="${cohortDefinitions}" var="cohortDefinition">
							<option value="${cohortDefinition.uuid}">${cohortDefinition.name}</option>
						</c:forEach>
					</select>
				</li>
			</ol>
		</fieldset>
	</form>

	<fieldset>
		<legend><span><spring:message code="odkconnector.cohortDefinition.property.header"/></span></legend>
		<table cellpadding="5px">
			<thead>
			<tr>
				<th class="header"><span><spring:message code="odkconnector.cohortDefinition.property"/></span></th>
				<th class="header"><span><spring:message code="odkconnector.cohortDefinition.propertyValue"/></span></th>
				<th class="header"><span><spring:message code="odkconnector.cohortDefinition.propertyDescription"/></span></th>
			</tr>
			</thead>
			<tbody id="properties"></tbody>
			<tfoot>
			<tr id="newPropertyRow" style='display: none;'>
				<form id="newProperty" method="post" action="">
					<td><input type="text" name="property"/></td>
					<td><input type="text" name="propertyValue"/></td>
					<td><input type="text" name="propertyDescription"/></td>
					<td><input type="button" id="saveProperty" value="<spring:message code='odkconnector.cohortDefinition.saveProperty' />"/></td>
				</form>
			</tr>
			<tr id="addPropertyRow">
				<td><input type="button" id="addNew" value="<spring:message code='odkconnector.cohortDefinition.newProperty' />"/></td>
			</tr>
			</tfoot>
		</table>
	</fieldset>
</div>


<%@ include file="/WEB-INF/template/footer.jsp" %>
