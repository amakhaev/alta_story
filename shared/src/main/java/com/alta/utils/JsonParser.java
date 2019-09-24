package com.alta.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Provides the parser of json
 */
@UtilityClass
@Slf4j
public class JsonParser {

    private Gson gson = new GsonBuilder().create();

    /**
     * Parses the file to POJO by given path
     *
     * @param path - the path to file
     * @param resultType - the type of result class
     * @return parsed class instance
     */
    public <T> T parse(String path, Class<T> resultType) {
        try (Reader reader = new InputStreamReader(new FileInputStream(path))) {
            return gson.fromJson(reader, resultType);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * Parses the file to POJO by given path
     *
     * @param path - the path to file
     * @param resultType - the type of result class
     * @param deserializer - the matcher that will be used to create POJO.
     * @return parsed class instance
     */
    public <T> T parse(String path, Class<T> resultType, JsonDeserializer<T> deserializer) {
        try (Reader reader = new InputStreamReader(new FileInputStream(path))) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(resultType, deserializer);
            return gsonBuilder.create().fromJson(reader, resultType);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * Parses the file to POJO by given path
     *
     * @param path - the path to file
     * @param resultType - the type of result class
     * @return parsed class instance
     */
    public <T> T parse(String path, Type resultType) {
        try (Reader reader = new InputStreamReader(new FileInputStream(path))) {
            return gson.fromJson(reader, resultType);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * Parses the file to POJO by given path and custom matcher.
     *
     * @param path - the path to file
     * @param resultType - the type of result class
     * @param deserializers - the matcher that will be used to create POJO.
     * @return parsed class instance
     */
    public <T> T parse(String path, Type resultType, Map<Type, JsonDeserializer> deserializers) {
        try (Reader reader = new InputStreamReader(new FileInputStream(path))) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            deserializers.forEach(gsonBuilder::registerTypeAdapter);
            return gsonBuilder.create().fromJson(reader, resultType);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * Creates the JSON string from POJO.
     *
     * @param data - the data to be serialized.
     * @return the json string.
     */
    public String toJson(Object data) {
        return gson.toJson(data);
    }

}
