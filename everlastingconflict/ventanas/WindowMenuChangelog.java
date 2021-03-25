package everlastingconflict.ventanas;

import everlastingconflict.elementosvisuales.BotonSimple;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class WindowMenuChangelog extends WindowMenuBasic {

    public BotonSimple volver, salir;
    public String changelog;

    @Override
    public void init(GameContainer container) throws SlickException {
        volver = new BotonSimple("Volver", WindowCombat.responsiveX(40), WindowCombat.responsiveY(85));
        salir = new BotonSimple("Salir", WindowCombat.VIEWPORT_SIZE_WIDTH - 60, 0);
        changelog = "0.5\n\n";
        changelog += "- Modo de combate juggernaut : Todos los jugadores aliados contra otro que tiene una ventaja en función del número de enemigos\n";
        changelog += "- Corregido bug por el que no sonaba la destruccion de edificios\n";
        changelog += "- Modo de juego nuevo : Desafíos, partidas cortas en las que la habilidad permite al jugador ganar frente a una desventaja\n";
        changelog += "--------------------------------------------------------------\n";
        changelog += "0.4\n\n";
        changelog += "- Permitir no mostrar punto de reunión en edificios que no generan unidades\n";
        changelog += "- Corregido bug por el que empezando varias partidas añadía jugadores adicionales\n";
        changelog += "- Ventana de pausa en combate al pulsar ESC\n";
        changelog += "- Permitir salir del menú pulsando ESC\n";
        changelog += "- Corregido error por el que el equipo principal nunca ganaba la partida\n";
        changelog += "- Subfacciones Clark Implementadas : Despedazadores, Regurgitadores, Rumiantes\n";
        changelog += "- Subfacciones Maestros Implementadas : Invocador, Hechicero, Luchador\n";
        changelog += "- Subfacciones Guardianes Implementadas : Policía, Iglesia, Ejército\n";
        changelog += "- Mejoras visuales varias\n";
        changelog += "--------------------------------------------------------------\n";
        changelog += "0.3\n\n";
        changelog += "- Subfaccion Fenix Restante: Cuervo\n";
        changelog += "- Implementación de Combos de selección de subrazas para todos los jugadores\n";
        changelog += "- Comboboxes refactorizados para mostrar descripcion de los elementos sobre los que se encuentra el cursor\n";
        changelog += "- Subfacciones Eternium implementadas\n";
        changelog += "- Resto de subfacciones nombradas y con icono\n";
        changelog += "--------------------------------------------------------------\n";
        changelog += "0.2\n\n";
        changelog += "- Ventana Changelog\n";
        changelog += "- Mejoras visuales en botones de atributos de Manipulador\n";
        changelog += "- Iconos de razas en ComboBoxes de selección de Raza\n";
        changelog += "- Primer desarrollo de subfacciones: Subfacciones Oso y Tortuga\n";
        changelog += "--------------------------------------------------------------\n";
        changelog += "0.1\n\n";
        changelog += "- Versión inicial\n";
        changelog += "- 5 razas funcionando: Fenix, Eternium, Clark, Guardianes, Maestros\n";
        changelog += "- IA Eternium y Fenix\n";
        changelog += "- Tutorial para todas las razas\n";
        changelog += "- Modos multijugador: Aniquilacion y Jugador Lider\n";
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        super.update(container, delta);
        Input input = container.getInput();
        //Boton izquierdo
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            if (salir.isHovered(input.getMouseX(), input.getMouseY())) {
                WindowMain.exit(container);
            } else if (volver.isHovered(input.getMouseX(), input.getMouseY())) {
                WindowMain.windowSwitch(container, null, "Menu");
            }
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        super.render(container, g);
        g.drawString(changelog, WindowCombat.responsiveX(5), WindowCombat.responsiveY(10));
        salir.render(g);
        volver.render(g);
    }
}
