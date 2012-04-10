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

package org.openmrs.module.odkconnector.serialization.processor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.odkconnector.api.service.ConnectorService;
import org.openmrs.module.odkconnector.reporting.metadata.DefinitionProperty;
import org.openmrs.module.odkconnector.reporting.metadata.ExtendedDefinition;
import org.openmrs.module.odkconnector.reporting.service.ReportingConnectorService;
import org.openmrs.module.odkconnector.serialization.Processor;
import org.openmrs.module.odkconnector.serialization.Serializer;
import org.openmrs.module.odkconnector.serialization.serializable.SerializedForm;
import org.openmrs.module.odkconnector.serialization.utils.SerializationConstants;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.util.HandlerUtil;

public class HttpProcessor implements Processor {

	private static final Log log = LogFactory.getLog(HttpProcessor.class);

	public static final String PROCESS_COHORT = "download.cohort";

	public static final String PROCESS_PATIENTS = "download.patients";

	private String action;

	public HttpProcessor(final String action) {
		this.action = action;
	}

	/**
	 * Get the http processor action
	 *
	 * @return the http processor action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Set the http processor action
	 *
	 * @param action the http processor action
	 */
	public void setAction(final String action) {
		this.action = action;
	}

	/**
	 * Process any stream connection to this module
	 *
	 * @param inputStream  the input stream
	 * @param outputStream the output stream
	 * @throws Exception when the stream processing failed
	 */
	@Override
	public void process(final InputStream inputStream, final OutputStream outputStream) throws Exception {

		GZIPInputStream gzipInputStream = new GZIPInputStream(new BufferedInputStream(inputStream));

		DataInputStream dataInputStream = new DataInputStream(gzipInputStream);
		String username = dataInputStream.readUTF();
		String password = dataInputStream.readUTF();
		Integer cohortId = 0;
		if (StringUtils.equalsIgnoreCase(getAction(), HttpProcessor.PROCESS_PATIENTS))
			cohortId = dataInputStream.readInt();
		dataInputStream.close();

		GZIPOutputStream gzipOutputStream = new GZIPOutputStream(new BufferedOutputStream(outputStream));
		DataOutputStream dataOutputStream = new DataOutputStream(gzipOutputStream);
		try {
			Context.openSession();
			Context.authenticate(username, password);

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Serializer serializer = HandlerUtil.getPreferredHandler(Serializer.class, List.class);
			if (StringUtils.equalsIgnoreCase(getAction(), HttpProcessor.PROCESS_PATIENTS)) {
				ConnectorService connectorService = Context.getService(ConnectorService.class);
				Cohort cohort = Context.getCohortService().getCohort(cohortId);
				serializer.write(stream, connectorService.getCohortPatients(cohort));
				// check the concept list
				List<Concept> concepts = new ArrayList<Concept>();
				String conceptIds = Context.getAdministrationService().getGlobalProperty(SerializationConstants.CLINIC_CONCEPTS);
				for (String conceptId : StringUtils.split(StringUtils.defaultString(conceptIds), ",")) {
					Concept concept = Context.getConceptService().getConcept(conceptId);
					if (concept != null)
						concepts.add(concept);
				}
				serializer.write(stream, connectorService.getCohortObservations(cohort, concepts));

				// evaluate and get the applicable form for the patients
				CohortDefinitionService cohortDefinitionService = Context.getService(CohortDefinitionService.class);
				ReportingConnectorService reportingConnectorService = Context.getService(ReportingConnectorService.class);
				List<ExtendedDefinition> definitions = reportingConnectorService.getAllExtendedDefinition();

				EvaluationContext context = new EvaluationContext();
				context.setBaseCohort(cohort);

				List<SerializedForm> serializedForms = new ArrayList<SerializedForm>();
				for (ExtendedDefinition definition : definitions) {
					if (definition.containsProperty(ExtendedDefinition.DEFINITION_PROPERTY_FORM)) {
						EvaluatedCohort evaluatedCohort = cohortDefinitionService.evaluate(definition.getCohortDefinition(), context);
						for (DefinitionProperty definitionProperty : definition.getProperties()) {
							Integer formId = NumberUtils.toInt(definitionProperty.getPropertyValue());
							for (Object patientId : evaluatedCohort.getMemberIds())
								serializedForms.add(new SerializedForm(NumberUtils.toInt(String.valueOf(patientId)), formId));
						}
					}
				}
				serializer.write(stream, serializedForms);
			} else {
				serializer.write(stream, Context.getCohortService().getAllCohorts());
			}

			dataOutputStream.writeInt(HttpURLConnection.HTTP_OK);
			dataOutputStream.write(stream.toByteArray());
			dataOutputStream.close();
		} catch (Exception e) {
			log.error("Processing stream failed!", e);
			dataOutputStream.writeInt(HttpURLConnection.HTTP_UNAUTHORIZED);
			dataOutputStream.close();
		} finally {
			Context.closeSession();
		}
	}

}
