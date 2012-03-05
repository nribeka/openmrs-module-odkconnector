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

package org.openmrs.module.odkconnector.definition;

public interface CohortDefinition {

	// create per patient evaluator
	// when we evaluate cohort, do a query to get the requirement for per patient evaluator (obs, encounter)
	// pass the correct obs and encounter to the evaluator, evaluate and send the output

	// every evaluator knows what it need (have property to say that they need obs x, or encounter y, or other form of data)

	// caching strategy across evaluation process

	// evaluate in chunks, don't evaluate 200,000 patients at the same time

	// this feels like rewriting the reporting framework
}
