package com.alta.interaction.scenario;

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
}
