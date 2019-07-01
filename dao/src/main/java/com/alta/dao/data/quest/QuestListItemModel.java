package com.alta.dao.data.quest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Provides the lightweight model of quest.
 */
@Getter
@RequiredArgsConstructor
public class QuestListItemModel {

    private final String uuid;
    private final String name;
    private final String pathToDescriptor;

}
