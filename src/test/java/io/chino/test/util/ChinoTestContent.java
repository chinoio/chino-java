package io.chino.test.util;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.chino.java.ChinoBaseAPI;

import io.chino.api.auth.LoggedUser;
import io.chino.api.document.Document;
import io.chino.api.document.GetDocumentsResponse;
import io.chino.api.group.Group;
import io.chino.api.repository.Repository;
import io.chino.api.schema.Schema;
import io.chino.api.user.User;
import io.chino.api.userschema.UserSchema;

public class ChinoTestContent {
	private static final Integer UUIDLen=36;
	
	public static boolean validLoggedUser(LoggedUser loggedUser) {
		if(loggedUser==null)
			return false;
		if(!validUUID(loggedUser.getAccessToken()))
			return false;
		if(loggedUser.getExpiresIn()!=1800)
			return false;
		if(!validUUID(loggedUser.getRefreshToken()))
			return false;
		if(loggedUser.getScope()==null)
			return false;
		if(loggedUser.getTokenType()==null)
			return false;

		return true;
	}

	public static boolean validUser(User user) {
		if(user==null)
			return false;
		if(!validUUID(user.getUserId()))
			return false;
		if(!validDates(user.getInsertDate(), user.getLastUpdate()))
			return false;
		for (String group : user.getGroups()) {
			if(!validUUID(group))
				return false;
		}
		if(user.getIsActive()==null)
			return false;
		/*
		if(user.getAttributes()!=null){
			if(!isValidJSON(user.getAttributes())){
				return false;
			}
		}
		*/
		return true;
	}

	
	public static boolean validGroup(Group group) {
		if(group==null)
			return false;
		if(!validUUID(group.getGroupId()))
			return false;
		if(!validDates(group.getInsertDate(), group.getLastUpdate()))
			return false;
		if(group.getIsActive()==null)
			return false;
		if((group.getGroupName()==null)||(group.getGroupName().length()<1))
			return false;
		
		if(group.getAttributes()!=null){
			if(!isValidJSON(group.getAttributes())){
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean validRepository(Repository repo) {
		if(repo==null)
			return false;
		if(!validUUID(repo.getRepositoryId()))
			return false;
		if(!validDates(repo.getInsertDate(), repo.getLastUpdate()))
			return false;
		if(repo.getIsActive()==null)
			return false;
		if((repo.getDescription()==null)||(repo.getDescription().length()<1))
			return false;
		
		return true;
	}
	
	public static boolean validDocument(Document document) {
		if(document==null)
			return false;
		if(!validUUID(document.getDocumentId()))
			return false;
		if(!validUUID(document.getRepositoryId()))
			return false;
		if(!validUUID(document.getSchemaId()))
			return false;
		if(!validDates(document.getInsertDate(), document.getLastUpdate()))
			return false;
		
		if(document.getContent()!=null){
			if(!isValidJSON(document.getContent())){
				return false;
			}
		}
		
		
		return true;
	}

	public static boolean validSchema(Schema schema) {
		if(schema==null)
			return false;
		if(!validUUID(schema.getRepositoryId()))
			return false;
		if(!validUUID(schema.getSchemaId()))
			return false;
		if(!validDates(schema.getInsertDate(), schema.getLastUpdate()))
			return false;
		
		if(schema.getStructure()==null)
			return false;
		if(schema.getStructure().getFields()==null)
			return false;
		if(schema.getStructure().getFields().size()==0)
			return false;
		
		return true;
	}

	public static boolean validUserSchema(UserSchema userSchema) {
		if(userSchema==null)
			return false;
		if(!validUUID(userSchema.getUserSchemaId()))
			return false;
		if(!validDates(userSchema.getInsertDate(), userSchema.getLastUpdate()))
			return false;
		if(userSchema.getStructure()==null)
			return false;
		if(userSchema.getStructure().getFields()==null)
			return false;
		if(userSchema.getStructure().getFields().size()==0)
			return false;
		
		return true;
	}
	
	
	public static boolean validGetDocumentsResponse(GetDocumentsResponse getDocumentsResponse) {
		if(getDocumentsResponse==null)
			return false;
		
		int documentsCurrentOffset=0;
		int documentsCurrentCount=0;
		int documentsTotalCount=Integer.MAX_VALUE;
		while(documentsTotalCount>documentsCurrentOffset){
			documentsTotalCount=getDocumentsResponse.getTotalCount();
//			System.out.println("\t\t documentsCurrentOffset: "+documentsCurrentOffset+" totalCount: "+getDocumentsResponse.getTotalCount());
	
			for (Document doc : getDocumentsResponse.getDocuments()) {
//				System.out.println("\t\tDoc n:"+documentsCurrentCount+" id: "+doc.toString());
				documentsCurrentCount++;
				
				if(!validDocument(doc))
					return false;
			}
			
			documentsCurrentOffset+=getDocumentsResponse.getLimit();
		}

	return true;	
	}
	
	
	
	public static boolean validUUID(String string){
		if(string==null)
			return false;
		if(string.length()!=UUIDLen)
			return false;
		
		return true;
	}
	
	public static boolean validDates(Date insertDate, Date updateDate ){
		
		if(insertDate==null)
			return false;
		if(insertDate.after(new Date(2015,1,1)))
			return false;
		if(updateDate==null)
			return false;
		if(updateDate.after(new Date(2015,1,1)))
			return false;
		if(!updateDate.equals(insertDate))
			if(updateDate.before(insertDate))
				return false;	
		
		return true;
	}

	public static boolean isValidJSON(final JsonNode json) {
		String jsonString;
		try {
			jsonString = ChinoBaseAPI.getMapper().writeValueAsString(json);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		boolean valid = false;
		try {
			final JsonParser parser = new ObjectMapper().getJsonFactory().createJsonParser(jsonString);
			while (parser.nextToken() != null) {
			}
			valid = true;
		} catch (JsonParseException jpe) {
			jpe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return valid;
	}
}
