/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.mapas;

import everlastingconflict.Experimento_Multiplayer_Real.Client;
import everlastingconflict.Experimento_Multiplayer_Real.Message;
import everlastingconflict.ai.AI;
import everlastingconflict.campaign.tutorial.*;
import everlastingconflict.elementosvisuales.BotonSimple;
import everlastingconflict.elementosvisuales.ComboBox;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.Raza;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.TextField;

import java.util.Arrays;

/**
 *
 * @author Elías
 */
public class VentanaMenu extends Ventana {

    public Partida partida;
    public BotonSimple combate, multijugador, tutorial, clark, eternium, fenix, maestros, guardianes, volver, aceptar, salir, opciones;
    public String otro_usuario, otro_usuario_raza;
    private Client client;
    private final String titulo = "THE EVERLASTING CONFLICT";
    private ComboBox racePlayer1;
    private ComboBox racePlayer2;
    private ComboBox player2Type;
    private ComboBox mapType;
    private TextField usuario;
    private boolean seleccion;
    public boolean start;

    public VentanaMenu() {
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
        volver = new BotonSimple("Volver", 600, 700);
        aceptar = new BotonSimple("Aceptar", 700, 700);
        usuario = new TextField(container, container.getDefaultFont(), 100, 50, 100, 20);
        usuario.setText("Prueba");
        racePlayer1 = new ComboBox(Raza.getAllRaceNames(), 700, 400);
        player2Type = new ComboBox(Arrays.asList("AI", "Jugador"), 200, 500);
        racePlayer2 = new ComboBox(Raza.getAllRaceNames(), 700, 500);
        mapType = new ComboBox(MapEnum.getAllNames(), 300, 600);
        seleccion = aceptar.activado = volver.activado = maestros.activado = fenix.activado = eternium.activado = clark.activado = guardianes.activado = false;
    }

    public void start(GameContainer container) {
        try {
            VentanaPrincipal.mapac.client = client;
            Jugador secondPlayer;
            if ("AI".equals(player2Type.opcion_seleccionada)) {
                secondPlayer = AI.crearAI(racePlayer2.opcion_seleccionada);
            } else {
                secondPlayer = new Jugador(otro_usuario, otro_usuario_raza);
            }
            Partida partida = new Partida(new Jugador(usuario.getText(), racePlayer1.opcion_seleccionada), secondPlayer, MapEnum.findByName(mapType.opcion_seleccionada));
            VentanaPrincipal.cambio_de_mapa(container, partida, "Campo");
        } catch (SlickException ex) {
        }
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        Input input = container.getInput();
        if (start) {
            start(container);
            start = false;
        }
        //Boton izquierdo
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            if (combate.presionado(input.getMouseX(), input.getMouseY())) {
                multijugador.activado = opciones.activado = tutorial.activado = combate.activado = false;
                aceptar.activado = volver.activado = seleccion = true;
                /*client = new Client("localhost", 1500, usuario.getText());
                if (!client.start()) {
                    return;
                }*/
            } else if (aceptar.presionado(input.getMouseX(), input.getMouseY())) {
                //client.send_start();
                start = true;
            } else if (multijugador.presionado(input.getMouseX(), input.getMouseY())) {

            } else if (tutorial.presionado(input.getMouseX(), input.getMouseY())) {
                volver.activado = guardianes.activado = fenix.activado = eternium.activado = clark.activado = maestros.activado = true;
                opciones.activado = combate.activado = tutorial.activado = false;
            } else if (fenix.presionado(input.getMouseX(), input.getMouseY())) {
                VentanaPrincipal.cambio_de_mapa(container, new FenixTutorial(), "Campo");
            } else if (clark.presionado(input.getMouseX(), input.getMouseY())) {
                VentanaPrincipal.cambio_de_mapa(container, new ClarkTutorial(), "Campo");
            } else if (eternium.presionado(input.getMouseX(), input.getMouseY())) {
                VentanaPrincipal.cambio_de_mapa(container, new EterniumTutorial(), "Campo");
            } else if (maestros.presionado(input.getMouseX(), input.getMouseY())) {
                VentanaPrincipal.cambio_de_mapa(container, new MaestrosTutorial(), "Campo");
            } else if (guardianes.presionado(input.getMouseX(), input.getMouseY())) {
                VentanaPrincipal.cambio_de_mapa(container, new GuardianesTutorial(), "Campo");
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
                container.exit();
                //Client.ventana_mapa.dispose();
                System.exit(0);
            } else if (racePlayer1.desplegar.presionado(input.getMouseX(), input.getMouseY())) {
                racePlayer1.desplegar.efecto();
            } else  if (player2Type.desplegar.presionado(input.getMouseX(), input.getMouseY())) {
                player2Type.desplegar.efecto();
            } else  if (racePlayer2.desplegar.presionado(input.getMouseX(), input.getMouseY())) {
                racePlayer2.desplegar.efecto();
            } else  if (mapType.desplegar.presionado(input.getMouseX(), input.getMouseY())) {
                mapType.desplegar.efecto();
            }
            if (racePlayer1.checkOptionSelected(input.getMouseX(), input.getMouseY())) {
                //client.send_message(new Message(Message.raze_type, racePlayer1.opcion_seleccionada));
            }
            player2Type.checkOptionSelected(input.getMouseX(), input.getMouseY());
            racePlayer2.checkOptionSelected(input.getMouseX(), input.getMouseY());
            mapType.checkOptionSelected(input.getMouseX(), input.getMouseY());
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
            racePlayer1.dibujar(g);
            player2Type.dibujar(g);
            g.drawString(otro_usuario, 450, 500);
            g.drawString("Raza: ", 600, 500);
            racePlayer2.dibujar(g);
            g.drawString("Mapa: ", 200, 600);
            mapType.dibujar(g);
        }
    }

}
