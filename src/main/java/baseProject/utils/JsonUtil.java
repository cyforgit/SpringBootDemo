package baseProject.utils;

import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class JsonUtil {
	private static Logger logger = LogUtil.getLogger();
	private static ObjectMapper mapper = new ObjectMapper();
	private static XmlMapper xmlMapper = new XmlMapper();

	static {
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.setSerializationInclusion(Include.NON_NULL);
		xmlMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		xmlMapper.setSerializationInclusion(Include.NON_NULL);
	}

	/**
	 * 对象转json
	 * 
	 * @param jsonString
	 * @param c
	 * @return
	 */
	public static Object getObjectFromJson(String jsonString, Class<?> c) {
		try {
			return mapper.readValue(jsonString, c);
		} catch (Exception e) {
			logger.info("Get Object From Json Error:" + e.toString());
			return null;
		}
	}

	/**
	 * 将对象转为JSON字符串
	 * 
	 * @param object
	 * @return
	 */
	public static String getJsonFromObject(Object object) {
		String jsonString;
		// 美化输出，仅调试使用
		// mapper.enable(SerializationFeature.INDENT_OUTPUT);
		// 允许输出空对象

		try {
			jsonString = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			jsonString = null;
			logger.warn(e.toString());
		}
		return jsonString;
	}

	/**
	 * 将对象转换为XML字符串
	 * 
	 * @param object
	 * @return
	 */
	public static String getXMLFromObject(Object object) {
		String xmlString;
		try {
			xmlString = xmlMapper.writeValueAsString(object);
		} catch (Exception e) {
			xmlString = null;
		}
		return xmlString;
	}

	public static Object getObjectFromXML(String XMLString, Class<?> c) {
		try {
			return xmlMapper.convertValue(XMLString, c);
		} catch (Exception e) {
			return null;
		}
	}
}
