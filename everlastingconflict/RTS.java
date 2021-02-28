/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict;

import everlastingconflict.ventanas.VentanaCombate;
import everlastingconflict.ventanas.VentanaPrincipal;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.SlickException;

import javax.swing.*;
import java.awt.*;
 
/**
 *
 * @author Elías    
 */
public class RTS {

    public static CanvasGameContainer canvas;
    public static VentanaPrincipal mainController;
    public static JFrame mainFrame;
    public static final boolean DEBUG_MODE = false;
    
    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        VentanaCombate.VIEWPORT_SIZE_WIDTH = screenSize.width;
        VentanaCombate.VIEWPORT_SIZE_HEIGHT = screenSize.height;
        mainController = new VentanaPrincipal();
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
            canvas.getContainer().setFullscreen(true);
        } catch (SlickException e) {
        }
    }
}
