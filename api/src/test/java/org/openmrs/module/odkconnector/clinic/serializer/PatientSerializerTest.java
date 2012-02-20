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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.openmrs.Cohort;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.odkconnector.clinic.Serializer;
import org.openmrs.module.odkconnector.clinic.data.PatientData;
import org.openmrs.module.odkconnector.clinic.data.PatientForm;
import org.openmrs.module.odkconnector.clinic.data.PatientObs;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.TestUtil;
import org.openmrs.util.OpenmrsUtil;

/**
 * Test {@link PatientSerializer}
 */
public class PatientSerializerTest extends BaseModuleContextSensitiveTest {

	private static final Log log = LogFactory.getLog(PatientSerializerTest.class);

	@Test
	public void serialize_shouldSerializePatientInformation() throws Exception {
		TestUtil.printOutTableContents(getConnection(), "person");
		TestUtil.printOutTableContents(getConnection(), "patient");
		TestUtil.printOutTableContents(getConnection(), "patient_identifier");
		TestUtil.printOutTableContents(getConnection(), "obs");

		File file = File.createTempFile("Serialization", "Example");
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));

		log.info("Writing to: " + file.getAbsolutePath());

		Cohort cohort = new Cohort();
		cohort.addMember(6);
		cohort.addMember(7);

		List<PatientData> patientsData = new ArrayList<PatientData>();
		for (Integer patientId : cohort.getMemberIds()) {
			Patient patient = Context.getPatientService().getPatient(patientId);

			PatientData patientData = new PatientData();
			patientData.setPatient(patient);

			List<Obs> observations = Context.getObsService().getObservationsByPerson(patient);
			for (Obs observation : observations) {
				PatientObs patientObs = new PatientObs();
				patientObs.setConcept(observation.getConcept().getDisplayString());
				patientObs.setDatetime(observation.getObsDatetime());

				if (observation.getValueDatetime() != null) {
					patientObs.setType(PatientObs.TYPE_DATE);
					patientObs.setValue(observation.getValueDatetime());
				} else if (observation.getValueCoded() != null) {
					patientObs.setType(PatientObs.TYPE_INT);
					patientObs.setValue(observation.getValueCoded());
				} else if (observation.getValueNumeric() != null) {
					patientObs.setType(PatientObs.TYPE_DOUBLE);
					patientObs.setValue(observation.getValueNumeric());
				} else {
					patientObs.setType(PatientObs.TYPE_STRING);
					patientObs.setValue(observation.getValueAsString(Context.getLocale()));
				}

				patientData.addObservations(patientObs);
			}

			PatientForm patientForm = new PatientForm();
			patientForm.setFormId(256);
			patientData.addForm(patientForm);

			patientsData.add(patientData);
		}

		Serializer serializer = new PatientSerializer();
		serializer.serialize(outputStream, patientsData);

		outputStream.close();

		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		DataInputStream dataInputStream = new DataInputStream(inputStream);

		// total number of patients
		Integer patientCounter = dataInputStream.readInt();
		System.out.println("Patient Counter: " + patientCounter);
		for (int i = 0; i < patientCounter; i++) {
			System.out.println("=================Patient=====================");
			System.out.println("Patient Id: " + dataInputStream.readInt());
			System.out.println("Family Name: " + dataInputStream.readUTF());
			System.out.println("Middle Name: " + dataInputStream.readUTF());
			System.out.println("Last Name: " + dataInputStream.readUTF());
			System.out.println("Gender: " + dataInputStream.readUTF());
			System.out.println("Birth Date: " + dataInputStream.readLong());
			System.out.println("Identifier" + dataInputStream.readUTF());

			Integer obsCounter = dataInputStream.readInt();
			for (int j = 0; j < obsCounter; j++) {
				System.out.println("==================Observation=================");
				System.out.println("Concept Name: " + dataInputStream.readUTF());

				Integer type = dataInputStream.readInt();
				if (OpenmrsUtil.nullSafeEquals(type, PatientObs.TYPE_STRING))
					System.out.println("Value: " + dataInputStream.readUTF());
				else if (OpenmrsUtil.nullSafeEquals(type, PatientObs.TYPE_INT))
					System.out.println("Value: " + dataInputStream.readInt());
				else if (OpenmrsUtil.nullSafeEquals(type, PatientObs.TYPE_DOUBLE))
					System.out.println("Value: " + dataInputStream.readDouble());
				else if (OpenmrsUtil.nullSafeEquals(type, PatientObs.TYPE_DATE))
					System.out.println("Value: " + dataInputStream.readLong());
				System.out.println("Time: " + dataInputStream.readLong());
			}

			Integer formCounter = dataInputStream.readInt();
			for (int j = 0; j < formCounter; j++) {
				System.out.println("==================Form=================");
				System.out.println("Form Id: " + dataInputStream.readInt());
			}

			System.out.println();
		}

		inputStream.close();
	}

}
