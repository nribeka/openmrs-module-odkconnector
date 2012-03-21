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

package org.openmrs.module.odkconnector.reporting.db.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.odkconnector.reporting.db.ReportingConnectorDAO;
import org.openmrs.module.odkconnector.reporting.metadata.DefinitionProperty;
import org.openmrs.module.odkconnector.reporting.metadata.ExtendedDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;

public class HibernateReportingConnectorDAO implements ReportingConnectorDAO {

	private static final Log log = LogFactory.getLog(HibernateReportingConnectorDAO.class);

	private SessionFactory sessionFactory;

	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @see org.openmrs.module.odkconnector.reporting.service.ReportingConnectorService#saveExtendedDefinition(org.openmrs.module.odkconnector.reporting.metadata.ExtendedDefinition)
	 */
	@Override
	public ExtendedDefinition saveExtendedDefinition(final ExtendedDefinition extendedDefinition) throws DAOException {
		sessionFactory.getCurrentSession().saveOrUpdate(extendedDefinition);
		return extendedDefinition;
	}

	/**
	 * @see org.openmrs.module.odkconnector.reporting.service.ReportingConnectorService#getExtendedDefinitionByUuid(String)
	 */
	@Override
	public ExtendedDefinition getExtendedDefinitionByUuid(final String uuid) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ExtendedDefinition.class);
		criteria.add(Restrictions.eq("uuid", uuid));
		criteria.add(Restrictions.eq("retired", Boolean.FALSE));
		return (ExtendedDefinition) criteria.uniqueResult();
	}

	/**
	 * @see org.openmrs.module.odkconnector.reporting.service.ReportingConnectorService#getExtendedDefinitionByDefinition(org.openmrs.module.reporting.cohort.definition.CohortDefinition)
	 */
	@Override
	public ExtendedDefinition getExtendedDefinitionByDefinition(final CohortDefinition definition) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ExtendedDefinition.class);
		criteria.add(Restrictions.eq("cohortDefinition", definition));
		criteria.add(Restrictions.eq("retired", Boolean.FALSE));
		return (ExtendedDefinition) criteria.uniqueResult();
	}

	/**
	 * @see org.openmrs.module.odkconnector.reporting.service.ReportingConnectorService#getExtendedDefinition(Integer)
	 */
	@Override
	public ExtendedDefinition getExtendedDefinition(final Integer id) throws DAOException {
		return (ExtendedDefinition) sessionFactory.getCurrentSession().get(ExtendedDefinition.class, id);
	}

	/**
	 * @see org.openmrs.module.odkconnector.reporting.service.ReportingConnectorService#getAllExtendedDefinition()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ExtendedDefinition> getAllExtendedDefinition() throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ExtendedDefinition.class);
		criteria.add(Restrictions.eq("retired", Boolean.FALSE));
		return criteria.list();
	}

	/**
	 * @see org.openmrs.module.odkconnector.reporting.service.ReportingConnectorService#saveDefinitionProperty(org.openmrs.module.odkconnector.reporting.metadata.DefinitionProperty)
	 */
	@Override
	public DefinitionProperty saveDefinitionProperty(final DefinitionProperty definitionProperty) throws DAOException {
		sessionFactory.getCurrentSession().saveOrUpdate(definitionProperty);
		return definitionProperty;
	}

	/**
	 * @see org.openmrs.module.odkconnector.reporting.service.ReportingConnectorService#getDefinitionProperty(Integer)
	 */
	@Override
	public DefinitionProperty getDefinitionProperty(final Integer id) throws DAOException {
		return (DefinitionProperty) sessionFactory.getCurrentSession().get(DefinitionProperty.class, id);
	}

}
