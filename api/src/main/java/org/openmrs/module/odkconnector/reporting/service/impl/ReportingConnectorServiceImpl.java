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

package org.openmrs.module.odkconnector.reporting.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.odkconnector.reporting.db.ReportingConnectorDAO;
import org.openmrs.module.odkconnector.reporting.metadata.DefinitionProperty;
import org.openmrs.module.odkconnector.reporting.service.ReportingConnectorService;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;

import java.util.List;

public class ReportingConnectorServiceImpl extends BaseOpenmrsService implements ReportingConnectorService {

    private static final Log log = LogFactory.getLog(ReportingConnectorServiceImpl.class);

    private ReportingConnectorDAO dao;

    /**
     * @param dao the dao to set
     */
    public void setDao(final ReportingConnectorDAO dao) {
        this.dao = dao;
    }

    /**
     * @return the dao
     */
    public ReportingConnectorDAO getDao() {
        return dao;
    }

    /**
     * Service method to save or update a definition property
     *
     * @param definitionProperty the definition property
     * @return the saved definition property
     * @throws org.openmrs.api.APIException when saving the definition property failed
     */
    @Override
    public DefinitionProperty saveDefinitionProperty(final DefinitionProperty definitionProperty) throws APIException {
        return dao.saveDefinitionProperty(definitionProperty);
    }

    /**
     * Service method to get the definition property by the definition property id
     *
     * @param id the definition property id
     * @return the definition property or null
     * @throws org.openmrs.api.APIException when getting the definition property failed
     */
    @Override
    public DefinitionProperty getDefinitionProperty(final Integer id) throws APIException {
        return dao.getDefinitionProperty(id);
    }

    /**
     * Service method to get the definition property by the definition property uuid
     *
     * @param uuid the definition property uuid
     * @return the definition property or null
     * @throws org.openmrs.api.APIException when getting the definition property failed
     */
    @Override
    public DefinitionProperty getDefinitionPropertyByUuid(final String uuid) throws APIException {
        return dao.getDefinitionPropertyByUuid(uuid);
    }

    @Override
    public List<DefinitionProperty> getDefinitionPropertiesByCohortDefinition(final CohortDefinition cohortDefinition)
            throws APIException {
        return dao.getDefinitionPropertiesByCohortDefinition(cohortDefinition);
    }

    @Override
    public List<DefinitionProperty> getAllDefinitionProperties() throws APIException {
        return dao.getAllDefinitionProperties();
    }
}
