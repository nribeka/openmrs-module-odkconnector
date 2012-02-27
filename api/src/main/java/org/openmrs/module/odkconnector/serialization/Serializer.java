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

package org.openmrs.module.odkconnector.serialization;

import java.io.IOException;
import java.io.OutputStream;

public interface Serializer {

	int ZERO = 0;

	/**
	 * Write the data to the output stream.
	 *
	 * @param stream the output stream
	 * @param data   the data that need to be written to the output stream
	 * @throws java.io.IOException thrown when the writing process encounter is failing
	 */
	void write(final OutputStream stream, final Object data) throws IOException;

}
