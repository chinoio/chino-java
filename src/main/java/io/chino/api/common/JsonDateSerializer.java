package io.chino.api.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class JsonDateSerializer extends JsonSerializer<Date> {

//	private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"); //yyyy-MM-dd'T'HH:mm:ss.SSSZ

//	private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSSZ"); //HH:mm:ss.SSSZ

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //yyyy-MM-dd

	
	@Override
	public void serialize(Date date, JsonGenerator gen, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		String formattedDate = dateFormat.format(date);

		gen.writeString(formattedDate);
	}
	
	
//	public getDate()

}