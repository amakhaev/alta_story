package com.alta.dao.data.common.effect.background;

import com.alta.dao.data.common.effect.EffectDataModel;
import lombok.Getter;

/**
 * The data model that describes increment preservation chapter.
 */
@Getter
public class UpdateChapterIndicatorDataModel extends EffectDataModel {

    private final int chapterValue;

    /**
     * Initialize new instance of {@link UpdateChapterIndicatorDataModel}.
     */
    public UpdateChapterIndicatorDataModel(int chapterValue) {
        super(EffectType.UPDATE_CHAPTER_INDICATOR);
        this.chapterValue = chapterValue;
    }
}
