package com.alta.scene.di;

import com.alta.scene.configuration.SceneConfig;
import com.google.inject.Provider;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStream;

/**
 * Describes the provider for the yaml file
 */
public class SceneYamlProvider implements Provider<SceneConfig> {

    private static final String FILE_NAME = "scene-config.yaml";

    private SceneConfig sceneConfig;

    /**
     * Initialize ew instance of {@link SceneYamlProvider}
     */
    public SceneYamlProvider() {
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(FILE_NAME);

        PropertyUtils propUtils = new PropertyUtils();
        propUtils.setAllowReadOnlyProperties(true);
        Representer repr = new Representer();
        repr.setPropertyUtils(propUtils);
        Yaml configFile = new Yaml(new Constructor(SceneConfig.class), repr);
        this.sceneConfig = configFile.load(inputStream);
    }

    /**
     * Provides an instance of {@code T}.
     */
    @Override
    public SceneConfig get() {
        return this.sceneConfig;
    }
}
