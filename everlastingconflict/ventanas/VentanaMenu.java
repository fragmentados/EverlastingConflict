/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.ventanas;

import everlastingconflict.campaign.tutorial.Tutorial;
import everlastingconflict.elementosvisuales.BotonSimple;
import everlastingconflict.elementosvisuales.ComboBox;
import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.RaceEnum;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.TextField;

/**
 *
 * @author Elías
 */
public class VentanaMenu extends Ventana {

    //private Client client;
    private final String titulo = "THE EVERLASTING CONFLICT";
    public BotonSimple combate, multijugador, tutorial, volver, aceptar, salir, opciones;
    private ComboBox raceTutorial;
    private Image tutorialImage;
    public static TextField usuario;
    public boolean start;
    private boolean tutorialMode = false;

    public VentanaMenu() {
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        tutorial = new BotonSimple("Tutorial", 600, 300);
        combate = new BotonSimple("Combate", 600, 400);
        multijugador = new BotonSimple("Multijugador", 600, 400);
        opciones = new BotonSimple("Opciones", 600, 600);
        salir = new BotonSimple("Salir", VentanaCombate.VIEWPORT_SIZE_WIDTH - 60, 0);
        volver = new BotonSimple("Volver", 600, 800);
        aceptar = new BotonSimple("Aceptar", 700, 800);
        usuario = new TextField(container, container.getDefaultFont(), 100, 50, 100, 20);
        usuario.setText("Prueba");
        aceptar.canBeUsed = volver.canBeUsed = false;
        raceTutorial = new ComboBox("Raza:", RaceEnum.getAllNames(), 700, 400);
        tutorialImage = new Image("media/" + raceTutorial.opcion_seleccionada + ".png");
    }

    public void start(GameContainer container) {
        try {
            VentanaPrincipal.windowSwitch(container, Tutorial.createTutorialByRace(raceTutorial.opcion_seleccionada), "Campo");
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
                aceptar.canBeUsed = volver.canBeUsed = true;
                VentanaPrincipal.windowSwitch(container, null, "Seleccion");
                /*client = new Client("localhost", 1500, usuario.getText());
                if (!client.start()) {
                    return;
                }*/
            } else if (aceptar.isHovered(input.getMouseX(), input.getMouseY())) {
                //client.send_start();
                start = true;
                tutorialMode = false;
            } else if (multijugador.isHovered(input.getMouseX(), input.getMouseY())) {

            } else if (tutorial.isHovered(input.getMouseX(), input.getMouseY())) {
                tutorialMode = aceptar.canBeUsed = true;
                volver.canBeUsed = true;
                multijugador.canBeUsed = opciones.canBeUsed = combate.canBeUsed = tutorial.canBeUsed = false;
            } else if (volver.isHovered(input.getMouseX(), input.getMouseY())) {
                tutorialMode = volver.canBeUsed = aceptar.canBeUsed = false;
                multijugador.canBeUsed = opciones.canBeUsed = combate.canBeUsed = tutorial.canBeUsed = true;
            } else if (opciones.isHovered(input.getMouseX(), input.getMouseY())) {

            } else if (salir.isHovered(input.getMouseX(), input.getMouseY())) {
                VentanaPrincipal.exit(container);
            }
            raceTutorial.checkIfItsClicked(input);
            if (raceTutorial.checkOptionSelected(input.getMouseX(), input.getMouseY()) != null) {
                tutorialImage  = new Image("media/" + raceTutorial.opcion_seleccionada + ".png");
            }
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.setBackground(new Color(0.3f, 0.3f, 0.1f, 0f));
        g.drawString(titulo, (VentanaCombate.VIEWPORT_SIZE_WIDTH - (titulo.length() * 10)) / 2, 0);
        combate.render(g);
        tutorial.render(g);
        /*multijugador.dibujar(g);
        opciones.dibujar(g);*/
        salir.render(g);
        volver.render(g);
        aceptar.render(g);
        g.drawString("Usuario: ", 20, 50);
        usuario.render(container, g);
        if (tutorialMode) {
            tutorialImage.draw(350, 450);
            g.drawString(Partida.anadir_saltos_de_linea(RaceEnum.raceEnumMap.get(raceTutorial.opcion_seleccionada).getDescription(), 500), 600, 500);
            raceTutorial.render(g);
        }
    }

}
