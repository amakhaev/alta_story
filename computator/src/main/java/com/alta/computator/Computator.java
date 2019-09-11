package com.alta.computator;

import com.alta.computator.calculator.CalculatorCache;
import com.alta.computator.calculator.actingCharacter.ActingCharacterMediatorImpl;
import com.alta.computator.calculator.facility.FacilityMediatorImpl;
import com.alta.computator.calculator.focusPoint.FocusPointMediatorImpl;
import com.alta.computator.calculator.map.MapMediatorImpl;
import com.alta.computator.calculator.npc.NpcMediatorImpl;
import com.alta.computator.facade.action.ActionFacade;
import com.alta.computator.facade.action.ActionFacadeImpl;
import com.alta.computator.facade.dataReader.DataReaderFacade;
import com.alta.computator.facade.dataReader.DataReaderFacadeImpl;
import com.alta.computator.facade.dataWriter.DataWriterFacade;
import com.alta.computator.facade.dataWriter.DataWriterFacadeImpl;
import com.alta.computator.facade.updater.UpdaterFacade;
import com.alta.computator.facade.updater.UpdaterFacadeImpl;
import com.alta.computator.core.storage.*;
import lombok.Getter;

/**
 * Provides the proxy for access to internal logic.
 */
@Getter
public class Computator {

    private final ActionFacade actionFacade;
    private final DataReaderFacade dataReaderFacade;
    private final DataWriterFacade dataWriterFacade;
    private final UpdaterFacade updaterFacade;

    /**
     * Initialize new instance of {@link Computator}.
     */
    public Computator() {
        ModelStorage modelStorage = new ModelStorage();
        CalculatorCache calculatorCache = new CalculatorCache();

        StorageReader storageReader = new StorageReaderImpl(modelStorage);
        StorageWriter storageWriter = new StorageWriterImpl(modelStorage);

        ActingCharacterMediatorImpl actingCharacterMediator = new ActingCharacterMediatorImpl(storageReader, calculatorCache);
        FacilityMediatorImpl facilityMediator = new FacilityMediatorImpl(storageReader);
        FocusPointMediatorImpl focusPointMediator = new FocusPointMediatorImpl(storageReader, calculatorCache);
        MapMediatorImpl mapMediator = new MapMediatorImpl(storageReader);
        NpcMediatorImpl npcMediator = new NpcMediatorImpl(storageReader);

        this.actionFacade = new ActionFacadeImpl(actingCharacterMediator, focusPointMediator, npcMediator);
        this.dataReaderFacade = new DataReaderFacadeImpl(
                mapMediator, storageReader, npcMediator, actingCharacterMediator, facilityMediator
        );
        this.dataWriterFacade = new DataWriterFacadeImpl(storageWriter, storageReader, calculatorCache);
        this.updaterFacade = new UpdaterFacadeImpl(
                focusPointMediator, mapMediator, actingCharacterMediator, facilityMediator, npcMediator
        );
    }
}
