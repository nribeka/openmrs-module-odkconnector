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

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.odkconnector.api.ConceptConfiguration;
import org.openmrs.module.odkconnector.api.ConfiguredConcept;
import org.openmrs.module.odkconnector.api.service.ConnectorService;
import org.openmrs.module.odkconnector.api.utils.ConnectorUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ManageConceptController {

    private static final Log log = LogFactory.getLog(ManageConceptController.class);

    @RequestMapping(value = "/module/odkconnector/concept/manageConcept", method = RequestMethod.GET)
    public void preparePage(final @RequestParam(value = "uuid", required = true) String uuid,
                            final Model model) {
        ConnectorService service = Context.getService(ConnectorService.class);
        ConceptConfiguration conceptConfiguration = service.getConceptConfigurationByUuid(uuid);
        Set<Concept> concepts = ConnectorUtils.getConcepts(conceptConfiguration.getConfiguredConcepts());
        Set<String> conceptUuids = ConnectorUtils.getConceptUuids(concepts);

        model.addAttribute("configuration", conceptConfiguration);
        model.addAttribute("concepts", concepts);
        model.addAttribute("conceptUuids", ConnectorUtils.convertString(conceptUuids));
    }

    @RequestMapping(value = "/module/odkconnector/concept/manageConcept", method = RequestMethod.POST)
    public void process(final @RequestParam(value = "conceptUuids", required = true) String conceptUuids,
                        final @RequestParam(value = "configurationUuid", required = true) String configurationUuid,
                        final Model model, final HttpServletRequest request) {

        ConnectorService service = Context.getService(ConnectorService.class);
        ConceptConfiguration conceptConfiguration = service.getConceptConfigurationByUuid(configurationUuid);

        // the uuids coming from the web page. might contains new uuid and will not contains retired uuid
        Set<String> createdConceptUuidValues = new LinkedHashSet<String>(Arrays.asList(StringUtils.split(StringUtils.defaultString(conceptUuids), ",")));
        // the saved uuids. might contains retired uuid and will not contains new uuid
        Set<String> savedConceptUuidValues = new LinkedHashSet<String>();
        for (ConfiguredConcept configuredConcept : conceptConfiguration.getConfiguredConcepts()) {
            if (!configuredConcept.isRetired()) {
                Concept concept = configuredConcept.getConcept();
                savedConceptUuidValues.add(concept.getUuid());
            }
        }

        Collection intersectedUuids = CollectionUtils.intersection(createdConceptUuidValues, savedConceptUuidValues);
        Collection retiredConceptUuids = CollectionUtils.subtract(savedConceptUuidValues, intersectedUuids);
        Collection createdConceptUuids = CollectionUtils.subtract(createdConceptUuidValues, intersectedUuids);

        for (ConfiguredConcept configuredConcept : conceptConfiguration.getConfiguredConcepts()) {
            Concept concept = configuredConcept.getConcept();
            if (retiredConceptUuids.contains(concept.getUuid())) {
                configuredConcept.setRetired(Boolean.TRUE);
                configuredConcept.setRetiredBy(Context.getAuthenticatedUser());
                configuredConcept.setDateRetired(new Date());
            }
        }

        for (Object conceptUuid : createdConceptUuids) {
            Concept concept = Context.getConceptService().getConceptByUuid(String.valueOf(conceptUuid));
            if (concept != null) {
                ConfiguredConcept configuredConcept = new ConfiguredConcept();
                configuredConcept.setConcept(concept);
                configuredConcept.setConceptConfiguration(conceptConfiguration);
                conceptConfiguration.addConfiguredConcept(configuredConcept);
            }
        }
        service.saveConceptConfiguration(conceptConfiguration);

        Set<Concept> concepts = ConnectorUtils.getConcepts(conceptConfiguration.getConfiguredConcepts());
        model.addAttribute("configuration", conceptConfiguration);
        model.addAttribute("concepts", concepts);
        model.addAttribute("conceptUuids", ConnectorUtils.convertString(ConnectorUtils.getConceptUuids(concepts)));
    }
}
