package net.b521.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

import java.util.List;
import java.util.Map;

/**
 * @author Allen
 * @Date: 2020/6/11 下午3:32
 * @Description: jackson转换工具类
 * @Version 1.0
 **/
public class JsonUtils {

    /**
     * ObjectMapper是JSON操作的核心，Jackson的所有JSON操作都是在ObjectMapper中实现。
     * ObjectMapper有多个JSON序列化的方法，可以把JSON字符串保存File、OutputStream等不同的介质中。
     * writeValue(File arg0, Object arg1)把arg1转成json序列，并保存到arg0文件中。
     * writeValue(OutputStream arg0, Object arg1)把arg1转成json序列，并保存到arg0输出流中。
     * writeValueAsBytes(Object arg0)把arg0转成json序列，并把结果输出成字节数组。
     * writeValueAsString(Object arg0)把arg0转成json序列，并把结果输出成字符串。
     */

    /**
     * 初始化变量
     */
    private static ObjectMapper mapper = new ObjectMapper();

    public static ObjectMapper getMapper() {
        return mapper;
    }

    static {
        // 解决实体未包含字段反序列化时抛出异常
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 对于空的对象转json的时候不抛出错误
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        // 允许属性名称没有引号
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        // 允许单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }

    /**
     * Description : 将一个object转换为json, 可以是一个java对象，也可以是集合
     * @param obj 传入的数据
     * @return String
     */
    public static String objectToJson(Object obj) {
        String json = null;
        try {
            json = mapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * ObjectMapper支持从byte[]、File、InputStream、字符串等数据的JSON反序列化。
     */

    /**
     * Description : 将json结果集转化为对象
     * @param json json数据
     * @param beanType 转换的实体类型
     * @return T
     */
    public static <T> T jsonToClass(String json, Class<T> beanType) {
        T t = null;
        try {
            t = mapper.readValue(json, beanType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * Description : 将json数据转换成Map
     * @param json 转换的数据
     * @return Map
     */
    public static Map<String, Object> jsonToMap(String json) {
        Map<String, Object> map = null;
        try {
            map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Description : 将json数据转换成list
     * @param json 转换的数据
     * @param beanType List.class
     * @return List
     */
    public static <T> List<T> jsonToList(String json, Class<T> beanType) {
        List<T> list = null;
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, beanType);
            list = mapper.readValue(json, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Description : 获取json对象数据的属性
     * @param resData 请求的数据
     * @param resPro 请求的属性
     * @return 返回String类型数据
     */
    public static String findValue(String resData, String resPro) {
        String result = null;
        try {
            JsonNode node = mapper.readTree(resData);
            JsonNode resProNode = node.get(resPro);
            result = JsonUtils.objectToJson(resProNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Description : 获取JsonNode对象
     * @date : 2020/06/15 15:39:03
     * @param resData 请求的json数据
     * @return JsonNode
     */
    public static JsonNode toJsonNode(String resData) {
        JsonNode node = null;
        try {
            node = mapper.readTree(resData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return node;
    }

    /**
     * Description : 获取JsonNode对象
     * @date : 2020/06/15 15:42:02
     * @param obj 实例化对象
     * @return JsonNode
     */
    public static JsonNode toJsonNode(Object obj) {
        JsonNode node = null;
        try {
            node = mapper.convertValue(obj, JsonNode.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return node;
    }

    /**
     * Description : 格式化JSON
     * @date : 2020/06/15 15:36:11
     * @param object
     * @return String
     */
    public static String prettyJson(Object object) {
        String res = null;
        try {
            res = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            return res;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
