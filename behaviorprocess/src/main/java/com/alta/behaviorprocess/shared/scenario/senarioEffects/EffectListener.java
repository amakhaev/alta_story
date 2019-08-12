package com.alta.behaviorprocess.shared.scenario.senarioEffects;

import com.alta.behaviorprocess.data.common.FaceSetDescription;

/**
 * Provides the listener of effects.
 */
public interface EffectListener {

    /**
     * Shows the message.
     *
     * @param targetUuid        - the uuid of target NPC.
     * @param message           - the message to be shown.
     */
    void onShowMessage(String targetUuid, String message);

    /**
     * Shows the message.
     *
     * @param targetUuid        - the uuid of target NPC.
     * @param message           - the message to be shown.
     * @param speakerUuid       - the uuid of speaker.
     * @param speakerEmotion    - the emotion that should be shown when speaker say.
     */
    void onShowMessage(String targetUuid, String message, String speakerUuid, String speakerEmotion);

    /**
     * Shows the message.
     *
     * @param targetUuid            - the uuid of target NPC.
     * @param message               - the message to be shown.
     * @param faceSetDescription    - the descriptor of face set.
     * @param speakerEmotion        - the emotion that should be shown when speaker say.
     */
    void onShowMessage(String targetUuid, String message, FaceSetDescription faceSetDescription, String speakerEmotion);

    /**
     * Triggers the state of message box to next one.
     *
     * @param targetUuid        - the uuid of target NPC.
     * @param completeCallback  - the callback to be invoked after complete showing of message.
     */
    void onTriggerNextStateForMessage(String targetUuid, Runnable completeCallback);

    /**
     * Hides the facility with given uuid.
     *
     * @param facilityUuid - the uuid of facility to be hide.
     */
    void onHideFacility(String facilityUuid);

    /**
     * Shows the facility with given uuid.
     *
     * @param facilityUuid - the uuid of facility to be show.
     */
    void onShowFacility(String facilityUuid);

    /**
     * Runs the movement process for NPC with given UUID.
     *
     * @param npcTargetUuid - the UUID of NPC to be moved.
     * @param x             - the target X coordinate.
     * @param y             - the target Y coordinate.
     */
    void onRouteMovement(String npcTargetUuid, int x, int y);
}
