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

package org.openmrs.module.odkconnector.web.controller.reporting;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/module/odkconnector/reporting/saveProperty.form")
public class SavePropertyController {

	private static final Log log = LogFactory.getLog(SavePropertyController.class);

	@RequestMapping(method = RequestMethod.POST)
	public void process(final @RequestParam(value = "id", required = true) String id,
	                    final @RequestParam(value = "value", required = true) String value) {

		System.out.println("id: " + id + ", value: " + value);

	}

}
