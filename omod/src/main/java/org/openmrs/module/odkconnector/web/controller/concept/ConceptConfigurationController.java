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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.odkconnector.api.ConceptConfiguration;
import org.openmrs.module.odkconnector.api.service.ConnectorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ConceptConfigurationController {

	private static final Log log = LogFactory.getLog(ConceptConfigurationController.class);

	@RequestMapping(value = "/module/odkconnector/concept/conceptConfiguration", method = RequestMethod.GET)
	public void preparePage(final @RequestParam(value = "uuid", required = false) String uuid,
	                        final Model model) {
		ConnectorService service = Context.getService(ConnectorService.class);
		ConceptConfiguration conceptConfiguration = service.getConceptConfigurationByUuid(uuid);
		model.addAttribute("configuration", conceptConfiguration);
	}

	@RequestMapping(value = "/module/odkconnector/concept/conceptConfiguration", method = RequestMethod.POST)
	public String process(final @RequestParam(value = "name", required = true) String name,
	                      final @RequestParam(value = "description", required = true) String description,
	                      final @RequestParam(value = "configurationUuid", required = false) String configurationUuid) {
		ConnectorService service = Context.getService(ConnectorService.class);

		ConceptConfiguration conceptConfiguration = service.getConceptConfigurationByUuid(configurationUuid);
		if (conceptConfiguration == null)
			conceptConfiguration = new ConceptConfiguration();
		conceptConfiguration.setName(name);
		conceptConfiguration.setDescription(description);

		service.saveConceptConfiguration(conceptConfiguration);

		return "redirect:manageConcept.form?uuid=" + conceptConfiguration.getUuid();
	}

	@RequestMapping(value = "/module/odkconnector/concept/deleteConfiguration", method = RequestMethod.GET)
	public
	@ResponseBody
	Boolean delete(final @RequestParam(value = "uuid", required = false) String uuid) {
		Boolean deleted = Boolean.FALSE;
		ConnectorService service = Context.getService(ConnectorService.class);
		ConceptConfiguration conceptConfiguration = service.getConceptConfigurationByUuid(uuid);
		if (conceptConfiguration != null) {
			conceptConfiguration.setRetired(Boolean.TRUE);
			service.saveConceptConfiguration(conceptConfiguration);
			deleted = Boolean.TRUE;
		}
		return deleted;
	}
}
