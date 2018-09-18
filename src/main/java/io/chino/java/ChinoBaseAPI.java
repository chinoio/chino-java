package io.chino.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.chino.api.common.ChinoApiException;
import io.chino.api.common.ErrorResponse;
import io.chino.api.common.Field;
import io.chino.api.common.indexed;
import javafx.util.Pair;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    private final ChinoAPI parent;
    private final String hostUrl;
    static ObjectMapper mapper;


    /**
     * The default constructor used by all {@link ChinoBaseAPI} subclasses
     *
     * @param baseApiUrl the base URL of the Chino.io API. For testing, use:<br>
     *     {@code https://api.test.chino.io/v1/}
     * @param parentApiClient the instance of {@link ChinoAPI} that created this object
     */
    public ChinoBaseAPI(String baseApiUrl, ChinoAPI parentApiClient) {
        this.hostUrl = baseApiUrl;
        parent = parentApiClient;
        mapper = getMapper();
    }

    /**
     * The default function to make a POST call to the server saved in hostUrl
     * @param path the path of the URL
     * @param resource the Object that would be mapped in a JSON format for the request
     * @return JsonNode Object with the response of the server if there are no errors
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public JsonNode postResource(String path, Object resource) throws IOException, ChinoApiException {
        String json = mapper.writeValueAsString(resource);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(hostUrl + path)
                .post(requestBody)
                .build();
        Response response = parent.getHttpClient().newCall(request).execute();
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
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public JsonNode postResource(String path, Object resource, int offset, int limit) throws IOException, ChinoApiException {
        String json = mapper.writeValueAsString(resource);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(hostUrl+path+"?offset="+offset+"&limit="+limit)
                .post(requestBody)
                .build();
        Response response = parent.getHttpClient().newCall(request).execute();
        String body = response.body().string();
        if (response.code() == 200) {
            return mapper.readTree(body).get("data");
        } else {
            throw new ChinoApiException(mapper.readValue(body, ErrorResponse.class));
        }
    }

    /**
     * The default function to make a GET call to the server saved in hostUrl.
     * This is the default method to get paginated results.
     * @param path the path of the URL
     * @param offset the offset value in the request
     * @param limit the limit value in the request
     * @return JsonNode Object with the response of the server if there are no errors
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public JsonNode getResource(String path, int offset, int limit) throws IOException, ChinoApiException {
        Request request = new Request.Builder()
                .url(hostUrl + path+"?offset="+offset+"&limit="+limit)
                .get()
                .build();
        Response response = parent.getHttpClient().newCall(request).execute();
        String body = response.body().string();
        if (response.code() == 200) {
            return mapper.readTree(body).get("data");
        } else {
            throw new ChinoApiException(mapper.readValue(body, ErrorResponse.class));
        }
    }

    
    /**
     * The default function to make a GET call to the server saved in hostUrl.
     * This call allows to pass any value as a URL parameter with the call.
     * @param path the path of the URL
     * @param urlParameters a <code>&lt;</code><code>key, value</code><code>&gt;</code>
     * {@link HashMap} containing the URL parameters.
     * @return JsonNode Object with the response of the server if there are no errors
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public JsonNode getResource(String path, HashMap<String, String> urlParameters) throws IOException, ChinoApiException {
        // parse parameters
        String paramString = "";
        if (!urlParameters.isEmpty()) {
            paramString = "?";
            int concatCounter = urlParameters.keySet().size() - 1;
            for (String paramName:urlParameters.keySet()) {
                String param = urlParameters.get(paramName);
                paramString += paramName + "=" + param;
                paramString += (concatCounter > 0) ? "&" : "";
                concatCounter --;
            }
        }
        // send GET request
        Request request = new Request.Builder()
                .url(hostUrl + path + paramString)
                .get()
                .build();
        Response response = parent.getHttpClient().newCall(request).execute();
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
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public JsonNode getResource(String path) throws IOException, ChinoApiException {
        Request request = new Request.Builder()
                .url(hostUrl + path)
                .get()
                .build();
        Response response = parent.getHttpClient().newCall(request).execute();
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
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public JsonNode putResource(String path, Object resource) throws IOException, ChinoApiException {
        String json = mapper.writeValueAsString(resource);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(hostUrl+path)
                .put(requestBody)
                .build();
        Response response = parent.getHttpClient().newCall(request).execute();
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
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public JsonNode patchResource(String path, Object resource) throws IOException, ChinoApiException {
        String json = mapper.writeValueAsString(resource);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(hostUrl+path)
                .patch(requestBody)
                .build();
        Response response = parent.getHttpClient().newCall(request).execute();
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
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public JsonNode putResource(String path, byte[] resource, int offset, int length) throws IOException, ChinoApiException {
        RequestBody requestBody = RequestBody.create(OCTET_STREAM, resource);

        Request request = new Request.Builder()
                .url(hostUrl+path)
                .header("offset", String.valueOf(offset))
                .header("length", String.valueOf(length))
                .put(requestBody)
                .build();

        Response response = parent.getHttpClient().newCall(request).execute();
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
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String deleteResource(String path, boolean force) throws IOException, ChinoApiException {
        Request request;
        if(force)
            request = new Request.Builder().url(hostUrl + path+"?force=true").delete().build();
        else
            request = new Request.Builder().url(hostUrl + path).delete().build();
        Response response = parent.getHttpClient().newCall(request).execute();
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

    protected HashMap<String, Object> fromStringToHashMap(String value) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        //convert JSON string to Map
        HashMap<String, Object> map = mapper.readValue(
                value,
                new TypeReference<HashMap<String, Object>>() {}
        );
        return map;
    }

    /**
     * Verify that the {@link Object} is not a {@code null} reference; otherwise
     * interrupts execution with an exception, specifying the name of the {@code null} Object.
     *
     * @param object an {@link Object}
     * @param name a name that identifies the Object for the dev
     *
     * @throws NullPointerException the object (whose name is reported in the Exception) is {@code null}.
     */
    protected void checkNotNull(Object object, String name){
        if(object == null){
            throw new NullPointerException(name);
        }
    }

    /**
     * Verify that every {@link Pair} in the varargs contains a non-{@code null} {@link Object} reference; otherwise
     * interrupts execution with an exception, specifying the name of the {@code null} Object.
     *
     * @param elements a list of {@link Pair} where the first element is an {@link Object}
     *                 and the second is a {@link String}.
     *
     * @see #checkNotNull(Object, String)
     *
     * @throws NullPointerException one of the objects (whose name is reported in the Exception) is {@code null}.
     */
    protected void checkNotNull(Pair<Object, String>... elements){
        for (Pair<Object, String> element : elements) {
            checkNotNull(element.getKey(), element.getValue());
        }
    }
}
