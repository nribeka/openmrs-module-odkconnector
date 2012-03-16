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

package org.openmrs.module.odkconnector.serialization.serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SerializedForm {

	private static final Log log = LogFactory.getLog(SerializedForm.class);

	private Integer patientId;

	private Integer formId;

	/**
	 * Create new patient form information
	 *
	 * @param patientId the patient id
	 * @param formId    the form id
	 */
	public SerializedForm(final Integer patientId, final Integer formId) {
		this.patientId = patientId;
		this.formId = formId;
	}

	/**
	 * Get the patient id for which the form is applicable to
	 *
	 * @return the patient id
	 */
	public Integer getPatientId() {
		return patientId;
	}

	/**
	 * Set the patient id for which the form is applicable to
	 *
	 * @param patientId the patient id
	 */
	public void setPatientId(final Integer patientId) {
		this.patientId = patientId;
	}

	/**
	 * Get the form id for which the patient id is applicable to
	 *
	 * @return the form id
	 */
	public Integer getFormId() {
		return formId;
	}

	/**
	 * Set the form id which the patient id is applicable to
	 *
	 * @param formId the form id
	 */
	public void setFormId(final Integer formId) {
		this.formId = formId;
	}
}
