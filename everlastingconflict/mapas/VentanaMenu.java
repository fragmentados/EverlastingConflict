/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.mapas;

import everlastingconflict.Experimento_Multiplayer_Real.Client;
import everlastingconflict.ai.AI;
import everlastingconflict.campaign.tutorial.Tutorial;
import everlastingconflict.elementosvisuales.BotonSimple;
import everlastingconflict.elementosvisuales.ComboBox;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.RaceNameEnum;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.TextField;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Elías
 */
public class VentanaMenu extends Ventana {

    public Partida partida;
    public BotonSimple combate, multijugador, tutorial, volver, aceptar, salir, opciones;
    public String otro_usuario, otro_usuario_raza;
    private Client client;
    private final String titulo = "THE EVERLASTING CONFLICT";
    private ComboBox racePlayer1;
    private ComboBox racePlayer2;
    private ComboBox player2Type;
    private ComboBox mapType;
    private TextField usuario;
    private boolean seleccion;
    private boolean tutorialMode = false;
    public boolean start;

    public VentanaMenu() {
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        otro_usuario = "Libre";
        otro_usuario_raza = "-";
        tutorial = new BotonSimple("Tutorial", 600, 300);
        combate = new BotonSimple("Combate", 600, 400);
        multijugador = new BotonSimple("Multijugador", 600, 400);
        opciones = new BotonSimple("Opciones", 600, 600);
        salir = new BotonSimple("Salir", 1365 - 60, 0);
        volver = new BotonSimple("Volver", 600, 800);
        aceptar = new BotonSimple("Aceptar", 700, 800);
        usuario = new TextField(container, container.getDefaultFont(), 100, 50, 100, 20);
        usuario.setText("Prueba");
        player2Type = new ComboBox(Arrays.asList("AI", "Jugador"), 200, 600);
        mapType = new ComboBox(MapEnum.getAllNames(), 300, 700);
        seleccion = aceptar.canBeUsed = volver.canBeUsed = false;
        initRaceComboBoxes();
    }

