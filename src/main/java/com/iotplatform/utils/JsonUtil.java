
package com.iotplatform.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import java.io.IOException;
import java.util.List;

public class JsonUtil {

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();

        // 设置FAIL_ON_EMPTY_BEANS属�?�，当序列化空对象不要抛异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 设置FAIL_ON_UNKNOWN_PROPERTIES属�?�，当JSON字符串中存在Java对象没有的属性，忽略
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Convert Object to JsonString
     * 
     * @param jsonObj
     * @return
     */
    public static String jsonObj2Sting(Object jsonObj) {
        String jsonString = null;

        try {
            jsonString = objectMapper.writeValueAsString(jsonObj);
        } catch (IOException e) {
            System.out.printf("pasre json Object[{}] to string failed.",jsonString);
        }

        return jsonString;
    }

    /**
     * Convert JsonString to Simple Object
     * 
     * @param jsonString
     * @param cls
     * @return
     */
    public static <T> T jsonString2SimpleObj(String jsonString, Class<T> cls) {
        T jsonObj = null;

        try {
            jsonObj = objectMapper.readValue(jsonString, cls);
        } catch (IOException e) {
        	System.out.printf("pasre json Object[{}] to string failed.",jsonString);
        }

        return jsonObj;
    }
   
    /**
     * Method that will convert object to the ObjectNode.
     *
     *            the source data; if null, will return null.
     * @return the ObjectNode data after converted.
     */
    public static <T> ObjectNode convertObject2ObjectNode(T object)
            throws Exception {
        if (null == object) {
            return null;
        }

        ObjectNode objectNode = null;

        if (object instanceof String) {
            objectNode = convertJsonStringToObject((String) object,
                    ObjectNode.class);
        } else {
            objectNode = convertValue(object, ObjectNode.class);
        }

        return objectNode;
    }
    
    /**
     * Method that will convert the json string to destination by the type(cls).
     *
     * @param jsonString
     *            the source json string; if null, will return null.
     * @param cls
     *            the destination data type.
     * @return
     */
    public static <T> T convertJsonStringToObject(String jsonString,
            Class<T> cls) throws Exception {
        if ("".equals(jsonString)||null==jsonString) {
            return null;
        }

        try {
            T object = objectMapper.readValue(jsonString, cls);
            return object;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Method that will convert from given value into instance of given value
     * type.
     * 
     * @param fromValue
     * @param toValueType
     * @return
     */
    private static <T> T convertValue(Object fromValue, Class<T> toValueType)
            throws Exception {
        try {
            return objectMapper.convertValue(fromValue, toValueType);
        } catch (IllegalArgumentException e) {
            throw new Exception(e);
        }
    }
    public JsonUtil() {
    }

    public static JsonConfig getJsonConfigWithFilter() {
        JsonConfig jsonConfig = new JsonConfig();
        PropertyFilter propertyFilter = new PropertyFilter() {
            public boolean apply(Object arg0, String arg1, Object arg2) {
                if (arg2 instanceof List) {
                    List<Object> list = (List)arg2;
                    if (list.size() == 0) {
                        return true;
                    }
                }

                return arg2 == null;
            }
        };
        jsonConfig.setJsonPropertyFilter(propertyFilter);
        return jsonConfig;
    }

}
