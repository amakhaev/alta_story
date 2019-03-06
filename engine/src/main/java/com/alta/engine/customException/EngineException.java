package com.alta.engine.customException;

/**
 * Provides the exception that indicates about any problems in the engine project
 */
public class EngineException extends RuntimeException {

    /**
     * Initialize new instance of {@link EngineException}
     *
     * @param message - the message that describes the reason of exception
     */
    public EngineException(String message) {
        super(message);
    }

}
