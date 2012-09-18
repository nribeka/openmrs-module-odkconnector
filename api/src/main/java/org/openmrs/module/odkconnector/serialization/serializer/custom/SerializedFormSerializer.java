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

package org.openmrs.module.odkconnector.serialization.serializer.custom;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.annotation.Handler;
import org.openmrs.module.odkconnector.reporting.metadata.ExtendedDefinition;
import org.openmrs.module.odkconnector.serialization.Serializer;
import org.openmrs.module.odkconnector.serialization.serializable.SerializedForm;

@Handler(supports = SerializedForm.class, order = 50)
public class SerializedFormSerializer implements Serializer {

    private static final Log log = LogFactory.getLog(SerializedFormSerializer.class);

    public static final Integer TYPE_INT = 2;

    /**
     * Write the data to the output stream.
     *
     * @param stream the output stream
     * @param data   the data that need to be written to the output stream
     * @throws java.io.IOException thrown when the writing process encounter is failing
     */
    @Override
    public void write(final OutputStream stream, final Object data) throws IOException {
        try {
            SerializedForm serializedForm = (SerializedForm) data;

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            DataOutputStream outputStream = new DataOutputStream(stream);

            outputStream.writeInt(serializedForm.getPatientId());
            outputStream.writeUTF(ExtendedDefinition.DEFINITION_PROPERTY_FORM);
            outputStream.writeByte(TYPE_INT);
            outputStream.writeInt(serializedForm.getFormId());
            outputStream.writeUTF(dateFormat.format(new Date()));
        } catch (IOException e) {
            log.info("Writing form information failed!", e);
        }
    }
}
