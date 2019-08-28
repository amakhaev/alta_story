package com.alta.scene.di;

import com.alta.scene.configuration.SceneConfig;
import com.alta.utils.YamlParser;
import com.google.inject.Provider;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStream;
import java.net.URL;

/**
 * Describes the provider for the yaml file
 */
public class SceneYamlProvider implements Provider<SceneConfig> {

    private static final String FILE_NAME = "scene-config.yaml";

    private SceneConfig sceneConfig;

    /**
     * Initialize new instance of {@link SceneYamlProvider}
     */
    public SceneYamlProvider() {
        URL configUrl = this.getClass().getClassLoader().getResource(FILE_NAME);
        if (configUrl == null) {
            throw new RuntimeException("Config with given name " + FILE_NAME + " not found.");
        }

        this.sceneConfig = YamlParser.parse(configUrl.getPath(), SceneConfig.class);
    }

    /**
     * Provides an instance of {@code T}.
     */
    @Override
    public SceneConfig get() {
        return this.sceneConfig;
    }
}
