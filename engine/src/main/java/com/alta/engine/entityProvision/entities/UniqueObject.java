package com.alta.engine.entityProvision.entities;

import java.util.UUID;

/**
 * Provides the interface to identify object as unique
 */
public interface UniqueObject {

    /**
     * Gets the unique identifier of object
     *
     * @return the {@link UUID} instance
     */
    UUID getUuid();

}
