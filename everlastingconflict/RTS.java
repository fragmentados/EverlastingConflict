/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict;

import everlastingconflict.elements.util.ElementosComunes;
import everlastingconflict.windows.WindowCombat;
import everlastingconflict.windows.WindowMain;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.SlickException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RTS {

    public static CanvasGameContainer canvas;
    public static WindowMain mainController;
    public static JFrame mainFrame;
    public static final boolean DEBUG_MODE = false;

    public static void main(String[] args) {
        // Launch new thread for loading bsos
        new Thread(() -> {
            try {
                ElementosComunes.initBackGroundMusics();
            } catch (SlickException e) {
            }
        }).start();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        WindowCombat.VIEWPORT_SIZE_WIDTH = screenSize.width;
        WindowCombat.VIEWPORT_SIZE_HEIGHT = screenSize.height;
        mainController = new WindowMain();
        try {
            canvas = new CanvasGameContainer(mainController);
            canvas.getContainer().setShowFPS(false);
            mainFrame = new JFrame();
            mainFrame.setUndecorated(true);
            mainFrame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
            mainFrame.setVisible(true);
            mainFrame.add(canvas);
            canvas.start();
            mainFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    // call terminate
                    WindowMain.exit(canvas.getContainer());
                }
            });
            canvas.getContainer().setFullscreen(true);
        } catch (SlickException e) {
        }
    }
}
