package everlastingconflict.windows;

import everlastingconflict.elementosvisuales.BotonSimple;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class WindowMenuStory extends WindowMenuBasic {

    public BotonSimple volver, salir, anterior, siguiente;
    public List<String> storyList;
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
        storyList = new ArrayList();
        storyList.add("0.7\n\n- Sonidos de movimiento de unidades\n" +
                "- Diferente miniatura por raza\n" +
                "- No mostrar barra de vida salvo que estén dañados o seleciicionados\n");
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
        siguiente.canBeUsed = storyList.size() > (currentIndex + pageSize);
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
        float lastIndex = storyList.size() >= (currentIndex + pageSize) ? (currentIndex + pageSize) :
                storyList.size();
        for (int i = currentIndex; i < lastIndex; i++) {
            String version = storyList.get(i);
            long numberOfLines = version.chars().filter(ch -> ch == '\n').count();
            g.drawString(storyList.get(i), WindowCombat.responsiveX(5), WindowCombat.responsiveY(textY));
            g.drawString(divider, WindowCombat.responsiveX(5),
                    WindowCombat.responsiveY(textY + numberOfLines * 2));
            textY += numberOfLines * 2.5;
        }
    }
}
