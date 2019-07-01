package com.alta.dao.domain.quest;

import com.alta.dao.data.common.effect.EffectDataModel;
import com.alta.dao.data.quest.QuestModel;
import com.alta.dao.domain.common.effect.EffectDeserializer;
import com.alta.utils.JsonParser;
import com.google.common.base.Strings;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
