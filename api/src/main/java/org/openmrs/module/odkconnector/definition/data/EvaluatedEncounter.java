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
import org.openmrs.Encounter;
import org.openmrs.EncounterType;

public class EvaluatedEncounter {

	private static final Log log = LogFactory.getLog(EvaluatedObservation.class);

	private EncounterType encounterType;

	private List<Encounter> encounters;

	/**
	 * Get the encounter type for the evaluation process
	 *
	 * @return the encounter type
	 */
	public EncounterType getEncounterType() {
		return encounterType;
	}

	/**
	 * Set the encounter type for the evaluation process
	 *
	 * @param encounterType the encounter type
	 */
	public void setEncounterType(final EncounterType encounterType) {
		this.encounterType = encounterType;
	}

	/**
	 * Get the encounters for the evaluation process
	 *
	 * @return the encounters
	 */
	public List<Encounter> getEncounters() {
		return encounters;
	}

	/**
	 * Set the encounters for the evaluation process
	 *
	 * @param encounters the encounters
	 */
	public void setEncounters(final List<Encounter> encounters) {
		this.encounters = encounters;
	}
}
