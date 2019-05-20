package com.alta.dao.domain.map.internalEntities;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the entity that describes the alterable NPC
 */
@Getter
@Setter
public class AlterableNpcEntity extends NpcEntity {

    private MovementRulesEntity movementRules;

}
