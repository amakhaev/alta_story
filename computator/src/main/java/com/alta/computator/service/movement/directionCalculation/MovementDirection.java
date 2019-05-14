package com.alta.computator.service.movement.directionCalculation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Provides possibles directions for participantComputator
 */
public enum MovementDirection {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    private static final List<MovementDirection> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static MovementDirection randomDirection()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
