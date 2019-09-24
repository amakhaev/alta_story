import com.alta.behaviorprocess.BehaviorProcessInjectorModule;
import com.alta.dao.DaoInjectorModule;
import com.alta.dao.domain.snapshot.PreservationSnapshotService;
import com.alta.engine.EngineInjectorModule;
import com.alta.mediator.Mediator;
import com.alta.mediator.MediatorInjectorModule;
import com.alta.scene.SceneInjectorModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new MediatorInjectorModule(),
                new DaoInjectorModule(),
                new EngineInjectorModule(),
                new SceneInjectorModule(),
                new BehaviorProcessInjectorModule()
        );
        injector.getInstance(Mediator.class).loadSavedGameAndStart();

        /*try (Cluster cluster = Cluster.builder()
                .addContactPoint("127.0.0.1")
                .build()) {
            Session session = cluster.connect("alta_game");

            MappingManager manager = new MappingManager(session);

            PreservationAccessor preservationAccessor = manager.createAccessor(PreservationAccessor.class);
            Mapper<PreservationEntity> preservationMapper = manager.mapper(PreservationEntity.class, "alta_game");

            // PreservationEntity preservationEntity = preservationMapper.get(UUID.fromString("2d83b080-d9f9-11e9-8fb6-07d14f30e96a"), 1);

            System.out.println("Completed");
        }*/
    }
}
