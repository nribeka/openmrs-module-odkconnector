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

package org.openmrs.module.odkconnector.clinic.serializer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.odkconnector.clinic.Serializer;
import org.openmrs.module.odkconnector.clinic.data.PatientData;

public class PatientSerializer implements Serializer {

	private static final Log log = LogFactory.getLog(PatientSerializer.class);

	/**
	 * Write the data to the output stream.
	 *
	 * @param os   the output stream
	 * @param data the data that need to be written to the output stream
	 */
	@Override
	public void serialize(final OutputStream os, final Object data) throws IOException {

		DataOutputStream stream = new DataOutputStream(os);

		List patientsData = null;
		if (ClassUtils.isAssignable(data.getClass(), List.class))
			patientsData = (List) data;

		if (patientsData == null || CollectionUtils.isEmpty(patientsData))
			stream.writeInt(Serializer.ZERO);
		else {
			stream.writeInt(patientsData.size());
			for (Object patientData : patientsData) {
				if (ClassUtils.isAssignable(patientData.getClass(), PatientData.class))
					serialize(os, (PatientData) patientData);
			}
		}
	}

	private void serialize(final OutputStream os, final PatientData patientData) {
		try {
			DataOutputStream stream = new DataOutputStream(os);
			patientData.write(stream);
		} catch (IOException e) {
			log.error("Serializing patient data element failed", e);
		}
	}
}
