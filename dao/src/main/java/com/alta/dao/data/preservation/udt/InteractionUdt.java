package com.alta.dao.data.preservation.udt;

import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.UDT;
import lombok.*;

import java.util.UUID;

/**
 * Represent preservation of interaction UDT in database.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@UDT(name = "interaction")
public class InteractionUdt {

    @Field(name = "interaction_uuid")
    private UUID interactionUuid;

    @Field(name = "map_name")
    private String mapName;

    @Field(name = "completed")
    private boolean isCompleted;

}
