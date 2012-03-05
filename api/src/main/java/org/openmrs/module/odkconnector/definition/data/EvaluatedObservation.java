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

package org.openmrs.module.odkconnector.definition.data;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Obs;

public class EvaluatedObservation {

	private static final Log log = LogFactory.getLog(EvaluatedObservation.class);

	private Concept concept;

	private List<Obs> observations;

	/**
	 * Get the concept for the evaluation process
	 *
	 * @return the concept
	 */
	public Concept getConcept() {
		return concept;
	}

	/**
	 * Set the concept for the evaluation process
	 *
	 * @param concept the concept
	 */
	public void setConcept(final Concept concept) {
		this.concept = concept;
	}

	/**
	 * Get the observations for the evaluation process
	 *
	 * @return the observations
	 */
	public List<Obs> getObservations() {
		return observations;
	}

	/**
	 * Set the observations for the evaluation process
	 *
	 * @param observations the observations
	 */
	public void setObservations(final List<Obs> observations) {
		this.observations = observations;
	}
}
