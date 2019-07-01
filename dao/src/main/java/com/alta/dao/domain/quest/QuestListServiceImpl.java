package com.alta.dao.domain.quest;

import com.alta.dao.data.common.effect.EffectDataModel;
import com.alta.dao.data.quest.QuestListItemModel;
import com.alta.dao.domain.common.effect.EffectDeserializer;
import com.alta.dao.domain.quest.matcher.QuestNameMatcher;
import com.alta.utils.JsonParser;
import com.google.common.base.Strings;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Provides the service to make CRUD operations with quest list.
 */
@Slf4j
public class QuestListServiceImpl implements QuestListService {

    private final static int CACHE_ITEM_LIMIT = 30;

    private final EffectDeserializer effectDeserializer;
    private Deque<QuestListItemModel> cache;

    /**
     * Initialize new instance of {@link QuestListServiceImpl}.
     */
    @Inject
    public QuestListServiceImpl(EffectDeserializer effectDeserializer) {
        this.effectDeserializer = effectDeserializer;
        this.cache = new LinkedList<>();
    }

    /**
     * Finds the quest by given name.
     *
     * @param name - the name of quest.
     * @return found {@link QuestListItemModel} instance.
     */
    @Override
    public QuestListItemModel findQuestByName(String name) {
        if (Strings.isNullOrEmpty(name)) {
            log.warn("The findQuestByName was calling but quest name is null or empty.");
            return null;
        }

        QuestListItemModel questListItemModel = this.cache.stream()
                .filter(cacheItem -> cacheItem.getName().equals(name))
                .findFirst()
                .orElse(null);

        if (questListItemModel != null) {
            return questListItemModel;
        }

        QuestListDeserializer deserializerByName = new QuestListDeserializer(Collections.singletonList(new QuestNameMatcher(name)));

        Map<Type, JsonDeserializer> deserializers = new HashMap<>();
        deserializers.put(new TypeToken<ArrayList<QuestListItemModel>>(){}.getType(), deserializerByName);
        deserializers.put(new TypeToken<ArrayList<EffectDataModel>>(){}.getType(), this.effectDeserializer);

        List<QuestListItemModel> questsFromFile = JsonParser.parse(
                this.getClass().getClassLoader().getResource("scene_data/quests/available_quests.dscr").getPath(),
                new TypeToken<ArrayList<QuestListItemModel>>(){}.getType(),
                deserializers
        );

        if (questsFromFile.size() == 0) {
            log.error("Quest with name {} not found", name);
            return null;
        }
        this.addToCache(questsFromFile);

        if (questsFromFile.size() > 1) {
            log.error("Found {} quests with name {}", questsFromFile.size(), name);
        }

        return questsFromFile.get(0);
    }

    private void addToCache(List<QuestListItemModel> questListItems) {
        this.cache.addAll(questListItems);

        if (this.cache.size() <= CACHE_ITEM_LIMIT) {
            return;
        }

        do {
            this.cache.pollFirst();
        }
        while (this.cache.size() > CACHE_ITEM_LIMIT || this.cache.size() != 0);
    }
}
