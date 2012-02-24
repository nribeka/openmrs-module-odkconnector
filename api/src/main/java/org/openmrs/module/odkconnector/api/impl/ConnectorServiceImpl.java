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

package org.openmrs.module.odkconnector.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.odkconnector.api.ConnectorService;
import org.openmrs.module.odkconnector.api.db.ConnectorDAO;
import org.openmrs.module.odkconnector.clinic.data.PatientObs;

/**
 * It is a default implementation of {@link org.openmrs.module.odkconnector.api.ConnectorService}.
 */
public class ConnectorServiceImpl extends BaseOpenmrsService implements ConnectorService {

	protected final Log log = LogFactory.getLog(ConnectorServiceImpl.class);

	private ConnectorDAO dao;

	/**
	 * @param dao the dao to set
	 */
	public void setDao(ConnectorDAO dao) {
		this.dao = dao;
	}

	/**
	 * @return the dao
	 */
	public ConnectorDAO getDao() {
		return dao;
	}


	/**
	 * Get all applicable patient observations based on the concept list information
	 *
	 * @param patient  the patient
	 * @param concepts the applicable concept list
	 * @return all observation for the patient based on the concept list
	 * @throws APIException when failed getting the observation information
	 */
	@Override
	public List<PatientObs> getPatientObservations(final Patient patient, final List<Concept> concepts) throws APIException {
		List<PatientObs> patientObservations = new ArrayList<PatientObs>();
		for (Concept concept : concepts) {
			List<Obs> observations = Context.getObsService().getObservationsByPersonAndConcept(patient, concept);
			for (Obs observation : observations) {
				PatientObs patientObservation = new PatientObs();
				patientObservation.setConcept(observation.getConcept().getDisplayString());
				patientObservation.setDatetime(observation.getObsDatetime());

				if (observation.getValueDatetime() != null) {
					patientObservation.setType(PatientObs.TYPE_DATE);
					patientObservation.setValue(observation.getValueDatetime());
				} else if (observation.getValueCoded() != null) {
					patientObservation.setType(PatientObs.TYPE_INT);
					patientObservation.setValue(observation.getValueCoded());
				} else if (observation.getValueNumeric() != null) {
					patientObservation.setType(PatientObs.TYPE_DOUBLE);
					patientObservation.setValue(observation.getValueNumeric());
				} else {
					patientObservation.setType(PatientObs.TYPE_STRING);
					patientObservation.setValue(observation.getValueAsString(Context.getLocale()));
				}

				patientObservations.add(patientObservation);
			}
		}

		return patientObservations;
	}
}
