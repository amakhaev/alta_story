package com.alta.engine.view.components.frameStage;

import com.alta.computator.Computator;
import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.model.participant.facility.FacilityPartParticipant;
import com.alta.engine.core.asyncTask.AsyncTaskManager;
import com.alta.engine.view.components.actor.ActorCharacterComponent;
import com.alta.engine.view.components.facility.FacilityComponent;
import com.alta.engine.view.components.frameTemplate.FrameTemplateComponent;
import com.alta.scene.entities.FrameStage;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides base implementation of frame stage
 */
@Slf4j
public class FrameStageComponent extends FrameStage {

    private final AsyncTaskManager asyncTaskManager;
    private final Computator computator;

    private final Map<String, FacilityComponent> facilitiesByUuid;
    private final Map<String, ActorCharacterComponent> actorCharacters;

    private List<FacilityComponent> facilitiesToInitialize;

    /**
     * Initialize new instance of {@link FrameStage}
     */
    @AssistedInject
    public FrameStageComponent(@Assisted FrameTemplateComponent frameTemplate,
                               @Assisted List<ActorCharacterComponent> actorCharacters,
                               @Assisted List<FacilityComponent> facilities,
                               @Assisted Computator computator,
                               AsyncTaskManager asyncTaskManager) {
        super(frameTemplate, actorCharacters, facilities);
        this.asyncTaskManager = asyncTaskManager;
        this.computator = computator;

        this.facilitiesByUuid = facilities.stream().collect(Collectors.toMap(FacilityComponent::getUuid, f -> f));
        this.actorCharacters = actorCharacters.stream().collect(Collectors.toMap(ActorCharacterComponent::getUuid, npc -> npc));
        this.facilitiesToInitialize = new ArrayList<>();
    }

    /**
     * Updates the stage
     *
     * @param gameContainer - the game container instance
     * @param delta         - the delta between last and current calls
     */
    @Override
    public void onUpdateStage(GameContainer gameContainer, int delta) {
        this.onUpdate(gameContainer, delta);
    }

    /**
     * Renders the stage
     *
     * @param gameContainer - the game container instance
     * @param graphics      - the graphic to render primitives
     */
    @Override
    public void onRenderStage(GameContainer gameContainer, Graphics graphics) {
        this.renderFrame();
        this.renderAllParticipants();
    }

    /**
     * Initializes frame stage in GL context
     *
     * @param gameContainer - the game container instance
     */
    @Override
    public void onInit(GameContainer gameContainer) {
        super.onInit(gameContainer);

        this.asyncTaskManager.executeTask(
                "init-base-frame",
                () -> {
                    log.debug("Initialize computator for FrameStageComponent");
                    this.computator.getDataWriterFacade().addAltitudeMap(
                            new AltitudeMap(
                                    this.frameTemplate.getTiledMap(), gameContainer.getWidth(), gameContainer.getHeight()
                            )
                    );
                    log.debug("Completed initialization of computator for FrameStageComponent.");

                    log.debug("Call update method (initial) to avoid incorrect rendering animation");
                    this.onUpdate(gameContainer,0);
                    log.debug("Initial update completed.");
                }
        );
    }

    /**
     * Added facility component to stage.
     *
     * @param facilityComponent - the facility to be added.
     */
    public void addFacilityComponent(FacilityComponent facilityComponent) {
        this.facilitiesToInitialize.add(facilityComponent);
    }

    /**
     * Removes the facility from frame stage.
     *
     * @param facilityUuid - the uuid of facility to be removed.
     */
    public void removeFacilityComponent(String facilityUuid) {
        if (this.facilitiesByUuid.containsKey(facilityUuid)) {
            this.facilitiesByUuid.remove(facilityUuid);
            log.info("Facility with UUID {} was removed from frame stage.", facilityUuid);
        } else {
            log.warn("Facility with UUID {} not found.", facilityUuid);
        }
    }

    private void renderFrame() {
        Point mapCoordinates = this.computator.getDataReaderFacade().getMapGlobalCoordinates();
        if (mapCoordinates == null) {
            return;
        }

        this.frameTemplate.render(mapCoordinates);
    }

    private synchronized void renderAllParticipants() {
        List<CoordinatedParticipant> sortedParticipants = this.computator.getDataReaderFacade().getSortedParticipants();
        if (sortedParticipants.isEmpty()) {
            log.debug("Not participants to render");
            return;
        }

        sortedParticipants.forEach(participant -> {
            switch (participant.getParticipantType()) {
                case FACILITY_PART:
                    if (this.facilitiesByUuid.containsKey(participant.getUuid())) {
                        this.facilitiesByUuid.get(participant.getUuid()).render((FacilityPartParticipant)participant);
                    }
                    break;
                case ACTING_CHARACTER:
                case SIMPLE_NPC:
                case ROUTE_NPC:
                    if (this.actorCharacters.containsKey(participant.getUuid())) {
                        this.actorCharacters.get(participant.getUuid()).render((ActorParticipant) participant);
                    }
                    break;
            }
        });
    }

    private void onUpdate(GameContainer gameContainer, int delta) {
        if (this.computator.getUpdaterFacade().isLock()) {
            return;
        }

        if (!this.facilitiesToInitialize.isEmpty()) {
            this.facilitiesToInitialize.forEach(facility -> {
                facility.initialize(gameContainer);
                this.facilitiesByUuid.put(facility.getUuid(), facility);
            });
        }

        this.actorCharacters.forEach((uuid, baseSimpleNpc) -> {
            ActorParticipant participant = this.computator.getDataReaderFacade().findActorByUuid(uuid);
            if (participant != null) {
                baseSimpleNpc.update(participant, delta);
            }
        });

        this.computator.getUpdaterFacade().onUpdate(delta);
    }
}
