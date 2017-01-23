package io.chino.java;

import io.chino.api.common.*;
import okhttp3.*;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.*;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ChinoBaseAPI {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType OCTET_STREAM = MediaType.parse("application/octet-stream");
    String hostUrl;
    OkHttpClient client;
    LoggingInterceptor interceptor;
    static ObjectMapper mapper;

    /**
     * The default constructor used by almost all classes
     * @param hostUrl the url of the server
     * @param client the OkHttpClient Object used for the calls to the server
     */
    public ChinoBaseAPI(String hostUrl, OkHttpClient client){
        this.hostUrl = hostUrl;
        this.client = client;
        mapper = getMapper();
    }

    /**
     * The constructor for the Auth class because it needs to access the same interceptor as ChinoAPI for the authentication
     * @param hostUrl the url of the server
     * @param client the OkHttpClient Object used for the calls to the server
     * @param interceptor the LoggingInterceptor used by ChinoAPI too
     */
    public ChinoBaseAPI(String hostUrl, OkHttpClient client, LoggingInterceptor interceptor){
        this.hostUrl = hostUrl;
        this.client = client;
        this.interceptor = interceptor;
        mapper = getMapper();
    }

    /**
     * The default function to make a POST call to the server saved in hostUrl
     * @param path the path of the URL
     * @param resource the Object that would be mapped in a JSON format for the request
     * @return JsonNode Object with the response of the server if there are no errors
     * @throws IOException
     * @throws ChinoApiException
     */
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

    /**
     * It makes a POST call to the server saved in hostUrl
     * @param path the path of the URL
     * @param resource the Object that would be mapped in a JSON format for the request
     * @param offset the offset value in the request
     * @param limit the limit value in the request
     * @return JsonNode Object with the response of the server if there are no errors
     * @throws IOException
     * @throws ChinoApiException
     */
    public JsonNode postResource(String path, Object resource, int offset, int limit) throws IOException, ChinoApiException {
        String json = mapper.writeValueAsString(resource);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(hostUrl+path+"?offset="+offset+"&limit="+limit)
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

    /**
     * The default function to make a GET call to the server saved in hostUrl
     * @param path the path of the URL
     * @param offset the offset value in the request
     * @param limit the limit value in the request
     * @return JsonNode Object with the response of the server if there are no errors
     * @throws IOException
     * @throws ChinoApiException
     */
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

    /**
     * It makes a GET call to the server saved in hostUrl
     * @param path the path of the URL
     * @return JsonNode Object with the response of the server if there are no errors
     * @throws IOException
     * @throws ChinoApiException
     */
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

    /**
     * The default function to make a PUT call to the server saved in hostUrl
     * @param path the path of the URL
     * @param resource the Object that would be mapped in a JSON format for the request
     * @return JsonNode Object with the response of the server if there are no errors
     * @throws IOException
     * @throws ChinoApiException
     */
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

    /**
     * The default function to make a PATCH call to the server saved in hostUrl
     * @param path the path of the URL
     * @param resource the Object that would be mapped in a JSON format for the request
     * @return JsonNode Object with the response of the server if there are no errors
     * @throws IOException
     * @throws ChinoApiException
     */
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

    /**
     * The function used by Blob class to make a PUT call to upload the chunks
     * @param path the path of the URL
     * @param resource the byte array of the chunk which needs to be uploaded
     * @param offset the offset value in the request
     * @param length the length value in the request
     * @return JsonNode Object with the response of the server if there are no errors
     * @throws IOException
     * @throws ChinoApiException
     */
    public JsonNode putResource(String path, byte[] resource, int offset, int length) throws IOException, ChinoApiException {
        RequestBody requestBody = RequestBody.create(OCTET_STREAM, resource);

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

    /**
     * The default function to make a DELETE call to the server saved in hostUrl
     * @param path the path of the URL
     * @param force the force parameter in the request
     * @return String with the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
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
        }
        return mapper;
    }

    /*
        This is a function used in both Schema and UserSchema. It is used when a custom class is passed in the constructor to create or update a new
        Schema or UserSchema, and all the Fields in such class are checked. This function returns a String of the type of the Field passed.
    */
    protected String checkType(Class type) throws ChinoApiException {
        if(type==String.class){
            return "string";
        } else if (type==int.class || type == Integer.class){
            return "integer";
        } else if (type==boolean.class || type == Boolean.class){
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
            throw new ChinoApiException("error, invalid type: " + type + ".");
        }
    }

    //Used for the Reflection operation when a custom class is passed as argument to create or update a Schema or a UserSchema to retrieve the fields in the class
    protected List<Field> returnFields(Class myClass) throws ChinoApiException{
        checkNotNull(myClass, "my_class");
        java.lang.reflect.Field[] fields = myClass.getDeclaredFields();
        List<Field> fieldsList= new ArrayList<Field>();
        for(java.lang.reflect.Field field : fields){
            String temp = checkType(field.getType());
            if(temp!=null) {
                if(field.getAnnotation(indexed.class)!=null){
                    fieldsList.add(new Field(field.getName(), temp, true));
                } else {
                    fieldsList.add(new Field(field.getName(), temp));
                }
            }
        }
        return fieldsList;
    }

    //Function used to convert a String to a HashMap
    protected HashMap<String, Object> fromStringToHashMap(String value){
        checkNotNull(value, "content/attributes");
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

    protected void checkNotNull(Object object, String name){
        if(object == null){
            throw new NullPointerException(name);
        }
    }
}
