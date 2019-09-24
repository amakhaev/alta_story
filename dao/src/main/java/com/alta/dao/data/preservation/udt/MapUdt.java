package com.alta.dao.data.preservation.udt;

import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.UDT;
import lombok.*;

import java.util.UUID;

/**
 * Represent preservation of map UDT in database.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@UDT(name = "map")
public class MapUdt {

    @Field(name = "participant_uuid")
    private UUID participantUuid;

    @Field(name = "map_name")
    private String mapName;

    @Field(name = "visible")
    private boolean isVisible;

}
