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

package org.openmrs.module.odkconnector.api.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.odkconnector.api.ConceptConfiguration;
import org.openmrs.module.odkconnector.api.db.ConnectorDAO;
import org.openmrs.module.odkconnector.api.service.ConnectorService;

/**
 * It is a default implementation of {@link org.openmrs.module.odkconnector.api.service.ConnectorService}.
 */
public class ConnectorServiceImpl extends BaseOpenmrsService implements ConnectorService {

	protected final Log log = LogFactory.getLog(ConnectorServiceImpl.class);

	private ConnectorDAO dao;

	/**
	 * @param dao the dao to set
	 */
	public void setDao(final ConnectorDAO dao) {
		this.dao = dao;
	}

	/**
	 * @return the dao
	 */
	public ConnectorDAO getDao() {
		return dao;
	}

	/**
	 * Service method to get all patients inside the cohort
	 *
	 * @param cohort the cohort
	 * @return all patients in the cohort or empty list when no patient match the patient id in the cohort
	 * @throws org.openmrs.api.APIException when the process failed
	 */
	@Override
	public List<Patient> getCohortPatients(final Cohort cohort) throws APIException {
		if (cohort == null || CollectionUtils.isEmpty(cohort.getMemberIds()))
			return new ArrayList<Patient>();
		return dao.getCohortPatients(cohort);
	}

	/**
	 * Service method to get all observations for all patients in the cohort
	 *
	 *
	 * @param cohort   the cohort
	 * @param concepts the concepts
	 * @return all observations for patients in the cohort or empty list when no observations for the patient ids in the cohort exists
	 * @throws org.openmrs.api.APIException when the process failed
	 */
	@Override
	public List<Obs> getCohortObservations(final Cohort cohort, final Collection<Concept> concepts) throws APIException {
		if (cohort == null || CollectionUtils.isEmpty(cohort.getMemberIds()) || CollectionUtils.isEmpty(concepts))
			return new ArrayList<Obs>();
		return dao.getCohortObservations(cohort, concepts);
	}

	/**
	 * Service method to save the concept configuration to the database
	 *
	 * @param conceptConfiguration the concept configuration
	 * @return saved concept configuration
	 * @throws org.openmrs.api.APIException when saving failed
	 */
	@Override
	public ConceptConfiguration saveConceptConfiguration(final ConceptConfiguration conceptConfiguration) throws APIException {
		return dao.saveConceptConfiguration(conceptConfiguration);
	}

	/**
	 * Get concept configuration based on the configuration id
	 *
	 * @param id the concept configuration id
	 * @return the matching concept configuration or null if no matching record found in the database
	 * @throws org.openmrs.api.APIException when fetching failed
	 */
	@Override
	public ConceptConfiguration getConceptConfiguration(final Integer id) throws APIException {
		return dao.getConceptConfiguration(id);
	}

	/**
	 * Get concept configuration based on the configuration uuid
	 *
	 * @param uuid the concept configuration id
	 * @return the matching concept configuration or null if no matching record found in the database
	 * @throws org.openmrs.api.APIException when fetching failed
	 */
	@Override
	public ConceptConfiguration getConceptConfigurationByUuid(final String uuid) throws APIException {
		return dao.getConceptConfigurationByUuid(uuid);
	}

	/**
	 * Get all saved concept configuration
	 *
	 * @return all saved concept configuration or empty list when there's no saved concept configuration
	 * @throws org.openmrs.api.APIException when fetching failed
	 */
	@Override
	public List<ConceptConfiguration> getConceptConfigurations() throws APIException {
		return dao.getConceptConfigurations();
	}
}
