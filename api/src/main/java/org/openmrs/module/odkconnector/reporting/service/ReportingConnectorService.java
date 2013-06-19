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

package org.openmrs.module.odkconnector.reporting.service;

import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.odkconnector.reporting.metadata.DefinitionProperty;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ReportingConnectorService extends OpenmrsService {

    /**
     * Service method to save or update a definition property
     *
     * @param definitionProperty the definition property
     * @return the saved definition property
     * @throws org.openmrs.api.APIException when saving the definition property failed
     */
    DefinitionProperty saveDefinitionProperty(final DefinitionProperty definitionProperty) throws APIException;

    /**
     * Service method to get the definition property by the definition property id
     *
     * @param id the definition property id
     * @return the definition property or null
     * @throws org.openmrs.api.APIException when getting the definition property failed
     */
    DefinitionProperty getDefinitionProperty(final Integer id) throws APIException;

    /**
     * Service method to get the definition property by the definition property uuid
     *
     * @param uuid the definition property uuid
     * @return the definition property or null
     * @throws org.openmrs.api.APIException when getting the definition property failed
     */
    DefinitionProperty getDefinitionPropertyByUuid(final String uuid) throws APIException;

    List<DefinitionProperty> getDefinitionPropertiesByCohortDefinition(final CohortDefinition cohortDefinition) throws APIException;

    List<DefinitionProperty> getAllDefinitionProperties() throws APIException;
}
