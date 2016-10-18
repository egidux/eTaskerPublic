package org.eTasker.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//Builds Json String from Object
public class JsonBuilder {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonBuilder.class);
    
	public static String build(Object obj) {
        try {
        	ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			LOGGER.debug("Failed create JSON string", e);
		}
        return null;
    }
}
