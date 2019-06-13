package com.alta.eventStream;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the event that stored data to event of any type
 */
@Getter
@Setter
class GenericEvent<T> {

    private T data;

}
