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

package org.openmrs.module.odkconnector.clinic;

import java.io.InputStream;
import java.io.OutputStream;

public interface Serializable {

	/**
	 * Method to read the object
	 *
	 * @param stream the input stream
	 */
	void read(InputStream stream);

	/**
	 * Method to write the object to the output stream
	 *
	 * @param stream the output stream
	 */
	void write(OutputStream stream);
}
