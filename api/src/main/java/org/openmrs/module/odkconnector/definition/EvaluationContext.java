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

package org.openmrs.module.odkconnector.definition;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.module.odkconnector.definition.data.EvaluatedEncounter;
import org.openmrs.module.odkconnector.definition.data.EvaluatedObservation;

public class EvaluationContext {

	private static final Log log = LogFactory.getLog(EvaluationContext.class);

	private Cohort cohort;

	private List<EvaluatedEncounter> evaluatedEncounters;

	private List<EvaluatedObservation> evaluatedObservations;

	/**
	 * Get the evaluated cohort
	 *
	 * @return the evaluated cohort
	 */
	public Cohort getCohort() {
		return cohort;
	}

	/**
	 * Set the evaluated cohort
	 *
	 * @param cohort the evaluated cohort
	 */
	public void setCohort(final Cohort cohort) {
		this.cohort = cohort;
	}

	/**
	 * Get the evaluated encounters
	 *
	 * @return the evaluated encounters
	 */
	public List<EvaluatedEncounter> getEvaluatedEncounters() {
		return evaluatedEncounters;
	}

	/**
	 * Set the evaluated encounters
	 *
	 * @param evaluatedEncounters the evaluated encounters
	 */
	public void setEvaluatedEncounters(final List<EvaluatedEncounter> evaluatedEncounters) {
		this.evaluatedEncounters = evaluatedEncounters;
	}

	/**
	 * Get the evaluated observations
	 *
	 * @return the evaluated observations
	 */
	public List<EvaluatedObservation> getEvaluatedObservations() {
		return evaluatedObservations;
	}

	/**
	 * Set the evaluated observations
	 *
	 * @param evaluatedObservations the evaluated observations
	 */
	public void setEvaluatedObservations(final List<EvaluatedObservation> evaluatedObservations) {
		this.evaluatedObservations = evaluatedObservations;
	}
}
