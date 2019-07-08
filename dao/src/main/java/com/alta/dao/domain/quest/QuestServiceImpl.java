package com.alta.dao.domain.quest;

import com.alta.dao.data.common.effect.EffectDataModel;
import com.alta.dao.data.quest.QuestModel;
import com.alta.dao.domain.common.effect.EffectDeserializer;
import com.alta.dao.domain.quest.matcher.QuestStepChapterIndicatorMatcher;
import com.alta.dao.domain.quest.matcher.QuestStepNumberMatcher;
import com.alta.utils.JsonParser;
import com.google.common.base.Strings;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides the service to make CRUD operations with quests.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class QuestServiceImpl implements QuestService {

    private final QuestDeserializer questDeserializer;
    private final EffectDeserializer effectDeserializer;

    /**
     * Gets the quest by given path;
     *
     * @param path - the path to descriptor file/
     * @return found {@link QuestModel} instance.
     */
    @Override
    public QuestModel getQuest(String path) {
        if (Strings.isNullOrEmpty(path)) {
            log.error("Path to descriptor file is null or empty.");
            return null;
        }

        this.questDeserializer.setStepMatchers(null);
        Map<Type, JsonDeserializer> deserializers = new HashMap<>();
        deserializers.put(new TypeToken<QuestModel>(){}.getType(), this.questDeserializer);
        deserializers.put(new TypeToken<ArrayList<EffectDataModel>>(){}.getType(), this.effectDeserializer);

        return JsonParser.parse(
                this.getClass().getClassLoader().getResource(path).getPath(),
                new TypeToken<QuestModel>(){}.getType(),
                deserializers
        );
    }

    /**
     * Gets the quest by given path and specified list of steps.
     *
     * @param path                 - the path to descriptor file.
     * @param chapterIndicatorFrom - the character indicator from.
     * @param chapterIndicatorTo   - the character indicator to.
     * @return found {@link QuestModel} instance.
     */
    @Override
    public QuestModel getQuestWithSpecifiedSteps(String path, int chapterIndicatorFrom, int chapterIndicatorTo) {
        if (Strings.isNullOrEmpty(path)) {
            log.error("Path to descriptor file is null or empty.");
            return null;
        }

        this.questDeserializer.setStepMatchers(Collections.singletonList(
                new QuestStepChapterIndicatorMatcher(chapterIndicatorFrom, chapterIndicatorTo))
        );
        Map<Type, JsonDeserializer> deserializers = new HashMap<>();
        deserializers.put(new TypeToken<QuestModel>(){}.getType(), this.questDeserializer);
        deserializers.put(new TypeToken<ArrayList<EffectDataModel>>(){}.getType(), this.effectDeserializer);

        return JsonParser.parse(
                this.getClass().getClassLoader().getResource(path).getPath(),
                new TypeToken<QuestModel>(){}.getType(),
                deserializers
        );
    }

    /**
     * Gets the quest path and specified list of steps.
     *
     * @param path              - the path to descriptor file.
     * @param currentStepNumber - the number of current step.
     * @return found {@link QuestModel} instance.
     */
    @Override
    public QuestModel getQuestWithSpecifiedSteps(String path, int currentStepNumber) {
        if (Strings.isNullOrEmpty(path)) {
            log.error("Path to descriptor file is null or empty.");
            return null;
        }

        this.questDeserializer.setStepMatchers(Collections.singletonList(new QuestStepNumberMatcher(currentStepNumber)));
        Map<Type, JsonDeserializer> deserializers = new HashMap<>();
        deserializers.put(new TypeToken<QuestModel>(){}.getType(), this.questDeserializer);
        deserializers.put(new TypeToken<ArrayList<EffectDataModel>>(){}.getType(), this.effectDeserializer);

        return JsonParser.parse(
                this.getClass().getClassLoader().getResource(path).getPath(),
                new TypeToken<QuestModel>(){}.getType(),
                deserializers
        );
    }
}
