/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict;

import everlastingconflict.mapas.MapaEjemplo;
import java.awt.Frame;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.SlickException;
 
/**
 *
 * @author Elías    
 */
public class RTS {

    public static CanvasGameContainer canvas;
    public static MapaEjemplo mapa_ejemplo;
    public static JFrame ventana_mapa; 
    
    public static void main(String[] args) { 
        mapa_ejemplo = new MapaEjemplo();   
        try {
            canvas = new CanvasGameContainer(mapa_ejemplo);
            canvas.getContainer().setShowFPS(false);
            ventana_mapa = new JFrame();
            ventana_mapa.setUndecorated(true);
            ventana_mapa.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
            ventana_mapa.setVisible(true);
            ventana_mapa.add(canvas);
            canvas.start();
            ventana_mapa.setExtendedState(Frame.MAXIMIZED_BOTH);
            ventana_mapa.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        } catch (SlickException e) {
        }
    }
}
