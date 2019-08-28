package com.alta.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Provides the parser of yaml files.
 */
@Slf4j
@UtilityClass
public class YamlParser {

    /**
     * Parses the file to POJO by given path
     *
     * @param path - the path to file
     * @param resultType - the type of result class
     * @return parsed class instance
     */
    public <T> T parse(String path, Class<T> resultType) {
        try (Reader reader = new InputStreamReader(new FileInputStream(path))) {
            PropertyUtils propUtils = new PropertyUtils();
            propUtils.setAllowReadOnlyProperties(true);
            Representer repr = new Representer();
            repr.setPropertyUtils(propUtils);
            Yaml configFile = new Yaml(new Constructor(resultType), repr);
            return configFile.load(reader);
        } catch (IOException e) {
            log.error("Failed to parse " + path + " file", e);
            return null;
        }
    }

}
