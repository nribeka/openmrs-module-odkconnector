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
package org.openmrs.module.odkconnector.serialization.web;

import org.openmrs.module.odkconnector.serialization.serializer.openmrs.ObsSerializer;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * TODO: Write brief description about the class here.
 */
public class ConnectorDownloader {

    private static final String SERVER_URL = "https://amrs.ampath.or.ke/amrs";

    public void download(final int cohortId, final String username, final String password) throws Exception {

        // compose url
        URL u = new URL(SERVER_URL + "/module/odkconnector/download/patients.form");

        // setup http url connection
        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(10000);
        connection.addRequestProperty("Content-type", "application/octet-stream");

        // write auth details to connection
        DataOutputStream outputStream = new DataOutputStream(new GZIPOutputStream(connection.getOutputStream()));
        outputStream.writeUTF(username);
        outputStream.writeUTF(password);
        outputStream.writeBoolean(true);
        outputStream.writeInt(cohortId);
        outputStream.writeInt(1);
        outputStream.close();

        DataInputStream inputStream = new DataInputStream(new GZIPInputStream(connection.getInputStream()));
        Integer responseStatus = inputStream.readInt();

        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

        int count = 0;
        byte[] buffer = new byte[1024];
        while ((count = inputStream.read(buffer)) > 0) {
            arrayOutputStream.write(buffer, 0, count);
        }
        arrayOutputStream.close();
        inputStream.close();

        File file = new File(System.getProperty("java.io.tmpdir"), "connector.data");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(arrayOutputStream.toByteArray());
        fos.close();

        inputStream = new DataInputStream(new FileInputStream(file));

        if (responseStatus == HttpURLConnection.HTTP_OK) {

            // total number of patients
            Integer patientCounter = inputStream.readInt();
            System.out.println("Patient Counter: " + patientCounter);
            for (int j = 0; j < patientCounter; j++) {
                System.out.println("=================Patient=====================");
                System.out.println("Patient Id: " + inputStream.readInt());
                System.out.println("Family Name: " + inputStream.readUTF());
                System.out.println("Middle Name: " + inputStream.readUTF());
                System.out.println("Last Name: " + inputStream.readUTF());
                System.out.println("Gender: " + inputStream.readUTF());
                System.out.println("Birth Date: " + inputStream.readUTF());
                System.out.println("Identifier: " + inputStream.readUTF());
                System.out.println("Patients: " + j + " out of " + patientCounter);
            }

            Integer obsCounter = inputStream.readInt();
            System.out.println("Observation Counter: " + obsCounter);
            for (int j = 0; j < obsCounter; j++) {
                System.out.println("==================Observation=================");
                System.out.println("Patient Id: " + inputStream.readInt());
                System.out.println("Concept Name: " + inputStream.readUTF());

                byte type = inputStream.readByte();
                if (type == ObsSerializer.TYPE_STRING)
                    System.out.println("Value: " + inputStream.readUTF());
                else if (type == ObsSerializer.TYPE_INT)
                    System.out.println("Value: " + inputStream.readInt());
                else if (type == ObsSerializer.TYPE_DOUBLE)
                    System.out.println("Value: " + inputStream.readDouble());
                else if (type == ObsSerializer.TYPE_DATE)
                    System.out.println("Value: " + inputStream.readUTF());
                System.out.println("Time: " + inputStream.readUTF());
                System.out.println("Obs: " + j + " out of: " + obsCounter);
            }
            Integer formCounter = inputStream.readInt();
            System.out.println("Form Counter: " + formCounter);
            for (int j = 0; j < formCounter; j++) {
                System.out.println("==================Observation=================");
                System.out.println("Patient Id: " + inputStream.readInt());
                System.out.println("Concept Name: " + inputStream.readUTF());

                byte type = inputStream.readByte();
                if (type == ObsSerializer.TYPE_STRING)
                    System.out.println("Value: " + inputStream.readUTF());
                else if (type == ObsSerializer.TYPE_INT)
                    System.out.println("Value: " + inputStream.readInt());
                else if (type == ObsSerializer.TYPE_DOUBLE)
                    System.out.println("Value: " + inputStream.readDouble());
                else if (type == ObsSerializer.TYPE_DATE)
                    System.out.println("Value: " + inputStream.readUTF());
                System.out.println("Time: " + inputStream.readUTF());
                System.out.println("Form: " + j + " out of: " + formCounter);
            }
        }
        inputStream.close();
    }

    public static void main(final String[] args) {
        try {
            ConnectorDownloader c = new ConnectorDownloader();
            c.download(333, "nribeka", "Winyo1604?");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
