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

package org.openmrs.module.odkconnector.serialization.serializer.openmrs;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Obs;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.odkconnector.serialization.Serializer;


@Handler(supports = Obs.class, order = 50)
public class ObsSerializer implements Serializer {

    private static final Log log = LogFactory.getLog(ObsSerializer.class);

    public static final Integer TYPE_STRING = 1;

    public static final Integer TYPE_INT = 2;

    public static final Integer TYPE_DOUBLE = 3;

    public static final Integer TYPE_DATE = 4;

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
            Obs obs = (Obs) data;

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            DataOutputStream outputStream = new DataOutputStream(stream);
            // write the person id of the observation
            outputStream.writeInt(obs.getPersonId());
            // write the concept name of the observation
            outputStream.writeUTF(obs.getConcept().getDisplayString());
            // write the data type and the value of the observation
            if (obs.getValueDatetime() != null) {
                outputStream.writeByte(TYPE_DATE);
                outputStream.writeUTF(dateFormat.format(obs.getValueDatetime()));
            } else if (obs.getValueCoded() != null) {
                outputStream.writeByte(TYPE_STRING);
                outputStream.writeUTF(obs.getValueCoded().getDisplayString());
            } else if (obs.getValueNumeric() != null) {
                outputStream.writeByte(TYPE_DOUBLE);
                outputStream.writeDouble(obs.getValueNumeric());
            } else {
                outputStream.writeByte(TYPE_STRING);
                outputStream.writeUTF(obs.getValueAsString(Context.getLocale()));
            }
            // write the datetime of the observation
            outputStream.writeUTF(dateFormat.format(obs.getObsDatetime()));
        } catch (IOException e) {
            log.info("Writing obs information failed!", e);
        }
    }
}
