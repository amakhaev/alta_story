package com.alta.engine.presenter;

import com.alta.engine.view.MessageBoxView;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * Provides the presenter relate to {@link com.alta.engine.view.MessageBoxView}
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MessageBoxPresenter {

    private final MessageBoxView messageBoxView;

    /**
     * Shows the title message on the top of scene.
     *
     * @param title - the title that should be shown.
     */
    public void showTitle(String title) {
        this.messageBoxView.showTitle(title);
    }

    /**
     * Shown the message on the bottom of scene.
     *
     * @param message - the message to be shown.
     */
    public void showMessage(String message) {
        this.messageBoxView.showMessage(message);
    }
}
