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

package org.openmrs.module.odkconnector.serialization.utils;

import java.io.OutputStream;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.util.OpenmrsClassLoader;

public class SerializationUtils {

    private static final Log log = LogFactory.getLog(SerializationUtils.class);

    /**
     * Invoke the serialization process
     *
     * @param serializerKey    the key to the implementation of the serializer
     * @param defaultClassName the default name of the serializer
     * @param outputStream     the output stream where the serializer will write the data
     * @param data             the data to be serialized
     * @throws Exception when the serialization process failed
     */
    public static void invokeSerializationMethod(final String serializerKey, final String defaultClassName,
                                                 final OutputStream outputStream, final Object data) throws Exception {
        String serializerClass = Context.getAdministrationService().getGlobalProperty(serializerKey);

        if (serializerClass == null || serializerClass.length() == 0)
            serializerClass = defaultClassName;

        Object obj = OpenmrsClassLoader.getInstance().loadClass(serializerClass).newInstance();

        Method method = obj.getClass().getMethod("write", new Class[]{OutputStream.class, Object.class});
        method.invoke(obj, outputStream, data);
    }

}
