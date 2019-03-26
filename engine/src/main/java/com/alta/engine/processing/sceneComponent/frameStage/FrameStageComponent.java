package com.alta.engine.processing.sceneComponent.frameStage;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.model.participant.facility.FacilityPartParticipant;
import com.alta.computator.service.stage.StageComputator;
import com.alta.engine.core.asyncTask.AsyncTaskManager;
import com.alta.engine.processing.sceneComponent.actor.ActorCharacterComponent;
import com.alta.engine.processing.sceneComponent.facility.FacilityComponent;
import com.alta.engine.processing.sceneComponent.frameTemplate.FrameTemplateComponent;
import com.alta.scene.entities.FrameStage;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides base implementation of frame stage
 */
@Slf4j
public class FrameStageComponent extends FrameStage {

    private final AsyncTaskManager asyncTaskManager;
    private final StageComputator stageComputator;

    private final Map<String, FacilityComponent> facilitiesByUuid;
    private final Map<String, ActorCharacterComponent> actorCharacters;

    /**
     * Initialize new instance of {@link FrameStage}
     */
    public FrameStageComponent(FrameTemplateComponent frameTemplate,
                               List<ActorCharacterComponent> actorCharacters,
                               List<FacilityComponent> facilities,
                               StageComputator stageComputator,
                               AsyncTaskManager asyncTaskManager) {
        super(frameTemplate, actorCharacters, facilities);
        this.asyncTaskManager = asyncTaskManager;
        this.stageComputator = stageComputator;

        this.facilitiesByUuid = facilities.stream().collect(Collectors.toMap(f -> f.getUuid().toString(), f -> f));
        this.actorCharacters = actorCharacters.stream().collect(Collectors.toMap(ActorCharacterComponent::getUuid, npc -> npc));
    }

    /**
     * Updates the stage
     *
     * @param gameContainer - the game container instance
     * @param delta         - the delta between last and current calls
     */
    @Override
    public void onUpdateStage(GameContainer gameContainer, int delta) {
        this.onUpdate(delta);
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
                    log.info("Initialize computator for FrameStageComponent");
                    this.stageComputator.setAltitudeMap(
                            new AltitudeMap(
                                    this.frameTemplate.getTiledMap(),
                                    gameContainer.getWidth(),
                                    gameContainer.getHeight()
                            )
                    );
                    log.info("Completed initialization of computator for FrameStageComponent");
                }
        );
    }

    private void renderFrame() {
        Point mapCoordinates = this.stageComputator.getMapGlobalCoordinates();
        if (mapCoordinates == null) {
            return;
        }

        this.frameTemplate.render(mapCoordinates);
    }

    private void renderAllParticipants() {
        List<CoordinatedParticipant> sortedParticipants = this.stageComputator.getSortedParticipants();
        if (sortedParticipants == null || sortedParticipants.isEmpty()) {
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
                    if (this.actorCharacters.containsKey(participant.getUuid())) {
                        this.actorCharacters.get(participant.getUuid()).render((ActorParticipant) participant);
                    }
                    break;
            }
        });
    }

    private void onUpdate(int delta) {
        this.actorCharacters.forEach((uuid, baseSimpleNpc) -> {
            ActorParticipant participant = this.stageComputator.getActorParticipant(uuid);
            if (participant != null) {
                baseSimpleNpc.update(participant, delta);
            }
        });

        this.stageComputator.onTick(delta);
    }
}
