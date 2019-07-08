package com.alta.dao.data.quest;

import lombok.Builder;
import lombok.Getter;

/**
 * Provides the lightweight model of quest.
 */
@Getter
@Builder
public class QuestListItemModel {

    private final String uuid;
    private final String name;
    private final String pathToDescriptor;
    private Integer chapterIndicatorFrom;
    private Integer chapterIndicatorTo;

}
