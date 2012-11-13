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

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.BaseOpenmrsMetadata;

public class ConceptConfiguration extends BaseOpenmrsMetadata {

    private static final Log log = LogFactory.getLog(ConceptConfiguration.class);

    private Integer id;

    private Set<ConfiguredConcept> configuredConcepts;

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
     * Return set of configured concepts
     *
     * @return
     */
    public Set<ConfiguredConcept> getConfiguredConcepts() {
        return configuredConcepts;
    }

    /**
     * Set the configured concepts
     *
     * @param configuredConcepts
     */
    public void setConfiguredConcepts(final Set<ConfiguredConcept> configuredConcepts) {
        this.configuredConcepts = configuredConcepts;
    }

    /**
     * Add a configured concept into the configuration
     *
     * @param configuredConcept
     */
    public void addConfiguredConcept(final ConfiguredConcept configuredConcept) {
        if (configuredConcepts == null)
            configuredConcepts = new LinkedHashSet<ConfiguredConcept>();
        configuredConcepts.add(configuredConcept);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ConceptConfiguration {").append("\n");
        builder.append("    id=" + id).append("\n");
        builder.append("    configuredConcepts:").append("\n");
        for (ConfiguredConcept configuredConcept : configuredConcepts) {
            builder.append("        ").append(configuredConcept).append("\n");
        }
        builder.append("}");
        return builder.toString();
    }
}
