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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.APIException;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.odkconnector.reporting.db.ReportingConnectorDAO;
import org.openmrs.module.odkconnector.reporting.metadata.DefinitionProperty;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * DAO method to get the definition property by the definition property uuid
     *
     * @param uuid the definition property uuid
     * @return the definition property or null
     * @throws org.openmrs.api.db.DAOException
     *          when getting the definition property failed
     */
    @Override
    public DefinitionProperty getDefinitionPropertyByUuid(final String uuid) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DefinitionProperty.class);
        criteria.add(Restrictions.eq("uuid", uuid));
        criteria.add(Restrictions.eq("retired", Boolean.FALSE));
        return (DefinitionProperty) criteria.uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DefinitionProperty> getDefinitionPropertiesByCohortDefinition(final CohortDefinition cohortDefinition)
            throws APIException {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DefinitionProperty.class);
        criteria.add(Restrictions.eq("cohortDefinition", cohortDefinition));
        criteria.add(Restrictions.eq("retired", Boolean.FALSE));
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DefinitionProperty> getAllDefinitionProperties() throws APIException {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DefinitionProperty.class);
        criteria.add(Restrictions.eq("retired", Boolean.FALSE));
        return criteria.list();
    }

}
