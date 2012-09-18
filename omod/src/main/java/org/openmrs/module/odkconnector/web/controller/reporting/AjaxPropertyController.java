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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.openmrs.api.context.Context;
import org.openmrs.module.odkconnector.reporting.metadata.DefinitionProperty;
import org.openmrs.module.odkconnector.reporting.metadata.ExtendedDefinition;
import org.openmrs.module.odkconnector.reporting.service.ReportingConnectorService;
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
    public void processSave(final @RequestParam(value = "propertyUuid", required = true) String propertyUuid,
                            final @RequestParam(value = "property", required = true) String property,
                            final @RequestParam(value = "value", required = true) String value) throws Exception {
        ReportingConnectorService reportingConnectorService = Context.getService(ReportingConnectorService.class);
        // search the definition property
        DefinitionProperty definitionProperty = reportingConnectorService.getDefinitionPropertyByUuid(propertyUuid);
        // update the definition property and then save it
        BeanUtils.setProperty(definitionProperty, property, value);
        reportingConnectorService.saveDefinitionProperty(definitionProperty);
    }

    @RequestMapping(value = "/module/odkconnector/reporting/deleteProperty.form", method = RequestMethod.POST)
    public void processDelete(final @RequestParam(value = "propertyUuid", required = true) String propertyUuid) throws Exception {
        ReportingConnectorService reportingConnectorService = Context.getService(ReportingConnectorService.class);
        DefinitionProperty definitionProperty = reportingConnectorService.getDefinitionPropertyByUuid(propertyUuid);
        definitionProperty.setRetired(Boolean.TRUE);
        reportingConnectorService.saveDefinitionProperty(definitionProperty);
    }

    @RequestMapping(value = "/module/odkconnector/reporting/newProperty.form", method = RequestMethod.POST)
    public void processNew(final @RequestParam(value = "uuid", required = true) String uuid,
                           final @RequestParam(value = "property", required = true) String property,
                           final @RequestParam(value = "propertyValue", required = true) String propertyValue,
                           final @RequestParam(value = "propertyDescription", required = true) String propertyDescription) throws Exception {

        CohortDefinitionService definitionService = Context.getService(CohortDefinitionService.class);
        ReportingConnectorService reportingConnectorService = Context.getService(ReportingConnectorService.class);
        // create the new definition property
        DefinitionProperty definitionProperty = new DefinitionProperty(property, propertyValue, propertyDescription);
        // check if the extended definition for this cohort definition already exist or not
        CohortDefinition definition = definitionService.getDefinitionByUuid(uuid);
        ExtendedDefinition extendedDefinition = reportingConnectorService.getExtendedDefinitionByDefinition(definition);
        if (extendedDefinition == null) {
            extendedDefinition = new ExtendedDefinition();
            extendedDefinition.setCohortDefinition(definition);
        }
        extendedDefinition.addDefinitionProperty(definitionProperty);
        reportingConnectorService.saveExtendedDefinition(extendedDefinition);
    }

    @RequestMapping(value = "/module/odkconnector/reporting/searchProperty.form", method = RequestMethod.POST)
    public void processSearch(final @RequestParam(value = "uuid", required = true) String uuid,
                              final HttpServletResponse response) throws IOException {
        CohortDefinitionService definitionService = Context.getService(CohortDefinitionService.class);
        ReportingConnectorService reportingConnectorService = Context.getService(ReportingConnectorService.class);

        CohortDefinition definition = definitionService.getDefinitionByUuid(uuid);
        ExtendedDefinition extendedDefinition = reportingConnectorService.getExtendedDefinitionByDefinition(definition);

        OutputStream stream = response.getOutputStream();

        JsonFactory f = new JsonFactory();
        JsonGenerator g = f.createJsonGenerator(stream, JsonEncoding.UTF8);
        g.useDefaultPrettyPrinter();

        g.writeStartArray();
        if (extendedDefinition != null)
            for (DefinitionProperty property : extendedDefinition.getProperties()) {
                if (property.isRetired())
                    continue;

                g.writeStartObject();
                g.writeNumberField("propertyId", property.getId());
                g.writeStringField("property", property.getProperty());
                g.writeStringField("propertyValue", property.getPropertyValue());
                g.writeStringField("propertyDescription", property.getPropertyDescription());
                g.writeStringField("propertyUuid", property.getUuid());
                g.writeEndObject();
            }
        g.writeEndArray();

        g.close();
    }

}
