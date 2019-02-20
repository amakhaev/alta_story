package com.alta.computator.computationExceptions;

/**
 * Provides the ustom exception that indicates about any mistakes in the altitude map
 */
public class AltitudeMapException extends RuntimeException {

    /**
     * Initialize new instance of {@link AltitudeMapException}
     */
    public AltitudeMapException(String message) {
        super(message);
    }

}
