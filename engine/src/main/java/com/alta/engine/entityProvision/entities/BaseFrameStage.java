package com.alta.engine.entityProvision.entities;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.model.participant.facility.FacilityPartParticipant;
import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.computator.service.stage.StageComputator;
import com.alta.engine.asyncTask.AsyncTaskManager;
import com.alta.engine.inputListener.ActionProducer;
import com.alta.engine.inputListener.SceneAction;
import com.alta.scene.entities.FrameStage;
import com.alta.utils.ExecutorServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Provides base implementation of frame stage
 */
@Slf4j
public class BaseFrameStage extends FrameStage {

    private final AsyncTaskManager asyncTaskManager;
    private final StageComputator stageComputator;

    private final Map<String, BaseFacility> facilitiesByUuid;
    private final Map<String, BaseActorCharacter> actorCharacters;

    private AtomicBoolean isUpdateInProgress;

    /**
     * Initialize new instance of {@link FrameStage}
     */
    public BaseFrameStage(BaseFrameTemplate frameTemplate,
                          List<BaseActorCharacter> actorCharacters,
                          List<BaseFacility> facilities,
                          StageComputator stageComputator,
                          ActionProducer actionProducer, AsyncTaskManager asyncTaskManager) {
        super(frameTemplate, actorCharacters, facilities);
        this.asyncTaskManager = asyncTaskManager;
        this.stageComputator = stageComputator;

        this.facilitiesByUuid = facilities.stream().collect(Collectors.toMap(f -> f.getUuid().toString(), f -> f));
        this.actorCharacters = actorCharacters.stream().collect(Collectors.toMap(BaseActorCharacter::getUuid, npc -> npc));

        this.isUpdateInProgress = new AtomicBoolean(false);

        actionProducer.setListener(this::handleAction);
    }

    /**
     * Updates the stage
     *
     * @param gameContainer - the game container instance
     * @param delta         - the delta between last and current calls
     */
    @Override
    public void onUpdateStage(GameContainer gameContainer, int delta) {
        if (this.isUpdateInProgress.get()) {
            return;
        }

        this.asyncTaskManager.executeTask("update-stage", () -> this.onUpdate(delta));
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
                    log.info("Initialize computator for BaseFrameStage");
                    this.stageComputator.setAltitudeMap(
                            new AltitudeMap(
                                    this.frameTemplate.getTiledMap(),
                                    gameContainer.getWidth(),
                                    gameContainer.getHeight()
                            )
                    );
                    log.info("Completed initialization of computator for BaseFrameStage");
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

    private void handleAction(SceneAction action) {
        switch (action) {
            case MOVE_UP:
                this.stageComputator.tryToRunMovement(MovementDirection.UP);
                break;
            case MOVE_DOWN:
                this.stageComputator.tryToRunMovement(MovementDirection.DOWN);
                break;
            case MOVE_LEFT:
                this.stageComputator.tryToRunMovement(MovementDirection.LEFT);
                break;
            case MOVE_RIGHT:
                this.stageComputator.tryToRunMovement(MovementDirection.RIGHT);
                break;
        }
    }

    private void onUpdate(int delta) {
        this.isUpdateInProgress.set(true);
        this.actorCharacters.forEach((uuid, baseSimpleNpc) -> {
            ActorParticipant participant = this.stageComputator.getActorParticipant(uuid);
            if (participant != null) {
                baseSimpleNpc.update(participant, delta);
            }
        });

        this.stageComputator.onTick(delta);
        this.isUpdateInProgress.set(false);
    }
}
