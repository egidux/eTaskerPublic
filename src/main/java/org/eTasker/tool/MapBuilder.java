package org.eTasker.tool;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder {

	public static Map<String, String> build(String key, String value) {
		Map<String, String> map =  new HashMap<>();
		map.put(key, value);
		return map;
	}
}
