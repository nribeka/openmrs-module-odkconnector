<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>

<%@ include file="template/localHeader.jsp" %>

<script type="text/javascript">

	var $j = jQuery.noConflict();

	function split(val) {
		if (jQuery.trim(val) === "")
			return new Array();
		return val.split(/,\s*/);
	}

	function extractLast(term) {
		return split(term).pop();
	}

	function valueExists(id, idList) {
		var ids = split(idList);
		for (i = 0; i < ids.length; i++)
			if (ids[i] == id)
				return true;
		return false;
	}

	$j(document).ready(function () {
		$j(".search").autocomplete({
			source:function (request, response) {
				jQuery.ajax({
					url:"${pageContext.request.contextPath}/module/odkconnector/search/concept.form",
					dataType:"json",
					data:{
						searchTerm:extractLast(request.term)
					},
					success:function (data) {
						jQuery.map(data.elements, function (value, index) {
							console.log("Value: " + value);
							console.log("Index: " + index);
						});
						response(jQuery.map(data.elements, function (key, item) {
							console.log("Inside Response: " + data);
							console.log("Key: " + key);
							console.log("Value: " + item);
							return {
								label:item, value:key
							}
						}));
					}
				});
			},
			minLength:0,
			focus:function () {
				// prevent value inserted on focus
				return false;
			},
			select:function (event, ui) {
				var name = ui.label
				var id = ui.value;

				var list = document.getElementById("conceptList");
				var idList = document.getElementById("conceptIds");

				if (!valueExists(id, idList.value)) {

					var option = new Option(name, id);
					option.selected = true;
					list.options[list.options.length] = option;

					var terms = split(idList.value);
					terms[terms.length] = id;
					idList.value = terms.join(",");
				}
				// clear out the field
				this.value = "";
				return false;
			}
		});
	})
</script>

<div>
	<form method="post">
		<fieldset>
			<ol>
				<li>
					<label for="conceptList"><spring:message code="odkconnector.manage.conceptList"/></label>
					<select id="conceptList" class="largeWidth" size="6" multiple="multiple">
						<c:forEach items="${concepts}" var="concept">
							<option value="${concept.conceptId}">${concept.name}</option>
						</c:forEach>
					</select>
				</li>
				<li>
					<input type="hidden" id="conceptIds" name="conceptIds" value="${conceptIds}"/>
					<input type="text" class="search" searchType="concept" size="43"/>
				</li>
			</ol>
		</fieldset>
	</form>
</div>


<%@ include file="/WEB-INF/template/footer.jsp" %>
