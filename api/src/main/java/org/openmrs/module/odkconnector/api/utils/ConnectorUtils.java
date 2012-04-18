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

package org.openmrs.module.odkconnector.api.utils;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.OpenmrsObject;
import org.openmrs.module.odkconnector.api.ConfiguredConcept;

public class ConnectorUtils {

	private static final Log log = LogFactory.getLog(ConnectorUtils.class);

	public static Set<Concept> getConcepts(Collection<ConfiguredConcept> configuredConcepts) {
		Set<Concept> concepts = new LinkedHashSet<Concept>();
		for (ConfiguredConcept configuredConcept : configuredConcepts)
			concepts.add(configuredConcept.getConcept());
		return concepts;
	}

	public static Set<String> getInternalUuids(Collection<? extends OpenmrsObject> openmrsObjects) {
		Set<String> uuids = new LinkedHashSet<String>();
		for (OpenmrsObject openmrsObject : openmrsObjects)
			uuids.add(openmrsObject.getUuid());
		return uuids;
	}

	public static String convertString(Collection<String> strings) {
		StringBuilder builder = new StringBuilder();
		for (String string : strings)
			builder.append(string).append(",");
		return builder.toString();
	}

}
