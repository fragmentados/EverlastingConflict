/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.ventanas;

import everlastingconflict.RTS;
import everlastingconflict.elementos.util.ElementosComunes;
import everlastingconflict.gestion.Partida;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * @author Elías
 */
public class VentanaPrincipal extends BasicGame {

    public static VentanaCombate ventanaCombate = new VentanaCombate();
    public static VentanaIntro ventanaIntro = new VentanaIntro();
    public static VentanaMenu ventanaMenu = new VentanaMenu();
    public static VentanaSeleccion ventanaSeleccion = new VentanaSeleccion();
    public static Ventana ventanaActual;

    public static void windowSwitch(GameContainer container, Partida p, String t) throws SlickException {
        Ventana contador = null;
        switch (t) {
            case "Menu":
                contador = ventanaMenu;
                break;
            case "Campo":
                ventanaCombate.partida = p;
                contador = ventanaCombate;
                break;
            case "Intro":
                ventanaIntro.partida = p;
                contador = ventanaIntro;
                break;
            case "Seleccion":
                contador = ventanaSeleccion;
                break;
        }
        if (contador != null) {
            contador.init(container);
            ventanaActual = contador;
        }

    }

    public VentanaPrincipal() {
        super("VentanaPrincipal");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        ElementosComunes.init();
        windowSwitch(container, new Partida(), "Menu");
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        try {
            ventanaActual.update(container, delta);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        try {
            ventanaActual.render(container, g);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public static void exit(GameContainer container) {
        RTS.canvas.dispose();
        RTS.mainFrame.dispose();
        container.exit();
        System.exit(0);
    }
}
