package com.givee.demo.server.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtil {
	private static final Logger log = LoggerFactory.getLogger(StringUtil.class);

	public static String abbreviate(String str, int maxWidth) {
		return abbreviate(str, 0, maxWidth);
	}

	public static String abbreviate(String str, int offset, int maxWidth) {
		if (str == null) {
			return null;
		} else if (maxWidth < 4) {
			throw new IllegalArgumentException("Minimum abbreviation width is 4");
		} else if (str.length() <= maxWidth) {
			return str;
		} else {
			if (offset > str.length()) offset = str.length();
			if (str.length() - offset < maxWidth - 3) offset = str.length() - (maxWidth - 3);
			String abrevMarker = "...";
			if (offset <= 4) {
				return str.substring(0, maxWidth - 3) + abrevMarker;
			} else if (maxWidth < 7) {
				throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
			} else {
				return offset + maxWidth - 3 < str.length() ?
						abrevMarker + abbreviate(str.substring(offset), maxWidth - 3) :
						abrevMarker + str.substring(str.length() - (maxWidth - 3));
			}
		}
	}

	public static String splitString(String str, int maxWidth) {
		String result = "";
		StringBuilder builder = new StringBuilder();
		int count = 0;
		for (char character : str.toCharArray()) {
			if (count < maxWidth - 1) {
				builder.append(character);
				count++;
			} else {
				if (character == ' ') {
					builder.append(character);
					result = result.concat(builder.toString()).concat("\n");
					builder.setLength(0);
					count = 0;
				} else {
					builder.append(character);
					count++;
				}
			}
		}
		result = result.concat(builder.toString()).concat("\n");
		builder.setLength(0);
		return result;
	}

	public static Boolean isNull(String s) {
		return s == null || s.trim().isEmpty() || s.equalsIgnoreCase("null");
	}

	public static String writeValueAsString(Object value) {
		if (value == null) return "";
		ObjectMapper objectMapper = BeanUtil.getBean(ObjectMapper.class);
		try {
			return objectMapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
			return "";
		}
	}

	public static <T> T writeStringAsValue(String value, Class<T> type) {
		if (isNull(value)) return null;
		ObjectMapper objectMapper = BeanUtil.getBean(ObjectMapper.class);
		try {
			return objectMapper.readValue(value, type);
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
	}

	public static <T> T writeStringAsFirstValue(String value, Class<T[]> type) {
		if (isNull(value)) return null;
		List<T> array = writeStringAsArray(value, type);
		if (array.isEmpty()) return null;
		else return array.get(0);
	}

	public static <T> List<T> writeStringAsArray(String value, Class<T[]> type) {
		if (isNull(value)) return Collections.emptyList();
		ObjectMapper objectMapper = BeanUtil.getBean(ObjectMapper.class);
		try {
			T[] result = objectMapper.readValue(value, type);
			if (result.length > 0) {
				return Arrays.asList(result);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return Collections.emptyList();
	}
}
