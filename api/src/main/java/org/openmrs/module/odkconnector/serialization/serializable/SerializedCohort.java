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

package org.openmrs.module.odkconnector.serialization.serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SerializedCohort {

	private static final Log log = LogFactory.getLog(SerializedCohort.class);

	private Integer id;

	private String name;

	/**
	 * Get the serialized cohort id
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Set the serialized cohort id
	 *
	 * @param id the id
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * Get the serialized cohort name
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the serialized cohort name
	 *
	 * @param name the name
	 */
	public void setName(final String name) {
		this.name = name;
	}
}
