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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.openmrs.Cohort;
import org.openmrs.PatientIdentifier;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.common.SortCriteria;
import org.openmrs.module.reporting.common.TimeQualifier;
import org.openmrs.module.reporting.data.converter.AgeConverter;
import org.openmrs.module.reporting.data.converter.BirthdateConverter;
import org.openmrs.module.reporting.data.converter.DateConverter;
import org.openmrs.module.reporting.data.converter.ListConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDatetimeDataDefinition;
import org.openmrs.module.reporting.data.encounter.definition.EncounterIdDataDefinition;
import org.openmrs.module.reporting.data.encounter.definition.EncounterTypeDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.data.person.definition.AgeDataDefinition;
import org.openmrs.module.reporting.data.person.definition.BirthdateDataDefinition;
import org.openmrs.module.reporting.data.person.definition.GenderDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PreferredNameDataDefinition;
import org.openmrs.module.reporting.dataset.DataSetUtil;
import org.openmrs.module.reporting.dataset.SimpleDataSet;
import org.openmrs.module.reporting.dataset.definition.EncounterDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.service.DataSetDefinitionService;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.query.encounter.definition.AllEncounterQuery;
import org.openmrs.module.reporting.query.encounter.definition.MostRecentEncounterForPatientQuery;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class PatientSummaryDataDefinitionTest extends BaseModuleContextSensitiveTest {

	private static final Log log = LogFactory.getLog(PatientSummaryDataDefinitionTest.class);

	@Test
	public void generateSummary() throws Exception {

		PatientService patientService = Context.getPatientService();
		EncounterService encounterService = Context.getEncounterService();

		EvaluationContext context = new EvaluationContext();
		context.addParameterValue("startDate", DateUtil.getDateTime(2010, 7, 1));
		context.addParameterValue("identifierTypes", Arrays.asList(patientService.getPatientIdentifierType(1),
				patientService.getPatientIdentifierType(2)));
		context.addParameterValue("encounterTypes", Arrays.asList(encounterService.getEncounterType(1),
				encounterService.getEncounterType(2)));
		context.addParameterValue("firstTimeQualifier", TimeQualifier.FIRST);
		context.addParameterValue("lastTimeQualifier", TimeQualifier.LAST);

		Cohort c = new Cohort("2,6,7");
		context.setBaseCohort(c);

		PatientDataSetDefinition definition = new PatientDataSetDefinition();

		definition.addColumn("id", new PatientIdDataDefinition(), null);

		ListConverter listConverter = new ListConverter();
		listConverter.setMaxNumberOfItems(1);

		PatientIdentifierDataDefinition preferredIdentifier = new PatientIdentifierDataDefinition();
		preferredIdentifier.addParameter(new Parameter("types", "Identifier Types", PatientIdentifier.class));
		definition.addColumn("identifier", preferredIdentifier, "types=${identifierTypes}", listConverter);

		definition.addColumn("name", new PreferredNameDataDefinition(), null, new ObjectFormatter("{familyName}, {givenName}"));

		AgeDataDefinition ageOnDate = new AgeDataDefinition();
		ageOnDate.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
		definition.addColumn("age", ageOnDate, "effectiveDate=${startDate}", new AgeConverter());

		definition.addColumn("birthdate", new BirthdateDataDefinition(), null, new BirthdateConverter("dd-MMM-yyyy"));
		definition.addColumn("gender", new GenderDataDefinition(), null);

		EncounterDataSetDefinition encounterDataSet = new EncounterDataSetDefinition();
		encounterDataSet.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));

		MostRecentEncounterForPatientQuery encounterQuery = new MostRecentEncounterForPatientQuery();
		encounterQuery.addParameter(new Parameter("onOrBefore", "On or Before Date", Date.class));
		encounterDataSet.addRowFilter(encounterQuery, "onOrBefore=${startDate}");

		encounterDataSet.addColumn("Last Encounter ID", new EncounterIdDataDefinition(), null, new ObjectFormatter());
		encounterDataSet.addColumn("Last Encounter Type", new EncounterTypeDataDefinition(), null, new ObjectFormatter());
		encounterDataSet.addColumn("Last Encounter Date", new EncounterDatetimeDataDefinition(), null, new DateConverter("dd-MMM-yyyy"));

		definition.addColumns("Last Scheduled Visit", encounterDataSet, "effectiveDate=${startDate}", new ObjectFormatter());
		definition.addSortCriteria("Last Encounter Date", SortCriteria.SortDirection.ASC);

		SimpleDataSet dataset = (SimpleDataSet) Context.getService(DataSetDefinitionService.class).evaluate(definition, context);

		DataSetUtil.printDataSet(dataset, System.out);
	}

	@Test
	public void generateEncounter() throws Exception {


		EvaluationContext context = new EvaluationContext();

		PatientDataSetDefinition d = new PatientDataSetDefinition();
		d.addColumn("patientId", new PatientIdDataDefinition(), null);

		EncounterDataSetDefinition encounterDataSet = new EncounterDataSetDefinition();
		encounterDataSet.addRowFilter(new AllEncounterQuery(), "");
		encounterDataSet.addColumn("Encounter Type", new EncounterTypeDataDefinition(), null, new ObjectFormatter());
		encounterDataSet.addColumn("Encounter Date", new EncounterDatetimeDataDefinition(), null, new DateConverter("dd-MMM-yyyy"));

		d.addColumns("Last 4 Encounters", encounterDataSet, null, TimeQualifier.ANY, 4);

		SimpleDataSet dataset = (SimpleDataSet)Context.getService(DataSetDefinitionService.class).evaluate(d, context);
		DataSetUtil.printDataSet(dataset, System.out);

	}

}