    public void start(GameContainer container) {
        try {
            if (tutorialMode) {
                VentanaPrincipal.cambio_de_mapa(container, Tutorial.createTutorialByRace(racePlayer1.opcion_seleccionada), "Campo");
                tutorialMode = false;
            } else {
                VentanaPrincipal.mapac.client = client;
                Jugador secondPlayer;
                if ("AI".equals(player2Type.opcion_seleccionada)) {
                    secondPlayer = AI.crearAI(racePlayer2.opcion_seleccionada);
                } else {
                    secondPlayer = new Jugador(otro_usuario, otro_usuario_raza);
                }
                Partida partida = new Partida(new Jugador(usuario.getText(), racePlayer1.opcion_seleccionada), secondPlayer, MapEnum.findByName(mapType.opcion_seleccionada));
                VentanaPrincipal.cambio_de_mapa(container, partida, "Campo");
            }
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
            if (combate.isHovered(input.getMouseX(), input.getMouseY())) {
                multijugador.canBeUsed = opciones.canBeUsed = tutorial.canBeUsed = combate.canBeUsed = false;
                aceptar.canBeUsed = volver.canBeUsed = seleccion = true;
                initRaceComboBoxes();
                /*client = new Client("localhost", 1500, usuario.getText());
                if (!client.start()) {
                    return;
                }*/
            } else if (aceptar.isHovered(input.getMouseX(), input.getMouseY())) {
                //client.send_start();
                start = true;
            } else if (multijugador.isHovered(input.getMouseX(), input.getMouseY())) {

            } else if (tutorial.isHovered(input.getMouseX(), input.getMouseY())) {
                tutorialMode = true;
                aceptar.canBeUsed = true;
                volver.canBeUsed = true;
                multijugador.canBeUsed = opciones.canBeUsed = combate.canBeUsed = tutorial.canBeUsed = false;
                initTutorialRaceComboBox();
            } else if (volver.isHovered(input.getMouseX(), input.getMouseY())) {
                volver.canBeUsed = tutorialMode = aceptar.canBeUsed = seleccion = false;
                multijugador.canBeUsed = opciones.canBeUsed = combate.canBeUsed = tutorial.canBeUsed = true;
            } else if (opciones.isHovered(input.getMouseX(), input.getMouseY())) {

            } else if (salir.isHovered(input.getMouseX(), input.getMouseY())) {
                container.exit();
                //Client.ventana_mapa.dispose();
                System.exit(0);
            } else if (racePlayer1.desplegar.isHovered(input.getMouseX(), input.getMouseY())) {
                racePlayer1.desplegar.efecto();
            } else  if (player2Type.desplegar.isHovered(input.getMouseX(), input.getMouseY())) {
                player2Type.desplegar.efecto();
            } else  if (racePlayer2.desplegar.isHovered(input.getMouseX(), input.getMouseY())) {
                racePlayer2.desplegar.efecto();
            } else  if (mapType.desplegar.isHovered(input.getMouseX(), input.getMouseY())) {
                mapType.desplegar.efecto();
            }
            String previousRacePlayer1 = racePlayer1.checkOptionSelected(input.getMouseX(), input.getMouseY());
            if (previousRacePlayer1 != null) {
                racePlayer2.opciones.remove(racePlayer1.opcion_seleccionada);
                if (racePlayer2.opcion_seleccionada.equals(racePlayer1.opcion_seleccionada)) {
                    racePlayer2.opcion_seleccionada = racePlayer2.opciones.get(0);
                }
                racePlayer2.opciones.add(previousRacePlayer1);
                racePlayer2.opciones = RaceNameEnum.sortRaceNames(racePlayer2.opciones);
            }
            String previousRacePlayer2 = racePlayer2.checkOptionSelected(input.getMouseX(), input.getMouseY());
            if (previousRacePlayer2 != null) {
                racePlayer1.opciones.remove(racePlayer2.opcion_seleccionada);
                if (racePlayer1.opcion_seleccionada.equals(racePlayer2.opcion_seleccionada)) {
                    racePlayer1.opcion_seleccionada = racePlayer1.opciones.get(0);
                }
                racePlayer1.opciones.add(previousRacePlayer2);
                racePlayer1.opciones = RaceNameEnum.sortRaceNames(racePlayer1.opciones);
            }
            player2Type.checkOptionSelected(input.getMouseX(), input.getMouseY());
            mapType.checkOptionSelected(input.getMouseX(), input.getMouseY());
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.setBackground(new Color(0.3f, 0.3f, 0.1f, 0f));
        g.drawString(titulo, (1365 - (titulo.length() * 10)) / 2, 0);
        combate.dibujar(g);
        tutorial.dibujar(g);
        /*multijugador.dibujar(g);
        opciones.dibujar(g);
        salir.dibujar(g);*/
        volver.dibujar(g);
        aceptar.dibujar(g);
        g.drawString("Usuario: ", 20, 50);
        usuario.render(container, g);
        if (seleccion || tutorialMode) {
            g.drawString("Raza: ", 600, 400);
            racePlayer1.dibujar(g);
        }
        if (seleccion) {
            g.drawString("Jugador nº 1: ", 200, 400);
            g.drawString(usuario.getText(), 450, 400);
            player2Type.dibujar(g);
            g.drawString(otro_usuario, 450, 600);
            g.drawString("Raza: ", 600, 600);
            racePlayer2.dibujar(g);
            g.drawString("Mapa: ", 200, 700);
            mapType.dibujar(g);
        }
    }

    private void initTutorialRaceComboBox() {
        racePlayer1 = new ComboBox(RaceNameEnum.getAllNames(), 700, 400);
    }

    private void initRaceComboBoxes() {
        List<String> racesPlayer1 = RaceNameEnum.getAllNames();
        racesPlayer1.remove(RaceNameEnum.ETERNIUM.getName());
        racePlayer1 = new ComboBox(racesPlayer1, 700, 400);
        List<String> racesPlayer2 = RaceNameEnum.getAllNames();
        racesPlayer2.remove(RaceNameEnum.FENIX.getName());
        racePlayer2 = new ComboBox(racesPlayer2, 700, 600);
    }

}
