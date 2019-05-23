package com.alta.dao.domain.actor;

import com.alta.dao.data.actor.ActorModel;
import com.alta.utils.JsonParser;
import com.google.common.base.Strings;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ActorServiceImpl implements ActorService {

    private static final String ACTORS_FOLDER = "scene_data/actors/";
    private static final String TILE_SETS_FOLDER = ACTORS_FOLDER + "tile_sets/";
    private static final String ACTOR_TILE_SET_DESCRIPTOR_PATH = ACTORS_FOLDER + "actor_tile_set.dscr";
    private static final String ACTOR_LIST_DESCRIPTOR_PATH = ACTORS_FOLDER + "actor_list.dscr";

    private TileSetDescriptorEntity tileSetDescriptorEntity;
    private Map<String, ActorEntity> actors;

    /**
     * Gets the actor model by given file name of tile sets.
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

        if (this.tileSetDescriptorEntity == null) {
            this.tileSetDescriptorEntity = this.loadTileSetDescriptor();
        }

        if (this.actors == null) {
            this.actors = this.loadActorList();
        }

        if (!this.actors.containsKey(name)) {
            return null;
        }

        ActorEntity actorEntity = this.actors.get(name);

        return ActorModel.builder()
                .descriptor(this.tileSetDescriptorEntity)
                .durationTime(actorEntity.getDurationTime())
                .zIndex(actorEntity.getZIndex())
                .pathToImageSet(this.getAbsolutePathToTileSet(actorEntity.getImageName()))
                .build();
    }

    private TileSetDescriptorEntity loadTileSetDescriptor() {
        return JsonParser.parse(
                this.getClass().getClassLoader().getResource(ACTOR_TILE_SET_DESCRIPTOR_PATH).getPath(),
                TileSetDescriptorEntity.class
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

    private String getAbsolutePathToTileSet(String imageFileName) {
        URL absolutePath = this.getClass()
                .getClassLoader()
                .getResource(TILE_SETS_FOLDER + imageFileName);

        if (absolutePath == null) {
            log.error("File with given name doesn't exists: {}", imageFileName);
            return null;
        }

        return absolutePath.getPath();
    }
}
