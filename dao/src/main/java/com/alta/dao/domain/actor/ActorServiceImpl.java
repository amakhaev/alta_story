package com.alta.dao.domain.actor;

import com.alta.dao.data.actor.ActorModel;
import com.alta.utils.JsonParser;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;

/**
 * Provides the implementation of {@link ActorService}
 */
@Slf4j
public class ActorServiceImpl implements ActorService {

    private static final String ACTORS_FOLDER = "scene_data/actors/";
    private static final String PERSONS_DESCRIPTOR_FILE_NAME = "persons.dscr";

    private TileSetDescriptorEntity tileSetDescriptorEntity;

    /**
     * Gets the actor model by given file name of tile sets.
     *
     * @param descriptorFileName - the full name of descriptor file like "file.dscr".
     * @return the {@link ActorModel} instance.
     */
    @Override
    public ActorModel getActorModel(String descriptorFileName) {
        if (Strings.isNullOrEmpty(descriptorFileName)) {
            log.error("Given descriptor name is null or empty");
            return null;
        }

        if (this.tileSetDescriptorEntity == null) {
            this.tileSetDescriptorEntity = this.loadTileSetDescriptor();
        }

        ActorEntity actorEntity = this.loadActorEntity(descriptorFileName);
        return new ActorModel(
                actorEntity,
                this.getAbsolutePathToImageSet(actorEntity.getImageName()),
                this.tileSetDescriptorEntity
        );
    }

    private TileSetDescriptorEntity loadTileSetDescriptor() {
        return JsonParser.parse(
                this.getClass().getClassLoader().getResource(ACTORS_FOLDER + PERSONS_DESCRIPTOR_FILE_NAME).getPath(),
                TileSetDescriptorEntity.class
        );
    }

    private ActorEntity loadActorEntity(String descriptorFileName) {
        return JsonParser.parse(
                this.getClass().getClassLoader().getResource(ACTORS_FOLDER + descriptorFileName).getPath(),
                ActorEntity.class
        );
    }

    private String getAbsolutePathToImageSet(String imageFileName) {
        URL absolutePath = this.getClass()
                .getClassLoader()
                .getResource(ACTORS_FOLDER + imageFileName);

        if (absolutePath == null) {
            log.error("File with given name doesn't exists: {}", imageFileName);
            return null;
        }

        return absolutePath.getPath();
    }
}
