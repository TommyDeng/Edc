package com.edc.utils;

import com.edc.common.bo.EventMessage;
import com.google.gson.Gson;

public class EdcStringUtils {
	public static String toJsonString(EventMessage<?> outContent) {
		Gson gson = new Gson();
		return gson.toJson(outContent);
	}
}
