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

package org.openmrs.module.odkconnector.reporting.metadata;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.BaseOpenmrsMetadata;

public class DefinitionProperty extends BaseOpenmrsMetadata {

    private static final Log log = LogFactory.getLog(DefinitionProperty.class);

    private Integer id;

    private String property;

    private String propertyValue;

    private String propertyDescription;

    private ExtendedDefinition extendedDefinition;

    /**
     * Default constructor to create definition property
     */
    public DefinitionProperty() {
    }

    /**
     * Constructor to create definition property
     *
     * @param property            the property
     * @param propertyValue       the property value
     * @param propertyDescription the property description
     */
    public DefinitionProperty(final String property, final String propertyValue, final String propertyDescription) {
        this.property = property;
        this.propertyValue = propertyValue;
        this.propertyDescription = propertyDescription;
    }

    /**
     * @return id - The unique Identifier for the object
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * @param id - The unique Identifier for the object
     */
    @Override
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     * Get the property of the definition property
     *
     * @return the property
     */
    public String getProperty() {
        return property;
    }

    /**
     * Set the property of the definition property
     *
     * @param property the property
     */
    public void setProperty(final String property) {
        this.property = property;
    }

    /**
     * Get the property value of the definition property
     *
     * @return the property value
     */
    public String getPropertyValue() {
        return propertyValue;
    }

    /**
     * Set the property value of the definition property
     *
     * @param propertyValue the property value
     */
    public void setPropertyValue(final String propertyValue) {
        this.propertyValue = propertyValue;
    }

    /**
     * Set the property description of the definition property
     *
     * @return the property description
     */
    public String getPropertyDescription() {
        return propertyDescription;
    }

    /**
     * Set the property description of the definition property
     *
     * @param propertyDescription the property description
     */
    public void setPropertyDescription(final String propertyDescription) {
        this.propertyDescription = propertyDescription;
    }

    /**
     * The inverse reference to the definition property
     *
     * @return the definition property
     */
    public ExtendedDefinition getExtendedDefinition() {
        return extendedDefinition;
    }

    /**
     * Set the inverse reference to the definition property
     *
     * @param extendedDefinition the definition property
     */
    public void setExtendedDefinition(final ExtendedDefinition extendedDefinition) {
        this.extendedDefinition = extendedDefinition;
    }
}
