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

package org.openmrs.module.odkconnector.serialization.serializer.openmrs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.openmrs.Cohort;
import org.openmrs.Form;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.odkconnector.serialization.Serializer;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.util.HandlerUtil;

/**
 * Test {@link PatientSerializer}
 */
public class PatientSerializerTest extends BaseModuleContextSensitiveTest {

	private static final Log log = LogFactory.getLog(PatientSerializerTest.class);

	@Test
	public void serialize_shouldSerializePatientInformation() throws Exception {
		File file = File.createTempFile("PatientSerialization", "Example");
		GZIPOutputStream outputStream = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

		log.info("Writing to: " + file.getAbsolutePath());

		Cohort cohort = new Cohort();
		cohort.addMember(6);
		cohort.addMember(7);
		cohort.addMember(8);

		List<Patient> patients = new ArrayList<Patient>();
		List<Obs> observations = new ArrayList<Obs>();
		List<Form> forms = new ArrayList<Form>();

		for (Integer patientId : cohort.getMemberIds()) {
			Patient patient = Context.getPatientService().getPatient(patientId);
			observations.addAll(Context.getObsService().getObservationsByPerson(patient));
			patients.add(patient);
		}

		Serializer serializer = HandlerUtil.getPreferredHandler(Serializer.class, List.class);
		serializer.write(outputStream, patients);
		serializer.write(outputStream, observations);
		serializer.write(outputStream, forms);

		outputStream.close();

		GZIPInputStream inputStream = new GZIPInputStream(new BufferedInputStream(new FileInputStream(file)));
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
		}

		Integer obsCounter = dataInputStream.readInt();
		for (int j = 0; j < obsCounter; j++) {
			System.out.println("==================Observation=================");
			System.out.println("Patient Id: " + dataInputStream.readInt());
			System.out.println("Concept Name: " + dataInputStream.readUTF());

			byte type = dataInputStream.readByte();
			if (type == ObsSerializer.TYPE_STRING)
				System.out.println("Value: " + dataInputStream.readUTF());
			else if (type == ObsSerializer.TYPE_INT)
				System.out.println("Value: " + dataInputStream.readInt());
			else if (type == ObsSerializer.TYPE_DOUBLE)
				System.out.println("Value: " + dataInputStream.readDouble());
			else if (type == ObsSerializer.TYPE_DATE)
				System.out.println("Value: " + dataInputStream.readLong());
			System.out.println("Time: " + dataInputStream.readLong());
		}

		Integer formCounter = dataInputStream.readInt();
		for (int j = 0; j < formCounter; j++) {
			System.out.println("==================Form=================");
			System.out.println("Form Id: " + dataInputStream.readInt());
		}

		System.out.println();

		inputStream.close();
	}

}
