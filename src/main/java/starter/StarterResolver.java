package starter;

import com.google.common.base.Strings;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Resolves the parameters from command line.
 */
@Slf4j
@UtilityClass
public class StarterResolver {

    private static final String GAME_START_TYPE = "game";
    private static final String CHECK_START_TYPE = "check";

    /**
     * Resolves the start type of application.
     *
     * @param startType - the start type as string.
     */
    public Starter resolve(String startType) {
        if (Strings.isNullOrEmpty(startType)) {
            log.info("Start type is null or empty. Select default: {}", GameStarter.class.getSimpleName());
            return new GameStarter();
        }

        log.info("Begin resolving start type: {}", startType);
        String cleanStartType = startType.trim().toLowerCase();
        switch (cleanStartType) {
            case GAME_START_TYPE:
                log.info("Start type resolved: {}", GameStarter.class.getSimpleName());
                return new GameStarter();
            case CHECK_START_TYPE:
                log.info("Start type resolved: {}", CheckSoftStarter.class.getSimpleName());
                return new CheckSoftStarter();
            default:
                log.info("Start type not found, select default: {}", GameStarter.class.getSimpleName());
                return new GameStarter();
        }
    }

}
