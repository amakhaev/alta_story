package behaviorprocess.sync;

import com.alta.behaviorprocess.data.interaction.InteractionModel;
import com.alta.behaviorprocess.data.interaction.InteractionRepository;
import com.alta.behaviorprocess.data.quest.QuestModel;
import com.alta.behaviorprocess.data.quest.QuestRepository;
import com.alta.behaviorprocess.sync.DataStorage;
import com.alta.behaviorprocess.sync.DataType;
import com.alta.behaviorprocess.sync.SynchronizationManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

public class SynchronizationManagerTest {

    private CountDownLatch lock = new CountDownLatch(1);

    private SynchronizationManager synchronizationManager;
    private QuestRepository questRepository;
    private InteractionRepository interactionRepository;
    private DataStorage dataStorage;

    @Before
    public void setUp() {
        this.questRepository = mock(QuestRepository.class);
        this.interactionRepository = mock(InteractionRepository.class);
        this.dataStorage = mock(DataStorage.class);

        this.synchronizationManager = new SynchronizationManager(
                this.interactionRepository,
                this.questRepository,
                this.dataStorage
        );
    }

    @Test
    public void synchronizationManager_syncMainQuest_synchronizedMainQuestOnly() throws InterruptedException {
        QuestModel questModel = QuestModel.builder().name("test").build();

        when(this.questRepository.getMainQuest()).thenReturn(questModel);
        ArgumentCaptor<QuestModel> questArgumentCaptor = ArgumentCaptor.forClass(QuestModel.class);

        this.synchronizationManager.sync(Collections.singletonList(DataType.MAIN_QUEST));

        lock.await(500, TimeUnit.MILLISECONDS);

        verify(this.questRepository, times(1)).getMainQuest();
        verify(this.dataStorage, times(1)).evictAndSaveMainQuest(questArgumentCaptor.capture());
        Assert.assertEquals("test", questArgumentCaptor.getValue().getName());

        verify(this.dataStorage, times(0)).evictAndSaveCurrentMap(anyString());
        verify(this.interactionRepository, times(0)).findInteractions(anyString());
        verify(this.dataStorage, times(0)).evictAndSaveInteractions(anyList());
    }

    @Test
    public void synchronizationManager_syncInteraction_synchronizedInteractionsOnly() throws InterruptedException {
        InteractionModel interactionModel = InteractionModel.builder().mapName("testMap").build();

        when(this.interactionRepository.findInteractions("testMap")).thenReturn(Collections.singletonList(interactionModel));

        this.synchronizationManager.sync(Collections.singletonList(DataType.INTERACTION), "testMap");

        lock.await(500, TimeUnit.MILLISECONDS);

        verify(this.questRepository, times(0)).getMainQuest();
        verify(this.dataStorage, times(0)).evictAndSaveMainQuest(any(QuestModel.class));
        verify(this.dataStorage, times(0)).evictAndSaveCurrentMap(anyString());

        verify(this.interactionRepository, times(1)).findInteractions("testMap");
        verify(this.dataStorage, times(1)).evictAndSaveInteractions(anyList());
    }

    @Test
    public void synchronizationManager_syncCurrentMap_synchronizedCurrentMapOnly() throws InterruptedException {
        this.synchronizationManager.sync(Collections.singletonList(DataType.CURRENT_MAP), "testMap");

        lock.await(500, TimeUnit.MILLISECONDS);

        verify(this.questRepository, times(0)).getMainQuest();
        verify(this.dataStorage, times(0)).evictAndSaveMainQuest(any(QuestModel.class));
        verify(this.interactionRepository, times(0)).findInteractions(anyString());
        verify(this.dataStorage, times(0)).evictAndSaveInteractions(anyList());
        verify(this.dataStorage, times(1)).evictAndSaveCurrentMap("testMap");
    }

    @Test
    public void synchronizationManager_syncCurrentMapNullMapName_nothingToSynchronize() throws InterruptedException {
        this.synchronizationManager.sync(Collections.singletonList(DataType.CURRENT_MAP), null);

        lock.await(500, TimeUnit.MILLISECONDS);

        verify(this.questRepository, times(0)).getMainQuest();
        verify(this.dataStorage, times(0)).evictAndSaveMainQuest(any(QuestModel.class));
        verify(this.interactionRepository, times(0)).findInteractions(anyString());
        verify(this.dataStorage, times(0)).evictAndSaveInteractions(anyList());
        verify(this.dataStorage, times(0)).evictAndSaveCurrentMap(anyString());
    }

    @Test
    public void synchronizationManager_syncAll_synchronizedAllDataTypes() throws InterruptedException {
        QuestModel questModel = QuestModel.builder().name("test").build();

        when(this.questRepository.getMainQuest()).thenReturn(questModel);

        this.synchronizationManager.sync(Arrays.asList(DataType.CURRENT_MAP, DataType.INTERACTION, DataType.MAIN_QUEST), "testMap");

        lock.await(500, TimeUnit.MILLISECONDS);

        verify(this.questRepository, times(1)).getMainQuest();
        verify(this.dataStorage, times(1)).evictAndSaveMainQuest(any(QuestModel.class));
        verify(this.interactionRepository, times(1)).findInteractions(anyString());
        verify(this.dataStorage, times(1)).evictAndSaveInteractions(anyList());
        verify(this.dataStorage, times(1)).evictAndSaveCurrentMap(anyString());
    }

    @Test
    public void synchronizationManager_syncAllWithoutMapName_synchronizedMainQuestOnly() throws InterruptedException {
        QuestModel questModel = QuestModel.builder().name("test").build();

        when(this.questRepository.getMainQuest()).thenReturn(questModel);

        this.synchronizationManager.sync(Arrays.asList(DataType.CURRENT_MAP, DataType.INTERACTION, DataType.MAIN_QUEST));

        lock.await(500, TimeUnit.MILLISECONDS);

        verify(this.questRepository, times(1)).getMainQuest();
        verify(this.dataStorage, times(1)).evictAndSaveMainQuest(any(QuestModel.class));
        verify(this.interactionRepository, times(0)).findInteractions(anyString());
        verify(this.dataStorage, times(0)).evictAndSaveInteractions(anyList());
        verify(this.dataStorage, times(0)).evictAndSaveCurrentMap(anyString());
    }

    @Test
    public void synchronizationManager_syncCurrentMapAndMainQuest_synchronizationInteractionNeverCalled() throws InterruptedException {
        QuestModel questModel = QuestModel.builder().name("test").build();

        when(this.questRepository.getMainQuest()).thenReturn(questModel);

        this.synchronizationManager.sync(Arrays.asList(DataType.CURRENT_MAP, DataType.MAIN_QUEST), "testMap");

        lock.await(500, TimeUnit.MILLISECONDS);

        verify(this.questRepository, times(1)).getMainQuest();
        verify(this.dataStorage, times(1)).evictAndSaveMainQuest(any(QuestModel.class));
        verify(this.interactionRepository, times(0)).findInteractions(anyString());
        verify(this.dataStorage, times(0)).evictAndSaveInteractions(anyList());
        verify(this.dataStorage, times(1)).evictAndSaveCurrentMap("testMap");
    }
}
