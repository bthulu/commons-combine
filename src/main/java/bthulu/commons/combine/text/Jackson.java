package bthulu.commons.combine.text;

import bthulu.commons.combine.exception.ExceptionUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

/**
 * @author gejian at 2018/9/1 10:01
 */
public class Jackson {

	private static volatile ObjectMapper mapper;

	public static ObjectMapper getMapper() {
		if (mapper == null) {
			synchronized (Jackson.class) {
				if (mapper == null) {
					ObjectMapper mp = new ObjectMapper();
					mp.setTimeZone(TimeZone.getDefault());
					try {
						Class.forName("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule");
						mp.registerModule(new JavaTimeModule());
					} catch (ClassNotFoundException ignored) {
					}
					try {
						Class.forName("com.fasterxml.jackson.datatype.jdk8.Jdk8Module");
						mp.registerModule(new Jdk8Module());
					} catch (ClassNotFoundException ignored) {
					}
					mp.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
					mapper = mp;
				}
			}
		}
		return mapper;
	}

	public static void setMapper(ObjectMapper mapper) {
		Jackson.mapper = mapper;
	}

	// API like fastjson

	public static <T> T parseObject(String content, Class<T> clazz) {
		return readValue(content, clazz);
	}

	public static <T> T parseObject(String content, Type type) {
		return readValue(content, getJavaType(type));
	}

	private static JavaType getJavaType(Type type) {
		return getMapper().getTypeFactory().constructType(type);
	}

	public static <T> T parseObject(String content, TypeReference type) {
		return readValue(content, type);
	}

	public static <T> T parseObject(byte[] content, Class<T> clazz) {
		return readValue(content, clazz);
	}

	public static <T> T parseObject(byte[] content, Type type) {
		return readValue(content, getJavaType(type));
	}

	public static <T> T parseObject(byte[] content, TypeReference type) {
		return readValue(content, type);
	}

	public static <T> List<T> parseArray(String text, Class<T[]> clazz) {
		return Arrays.asList(readValue(text, clazz));
	}

	public static String toJSONString(Object object) {
		return writeValueAsString(object);
	}

	public static byte[] toJSONBytes(Object object) {
		return writeValueAsBytes(object);
	}

	// mapping from json to Java types

	public static JsonNode readTree(InputStream in) {
		try {
			return getMapper().readTree(in);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static JsonNode readTree(Reader r) {
		try {
			return getMapper().readTree(r);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static JsonNode readTree(String content) {
		try {
			return getMapper().readTree(content);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static JsonNode readTree(byte[] content) {
		try {
			return getMapper().readTree(content);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static JsonNode readTree(File file) {
		try {
			return getMapper().readTree(file);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static JsonNode readTree(URL source) {
		try {
			return getMapper().readTree(source);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T extends TreeNode> T readTree(JsonParser p) {
		try {
			return getMapper().readTree(p);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(JsonParser p, Class<T> valueType) {
		try {
			return getMapper().readValue(p, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(JsonParser p, TypeReference<?> valueTypeRef) {
		try {
			return getMapper().readValue(p, valueTypeRef);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(JsonParser p, ResolvedType valueType) {
		try {
			return getMapper().readValue(p, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(JsonParser p, JavaType valueType) {
		try {
			return getMapper().readValue(p, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(File src, Class<T> valueType) {
		try {
			return getMapper().readValue(src, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(File src, TypeReference valueTypeRef) {
		try {
			return getMapper().readValue(src, valueTypeRef);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(File src, JavaType valueType) {
		try {
			return getMapper().readValue(src, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(URL src, Class<T> valueType) {
		try {
			return getMapper().readValue(src, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(URL src, TypeReference valueTypeRef) {
		try {
			return getMapper().readValue(src, valueTypeRef);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(URL src, JavaType valueType) {
		try {
			return getMapper().readValue(src, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(String content, Class<T> valueType) {
		try {
			return getMapper().readValue(content, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(String content, TypeReference valueTypeRef) {
		try {
			return getMapper().readValue(content, valueTypeRef);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(String content, JavaType valueType) {
		try {
			return getMapper().readValue(content, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(Reader src, Class<T> valueType) {
		try {
			return getMapper().readValue(src, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(Reader src, TypeReference valueTypeRef) {
		try {
			return getMapper().readValue(src, valueTypeRef);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(Reader src, JavaType valueType) {
		try {
			return getMapper().readValue(src, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(InputStream src, Class<T> valueType) {
		try {
			return getMapper().readValue(src, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(InputStream src, TypeReference valueTypeRef) {
		try {
			return getMapper().readValue(src, valueTypeRef);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(InputStream src, JavaType valueType) {
		try {
			return getMapper().readValue(src, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(byte[] src, Class<T> valueType) {
		try {
			return getMapper().readValue(src, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(byte[] src, int offset, int len, Class<T> valueType) {
		try {
			return getMapper().readValue(src, offset, len, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(byte[] src, TypeReference valueTypeRef) {
		try {
			return getMapper().readValue(src, valueTypeRef);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(byte[] src, int offset, int len, TypeReference valueTypeRef) {
		try {
			return getMapper().readValue(src, offset, len, valueTypeRef);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(byte[] src, JavaType valueType) {
		try {
			return getMapper().readValue(src, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(byte[] src, int offset, int len, JavaType valueType) {
		try {
			return getMapper().readValue(src, offset, len, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(DataInput src, Class<T> valueType) {
		try {
			return getMapper().readValue(src, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T readValue(DataInput src, JavaType valueType) {
		try {
			return getMapper().readValue(src, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> MappingIterator<T> readValues(JsonParser p, ResolvedType valueType) {
		try {
			return getMapper().readValues(p, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> MappingIterator<T> readValues(JsonParser p, JavaType valueType) {
		try {
			return getMapper().readValues(p, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> MappingIterator<T> readValues(JsonParser p, Class<T> valueType) {
		try {
			return getMapper().readValues(p, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> MappingIterator<T> readValues(JsonParser p, TypeReference<?> valueTypeRef) {
		try {
			return getMapper().readValues(p, valueTypeRef);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	// Public API (from TreeCodec via ObjectCodec): Tree Model support

	public static void writeTree(JsonGenerator jgen, TreeNode rootNode) {
		try {
			getMapper().writeTree(jgen, rootNode);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static void writeTree(JsonGenerator jgen, JsonNode rootNode) {
		try {
			getMapper().writeTree(jgen, rootNode);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static <T> T treeToValue(TreeNode n, Class<T> valueType) {
		try {
			return getMapper().treeToValue(n, valueType);
		} catch (JsonProcessingException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	// mapping from Java types to Json
	public static void writeValue(JsonGenerator g, Object value) {
		try {
			getMapper().writeValue(g, value);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static void writeValue(File resultFile, Object value) {
		try {
			getMapper().writeValue(resultFile, value);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static void writeValue(OutputStream out, Object value) {
		try {
			getMapper().writeValue(out, value);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static void writeValue(DataOutput out, Object value) {
		try {
			getMapper().writeValue(out, value);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static void writeValue(Writer w, Object value) {
		try {
			getMapper().writeValue(w, value);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public static byte[] writeValueAsBytes(Object value) {
		try {
			return getMapper().writeValueAsBytes(value);
		} catch (JsonProcessingException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}


	public static String writeValueAsString(Object value) {
		try {
			return getMapper().writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

}
