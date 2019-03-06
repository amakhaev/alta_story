package com.alta.engine.entityProvision.entities;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.model.participant.facility.FacilityPartParticipant;
import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.computator.service.stage.StageComputator;
import com.alta.engine.inputListener.ActionProducer;
import com.alta.engine.inputListener.SceneAction;
import com.alta.scene.entities.Actor;
import com.alta.scene.entities.FrameStage;
import com.alta.utils.ThreadPoolExecutor;
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

    private static final int THREAD_POOL_SIZE = 3;
    private static final String THREAD_POOL_NAME = "base-frame-stage";

    private final ThreadPoolExecutor threadPoolExecutor;
    private final StageComputator stageComputator;
    private final Map<String, BaseFacility> facilitiesByUuid;
    private AtomicBoolean isUpdateInProgress;

    /**
     * Initialize new instance of {@link FrameStage}
     */
    public BaseFrameStage(BaseFrameTemplate frameTemplate,
                          List<Actor> actors,
                          List<BaseFacility> facilities,
                          StageComputator stageComputator,
                          ActionProducer actionProducer) {
        super(frameTemplate, actors, facilities);
        this.threadPoolExecutor = new ThreadPoolExecutor(THREAD_POOL_SIZE, THREAD_POOL_NAME);
        this.stageComputator = stageComputator;
        this.facilitiesByUuid = facilities.stream().collect(Collectors.toMap(f -> f.getUuid().toString(), f -> f));
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

        this.threadPoolExecutor.run(() -> {
            this.isUpdateInProgress.set(true);
            this.stageComputator.onTick();
            this.isUpdateInProgress.set(false);
        });
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

        this.threadPoolExecutor.run(
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
}
