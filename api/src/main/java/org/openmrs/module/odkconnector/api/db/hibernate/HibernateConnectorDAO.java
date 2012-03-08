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

package org.openmrs.module.odkconnector.api.db.hibernate;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Cohort;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.odkconnector.api.db.ConnectorDAO;
import org.openmrs.module.odkconnector.reporting.metadata.ExtendedDefinition;

/**
 * It is a default implementation of  {@link org.openmrs.module.odkconnector.api.db.ConnectorDAO}.
 */
public class HibernateConnectorDAO implements ConnectorDAO {

	protected final Log log = LogFactory.getLog(HibernateConnectorDAO.class);

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
	 * @see org.openmrs.module.odkconnector.api.ConnectorService#getCohortPatients(org.openmrs.Cohort)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Patient> getCohortPatients(final Cohort cohort) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Patient.class);
		criteria.add(Restrictions.in("patientId", cohort.getMemberIds()));
		criteria.add(Restrictions.eq("voided", Boolean.FALSE));
		return criteria.list();
	}

	/**
	 * @see org.openmrs.module.odkconnector.api.ConnectorService#getCohortObservations(org.openmrs.Cohort, java.util.List
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Obs> getCohortObservations(final Cohort cohort, final List<Concept> concepts) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Obs.class);
		criteria.add(Restrictions.in("personId", cohort.getMemberIds()));
		// only put the concepts restriction when they are not empty. otherwise, just return all obs
		if (CollectionUtils.isNotEmpty(concepts))
			criteria.add(Restrictions.in("concept", concepts));
		criteria.add(Restrictions.eq("voided", Boolean.FALSE));
		return criteria.list();
	}

	/**
	 * @see org.openmrs.module.odkconnector.api.ConnectorService#saveExtendedDefinition(org.openmrs.module.odkconnector.reporting.metadata.ExtendedDefinition)
	 */
	@Override
	public ExtendedDefinition saveExtendedDefinition(final ExtendedDefinition extendedDefinition) throws DAOException {
		sessionFactory.getCurrentSession().saveOrUpdate(extendedDefinition);
		return extendedDefinition;
	}

	/**
	 * @see org.openmrs.module.odkconnector.api.ConnectorService#getExtendedDefinitionByUuid(String)
	 */
	@Override
	public ExtendedDefinition getExtendedDefinitionByUuid(final String uuid) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ExtendedDefinition.class);
		criteria.add(Restrictions.eq("uuid", uuid));
		criteria.add(Restrictions.eq("retired", Boolean.FALSE));
		return (ExtendedDefinition) criteria.uniqueResult();
	}

	/**
	 * @see org.openmrs.module.odkconnector.api.ConnectorService#getExtendedDefinition(Integer)
	 */
	@Override
	public ExtendedDefinition getExtendedDefinition(final Integer id) throws DAOException {
		return (ExtendedDefinition) sessionFactory.getCurrentSession().get(ExtendedDefinition.class, id);
	}

	/**
	 * @see org.openmrs.module.odkconnector.api.ConnectorService#getAllExtendedDefinition()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ExtendedDefinition> getAllExtendedDefinition() throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ExtendedDefinition.class);
		criteria.add(Restrictions.eq("retired", Boolean.FALSE));
		return criteria.list();
	}
}
