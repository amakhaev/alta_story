package com.alta.mediator.sceneModule.entities;

import com.alta.computator.model.participant.facility.FacilityPartParticipant;
import com.alta.computator.model.participant.facility.FacilityParticipant;
import com.alta.scene.entities.Facility;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import java.util.UUID;

/**
 * Provides the base implementation of facility scene object
 */
@Slf4j
public class BaseFacility implements Facility<FacilityPartParticipant>, UniqueObject {

    private final UUID uuid;
    private final String absolutePathToSpriteSheet;
    private final int tileWidth;
    private final int tileHeight;

    private SpriteSheet spriteSheet;

    /**
     * Initialize new instance of {@link BaseFacility}
     */
    @Inject
    public BaseFacility(UUID uuid, String absolutePathToSpriteSheet, int tileWidth, int tileHeight) {
        this.uuid = uuid;
        this.absolutePathToSpriteSheet = absolutePathToSpriteSheet;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    /**
     * Gets the unique identifier of object
     *
     * @return the {@link UUID} instance
     */
    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Initializes the renderable object in GL context if needed.
     *
     * @param container - the game container instance.
     */
    @Override
    public void initialize(GameContainer container) {
        try {
            this.spriteSheet = new SpriteSheet(this.absolutePathToSpriteSheet, this.tileWidth, this.tileHeight);
        } catch (SlickException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Renders the object on given coordinates
     *
     * @param facilityPartParticipant - the participant that contains information about participant
     */
    @Override
    public void render(FacilityPartParticipant facilityPartParticipant) {
        this.spriteSheet.getSubImage(
                facilityPartParticipant.getSpriteSheetTileMapCoordinate().x,
                facilityPartParticipant.getSpriteSheetTileMapCoordinate().y)
                .draw(
                        facilityPartParticipant.getCurrentGlobalCoordinates().x,
                        facilityPartParticipant.getCurrentGlobalCoordinates().y
                );
    }
}
