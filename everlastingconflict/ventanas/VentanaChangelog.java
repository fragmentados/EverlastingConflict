package everlastingconflict.ventanas;

import everlastingconflict.elementosvisuales.BotonSimple;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class VentanaChangelog extends Ventana {

    public BotonSimple volver, salir;
    public String changelog;

    @Override
    public void init(GameContainer container) throws SlickException {
        volver = new BotonSimple("Volver", VentanaCombate.responsiveX(40), VentanaCombate.responsiveY(85));
        salir = new BotonSimple("Salir", VentanaCombate.VIEWPORT_SIZE_WIDTH - 60, 0);
        changelog = "0.2\n\n";
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
        Input input = container.getInput();
        //Boton izquierdo
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            if (salir.isHovered(input.getMouseX(), input.getMouseY())) {
                VentanaPrincipal.exit(container);
            } else if (volver.isHovered(input.getMouseX(), input.getMouseY())) {
                VentanaPrincipal.windowSwitch(container, null, "Menu");
            }
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.drawString(changelog, VentanaCombate.responsiveX(5), VentanaCombate.responsiveY(5));
        salir.render(g);
        volver.render(g);
    }
}
