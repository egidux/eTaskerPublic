package org.eTasker.tool;

import java.util.List;

import org.eTasker.model.Worker;
import org.json.JSONArray;
import org.json.JSONObject;
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
	
	public static String datatable(List<?> list) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("draw", 1);
		jsonObject.put("recordsTotal", list.size());
		jsonObject.put("recordsFiltered", list.size());
		JSONArray jsonArray = new JSONArray();
		jsonObject.put("data", jsonArray);
		if (list.isEmpty()) {
			LOGGER.info(jsonObject.toString());
			return jsonObject.toString();
		}
		if (list.get(0) instanceof Worker) {
			for (int i = 0; i < list.size(); i++) {
				Worker worker = (Worker)list.get(i);
				JSONArray tempArray = new JSONArray();
				tempArray.put(worker.getId());
				tempArray.put(worker.getName());
				tempArray.put(worker.getEmail());
				jsonArray.put(tempArray);
			}
		}
		LOGGER.info(jsonObject.toString());
		return jsonObject.toString();
    }
}
