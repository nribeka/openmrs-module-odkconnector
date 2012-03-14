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

import java.util.List;

import org.openmrs.Cohort;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.odkconnector.reporting.metadata.ExtendedDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;

/**
 * Database methods for {@link org.openmrs.module.odkconnector.api.ConnectorService}.
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
	 * @param cohort   the cohort
	 * @param concepts the concepts
	 * @return all observations for patients in the cohort or empty list when no observations for the patient ids in the cohort exists
	 * @throws org.openmrs.api.db.DAOException
	 *          when the process failed
	 */
	List<Obs> getCohortObservations(final Cohort cohort, final List<Concept> concepts) throws DAOException;

	/**
	 * DAO method to save extended information for the reporting cohort definition
	 *
	 * @param extendedDefinition the extended definition
	 * @return saved extended definition
	 * @throws org.openmrs.api.db.DAOException
	 *          when the saving process failed
	 */
	ExtendedDefinition saveExtendedDefinition(final ExtendedDefinition extendedDefinition) throws DAOException;

	/**
	 * DAO method to get extended definition by the extended definition uuid
	 *
	 * @param uuid the extended definition uuid
	 * @return the extended definition
	 * @throws org.openmrs.api.db.DAOException
	 *          when retrieving the extended definition failed
	 */
	ExtendedDefinition getExtendedDefinitionByUuid(final String uuid) throws DAOException;

	/**
	 * DAO method to get extended definition by the definition
	 *
	 * @param definition the definition
	 * @return the extended definition
	 * @throws org.openmrs.api.db.DAOException
	 *          when retrieving the extended definition failed
	 */
	ExtendedDefinition getExtendedDefinitionByDefinition(final CohortDefinition definition) throws DAOException;

	/**
	 * DAO method to get extended definition by the extended definition id
	 *
	 * @param id the extended definition id
	 * @return the extended definition
	 * @throws org.openmrs.api.db.DAOException
	 *          when retrieving the extended definition failed
	 */
	ExtendedDefinition getExtendedDefinition(Integer id) throws DAOException;

	/**
	 * DAO method to get all saved extended definitions
	 *
	 * @return alll saved extended definitions
	 * @throws org.openmrs.api.db.DAOException
	 *          when retrieving all extended definitions failed
	 */
	List<ExtendedDefinition> getAllExtendedDefinition() throws DAOException;
}
