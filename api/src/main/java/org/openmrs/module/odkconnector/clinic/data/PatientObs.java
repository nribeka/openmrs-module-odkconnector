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

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.odkconnector.clinic.Serializable;
import org.openmrs.util.OpenmrsUtil;

public class PatientObs implements Serializable {

	private static final Log log = LogFactory.getLog(PatientObs.class);

	public static final Integer TYPE_STRING = 1;

	public static final Integer TYPE_INT = 2;

	public static final Integer TYPE_DOUBLE = 3;

	public static final Integer TYPE_DATE = 4;

	private String concept;

	private Integer type;

	private Object value;

	private Date datetime;

	/**
	 * Get the current observation concept
	 *
	 * @return the concept name
	 */
	public String getConcept() {
		return concept;
	}

	/**
	 * Set the current observation concept
	 *
	 * @param concept the concept name
	 */
	public void setConcept(final String concept) {
		this.concept = concept;
	}

	/**
	 * Get the type of the observation data
	 *
	 * @return the type of the observation data
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * Set the type of the observation data
	 *
	 * @param type the type of the observation data
	 */
	public void setType(final Integer type) {
		this.type = type;
	}

	/**
	 * Get the observation value
	 *
	 * @return the value of the observation
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Set the value of the observation
	 *
	 * @param value the value of the observation
	 */
	public void setValue(final Object value) {
		this.value = value;
	}

	/**
	 * Get the datetime of the observation
	 *
	 * @return the datetime of the observation
	 */
	public Date getDatetime() {
		return datetime;
	}

	/**
	 * Set the datetime of the observation
	 *
	 * @param datetime the datetime of the observation
	 */
	public void setDatetime(final Date datetime) {
		this.datetime = datetime;
	}

	/**
	 * Method to write the object to the output stream
	 *
	 * @param stream the output stream
	 */
	@Override
	public void write(final DataOutputStream stream) throws IOException {
		stream.writeUTF(getConcept());
		stream.writeInt(getType());
		// write the actual value based on the type
		if (OpenmrsUtil.nullSafeEquals(getType(), TYPE_STRING))
			stream.writeUTF((String) getValue());
		else if (OpenmrsUtil.nullSafeEquals(getType(), TYPE_INT))
			stream.writeInt((Integer) getValue());
		else if (OpenmrsUtil.nullSafeEquals(getType(), TYPE_DOUBLE))
			stream.writeDouble((Double) getValue());
		else if (OpenmrsUtil.nullSafeEquals(getType(), TYPE_DATE))
			stream.writeLong(((Date) getValue()).getTime());
		// write the datetime of the observation
		stream.writeLong(getDatetime().getTime());
	}
}
