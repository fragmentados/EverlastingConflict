/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict;

import everlastingconflict.mapas.VentanaCombate;
import everlastingconflict.mapas.VentanaPrincipal;
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
    public static VentanaPrincipal map;
    public static JFrame ventana_mapa; 
    
    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        VentanaCombate.VIEWPORT_SIZE_X = screenSize.width;
        VentanaCombate.VIEWPORT_SIZE_Y = screenSize.height;
        map = new VentanaPrincipal();
        try {
            canvas = new CanvasGameContainer(map);
            canvas.getContainer().setShowFPS(false);
            ventana_mapa = new JFrame();
            ventana_mapa.setUndecorated(true);
            ventana_mapa.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
            ventana_mapa.setVisible(true);
            ventana_mapa.add(canvas);
            canvas.start();
            ventana_mapa.setExtendedState(Frame.MAXIMIZED_BOTH);
            ventana_mapa.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            canvas.getContainer().setFullscreen(true);
        } catch (SlickException e) {
        }
    }
}
