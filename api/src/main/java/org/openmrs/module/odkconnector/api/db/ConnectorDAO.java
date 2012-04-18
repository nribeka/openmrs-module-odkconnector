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

package org.openmrs.module.odkconnector.api.db;

import java.util.Collection;
import java.util.List;

import org.openmrs.Cohort;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.odkconnector.api.ConceptConfiguration;

/**
 * Database methods for {@link org.openmrs.module.odkconnector.api.service.ConnectorService}.
 */
public interface ConnectorDAO {

	/*
		 * Add DAO methods here
		 */

	/**
	 * DAO method to get all patients inside the cohort
	 *
	 * @param cohort the cohort
	 * @return all patients in the cohort or empty list when no patient match the patient id in the cohort
	 * @throws org.openmrs.api.db.DAOException
	 *          when the process failed
	 */
	List<Patient> getCohortPatients(final Cohort cohort) throws DAOException;

	/**
	 * DAO method to get all observations for all patients in the cohort
	 *
	 *
	 * @param cohort   the cohort
	 * @param concepts the concepts
	 * @return all observations for patients in the cohort or empty list when no observations for the patient ids in the cohort exists
	 * @throws org.openmrs.api.db.DAOException
	 *          when the process failed
	 */
	List<Obs> getCohortObservations(final Cohort cohort, final Collection<Concept> concepts) throws DAOException;

	/**
	 * DAO method to save the concept configuration to the database
	 *
	 * @param conceptConfiguration the concept configuration
	 * @return saved concept configuration
	 * @throws org.openmrs.api.db.DAOException
	 *          when saving failed
	 */
	ConceptConfiguration saveConceptConfiguration(final ConceptConfiguration conceptConfiguration) throws DAOException;

	/**
	 * Get concept configuration based on the configuration id
	 *
	 *
	 * @param id the concept configuration id
	 * @return the matching concept configuration or null if no matching record found in the database
	 * @throws org.openmrs.api.db.DAOException
	 *          when fetching failed
	 */
	ConceptConfiguration getConceptConfiguration(final Integer id) throws DAOException;

	/**
	 * Get concept configuration based on the configuration uuid
	 *
	 *
	 * @param uuid the concept configuration id
	 * @return the matching concept configuration or null if no matching record found in the database
	 * @throws org.openmrs.api.db.DAOException
	 *          when fetching failed
	 */
	ConceptConfiguration getConceptConfigurationByUuid(final String uuid) throws DAOException;

	/**
	 * Get all saved concept configuration
	 *
	 * @return all saved concept configuration or empty list when there's no saved concept configuration
	 * @throws org.openmrs.api.APIException when fetching failed
	 */
	List<ConceptConfiguration> getConceptConfigurations();
}
