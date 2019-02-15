package com.alta.mediator.sceneUtility;

import com.google.inject.Singleton;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

/**
 * Provides the input listener of scene
 */
@Singleton
public class SceneInputListener implements KeyListener {

    @Override
    public void keyPressed(int i, char c) {
        System.out.println("pressed " + i);
    }

    @Override
    public void keyReleased(int i, char c) {
        System.out.println("released " + i);
    }

    @Override
    public void setInput(Input input) {

    }

    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public void inputEnded() {

    }

    @Override
    public void inputStarted() {

    }
}
