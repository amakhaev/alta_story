package com.alta.dao.domain.preservation;

import com.alta.dao.configuration.CassandraConnector;
import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.data.preservation.MapPreservationModel;
import com.alta.dao.data.preservation.PreservationModel;
import com.alta.dao.data.preservation.QuestPreservationModel;
import com.alta.dao.data.preservation.udt.ActingCharacterUdt;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

/**
 * Provides the service to make CRUD operation with preservation
 */
@Slf4j
public class PreservationServiceImpl implements PreservationService {

    private final Mapper<PreservationModel> preservationModelMapper;
    private final Mapper<InteractionPreservationModel> interactionPreservationModelMapper;
    private final Mapper<QuestPreservationModel> questPreservationModelMapper;
    private final Mapper<MapPreservationModel> mapPreservationModelMapper;

    private final MapPreservationAccessor mapPreservationAccessor;
    private final InteractionPreservationAccessor interactionPreservationAccessor;

    /**
     * Initialize new instance of {@link PreservationServiceImpl}.
     */
    @Inject
    public PreservationServiceImpl(CassandraConnector cassandraConnector) {
        MappingManager mappingManager = new MappingManager(cassandraConnector.getSession());

        this.preservationModelMapper = mappingManager.mapper(PreservationModel.class);
        this.interactionPreservationModelMapper = mappingManager.mapper(InteractionPreservationModel.class);
        this.questPreservationModelMapper = mappingManager.mapper(QuestPreservationModel.class);
        this.mapPreservationModelMapper = mappingManager.mapper(MapPreservationModel.class);

        this.mapPreservationAccessor = mappingManager.createAccessor(MapPreservationAccessor.class);
        this.interactionPreservationAccessor = mappingManager.createAccessor(InteractionPreservationAccessor.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateChapterIndicator(int id, int chapterIndicator) {
        log.debug("Update chapter indicator of preservation {} with value {}", id, chapterIndicator);

        PreservationModel preservationModel = this.preservationModelMapper.get(id);
        if (preservationModel == null) {
            throw new RuntimeException("Preservation with given id " + id + " not found.");
        }

        preservationModel.setChapterIndicator(chapterIndicator);
        this.preservationModelMapper.save(preservationModel);

        log.debug(
                "Updating of chapter indicator of preservation {} with value {} was completed successfully",
                id, chapterIndicator
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateActingCharacter(int preservationId, ActingCharacterUdt actingCharacter) {
        PreservationModel preservationModel = this.preservationModelMapper.get(preservationId);
        if (preservationModel == null) {
            throw new RuntimeException("Preservation with given id " + preservationId + " not found.");
        }

        preservationModel.setActingCharacter(actingCharacter);
        this.preservationModelMapper.save(preservationModel);

        log.debug("Updating of acting character in preservation {} was completed successfully", preservationId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void upsertInteraction(int preservationId, UUID interactionUuid, String mapName, boolean isComplete) {
        log.debug(
                "Upsert interaction of preservation {} with interaction uuid: {}, map name: {}, complete status: {}",
                preservationId, interactionUuid, mapName, isComplete
        );

        InteractionPreservationModel model = InteractionPreservationModel.builder()
                .preservationId(preservationId)
                .interactionUuid(interactionUuid)
                .mapName(mapName)
                .isCompleted(isComplete)
                .build();

        this.interactionPreservationModelMapper.save(model);

        log.debug("Upserting interaction of preservation was completed successfully");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void upsertQuest(int preservationId, String name, int currentStep, boolean isComplete) {
        log.debug(
                "Upsert quest of preservation {} with quest name: {}, current step: {}, complete status: {}",
                preservationId, name, currentStep, isComplete
        );

        QuestPreservationModel model = QuestPreservationModel.builder()
                .preservationId(preservationId)
                .questName(name)
                .currentStepNumber(currentStep)
                .isCompleted(isComplete)
                .build();

        this.questPreservationModelMapper.save(model);

        log.debug("Upserting quest of preservation was completed successfully");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void upsertMap(int preservationId, UUID participantUuid, String mapName, boolean isVisible) {
        log.debug(
                "Upsert map of preservation {} with participant uuid: {}, map name: {}, visible status: {}",
                preservationId, participantUuid, mapName, isVisible
        );

        MapPreservationModel model = MapPreservationModel.builder()
                .preservationId(preservationId)
                .participantUuid(participantUuid)
                .mapName(mapName)
                .isVisible(isVisible)
                .build();

        this.mapPreservationModelMapper.save(model);

        log.debug("Upserting map of preservation was completed successfully");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestPreservationModel getQuest(int preservationId, String questName) {
        return this.questPreservationModelMapper.get(preservationId, questName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreservationModel getPreservation(int preservationId) {
        return this.preservationModelMapper.get(preservationId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InteractionPreservationModel getInteraction(int preservationId, UUID interactionUuid) {
        return this.interactionPreservationModelMapper.get(preservationId, interactionUuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MapPreservationModel> getMaps(int preservationId, String mapName) {
        return this.mapPreservationAccessor.findAllByPreservationIdAndMapName(preservationId, mapName).all();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<InteractionPreservationModel> getInteractions(int preservationId, String mapName) {
        return this.interactionPreservationAccessor.findAllByPreservationIdAndMapName(preservationId, mapName).all();
    }
}
