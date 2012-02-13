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

package org.openmrs.module.odkconnector.clinic.data;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.odkconnector.clinic.Serializable;

public class PatientData implements Serializable {

	private static final Log log = LogFactory.getLog(PatientData.class);

	/**
	 * Method to read the object
	 *
	 * @param stream the input stream
	 */
	@Override
	public void read(final InputStream stream) {
	}

	/**
	 * Method to write the object to the output stream
	 *
	 * @param stream the output stream
	 */
	@Override
	public void write(final OutputStream stream) {
	}
}
