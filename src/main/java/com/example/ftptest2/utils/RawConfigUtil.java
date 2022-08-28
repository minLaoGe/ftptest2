package com.example.ftptest2.utils;

import java.io.IOException;
import java.util.Properties;
import org.springframework.core.io.ClassPathResource;

/**
 * 直接读取配置文件
 *
 * @author jimo
 * @version 1.0.0
 */
public class RawConfigUtil {

    private static Properties properties = readProperties("application.properties");

    private static Properties readProperties(String... confFile) {
        final Properties properties = new Properties();
        try {
            for (String path : confFile) {
                final ClassPathResource resource = new ClassPathResource(path);
                properties.load(resource.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static String getString(String key) {
        return properties.getProperty(key);
    }
}
