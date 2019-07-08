package com.alta.behaviorprocess.data.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;
import java.util.Map;

/**
 * Provides the description for face set.
 */
@Getter
@AllArgsConstructor
public class FaceSetDescription {

    private int tileWidth;
    private int tileHeight;
    private Map<String, Point> emotions;
    private String pathToImageSet;

}
