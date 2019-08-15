package com.alta.dao.data.common.effect.background;

import com.alta.dao.data.common.effect.EffectDataModel;
import lombok.Getter;
import lombok.Setter;

/**
 * The data model that describes increment preservation chapter.
 */
@Getter
@Setter
public class IncrementChapterIndicatorDataModel extends EffectDataModel {

    /**
     * Initialize new instance of {@link IncrementChapterIndicatorDataModel}.
     */
    public IncrementChapterIndicatorDataModel() {
        super(EffectType.INCREMENT_CHAPTER_INDICATOR);
    }
}
