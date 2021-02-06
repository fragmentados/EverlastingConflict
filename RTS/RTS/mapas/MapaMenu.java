/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RTS.mapas;

import ElementosSlick2D.BotonSimple;
import ElementosSlick2D.ComboBox;
import RTS.campaign.tutorial.ClarkTutorial;
import RTS.campaign.tutorial.EterniumTutorial;
import RTS.campaign.tutorial.FenixTutorial;
import RTS.campaign.tutorial.GuardianesTutorial;
import RTS.campaign.tutorial.MaestrosTutorial;
import RTS.Experimento_Multiplayer_Real.Client;
import RTS.Experimento_Multiplayer_Real.Message;
import RTS.gestion.Jugador;
import RTS.gestion.Partida;
import RTS.razas.Clark;
import RTS.razas.Eternium;
import RTS.razas.Fenix;
import RTS.razas.Guardianes;
import RTS.razas.Maestros;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.TextField;

/**
 *
 * @author Elías
 */
public class MapaMenu extends Mapa {

    public Partida partida;
    public BotonSimple combate, multijugador, tutorial, clark, eternium, fenix, maestros, guardianes, volver, aceptar, salir, opciones;
    public String otro_usuario, otro_usuario_raza;
    private Client client;
    private final String titulo = "THE EVERLASTING CONFLICT";
    private ComboBox r1;
    private TextField usuario;
    private boolean seleccion;
    public boolean start;

    public MapaMenu() {
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        otro_usuario = "Libre";
        otro_usuario_raza = "-";
        combate = new BotonSimple("Combate", 600, 300);
        multijugador = new BotonSimple("Multijugador", 600, 400);
        tutorial = new BotonSimple("Tutorial", 600, 500);
        opciones = new BotonSimple("Opciones", 600, 600);
        salir = new BotonSimple("Salir", 1365 - 60, 0);
        clark = new BotonSimple("Tutorial Clark", 600, 100);
        eternium = new BotonSimple("Tutorial Eternium", 600, 200);
        fenix = new BotonSimple("Tutorial Fénix", 600, 300);
        maestros = new BotonSimple("Tutorial Maestros", 600, 400);
        guardianes = new BotonSimple("Tutorial Guardianes", 600, 500);
        volver = new BotonSimple("Volver", 600, 600);
        aceptar = new BotonSimple("Aceptar", 700, 600);
        List<String> l = new ArrayList<>();
        l.add(Fenix.nombre_raza);
        l.add(Clark.nombre_raza);
        l.add(Eternium.nombre_raza);
        l.add(Maestros.nombre_raza);
        l.add(Guardianes.nombre_raza);
        usuario = new TextField(container, container.getDefaultFont(), 100, 50, 100, 20);
        usuario.setText("Prueba");
        r1 = new ComboBox(l, 700, 400);
        seleccion = aceptar.activado = volver.activado = maestros.activado = fenix.activado = eternium.activado = clark.activado = guardianes.activado = false;
    }

    public void start() {
        try {
            MapaEjemplo.mapac.client = client;
            Client.mapa_ejemplo.cambio_de_mapa(Client.canvas.getContainer(), new Partida(new Jugador(usuario.getText(), r1.opcion_seleccionada), new Jugador(otro_usuario, otro_usuario_raza)), "Campo");
        } catch (SlickException ex) {
            Logger.getLogger(MapaMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        Input input = container.getInput();
        if (start) {
            start();
        }
        //Boton izquierdo
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            if (combate.presionado(input.getMouseX(), input.getMouseY())) {
                multijugador.activado = opciones.activado = tutorial.activado = combate.activado = false;
                aceptar.activado = volver.activado = seleccion = true;
                client = new Client("localhost", 1500, usuario.getText());
                if (!client.start()) {
                    return;
                }
            } else if (aceptar.presionado(input.getMouseX(), input.getMouseY())) {
                client.send_start();
                start = true;
            } else if (multijugador.presionado(input.getMouseX(), input.getMouseY())) {

            } else if (tutorial.presionado(input.getMouseX(), input.getMouseY())) {
                volver.activado = guardianes.activado = fenix.activado = eternium.activado = clark.activado = maestros.activado = true;
                opciones.activado = combate.activado = tutorial.activado = false;
            } else if (fenix.presionado(input.getMouseX(), input.getMouseY())) {
                Client.mapa_ejemplo.cambio_de_mapa(container, new FenixTutorial(), "Campo");
            } else if (clark.presionado(input.getMouseX(), input.getMouseY())) {
                Client.mapa_ejemplo.cambio_de_mapa(container, new ClarkTutorial(), "Campo");
            } else if (eternium.presionado(input.getMouseX(), input.getMouseY())) {
                Client.mapa_ejemplo.cambio_de_mapa(container, new EterniumTutorial(), "Campo");
            } else if (maestros.presionado(input.getMouseX(), input.getMouseY())) {
                Client.mapa_ejemplo.cambio_de_mapa(container, new MaestrosTutorial(), "Campo");
            } else if (guardianes.presionado(input.getMouseX(), input.getMouseY())) {
                Client.mapa_ejemplo.cambio_de_mapa(container, new GuardianesTutorial(), "Campo");
            } else if (volver.presionado(input.getMouseX(), input.getMouseY())) {
                if (seleccion) {
                    aceptar.activado = seleccion = false;
                } else {
                    maestros.activado = guardianes.activado = fenix.activado = eternium.activado = clark.activado = false;
                }
                multijugador.activado = opciones.activado = combate.activado = tutorial.activado = true;
                volver.activado = false;
            } else if (opciones.presionado(input.getMouseX(), input.getMouseY())) {

            } else if (salir.presionado(input.getMouseX(), input.getMouseY())) {
                Client.ventana_mapa.dispose();
                System.exit(0);
            } else if (r1.desplegar.presionado(input.getMouseX(), input.getMouseY())) {
                r1.desplegar.efecto();
            }
            if (r1.elegir_opcion(input.getMouseX(), input.getMouseY())) {
                client.send_message(new Message(Message.raze_type, r1.opcion_seleccionada));
            }
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.setBackground(new Color(0.3f, 0.3f, 0.1f, 0f));
        g.drawString(titulo, (1365 - (titulo.length() * 10)) / 2, 0);
        combate.dibujar(g);
        multijugador.dibujar(g);
        tutorial.dibujar(g);
        eternium.dibujar(g);
        clark.dibujar(g);
        fenix.dibujar(g);
        maestros.dibujar(g);
        guardianes.dibujar(g);
        volver.dibujar(g);
        aceptar.dibujar(g);
        opciones.dibujar(g);
        salir.dibujar(g);
        g.drawString("Usuario: ", 20, 50);
        usuario.render(container, g);
        if (seleccion) {
            g.drawString("Jugador nº 1: ", 200, 400);
            g.drawString(usuario.getText(), 450, 400);
            g.drawString("Raza: ", 600, 400);
            g.drawString("Jugador nº 2: ", 200, 500);
            g.drawString(otro_usuario, 450, 500);
            g.drawString("Raza: ", 600, 500);
            g.drawString(otro_usuario_raza, 700, 500);
            r1.dibujar(g);
        }
    }

}
