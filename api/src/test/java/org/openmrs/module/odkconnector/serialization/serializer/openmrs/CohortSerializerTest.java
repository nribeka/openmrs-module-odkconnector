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
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.openmrs.Cohort;
import org.openmrs.api.CohortService;
import org.openmrs.api.context.Context;
import org.openmrs.module.odkconnector.serialization.Serializer;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.util.HandlerUtil;

public class CohortSerializerTest extends BaseModuleContextSensitiveTest {

	private static final Log log = LogFactory.getLog(CohortSerializerTest.class);

	@Test
	public void serialize_shouldSerializeCohortInformation() throws Exception {

		CohortService cohortService = Context.getCohortService();

		Cohort firstCohort = new Cohort();
		firstCohort.addMember(6);
		firstCohort.addMember(7);
		firstCohort.addMember(8);
		firstCohort.setName("First Cohort");
		firstCohort.setDescription("First cohort for testing the serializer");
		cohortService.saveCohort(firstCohort);

		Cohort secondCohort = new Cohort();
		secondCohort.addMember(6);
		secondCohort.addMember(7);
		secondCohort.addMember(8);
		secondCohort.setName("Second Cohort");
		secondCohort.setDescription("Second cohort for testing the serializer");
		cohortService.saveCohort(secondCohort);

		File file = File.createTempFile("CohortSerialization", "Example");
		GZIPOutputStream outputStream = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

		List<Cohort> cohorts = cohortService.getAllCohorts();
		Serializer serializer = HandlerUtil.getPreferredHandler(Serializer.class, List.class);
		serializer.write(outputStream, cohorts);

		outputStream.close();

		GZIPInputStream inputStream = new GZIPInputStream(new BufferedInputStream(new FileInputStream(file)));
		DataInputStream dataInputStream = new DataInputStream(inputStream);

		Integer cohortCounts = dataInputStream.readInt();
		System.out.println("Number of cohorts: " + cohortCounts);

		for (int i = 0; i < cohortCounts; i++) {
			System.out.println("Cohort ID: " + dataInputStream.readInt());
			System.out.println("Cohort Name: " + dataInputStream.readUTF());
		}

		inputStream.close();
	}

}
