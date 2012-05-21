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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.module.odkconnector.serialization.Serializer;

@Handler(supports = Cohort.class, order = 50)
public class CohortSerializer implements Serializer {

	private static final Log log = LogFactory.getLog(CohortSerializer.class);

	/**
	 * Write the data to the output stream.
	 *
	 * @param stream the output stream
	 * @param data   the data that need to be written to the output stream
	 */
	@Override
	public void write(final OutputStream stream, final Object data) throws IOException {

		Cohort cohort = (Cohort) data;

		DataOutputStream outputStream = new DataOutputStream(stream);

		outputStream.writeInt(cohort.getCohortId());
		outputStream.writeUTF(cohort.getName());
        outputStream.flush();
	}
}
