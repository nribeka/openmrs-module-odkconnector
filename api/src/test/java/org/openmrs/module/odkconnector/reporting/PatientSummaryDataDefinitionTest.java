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

package org.openmrs.module.odkconnector.reporting;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.PatientIdentifier;
import org.openmrs.PersonName;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PatientService;
import org.openmrs.api.PatientSetService;
import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.common.Age;
import org.openmrs.module.reporting.common.TimeQualifier;
import org.openmrs.module.reporting.data.converter.AgeConverter;
import org.openmrs.module.reporting.data.converter.BirthdateConverter;
import org.openmrs.module.reporting.data.converter.DateConverter;
import org.openmrs.module.reporting.data.converter.ListConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.converter.PropertyConverter;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDatetimeDataDefinition;
import org.openmrs.module.reporting.data.encounter.definition.EncounterIdDataDefinition;
import org.openmrs.module.reporting.data.encounter.definition.EncounterLocationDataDefinition;
import org.openmrs.module.reporting.data.encounter.definition.EncounterProviderDataDefinition;
import org.openmrs.module.reporting.data.encounter.definition.EncounterTypeDataDefinition;
import org.openmrs.module.reporting.data.obs.definition.ObsConceptDataDefinition;
import org.openmrs.module.reporting.data.obs.definition.ObsDatetimeDataDefinition;
import org.openmrs.module.reporting.data.obs.definition.ObsIdDataDefinition;
import org.openmrs.module.reporting.data.obs.definition.ObsNumericDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.data.person.definition.AgeDataDefinition;
import org.openmrs.module.reporting.data.person.definition.BirthdateDataDefinition;
import org.openmrs.module.reporting.data.person.definition.GenderDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PreferredNameDataDefinition;
import org.openmrs.module.reporting.dataset.DataSetUtil;
import org.openmrs.module.reporting.dataset.SimpleDataSet;
import org.openmrs.module.reporting.dataset.definition.EncounterDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.ObsDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.service.DataSetDefinitionService;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.query.encounter.definition.BasePatientEncounterQuery;
import org.openmrs.module.reporting.query.obs.definition.DateObsQuery;
import org.openmrs.module.reporting.query.obs.definition.NumericObsQuery;
import org.openmrs.module.reporting.report.ReportData;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.openmrs.module.reporting.report.renderer.CsvReportRenderer;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class PatientSummaryDataDefinitionTest extends BaseModuleContextSensitiveTest {

    private static final Log log = LogFactory.getLog(PatientDataSetDefinition.class);

    @Before
    public void setup() throws Exception {
        executeDataSet("org/openmrs/module/reporting/include/ReportTestDataset.xml");
    }

    private EvaluationContext prepareEvaluationContext() {
        PatientService patientService = Context.getPatientService();
        EncounterService encounterService = Context.getEncounterService();

        EvaluationContext context = new EvaluationContext();
        context.addParameterValue("currentDate", new Date());
        context.addParameterValue("identifierTypes",
                Arrays.asList(patientService.getPatientIdentifierType(1), patientService.getPatientIdentifierType(2)));
        context.addParameterValue("encounterTypes",
                Arrays.asList(encounterService.getEncounterType(1), encounterService.getEncounterType(2), encounterService.getEncounterType(6)));

        return context;
    }

    @Test
    public void generateSummary() throws Exception {

        PatientDataSetDefinition definition = new PatientDataSetDefinition();

        definition.addColumn("id", new PatientIdDataDefinition(), StringUtils.EMPTY, new ObjectFormatter());

        ListConverter listConverter = new ListConverter();
        listConverter.setMaxNumberOfItems(1);

        PatientIdentifierDataDefinition preferredIdentifier = new PatientIdentifierDataDefinition();
        preferredIdentifier.addParameter(new Parameter("types", "identifier types", PatientIdentifier.class));
        definition.addColumn("identifier", preferredIdentifier, "types=${identifierTypes}", listConverter);

        definition.addColumn("name", new PreferredNameDataDefinition(), StringUtils.EMPTY, new ObjectFormatter("{familyName}, {givenName}"));

        AgeDataDefinition ageOnDate = new AgeDataDefinition();
        ageOnDate.addParameter(new Parameter("effectiveDate", "effective date", Date.class));
        definition.addColumn("age", ageOnDate, "effectiveDate=${currentDate}", new AgeConverter());

        definition.addColumn("birthdate", new BirthdateDataDefinition(), StringUtils.EMPTY, new BirthdateConverter("dd-MMM-yyyy"));
        definition.addColumn("gender", new GenderDataDefinition(), StringUtils.EMPTY, new ObjectFormatter());

        DateObsQuery returnVisitQuery = new DateObsQuery();
        returnVisitQuery.setTimeModifier(PatientSetService.TimeModifier.LAST);
        returnVisitQuery.addConcept(Context.getConceptService().getConcept(20));

        ObsDataSetDefinition returnVisit = new ObsDataSetDefinition();
        returnVisit.addRowFilter(returnVisitQuery, null);
        returnVisit.addColumn("return-visit-date", new ObsDatetimeDataDefinition(), null, new DateConverter("dd-MMM-yyyy"));
        definition.addColumns("return-visit", returnVisit, null);

        BasePatientEncounterQuery recentEncounterQuery = new BasePatientEncounterQuery();
        recentEncounterQuery.setTimeModifier(PatientSetService.TimeModifier.LAST);
        recentEncounterQuery.addParameter(new Parameter("encounterTypes", "encounter types", List.class));

        EncounterDataSetDefinition recentEncounterDataSet = new EncounterDataSetDefinition();
        recentEncounterDataSet.addRowFilter(recentEncounterQuery, "encounterTypes=${encounterTypes}");
        recentEncounterDataSet.addColumn("last-encounter-id", new EncounterIdDataDefinition(), null, new ObjectFormatter());
        recentEncounterDataSet.addColumn("last-encounter-type", new EncounterTypeDataDefinition(), null, new ObjectFormatter());
        recentEncounterDataSet.addColumn("last-encounter-location", new EncounterLocationDataDefinition(), null, new ObjectFormatter());
        recentEncounterDataSet.addColumn("last-encounter-provider", new EncounterProviderDataDefinition(), null, new PropertyConverter(PersonName.class, "personName"), new ObjectFormatter("{familyName}, {givenName}"));
        recentEncounterDataSet.addColumn("last-encounter-provider-age", new EncounterProviderDataDefinition(), null, new PropertyConverter(Age.class, "age"), new ObjectFormatter());
        recentEncounterDataSet.addColumn("last-encounter-date", new EncounterDatetimeDataDefinition(), null, new DateConverter("dd-MMM-yyyy"));

        definition.addColumns("last-visit", recentEncounterDataSet, null);

        BasePatientEncounterQuery firstEncounterQuery = new BasePatientEncounterQuery();
        firstEncounterQuery.setTimeModifier(PatientSetService.TimeModifier.FIRST);
        firstEncounterQuery.addParameter(new Parameter("encounterTypes", "encounter types", List.class));

        EncounterDataSetDefinition firstEncounterDataSet = new EncounterDataSetDefinition();
        firstEncounterDataSet.addRowFilter(firstEncounterQuery, "encounterTypes=${encounterTypes}");
        firstEncounterDataSet.addColumn("first-encounter-id", new EncounterIdDataDefinition(), null, new ObjectFormatter());
        firstEncounterDataSet.addColumn("first-encounter-type", new EncounterTypeDataDefinition(), null, new ObjectFormatter());
        firstEncounterDataSet.addColumn("first-encounter-date", new EncounterDatetimeDataDefinition(), null, new DateConverter("dd-MMM-yyyy"));

        definition.addColumns("first-visit", firstEncounterDataSet, null);

        SimpleDataSet dataset = (SimpleDataSet)Context.getService(DataSetDefinitionService.class).evaluate(definition, prepareEvaluationContext());
        DataSetUtil.printDataSet(dataset, System.out);
    }
}
