package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

public class DisplayManager {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int FPS_CAP = 120;

    public static void createDisplay() {

        ContextAttribs contextAttribs = new ContextAttribs(3,2)
                .withForwardCompatible(true)
                .withProfileCore(true);

        try {
            // 1. determino la size del display
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            // 2. creo il display
            Display.create(new PixelFormat(), contextAttribs);
            // posso settare il titolo del display
            Display.setTitle("Tutorial");
        } catch (LWJGLException e) {
            throw new RuntimeException(e);
        }

        // 3. dire a OpenGL dove fare render
        GL11.glViewport(0, 0, WIDTH, HEIGHT); // usa tutto lo schermo
    }

    public static void updateDisplay(){

        // Sincronizza il display ad un determinato FPS
        Display.sync(FPS_CAP);
        Display.update();

    }

    public static void closeDisplay(){

        Display.destroy();

    }
}
