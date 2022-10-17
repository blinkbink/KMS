package com.digisign.kms.externalapi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import com.digisign.kms.model.Log;
import com.digisign.kms.util.Description;
import com.digisign.kms.util.LogSystem;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ActivityLog extends Description {

    private Date tsp;
    public ActivityLog(Date tsp) {
        this.tsp=tsp;
    }

    public void sendPost(Log data) {

        // url is missing?
        //String url = "https://selfsolve.apple.com/wcResults.do";
        String url = LOG_SYS_API+"/create/activitylog";

        LogSystem.info("URL " + url);
        HttpURLConnection httpClient;
        try {
            httpClient = (HttpURLConnection) new URL(url).openConnection();

            //add reuqest header
            httpClient.setRequestMethod("POST");
            httpClient.setRequestProperty("Content-Type", "application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody =  "";

            jsonBody=objectMapper.writeValueAsString(data) ;

            // Send post request
            httpClient.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
                wr.writeBytes(jsonBody);
                wr.flush();
            }

            int responseCode = httpClient.getResponseCode();
            LogSystem.info("Sending 'POST' request to URL : " + url);
            LogSystem.info("Post parameters : " + jsonBody);
            LogSystem.info("Response Code : " + responseCode);

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpClient.getInputStream()))) {

                String line;
                StringBuilder response = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }

                //print result
                LogSystem.info(response.toString());

            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            LogSystem.error(e.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            LogSystem.error(e.toString());
        }
    }
}
