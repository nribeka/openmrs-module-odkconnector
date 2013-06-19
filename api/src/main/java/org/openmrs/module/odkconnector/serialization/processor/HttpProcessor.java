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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.odkconnector.api.ConceptConfiguration;
import org.openmrs.module.odkconnector.api.service.ConnectorService;
import org.openmrs.module.odkconnector.api.utils.ConnectorUtils;
import org.openmrs.module.odkconnector.reporting.metadata.DefinitionProperty;
import org.openmrs.module.odkconnector.reporting.service.ReportingConnectorService;
import org.openmrs.module.odkconnector.serialization.Processor;
import org.openmrs.module.odkconnector.serialization.Serializer;
import org.openmrs.module.odkconnector.serialization.serializable.SerializedForm;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.util.HandlerUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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
        Boolean savedSearch = dataInputStream.readBoolean();
        Integer cohortId = 0;
        Integer programId = 0;
        if (StringUtils.equalsIgnoreCase(getAction(), HttpProcessor.PROCESS_PATIENTS)) {
            cohortId = dataInputStream.readInt();
            programId = dataInputStream.readInt();
        }
        dataInputStream.close();

        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(new BufferedOutputStream(outputStream));
        DataOutputStream dataOutputStream = new DataOutputStream(gzipOutputStream);
        try {
            Context.openSession();
            Context.authenticate(username, password);

            dataOutputStream.writeInt(HttpURLConnection.HTTP_OK);
            dataOutputStream.flush();

            if (log.isDebugEnabled()) {
                log.debug("Saved search: " + savedSearch + ", Cohort: " + cohortId + ", Program: " + programId);
            }

            Serializer serializer = HandlerUtil.getPreferredHandler(Serializer.class, List.class);
            if (StringUtils.equalsIgnoreCase(getAction(), HttpProcessor.PROCESS_PATIENTS)) {
                ConnectorService connectorService = Context.getService(ConnectorService.class);

                EvaluationContext context = new EvaluationContext();

                // evaluate and get the applicable form for the patients
                CohortDefinitionService definitionService = Context.getService(CohortDefinitionService.class);
                CohortDefinition cohortDefinition = definitionService.getDefinition(CohortDefinition.class, cohortId);

                Cohort bigCohort = new Cohort();
                if (cohortDefinition != null) {
                    bigCohort = definitionService.evaluate(cohortDefinition, context);
                }

                List<Patient> patients = connectorService.getCohortPatients(bigCohort);

                if (log.isDebugEnabled()) {
                    log.debug("Cohort data (size: " + bigCohort.getMemberIds().size() + ") :" + bigCohort.getMemberIds());
                }

                log.info("Streaming " + patients.size() + " patients information!");
                serializer.write(dataOutputStream, patients);

                // check the concept list
                Collection<Concept> concepts = null;
                ConceptConfiguration conceptConfiguration = connectorService.getConceptConfiguration(programId);
                if (conceptConfiguration != null) {
                    concepts = ConnectorUtils.getConcepts(conceptConfiguration.getConfiguredConcepts());
                    if (log.isDebugEnabled()) {
                        log.debug("Printing concept configuration information: " + conceptConfiguration);
                    }
                }

                List<Obs> observations = connectorService.getCohortObservations(bigCohort, concepts);
                log.info("Streaming " + observations.size() + " observations information!");
                serializer.write(dataOutputStream, observations);

                ReportingConnectorService reportingService = Context.getService(ReportingConnectorService.class);
                List<DefinitionProperty> definitionProperties = reportingService.getAllDefinitionProperties();
                Map<CohortDefinition, List<DefinitionProperty>> definitionMap = new HashMap<CohortDefinition, List<DefinitionProperty>>();
                for (DefinitionProperty definitionProperty : definitionProperties) {
                    List<DefinitionProperty> definitionPropertyList = definitionMap.get(definitionProperty.getCohortDefinition());
                    if (definitionPropertyList == null) {
                        definitionPropertyList = new ArrayList<DefinitionProperty>();
                        definitionMap.put(definitionProperty.getCohortDefinition(), definitionPropertyList);
                    }
                    definitionPropertyList.add(definitionProperty);
                }

                context.setBaseCohort(bigCohort);

                List<SerializedForm> serializedForms = new ArrayList<SerializedForm>();
                for (CohortDefinition definition : definitionMap.keySet()) {
                    if (log.isDebugEnabled()) {
                        log.debug("Evaluating cohort definition: " + definition.getName());
                    }

                    Cohort smallCohort = definitionService.evaluate(definition, context);
                    // the cohort could be null, so we don't want to get exception during the intersection process
                    Cohort intersectedCohort = Cohort.intersect(bigCohort, smallCohort);
                    if (log.isDebugEnabled()) {
                        log.debug("Cohort data after intersection: " + intersectedCohort.getMemberIds());
                    }

                    for (DefinitionProperty definitionProperty : definitionMap.get(cohortDefinition)) {
                        Integer formId = NumberUtils.toInt(definitionProperty.getPropertyValue());
                        for (Integer patientId : intersectedCohort.getMemberIds()) {
                            serializedForms.add(new SerializedForm(patientId, formId));
                        }
                    }
                }

                if (log.isDebugEnabled()) {
                    log.debug("Serialized form informations:" + serializedForms);
                }

                log.info("Streaming " + serializedForms.size() + " forms information!");
                serializer.write(dataOutputStream, serializedForms);

            } else {
                serializer.write(dataOutputStream, Context.getCohortService().getAllCohorts());
            }
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
