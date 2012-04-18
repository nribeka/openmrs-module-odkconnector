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

package org.openmrs.module.odkconnector.api;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.odkconnector.api.service.ConnectorService;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 * Tests {@link ConnectorService}.
 */
public class ConnectorServiceTest extends BaseModuleContextSensitiveTest {

	private static final Log log = LogFactory.getLog(ConnectorServiceTest.class);

	@Test
	public void getConceptConfiguration_shouldSaveConceptConfigurationIntoTheDatabase() {
		ConceptConfiguration conceptConfiguration = new ConceptConfiguration();
		conceptConfiguration.setName("Concept Configuration");
		conceptConfiguration.setDescription("Concept Configuration Description");

		ConfiguredConcept configuredConcept = new ConfiguredConcept();
		configuredConcept.setConcept(Context.getConceptService().getConcept(20));
		configuredConcept.setConceptConfiguration(conceptConfiguration);

		conceptConfiguration.addConfiguredConcept(configuredConcept);

		Context.getService(ConnectorService.class).saveConceptConfiguration(conceptConfiguration);
		Assert.assertNotNull(conceptConfiguration.getId());
		Assert.assertEquals("Concept Configuration", conceptConfiguration.getName());
		Assert.assertEquals("Concept Configuration Description", conceptConfiguration.getDescription());
		Assert.assertEquals(1, conceptConfiguration.getConfiguredConcepts().size());
	}

	@Test
	public void getConceptConfiguration_shouldReturnConceptConfigurationGivenAnId() {
		ConceptConfiguration conceptConfiguration = new ConceptConfiguration();
		conceptConfiguration.setName("Concept Configuration");
		conceptConfiguration.setDescription("Concept Configuration Description");

		ConfiguredConcept configuredConcept = new ConfiguredConcept();
		configuredConcept.setConcept(Context.getConceptService().getConcept(20));
		configuredConcept.setConceptConfiguration(conceptConfiguration);

		conceptConfiguration.addConfiguredConcept(configuredConcept);

		Context.getService(ConnectorService.class).saveConceptConfiguration(conceptConfiguration);
		Assert.assertNotNull(conceptConfiguration.getId());
		Assert.assertEquals("Concept Configuration", conceptConfiguration.getName());
		Assert.assertEquals("Concept Configuration Description", conceptConfiguration.getDescription());
		Assert.assertEquals(1, conceptConfiguration.getConfiguredConcepts().size());

		Integer id = conceptConfiguration.getId();
		ConceptConfiguration savedConceptConfiguration = Context.getService(ConnectorService.class).getConceptConfiguration(id);
		Assert.assertNotNull(savedConceptConfiguration);
		Assert.assertEquals("Concept Configuration", conceptConfiguration.getName());
		Assert.assertEquals("Concept Configuration Description", conceptConfiguration.getDescription());
		Assert.assertEquals(1, conceptConfiguration.getConfiguredConcepts().size());
	}
}
