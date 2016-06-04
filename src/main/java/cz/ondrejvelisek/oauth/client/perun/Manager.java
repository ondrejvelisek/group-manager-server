package cz.ondrejvelisek.oauth.client.perun;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import cz.ondrejvelisek.oauth.client.model.Group;
import cz.ondrejvelisek.oauth.client.model.PerunException;
import cz.ondrejvelisek.oauth.client.controllers.OauthFilter;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public abstract class Manager {

    private String token;

    public Manager(String token) {
        this.token = token;
    }

    protected abstract String getManager();

    protected String get(String method) throws PerunException {
        return get(method, new HashMap<String, Object>());
    }

    protected String get(String method, Map<String, Object> params) throws PerunException {

        try {
            URL resourceURL = new URL(OauthFilter.RESOURCE_ENDPOINT+"/"+getManager()+"/"+method+"?"+queryString(params));

            HttpURLConnection conn = (HttpURLConnection) resourceURL.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", (new BearerAccessToken(token)).toAuthorizationHeader());

            return getString(conn);

        } catch (IOException e) {
            throw new PerunException(e);
        }
    }



    protected String post(String method, Map<String, Object> params) throws PerunException {

        try {
            URL object = new URL(OauthFilter.RESOURCE_ENDPOINT+"/"+getManager()+"/"+method);

            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", (new BearerAccessToken(token)).toAuthorizationHeader());
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");

            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(jsonString(params));
            wr.flush();

            return getString(con);

        } catch (IOException e) {
            throw new PerunException(e);
        }
    }


    private String getString(HttpURLConnection c) throws PerunException, IOException {
        try {
            c.connect();

            int httpCode = c.getResponseCode();
            if (200 <= httpCode && httpCode < 300) {

                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                return sb.toString();

            } else {

                BufferedReader br = new BufferedReader(new InputStreamReader(c.getErrorStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                String response = sb.toString();
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    throw mapper.readValue(response, PerunException.class);
                } catch (IOException e){
                    throw new PerunException("UnsupportedDataTypeException", "Fail while reading error response from Perun. Response data: " + response);
                }
            }

        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception e) {
                    throw new PerunException(e);
                }
            }
        }
    }


    private String jsonString(Map<String, Object> params) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(params);

    }

    private String queryString(Map<String, Object> params) throws UnsupportedEncodingException, JsonProcessingException {
        if (params == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, Object> e : params.entrySet()){
            if(sb.length() > 0){
                sb.append('&');
            }
            String key = URLEncoder.encode(e.getKey(), "UTF-8");
            String value;
            if (e.getValue() instanceof String) {
                value = URLEncoder.encode(e.getValue().toString(), "UTF-8");
            } else if (Utils.isWrapperType(e.getValue().getClass())) {
                value = URLEncoder.encode(e.getValue().toString(), "UTF-8");
            } else {
                ObjectMapper mapper = new ObjectMapper();
                value = URLEncoder.encode(mapper.writeValueAsString(e.getValue()));
            }

            sb.append(key).append('=').append(value);
        }
        return sb.toString();
    }

}
