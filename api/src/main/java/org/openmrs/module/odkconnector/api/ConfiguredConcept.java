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

package org.openmrs.module.odkconnector.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;

public class ConfiguredConcept extends BaseOpenmrsMetadata {

    private static final Log log = LogFactory.getLog(ConfiguredConcept.class);

    private Integer id;

    private Concept concept;

    private ConceptConfiguration conceptConfiguration;

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
     * Return the concept
     *
     * @return
     */
    public Concept getConcept() {
        return concept;
    }

    /**
     * Set the concept
     *
     * @param concept the concept
     */
    public void setConcept(final Concept concept) {
        this.concept = concept;
    }

    /**
     * @return the concept configuration
     */
    public ConceptConfiguration getConceptConfiguration() {
        return conceptConfiguration;
    }

    /**
     * Set the concept configuration
     *
     * @param conceptConfiguration the concept configuration
     */
    public void setConceptConfiguration(final ConceptConfiguration conceptConfiguration) {
        this.conceptConfiguration = conceptConfiguration;
    }

    @Override
    public String toString() {
        return "ConfiguredConcept{" +
                "id=" + id
                + ", concept id=" + concept.getId()
                + ", concept name=" + concept.getName(Context.getLocale()).getName()
                + ", concept uuid=" + concept.getUuid()
                + ", conceptConfiguration id=" + conceptConfiguration.getId() + '}';
    }
}
