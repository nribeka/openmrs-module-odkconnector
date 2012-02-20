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

package org.openmrs.module.odkconnector.clinic.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PersonName;
import org.openmrs.module.odkconnector.clinic.Serializable;
import org.openmrs.module.odkconnector.clinic.Serializer;

public class SerializationUtils {

	private static final Log log = LogFactory.getLog(SerializationUtils.class);

	/**
	 * Utility method to write list of serializable objects to the stream
	 *
	 * @param serializables the list of all serializable objects
	 * @param stream        the stream where the serializable objects should be written
	 * @throws IOException thrown when writing process fail
	 */
	public static void serialize(List<? extends Serializable> serializables, DataOutputStream stream) throws IOException {
		if (serializables != null) {
			stream.writeInt(serializables.size());
			for (Serializable serializable : serializables)
				serializable.write(stream);
		} else
			stream.writeInt(Serializer.ZERO);
	}

	/**
	 * Utility method to write a patient information to the stream
	 *
	 * @param patient the patient
	 * @param stream  the stream where the patient information should be written
	 * @throws IOException thrown when the writing process fail
	 */
	public static void serialize(Patient patient, DataOutputStream stream) throws IOException {
		if (patient == null || patient.getPersonName() == null || patient.getPatientIdentifier() == null)
			throw new IOException("Trying to serialize null patient information.");

		stream.writeInt(patient.getPatientId());

		PersonName personName = patient.getPersonName();
		stream.writeUTF(StringUtils.defaultString(personName.getFamilyName()));
		stream.writeUTF(StringUtils.defaultString(personName.getMiddleName()));
		stream.writeUTF(StringUtils.defaultString(personName.getGivenName()));

		stream.writeUTF(StringUtils.defaultString(patient.getGender()));

		Date birthDate = patient.getBirthdate();
		stream.writeLong(birthDate != null ? birthDate.getTime() : 0);

		PatientIdentifier patientIdentifier = patient.getPatientIdentifier();
		stream.writeUTF(StringUtils.defaultString(patientIdentifier.getIdentifier()));
	}

}
