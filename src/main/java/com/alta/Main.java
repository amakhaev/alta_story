package com.alta;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main {

    public Main() {
        try {
            AppGameContainer container = new AppGameContainer(new BaseGameContainer());
            container.setDisplayMode(800, 600, false);
            container.start();
        } catch(SlickException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Main();
    }

}
