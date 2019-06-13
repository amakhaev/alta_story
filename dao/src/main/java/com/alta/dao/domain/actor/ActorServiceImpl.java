package com.alta.dao.domain.actor;

import com.alta.dao.data.actor.ActorFaceSetDescriptorModel;
import com.alta.dao.data.actor.ActorModel;
import com.alta.dao.data.actor.ActorTileSetDescriptorModel;
import com.alta.utils.JsonParser;
import com.google.common.base.Strings;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides the implementation of {@link ActorService}
 */
@Slf4j
public class ActorServiceImpl implements ActorService {

    private static final String ACTORS_FOLDER = "scene_data/actors/";
    private static final String TILE_SETS_FOLDER = ACTORS_FOLDER + "tile_sets/";
    private static final String FACE_SETS_FOLDER = ACTORS_FOLDER + "face_sets/";
    private static final String ACTOR_TILE_SET_DESCRIPTOR_PATH = ACTORS_FOLDER + "actor_tile_set.dscr";
    private static final String ACTOR_FACE_SET_DESCRIPTOR_PATH = ACTORS_FOLDER + "face_set.dscr";
    private static final String ACTOR_LIST_DESCRIPTOR_PATH = ACTORS_FOLDER + "actor_list.dscr";

    private final ActorTileSetDescriptorDeserializer actorTileSetDescriptorDeserializer;
    private final ActorFaceSetDescriptoDeserializer actorFaceSetDescriptoDeserializer;

    private ActorTileSetDescriptorModel actorTileSetDescriptorModel;
    private ActorFaceSetDescriptorModel actorFaceSetDescriptorModel;
    private Map<String, ActorEntity> actors;

    @Inject
    public ActorServiceImpl(ActorTileSetDescriptorDeserializer actorTileSetDescriptorDeserializer,
                            ActorFaceSetDescriptoDeserializer actorFaceSetDescriptoDeserializer) {

        this.actorTileSetDescriptorDeserializer = actorTileSetDescriptorDeserializer;
        this.actorFaceSetDescriptoDeserializer = actorFaceSetDescriptoDeserializer;

        this.actorTileSetDescriptorModel = this.loadTileSetDescriptor();
        this.actorFaceSetDescriptorModel = this.loadFaceSetDescriptor();
        this.actors = this.loadActorList();
    }

    /**
     * Gets the actor data by given file name of tile sets.
     *
     * @param name - the name of actor.
     *
     * @return the {@link ActorModel} instance.
     */
    @Override
    public ActorModel getActorModel(String name) {
        if (Strings.isNullOrEmpty(name)) {
            log.error("Given actor name is null or empty");
            return null;
        }

        if (!this.actors.containsKey(name)) {
            return null;
        }

        ActorEntity actorEntity = this.actors.get(name);
        return ActorModel.builder()
                .tileSetDescriptor(this.actorTileSetDescriptorModel)
                .faceSetDescriptor(this.actorFaceSetDescriptorModel)
                .durationTime(actorEntity.getDurationTime())
                .zIndex(actorEntity.getZIndex())
                .pathToTileSetImage(this.getAbsolutePathToResource(TILE_SETS_FOLDER + actorEntity.getTileSetImageName()))
                .pathToFaceSetImage(
                        Strings.isNullOrEmpty(actorEntity.getFaceSetImageName()) ?
                                null :
                                this.getAbsolutePathToResource(FACE_SETS_FOLDER + actorEntity.getFaceSetImageName())
                )
                .build();
    }

    private ActorTileSetDescriptorModel loadTileSetDescriptor() {
        return JsonParser.parse(
                this.getClass().getClassLoader().getResource(ACTOR_TILE_SET_DESCRIPTOR_PATH).getPath(),
                ActorTileSetDescriptorModel.class,
                this.actorTileSetDescriptorDeserializer
        );
    }

    private ActorFaceSetDescriptorModel loadFaceSetDescriptor() {
        return JsonParser.parse(
                this.getClass().getClassLoader().getResource(ACTOR_FACE_SET_DESCRIPTOR_PATH).getPath(),
                ActorFaceSetDescriptorModel.class,
                this.actorFaceSetDescriptoDeserializer
        );
    }

    private Map<String, ActorEntity> loadActorList() {
        List<ActorEntity> actors =  JsonParser.parse(
                this.getClass().getClassLoader().getResource(ACTOR_LIST_DESCRIPTOR_PATH).getPath(),
                new TypeToken<ArrayList<ActorEntity>>(){}.getType()
        );

        if (actors == null || actors.isEmpty()) {
            throw new RuntimeException("No available actors.");
        }

        return actors.stream().collect(Collectors.toMap(ActorEntity::getName, actor -> actor ));
    }

    private String getAbsolutePathToResource(String relatedFilePath) {
        URL absolutePath = this.getClass()
                .getClassLoader()
                .getResource(relatedFilePath);

        if (absolutePath == null) {
            log.error("File with given name doesn't exists: {}", relatedFilePath);
            return null;
        }

        return absolutePath.getPath();
    }
}
