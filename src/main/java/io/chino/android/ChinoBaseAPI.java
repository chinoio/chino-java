package io.chino.android;

import io.chino.api.common.ChinoApiException;
import io.chino.api.common.ErrorResponse;
import okhttp3.*;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.*;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

public class ChinoBaseAPI {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String hostUrl;
    OkHttpClient client;
    static ObjectMapper mapper;

    public ChinoBaseAPI(String hostUrl, OkHttpClient client){
        this.hostUrl = hostUrl;
        this.client = client;
        mapper = getMapper();
    }


    public JsonNode postResource(String path, Object resource) throws IOException, ChinoApiException {
        String json = mapper.writeValueAsString(resource);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(hostUrl+path)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        if (response.code() == 200) {
            return mapper.readTree(body).get("data");
        } else {
            throw new ChinoApiException(mapper.readValue(body, ErrorResponse.class));
        }
    }

    public JsonNode getResource(String path, int offset, int limit) throws IOException, ChinoApiException {
        Request request = new Request.Builder()
                .url(hostUrl + path+"?offset="+offset+"&limit="+limit)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        if (response.code() == 200) {
            return mapper.readTree(body).get("data");
        } else {
            throw new ChinoApiException(mapper.readValue(body, ErrorResponse.class));
        }
    }

    public JsonNode getResource(String path) throws IOException, ChinoApiException {
        Request request = new Request.Builder()
                .url(hostUrl + path)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        if (response.code() == 200) {
            return mapper.readTree(body).get("data");
        } else {
            throw new ChinoApiException(mapper.readValue(body, ErrorResponse.class));
        }
    }

    public JsonNode putResource(String path, Object resource) throws IOException, ChinoApiException {
        String json = mapper.writeValueAsString(resource);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(hostUrl+path)
                .put(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        if (response.code() == 200) {
            return mapper.readTree(body).get("data");
        } else {
            throw new ChinoApiException(mapper.readValue(body, ErrorResponse.class));
        }
    }

    public JsonNode patchResource(String path, Object resource) throws IOException, ChinoApiException {
        String json = mapper.writeValueAsString(resource);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(hostUrl+path)
                .patch(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        if (response.code() == 200) {
            return mapper.readTree(body).get("data");
        } else {
            throw new ChinoApiException(mapper.readValue(body, ErrorResponse.class));
        }
    }

    public JsonNode putResource(String path, byte[] resource, int offset, int length) throws IOException, ChinoApiException {
        RequestBody requestBody = RequestBody.create(JSON, resource);
        Request request = new Request.Builder()
                .url(hostUrl+path)
                .header("offset", String.valueOf(offset))
                .header("length", String.valueOf(length))
                .put(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        if (response.code() == 200) {
            return mapper.readTree(body).get("data");
        } else {
            throw new ChinoApiException(mapper.readValue(body, ErrorResponse.class));
        }
    }

    public String deleteResource(String path, boolean force) throws IOException, ChinoApiException {
        Request request;
        if(force)
            request = new Request.Builder().url(hostUrl + path+"?force=true").delete().build();
        else
            request = new Request.Builder().url(hostUrl + path).delete().build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        if (response.code() == 200) {
            return "success";
        } else {
            throw new ChinoApiException(mapper.readValue(body, ErrorResponse.class));
        }
    }

    //This function is static because there are some classes in io.chino.api that needs a mapper without instantiating a new ChinoBaseAPI()
    public static ObjectMapper getMapper(){
        if(mapper==null) {
            mapper = new ObjectMapper();
            /*mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
            mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);*/
        }
        return mapper;
    }

    /*
        This is a function used in both Schema and UserSchema. It's used when a class is passed in their constructor to create or update a new
        Schema or UserSchema, and all the Fields in such class are checked. This function returns a String of the type of the Field passed.
    */
    protected String checkType(Class type) throws ChinoApiException {
        if(type==String.class){
            return "string";
        } else if (type==int.class){
            return "integer";
        } else if (type==boolean.class){
            return "boolean";
        } else if (type==float.class){
            return "float";
        } else if (type == Date.class || type == java.sql.Date.class){
            return "date";
        } else if (type == Time.class){
            return "time";
        } else if (type == Timestamp.class || type == java.security.Timestamp.class){
            return "datetime";
        } else if (type == File.class){
            return "blob";
        } else {
            //throw new ChinoApiException("error, invalid type: "+type+".");
            return null;
        }
    }

    protected HashMap<String, Object> fromStringToHashMap(String value){
        HashMap<String, Object> map = new HashMap<String, Object>();
        value = value.replaceAll("\\s", "");
        value = value.replaceAll("\"", "");
        String[] pairs = value.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            //To check if it's a digit
            if(keyValue[1].matches("^-?\\d+$"))
                map.put(keyValue[0], Integer.parseInt(keyValue[1]));
                //To check if it is a double
            else if(keyValue[1].matches("^[-+]?[0-9]*\\.?[0-9]+$"))
                map.put(keyValue[0], Double.parseDouble(keyValue[1]));
            else if (keyValue[1].equals("true"))
                map.put(keyValue[0], true);
            else if (keyValue[1].equals("false"))
                map.put(keyValue[0], false);
            else
                map.put(keyValue[0], keyValue[1]);
        }
        return map;
    }
}
