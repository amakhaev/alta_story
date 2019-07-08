package com.alta.dao.data.quest;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;

/**
 * Provides the const names of available quests.
 */
@UtilityClass
public class AvailableQuest {

    public static final String MAIN_QUEST = "main";

    /**
     * The list of available quests by name.
     */
    public static final List<String> QUESTS = Arrays.asList(MAIN_QUEST);
}
