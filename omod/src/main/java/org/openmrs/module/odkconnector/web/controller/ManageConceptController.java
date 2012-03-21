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

package org.openmrs.module.odkconnector.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.odkconnector.serialization.utils.SerializationConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/module/odkconnector/manageConcept")
public class ManageConceptController {

	private static final Log log = LogFactory.getLog(ManageConceptController.class);

	@RequestMapping(method = RequestMethod.GET)
	public void preparePage(final Model model) {
		AdministrationService service = Context.getAdministrationService();
		String conceptIds = service.getGlobalProperty(SerializationConstants.CLINIC_CONCEPTS);
		model.addAttribute("concepts", searchConcept(conceptIds));
		model.addAttribute("conceptIds", conceptIds);
	}

	@RequestMapping(method = RequestMethod.POST)
	public void process(final @RequestParam(value = "conceptIds", required = true) String conceptIds,
	                    final Model model) {

		GlobalProperty property = new GlobalProperty();
		property.setProperty(SerializationConstants.CLINIC_CONCEPTS);
		property.setDescription("Global property for concepts of observations which will be transferred to the ODK Clinic.");
		property.setPropertyValue(conceptIds);

		AdministrationService service = Context.getAdministrationService();
		service.saveGlobalProperty(property);

		model.addAttribute("message", "odkconnector.manage.conceptSaved");
		model.addAttribute("concepts", searchConcept(conceptIds));
		model.addAttribute("conceptIds", conceptIds);
	}

	private static List<Concept> searchConcept(String conceptIds) {
		List<Concept> concepts = new ArrayList<Concept>();
		for (String conceptId : StringUtils.split(StringUtils.defaultString(conceptIds), ",")) {
			Concept concept = Context.getConceptService().getConcept(conceptId);
			if (concept != null)
				concepts.add(concept);
		}
		return concepts;
	}

}
