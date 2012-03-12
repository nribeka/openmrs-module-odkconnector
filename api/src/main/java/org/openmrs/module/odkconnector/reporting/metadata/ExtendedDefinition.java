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

package org.openmrs.module.odkconnector.reporting.metadata;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;

public class ExtendedDefinition extends BaseOpenmrsMetadata {

	private static final Log log = LogFactory.getLog(ExtendedDefinition.class);

	private Integer id;

	private CohortDefinition definition;

	private Set<DefinitionProperty> properties;

	/**
	 * @return id - The unique Identifier for the object
	 */
	@Override
	public Integer getId() {
		return id;
	}

	/**
	 * @param id - The unique Identifier for the object
	 */
	@Override
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * Get the cohort definition object
	 *
	 * @return the cohort definition object
	 */
	public CohortDefinition getDefinition() {
		return definition;
	}

	/**
	 * Set the cohort definition object
	 *
	 * @param definition the cohort definition object
	 */
	public void setDefinition(final CohortDefinition definition) {
		this.definition = definition;
	}

	/**
	 * Get the properties for the cohort definition
	 *
	 * @return the properties
	 */
	public Set<DefinitionProperty> getProperties() {
		return properties;
	}

	/**
	 * Set the properties for the cohort definition
	 *
	 * @param properties the properties
	 */
	public void setProperties(final Set<DefinitionProperty> properties) {
		this.properties = properties;
	}
}
