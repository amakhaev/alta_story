package com.alta.ui.scene;

import com.alta.ui.component.LoadingComponent;
import com.alta.ui.componentContainer.Container;

/**
 * Provides base scene class.
 */
public class LoadScene extends Scene {

    /**
     * Initialize new instance of {@link LoadScene}.
     */
    public LoadScene() {
        super(new Container());
        this.componentContainer.add(new LoadingComponent());
    }
}
