package everlastingconflict.windows;

import everlastingconflict.elementosvisuales.BotonSimple;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class WindowMenuChangelog extends WindowMenuBasic {

    public BotonSimple volver, salir, anterior, siguiente;
    public List<String> changelogList;
    public int currentIndex;
    public final int pageSize = 4;
    public final String divider =
            "-----------------------------------------------------------------------------------------------------------------------------------------\n";

    @Override
    public void init(GameContainer container) throws SlickException {
        super.init(container);
        volver = new BotonSimple("Volver", WindowCombat.middleScreenX("Volver"), WindowCombat.responsiveY(95));
        anterior = new BotonSimple("Anterior", WindowCombat.responsiveX(40), WindowCombat.responsiveY(90));
        siguiente = new BotonSimple("Siguiente", WindowCombat.responsiveX(50), WindowCombat.responsiveY(90));
        salir = new BotonSimple("Salir", WindowCombat.VIEWPORT_SIZE_WIDTH - "Salir".length() * 10, 0);
        changelogList = new ArrayList();
        changelogList.add("0.7\n\n- Sonidos de movimiento de unidades\n" +
                "- Diferente miniatura por raza\n" +
                "- No mostrar barra de vida salvo que estén dañados o seleccionados\n");
        changelogList.add("0.6\n\n- Versión inicial de la Sexta Raza: Alianza Estelar!\n" +
                "- Implementado desembarco de tropas de la Alianza Estelar y sus dos primeras unidades\n" +
                "- Mejorada gestión de las unidades cuerpo a cuerpo para no disparar proyectiles\n" +
                "- Mejorada gestión de animaciones de edificios\n");
        changelogList.add("0.5.2\n\n- Mejoras visuales en la interfaz: Cambiado color de fondo, Navegacion entre pantallas con ESC y Enter\n" +
                "Mejorada ventana de changelog para permitir paginar entre las versiones con anterior y siguiente\n" +
                "Añadido subtitulo a las pantallas de Combate / Tutorial / Desafios\n");
        changelogList.add("0.5.1\n\n- Mejoras visuales : Iconos de estados, Feedback de construccion, meditacion y " +
                "recoleccion\n- Mejoras de selección de elementos en la UI\n- Bajada la dificultad de las IAs fáciles" +
                " añadiendo un delay de 10 segundos en las acciones\n- Música de fondo\n");
        changelogList.add("0.5\n\n- Modo de combate juggernaut : Todos los jugadores aliados contra el juggernaut que" +
                " tiene una " +
                "ventaja en función del número de enemigos\n- Corregido bug por el que no sonaba la destrucción de " +
                "edificios\n- Modo de juego nuevo : Desafíos, partidas cortas en las que la habilidad permite al " +
                "jugador " +
                "ganar frente a una desventaja numérica\n- Implementado un desafío básico por cada raza\n");
        changelogList.add("0.4\n\n- Permitir no mostrar punto de reunión en edificios que no generan unidades\n- " +
                "Corregido bug por el que empezando varias partidas añadía jugadores adicionales\n- Ventana de pausa " +
                "en combate al pulsar ESC\n- Permitir salir del menú pulsando ESC\n- Corregido error por el que el " +
                "equipo principal nunca ganaba la partida\n- Subfacciones Clark Implementadas : Despedazadores, " +
                "Regurgitadores, Rumiantes\n- Subfacciones Maestros Implementadas : Invocador, Hechicero, Luchador\n-" +
                " Subfacciones Guardianes Implementadas : Policía, Iglesia, Ejército\n- Mejoras visuales varias\n");
        changelogList.add("0.3\n\n- Subfaccion Fenix Restante: Cuervo\n- Implementación de Combos de selección de " +
                "subrazas para todos los jugadores\n- Comboboxes refactorizados para mostrar descripcion de los " +
                "elementos sobre los que se " +
                "encuentra el cursor\n- Subfacciones Eternium implementadas\n- Resto de subfacciones nombradas y con " +
                "icono\n");
        changelogList.add("0.2\n\n- Ventana Changelog\n- Mejoras visuales en botones de atributos de Manipulador\n- " +
                "Iconos de razas en ComboBoxes de selección de Raza\n- Primer desarrollo de subfacciones: " +
                "Subfacciones Oso y Tortuga\n");
        changelogList.add("0.1\n\n- Versión inicial\n- 5 razas funcionando: Fenix, Eternium, Clark, Guardianes, " +
                "Maestros\n- IA Eternium y Fenix\n- Tutorial para todas las razas\n- Modos multijugador: Aniquilacion" +
                " y Jugador Lider\n");
        currentIndex = 0;
        enablePageButtons();
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
                WindowMain.windowSwitch(container, "Menu");
            } else if (anterior.isHovered(input.getMouseX(), input.getMouseY())) {
                currentIndex -= pageSize;
                enablePageButtons();
            } else if (siguiente.isHovered(input.getMouseX(), input.getMouseY())) {
                currentIndex += pageSize;
                enablePageButtons();
            }
        }
    }

    public void enablePageButtons() {
        anterior.canBeUsed = currentIndex > 0;
        siguiente.canBeUsed = changelogList.size() > (currentIndex + pageSize);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        super.render(container, g);
        renderChangelog(g);
        anterior.render(g);
        siguiente.render(g);
        salir.render(g);
        volver.render(g);
    }

    private void renderChangelog(Graphics g) {
        g.drawString(divider, WindowCombat.responsiveX(5), WindowCombat.responsiveY(8));
        float textY = 10;
        float lastIndex = changelogList.size() >= (currentIndex + pageSize) ? (currentIndex + pageSize) :
                changelogList.size();
        for (int i = currentIndex; i < lastIndex; i++) {
            String version = changelogList.get(i);
            long numberOfLines = version.chars().filter(ch -> ch == '\n').count();
            g.drawString(changelogList.get(i), WindowCombat.responsiveX(5), WindowCombat.responsiveY(textY));
            g.drawString(divider, WindowCombat.responsiveX(5),
                    WindowCombat.responsiveY(textY + numberOfLines * 2));
            textY += numberOfLines * 2.5;
        }
    }
}
