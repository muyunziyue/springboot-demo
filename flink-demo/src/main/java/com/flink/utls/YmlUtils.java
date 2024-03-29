package com.flink.utls;

/**
 * @author ldx
 * @date 2022/9/23
 */

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YmlUtils {

    /**
     * 获取yml数据
     *
     * @param ymlName yml文件名
     * @param path    yml属性地址 例如：spring.redis.host
     * @return 返回属性值
     */
    public static Object getAssignYmlProperties(String ymlName, String path) throws Exception {
        Map<String, Object> obj = null;
        try {
            Yaml yaml = new Yaml();
            InputStream resourceAsStream = YmlUtils.class.getClassLoader().getResourceAsStream(ymlName + ".yml");
            obj = (Map) yaml.load(resourceAsStream);
            //解析属性地址
            if (!StringUtils.isEmpty(path)) {
                String[] arrayPath = path.split("\\.");
                int len = arrayPath.length;
                Map<String, Object> map = (Map<String, Object>) obj.get(arrayPath[0]);
                ;
                for (int i = 1; i < len - 1; i++) {
                    map = (Map<String, Object>) map.get(arrayPath[i]);
                }
                return (Object) map.get(arrayPath[len - 1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("读取yml文件信息时，属性地址错误");
        }
        return "";
    }
}
