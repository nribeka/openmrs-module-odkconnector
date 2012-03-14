/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.odkconnector.web.controller.reporting;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.openmrs.api.context.Context;
import org.openmrs.module.odkconnector.api.ConnectorService;
import org.openmrs.module.odkconnector.reporting.metadata.DefinitionProperty;
import org.openmrs.module.odkconnector.reporting.metadata.ExtendedDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AjaxPropertyController {

	private static final Log log = LogFactory.getLog(AjaxPropertyController.class);

	@RequestMapping(value = "/module/odkconnector/reporting/saveProperty.form", method = RequestMethod.POST)
	public void processSave(final @RequestParam(value = "id", required = true) String id,
	                        final @RequestParam(value = "value", required = true) String value,
	                        final HttpServletResponse response) throws IOException {

		System.out.println("id: " + id + ", value: " + value);
		OutputStream stream = response.getOutputStream();

		JsonFactory f = new JsonFactory();
		JsonGenerator g = f.createJsonGenerator(stream, JsonEncoding.UTF8);
		g.useDefaultPrettyPrinter();
		g.writeStartObject();
		g.writeStringField("id", id);
		g.writeStringField("value", value);
		g.writeEndObject();
		g.close();
	}

	@RequestMapping(value = "/module/odkconnector/reporting/searchProperty.form", method = RequestMethod.POST)
	public void processSearch(final @RequestParam(value = "uuid", required = true) String uuid,
	                          final HttpServletResponse response) throws IOException {
		CohortDefinitionService definitionService = Context.getService(CohortDefinitionService.class);
		ConnectorService connectorService = Context.getService(ConnectorService.class);

		CohortDefinition definition = definitionService.getDefinitionByUuid(uuid);
		ExtendedDefinition extendedDefinition = connectorService.getExtendedDefinitionByDefinition(definition);

		OutputStream stream = response.getOutputStream();

		JsonFactory f = new JsonFactory();
		JsonGenerator g = f.createJsonGenerator(stream, JsonEncoding.UTF8);
		g.useDefaultPrettyPrinter();

		g.writeStartArray();
		for (DefinitionProperty property : extendedDefinition.getProperties()) {
			g.writeStartObject();
			g.writeStringField("property", property.getProperty());
			g.writeStringField("propertyValue", property.getPropertyValue());
			g.writeStringField("propertyDescription", property.getPropertyDescription());
			g.writeEndObject();
		}
		g.writeEndArray();

		g.close();
	}

}
