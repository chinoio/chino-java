package io.chino.client;

import io.chino.api.common.*;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

//This Class is extended by the other classes (except for ChinoAPI) because it has some variables and functions used by all classes
public class ChinoBaseAPI {
    static Client client;
    static String host;
    static ObjectMapper mapper;

    public ChinoBaseAPI(String hostUrl, Client clientInitialized){
        client = clientInitialized;
        host = hostUrl;
        initializeMapper();
    }

    //This function is static because there are some classes in io.chino.api that needs a mapper without instantiating a new ChinoBaseAPI()
    public static ObjectMapper getMapper(){
        if(mapper==null) {
            mapper = new ObjectMapper();
            mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
            mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        }
        return mapper;
    }

    //Those below are all the functions needed to GET, POST, PUT or DELETE a resource
    public JsonNode getResource(String path, int offset, int limit) throws IOException, ChinoApiException {
        Response response= client.target(host).path(path).queryParam("offset", offset).queryParam("limit", limit).request().get();

        if(response.getStatus()==200){
            return mapper.readTree(response.readEntity(String.class)).get("data");
        }else{
            throw new ChinoApiException(mapper.readValue(response.readEntity(String.class), ErrorResponse.class));
        }
    }


    public JsonNode postResource(String path, Object resource) throws IOException, ChinoApiException {
        Response response = client.target(host).path(path).request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(mapper.writeValueAsString(resource)));

        if(response.getStatus()==200){
            return mapper.readTree(response.readEntity(String.class)).get("data");
        }else{
            throw new ChinoApiException(mapper.readValue(response.readEntity(String.class), ErrorResponse.class));
        }
    }

    public JsonNode putResource(String path, Object resource) throws IOException, ChinoApiException {

        Response response = client.target(host).path(path).request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(mapper.writeValueAsString(resource)));

        if(response.getStatus()==200){
            return mapper.readTree(response.readEntity(String.class)).get("data");
        }else{
            throw new ChinoApiException(mapper.readValue(response.readEntity(String.class), ErrorResponse.class));
        }
    }


    public String deleteResource(String path, boolean force) throws IOException, ChinoApiException {
        Response response;

        if(force){
            response = client.target(host).path(path).queryParam("force", true).request().delete();
        }else{
            response = client.target(host).path(path).request().delete();
        }

        if(response.getStatus()==200){
            return mapper.readValue(response.readEntity(String.class), DeleteResponse.class).getResult();
        }else{
            throw new ChinoApiException(mapper.readValue(response.readEntity(String.class), ErrorResponse.class));
        }
    }

    //The mapper is created once, and than used by all classes
    private void initializeMapper(){
        mapper = new ObjectMapper();
        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false); // in case you want to serialize a JsonNode to String
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
            throw new ChinoApiException("error, invalid type: "+type+".");
        }
    }
}
