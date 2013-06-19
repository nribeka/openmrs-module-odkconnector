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

package org.openmrs.module.odkconnector.reporting;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.odkconnector.reporting.metadata.DefinitionProperty;
import org.openmrs.module.odkconnector.reporting.service.ReportingConnectorService;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.EncounterCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class ReportingConnectorServiceTest extends BaseModuleContextSensitiveTest {

    private static final Log log = LogFactory.getLog(ReportingConnectorServiceTest.class);

    @Test
    public void saveExtendedDefinition_shouldSaveExtendedDefinitionIntoTheDatabase() throws Exception {

        CohortDefinitionService definitionService = Context.getService(CohortDefinitionService.class);

        CohortDefinition cohortDefinition = new EncounterCohortDefinition();
        cohortDefinition.setName("Cohort Definition");
        cohortDefinition.setDescription("Cohort Definition Description");
        CohortDefinition savedDefinition = definitionService.saveDefinition(cohortDefinition);
        Assert.assertNotNull(savedDefinition.getUuid());

        ReportingConnectorService reportingConnectorService = Context.getService(ReportingConnectorService.class);
        // create the new definition property

        String property = "example.property";
        String propertyValue = "Example Property";
        String propertyDescription = "Example Property Description";

        DefinitionProperty definitionProperty = new DefinitionProperty(property, propertyValue, propertyDescription);
        // check if the extended definition for this cohort definition already exist or not
        CohortDefinition definition = definitionService.getDefinitionByUuid(savedDefinition.getUuid());
        definitionProperty.setCohortDefinition(definition);
        reportingConnectorService.saveDefinitionProperty(definitionProperty);
    }

}
