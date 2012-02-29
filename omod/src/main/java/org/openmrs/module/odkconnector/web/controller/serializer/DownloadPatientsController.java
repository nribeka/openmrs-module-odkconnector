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

package org.openmrs.module.odkconnector.web.controller.serializer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.odkconnector.serialization.Processor;
import org.openmrs.module.odkconnector.serialization.processor.HttpProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/module/odkconnector/download/patients")
public class DownloadPatientsController {

	private static final Log log = LogFactory.getLog(DownloadPatientsController.class);

	@RequestMapping(method = RequestMethod.POST)
	public void process(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		Processor processor = new HttpProcessor(HttpProcessor.PROCESS_PATIENTS);
		processor.process(request.getInputStream(), response.getOutputStream());
	}

}
