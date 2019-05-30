package com.alta.scene.messageBox;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Provides the descriptor of face set.
 */
@Getter
@AllArgsConstructor
public class FaceSetDescriptor {

    private int faceSetTileWidth;
    private int faceSetTileHeight;
    private int tileToShowX;
    private int tileToShowY;
    private String faceSetFilePath;

}
