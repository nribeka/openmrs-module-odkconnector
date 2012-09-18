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

package org.openmrs.module.odkconnector.web.controller.concept;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 */
@Controller
@RequestMapping("/module/odkconnector/search/concept")
public class AutocompleteConceptController {

    /**
     * Logger for this class and subclasses
     */
    private static final Log log = LogFactory.getLog(AutocompleteConceptController.class);

    @RequestMapping(method = RequestMethod.GET)
    public void search(final @RequestParam(required = true, value = "searchTerm") String searchTerm,
                       final HttpServletResponse response) throws Exception {
        List<Concept> concepts = Context.getConceptService().getConceptsByName(searchTerm);
        OutputStream stream = response.getOutputStream();

        JsonFactory f = new JsonFactory();
        JsonGenerator g = f.createJsonGenerator(stream, JsonEncoding.UTF8);
        g.useDefaultPrettyPrinter();

        g.writeStartObject();
        g.writeArrayFieldStart("elements");

        for (Concept concept : concepts) {
            g.writeStartObject();
            g.writeStringField("uuid", concept.getUuid());
            g.writeStringField("name", concept.getDisplayString());
            g.writeEndObject();
        }
        g.writeEndArray();
        g.writeEndObject();

        g.close();
    }
}
