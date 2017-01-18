package io.chino.android;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.common.ChinoApiConstants;
import io.chino.api.common.ChinoApiException;
import io.chino.api.permission.GetPermissionsResponse;
import io.chino.api.permission.PermissionRule;
import io.chino.api.permission.PermissionRuleCreatedDocument;
import okhttp3.OkHttpClient;
import java.io.IOException;

public class Permissions extends ChinoBaseAPI {
    public Permissions(String hostUrl, OkHttpClient clientInitialized){
        super(hostUrl, clientInitialized);
    }

    /**
     * Used to read all Permissions
     * @param offset the offset
     * @return a GetPermissionsResponse Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetPermissionsResponse readPermissions(int offset) throws IOException, ChinoApiException {
        JsonNode data = getResource("/perms", offset, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetPermissionsResponse.class);

        return null;
    }

    /**
     * Used to read Permissions on a Document
     * @param documentId the id of the Document
     * @param offset the offset
     * @return a GetPermissionsResponse Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetPermissionsResponse readPermissionsOnaDocument(String documentId, int offset) throws IOException, ChinoApiException {
        JsonNode data = getResource("/perms/documents/"+documentId, offset, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null) {
            return mapper.convertValue(data, GetPermissionsResponse.class);
        }
        return null;
    }

    /**
     * Used to read Permissions of a User
     * @param userId the id of the User
     * @param offset the offset
     * @return a GetPermissionsResponse Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetPermissionsResponse readPermissionsOfaUser(String userId, int offset) throws IOException, ChinoApiException{
        JsonNode data = getResource("/perms/users/"+userId, offset, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetPermissionsResponse.class);

        return null;
    }

    /**
     * Used to read Permissions of a Group
     * @param groupId the id of the Group
     * @param offset the offset
     * @return a GetPermissionsResponse Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetPermissionsResponse readPermissionsOfaGroup(String groupId, int offset) throws IOException, ChinoApiException{
        JsonNode data = getResource("/perms/groups/"+groupId, offset, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetPermissionsResponse.class);

        return null;
    }

    /**
     * Used to create Permissions on Resources
     * @param action a String that specifies the action
     * @param resourceType the type of the Resources
     * @param subjectType the type of the Subject
     * @param subjectId the id of the Subject
     * @param permissionRule the PermissionRule Object
     * @return a String that represents the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String permissionsOnResources(String action, String resourceType, String subjectType, String subjectId, PermissionRule permissionRule) throws IOException, ChinoApiException {
        postResource("/perms/"+action+"/"+resourceType+"/"+subjectType+"/"+subjectId, permissionRule);
        return "success";
    }

    /**
     * Used to create Permissions on a Resource
     * @param action a String that specifies the action
     * @param resourceType the type of the Resource
     * @param resourceId the id of the Resource
     * @param subjectType the type of the Subject
     * @param subjectId the id of the Subject
     * @param permissionRule the PermissionRule Object
     * @return a String that represents the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String permissionsOnaResource(String action, String resourceType, String resourceId, String subjectType, String subjectId, PermissionRule permissionRule) throws IOException, ChinoApiException {
        postResource("/perms/"+action+"/"+resourceType+"/"+resourceId+"/"+subjectType+"/"+subjectId, permissionRule);
        return "success";
    }

    /**
     * Used to create Permissions on a Resource Children
     * @param action a String that specifies the action
     * @param resourceType the type of the Resource
     * @param resourceId the id of the Resource
     * @param resourceChildren the type of the ResourceChildren
     * @param subjectType the type of the Subject
     * @param subjectId the id of the Subject
     * @param permissionRule the PermissionRule Object
     * @return a String that represents the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String permissionsOnResourceChildren(String action, String resourceType, String resourceId, String resourceChildren, String subjectType, String subjectId, PermissionRule permissionRule) throws IOException, ChinoApiException {
        postResource("/perms/"+action+"/"+resourceType+"/"+resourceId+"/"+resourceChildren+"/"+subjectType+"/"+subjectId, permissionRule);
        return "success";
    }

    /**
     * Used to create Permissions on a Resource Children
     * @param action a String that specifies the action
     * @param resourceType the type of the Resource
     * @param resourceId the id of the Resource
     * @param resourceChildren the type of the ResourceChildren
     * @param subjectType the type of the Subject
     * @param subjectId the id of the Subject
     * @param permissionRule the PermissionRuleCreatedDocument Object
     * @return a String that represents the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String permissionsOnResourceChildren(String action, String resourceType, String resourceId, String resourceChildren, String subjectType, String subjectId, PermissionRuleCreatedDocument permissionRule) throws IOException, ChinoApiException {
        postResource("/perms/"+action+"/"+resourceType+"/"+resourceId+"/"+resourceChildren+"/"+subjectType+"/"+subjectId, permissionRule);
        return "success";
    }

}
